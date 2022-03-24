package net.boddo.btm.test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*
//rokan26.01.2021  import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;*/

import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.Limitation;

public class GoogleAdMob {

    private Context context;
    //rokan26.01.2021  InterstitialAd mInterstitialAd;

    public GoogleAdMob(Context context) {
        this.context = context;
    }

    /*
    //rokan26.01.2021
    public void getGooleAdMob(final String condition) {

        *//*
        //rokan26.01.2021  mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(Constants.INTERSTITIAL_AD);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());*//*


        if (!Data.isPalupPlusSubcriber) {
            if (Helper.getSignUp_day > Limitation.ADS_LIMIT_SHOW) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        }


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                if (!Data.isPalupPlusSubcriber){

                    if (Helper.getSignUp_day > Limitation.ADS_LIMIT_SHOW) {
                        if (condition.equals("photoblog")) {
                            if (Limitation.ADS_LIMIT == 0) {
                                mInterstitialAd.show();
                                Limitation.ADS_LIMIT = 1;
                            }
                        } else if (condition.equals("like")) {
                            mInterstitialAd.show();
                        } else if (condition.equals("navigationBar")) {
                            mInterstitialAd.show();
                        }
                    }

                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toast.makeText(context, errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {

            }
        });
    }*/


}
