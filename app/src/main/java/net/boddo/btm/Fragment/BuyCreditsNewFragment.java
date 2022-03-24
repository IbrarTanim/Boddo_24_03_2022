package net.boddo.btm.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BuyCreditsNewFragment extends Fragment {


    @BindView(R.id.buy_100pp)
    Button buyCredit100;

    @BindView(R.id.buy_600pp)
    Button buyCredit600;

    @BindView(R.id.buy_1400pp)
    Button buyCredit1400;

    @BindView(R.id.buy_3000pp)
    Button buyCredit3000;

    @BindView(R.id.buy_10000pp)
    Button buyCredit10000;

    int credit = 0;
    int price = 0;
    ApiInterface apiInterface;

    //billing api client



    public BuyCreditsNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_credits_new, container, false);
        ButterKnife.bind(this, view);

        return view ;
    }

    @OnClick(R.id.buy_100pp)
    public void onBuy100Credits() {
        credit = 100;
        price = 1;

    }

    @OnClick(R.id.buy_600pp)
    public void onBuy600Credits() {
        credit = 600;
        price = 5;

    }

    @OnClick(R.id.buy_1400pp)
    public void onBuy1400Credits() {
        credit = 1400;
        price = 10;

    }

    @OnClick(R.id.buy_3000pp)
    public void onBuy3000Credits() {
        credit = 3000;
        price = 20;

    }

    @OnClick(R.id.buy_10000pp)
    public void onBuy10000Credits() {
        credit = 10000;
        price = 50;

    }

//
//    public void buyCredit() {
//        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<BuyCredit> call = apiInterface.buyPalupCredits(Constants.SECRET_KEY, Data.userId, credit,price);
//        call.enqueue(new Callback<BuyCredit>() {
//            @Override
//            public void onResponse(Call<BuyCredit> call, Response<BuyCredit> response) {
//                BuyCredit userCredit = response.body();
//                if (userCredit.getStatus().equals("success")) {
//                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
//                    LayoutInflater inflater = getActivity().getLayoutInflater();
//                    View dialogView = inflater.inflate(R.layout.common_lottie_animation_layout, null);
//                    dialogBuilder.setView(dialogView);
//                    final AlertDialog alertDialog = dialogBuilder.create();
//                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    alertDialog.show();
//
//                    Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) + credit);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(), "Congratulations, You have bought " + credit + "Credits", Toast.LENGTH_SHORT).show();
//                            alertDialog.dismiss();
//                        }
//                    }, 2000);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BuyCredit> call, Throwable t) {
//                Log.d("BuyCreditsFragment", t.getMessage());
//            }
//        });
//    }

}
