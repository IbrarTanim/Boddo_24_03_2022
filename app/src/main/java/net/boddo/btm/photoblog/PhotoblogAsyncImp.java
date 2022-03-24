package net.boddo.btm.photoblog;

import java.util.List;

import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.photoblog.data.repository.PhotoBlogRepo;

public class PhotoblogAsyncImp implements PhotoblogAsync {

    @Override
    public void getPhotoBlogs(PhotoBlogRepo repo, LoadPhtotoBlogCallBack callback) {
        List<PhotoBlog> photoBlogs = repo.getPhotoBlogs();
        getPhotoBlogs(repo,callback);
    }

    void getPhotoBlogsAwait(List<PhotoBlog> photoBlogs,LoadPhtotoBlogCallBack callBack){
        if (photoBlogs ==null) callBack.onPhotoBlogNotAvailable();
        else callBack.onPhotoBlogLoaded(photoBlogs);
    }

}
