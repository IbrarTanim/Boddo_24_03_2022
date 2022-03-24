package net.boddo.btm.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureUploadActivity extends AppCompatActivity {


    private static final String TAG = "PictureUploadActivity";
    @BindView(R.id.add_button)
    ImageView selectImage;

    @BindView(R.id.back_arrow)
    ImageView backButton;
    @BindView(R.id.uploadable_image_view)
    ImageView selectedImage;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.btn_post)
    Button btnPost;
    @BindView(R.id.btn_public)
    Button btnPublic;
    @BindView(R.id.btn_cancel)
    Button btnCancel;


    @OnClick(R.id.btn_cancel)
    void setBtnCancel() {
        finish();
    }

    private String imageName = "";
    private String image;
    private Bitmap bitmap;
    private Uri resultUri;
    private final int PHOTO_BLOG_IMAGE_REQUEST_CODE = 101;
    private final int REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_upload);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                //File write logic here
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
        isStoragePermissionGranted();
        btnPost.setEnabled(false);
    }

    @OnClick(R.id.add_button)
    void selectImage() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.onPhotoLimit(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("limit expired")) {
                    if (Data.isPalupPlusSubcriber) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            PrettyDialog jj = new PrettyDialog(PictureUploadActivity.this);
                            jj.setTitle("Dear " + Data.userName)
                                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                                    .setMessage("You can Upload 20 photos in a day.").setMessageColor(R.color.red_A700);
                            jj.show();
                        } else {
                            PrettyDialog jj = new PrettyDialog(PictureUploadActivity.this);
                            jj.setTitle("Dear " + Data.userName)

                                    .setMessage("You can Upload 20 photos in a day.").setMessageColor(R.color.red_A700);
                            jj.show();
                        }
                    } else {
                        final PrettyDialog pretty = new PrettyDialog(PictureUploadActivity.this);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            pretty.setTitle("Dear " + Data.userName)
                                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                                    .setMessage("You can Upload 5 photos in a day.Please buy palup subscription for upload more photos.").setMessageColor(R.color.red_A700)
                                    .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            pretty.dismiss();
                                        }
                                    });

                            pretty.show();
                        } else {
                            pretty.setTitle("Dear " + Data.userName)
                                    .setMessage("You can Upload 5 photos in a day.Please buy palup subscription for upload more photos.").setMessageColor(R.color.red_A700)
                                    .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            pretty.dismiss();
                                        }
                                    });
                            pretty.show();
                        }
                    }

                } else {
                    selectedImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isStoragePermissionGranted()) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, PHOTO_BLOG_IMAGE_REQUEST_CODE);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(PictureUploadActivity.class.getSimpleName(), t.getMessage());
            }
        });

    }

    @OnClick(R.id.btn_post)
    void onPost() {
        if (selectedImage.getDrawable() != null) {
            image = imageToString();
            String caption = description.getText().toString();
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<UserPhotoBlogImages[]> call = apiInterface.uploadPhotoBlog(Constants.SECRET_KEY, image, imageName, Data.userId, !caption.equals("") ? caption : "");
            call.enqueue(new Callback<UserPhotoBlogImages[]>() {
                @Override
                public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {
                    if (response.code() == 200) {
                        Toast.makeText(PictureUploadActivity.this, "Uploaded successful.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                    Log.d("PictureUploadActivity", t.getMessage());
                    Toast.makeText(PictureUploadActivity.this, "Someting went wrong plese try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            final PrettyDialog prettyDialog = new PrettyDialog(PictureUploadActivity.this);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                prettyDialog.setTitle("Warning").setMessage("Please select an image !!!")
                        .setIcon(R.drawable.logo1).setIcon(R.color.colorPrimary)
                        .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        });

                prettyDialog.show();
            } else {
                prettyDialog.setTitle("Warning").setMessage("Please select an image !!!")
                        .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                prettyDialog.dismiss();
                            }
                        });
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED || data != null) {
            if (requestCode == PHOTO_BLOG_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    resultUri = data.getData();
//                    selectedImage.setImageURI(resultUri);
//                    selectedImage.setBackground(null);
                    setImageBitmap(resultUri);
                    btnPost.setEnabled(true);

                }
            }
        }
    }

    public void setImageBitmap(final Uri resultUri) {
        String filePath = getRealPathFromURIPath(resultUri, this);
        imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
        try {
            bitmap = new Compressor(PictureUploadActivity.this).compressToBitmap(new File(resultUri.getPath()));
            selectedImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        } else {
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
        }
    }

}
