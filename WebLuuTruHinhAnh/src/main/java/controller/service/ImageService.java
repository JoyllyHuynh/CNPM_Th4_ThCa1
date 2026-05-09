package controller.service;

import DAO.ImageDao;
import model.Image;

import java.util.List;

public class ImageService {

    private ImageDao imgd = new ImageDao();

    public List<Image> searchByKW(int userId, String kw){
        return imgd.searchByKW(userId, kw);
    }

    public List<Image> getImagesSorted(int userId, String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "newest";
        }
        return imgd.getImagesSorted(userId, sortBy);
    }
}
