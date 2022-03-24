package net.boddo.btm.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Model.PalupBuyCredits;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Helper;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.TransactionViewHolder> {

    Context context;
    PalupBuyCredits[] purchases;
    String credits,timeDate, date, time;

    public PurchaseAdapter(Context context, PalupBuyCredits[] purchases) {
        this.context = context;
        this.purchases = purchases;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_transaction_model, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.TransactionViewHolder holder, int position) {
        credits = purchases[position].getItem();
        String[] date = purchases[position].getCreatedAt().split(" ");

        if (credits.contains("subscription")){
            holder.title.setText("Bought subscription");
            if (credits.contains("6")){
                holder.details.setText("6 months subscription"+ ". Date: "+  Helper.getLastActionTime(purchases[position].getCreatedAt()));
            }else if (credits.contains("3")){
                holder.details.setText("3 months subscription"+ ". Date: "+  Helper.getLastActionTime(purchases[position].getCreatedAt()));
            }else {
                holder.details.setText("1 month subscription"+ ". Date: "+  Helper.getLastActionTime(purchases[position].getCreatedAt()));
            }
        }else if (credits.contains("com.palup_credits")){
            holder.title.setText( "Bought "+purchases[position].getPurchasedPp() +" Credits");
            holder.details.setText("+ "+purchases[position].getPurchasedPp()+ " Credits. Date: "+ Helper.getLastActionTime(purchases[position].getCreatedAt()));
        }
    }
    @Override
    public int getItemCount() {
        return purchases.length;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView title, details;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_textView);
            details = itemView.findViewById(R.id.details_textView);
        }
    }
}
