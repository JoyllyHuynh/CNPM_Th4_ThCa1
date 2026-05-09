package controller.AlbumDetail;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Album;
import model.Imagee;
import controller.service.AlbumsService;
import controller.service.ImagService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Album_detail", value = "/Album_detail")
public class AlbumDetail extends HttpServlet {
    ImagService imagService = new ImagService();
    AlbumsService albumsService= new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
            return;
        }


        String albumId = request.getParameter("aid");
        String userId = request.getParameter("userId");

        int aid=Integer.parseInt(albumId);
        int uid=Integer.parseInt(userId);

        List<Imagee> imageList=imagService.getListImage(uid,aid);
        List<Imagee> imageListOfUser=imagService.getListImageOfUser(uid);

        Album album=albumsService.getAlbum(aid);

        request.setAttribute("imageListOfUser",imageListOfUser);
        request.setAttribute("userId", uid);
        request.setAttribute("album",album);
        request.setAttribute("imageList",imageList);

        request.setAttribute("activeTopNav", "albums");
        request.getRequestDispatcher("user/album-detail.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}