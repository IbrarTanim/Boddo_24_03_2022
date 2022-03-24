package net.boddo.btm.Adepter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.ProfileOneActivity;
import net.boddo.btm.Model.SelectStatusModel;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

import java.util.List;

public class SelectStatusAdepter extends RecyclerView.Adapter<SelectStatusAdepter.SelectStatusViewHolder> {

    private Context context;
    private List<SelectStatusModel> selectStatusModels;

    public SelectStatusAdepter(Context context, List<SelectStatusModel> selectStatusModels) {
        this.context = context;
        this.selectStatusModels = selectStatusModels;
    }

    @Override
    public SelectStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.select_status_row_item,parent,false);
        return new SelectStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectStatusAdepter.SelectStatusViewHolder holder, int position) {

        String status = selectStatusModels.get(position).getStatus();
        holder.tvSelectStatusRowItem.setText(status);


        holder.tvSelectStatusRowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String name = selectStatusModels.get(position).getStatus();
                //String pos = String.valueOf(position);

                AboutUpdate obj = new AboutUpdate(context);
                obj.updateAbout("moto", status);
                Data.userMoto = status;

                Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), ProfileOneActivity.class);
                //intent.putExtra("name",name);
                //intent.putExtra("position",position);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectStatusModels.size();
    }

    class SelectStatusViewHolder extends RecyclerView.ViewHolder {

        TextView tvSelectStatusRowItem;
        public SelectStatusViewHolder(View itemView) {
            super(itemView);

            tvSelectStatusRowItem = itemView.findViewById(R.id.tvSelectStatusRowItem);
        }
    }
}
