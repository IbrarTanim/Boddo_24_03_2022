package net.boddo.btm.Activity;

import static net.boddo.btm.Utills.StaticAccess.REQUEST_CODE_TAKE_PICTURE;
import static net.boddo.btm.Utills.StaticAccess.SELECT_PICTURE;
import static net.boddo.btm.Utills.StaticAccess.TAG_UPLOADED_PHOTO;
import static net.boddo.btm.Utills.StaticAccess.TAG_UPLOADED_PHOTO_PATH;
import static net.boddo.btm.Utills.StaticAccess.TEMP_PHOTO_FILE_NAME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.boddo.btm.BuildConfig;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ImageProcessing;
import net.boddo.btm.Utills.InternalStorageContentProvider;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUploadActivity extends AppCompatActivity implements View.OnClickListener {
    ImageUploadActivity activity;
    TextView ibtnPicImage, ibtnCaptureImage;
    public String appImagePath = null;
    ImageProcessing imageProcessing;
    public int intent_source = 0;
    public File mFileTemp;
    Uri uriDataImage;
    public Bitmap imageBitMap;
    private Bitmap bitmapImage;
    String byteConvertedImage;
    String imgPath = "";
    SharedPreferences sharedpreferences;
    ImageButton ibBackImageUpload;
    CircleImageView civImageUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        activity = this;

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        sharedpreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        ibBackImageUpload = findViewById(R.id.ibBackImageUpload);
        ibtnPicImage = findViewById(R.id.ibtnPicImage);
        ibtnCaptureImage = findViewById(R.id.ibtnCaptureImage);
        civImageUpload = findViewById(R.id.civImageUpload);

        ibtnPicImage.setOnClickListener(this);
        ibtnCaptureImage.setOnClickListener(this);
        ibBackImageUpload.setOnClickListener(this);

        imageProcessing = new ImageProcessing(activity);
        appImagePath = imageProcessing.getImageDir();

        //Glide.with(getActivity()).load(Data.profilePhoto).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(userImageView);
        Glide.with(activity).load(Data.profilePhoto).into(civImageUpload);

        //getProfilePhoto()).into(holder.civAllLikesProfilePic);

        //initialize result launchers


    }


    @Override
    protected void onResume() {
        super.onResume();
        /*checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 101);
        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 102);
        checkSelfPermission(Manifest.permission.CAMERA, 103);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackImageUpload:
                finish();
                break;
            case R.id.ibtnPicImage:

                loadImageGallery();

                break;

            case R.id.ibtnCaptureImage:

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                loadImageCamera();

                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelection(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void imageSelection(int requestCode, int resultCode, Intent data) {

        Bitmap widgetImage = null;
        if (resultCode == RESULT_OK) {
            // Load Image from Gallery
            if (requestCode == SELECT_PICTURE && intent_source == 1) {
                Uri uriTest = data.getData();
                openCropper(uriTest);
            }

            // load image from Camera
            if (requestCode == REQUEST_CODE_TAKE_PICTURE && intent_source == 2) {
                Uri photoURIGallery = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(mFileTemp.getAbsolutePath()));
                openCropper(photoURIGallery);
            }

            // Load image after Cropping (Transparent)
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                uriDataImage = result.getUri();
                imageBitMap = null;
                try {
                    imageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriDataImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*if (sharedValue == TAG_GALLERY_PIC_MEASUREMENT) {
                    arrangementFragment.setBackgroundImage(imageBitMap);
                } else {
                    imageFragment.setImagePro(imageBitMap);
                }*/
                setImagePro(imageBitMap);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity, "Crop Error", Toast.LENGTH_SHORT).show();
            }


        }


        /*if (sharedValue == TAG_GALLERY_PIC_MEASUREMENT) {
            arrangementFragment.setBackgroundImage(widgetImage);
        } else {
            imageFragment.setImagePro(widgetImage);
        }*/

        setImagePro(widgetImage);
        intent_source = 0;

    }

    // Opening Image Cropper (Transparent)
    public void openCropper(Uri uri) {
        com.theartofdev.edmodo.cropper.CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setFixAspectRatio(true)
                .setAspectRatio(2, 3)
                .setAllowRotation(false)
                .setOutputCompressQuality(100)
                .start(activity);
    }


    /*public void openCropper(Uri uri) {
        CropImage.activity()
                .setAspectRatio(7, 11)
                .setAllowRotation(true)
                .setMinCropResultSize(720, 720)
                .setRequestedSize(720, 720)
                .setOutputCompressQuality(100)
                .start(activity);
    }*/



    // load image from camera
    @SuppressLint("NewApi")
    public void loadImageCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA};
                requestPermissions(trackerPerms, 100);
            } else {
                String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                requestPermissions(trackerPerms, 100);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA};
                    requestPermissions(trackerPerms, 100);
                } else {
                    String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    requestPermissions(trackerPerms, 100);
                }
            } else {
                // flag for using item or task
                File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + appImagePath);
                if (!sdCardDirectory.exists()) {
                    sdCardDirectory.mkdirs();
                }
                String state1 = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state1)) {
                    mFileTemp = new File(Environment.getExternalStorageDirectory() + appImagePath, TEMP_PHOTO_FILE_NAME);
                } else {
                    mFileTemp = new File(activity.getFilesDir() + appImagePath, TEMP_PHOTO_FILE_NAME);
                }
                //musicControl = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    Uri mImageCaptureUri = null;
                    String state2 = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state2)) {

                        // N is for Nougat Api 24 Android 7
                        if (Build.VERSION_CODES.N <= android.os.Build.VERSION.SDK_INT) {
                            try {
                                Log.e("errormImageCaptureUri", "loadImageCamera: ");
                                mImageCaptureUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", activity.mFileTemp);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("errormImageCaptureUri", "loadImageCamera: " + e.getLocalizedMessage());
                            }
                        } else {

                            mImageCaptureUri = Uri.fromFile(mFileTemp);
                        }

                    } else {
                        mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);

                } catch (ActivityNotFoundException e) {
                    Log.e("errCamera", "loadImageCamera: " + e.getLocalizedMessage());
                }

                intent_source = 2;
            }
        }
    }


    // load image from Gallery
    @SuppressLint("NewApi")
    public void loadImageGallery() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA};
                requestPermissions(trackerPerms, 100);
            } else {
                String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                requestPermissions(trackerPerms, 100);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA};
                    requestPermissions(trackerPerms, 100);
                } else {
                    String[] trackerPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    requestPermissions(trackerPerms, 100);
                }
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

                intent_source = 1;
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setImagePro(Bitmap bitmap) {
        //scale bitmap
        if (bitmap != null) {
            bitmapImage = bitmap;
            byteConvertedImage = Base64.encodeToString(imageProcessing.getBytesFromBitmap(bitmap), Base64.NO_WRAP);
            imgPath = imageProcessing.imageSave(bitmapImage);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(TAG_UPLOADED_PHOTO, byteConvertedImage);
            editor.putString(TAG_UPLOADED_PHOTO_PATH, imgPath);
            editor.commit();

            Intent intent = new Intent(activity, PhotoPostActivity.class);
            startActivity(intent);
            finish();

        }
    }


    /*public void uploadImageToServer(final Uri resultUri) {
        String filePath = getRealPathFromURIPath(resultUri, this);
        imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
        try {
            bitmap = new Compressor(activity).compressToBitmap(new File(resultUri.getPath()));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }*/

    private void checkSelfPermission(String permissions, int requestCode) {

        if (ContextCompat.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(activity, new String[]{permissions}, requestCode);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}