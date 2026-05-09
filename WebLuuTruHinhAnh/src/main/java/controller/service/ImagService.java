package controller.service;

import DAO.ImageeDao;
import model.Imagee;

import java.util.List;

public class ImagService {
    ImageeDao imageDao = new ImageeDao();

    public List<Imagee> getListImage(int uid, int aid) {
        return imageDao.getListImage(uid,aid);
    }

    public static void main(String[] args) {
        ImagService imagService = new ImagService();
        List<Imagee> images = imagService.getListImage(1,9);
        for (Imagee image : images) {
            System.out.println(image.toString());
        }
    }

    public boolean removePhotosFromAlbum(int albumId, List<Integer> photoIds) {
        return imageDao.removePhotosFromAlbum(albumId, photoIds);
    }

    public List<Imagee> getListImageOfUser(int uid) {
        return imageDao.getListImageOfUser(uid);
    }
}
