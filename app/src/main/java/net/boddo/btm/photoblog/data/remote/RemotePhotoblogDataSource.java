package net.boddo.btm.photoblog.data.remote;


import com.orhanobut.logger.Logger;

import java.util.List;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Model.PhotoBlog;
import retrofit2.Response;


public class RemotePhotoblogDataSource implements PhotoblogDataSource {

    @Override
    public List<PhotoBlog> getPhotoBlogs() {
        try {
            Logger.d("PhotoblogList (remote) =>");
            Response<List<PhotoBlog>> response = ApiClient
                    .getApiClient()
                    .create(PhotoblogEndPoints.class)
                    .getPhotoBlogs()
                    .execute();

        }catch (Exception e){
            Logger.e("Photoblog list failed to load => " +e.getMessage());
        }

        return null;
    }
}
