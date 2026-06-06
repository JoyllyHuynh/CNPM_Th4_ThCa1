package controller.Image;

import com.google.gson.Gson;
import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchSuggestionsServlet", value = "/search/suggestions")
public class SearchSuggestionsServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 3.3.3. Bộ điều khiển SearchSuggestionsServlet tiếp nhận request.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 3.3.4. Bộ điều khiển kiểm tra sự tồn tại của session chứa thông tin user (Tương tự Exception 3.4). Nếu session bị null hoặc userId bị null, servlet trả về status code 401 Unauthorized và danh sách gợi ý rỗng [].
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("[]");
            return;
        }

        // 3.3.5. Bộ điều khiển trích xuất tham số keyword, nếu trống thì trả về danh sách rỗng [].
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        // 3.3.6. Bộ điều khiển gọi phương thức getSearchSuggestions(userId, keyword) ở tầng ImageService.
        // 3.3.7. Tầng ImageService tiếp nhận và chuyển tiếp yêu cầu đến tầng ImageDao.
        // 3.3.8. Tầng ImageDao thực hiện truy vấn cơ sở dữ liệu để tìm kiếm danh sách các tên hình ảnh (giới hạn tối đa 7 kết quả, sử dụng DISTINCT).
        // 3.3.9. Danh sách gợi ý được trả về từ DB qua DAO, Service và quay lại Servlet.
        List<String> suggestions = imageService.getSearchSuggestions(user.getId(), keyword.trim());

        // 3.3.10. Servlet chuyển đổi danh sách các chuỗi gợi ý thành định dạng JSON bằng thư viện Gson và ghi trực tiếp vào luồng phản hồi (Response Writer) trả về phía Client.
        response.getWriter().write(gson.toJson(suggestions));
    }
}
