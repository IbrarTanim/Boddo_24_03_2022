package net.boddo.btm.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.rd.PageIndicatorView;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Adepter.PalupPlusSliderAdapter;
import net.boddo.btm.Billing.BillingManager;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.IabHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalupPlusFragment extends Fragment {

    @BindView(R.id.subscribe_RbGroup)
    RadioGroup subscribeRbGroup;

    RadioButton rbSubscribeButton;
    TextView text1, text2, text3, text4, text5, text6, text7, text8, text9;
    LinearLayout firstClassPalupPlusFragment,premiumClassPalupPlusFragment,plusPalupPlusFragment;

    TextView tvBackMembership;
    TextView month6_title,month2_title,month3_title;

    String radioButtonValue;

    @BindView(R.id.image_indicator)
    PageIndicatorView indecator;

    @BindView(R.id.six_month_subscription)
    RadioButton sixMonthsSubscription;

    @BindView(R.id.three_month_subscription)
    RadioButton threeMonthsSubscription;

    @BindView(R.id.one_month_subscription)
    RadioButton oneMonthSubscription;

    @BindView(R.id.continue_button)
    TextView activeButton;

    @BindView(R.id.subscription_dialog_show)
    TextView subscriptionTextView;

    ViewPager viewFlipper;

    @BindView(R.id.subscription_layout)
    RelativeLayout layout;

    BillingManager billingManager;
    IabHelper iabHelper;
    BuyCreditActivity activity;
    List<String> list;

    public PalupPlusFragment(BuyCreditActivity activity) {
        this.activity = activity;
    }

    public static final String SUBCRIPTION_ITEM_ONE = "com.palup_subscription_6";
    public static final String SUBCRIPTION_ITEM_TWO = "com.palup_subscription_3_months";
    public static final String SUBCRIPTION_ITEM_THREE = "com.palup_subscription_1";

    private static final String PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA08bDDN4RoTGfmEqKk6gFDXtPCSTHx2b65ARv7+B+rrqQX5hMWZo8HT0LplsWvq4ZGN6GottC2IQXqe6uS77ytjEtodIaeKPADG3Hla2Q9eeYhnw6ohntaSCa1b/sBJ1CA86QcUTDM60U2/+FOtX/HkloVjrCY8LWk6TVvdr3NHtsAB/YWUPiiF9ioNDMPY1NmkPZEoPRVrYv35e8BePWYUE0VFwpWTOIcFI2m4rSrMwpPpCtr3c1884rgzLXJIrOazS986QLE7Qgk2j/SvOEPD1W2MJyxPvvIkV+ihh09WFPi6tGT/DR39BxZjbB/CkeKw1OylZDBRorSuBBvBti3QIDAQAB";
    int amount = 0;


    int sixMonthCost = 45;
    int threeMonthCost = 25;
    int oneMonth = 10;
    boolean isAutoRenewOn = false;
    TextView tvLongText;

    Purchase purchaseList;
    public static boolean isAlreadySixMonthsSubscriber = false;

    PalupPlusSliderAdapter imageLoaderAdapter;
    boolean isActivityRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_palup_plus, container, false);
        ButterKnife.bind(this, view);
        init(view);
        list = new ArrayList<>();
        indecator = new PageIndicatorView(getContext());


        tvLongText = view.findViewById(R.id.tvLongText);
        firstClassPalupPlusFragment = view.findViewById(R.id.firstClassPalupPlusFragment);
        premiumClassPalupPlusFragment = view.findViewById(R.id.premiumClassPalupPlusFragment);
        plusPalupPlusFragment = view.findViewById(R.id.plusPalupPlusFragment);
        month6_title = view.findViewById(R.id.month6_title);
        month2_title = view.findViewById(R.id.month2_title);
        month3_title = view.findViewById(R.id.month3_title);

        /*String text = "By tapping 'Continue' and confirming your subscription, you accept Boddo's Terms &amp; Conditions and request that your subscription start immediately. Your Google Play account will be charged the fee mentioned above. Your subscription will automatically renew for the same period and price until you deactivate auto-renewal in your Google Play account settings at least 24 hours prior to the end of the current period. Google Play's cancellation rights will apply.";

        SpannableString ss = new SpannableString(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 1, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLongText.setText(ss);*/

        firstClassPalupPlusFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month6_title.setBackground(getResources().getDrawable(R.drawable.post_select_button));
                month2_title.setBackgroundColor(Color.parseColor("#aaa494"));
                month3_title.setBackgroundColor(Color.parseColor("#aaa494"));

            }
        });
        premiumClassPalupPlusFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month2_title.setBackground(getResources().getDrawable(R.drawable.post_select_button));
                month6_title.setBackgroundColor(Color.parseColor("#aaa494"));
                month3_title.setBackgroundColor(Color.parseColor("#aaa494"));
            }
        });
        plusPalupPlusFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month3_title.setBackground(getResources().getDrawable(R.drawable.post_select_button));
                month2_title.setBackgroundColor(Color.parseColor("#aaa494"));
                month6_title.setBackgroundColor(Color.parseColor("#aaa494"));

            }
        });


        isActivityRunning = true;
        billingManager = new BillingManager(getActivity(), new BillingManager.BillingUpdatesListener() {
            @Override
            public void onBillingClientSetupFinished() {
                billingManager.queryPurchases();
            }

            @Override
            public void onConsumeFinished(String token, int result) {
            }

            @Override
            public void onPurchasesUpdated(final List<Purchase> purchases) {
                if (purchases != null && purchases.size() != 0) {
                    if (purchases.get(0).getSku().contains("subscription")) {
                        if (purchases.get(0).isAutoRenewing()) {
                            subscriptionTextView.setVisibility(View.VISIBLE);
                            if (purchases.get(0).getSku().equals(SUBCRIPTION_ITEM_ONE)) {
                                subscriptionTextView.setText("You have already purchased 6 months subscription");
                            } else if (purchases.get(0).getSku().equals(SUBCRIPTION_ITEM_TWO)) {
                                subscriptionTextView.setText("You have already purchased 3 months subscription");
                            } else if (purchases.get(0).getSku().equals(SUBCRIPTION_ITEM_THREE)) {
                                subscriptionTextView.setText("You have already purchased 1 month subscription");
                            }
                            layout.setVisibility(View.GONE);
                            list.add(purchases.get(0).getSku());
                            activeButton.setText("Cancel Subscription");
                            isAutoRenewOn = true;
                        } else {
                            subscriptionTextView.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.VISIBLE);
                            activeButton.setText("Active");
                        }
                    }
                }

                if (BuyCreditActivity.isSubscribed) {
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<String> call = apiInterface.onSuccessfullySubscripttion(
                            Constants.SECRET_KEY,
                            Data.userId,
                            purchases.get(0).getOrderId(),
                            purchases.get(0).getSku(), amount
                    );
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body().equals("success")) {
                                Toast.makeText(getActivity(), "Successfully Subscribed", Toast.LENGTH_SHORT).show();
                                Data.isPalupPlusSubcriber = true;
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(BuyCreditsFragment.class.getSimpleName(), t.getMessage());
                        }
                    });
                }
            }
        });

        viewFlipper = view.findViewById(R.id.imageSlider);
        tvBackMembership = view.findViewById(R.id.tvBackMembership);
        tvBackMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DashBoadActivity.class);
                intent.putExtra("profile", "profile");
                startActivity(intent);
            }
        });

        imageLoaderAdapter = new PalupPlusSliderAdapter(getContext());
        viewFlipper.setAdapter(imageLoaderAdapter);

        Timer timeTasker = new Timer();
        timeTasker.scheduleAtFixedRate(new TimeTasker(), 1000, 2000);


        subscribeRbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSubscribeButton = view.findViewById(checkedId);
                radioButtonValue = rbSubscribeButton.getText().toString();
                switch (checkedId) {
                    case R.id.six_month_subscription:
                       // text1.setTextColor(getResources().getColor(R.color.white));
                        //text2.setTextColor(getResources().getColor(R.color.white));
                       // text3.setTextColor(getResources().getColor(R.color.white));
                        text4.setTextColor(getResources().getColor(R.color.Black));
                        text5.setTextColor(getResources().getColor(R.color.Black));
                        text6.setTextColor(getResources().getColor(R.color.Black));
                        text7.setTextColor(getResources().getColor(R.color.Black));
                        text8.setTextColor(getResources().getColor(R.color.Black));
                        text9.setTextColor(getResources().getColor(R.color.Black));
                        break;
                    case R.id.three_month_subscription:
                        text1.setTextColor(getResources().getColor(R.color.Black));
                        text2.setTextColor(getResources().getColor(R.color.Black));
                        text3.setTextColor(getResources().getColor(R.color.Black));
                      //  text4.setTextColor(getResources().getColor(R.color.white));
                       // text5.setTextColor(getResources().getColor(R.color.white));
                        //text6.setTextColor(getResources().getColor(R.color.white));
                        text7.setTextColor(getResources().getColor(R.color.Black));
                        text8.setTextColor(getResources().getColor(R.color.Black));
                        text9.setTextColor(getResources().getColor(R.color.Black));
                        break;
                    case R.id.one_month_subscription:
                        text1.setTextColor(getResources().getColor(R.color.Black));
                        text2.setTextColor(getResources().getColor(R.color.Black));
                        text3.setTextColor(getResources().getColor(R.color.Black));
                        text4.setTextColor(getResources().getColor(R.color.Black));
                        text5.setTextColor(getResources().getColor(R.color.Black));
                        text6.setTextColor(getResources().getColor(R.color.Black));
                        //text7.setTextColor(getResources().getColor(R.color.white));
                       // text8.setTextColor(getResources().getColor(R.color.white));
                      //  text9.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        });
        return view;
    }

    @OnClick(R.id.continue_button)
    public void onSubscriptionButtonClicked() {
        int id = subscribeRbGroup.getCheckedRadioButtonId();
        if (isAutoRenewOn) {
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(SUBCRIPTION_ITEM_ONE)) {
//                        showDialog(SUBCRIPTION_ITEM_ONE);
                        i = list.size();
                        billingManager.initiatePurchaseFlow(SUBCRIPTION_ITEM_ONE, null, BillingClient.SkuType.SUBS);
                    } else if (list.get(i).equals(SUBCRIPTION_ITEM_TWO)) {
//                        showDialog(SUBCRIPTION_ITEM_TWO);
                        billingManager.initiatePurchaseFlow(SUBCRIPTION_ITEM_TWO, null, BillingClient.SkuType.SUBS);
                        i = list.size();
                    } else if (list.get(i).equals(SUBCRIPTION_ITEM_THREE)) {
                        billingManager.initiatePurchaseFlow(SUBCRIPTION_ITEM_THREE, null, BillingClient.SkuType.SUBS);
//                        showDialog(SUBCRIPTION_ITEM_THREE);
                        i = list.size();
                    }
                }
                return;
            }
        } else {
            switch (id) {
                case R.id.six_month_subscription:
                    amount = 45;
                    BuyCreditActivity.isSubscribed = true;
                    billingManager.initiatePurchaseFlow(SUBCRIPTION_ITEM_ONE, null, BillingClient.SkuType.SUBS);
                    break;
                case R.id.three_month_subscription:
                    amount = 25;
                    BuyCreditActivity.isSubscribed = true;
                    billingManager.initiatePurchaseFlow(SUBCRIPTION_ITEM_TWO, null, BillingClient.SkuType.SUBS);
                    break;
                case R.id.one_month_subscription:
                    amount = 10;
                    BuyCreditActivity.isSubscribed = true;
                    billingManager.initiatePurchaseFlow(SUBCRIPTION_ITEM_THREE, null, BillingClient.SkuType.SUBS);
                    break;
            }
        }
    }

    public void showDialog(final String item) {
        final PrettyDialog dialog = new PrettyDialog(getContext());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            dialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setTitle("Dear " + Data.userName)
                    .setMessage("You are already a Palup plus user.")
                    .setMessageColor(R.color.blue_800)
                    .addButton("Got it", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                            return;
                        }
                    })
                    .addButton("Cancel Subscription", R.color.red_800, R.color.white, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            billingManager.initiatePurchaseFlow(item, null, BillingClient.SkuType.SUBS);
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            dialog.setTitle("Dear " + Data.userName)
                    .setMessage("You are already a Palup plus user.")
                    .setMessageColor(R.color.blue_800)
                    .addButton("Got it", R.color.white, R.color.amber_900, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                            return;
                        }
                    })
                    .addButton("Cancel Subscription", R.color.red_800, R.color.white, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            billingManager.initiatePurchaseFlow(item, null, BillingClient.SkuType.SUBS);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    private void init(View view) {
        text1 = view.findViewById(R.id.month_textView);
        text2 = view.findViewById(R.id.medal_textView);
        text3 = view.findViewById(R.id.money_textView);
        text4 = view.findViewById(R.id.month2_textView);
        text5 = view.findViewById(R.id.medal2_textView);
        text6 = view.findViewById(R.id.money2_textView);
        text7 = view.findViewById(R.id.month3_textView);
        text8 = view.findViewById(R.id.medal3_textView);
        text9 = view.findViewById(R.id.money3_textView);
        text1.setTextColor(getResources().getColor(R.color.Black));
        text2.setTextColor(getResources().getColor(R.color.Black));
        text3.setTextColor(getResources().getColor(R.color.Black));
        text4.setTextColor(getResources().getColor(R.color.Black));
        text5.setTextColor(getResources().getColor(R.color.Black));
        text6.setTextColor(getResources().getColor(R.color.Black));
        text7.setTextColor(getResources().getColor(R.color.Black));
        text8.setTextColor(getResources().getColor(R.color.Black));
        text9.setTextColor(getResources().getColor(R.color.Black));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BuyCreditActivity.isSubscribed = false;
    }

    class TimeTasker extends TimerTask {

        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (viewFlipper.getCurrentItem() == 0) {
                            viewFlipper.setCurrentItem(1);
                        } else if (viewFlipper.getCurrentItem() == 1) {
                            viewFlipper.setCurrentItem(2);
                        } else if (viewFlipper.getCurrentItem() == 2) {
                            viewFlipper.setCurrentItem(3);
                        } else if (viewFlipper.getCurrentItem() == 3) {
                            viewFlipper.setCurrentItem(4);
                        } else if (viewFlipper.getCurrentItem() == 4) {
                            viewFlipper.setCurrentItem(5);
                        } else if (viewFlipper.getCurrentItem() == 5) {
                            viewFlipper.setCurrentItem(6);
                        } else if (viewFlipper.getCurrentItem() == 6) {
                            viewFlipper.setCurrentItem(7);
                        } else {
                            viewFlipper.setCurrentItem(0);
                        }
                    }
                });
            } else {
                if (getActivity() == null)
                    return;
            }
        }
    }
}