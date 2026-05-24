package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Album;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AlbumsServlet", value = "/album")
public class AlbumsServlet extends HttpServlet {
    AlbumsService albumService=new AlbumsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
            return;
        }


        User user = (User) session.getAttribute("user");
        int uid=user.getId();
        List<Album> albums = albumService.getAllAlbums(uid);

        request.setAttribute("albums", albums);
        request.setAttribute("userId", uid);

        request.setAttribute("activeTopNav", "albums");
        request.getRequestDispatcher("/user/albums.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}