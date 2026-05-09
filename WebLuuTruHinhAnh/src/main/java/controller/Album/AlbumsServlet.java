package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AlbumsServlet", value = "/album")
public class AlbumsServlet extends HttpServlet {
    AlbumsService albumService=new AlbumsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<>

        request.getRequestDispatcher("/user/albums.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}