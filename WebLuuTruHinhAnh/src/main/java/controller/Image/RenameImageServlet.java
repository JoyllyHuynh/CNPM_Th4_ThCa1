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
        // 13.1. Normal Flow: Chỉnh sửa ảnh
        // =========================

        // 13.1.1. Người dùng nhấn vào nút "Đổi tên" tại một bức ảnh, nhập tên mới vào ô nhập liệu và nhấn "Xác nhận".
        // 13.1.2. Hệ thống (RenameImageServlet) tiếp nhận yêu cầu gửi lên thông qua phương thức HTTP POST đến đường dẫn /RenameImage.

        // 13.1.3. Hệ thống thiết lập bảng mã ký tự đầu vào và đầu ra là UTF-8 nhằm hỗ trợ các ký tự đa ngôn ngữ và tiếng Việt có dấu.
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 13.1.4. Hệ thống thu thập hai tham số từ Request Parameter bao gồm: Mã định danh ảnh ("id") và chuỗi tên mới cần thay đổi ("newName").
        String idStr = request.getParameter("id");
        String newName = request.getParameter("newName");

        // 13.1.5. Hệ thống kiểm tra điều kiện rỗng: Xác định các tham số dữ liệu thu được phải khác null và chuỗi tên mới sau khi cắt bỏ khoảng trắng đầu cuối không được trống.
        // =========================
        // Exception Flow 13.2: Dữ liệu đầu vào bị rỗng hoặc không hợp lệ
        // =========================
        // 13.2.1. Tại bước 13.1.5, nếu tham số "id" hoặc "newName" bị thiếu (null), hoặc người dùng chỉ nhập toàn dấu cách vào ô tên mới.
        if (idStr == null || newName == null || newName.trim().isEmpty()) {
            // 13.2.2. Hệ thống ngừng xử lý nghiệp vụ ngay lập tức.
            // 13.2.3. Hệ thống thiết lập mã trạng thái lỗi yêu cầu không hợp lệ HTTP 400 Bad Request và kết thúc xử lý.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 13.1.6. Hệ thống tiến hành cắt bỏ hoàn toàn các ký tự khoảng trắng thừa ở đầu và cuối chuỗi nhập liệu.
        newName = newName.trim();

        // 13.1.7. Hệ thống kiểm tra chuỗi tên mới xem đã kết thúc bằng phần mở rộng tệp tin .png hay chưa (không phân biệt chữ hoa hay chữ thường).
        // 13.1.8. Do chuỗi tên người dùng nhập chưa kết thúc bằng đuôi mở rộng, hệ thống tự động nối thêm chuỗi định dạng .png vào sau tên tệp tin.
        if (!newName.toLowerCase().endsWith(".png")) {
            newName += ".png";
        }

        // 13.1.9. Hệ thống thực hiện chuyển đổi tham số chuỗi mã định danh ảnh sang kiểu số nguyên.
        // =========================
        // Exception Flow 13.3: Sai định dạng mã hình ảnh (NumberFormatException)
        // =========================
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            // 13.3.1. Tại bước 13.1.9, tiến trình Integer.parseInt(idStr) ném ra ngoại lệ NumberFormatException.
            // 13.3.2. Hệ thống bắt lấy ngoại lệ tại khối catch, dừng toàn bộ tiến trình xử lý.
            // 13.3.3. Hệ thống thiết lập mã trạng thái lỗi HTTP 400 Bad Request và kết thúc xử lý.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 13.1.10. Lớp điều khiển gọi hàm nghiệp vụ xử lý đổi tên thuộc tầng dịch vụ ImageService.
        // 13.1.11. ImageService kết nối cơ sở dữ liệu, thực thi lệnh UPDATE cập nhật trường tên hiển thị của bản ghi ảnh có ID tương ứng thành giá trị newName mới.
        // 13.1.12. Cơ sở dữ liệu ghi nhận thay đổi thành công và trả về giá trị trạng thái isSuccess.
        boolean isSuccess = imageService.renameImage(id, newName);

        if (isSuccess) {
            // 13.1.13. Hệ thống thiết lập kiểu dữ liệu trả về cho client là dạng văn bản thuần túy (text/plain).
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            // 13.1.14. Hệ thống ghi chuỗi tên mới đã được chuẩn hóa (newName) vào luồng xuất dữ liệu và thiết lập trạng thái phản hồi thành công chuẩn HTTP 200 OK.
            response.getWriter().write(newName);
            response.setStatus(HttpServletResponse.SC_OK);

            // 13.1.15. Phía Frontend nhận được chuỗi phản hồi, dùng Javascript cập nhật thẻ text hiển thị tên ảnh trên màn hình mà không cần reload trang.
        } else {
            // =========================
            // Exception Flow 13.4: Lỗi cập nhật dữ liệu từ Server (Database Error)
            // =========================
            // 13.4.1. Tại bước 13.1.12, imageService.renameImage() trả về kết quả thất bại (isSuccess = false).
            // 13.4.2. Hệ thống bỏ qua bước ghi dữ liệu tên mới ra response.
            // 13.4.3. Hệ thống thiết lập mã trạng thái HTTP 500 Internal Server Error để thông báo lỗi cho Client.
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}