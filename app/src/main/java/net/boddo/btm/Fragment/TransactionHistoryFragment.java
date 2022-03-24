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
import net.boddo.btm.Adepter.TransactionAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Model.Transaction;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import retrofit2.Call;
import retrofit2.Callback;


public class TransactionHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    TextView messageView;
    ApiInterface apiInterface;
    public TransactionHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        recyclerView = view.findViewById(R.id.transactionHistory_recyclerView);
        messageView = view.findViewById(R.id.not_Transaction_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        networkCall();
        return view;
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

    private void networkCall() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Transaction[]> call = apiInterface.getAllTransaction(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<Transaction[]>() {
            @Override
            public void onResponse(Call<Transaction[]> call, retrofit2.Response<Transaction[]> response) {
                Transaction[] transactions = response.body();
                if (transactions.length == 0) {
                    recyclerView.setVisibility(View.GONE);
                    messageView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    messageView.setVisibility(View.GONE);
                }
                recyclerView.setAdapter(new TransactionAdapter(getContext(), transactions));
            }

            @Override
            public void onFailure(Call<Transaction[]> call, Throwable t) {
                Log.d(TransactionHistoryFragment.class.getSimpleName(),t.getMessage());
            }
        });
    }

}
