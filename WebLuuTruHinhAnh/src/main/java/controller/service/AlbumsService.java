package controller.service;

import DAO.AlbumsDao;
import model.Album;

import java.util.List;

public class AlbumsService {

    private final AlbumsDao albumsDao = new AlbumsDao();

    public List<Album> getAllAlbums(int uid) {
        return albumsDao.getAllAlbums(uid);
    }

    public boolean isAlbumNameExist(int uid, String albumName) {
        return albumsDao.isAlbumNameExist(uid, albumName);
    }

    public boolean createAlbum(int uid, String albumName) {

        if (albumName == null || albumName.trim().isEmpty()) {
            return false;
        }

        if (albumsDao.isAlbumNameExist(uid, albumName)) {
            return false;
        }

        return albumsDao.createAlbum(uid, albumName.trim());
    }

    public boolean deleteAlbum(int uid, int albumId) {
        return albumsDao.deleteAlbum(uid, albumId);
    }

    public Album getAlbum(int aid) {
        return albumsDao.getAlbum(aid);
    }

    public String addPhotosToAlbum(int uid, int albumId, List<Integer> ids) {

        if (ids == null || ids.isEmpty()) {
            return "Vui lòng chọn ít nhất một ảnh.";
        }

        return albumsDao.addPhotosToAlbum(uid, albumId, ids);
    }
}