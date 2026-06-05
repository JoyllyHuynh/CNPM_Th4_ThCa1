package controller.Image;

import DAO.ImageDao;
import controller.service.ImageService;
import model.Image;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "DownloadServlet", value = "/DownloadServlet")
public class DownloadServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();
    private final ImageDao imageDao = new ImageDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 12.1. Tiếp nhận tham số yêu cầu hình ảnh
        // =========================

        // 12.1.1 Tiếp nhận Request Parameter "id"
        String idStr = request.getParameter("id");



        // 12.1.2 Kiểm tra tham số khác null
        if (idStr != null) {

            try {

                // 12.1.2 Ép kiểu chuỗi sang số nguyên
                // Exception Flow 8.1:
                // Nếu idStr không phải số hợp lệ
                // -> NumberFormatException
                // -> Rơi vào catch(Exception)
                int id = Integer.parseInt(idStr);



                // =========================
                // 12.2. Truy vấn dữ liệu hình ảnh
                // =========================

                // 12.2.1 Controller gọi tầng Service
                // LUỒNG: Controller -> Service -> DAO

                // 12.2.2 Service truy vấn DB tìm ảnh theo ID
                Image img = imageService.getImageById(id);



                // 12.2.3 Kiểm tra img tồn tại hợp lệ
                // Exception Flow 8.2:
                // Nếu img == null
                // -> Dừng xử lý
                if (img != null) {



                    // =========================
                    // 12.3. Định vị vật lý và kiểm tra file trên server
                    // =========================

                    // 12.3.1 Xác định đường dẫn thư mục uploads
                    String uploadDir = getServletContext().getRealPath("/uploads");

                    // 12.3.2 Khởi tạo File bằng đường dẫn uploads + filePath
                    // Cách nối đường dẫn an toàn cho cả Windows và Linux
                    File file = new File(uploadDir, img.getFilePath());



                    // 12.3.3 Kiểm tra file có tồn tại vật lý trên server
                    if (file.exists()) {

                        imageDao.increaseDownloadCount(id);

                        // =========================
                        // 12.4. Cấu hình HTTP Response Header
                        // =========================

                        // 12.4.1 Encode tên file UTF-8
                        // và thay dấu "+" thành "%20"
                        String encodedFileName = URLEncoder.encode(
                                img.getFileName(),
                                StandardCharsets.UTF_8.toString()
                        ).replaceAll("\\+", "%20");



                        // 12.4.2 Thiết lập Content-Type dạng binary stream
                        response.setContentType("application/octet-stream");



                        // 12.4.3 Thiết lập Header Content-Disposition
                        // để trình duyệt tải file thay vì mở trực tiếp
                        response.setHeader(
                                "Content-Disposition",
                                "attachment; filename=\"" + encodedFileName
                                        + "\"; filename*=UTF-8''" + encodedFileName
                        );



                        // 12.4.4 Thiết lập kích thước file
                        response.setContentLength((int) file.length());



                        // =========================
                        // 12.5. Stream truyền tải dữ liệu
                        // =========================

                        // 12.5.1 Mở FileInputStream và OutputStream
                        try (
                                FileInputStream in = new FileInputStream(file);
                                OutputStream out = response.getOutputStream()
                        ) {

                            // 12.5.2 Khởi tạo buffer 8192 bytes
                            byte[] buffer = new byte[8192];

                            int length;

                            // 12.5.3 Đọc dữ liệu từ file
                            // và ghi liên tục ra response output stream
                            while ((length = in.read(buffer)) > 0) {
                                out.write(buffer, 0, length);
                            }

                            // 13.5.4 Try-with-resources tự động đóng stream
                        }

                    } else {

                        // =========================
                        // Alternative Flow 7.1
                        // 12.3.3 – File không tồn tại trên server
                        // =========================

                        // Trả về HTTP 404
                        response.sendError(404, "File không tồn tại trên server!");
                    }
                }

            } catch (Exception e) {

                // =========================
                // Exception Flow 8.1
                // 12.1.2 – Lỗi định dạng ID
                // =========================

                // Ghi log lỗi để quản trị viên theo dõi
                e.printStackTrace();
            }
        }
    }
}