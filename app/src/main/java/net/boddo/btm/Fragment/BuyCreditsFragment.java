package net.boddo.btm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Billing.BillingManager;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyCreditsFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "palup.inapbilling";

    private static final String TOKEN = "palup.inappbilling";

    public String ITEM_NO_ONE = "com.palup_credits_100_buy1";

    public String ITEM_NO_TWO = "com.palup_credits_600_buy5";

    public String ITEM_NO_THREE = "com.palup_credits_1400_buy10";

    public String ITEM_NO_FOUR = "com.palup_credits_3000_buy20";

    public String ITEM_NO_FIVE = "com.palup_credits_10000_buy50";

    private static final String PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA08bDDN4RoTGfmEqKk6gFDXtPCSTHx2b65ARv7+B+rrqQX5hMWZo8HT0LplsWvq4ZGN6GottC2IQXqe6uS77ytjEtodIaeKPADG3Hla2Q9eeYhnw6ohntaSCa1b/sBJ1CA86QcUTDM60U2/+FOtX/HkloVjrCY8LWk6TVvdr3NHtsAB/YWUPiiF9ioNDMPY1NmkPZEoPRVrYv35e8BePWYUE0VFwpWTOIcFI2m4rSrMwpPpCtr3c1884rgzLXJIrOazS986QLE7Qgk2j/SvOEPD1W2MJyxPvvIkV+ihh09WFPi6tGT/DR39BxZjbB/CkeKw1OylZDBRorSuBBvBti3QIDAQAB";


    RadioButton llBuyCredit_1000, llBuyCredit_2000, llBuyCredit_5000, llBuyCredit_10000;
    TextView tvContinue, tvBackCredits;


    //in app billing
// create new Person

    List<String> skuList;

    BillingManager billingManager;


    ApiInterface apiInterface;

    int pp = 0;
    int amount = 0;
    byte retryConsume = 0;
    //ArrayList<String> list;

    boolean isConsume = false;

    BuyCreditActivity activity;

    public BuyCreditsFragment(BuyCreditActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buy_credits, container, false);
        ButterKnife.bind(this, view);

        llBuyCredit_1000 = view.findViewById(R.id.llBuyCredit_1000);
        llBuyCredit_2000 = view.findViewById(R.id.llBuyCredit_2000);
        llBuyCredit_5000 = view.findViewById(R.id.llBuyCredit_5000);
        llBuyCredit_10000 = view.findViewById(R.id.llBuyCredit_10000);
        tvContinue = view.findViewById(R.id.continue_button);
        tvBackCredits = view.findViewById(R.id.tvBackCredits);

        llBuyCredit_1000.setOnClickListener(this);
        llBuyCredit_2000.setOnClickListener(this);
        llBuyCredit_5000.setOnClickListener(this);
        llBuyCredit_10000.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
        tvBackCredits.setOnClickListener(this);


        //list = new ArrayList<>();

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

                if (BuyCreditActivity.isConsumable) {
                    billingManager.consumeAsync(purchases.get(0).getPurchaseToken());
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<String> call = apiInterface.onSuccessfullyPurchased(
                            Constants.SECRET_KEY,
                            Data.userId,
                            purchases.get(0).getOrderId(),
                            purchases.get(0).getSku(),
                            pp,
                            amount);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body().equals("success")) {
                                Toast.makeText(getActivity(), "Successfully added point", Toast.LENGTH_SHORT).show();
                                Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) + pp);
                                EventBus.getDefault().post(new Event(Constants.UPDATE_PALUP_POINT));
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(BuyCreditsFragment.class.getSimpleName(), t.getMessage());
                        }
                    });
                }
                Log.d("Billing", purchases.toString());
//                Toast.makeText(getActivity(), "onPurchase", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llBuyCredit_1000:
                onFirstItemClick();
                break;

            case R.id.llBuyCredit_2000:
                onSecondItemClick();
                break;

            case R.id.llBuyCredit_5000:
                onThiredItemClick();
                break;

            case R.id.llBuyCredit_10000:
                onFourthItemClick();
                break;

            case R.id.tvContinue:
                onFifthItemClick();
                break;

            case R.id.tvBackCredits:
                Intent intent = new Intent(getActivity(), DashBoadActivity.class);
                intent.putExtra("profile", "profile");
                startActivity(intent);
                //getActivity().finish();
                //activity.goBack();
                break;


        }
    }


    public void onFirstItemClick() {
        BuyCreditActivity.isConsumable = true;
        amount = 1;
        pp = 100;
        billingManager.initiatePurchaseFlow(ITEM_NO_ONE, null, BillingClient.SkuType.INAPP);
    }

    public void onSecondItemClick() {
        BuyCreditActivity.isConsumable = true;
        amount = 5;
        pp = 600;
        billingManager.initiatePurchaseFlow(ITEM_NO_TWO, null, BillingClient.SkuType.INAPP);
    }

    public void onThiredItemClick() {
        BuyCreditActivity.isConsumable = true;
        amount = 10;
        pp = 1400;
        billingManager.initiatePurchaseFlow(ITEM_NO_THREE, null, BillingClient.SkuType.INAPP);
    }

    public void onFourthItemClick() {
        BuyCreditActivity.isConsumable = true;
        amount = 20;
        pp = 3000;
        billingManager.initiatePurchaseFlow(ITEM_NO_FOUR, null, BillingClient.SkuType.INAPP);
    }

    public void onFifthItemClick() {
        BuyCreditActivity.isConsumable = true;
        amount = 50;
        pp = 10000;
        billingManager.initiatePurchaseFlow(ITEM_NO_FIVE, null, BillingClient.SkuType.INAPP);
    }


}
