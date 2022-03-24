package net.boddo.btm.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.PurchaseAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;

import net.boddo.btm.Model.PalupBuyCredits;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    ApiInterface apiInterface;
    TextView messageView;


    public PurchaseHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_history, container, false);

        messageView = view.findViewById(R.id.not_Purchase_view);
        recyclerView = view.findViewById(R.id.purchase_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        networkCall();
        return view;
    }

    private void networkCall() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PalupBuyCredits[]> call = apiInterface.getAllPurchase(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<PalupBuyCredits[]>() {
            @Override
            public void onResponse(Call<PalupBuyCredits[]> call, Response<PalupBuyCredits[]> response) {
                PalupBuyCredits[] purchase = response.body();
                if (purchase.length == 0){
                    recyclerView.setVisibility(View.GONE);
                    messageView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    messageView.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(new PurchaseAdapter(getActivity(), purchase));
            }

            @Override
            public void onFailure(Call<PalupBuyCredits[]> call, Throwable t) {
                Log.d(PurchaseHistoryFragment.class.getSimpleName(),t.getMessage());
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.getEventType().equals("TransectionCall")) {
            networkCall();
        }

    }

}
