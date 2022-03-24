package net.boddo.btm.Fragment.chatroominfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.boddo.btm.Adepter.GlobalChatroomAdapter;
import net.boddo.btm.Model.ChatRoomUserInfo;
import net.boddo.btm.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomMainFragment extends DialogFragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private GlobalChatroomAdapter adapter;
    List<ChatRoomUserInfo> list ;
    public ChatRoomMainFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_room_main, container, false);
        ButterKnife.bind(this,view);
        adapter = new GlobalChatroomAdapter(getChildFragmentManager());
        adapter.addFragment(new AdminUserFragment(), "Admin");
        adapter.addFragment(new RoomUserFragment(), "Room User");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
