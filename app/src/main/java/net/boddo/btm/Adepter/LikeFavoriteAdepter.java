package net.boddo.btm.Adepter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.Liked;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Limitation;
import net.boddo.btm.Utills.SearchUser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeFavoriteAdepter extends RecyclerView.Adapter<LikeFavoriteAdepter.LoveFavoriteViewHolder> {

    Context context;
    List<Liked.LikedMe> likedMeList;
    Dialog mDialog;
    String viewType;
    ApiInterface apiInterface;
    PrettyDialog dialog;
    String age;
    int date = 0;
    int month = 0;
    int year = 0;


    private final static int FADE_DURATION = 350;

    public LikeFavoriteAdepter(Context context, List<Liked.LikedMe> likedMeList, String viewType) {
        this.context = context;
        this.likedMeList = likedMeList;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public LikeFavoriteAdepter.LoveFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_favorite_model, parent, false);


        return new LoveFavoriteViewHolder(view);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void convertStringToDateFormat(String stringDate) {
        String[] stringArray = stringDate.split("/");
        date = Integer.parseInt(stringArray[0]);
        month = Integer.parseInt(stringArray[1]);
        year = Integer.parseInt(stringArray[2]);
    }


    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = String.valueOf(ageInt);
        return ageS;
    }


    @Override
    public void onBindViewHolder(@NonNull final LikeFavoriteAdepter.LoveFavoriteViewHolder holder, final int position) {

        String dDate = likedMeList.get(position).getDateOfBirth();
        if (likedMeList.get(position).getDateOfBirth() != null) {
            if (!likedMeList.get(position).getDateOfBirth().equals("")) {
                convertStringToDateFormat(likedMeList.get(position).getDateOfBirth());
                age = getAge(year, month, date);
            }
        }

        if (Data.isPalupPlusSubcriber) {
            holder.tvUserNameActivity.setText(likedMeList.get(position).getName());
            holder.tvUserAgeActivity.setText(age);
            if (likedMeList.get(position).getGender() != null) {
                if (likedMeList.get(position).getGender().equals("Male")) {
                    holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
                } else {
                    holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);
                }
            }
            Picasso.get().load(likedMeList.get(position).getProfilePhoto()).into(holder.likeimageView);
            holder.likeimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAllData(position);
                }
            });
        } else if (likedMeList.get(position).getIsSeen() != null) {
            if (likedMeList.get(position).getIsSeen().equals("1")) {
                holder.tvUserNameActivity.setText(likedMeList.get(position).getName());
                holder.tvUserAgeActivity.setText(age);

                if (likedMeList.get(position).getGender() != null) {
                    if (likedMeList.get(position).getGender().equals("Male")) {
                        holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
                    } else {
                        holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);
                    }
                }


                Picasso.get().load(likedMeList.get(position).getProfilePhoto()).into(holder.likeimageView);
                holder.likeimageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAllData(position);
                    }
                });
            } else {
                if (position <= Limitation.LIKE_FAV_VISITOR_SHOW_PHOTO - 1) {
                    holder.tvUserNameActivity.setText(likedMeList.get(position).getName());
                    holder.tvUserAgeActivity.setText(age);
                    if (likedMeList.get(position).getGender() != null) {
                        Log.e("Gender issue", String.valueOf(likedMeList.get(position).getUserId()));
                        if (likedMeList.get(position).getGender().equals("Male")) {
                            holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
                        } else {
                            holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);

                        }
                    }
                    Picasso.get().load(likedMeList.get(position).getProfilePhoto()).into(holder.likeimageView);
                    holder.likeimageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAllData(position);
                        }
                    });
                } else {

                    holder.tvUserNameActivity.setText(likedMeList.get(position).getName());
                    holder.tvUserAgeActivity.setText(age);
                    if (likedMeList.get(position).getGender() != null) {
                        if (likedMeList.get(position).getGender().equals("Male")) {
                            holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
                        } else {
                            holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);
                        }
                    }


                    Picasso.get().load(likedMeList.get(position).getProfilePhoto()).into(holder.likeimageView);
                    //holder.quetionMark.setVisibility(View.VISIBLE);
                    holder.blurEffect.setVisibility(View.VISIBLE);
                    holder.rlUserInfoActivity.setVisibility(View.INVISIBLE);
                    holder.likeimageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            interestedToDialog(holder, position);

                            /*dialog = new PrettyDialog(context);
                            String message = "see who interested in you!\n" +
                                    "\n" +
                                    "Check it out who visited your profile and send a chat request or like their profile." +
                                    "\n" + "Cost of viewing of this user 10 credits";
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                dialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary);
                            }
                            showPrettyDialog(holder, position, message);*/
                        }
                    });

                }

            }
        }
        setScaleAnimation(holder.itemView);
    }

    private void showAllData(int position) {
        //Data.pd = new ProgressDialog(context);
        //Data.pd.setTitle("Loading...");
        //Data.pd.setMessage("Please wait for a while...");
        SearchUser userProfile = new SearchUser(context);
        Data.otherUserId = likedMeList.get(position).getUserId();
        userProfile.searchUserInfo();
        //Data.pd.show();
    }


    public void interestedToDialog(final LoveFavoriteViewHolder holder, final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.interested_to_dialog);
        dialog.setCancelable(false);

        final TextView tvFindoutMessage = dialog.findViewById(R.id.tvFindoutMessage);
        final Button payView = dialog.findViewById(R.id.payjoin);
        final Button btnDiscoverBoddoPlus = dialog.findViewById(R.id.btnDiscoverBoddoPlus);
        final Button notnow = dialog.findViewById(R.id.notnow);

        if (viewType.equals("like")) {
            tvFindoutMessage.setText("Find out who's liked your profile.");
        } else if (viewType.equals(Constants.FAVORITE_VIEW_TYPE)) {
            tvFindoutMessage.setText("Find out who's favorite your profile.");
        } else if (viewType.equals("visitors")) {
            tvFindoutMessage.setText("Find out who's visited your profile.");
        }

        payView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPrettyDialog(holder, position, message);
                Log.e("IntergerValue", "onClick: " + Integer.parseInt(Data.userPalupPoint));
                Log.e("IntergerValue", "onClick: " + Limitation.LIKE_FAV_VISITOR_CREDITS);

                if (Integer.parseInt(Data.userPalupPoint) >= Limitation.LIKE_FAV_VISITOR_CREDITS) {
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<String> call = apiInterface.getUnlockSeen(Constants.SECRET_KEY, Data.userId, likedMeList.get(position).getUserId(), viewType);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String res = response.body();

                            if (res.equals("success")) {

                                Picasso.get().load(likedMeList.get(position).getProfilePhoto()).into(holder.likeimageView);
                                holder.tvUserNameActivity.setText(likedMeList.get(position).getName());
                                holder.tvUserAgeActivity.setText(age);
                                try {
                                    if (likedMeList.get(position).getGender().equals("Male")) {
                                        holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
                                    } else {
                                        holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);
                                    }
                                } catch (Exception e) {
                                    //Skip
                                }


                                //holder.quetionMark.setVisibility(View.INVISIBLE);
                                holder.blurEffect.setVisibility(View.INVISIBLE);
                                holder.rlUserInfoActivity.setVisibility(View.VISIBLE);
                                Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) - 15);
                                EventBus.getDefault().post(new Event(Constants.UNLOCK_FAVORITE_IMAGE));
                                EventBus.getDefault().post(new Event(Constants.UPDATE_PALUP_POINT));
                                dialog.cancel();
                            } else if (response.body().equals("failed")) {
                                Toast.makeText(context, "Something went wrong! please try again later or Contact with Boddo team", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(context.getClass().getSimpleName(), t.getMessage());
                        }
                    });
                } else {

                    insufficientCredits();

                }


                dialog.dismiss();
            }
        });

        btnDiscoverBoddoPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPalupPlusWindow();
                dialog.dismiss();
            }
        });


        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    @Override
    public int getItemCount() {
        if (likedMeList == null) {
            likedMeList = new ArrayList<Liked.LikedMe>();
        }
        return likedMeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class LoveFavoriteViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView likeimageView;
        //TextView quetionMark;
        TextView tvUserNameActivity, tvUserAgeActivity;
        ImageView ivMaleFemale;
        LinearLayout blurEffect;
        RelativeLayout layout, rlUserInfoActivity;

        public LoveFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            likeimageView = itemView.findViewById(R.id.like_favorite_imageView);
            //quetionMark = itemView.findViewById(R.id.quetionMark);
            blurEffect = itemView.findViewById(R.id.blur_effect);
            layout = itemView.findViewById(R.id.like_favorite_layout);
            ivMaleFemale = itemView.findViewById(R.id.ivMaleFemale);
            tvUserNameActivity = itemView.findViewById(R.id.tvUserNameActivity);
            tvUserAgeActivity = itemView.findViewById(R.id.tvUserAgeActivity);
            rlUserInfoActivity = itemView.findViewById(R.id.rlUserInfoActivity);
        }
    }

    @Override
    public void onViewAttachedToWindow(LikeFavoriteAdepter.LoveFavoriteViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewRecycled(@NonNull LikeFavoriteAdepter.LoveFavoriteViewHolder holder) {
        super.onViewRecycled(holder);
        holder.layout.setVisibility(View.VISIBLE);
    }

    private void palupDailog() {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.custom_alert_dialog_subscription);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    public void goToPalupPlusWindow() {

        Intent intent = new Intent(context, BuyCreditActivity.class);
        intent.putExtra("Membership", true);
        //overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);


    }


    /*public void showPrettyDialog(final LoveFavoriteViewHolder holder, final int position, String message) {
        dialog.setTitle("Dear " + Data.userName).
                setMessage(message).setMessageColor(R.color.red_A700)
                .addButton("VIEW NOW", R.color.white, R.color.material_deep_teal_500, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {


                        if (Integer.parseInt(Data.userPalupPoint) >= Limitation.LIKE_FAV_VISITOR_CREDITS) {
                            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<String> call = apiInterface.getUnlockSeen(Constants.SECRET_KEY, Data.userId, likedMeList.get(position).getUserId(), viewType);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String res = response.body();
                                    if (res.equals("success")) {

                                        Picasso.get().load(likedMeList.get(position).getProfilePhoto()).into(holder.likeimageView);
                                        holder.tvUserNameActivity.setText(likedMeList.get(position).getName());
                                        holder.tvUserAgeActivity.setText(age);
                                        if (likedMeList.get(position).getGender().equals("Male")) {
                                            holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
                                        } else {
                                            holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);
                                        }


                                        //holder.quetionMark.setVisibility(View.INVISIBLE);
                                        holder.blurEffect.setVisibility(View.INVISIBLE);
                                        Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) - 15);
                                        EventBus.getDefault().post(new Event(Constants.UNLOCK_FAVORITE_IMAGE));
                                        EventBus.getDefault().post(new Event(Constants.UPDATE_PALUP_POINT));
                                        dialog.cancel();
                                    } else if (response.body().equals("failed")) {
                                        Toast.makeText(context, "Something wrong please try again later. Or Contact with palup team", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d(context.getClass().getSimpleName(), t.getMessage());
                                }
                            });
                        } else {


                            *//*PrettyDialog jj = new PrettyDialog(context)
                                    .setTitle("Dear " + Data.userName)
                                    .setMessage("You don't have sufficient palup credits, please recharge! \n Through the credits you can view who visit or liked, favorite you. You can use your credit for joining 'Be Seen' feature.").setMessageColor(R.color.red_A700);
                            jj.show();*//*

                        }
                    }
                }).addButton("ACTIVE PALUP PLUS", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                //    palupDailog();
                Intent intent = new Intent(context, BuyCreditActivity.class);
                intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
                context.startActivity(intent);
            }
        }).show();
    }*/


    public void insufficientCredits() {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.insufficient_credits_likefavorit_adapter);
        dialog.setCancelable(false);

        final Button payjoin = dialog.findViewById(R.id.payjoin);
        final Button btnDiscoverBoddoPlusInsufficient = dialog.findViewById(R.id.btnDiscoverBoddoPlusInsufficient);
        final Button notnow = dialog.findViewById(R.id.notnow);

        payjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                //context.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });


        btnDiscoverBoddoPlusInsufficient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPalupPlusWindow();
                dialog.dismiss();
            }
        });


        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
