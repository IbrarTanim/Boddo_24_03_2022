package net.boddo.btm.Adepter.othersuser;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import net.boddo.btm.Model.PojoClass;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;

public class OthersProfileInfoAdapter extends RecyclerView.Adapter<OthersProfileInfoAdapter.OtherProfileInfoHolder> {

    Context context;
    List<PojoClass> pojoClassList;
    public static String[] otherListItem;
    public boolean[] otherCheckedItem;
    public static ArrayList<Integer> otherUserLanguage = new ArrayList<>();

    public OthersProfileInfoAdapter(Context context, List<PojoClass> pojoClassList) {
        this.context = context;
        this.pojoClassList = pojoClassList;
    }


    @NonNull
    @Override
    public OthersProfileInfoAdapter.OtherProfileInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_details_row_item_others, parent, false);
        return new OtherProfileInfoHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OthersProfileInfoAdapter.OtherProfileInfoHolder holder, int position) {
        if (position == 0 && !Data.otherUserAboutMe.equals("")) {
            holder.description.setText(Data.otherUserAboutMe);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 0 && Data.otherUserAboutMe.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);

        }

        if (position == 1 && !Data.otherUserHomeTown.equals("")) {
            holder.description.setText(Data.otherUserHomeTown);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 1 && Data.otherUserHomeTown.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 2 && !Data.otherUserRelationShip.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
            holder.description.setText(Data.otherUserRelationShip);
        } else if (position == 2 && Data.otherUserRelationShip.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 3 && !Data.otherUserLookingFor.equals("")) {
            holder.description.setText(Data.otherUserLookingFor);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 3 && Data.otherUserLookingFor.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 4 && !Data.otherUserEducation.equals("")) {
            holder.description.setText(Data.otherUserEducation);

            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 4 && Data.otherUserEducation.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 5 && !Data.otherUserProfession.equals("")) {
            holder.description.setText(Data.otherUserProfession);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);

            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 5 && Data.otherUserProfession.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 6 && !Data.otherUserLanguage.equals("")) {
            holder.description.setText(Data.otherUserLanguage);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);

            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 6 && Data.otherUserLanguage.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 7 && !Data.otherUserHairColor.equals("")) {
            holder.description.setText(Data.otherUserHairColor);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);

            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 7 && Data.otherUserHairColor.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 8 && !Data.otherUserEyeColor.equals("")) {
            holder.description.setText(Data.otherUserEyeColor);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);

            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 8 && Data.otherUserEyeColor.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 9 && !Data.doOtherUserSmooke.equals("")) {
            holder.description.setText(Data.doOtherUserSmooke);
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);

            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 9 && Data.doOtherUserSmooke.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
        if (position == 10 && !Data.otherUserHeight.equals("")) {
            holder.description.setText(Data.otherUserHeight + "cm");
            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);

            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 10 && Data.otherUserHeight.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }


        if (position == 11 && !Data.otherUserHabits.equals("")) {
            holder.description.setText(Data.otherUserHabits);

            holder.aboutLayoutOthers.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(pojoClassList.get(position).getIconImage());
            holder.title.setText(pojoClassList.get(position).getTitle());
        } else if (position == 11 && Data.otherUserHabits.equals("")) {
            holder.aboutLayoutOthers.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return pojoClassList.size();
    }

    public class OtherProfileInfoHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        //ImageView imageView;
        RelativeLayout aboutLayoutOthers;

        public OtherProfileInfoHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_add_description);
            //imageView = itemView.findViewById(R.id.image_view_icon);
            aboutLayoutOthers = itemView.findViewById(R.id.aboutLayoutOthers);

        }
    }
}


