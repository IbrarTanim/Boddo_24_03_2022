package net.boddo.btm.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.R;
import net.boddo.btm.Model.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    Context context;
    Transaction[] transactions;
    private String credits, timeDate, date;

    private String bonus = "Bonus";
    private String refund = "Refund";

    public TransactionAdapter(Context context, Transaction[] transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transaction_model, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position) {

        timeDate = transactions[position].getCreatedAt();
        credits = transactions[position].getItem();
        String[] parts = timeDate.split(" ");
        date = parts[0];

        holder.title.setText(transactions[position].getItem());
        if (credits.contains(bonus) || credits.contains(refund)) {
            holder.details.setText("+ " + transactions[position].getCost() + " credits , Date: " + date);
            holder.details.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.details.setText("- " + transactions[position].getCost() + " credits , Date: " + date);
            holder.details.setTextColor(context.getResources().getColor(R.color.red_A700));
        }


    }

    @Override
    public int getItemCount() {
        return transactions.length;
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
