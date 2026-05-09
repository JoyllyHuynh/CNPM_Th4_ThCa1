package controller.service;

import DAO.ImageDao;
import model.Image;

import java.util.List;

public class ImagService {
    ImageDao imageDao = new ImageDao();

    public List<Image> getListImage(int uid, int aid) {
        return imageDao.getListImage(uid,aid);
    }

    public static void main(String[] args) {
        ImagService imagService = new ImagService();
        List<Image> images = imagService.getListImage(1,9);
        for (Image image : images) {
            System.out.println(image.toString());
        }
    }

    public boolean removePhotosFromAlbum(int albumId, List<Integer> photoIds) {
        return imageDao.removePhotosFromAlbum(albumId, photoIds);
    }

    public List<Image> getListImageOfUser(int uid) {
        return imageDao.getListImageOfUser(uid);
    }
}
