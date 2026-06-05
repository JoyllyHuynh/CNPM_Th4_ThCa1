package controller.Image;

import controller.service.ImageService;
import model.Image;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

@WebServlet(name = "DownloadServlet", value = "/DownloadServlet")
public class DownloadServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 12.1. Tiếp nhận tham số yêu cầu hình ảnh
        // =========================

        // 12.1.1 Tiếp nhận Request Parameter "id"
        String idStr = request.getParameter("id");
        String quality = request.getParameter("quality"); // Tham số chất lượng ảnh
        String reqFormat = request.getParameter("format"); // Tham số định dạng ảnh        
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 12.1.2 Kiểm tra tham số khác null
        if (idStr != null) {

            try {

                // 12.1.2 Ép kiểu chuỗi sang số nguyên
                // Exception Flow 8.1:
                // Nếu idStr không phải số hợp lệ
                // -> NumberFormatException
                // -> Rơi vào catch(Exception)
                int id = Integer.parseInt(idStr);



                // =========================
                // 12.2. Truy vấn dữ liệu hình ảnh
                // =========================

                // 12.2.1 Controller gọi tầng Service
                // LUỒNG: Controller -> Service -> DAO

                // 12.2.2 Service truy vấn DB tìm ảnh theo ID
                Image img = imageService.getImageById(id);



                // 12.2.3 Kiểm tra img tồn tại hợp lệ
                // Exception Flow 8.2:
                // Nếu img == null
                // -> Dừng xử lý
                if (img != null) {

                    // Kiểm tra quyền sở hữu ảnh
                    if (img.getUserId() != loggedInUser.getId()) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền tải bức ảnh này vì bạn không phải là chủ sở hữu.");
                        return;
                    }



                    // =========================
                    // 12.3. Định vị vật lý và kiểm tra file trên server
                    // =========================

                    // 12.3.1 Xác định đường dẫn thư mục uploads
                    String uploadDir = getServletContext().getRealPath("/uploads");

                    // 12.3.2 Khởi tạo File bằng đường dẫn uploads + filePath
                    // Cách nối đường dẫn an toàn cho cả Windows và Linux
                    File file = new File(uploadDir, img.getFilePath());



                    // 12.3.3 Kiểm tra file có tồn tại vật lý trên server
                    if (file.exists()) {



                        // =========================
                        // 12.4. Cấu hình HTTP Response Header
                        // =========================

                        // 12.4.1 Encode tên file UTF-8
                        // và thay dấu "+" thành "%20"
                        String encodedFileName = URLEncoder.encode(
                                img.getFileName(),
                                StandardCharsets.UTF_8.toString()
                        ).replaceAll("\\+", "%20");



                        // 12.4.2 Thiết lập Content-Type dạng binary stream
                        response.setContentType("application/octet-stream");



                        // 12.4.3 Xác định định dạng file để nén
                        String format = "jpg"; // Mặc định
                        String fileNameLower = img.getFileName().toLowerCase();
                        if (fileNameLower.endsWith(".png")) format = "png";
                        else if (fileNameLower.endsWith(".gif")) format = "gif";
                        
                        boolean isFormatChanged = false;
                        if (reqFormat != null && !reqFormat.equals("original") && !reqFormat.equals(format)) {
                            format = reqFormat;
                            isFormatChanged = true;
                            
                            // Thay đổi đuôi file
                            int lastDot = encodedFileName.lastIndexOf('.');
                            if (lastDot > 0) {
                                encodedFileName = encodedFileName.substring(0, lastDot) + "." + format;
                            } else {
                                encodedFileName += "." + format;
                            }
                        }

                        // 12.4.4 Thiết lập Header Content-Disposition
                        // để trình duyệt tải file thay vì mở trực tiếp
                        response.setHeader(
                                "Content-Disposition",
                                "attachment; filename=\"" + encodedFileName
                                        + "\"; filename*=UTF-8''" + encodedFileName
                        );

                        // =========================
                        // 12.5. Xử lý chất lượng ảnh và Stream truyền tải
                        // =========================

                        if ((quality == null || quality.equals("original")) && !isFormatChanged) {
                            // Tải ảnh gốc
                            response.setContentLength((int) file.length());
                            try (
                                    FileInputStream in = new FileInputStream(file);
                                    OutputStream out = response.getOutputStream()
                            ) {
                                byte[] buffer = new byte[8192];
                                int length;
                                while ((length = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, length);
                                }
                            }
                        } else {
                            // Tải ảnh theo chất lượng (Resize) hoặc thay đổi định dạng
                            BufferedImage originalImage = ImageIO.read(file);
                            if (originalImage == null) {
                                response.sendError(500, "Không thể đọc định dạng ảnh này để xử lý.");
                                return;
                            }

                            int targetWidth = originalImage.getWidth();
                            if (quality != null && !quality.equals("original")) {
                                switch (quality) {
                                    case "high": targetWidth = 1920; break;
                                    case "medium": targetWidth = 1280; break;
                                    case "low": targetWidth = 854; break;
                                }
                                // Không phóng to nếu kích thước gốc đã nhỏ hơn yêu cầu
                                if (targetWidth > originalImage.getWidth()) {
                                    targetWidth = originalImage.getWidth();
                                }
                            }

                            int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));
                                
                            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                            if (format.equals("jpg")) {
                                type = BufferedImage.TYPE_INT_RGB;
                            }
                            
                            BufferedImage processedImage = new BufferedImage(targetWidth, targetHeight, type);
                            Graphics2D g2d = processedImage.createGraphics();
                            
                            // Tối ưu hóa: Bật chế độ khử răng cưa và nội suy chất lượng cực cao
                            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            
                            // Nếu chuyển sang JPG, tô nền trắng để tránh viền đen khi ảnh gốc có trong suốt
                            if (format.equals("jpg")) {
                                g2d.setColor(java.awt.Color.WHITE);
                                g2d.fillRect(0, 0, targetWidth, targetHeight);
                            }
                            
                            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
                            g2d.dispose();

                            // Ghi ảnh đã xử lý ra ByteArrayOutputStream để lấy dung lượng
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            
                            // Nâng cấp: Thiết lập xuất ảnh JPG với chất lượng 100% (Không nén giảm chất lượng)
                            if (format.equals("jpg")) {
                                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                                if (writers.hasNext()) {
                                    ImageWriter writer = writers.next();
                                    ImageWriteParam param = writer.getDefaultWriteParam();
                                    if (param.canWriteCompressed()) {
                                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                                        param.setCompressionQuality(1.0f); // 1.0f = Max Quality
                                    }
                                    try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                                        writer.setOutput(ios);
                                        writer.write(null, new IIOImage(processedImage, null, null), param);
                                    }
                                    writer.dispose();
                                } else {
                                    ImageIO.write(processedImage, format, baos);
                                }
                            } else {
                                ImageIO.write(processedImage, format, baos);
                            }

                            response.setContentLength(baos.size());
                            try (OutputStream out = response.getOutputStream()) {
                                baos.writeTo(out);
                            }
                        }

                    } else {

                        // =========================
                        // Alternative Flow 7.1
                        // 12.3.3 – File không tồn tại trên server
                        // =========================

                        // Trả về HTTP 404
                        response.sendError(404, "File không tồn tại trên server!");
                    }
                }

            } catch (Exception e) {

                // =========================
                // Exception Flow 8.1
                // 12.1.2 – Lỗi định dạng ID
                // =========================

                // Ghi log lỗi để quản trị viên theo dõi
                e.printStackTrace();
            }
        }
    }
}