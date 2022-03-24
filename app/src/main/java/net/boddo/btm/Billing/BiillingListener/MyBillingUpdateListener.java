package net.boddo.btm.Billing.BiillingListener;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.util.List;

import net.boddo.btm.Billing.BillingManager;

public class MyBillingUpdateListener implements BillingManager.BillingUpdatesListener {


//    BillingManager billingManager;

    @Override
    public void onBillingClientSetupFinished() {

    }

    @Override
    public void onConsumeFinished(String token, int result) {
        if (result == BillingClient.BillingResponse.OK) {
        }
    }

    @Override
    public void onPurchasesUpdated(List<Purchase> purchases) {
        for (Purchase p : purchases) {
        }
    }
}
