package DAO;

import model.Imagee;

import java.util.List;

public class ImageeDao extends BaseDao{
    public List<Imagee> getListImage(int uid, int aid) {
        return getJdbi().withHandle(handle ->
            handle.createQuery("Select i.* from images i left join album_images ai on i.id=ai.image_id  left join albums a on a.id=ai.album_id WHERE a.user_id= :uid AND a.id=:aid and i.is_deleted=0")
                    .bind("uid",uid)
                    .bind("aid",aid)
                    .mapToBean(Imagee.class)
                    .list()
        );
    }

    public boolean removePhotosFromAlbum(int uid, int albumId, List<Integer> photoIds) {

        if (photoIds == null || photoIds.isEmpty()) {
            return false;
        }

        return getJdbi().inTransaction(handle -> {
            // [Bước 19.1.2] System: Kiểm tra quyền sở hữu album (SR-19, SR-24)
            int ownerCount = handle.createQuery("SELECT COUNT(*) FROM albums WHERE id = :aid AND user_id = :uid")
                    .bind("aid", albumId)
                    .bind("uid", uid)
                    .mapTo(int.class)
                    .one();

            if (ownerCount == 0) {
                // [Bước 19.4.1 & 19.4.2] System: Phát hiện không phải owner -> Từ chối
                // [Bước 19.4.3] System: Ghi log sự kiện (SR-32)
                System.err.println("[WARN - SR-32] User " + uid + " attempted to remove photos from album " + albumId + " without ownership.");
                return false;
            }

            // [Bước 19.1.8] System: Kiểm tra mapping image-album (SR-16)
            String sqlCheck = "SELECT COUNT(*) FROM album_images WHERE album_id = :aid AND image_id IN (<pids>)";
            int existCount = handle.createQuery(sqlCheck)
                    .bind("aid", albumId)
                    .bindList("pids", photoIds)
                    .mapTo(int.class)
                    .one();

            if (existCount == 0) {
                // [Bước 19.3.1 & 19.3.2] System: Không tìm thấy mapping -> Trả về thông báo lỗi
                return false;
            }

            // [Bước 19.1.9] System: Xóa relationship (album_id, image_id)
            String sqlDelete = "DELETE FROM album_images WHERE album_id = :aid AND image_id IN (<pids>)";
            handle.createUpdate(sqlDelete)
                    .bind("aid", albumId)
                    .bindList("pids", photoIds)
                    .execute();

            return true;
        });
    }

    public List<Imagee> getListImageOfUser(int uid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("Select * from images WHERE user_id = :uid AND is_deleted=0 ")
                        .bind("uid",uid)
                        .mapToBean(Imagee.class)
                        .list()
        );
    }
}
