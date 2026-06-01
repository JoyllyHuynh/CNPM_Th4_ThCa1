package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/RenameImage")
public class RenameImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ImageService imageService = new ImageService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 13.1. Tiếp nhận và thiết lập bảng mã dữ liệu
        // =========================

        // 13.1.1 Tiếp nhận request POST gửi đến /RenameImage

        // 13.1.2 Thiết lập bảng mã UTF-8 cho dữ liệu đầu vào
        request.setCharacterEncoding("UTF-8");

        // 13.1.2 Thiết lập bảng mã UTF-8 cho dữ liệu đầu ra
        response.setCharacterEncoding("UTF-8");



        // 13.1.3 Thu thập Request Parameter "id"
        String idStr = request.getParameter("id");

        // 13.1.3 Thu thập Request Parameter "newName"
        String newName = request.getParameter("newName");



        // =========================
        // 13.2. Kiểm tra tính hợp lệ dữ liệu đầu vào
        // =========================

        // 13.2.1 Kiểm tra dữ liệu null hoặc tên mới rỗng
        // Exception Flow 8.1:
        // Nếu id hoặc newName bị thiếu/null
        // hoặc newName chỉ chứa khoảng trắng
        // -> Trả về HTTP 400 BAD REQUEST
        // -> Dừng xử lý
        if (idStr == null || newName == null || newName.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }



        // 13.2.2 Loại bỏ khoảng trắng dư thừa đầu/cuối
        newName = newName.trim();



        // =========================
        // 13.3. Chuẩn hóa định dạng tên tệp tin
        // =========================

        // 13.3.1 Kiểm tra tên đã có đuôi .png chưa
        // (không phân biệt hoa thường)

        // 13.3.2 Nếu chưa có -> tự động nối thêm .png
        if (!newName.toLowerCase().endsWith(".png")) {
            newName += ".png";
        }



        // =========================
        // 13.4. Chuyển đổi kiểu dữ liệu định danh
        // =========================

        int id;

        try {

            // 13.4.1 Chuyển id từ String sang int
            // 13.4.2 Chuyển đổi thành công
            id = Integer.parseInt(idStr);

        } catch (NumberFormatException e) {

            // =========================
            // Exception Flow 8.2
            // 13.4.1 – Sai định dạng ID
            // =========================

            // Nếu id không phải số nguyên hợp lệ
            // -> Trả về HTTP 400 BAD REQUEST
            // -> Dừng xử lý
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }



        // =========================
        // 13.5. Cập nhật dữ liệu hệ thống
        // =========================

        // 13.5.1 Controller gọi tầng Service đổi tên ảnh

        // 13.5.2 Service thực hiện UPDATE tên ảnh trong DB

        // 13.5.3 Nhận kết quả trạng thái isSuccess
        boolean isSuccess = imageService.renameImage(id, newName);



        // =========================
        // 13.6. Phản hồi kết quả cho Client
        // =========================

        if (isSuccess) {

            // 13.6.1 Thiết lập Content-Type dạng text/plain
            response.setContentType("text/plain");

            // 13.6.1 Thiết lập UTF-8 cho response
            response.setCharacterEncoding("UTF-8");

            // 13.6.2 Ghi tên mới ra response
            // để Javascript cập nhật realtime giao diện
            response.getWriter().write(newName);

            // 13.6.3 Trả về HTTP 200 OK
            response.setStatus(HttpServletResponse.SC_OK);

            // 13.6.4 Frontend JS nhận dữ liệu
            // và cập nhật tên ảnh không cần reload trang

        } else {

            // =========================
            // Exception Flow 8.3
            // 13.5.3 – Lỗi cập nhật Database
            // =========================

            // Nếu renameImage() thất bại
            // -> Trả về HTTP 500 INTERNAL SERVER ERROR
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}