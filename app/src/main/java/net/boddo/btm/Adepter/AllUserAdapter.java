package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import net.boddo.btm.Model.AllUser;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SearchUser;

import java.util.Calendar;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.viewHolder> {


    AllUser[] allUsers;
    Context context;
    private long mLastClickTime = 0;

    String age;
    int date = 0;
    int month = 0;
    int year = 0;

    public AllUserAdapter(AllUser[] allUsers, Context context) {
        this.allUsers = allUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_all_user, parent, false);

        return new viewHolder(view);
    }


    private void convertStringToDateFormat(String stringDate) {
        String[] stringArray = stringDate.split("/");
        //String[] stringArray = stringDate.split("/");
        date = Integer.parseInt(stringArray[0]);
        month = Integer.parseInt(stringArray[1]);
        year = Integer.parseInt(stringArray[2]);

        /*date = Integer.parseInt(stringArray[0]);
        month = Integer.parseInt(stringArray[1]);
        year = Integer.parseInt(stringArray[2]);*/

    }


    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = String.valueOf(ageInt);
        return ageS;
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        int positionLocal = position;

        if (!allUsers[position].getDateOfBirth().equals("")) {
            convertStringToDateFormat(allUsers[positionLocal].getDateOfBirth());
            age = getAge(year, month, date);
            holder.description.setText(age);

        }

        Glide.with(holder.profileImage.getContext()).load(allUsers[positionLocal].getProfilePhoto()).into(holder.profileImage);
        String name = allUsers[positionLocal].getFirstName() + ", @" + allUsers[positionLocal].getUserName();

        holder.fullName.setText(allUsers[positionLocal].getFirstName());

        if (allUsers[positionLocal].getGender().equals("Female")) {
            holder.ivMaleFemale.setImageResource(R.drawable.female_icon_30_3_2021);
        } else {
            holder.ivMaleFemale.setImageResource(R.drawable.male_icon_30_3_2021);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                final SearchUser searchUser = new SearchUser(context);
                Data.otherUserId = allUsers[positionLocal].getUserId();
                /*Data.pd = new ProgressDialog(context);
                Data.pd.setTitle("Loading...");
                Data.pd.setMessage("Please wait for a while...");
                Data.pd.show();*/
                searchUser.searchUserInfo();

            }
        });
    }

    @Override
    public int getItemCount() {
        return allUsers.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        RoundedImageView profileImage;
        TextView fullName, description;
        ImageView ivMaleFemale;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image);
            fullName = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            ivMaleFemale = itemView.findViewById(R.id.ivMaleFemale);
        }
    }
}
