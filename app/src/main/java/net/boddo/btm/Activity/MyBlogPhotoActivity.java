package net.boddo.btm.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.theartofdev.edmodo.cropper.CropImage;

import net.boddo.btm.Adepter.UserPhotoBlogAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBlogPhotoActivity extends AppCompatActivity {
    MyBlogPhotoActivity activity;
    Uri resultUri;
    Bitmap bitmap;
    String image, imageName;
    String comeFrom;
    ApiInterface apiInterface;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    private int TODAY_UPLOAD_COUNT = 0;

   /* @BindView(R.id.add_image_button)
    FloatingActionButton addImage;*/

    String description = "";
    ImageView imageView;
    EditText editTextDescription;

    boolean wantToCloseDialog;
    UserPhotoBlogAdapter adapter;
    UserPhotoBlogImages[] userPhotoBlogImages;

    TextView tvBackMyBlogPhoto;
    ImageView ivCameraMyBlogPhoto;

    StaggeredGridLayoutManager layoutManager;

    @BindView(R.id.user_photo_blog_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blog_photo);
        activity = this;
        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        //getAllImageList();

        ivCameraMyBlogPhoto = findViewById(R.id.ivCameraMyBlogPhoto);
        tvBackMyBlogPhoto = findViewById(R.id.tvBackMyBlogPhoto);
        tvBackMyBlogPhoto.setText(Data.userFirstName + "'s" + " " + "pictures");

        tvBackMyBlogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileOneActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ivCameraMyBlogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<String> postCall = apiInterface.onPhotoLimit(Constants.SECRET_KEY, Data.userId);

                postCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.body() != null) {

                            Log.e("Photo Blog Limit", response.body());

                            if (response.body().equals("success")) {

                                Intent intent = new Intent(activity, ImageUploadActivity.class);
                                startActivity(intent);

                            } else if (response.body().equals("limit expired")) {

                                limitExpiredDialog();
                                //Toast.makeText(activity, "You have reached your limit. Please subscribe Boddo Plus to upload more.", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });


                /*if (!Data.isPalupPlusSubcriber && TODAY_UPLOAD_COUNT >= 3) {
                    Toast.makeText(activity, "You have reached your limit.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(activity, ImageUploadActivity.class);
                    startActivity(intent);
                }*/

            }
        });


    }


    private void getAllImageList() {
        /*Data.pd = new ProgressDialog(this);
        Data.pd.show();*/
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserPhotoBlogImages[]> call = apiInterface.userAllImagePhotoBlog(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<UserPhotoBlogImages[]>() {
            @Override
            public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {
                userPhotoBlogImages = response.body();

                /*Calendar calendar = Calendar.getInstance(Locale.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = formatter.format(calendar.getTime());
                Log.e("Current Date: ", currentDate);

                for (UserPhotoBlogImages userPhotoBlogImage1 : userPhotoBlogImages) {
                    Log.e("Gallery", userPhotoBlogImage1.toString());

                    String photoDate = userPhotoBlogImage1.getCreatedAt();
                    try {

                        Log.e("Photo Date: ", photoDate);

                        if (photoDate.contains(currentDate)) {

                            TODAY_UPLOAD_COUNT++;

                            Log.e("Photo Date: ", "Contains");


                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        Log.e("Photo Date Error: ", e.getMessage());
                    }
                }*/

                //recyclerView.setLayoutManager(new GridLayoutManager(MyBlogPhotoActivity.this, 2));

                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new UserPhotoBlogAdapter(MyBlogPhotoActivity.this, userPhotoBlogImages);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                Log.d("MyBlogPhotoActivty", t.getMessage());
            }
        });

    }


    /*private void getAllImageListOtherProfile() {
        Data.pd = new ProgressDialog(this);
        Data.pd.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserPhotoBlogImages[]> call = apiInterface.otherUserAllImagePhotoBlog(Constants.SECRET_KEY, Data.otherUserId);
        call.enqueue(new Callback<UserPhotoBlogImages[]>() {
            @Override
            public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {
                uploadedAllImages = response.body();
                adapter = new UserPhotoBlogAdapter(getContext(), uploadedAllImages);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                Log.d("PhotoFragment", t.getMessage());
            }
        });
        Data.pd.dismiss();
    }*/





    public void addNewPhoto() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.onPhotoLimit(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("limit expired")) {

                    if (Data.isPalupPlusSubcriber) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            PrettyDialog jj = new PrettyDialog(MyBlogPhotoActivity.this);
                            jj.setTitle("Dear " + Data.userName)
                                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                                    .setMessage("You can Upload 5 photos in a day.").setMessageColor(R.color.red_A700);
                            jj.show();
                        } else {
                            PrettyDialog jj = new PrettyDialog(MyBlogPhotoActivity.this);
                            jj.setTitle("Dear " + Data.userName)

                                    .setMessage("You can Upload 5 photos in a day.").setMessageColor(R.color.red_A700);
                            jj.show();
                        }

                    } else {
                        final PrettyDialog pretty = new PrettyDialog(MyBlogPhotoActivity.this);
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
                    wantToCloseDialog = false;
                    final Dialog alertadd = new Dialog(MyBlogPhotoActivity.this,
                            R.style.Theme_Dialog);
                    Objects.requireNonNull(alertadd.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater factory = LayoutInflater.from(MyBlogPhotoActivity.this);
                    final View view = factory.inflate(R.layout.user_upload_image_photoblog, null);
                    alertadd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    alertadd.setContentView(R.layout.user_upload_image_photoblog);
                    alertadd.setContentView(view);
                    Button cancelButton = view.findViewById(R.id.cancel_button);
                    final Button uploadButton = view.findViewById(R.id.upload_button);
                    imageView = view.findViewById(R.id.image_view_upload);
                    editTextDescription = view.findViewById(R.id.description);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (EasyPermissions.hasPermissions(MyBlogPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                CropImage.activity()
                                        .setAllowRotation(true)
                                        .setOutputCompressQuality(60)
                                        .start(MyBlogPhotoActivity.this);
                            } else {
                                EasyPermissions.requestPermissions(MyBlogPhotoActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        }
                    });
                    uploadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uploadButton.setEnabled(false);
                            description = editTextDescription.getText().toString();
                            if (imageView.getDrawable() != null) {
                                image = imageToString();
                                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<UserPhotoBlogImages[]> call = apiInterface.uploadPhotoBlog(Constants.SECRET_KEY, image, imageName, Data.userId, description);
                                call.enqueue(new Callback<UserPhotoBlogImages[]>() {
                                    @Override
                                    public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {

                                        userPhotoBlogImages = response.body();
                                        if (alertadd != null) {
                                            alertadd.cancel();
                                        }

                                        adapter = new UserPhotoBlogAdapter(MyBlogPhotoActivity.this, userPhotoBlogImages);
                                        recyclerView.setAdapter(adapter);
                                        Toast.makeText(MyBlogPhotoActivity.this, "Uploaded successful", Toast.LENGTH_SHORT).show();
                                        uploadButton.setEnabled(true);
                                    }

                                    @Override
                                    public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                                        Log.d("MyBlogPhotoActivity", t.getMessage());
                                        Toast.makeText(MyBlogPhotoActivity.this, "Someting went wrong plese try again", Toast.LENGTH_SHORT).show();
                                        if (alertadd != null) {
                                            alertadd.cancel();
                                        }
                                        uploadButton.setEnabled(true);
                                    }
                                });


                            } else {

                                final PrettyDialog prettyDialog = new PrettyDialog(MyBlogPhotoActivity.this);
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
                    });


                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertadd.dismiss();
                        }
                    });
                    alertadd.show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(MyBlogPhotoActivity.class.getSimpleName(), t.getMessage());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED || data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    imageView.setImageURI(resultUri);
                    imageView.setBackground(null);
                    uploadImageToServer(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }

    public void uploadImageToServer(final Uri resultUri) {
        String filePath = getRealPathFromURIPath(resultUri, this);
        imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
        try {
            bitmap = new Compressor(MyBlogPhotoActivity.this).compressToBitmap(new File(resultUri.getPath()));
            imageView.setImageBitmap(bitmap);
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

    private void limitExpiredDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.limit_expired_dialog);

        TextView titleText = dialog.findViewById(R.id.title);
        TextView infoText = dialog.findViewById(R.id.tvNormalInterfaceText);

        titleText.setText("Notification");
        infoText.setText("You have reached your daily picture share limit. For more picture share activate Boddo Plus.");

        Button button_decline = dialog.findViewById(R.id.button_decline);
        Button discoverBoddo = dialog.findViewById(R.id.discoverBoddo);
        discoverBoddo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPalupPlusWindow();
                dialog.dismiss();
            }
        });
        button_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void goToPalupPlusWindow() {
        Intent intent = new Intent(activity, BuyCreditActivity.class);
        intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
        intent.putExtra("Membership", true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllImageList();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(activity, ProfileOneActivity.class);
        startActivity(intent);
        finish();
    }
}
