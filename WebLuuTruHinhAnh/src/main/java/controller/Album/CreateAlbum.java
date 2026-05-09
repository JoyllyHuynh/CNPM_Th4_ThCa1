package controller.Album;

import service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CreateAlbum", value = "/CreateAlbum")
public class CreateAlbum extends HttpServlet {
    AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        String albumName = request.getParameter("albumName");
        String userId = request.getParameter("userId");

        int uid=Integer.parseInt(userId);

        boolean ok = albumsService.createAlbum(uid,albumName);
        if (ok) {
            response.getWriter().write("{\"success\":true,\"message\":\"Create album successfully\"}");
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"Album name already exists or create failed\"}");
        }



    }
}