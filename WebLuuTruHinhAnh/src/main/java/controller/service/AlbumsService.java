package controller.service;

import DAO.AlbumsDao;
import model.Album;

import java.util.List;

public class AlbumsService {
    private AlbumsDao  albumsDao = new AlbumsDao();
    public List<Album> getAllAlbums(int uid) {
        return albumsDao.getAllAlbums(uid);
    }

    public boolean isAlbumNameExist(int uid, String albumName) {
        return albumsDao.isAlbumNameExist(uid, albumName);
    }

    // [7.1.7]
    public boolean createAlbum(int uid, String albumName) {
        // BỔ SUNG: Kiểm tra nếu tên album bị null, rỗng ("") hoặc chỉ toàn dấu cách ("   ")
        if (albumName == null || albumName.trim().isEmpty()) {
            return false; // Dừng lại ngay, không gọi xuống DAO nữa
        }

        if (albumsDao.isAlbumNameExist(uid, albumName)) {
            return false;
        }
        return albumsDao.createAlbum(uid, albumName);
    }

    // =================================================================
    // [2.1.6] deleteAlbum(uid, albumId)
    // Tầng Service gọi tiếp xuống tầng DAO để tương tác với Cơ sở dữ liệu
    // =================================================================
    public boolean deleteAlbum(int uid, int albumId) {
        // =================================================================
        // [2.1.9] return (rows > 0)
        // Trả kết quả logic (true nếu có hàng bị xóa, false nếu không) ngược lên Controller
        // =================================================================
        return albumsDao.deleteAlbum(uid, albumId);
    }

    public Album getAlbum(int aid) {
        return  albumsDao.getAlbum(aid);
    }

    // =================================================================
    // [10.1.9] INSERT INTO album_images (album_id, image_id)
    // Tầng Service gọi phương thức xử lý của DAO để chạy vòng lặp hoặc Batch Insert câu lệnh SQL
    // =================================================================
    public boolean addPhotosToAlbum(int uid, int albumId, List<Integer> ids) {

        if (ids == null || ids.isEmpty()) {
            return false;
        }
        // =================================================================
        // [10.1.11] return message
        // Trả chuỗi thông điệp phản hồi kết quả ngược về cho Controller
        // =================================================================
        return albumsDao.addPhotosToAlbum(uid, albumId, ids);
    }
}
