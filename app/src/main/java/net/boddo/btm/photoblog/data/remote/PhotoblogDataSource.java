package net.boddo.btm.photoblog.data.remote;

import java.util.List;

import net.boddo.btm.Model.PhotoBlog;

public interface PhotoblogDataSource {
    List<PhotoBlog> getPhotoBlogs();
}
