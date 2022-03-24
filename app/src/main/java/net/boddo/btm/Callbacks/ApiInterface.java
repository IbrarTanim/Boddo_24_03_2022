package net.boddo.btm.Callbacks;

import java.util.List;

import net.boddo.btm.Model.ActiveChat;
import net.boddo.btm.Model.BlockData;
import net.boddo.btm.Model.Blocklist;
import net.boddo.btm.Model.AllComments;
import net.boddo.btm.Model.ChatAppMsgDTO;
import net.boddo.btm.Model.ChatRequest;
import net.boddo.btm.Model.ChatRoom;
import net.boddo.btm.Model.ChatRoomMessage;
import net.boddo.btm.Model.ChatRoomUser;
import net.boddo.btm.Model.ChatRoomUserInfo;
import net.boddo.btm.Model.CheckUserAlreadyValidInGlobalChatRoom;
import net.boddo.btm.Model.Favorite;
import net.boddo.btm.Model.Hotlist;
import net.boddo.btm.Model.Likes;
import net.boddo.btm.Model.Liked;
import net.boddo.btm.Model.Love;
import net.boddo.btm.Model.OtherUser;
import net.boddo.btm.Model.PalupBuyCredits;
import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.ProfileUser;
import net.boddo.btm.Model.RecentMatchModel;
import net.boddo.btm.Model.RoomFriendMessageClass;
import net.boddo.btm.Model.Transaction;
import net.boddo.btm.Model.User;
import net.boddo.btm.Model.UserPhotoBlogImages;

import net.boddo.btm.Utills.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("update_fcm_token.php")
    Call<String> updateFCMToken(@Field("token") String token,
                                @Field("secret_key") int secret_key);


    @FormUrlEncoded
    @POST("sign_up_api.php")
    Call<Pojo> storeNewUser(@Field("user_name") String user_name,
                            @Field("email") String email,
                            @Field("first_name") String first_name,
                            @Field("password") String password,
                            @Field("gender") String gender,
                            @Field("date_of_birth") String date_of_birth,
                            @Field("secret_key") int secret_key,
                            @Field("access_token") String access_token,
                            @Field("fcm_token") String fcmToken,
                            @Field("user_agent") String userAgent,
                            @Field("android_id") String androidID
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<Pojo> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("secret_key") int secret_key,
            @Field("access_token") String token,
            @Field("fcm_token") String fcmToken,
            @Field("user_agent") String userAgent
    );

    @FormUrlEncoded
    @POST("logout.php")
    Call<String> logout(@Field("user_id") String userId,
                        @Field("secret_key") int secret_key);


    @FormUrlEncoded
    @POST("check_user_exist.php")
    Call<String> checkUserNameExist(@Field("user_name") String user_name,
                                    @Field("secret_key") int secret_key);

    @FormUrlEncoded
    @POST("check_user_exist.php")
    Call<String> checkUserEmailExist(@Field("email") String user_name,
                                     @Field("secret_key") int secret_key);

    @FormUrlEncoded
    @POST("auto_login.php")
    Call<Pojo> autoLogin(@Field("user_id") String user_id,
                         @Field("access_token") String token,
                         @Field("secret_key") int secret_key,
                         @Field("fcm_token") String fcmToken
    );

    @FormUrlEncoded
    @POST("upload_image.php")
    Call<ProfileImageLoader> uploadFile(@Field("image") String image,
                                        @Field("image_name") String imageTitle,
                                        @Field("secret_key") int secret_key,
                                        @Field("user_id") String user_id,
                                        @Field("serial") int serial_of_image);

    @FormUrlEncoded
    @POST("delete_image.php")
    Call<ProfileImageLoader> deletePhoto(@Field("secret_key") int secret_key,
                                         @Field("user_id") String user_id,
                                         @Field("serial") int serial_of_image);

    @FormUrlEncoded
    @POST("all_images.php")
    Call<ProfileImageLoader> allPhotos(@Field("user_id") String user_id,
                                       @Field("secret_key") int secret_key);

    @FormUrlEncoded
    @POST("delete_image.php")
    Call<Void> onImageDelete(@Field("user_id") String user_id,
                             @Field("secret_key") int secret_key,
                             @Field("serial") int serial_of_image);


    @FormUrlEncoded
    @POST("about.php")
    Call<User> updateAbout(@Field("user_id") String user_id,
                           @Field("secret_key") int secret_key,
                           @Field("key") String key,
                           @Field("value") String value);


    @FormUrlEncoded
    @POST("profile-update.php")
    Call<User> editProfile(@Field("secret_key") int secret_key,
                             @Field("key") String key,
                             @Field("value") String value,
                             @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("self-close-account.php")
    Call<User> closeAccount(@Field("user_id") String user_id,
                            @Field("secret_key") int secret_key);

    @FormUrlEncoded
    @POST("all_room_admins.php")
    Call<ProfileUser> updateProfile(@Field("secret_key") int secret_key);




    //other user finding api
    @FormUrlEncoded
    @POST("search.php")
    Call<Pojo> searchOtherUser(@Field("user_id") String user_id,
                               @Field("other_user_id") String otherUserID,
                               @Field("secret_key") int secret_key);

    // giving favorit to other user
    @FormUrlEncoded
    @POST("love.php")
    Call<Love> selectLove(@Field("user_id") String user_id,
                          @Field("other_user_id") String other_user_id,
                          @Field("secret_key") int secret_key);

    // giving favorit to other user
    @FormUrlEncoded
    @POST("favorite.php")
    Call<Favorite> selectFavorite(@Field("user_id") String user_id,
                                  @Field("other_user_id") String othersUserId,
                                  @Field("secret_key") int secret_key);

    // giving favorit to other user
    @FormUrlEncoded
    @POST("all_user_list.php")
    Call<OtherUser> allOtherUserList(@Field("secret_key") int secret_key);


    //chat room apis
    @FormUrlEncoded
    @POST("room_friends.php")
    Call<RoomFriendMessageClass> roomMessageFriends(@Field("secret_key") int secret_key);

    //upload photoblog photo
    @FormUrlEncoded
    @POST("upload_photo_blog_image.php")
    Call<UserPhotoBlogImages[]> uploadPhotoBlog(@Field("secret_key") int secret_key,
                                                @Field("image") String image,
                                                @Field("image_name") String imageTitle,
                                                @Field("user_id") String user_id,
                                                @Field("description") String description
    );

    @FormUrlEncoded
    @POST("user_all_photo_blog.php")
    Call<UserPhotoBlogImages[]> userAllImagePhotoBlog(@Field("secret_key") int secret_key,
                                                      @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("other_user_all_photo_blog.php")
    Call<UserPhotoBlogImages[]> otherUserAllImagePhotoBlog(@Field("secret_key") int secret_key,
                                                           @Field("user_id") String user_id
    );


//    @FormUrlEncoded
//    @POST("buy_credit.php")
//    Call<BuyCredit> buyPalupCredits(@Field("secret_key") int secret_key,
//                                    @Field("user_id") String user_id,
//                                    @Field("credit") int credit,
//                                    @Field("price") int price
//    );


    @FormUrlEncoded
    @POST("profile_image_swipe.php")
    Call<ProfileImageLoader> profileImageSwipe(@Field("secret_key") int secret_key,
                                               @Field("user_id") String user_id,
                                               @Field("serial") int serial
    );

    @FormUrlEncoded
    @POST("purchases_message_request.php")
    Call<String> onPurchaseMessage(@Field("secret_key") int secret_key,
                                   @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("photo_blog_limit_check.php")
    Call<String> onPhotoLimit(@Field("secret_key") int secret_key,
                              @Field("user_id") String user_id

    );


    //like and comment
    @FormUrlEncoded
    @POST("add_photo_blog_like.php")
    Call<Likes> giveLike(@Field("secret_key") int secret_key,
                         @Field("user_id") String user_id,
                         @Field("photo_id") int photo_id
    );

    @FormUrlEncoded
    @POST("is_liked.php")
    Call<Likes> isLiked(@Field("secret_key") int secret_key,
                        @Field("user_id") String user_id,
                        @Field("photo_id") int photo_id
    );

    //like and comment
    @FormUrlEncoded
    @POST("add_photo_blog_comment.php")
    Call<AllComments> putComment(@Field("secret_key") int secret_key,
                                 @Field("user_id") String user_id,
                                 @Field("photo_id") int photo_id,
                                 @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("all_photo_blog_like.php")
    Call<Likes> getAllLikes(@Field("secret_key") int secret_key,
                            @Field("photo_id") int photo_id
    );

    //comment
    @FormUrlEncoded
    @POST("all_photo_Blog_comment.php")
    Call<AllComments> getAllComments(@Field("secret_key") int secret_key,
                                     @Field("photo_id") int photo_id
    );


    @FormUrlEncoded
    @POST("who_loved_me.php")
    Call<Liked> getAllLiked(@Field("secret_key") int secret_key,
                            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("who_favorite_me.php")
    Call<Liked> getAllfavourite(@Field("secret_key") int secret_key,
                                @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("total_favorite_count.php")
    Call<String> getFavoriteCount(@Field("secret_key") int secret_key,
                                  @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("who_view_me.php")
    Call<Liked> getAllvisitor(@Field("secret_key") int secret_key,
                              @Field("user_id") String user_id
    );

    //message
    @FormUrlEncoded
    @POST("message.php")
    Call<ChatAppMsgDTO> startChat(
            @Field("secret_key") int secretKey,
            @Field("user_id") String user_id,
            @Field("other_user_id") String other_user_id,
            @Field("message") String message

    );

    //message
    @FormUrlEncoded
    @POST("image_upload_message.php")
    Call<ChatAppMsgDTO> uploadImageThroughMessage(
            @Field("secret_key") int secretKey,
            @Field("user_id") String user_id,
            @Field("other_user_id") String other_user_id,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("get_all_private_message.php")
    Call<ChatAppMsgDTO> getAllPrivateMessage(@Field("secret_key") int secretKey,
                                             @Field("user_id") String user_id,
                                             @Field("other_user_id") String other_user_id);


    @FormUrlEncoded
    @POST("get_all_chat_requested_message.php")
    Call<ChatRequest> getAllRequestedList(@Field("secret_key")
                                                  int secretKey,
                                          @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("is_seen.php")
    Call<Void> messageSeen(@Field("secret_key") int secretKey,
                           @Field("conversation_id") String conversationId);

    @FormUrlEncoded
    @POST("get_conversation_id.php")
    Call<String> getConversationId(@Field("secret_key") int secretKey,
                                   @Field("user_id") String userId,
                                   @Field("other_user_id") String otherUserId);


    //chatroom apis
    @FormUrlEncoded
    @POST("get_all_chat_room_message.php")
    Call<ChatRoomMessage> getAllChatRoomMessage(@Field("secret_key") int secretKey,
                                                @Field("type") String type,
                                                @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("start_chat_room_message.php")
    Call<ChatRoomMessage> startRoomChat(@Field("secret_key") int secretKey,
                                        @Field("user_id") String userId,
                                        @Field("type") String type,
                                        @Field("message") String msgContent);

    @FormUrlEncoded
    @POST("chat_room.php")
    Call<ChatRoom> roomChat(@Field("secret_key") int secretKey,
                            @Field("user_id") String userId,
                            @Field("type") String msgContent,
                            @Field("is_subcribed") int isSubscribe);


    @FormUrlEncoded
    @POST("check_is_ban_for_chat_room.php")
    Call<ChatRoom> checkIsKickedOut(@Field("secret_key") int secretKey,
                                    @Field("user_id") String userId,
                                    @Field("type") String type);

    @FormUrlEncoded
    @POST("room_kick_out.php")
    Call<String> kickOutFromChatRoom(@Field("secret_key") int secretKey,
                                     @Field("user_id") String userId,
                                     @Field("type") String type,
                                     @Field("chat_room_id") String chatRoomId
    );

    @FormUrlEncoded
    @POST("chat_room_delete_message.php")
    Call<Void> deleteMessageChatRoom(@Field("secret_key") int secretKey,
                                     @Field("message_id") String messageId);

    @FormUrlEncoded
    @POST("chat_room_action.php")
    Call<String> chatRoomGateway(@Field("secret_key") int secretKey,
                                 @Field("user_id") String userId,
                                 @Field("type") String typeOne,
                                 @Field("action") String action

    );

    @FormUrlEncoded
    @POST("get_all_users_of_chat_room.php")
    Call<ChatRoomUserInfo[]> getAllChatRoomUserInfo(@Field("secret_key") int secretKey,
                                                    @Field("type") String typeOne,
                                                    @Field("user_id") String userId,
                                                    @Field("country") String country
    );

    @FormUrlEncoded
    @POST("all_purchase_log.php")
    Call<PalupBuyCredits[]> getAllPurchase(@Field("secret_key") int secret_key,
                                           @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("all_transaction.php")
    Call<Transaction[]> getAllTransaction(@Field("secret_key") int secret_key,
                                          @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("report_message_chatroom.php")
    Call<String> reportMessage(@Field("secret_key") int secretKey,
                               @Field("message_id") String messageId,
                               @Field("description") String description
    );

    @FormUrlEncoded//working well
    @POST("unlock_profile_by_pp.php")
    Call<String> getUnlockSeen(@Field("secret_key") int secretKey,
                               @Field("user_id") String user_id,
                               @Field("other_user_id") String other_user_id,
                               @Field("type") String type
    );

    @FormUrlEncoded
    @POST("global_subscription_check.php")
    Call<CheckUserAlreadyValidInGlobalChatRoom> checkUserAlreadyEnteredIntoGlobalChatRoom(@Field("secret_key") int secretKey,
                                                                                          @Field("user_id") String userId,
                                                                                          @Field("type") String type
    );


    @FormUrlEncoded
    @POST("chat_rooms_member.php")
    Call<ChatRoomUser> onlineUser(@Field("type") String type);

    @FormUrlEncoded
    @POST("user_report.php")
    Call<String> getReportUser(@Field("secret_key") int secretKey,
                               @Field("reported_id") String user_id,
                               @Field("reporter_id") String other_user_id,
                               @Field("description") String type

    );

    @FormUrlEncoded
    @POST("buy_credit.php")
    Call<String> onSuccessfullyPurchased(@Field("secret_key") int secretKey,
                                         @Field("user_id") String userId,
                                         @Field("order_id") String orderId,
                                         @Field("item_id") String sku,
                                         @Field("point") int pp,
                                         @Field("price") int price);

    @FormUrlEncoded
    @POST("buy_subscription.php")
    Call<String> onSuccessfullySubscripttion(@Field("secret_key") int secretKey,
                                             @Field("user_id") String userId,
                                             @Field("order_id") String orderId,
                                             @Field("item_id") String sku,
                                             @Field("price") int price);

    @FormUrlEncoded
    @POST("privacy.php")
    Call<String> onPrivacy(@Field("secret_key") int secretKey,
                           @Field("privacy") String privacy,
                           @Field("photo_id") int photo_id,
                           @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("block_user.php")
    Call<String> onBlockUser(@Field("secret_key") int secretKey,
                             @Field("other_user_id") String other_user_id,
                             @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("block_list.php")
    Call<List<Blocklist>> onBlocklist(@Field("secret_key") int secretKey,
                                      @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("all_block_data.php")
    Call<List<BlockData>> onBlockdata(@Field("secret_key") int secretKey,
                                      @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("reset_password.php")
    Call<String> onForgetPassword(@Field("secret_key") int secretKey,
                                  @Field("email") String email);

    @FormUrlEncoded
    @POST("chat_list.php")
    Call<ActiveChat> getAllChatList(@Field("secret_key") int secretKey,
                                    @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("last_access.php")
    Call<String> lastExcessTime(@Field("secret_key") int secretKey,
                                @Field("user_id") String userId);

    @GET("random_hotlist.php")
    Call<Hotlist[]> getRecentHotlist();
    //end of chat room apis

    @FormUrlEncoded
    @POST("photo_view_photoblog.php")
    Call<Response> viewPhoto(@Field("secret_key") int secretKey,
                             @Field("user_id") String userId,
                             @Field("order_user_id") String orderId,
                             @Field("photo_id") String photoId
    );

    @GET("photoblog_photo_loved_user.php")
    Call<Likes> getLovedUser(@Field("user_id") String userId,
                             @Field("photo_id") String photoId);

    @FormUrlEncoded
    @POST("photoblogapi.php")
    Call<PhotoBlog[]> getAllPhotoBlogImages(@Field("secret_key") int secretKey,
                                            @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("topphotoblogapi.php")
    Call<PhotoBlog[]> getAllTopPhotoBlog(@Field("secret_key") int secretKey,
                                         @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("new-match.php")
    Call<RecentMatchModel> getRecentMatchData(@Field("user_id") String userId,
                                              @Field("secret_key") int secretKey);


}
