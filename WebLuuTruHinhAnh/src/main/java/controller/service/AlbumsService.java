package controller.service;

import DAO.AlbumsDao;
import model.Album;

import java.util.List;

public class AlbumsService {
    private AlbumsDao  albumsDao = new AlbumsDao();
    public List<Album> getAllAlbums(int uid) {
        return albumsDao.getAllAlbums(uid);
    }

    public boolean createAlbum(int uid, String albumName) {
        if(albumsDao.isAlbumNameExist( uid, albumName)){
            return false;
        }
        return albumsDao.createAlbum(uid,albumName);
    }

    public boolean deleteAlbum(int uid, int albumId) {
        return albumsDao.deleteAlbum(uid,albumId);
    }
}
