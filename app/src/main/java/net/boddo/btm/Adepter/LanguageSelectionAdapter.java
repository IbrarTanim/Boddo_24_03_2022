package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import net.boddo.btm.Model.AllUser;
import net.boddo.btm.Model.LanguageSelection;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ItemOnClickListener;
import net.boddo.btm.Utills.SearchUser;

import java.util.ArrayList;
import java.util.Calendar;

public class LanguageSelectionAdapter extends RecyclerView.Adapter<LanguageSelectionAdapter.viewHolder> {

    ArrayList<LanguageSelection> languageSelectionList;
    Context context;
    ItemOnClickListener itemOnClickListener;


    public LanguageSelectionAdapter(ArrayList<LanguageSelection> languageSelectionList, Context context, ItemOnClickListener itemOnClickListener) {

        this.languageSelectionList = languageSelectionList;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cell_language_selection, parent, false);

        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        //holder.tvLanguageName.setText(languageSelectionList.get(position).getLanguageName());
        holder.languageCheckBox.setText(languageSelectionList.get(position).getLanguageName());

        holder.llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llLanguage.setBackgroundColor(ContextCompat.getColor(context, R.color.app_color));
                holder.tvLanguageName.setTextColor(ContextCompat.getColor(context, R.color.white));
                Toast.makeText(context, "languages", Toast.LENGTH_SHORT).show();

                String item = "";
                for (int i = 0; i < languageSelectionList.size(); i++) {
                    item = item + languageSelectionList.get(i).getLanguageName();
                    if (i != languageSelectionList.size() - 1) {
                        item = item + ",";
                    }
                }
                AboutUpdate obj = new AboutUpdate(context);
                obj.updateAbout("language", item);
                Data.userLanguage = item;
                notifyDataSetChanged();
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return languageSelectionList == null ? 0 : languageSelectionList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView tvLanguageName;
        LinearLayout llLanguage;
        CheckBox languageCheckBox;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvLanguageName = itemView.findViewById(R.id.tvLanguageName);
            llLanguage = itemView.findViewById(R.id.llLanguage);
            languageCheckBox = itemView.findViewById(R.id.languageCheckBox);

            languageCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.OnClick(v,getAdapterPosition(),false);
                }
            });


        }
    }
}
