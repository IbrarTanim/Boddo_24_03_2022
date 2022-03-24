package net.boddo.btm.Adepter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.about.AboutMeActivity;
import net.boddo.btm.Activity.about.EducationActivity;
import net.boddo.btm.Activity.about.EyeColorActivity;
import net.boddo.btm.Activity.about.HairColorActivity;
import net.boddo.btm.Activity.about.HeightActivity;
import net.boddo.btm.Activity.about.HobbiesActivity;
import net.boddo.btm.Activity.about.LanguageActivity;
import net.boddo.btm.Activity.about.LocationActivity;
import net.boddo.btm.Activity.about.LookingForActivity;
import net.boddo.btm.Activity.about.ProfessionActivity;
import net.boddo.btm.Activity.about.RelationshipActivity;
import net.boddo.btm.Activity.about.SmokingActivity;
import net.boddo.btm.Model.PojoClass;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

import java.util.ArrayList;
import java.util.List;

public class ProfileInfoAdapter extends RecyclerView.Adapter<ProfileInfoAdapter.ProfileViewHolder> {

    Context context;
    List<PojoClass> pojoClassList;
    public static String[] listItem;
    public boolean[] checkedItem;
    public static ArrayList<Integer> mUserItem;

    public ProfileInfoAdapter(Context context, List<PojoClass> pojoClassList) {
        this.context = context;
        this.pojoClassList = pojoClassList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_details_row_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {

        holder.description.setVisibility(View.VISIBLE);
        holder.title.setVisibility(View.VISIBLE);
        holder.layout.setVisibility(View.VISIBLE);

        holder.title.setText(pojoClassList.get(position).getTitle());

        if (position == 0 && !Data.userAboutMe.equals("")) {
            holder.description.setText(Data.userAboutMe);
        } else if (position == 0 && Data.userAboutMe.equals("")) {
            holder.description.setText("Write something about you");
        }
        if (position == 1 && !Data.userHomeTown.equals("")) {
            holder.description.setText(Data.userHomeTown);
        } else if (position == 1 && Data.userHomeTown.equals("")) {
            holder.description.setText("Where are you living?");
        }
        if (position == 2 && !Data.userRelationShip.equals("")) {
            holder.description.setText(Data.userRelationShip);
        } else if (position == 2 && Data.userRelationShip.equals("")) {
            holder.description.setText("What is your relationship status?");
        }
        if (position == 3 && !Data.userLookingFor.equals("")) {
            holder.description.setText(Data.userLookingFor);
        } else if (position == 3 && Data.userLookingFor.equals("")) {
            holder.description.setText("What are you looking for?");
        }
        if (position == 4 && !Data.userEducation.equals("")) {
            holder.description.setText(Data.userEducation);
        } else if (position == 4 && Data.userEducation.equals("")) {
            holder.description.setText("What is your educational background?");
        }
        if (position == 5 && !Data.userProfession.equals("")) {
            holder.description.setText(Data.userProfession);
        } else if (position == 5 && Data.userProfession.equals("")) {
            holder.description.setText("What is your profession?");
        }
        if (position == 6 && !Data.userLanguage.equals("")) {
            holder.description.setText(Data.userLanguage);
        } else if (position == 6 && Data.userLanguage.equals("")) {
            holder.description.setText("How many languages you speak?");
        }
        if (position == 7 && !Data.userHairColor.equals("")) {
            holder.description.setText(Data.userHairColor);
        } else if (position == 7 && Data.userHairColor.equals("")) {
            holder.description.setText("What is your hair color?");
        }
        if (position == 8 && !Data.userEyeColor.equals("")) {
            holder.description.setText(Data.userEyeColor);
        } else if (position == 8 && Data.userEyeColor.equals("")) {
            holder.description.setText("What is your eye color?");
        }
        if (position == 9 && !Data.doUserSmooke.equals("")) {
            holder.description.setText(Data.doUserSmooke);
        } else if (position == 9 && Data.doUserSmooke.equals("")) {
            holder.description.setText("Do you a smoker?");
        }
        if (position == 10 && !Data.userHeight.equals("")) {
            holder.description.setText(Data.userHeight + "cm");
        } else if (position == 10 && Data.userHeight.equals("")) {
            holder.description.setText("How tall are you?");
        }
        if (position == 11 && !Data.userHabits.equals("")) {
            holder.description.setText(Data.userHabits);
        } else if (position == 11 && Data.userHabits.equals("")) {
            holder.description.setText("What are your hobbies?");
        }
    }

    @Override
    public int getItemCount() {
        return pojoClassList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView title, description;
        RelativeLayout layout;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_add_description);
            layout = itemView.findViewById(R.id.about_layout);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == 0) {//about me
                context.startActivity(AboutMeActivity.newIntent(context));



                /*final Dialog dialog = new Dialog(context);
                dialog.setTitle("About Me");
                dialog.setContentView(R.layout.custom_alert_dialog);
                final EditText editTextAbout = dialog.findViewById(R.id.edit_text_about);
                if (!Data.userAboutMe.equals("")) {
                    editTextAbout.setText(Data.userAboutMe);
                }
                Button cancelAbout = (Button) dialog.findViewById(R.id.cancel_about);
                final Button saveAbout = dialog.findViewById(R.id.save_about);
                final TextView counter = dialog.findViewById(R.id.counter);
                editTextAbout.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count >= 2 && count <= 250) {
                            saveAbout.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        counter.setText(String.valueOf(s.length()));
                    }
                });
                cancelAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                saveAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdate obj = new AboutUpdate(context);
                        obj.updateAbout("about", editTextAbout.getText().toString());
                        Data.userAboutMe = editTextAbout.getText().toString();
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                });
                dialog.show();
                */

            } else if (getAdapterPosition() == 1) {  //Location
                context.startActivity(LocationActivity.newIntent(context));

                /*final Dialog dialog = new Dialog(context);
                dialog.setTitle("Location");
                dialog.setContentView(R.layout.custom_location_dialog);
                final EditText editTextAbout = dialog.findViewById(R.id.edit_text_location);
                if (!Data.userHomeTown.equals("")) {
                    editTextAbout.setText(Data.userHomeTown);
                }
                Button cancelAbout = (Button) dialog.findViewById(R.id.cancel_about);
                final Button saveAbout = dialog.findViewById(R.id.save_about);
                final TextView counter = dialog.findViewById(R.id.counter);
                editTextAbout.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count >= 2 && count <= 50) {
                            saveAbout.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        counter.setText(String.valueOf(s.length()));
                    }
                });
                cancelAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                saveAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdate obj = new AboutUpdate(context);
                        obj.updateAbout("hometown", editTextAbout.getText().toString());
                        Data.userHomeTown = editTextAbout.getText().toString();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();*/


            } else if (getAdapterPosition() == 2) { //relationship
                context.startActivity(RelationshipActivity.newIntent(context));

            } else if (getAdapterPosition() == 3) { //looking for
                context.startActivity(LookingForActivity.newIntent(context));

            } else if (getAdapterPosition() == 4) {  //education
                context.startActivity(EducationActivity.newIntent(context));
            } else if (getAdapterPosition() == 5) { //profession
                context.startActivity(ProfessionActivity.newIntent(context));

            } else if (getAdapterPosition() == 6) { //language
                context.startActivity(LanguageActivity.newIntent(context));






                listItem = context.getResources().getStringArray(R.array.language);
                checkedItem = new boolean[listItem.length];
                mUserItem = new ArrayList<>();
               /* final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Spoken languages");
                if (Data.getLanguages() != null && Data.getLanguages().length > 0) {
                    for (int i = 0; i < Data.getLanguages().length; i++) {
                        for (int j = 0; j < listItem.length; j++) {
                            if (listItem[j].equals(Data.getLanguages()[i])) {
                                mUserItem.add(j);
                                break;
                            }
                        }
                    }
                }
                if (mUserItem.size() > 0) {
                    for (int i = 0; i < mUserItem.size(); i++) {
                        checkedItem[mUserItem.get(i)] = true;
                    }
                }
                builder.setMultiChoiceItems(listItem, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserItem.contains(position)) {
                                mUserItem.add(position);
                            }
                        } else {
                            if (mUserItem.contains(position)) {
                                for (int i = 0; i < listItem.length; i++) {
                                    if (mUserItem.get(i) == position) {
                                        mUserItem.remove(i);
                                        notifyDataSetChanged();
                                        break;
                                    }
                                }
                                notifyDataSetChanged();
                            }
                        }

                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItem.size(); i++) {
                            item = item + listItem[mUserItem.get(i)];
                            if (i != mUserItem.size() - 1) {
                                item = item + ",";
                            }
                        }
                        AboutUpdate obj = new AboutUpdate(context);
                        obj.updateAbout("language", item);
                        Data.userLanguage = item;
                        notifyDataSetChanged();
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                //alertDialog.show();*/


            } else if (getAdapterPosition() == 7) {//hair color
                context.startActivity(HairColorActivity.newIntent(context));
            } else if (getAdapterPosition() == 8) { //eye color
                context.startActivity(EyeColorActivity.newIntent(context));
            } else if (getAdapterPosition() == 9) { //smoking
                context.startActivity(SmokingActivity.newIntent(context));
            } else if (getAdapterPosition() == 10) {//height
                context.startActivity(HeightActivity.newIntent(context));


                /*final Dialog dialog = new Dialog(context);
                dialog.setTitle("Height");
                dialog.setContentView(R.layout.custom_height_dialog);
                final TextView title = dialog.findViewById(R.id.title);
                title.setText("Height");
                final EditText editTextHeight = dialog.findViewById(R.id.edit_text_height);
                if (!Data.userHeight.equals("")) {
                    editTextHeight.setText(Data.userHeight);
                }
                Button cancelAbout = (Button) dialog.findViewById(R.id.cancel_about);
                final Button saveAbout = dialog.findViewById(R.id.save_about);
                final TextView counter = dialog.findViewById(R.id.counter);
                editTextHeight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0 && s.length() <= 6) {
                            saveAbout.setEnabled(true);
                        }
                        counter.setText(String.valueOf(s.length()));
                    }
                });
                cancelAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                saveAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdate obj = new AboutUpdate(context);
                        obj.updateAbout("height", editTextHeight.getText().toString());
                        Data.userHeight = editTextHeight.getText().toString();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();

                */


            } else if (getAdapterPosition() == 11) { //hobbies
                context.startActivity(HobbiesActivity.newIntent(context));

                /*final Dialog dialog = new Dialog(context);
                dialog.setTitle("Hobbies");
                dialog.setContentView(R.layout.custom_hobbies_dialog);
                final TextView title = dialog.findViewById(R.id.title);
                title.setText("Hobbies");
                final EditText editTextHobbies = dialog.findViewById(R.id.edit_text_hobbies);
                if (!Data.userHabits.equals("")) {
                    editTextHobbies.setText(Data.userHabits);
                }
                Button cancelAbout = (Button) dialog.findViewById(R.id.cancel_about);
                final Button saveAbout = dialog.findViewById(R.id.save_about);
                final TextView counter = dialog.findViewById(R.id.counter);
                editTextHobbies.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() >= 1 && count <= 50) {
                            saveAbout.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        counter.setText(String.valueOf(s.length()));
                    }
                });
                cancelAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                saveAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdate obj = new AboutUpdate(context);
                        obj.updateAbout("habits", editTextHobbies.getText().toString());
                        Data.userHabits = editTextHobbies.getText().toString();
                        notifyDataSetChanged();
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();*/
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
