package net.boddo.btm.Fragment.OthersFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.OthersProfileOneActivity;
import net.boddo.btm.Activity.ProfileOneActivity;
import net.boddo.btm.Adepter.othersuser.OthersProfileInfoAdapter;
import net.boddo.btm.Model.PojoClass;
import net.boddo.btm.R;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileInfoFragment extends Fragment {

    public OtherProfileInfoFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    List<PojoClass> aboutInfo;
    ImageView ivBackProfileActivity;


    private PojoClass pojoClass;
    OthersProfileInfoAdapter profileInfoAdapter;

    private Parcelable recyclerViewState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_profile_info, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        ivBackProfileActivity = view.findViewById(R.id.ivBackProfileActivity);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        getAllItemList();
        profileInfoAdapter = new OthersProfileInfoAdapter(getContext(), aboutInfo);
        recyclerView.setAdapter(profileInfoAdapter);

        ivBackProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OthersProfileOneActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        return view;
    }

    private List<PojoClass> getAllItemList() {
        aboutInfo = new ArrayList<PojoClass>();
        aboutInfo.add(new PojoClass(R.drawable.ic_about_me, "ABOUT ME"));
        aboutInfo.add(new PojoClass(R.drawable.ic_profile_location, "LOCATION"));
        aboutInfo.add(new PojoClass(R.drawable.ic_relationship, "RELATIONSHIP"));
        aboutInfo.add(new PojoClass(R.drawable.ic_looking_for, "LOOKING FOR"));
        aboutInfo.add(new PojoClass(R.drawable.ic_eduction, "EDUCATION"));
        aboutInfo.add(new PojoClass(R.drawable.ic_profesion, "PROFESSION"));
        aboutInfo.add(new PojoClass(R.drawable.ic_languages, "LANGUAGE"));
        aboutInfo.add(new PojoClass(R.drawable.ic_hair_color, "HAIR COLOR"));
        aboutInfo.add(new PojoClass(R.drawable.ic_eye_color, "EYE COLOR"));
        aboutInfo.add(new PojoClass(R.drawable.ic_smoking, "SMOKING"));
        aboutInfo.add(new PojoClass(R.drawable.ic_height, "HEIGHT"));
        aboutInfo.add(new PojoClass(R.drawable.ic_hobbies, "HOBBIES"));
        return aboutInfo;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setRetainInstance(true);
    }
}
