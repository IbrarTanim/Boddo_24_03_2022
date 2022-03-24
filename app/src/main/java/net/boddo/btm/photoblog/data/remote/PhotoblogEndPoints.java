package net.boddo.btm.photoblog.data.remote;

import java.util.List;

import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.Model.UserPhotoBlogImages;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PhotoblogEndPoints {

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




    @GET("photoblogapi.php")
    Call<List<PhotoBlog>> getPhotoBlogs();



}
