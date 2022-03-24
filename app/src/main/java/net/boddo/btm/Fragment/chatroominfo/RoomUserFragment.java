package net.boddo.btm.Fragment.chatroominfo;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.chatroom.ChatRoomAdminAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRoomUserInfo;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomUserFragment extends Fragment {


    public RoomUserFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ChatRoomUserInfo[] list;

    ChatRoomAdminAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room_user, container, false);
        ButterKnife.bind(this,view);
        if (!Data.chatRoomShowAdminAndUser.equals("")){
            getAllAdminUserList(Data.chatRoomShowAdminAndUser);
        }
        return view ;
    }


    private void getAllAdminUserList(String chatRoomShowAdminAndUser) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoomUserInfo[]> call = apiInterface.getAllChatRoomUserInfo(Constants.SECRET_KEY,Data.chatRoomShowAdminAndUser,Data.userId,"user");
        call.enqueue(new Callback<ChatRoomUserInfo[]>() {
            @Override
            public void onResponse(Call<ChatRoomUserInfo[]> call, Response<ChatRoomUserInfo[]> response) {
                list = response.body();
                adapter = new ChatRoomAdminAdapter(getActivity(),list);
                linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ChatRoomUserInfo[]> call, Throwable t) {
                Log.d(AdminUserFragment.class.getSimpleName(), t.getMessage());
            }
        });

    }

}
