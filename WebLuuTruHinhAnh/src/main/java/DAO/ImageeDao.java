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
        if (photoIds == null || photoIds.isEmpty()) return false;

        return getJdbi().withHandle(handle -> {
            // [Mục 8. Exceptions]: Tạo một Transaction khép kín để đảm bảo tính toàn vẹn dữ liệu
            return handle.inTransaction(txn -> {

                // [SR-19 & SR-24]: Xác minh quyền sở hữu album của người dùng hiện tại
                Integer albumOwner = txn.createQuery("SELECT user_id FROM albums WHERE id = :albumId")
                        .bind("albumId", albumId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                // [Luồng 19.4]: Nếu album không tồn tại hoặc không thuộc về user hiện tại -> Hủy bỏ, ghi log cảnh báo (SR-32)
                if (albumOwner == null || albumOwner != uid) {
                    System.err.println("[WARN - SR-32] Phát hiện hành vi truy cập trái phép từ user_id: " + uid + " tới album_id: " + albumId);
                    return false;
                }

                // [Bước 19.1.8 & Luồng 19.3]: Kiểm tra các mối liên kết (mapping) thực tế đang tồn tại trong album
                List<Integer> existingMappings = txn.createQuery("SELECT image_id FROM album_images WHERE album_id = :albumId AND image_id IN (<ids>)")
                        .bind("albumId", albumId)
                        .bindList("ids", photoIds)
                        .mapTo(Integer.class)
                        .list();

                if (existingMappings.isEmpty()) {
                    return false; // Không tìm thấy bất kỳ ảnh nào được chọn nằm trong album (Luồng 19.3)
                }

                // [Bước 19.1.9 & SR-12]: Tiến hành xóa liên kết, tuyệt đối KHÔNG thực thi DELETE từ bảng images
                int rowsDeleted = txn.createUpdate("DELETE FROM album_images WHERE album_id = :albumId AND image_id IN (<ids>)")
                        .bind("albumId", albumId)
                        .bindList("ids", existingMappings)
                        .execute();

                return rowsDeleted > 0;
            });
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
