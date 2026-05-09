package controller.AlbumDetail;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Album;
import model.Image;
import org.eclipse.tags.shaded.org.apache.bcel.generic.IADD;
import service.AlbumsService;
import service.ImagService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Album_detail", value = "/Album_detail")
public class AlbumDetail extends HttpServlet {
    ImagService imagService = new ImagService();
    AlbumsService albumsService= new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String albumId = request.getParameter("aid");
        String userId = request.getParameter("userId");

        int aid=Integer.parseInt(albumId);
//        int aid=9;
//        int uid=Integer.parseInt(userId);
        int uid=1;

        List<Image> imageList=imagService.getListImage(uid,aid);
        List<Image> imageListOfUser=imagService.getListImageOfUser(uid);

        Album album=albumsService.getAlbum(aid);

        request.setAttribute("imageListOfUser",imageListOfUser);
        request.setAttribute("userId", uid);
        request.setAttribute("album",album);
        request.setAttribute("imageList",imageList);

        request.getRequestDispatcher("user/album-detail.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}