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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // [Bước 3.3.4] Xác thực quyền truy cập của người dùng từ Session (Tương tự Exception 3.4)
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("[]");
            return;
        }

        // [Bước 3.3.5] Lấy tham số 'keyword' từ request
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        // [Bước 3.3.6] Thực hiện gọi service lấy danh sách gợi ý
        List<String> suggestions = imageService.getSearchSuggestions(user.getId(), keyword.trim());

        // [Bước 3.3.10] Trả về dữ liệu gợi ý dưới định dạng JSON
        response.getWriter().write(gson.toJson(suggestions));
    }
}
