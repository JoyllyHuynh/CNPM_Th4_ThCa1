package controller.Album;

import service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Album;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AlbumsServlet", value = "/album")
public class AlbumsServlet extends HttpServlet {
    AlbumsService albumService=new AlbumsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uid=1;
        List<Album> albums = albumService.getAllAlbums(uid);

        request.setAttribute("albums", albums);
        request.setAttribute("userId", uid);

        request.getRequestDispatcher("/user/albums.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}