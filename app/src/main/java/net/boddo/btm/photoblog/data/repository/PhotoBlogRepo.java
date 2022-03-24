package net.boddo.btm.photoblog.data.repository;

import java.util.List;

import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.photoblog.data.remote.PhotoblogDataSource;

public class PhotoBlogRepo implements PhotoblogDataSource {

    private volatile static PhotoBlogRepo sInstance = null;

    private final PhotoblogDataSource mRemoteDataSource;

    private PhotoBlogRepo(PhotoblogDataSource mRemoteDataSource){
        this.mRemoteDataSource = mRemoteDataSource;
    }

    public static PhotoBlogRepo getInstance(PhotoblogDataSource photoblogDataSource){
        if (sInstance == null){
            synchronized (PhotoBlogRepo.class){
                if (sInstance == null){
                    sInstance = new PhotoBlogRepo(photoblogDataSource);
                }
            }
        }
        return sInstance;
    }

    @Override
    public List<PhotoBlog> getPhotoBlogs() {
        return  mRemoteDataSource.getPhotoBlogs();
    }
}
