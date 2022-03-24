package net.boddo.btm.photoblog;

import java.util.List;

import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.photoblog.data.repository.PhotoBlogRepo;

public interface PhotoblogAsync {

    interface LoadPhtotoBlogCallBack {
        void onPhotoBlogLoaded(List<PhotoBlog> photoBlogs);
        void onPhotoBlogNotAvailable();
    }

    void getPhotoBlogs(PhotoBlogRepo repo,LoadPhtotoBlogCallBack callback);
}
