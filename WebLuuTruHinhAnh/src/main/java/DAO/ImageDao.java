package DAO;

import model.Image;

import java.util.List;

public class ImageDao extends BaseDao{
    public List<Image> getListImage(int uid, int aid) {
        return getJdbi().withHandle(handle ->
            handle.createQuery("Select i.* from images i left join album_images ai on i.id=ai.image_id  left join albums a on a.id=ai.album_id WHERE a.user_id= :uid AND a.id=:aid and i.is_deleted=0")
                    .bind("uid",uid)
                    .bind("aid",aid)
                    .mapToBean(Image.class)
                    .list()
        );
    }

    public boolean removePhotosFromAlbum(int albumId, List<Integer> photoIds) {

        if (photoIds == null || photoIds.isEmpty()) {
            return false;
        }

        return getJdbi().withHandle(handle -> {

            String sql = """
            DELETE FROM album_images
            WHERE album_id = :aid
            AND image_id = :iid
        """;

            int totalDeleted = 0;

            for (Integer photoId : photoIds) {
                totalDeleted += handle.createUpdate(sql)
                        .bind("aid", albumId)
                        .bind("iid", photoId)
                        .execute();
            }

            return totalDeleted > 0;
        });
    }

    public List<Image> getListImageOfUser(int uid) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("Select * from images WHERE user_id = :uid AND is_deleted=0 ")
                        .bind("uid",uid)
                        .mapToBean(Image.class)
                        .list()
        );
    }
}
