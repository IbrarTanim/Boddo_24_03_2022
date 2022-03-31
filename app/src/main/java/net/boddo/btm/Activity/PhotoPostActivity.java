package net.boddo.btm.Activity;

import static net.boddo.btm.Utills.StaticAccess.TAG_UPLOADED_PHOTO;
import static net.boddo.btm.Utills.StaticAccess.TAG_UPLOADED_PHOTO_PATH;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ImageProcessing;

import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoPostActivity extends AppCompatActivity implements View.OnClickListener {
    PhotoPostActivity activity;
    TextView ibBackPhotoPost;
    TextView tvPost, tvUpDown;
    CircleImageView civProfilePhotoPost;
    EditText edtPhotoPost;
    ImageView ivUploadedPhoto;
    SharedPreferences sharedpreferences;
    String imagePath;
    String image;
    ImageProcessing imageProcessing;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_post);
        activity = this;

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        sharedpreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        imageProcessing = new ImageProcessing(activity);
        ibBackPhotoPost = findViewById(R.id.ibBackPhotoPost);
        tvPost = findViewById(R.id.tvPost);
        tvUpDown = findViewById(R.id.tvUpDown);
        civProfilePhotoPost = findViewById(R.id.civProfilePhotoPost);
        edtPhotoPost = findViewById(R.id.edtPhotoPost);
        ivUploadedPhoto = findViewById(R.id.ivUploadedPhoto);
        imagePath = sharedpreferences.getString(TAG_UPLOADED_PHOTO_PATH, null);
        image = sharedpreferences.getString(TAG_UPLOADED_PHOTO, null);
        imageProcessing.setImageWith_loader(ivUploadedPhoto, imagePath);

        Glide.with(activity).load(Data.profilePhoto).into(civProfilePhotoPost);


        edtPhotoPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPost.setTextColor(getResources().getColor(R.color.app_color));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibBackPhotoPost.setOnClickListener(this);
        tvUpDown.setOnClickListener(this);
        tvPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackPhotoPost:
                intent = new Intent(activity, ImageUploadActivity.class);
                startActivity(intent);
                finish();


            case R.id.tvUpDown:

                break;

            case R.id.tvPost:
                onPost();
                break;

        }
    }


    private void onPost() {
        if (ivUploadedPhoto.getDrawable() != null) {
            //imagePath = imageToString();
            String caption = edtPhotoPost.getText().toString();
            //!caption.equals("") ? caption : ""
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<UserPhotoBlogImages[]> call = apiInterface.uploadPhotoBlog(Constants.SECRET_KEY, image, imagePath, Data.userId, caption);
            call.enqueue(new Callback<UserPhotoBlogImages[]>() {
                @Override
                public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {
                    if (response.code() == 200) {
                        UserPhotoBlogImages[] userPhotoBlogImages = response.body();
                        for (UserPhotoBlogImages userPhotoBlogImage1 : userPhotoBlogImages) {
                            Log.e("Photo Post", userPhotoBlogImage1.toString());
                        }
                        Toast.makeText(activity, "Your image is in review. Please wait for a while!", Toast.LENGTH_LONG).show();
                        finish();

                        //UploadedSuccessfully();
                    }
                }

                @Override
                public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                    Log.d("PictureUploadActivity", t.getMessage());
                    Toast.makeText(activity, "Someting went wrong plese try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            final PrettyDialog prettyDialog = new PrettyDialog(activity);
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





    private void UploadedSuccessfully() {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setTitle("info");
        dialog.setContentView(R.layout.upload_successfully);
        final Button btnDialogOK = dialog.findViewById(R.id.btnDialogOK);
        btnDialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

    }







}