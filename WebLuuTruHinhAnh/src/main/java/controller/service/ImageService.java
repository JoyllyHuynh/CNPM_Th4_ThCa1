package controller.service;

import DAO.ImageDao;
import model.Image;

import java.util.List;

public class ImageService {

    private ImageDao imgd = new ImageDao();

    // [Bước 3.1.7] Tầng ImageService tiếp nhận mã định danh userId và từ khóa keyword đã được chuẩn hóa
    public List<Image> searchByKW(int userId, String kw){
        // [Bước 3.1.8] Tầng nghiệp vụ gọi đến tầng truy cập dữ liệu (ImageDao)
        return imgd.searchByKW(userId, kw);
    }

    // 1.1.6 ImageService xử lý logic kiểm tra tham số sortBy
    // Alternative Flow 1.2: Nếu sortBy == null -> gán "newest"
    public List<Image> getImagesSorted(int userId, String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "newest";
        }
        // 1.1.7 ImageService kết nối xuống tầng dữ liệu (ImageDao) để thực thi truy vấn Database
        return imgd.getImagesSorted(userId, sortBy);
    }

    public Image getImageById(int id) {
        return imgd.findById(id);
    }

    public void uploadImage(Image image) {
        imgd.insertImage(image);
    }

    public boolean renameImage(int id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return false;
        }
        return imgd.updateImageName(id, newName.trim());
    }

    // [Bước 3.3.7] Gọi tầng truy cập dữ liệu để lấy các gợi ý từ khóa
    public List<String> getSearchSuggestions(int userId, String kw) {
        return imgd.getSearchSuggestions(userId, kw);
    }
}
