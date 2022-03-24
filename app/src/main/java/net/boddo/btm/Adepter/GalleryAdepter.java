package net.boddo.btm.Adepter;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.PrivateChatActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatAppMsgDTO;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryAdepter extends RecyclerView.Adapter<GalleryAdepter.GalleryHolder> {

    Context context;
    String photoLink;
    ApiInterface apiInterface;
    private UserPhotoBlogImages[] userPhotoBlogImagesList;
    ChatAppMsgDTO.Message imageUrl;
    LocalBroadcastManager broadcaster;

    private long mLastClickTime = 0;

    public GalleryAdepter(Context context, UserPhotoBlogImages[] userPhotoBlogImagesList) {
        this.context = context;
        this.userPhotoBlogImagesList = userPhotoBlogImagesList;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_model, parent, false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryHolder holder, final int position) {

        Picasso.get().load(userPhotoBlogImagesList[position].getPhoto()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                photoLink = userPhotoBlogImagesList[position].getPhoto();
                uploadImageToServer();
            }
        });
    }


    @Override
    public int getItemCount() {
        return userPhotoBlogImagesList.length;
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_gallery);
        }
    }

    public void uploadImageToServer() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatAppMsgDTO> call = apiInterface.startChat(Constants.SECRET_KEY, Data.userId, Data.otherUserId, photoLink);
        call.enqueue(new Callback<ChatAppMsgDTO>() {
            @Override
            public void onResponse(Call<ChatAppMsgDTO> call, Response<ChatAppMsgDTO> response) {
                if (response.body().getStatus().equals("success")) {
                    if (response.body().getRequest().equals("accepted")) {
                        imageUrl = response.body().getSingleMessage();
                        PrivateChatActivity.msgDtoList.add(imageUrl);
                        onImageSentSuccessfull();
                    } else if (response.body().getRequest().equals("requested")) {
                        if (PrivateChatActivity.msgDtoList.size() == 1) {
                            Toast.makeText(context, "Message request sent but not accepted", Toast.LENGTH_SHORT).show();
                        } else {
                            imageUrl = response.body().getSingleMessage();
                            PrivateChatActivity.msgDtoList.add(imageUrl);
                            onImageSentSuccessfull();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatAppMsgDTO> call, Throwable t) {
                Log.d(GalleryAdepter.class.getSimpleName(), t.getMessage());
            }
        });
    }

    public void onImageSentSuccessfull() {
        broadcaster = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(Constants.REQUEST_FOR_PRIVATE_MESSAGE_IMAGE_UPLOAD_SUCCESS);
        intent.putExtra(Constants.UPDATE_UI_FOR_CHAT, "imageUploaded");
        broadcaster.sendBroadcast(intent);
    }
}
