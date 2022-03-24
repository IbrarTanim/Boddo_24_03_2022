package net.boddo.btm.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import net.boddo.btm.Adepter.ProfileImageLoaderAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPhotoActivity extends AppCompatActivity implements Constants, EasyPermissions.PermissionCallbacks, View.OnLongClickListener, View.OnClickListener {

    AddPhotoActivity activity;

    @BindView(R.id.asecond_image_delete)
    ImageView asecondImageDelete;
    @BindView(R.id.asecond_image_plus)
    ImageView asecondImagePlus;
    @BindView(R.id.athird_image_delete)
    ImageView athirdImageDelete;
    @BindView(R.id.athird_image_plus)
    ImageView athirdImagePlus;
    @BindView(R.id.afourth_image_pluse)
    ImageView afourthImagePluse;
    @BindView(R.id.afourth_image_delete)
    ImageView afourthImageDelete;
    @BindView(R.id.afifth_image_delete)
    ImageView afifthImageDelete;
    @BindView(R.id.afifth_image_plus)
    ImageView afifthImagePlus;
    @BindView(R.id.asixth_image_plus)
    ImageView asixthImagePlus;
    @BindView(R.id.asixth_image_delete)
    ImageView asixthImageDelete;
    @BindView(R.id.aseventh_image_pluse)
    ImageView aseventhImagePluse;
    @BindView(R.id.aseventh_image_delete)
    ImageView aseventhImageDelete;
    @BindView(R.id.aeighth_image_delete)
    ImageView aeighthImageDelete;
    @BindView(R.id.aeighth_image_plus)
    ImageView aeighthImagePlus;
    @BindView(R.id.anineth_image_pluse)
    ImageView aninethImagePluse;
    @BindView(R.id.anineth_image_delete)
    ImageView aninethImageDelete;
    @BindView(R.id.FooterText)
    TextView FooterText;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddPhotoActivity.class);
        return intent;
    }

    ImageView profileImageView1, profileImageView2, profileImageView3, profileImageView4, profileImageView5, profileImageView6, profileImageView7, profileImageView8, profileImageView9;
    private static final int PICK_IMAGE = 100;
    ApiInterface apiInterface;
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    Bitmap bitmap;
    String image = "";
    String imageName = "";
    int serial = 0;
    @BindView(R.id.profile_image1)
    ImageView profileImageOne;

    TextView tvBackAddPhotoLow;

    @BindView(R.id.profile_image2)
    ImageView profileImageTwo;
    @BindView(R.id.profile_image3)
    ImageView profileImageThree;
    @BindView(R.id.profile_image4)
    ImageView profileImageFour;
    @BindView(R.id.profile_image5)
    ImageView profileImageFive;
    @BindView(R.id.profile_image6)
    ImageView profileImageSix;
    @BindView(R.id.profile_image7)
    ImageView profileImageSeven;
    @BindView(R.id.profile_image8)
    ImageView profileImageEight;
    @BindView(R.id.profile_image9)
    ImageView profileImageNine;

    @BindView(R.id.first_image_delete)
    ImageView firstImageDelete;

    /*@BindView(R.id.second_image_delete)
    ImageView secondImageDelete;
    @BindView(R.id.third_image_delete)
    ImageView thirdImageDelete;
    @BindView(R.id.fourth_image_delete)
    ImageView fourthImageDelete;

    @BindView(R.id.fifth_image_delete)
    ImageView fifthImageDelete;
    @BindView(R.id.sixth_image_delete)
    ImageView sixthImageDelete;

    @BindView(R.id.seventh_image_delete)
    ImageView seventhImageDelete;
    @BindView(R.id.eighth_image_delete)
    ImageView eighthImageDelete;
    @BindView(R.id.nineth_image_delete)
    ImageView ninethImageDelete;*/

    ArrayList<ImageView> imageViews;


    public static final String TAG = AddPhotoActivity.class.getSimpleName();
    ProfileImageLoaderAdapter adapter;
    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_low_sdk);

        activity = this;
      /*  int sdk = Build.VERSION.SDK_INT;

        if (sdk >= 23) {
            setContentView(R.layout.activity_add_photo);
        } else {
            setContentView(R.layout.activity_add_photo_low_sdk);
        }
*/
        ButterKnife.bind(this);
       /* ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }*/

        asecondImagePlus.setVisibility(View.VISIBLE);
        athirdImagePlus.setVisibility(View.VISIBLE);
        afourthImagePluse.setVisibility(View.VISIBLE);
        afifthImagePlus.setVisibility(View.VISIBLE);
        asixthImagePlus.setVisibility(View.VISIBLE);
        aseventhImagePluse.setVisibility(View.VISIBLE);
        aeighthImagePlus.setVisibility(View.VISIBLE);
        aninethImagePluse.setVisibility(View.VISIBLE);

        tvBackAddPhotoLow = findViewById(R.id.tvBackAddPhotoLow);
        tvBackAddPhotoLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileOneActivity.class);
                startActivity(intent);
                finish();
            }
        });

        adapter = new ProfileImageLoaderAdapter(this);
        initAllImageViews();
        setUpImages();



    }

    public void swipeProfileImage(int serial) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ProfileImageLoader> call = apiInterface.profileImageSwipe(Constants.SECRET_KEY, Data.userId, serial);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                ProfileImageLoader imageLoader = response.body();
                if (imageLoader.getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    ProfileFragment.imageList = photos.getProfileImageInfo();
                    recreate();
                } else {
                    Toast.makeText(AddPhotoActivity.this, "Image uploading " + imageLoader.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                Log.d(TAG, "Error:" + t.getMessage());
            }
        });
    }

    @OnClick(R.id.first_image_delete)
    public void onFirstImageDelete() {
        serial = 1;
        if (firstImageDelete.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.cancel).getConstantState())) {
            alertDialogForDeletingProfilePhoto(serial);
        } else if (firstImageDelete.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_plus).getConstantState())) {
            cropProfileImage();
        }
    }


    private void deletePhoto(int serial) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ProfileImageLoader> call = apiInterface.deletePhoto(Constants.SECRET_KEY, Data.userId, serial);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                ProfileImageLoader obj = response.body();
                if (obj.getStatus().equals("success")) {
                    ProfileFragment.imageList = obj.getProfileImageInfo();
                    Toast.makeText(AddPhotoActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    Toast.makeText(AddPhotoActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                Log.d("AddPhotoError", t.getMessage());
            }
        });
    }

    public void alertDialogForDeletingProfilePhoto(final int serial) {
        final AlertDialog.Builder alertadd = new AlertDialog.Builder(AddPhotoActivity.this);
        alertadd.setMessage("Are you sure you want to delete this your profile picture?");
        alertadd.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        deletePhoto(serial);
                    }
                });
        alertadd.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertadd.show();
    }

    private void setUpImages() {
        for (int i = 0; i < ProfileFragment.imageList.size(); i++) {
            if (ProfileFragment.imageList.get(i).getSerial().equals("1")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageOne);
                firstImageDelete.setVisibility(View.VISIBLE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("2")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageTwo);
                //secondImageDelete.setImageResource(R.drawable.cancel);
                asecondImageDelete.setVisibility(View.VISIBLE);
                asecondImagePlus.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("3")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageThree);
                //thirdImageDelete.setImageResource(R.drawable.cancel);
                athirdImageDelete.setVisibility(View.VISIBLE);
                athirdImagePlus.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("4")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageFour);
                //fourthImageDelete.setImageResource(R.drawable.cancel);
                afourthImageDelete.setVisibility(View.VISIBLE);
                afifthImagePlus.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("5")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageFive);
                //fifthImageDelete.setImageResource(R.drawable.cancel);
                afifthImageDelete.setVisibility(View.VISIBLE);
                afifthImagePlus.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("6")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageSix);
                //sixthImageDelete.setImageResource(R.drawable.cancel);
                asixthImageDelete.setVisibility(View.VISIBLE);
                asixthImagePlus.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("7")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageSeven);
                //seventhImageDelete.setImageResource(R.drawable.cancel);
                aseventhImageDelete.setVisibility(View.VISIBLE);
                aseventhImagePluse.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("8")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageEight);
                //eighthImageDelete.setImageResource(R.drawable.cancel);
                aeighthImageDelete.setVisibility(View.VISIBLE);
                aeighthImagePlus.setVisibility(View.GONE);
            }
            if (ProfileFragment.imageList.get(i).getSerial().equals("9")) {
                Picasso.get()
                        .load(ProfileFragment.imageList.get(i).getPhoto())
                        .into(profileImageNine);
                //ninethImageDelete.setImageResource(R.drawable.cancel);
                aninethImageDelete.setVisibility(View.VISIBLE);
                aninethImagePluse.setVisibility(View.GONE);
            }
        }
    }

    private void initAllImageViews() {
        imageViews = new ArrayList<ImageView>();
        imageViews.add((ImageView) profileImageOne);
        profileImageOne.setOnLongClickListener(this);
        profileImageOne.setOnClickListener(this);
        if (Data.userGender.equals("Male")) {
            profileImageOne.setImageDrawable(getResources().getDrawable(R.drawable.bg2));
        } else if (Data.userGender.equals("Female")) {
            profileImageOne.setImageDrawable(getResources().getDrawable(R.drawable.unkown_face_female));
        }
        imageViews.add((ImageView) profileImageTwo);
        profileImageTwo.setOnLongClickListener(this);
        profileImageTwo.setOnClickListener(this);

        imageViews.add((ImageView) profileImageThree);
        profileImageThree.setOnLongClickListener(this);
        profileImageThree.setOnClickListener(this);

        imageViews.add((ImageView) profileImageFour);
        profileImageFour.setOnLongClickListener(this);
        profileImageFour.setOnClickListener(this);

        imageViews.add((ImageView) profileImageFive);
        profileImageFive.setOnLongClickListener(this);
        profileImageFive.setOnClickListener(this);

        imageViews.add((ImageView) profileImageSix);
        profileImageSix.setOnLongClickListener(this);
        profileImageSix.setOnClickListener(this);

        imageViews.add((ImageView) profileImageSeven);
        profileImageSeven.setOnLongClickListener(this);
        profileImageSeven.setOnClickListener(this);

        imageViews.add((ImageView) profileImageEight);
        profileImageEight.setOnLongClickListener(this);
        profileImageEight.setOnClickListener(this);

        imageViews.add((ImageView) profileImageNine);
        profileImageNine.setOnLongClickListener(this);
        profileImageNine.setOnClickListener(this);
    }

    public void cropProfileImage() {
        CropImage.activity()
                .setFixAspectRatio(true)
                .setAspectRatio(2, 3)
                .setAllowRotation(false)
                .setOutputCompressQuality(100)
                .start(AddPhotoActivity.this);


                /*.setMinCropResultSize(720, 720)
                .setRequestedSize(720, 720)*/
    }


     /*public void cropProfileImage() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .setAllowRotation(true)
                .setMinCropResultSize(720, 720)
                .setRequestedSize(720, 720)
                .setOutputCompressQuality(100)
                .start(AddPhotoActivity.this);
    }*/







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 301) {
            //skip
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, AddPhotoActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                uploadImageToServer(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void uploadImageToServer(final Uri resultUri) {

        String filePath = getRealPathFromURIPath(resultUri, AddPhotoActivity.this);
        imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
        try {
            bitmap = new Compressor(AddPhotoActivity.this).compressToBitmap(new File(resultUri.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            image = imageToString();

            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ProfileImageLoader> fileUpload = apiInterface.uploadFile(image, imageName, Constants.SECRET_KEY, Data.userId, serial);
            fileUpload.enqueue(new Callback<ProfileImageLoader>() {
                @Override
                public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                    ProfileImageLoader imageLoader = response.body();
                    if (imageLoader.getStatus().equals("success")) {
                        ProfileImageLoader photos = response.body();
                        ProfileFragment.imageList = photos.getProfileImageInfo();
                        recreate();
                    } else {
                        Toast.makeText(AddPhotoActivity.this, "Image uploading " + imageLoader.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                    Toast.makeText(AddPhotoActivity.this, "Something wrong,Please check your network connection", Toast.LENGTH_SHORT).show();
                    Log.d(AddPhotoActivity.class.getSimpleName(), "Error " + t.getMessage());
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
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
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (resultUri != null) {
            uploadImageToServer(resultUri);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showAlertDialogForMakingProfilePicture(final int serial) {
        final AlertDialog.Builder alertadd = new AlertDialog.Builder(AddPhotoActivity.this);
        alertadd.setMessage("Are you sure you want to make this your profile picture?");
        alertadd.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        swipeProfileImage(serial);
                    }
                });
        alertadd.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertadd.show();
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.profile_image1:
                if (profileImageOne.getDrawable() != null) {
                    Toast.makeText(this, "This is already your profile image", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image2:
                if (profileImageTwo.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(2);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image3:
                if (profileImageThree.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(3);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.profile_image4:
                if (profileImageFour.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(4);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.profile_image5:
                if (profileImageFive.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(5);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.profile_image6:
                if (profileImageSix.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(6);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.profile_image7:
                if (profileImageSeven.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(7);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.profile_image8:
                if (profileImageEight.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(8);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.profile_image9:
                if (profileImageNine.getDrawable() != null) {
                    showAlertDialogForMakingProfilePicture(9);
                } else {
                    Toast.makeText(this, "Please Upload an image first", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.profile_image1:
                serial = 1;
                cropProfileImage();
                break;
            case R.id.profile_image2:
                serial = 2;
                cropProfileImage();
                break;
            case R.id.profile_image3:
                serial = 3;
                cropProfileImage();
                break;
            case R.id.profile_image4:
                serial = 4;
                cropProfileImage();
                break;
            case R.id.profile_image5:
                serial = 5;
                cropProfileImage();
                break;
            case R.id.profile_image6:
                serial = 6;
                cropProfileImage();
                break;
            case R.id.profile_image7:
                serial = 7;
                cropProfileImage();
                break;
            case R.id.profile_image8:
                serial = 8;
                cropProfileImage();
                break;
            case R.id.profile_image9:
                serial = 9;
                cropProfileImage();
                break;
        }
    }

    @OnClick({R.id.asecond_image_delete, R.id.asecond_image_plus, R.id.athird_image_delete, R.id.athird_image_plus, R.id.afourth_image_pluse, R.id.afourth_image_delete, R.id.afifth_image_delete, R.id.afifth_image_plus, R.id.asixth_image_plus, R.id.asixth_image_delete, R.id.aseventh_image_pluse, R.id.aseventh_image_delete, R.id.aeighth_image_delete, R.id.aeighth_image_plus, R.id.anineth_image_pluse, R.id.anineth_image_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.asecond_image_delete:
                serial = 2;
                alertDialogForDeletingProfilePhoto(serial);
                break;

            case R.id.asecond_image_plus:
                serial = 2;
                cropProfileImage();
                break;

            case R.id.athird_image_delete:
                serial = 3;
                alertDialogForDeletingProfilePhoto(serial);
                break;
            case R.id.athird_image_plus:
                serial = 3;
                cropProfileImage();
                break;
            case R.id.afourth_image_pluse:
                serial = 4;
                cropProfileImage();
                break;
            case R.id.afourth_image_delete:
                serial = 4;
                alertDialogForDeletingProfilePhoto(serial);
                break;
            case R.id.afifth_image_delete:
                serial = 5;
                alertDialogForDeletingProfilePhoto(serial);
                break;
            case R.id.afifth_image_plus:
                serial = 5;
                cropProfileImage();
                break;
            case R.id.asixth_image_plus:
                serial = 6;
                cropProfileImage();
                break;
            case R.id.asixth_image_delete:
                serial = 6;
                alertDialogForDeletingProfilePhoto(serial);
                break;
            case R.id.aseventh_image_pluse:
                serial = 7;
                cropProfileImage();
                break;
            case R.id.aseventh_image_delete:
                serial = 7;
                alertDialogForDeletingProfilePhoto(serial);
                break;
            case R.id.aeighth_image_delete:
                serial = 8;
                alertDialogForDeletingProfilePhoto(serial);
                break;
            case R.id.aeighth_image_plus:
                serial = 8;
                cropProfileImage();
                break;
            case R.id.anineth_image_pluse:
                serial = 9;
                cropProfileImage();
                break;
            case R.id.anineth_image_delete:
                serial = 9;
                alertDialogForDeletingProfilePhoto(serial);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        checkSelfPermission(permission, 301);

    }

    private void checkSelfPermission(String[] permissions, int requestCode) {

        if (ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_DENIED &&
                ContextCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_DENIED &&
                ContextCompat.checkSelfPermission(activity, permissions[2]) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(activity, permissions, requestCode);

        }

    }
}
