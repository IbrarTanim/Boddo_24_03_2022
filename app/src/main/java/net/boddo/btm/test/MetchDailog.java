package net.boddo.btm.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.squareup.picasso.Picasso;

import net.boddo.btm.R;

public class MetchDailog extends AppCompatDialogFragment {

    ImageView profileImage,otherProfiles;
    TextView messageTV;
    Button letsChatButton,cencel;

    String messages;
    String otherPhoto;
    String profilePhoto;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.matching_model,null);
        onDailodCreat(view);
        messageTV.setText(messages);
        Picasso.get().load(profilePhoto).into(profileImage);
        Picasso.get().load(otherPhoto).into(otherProfiles);
        return  builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    public void onDailodCreat(View view){
        messageTV = view.findViewById(R.id.text_view_message);
        profileImage = view.findViewById(R.id.user_profile_photo);
        otherProfiles = view.findViewById(R.id.other_user_profile_photo);
        letsChatButton = view.findViewById(R.id.chat_button);
        cencel = view.findViewById(R.id.cancel_button);


    }

}
