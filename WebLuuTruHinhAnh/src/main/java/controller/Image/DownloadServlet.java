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

        // 12.1.2. Hệ thống tiếp nhận các tham số được truyền lên từ client thông qua Request Parameter bao gồm: id, quality (chất lượng), và format (định dạng).
        String idStr = request.getParameter("id");
        String quality = request.getParameter("quality");
        String reqFormat = request.getParameter("format");        
        
        // =========================
        // [Sequence Diagram - Bước 1: Authentication & Authorization]
        // =========================

        // 12.1.3. Hệ thống kiểm tra phiên đăng nhập hiện tại từ HttpSession.
        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        // 12.3. Exception: Người dùng chưa đăng nhập
        // 12.3.1. Tại bước 12.1.3, nếu loggedInUser là null.
        if (loggedInUser == null) {
            // 12.3.2. Hệ thống dừng luồng tải ảnh.
            // 12.3.3. Hệ thống dùng response.sendRedirect để điều hướng người dùng về trang /login.jsp. Kết thúc luồng.
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 12.1.4. Hệ thống kiểm tra tham số id (khác null) và ép kiểu chuỗi sang số nguyên.
        if (idStr != null) {

            try {

                int id = Integer.parseInt(idStr);



                // =========================
                // 12.2. Truy vấn dữ liệu hình ảnh
                // =========================

                // 12.1.5. Controller gọi tầng nghiệp vụ ImageService để tìm thông tin ảnh bằng hàm getImageById(id).
                Image img = imageService.getImageById(id);

                // 12.1.6. Hệ thống xác nhận đối tượng ảnh img tồn tại hợp lệ trong cơ sở dữ liệu.
                // 12.5. Exception: Không tìm thấy ảnh trong database
                // 12.5.1. Tại bước 12.1.6, nếu đối tượng img trả về bằng null.
                // 12.5.2. Hệ thống dừng toàn bộ tiến trình xử lý mà không có file nào được trả về. Kết thúc luồng.
                if (img != null) {

                    // 12.1.7. Hệ thống kiểm tra quyền bằng cách so sánh img.getUserId() với loggedInUser.getId().
                    if (img.getUserId() != loggedInUser.getId()) {
                        // 12.6. Exception: Cố tình truy cập trái phép (Lỗi quyền sở hữu)
                        // 12.6.1. Tại bước 12.1.7, nếu ID người dùng hiện tại không khớp với userId của bức ảnh.
                        // 12.6.2. Hệ thống từ chối quyền truy cập, trả về mã lỗi HTTP 403 (Forbidden) kèm thông báo lỗi. Kết thúc luồng.
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền tải bức ảnh này vì bạn không phải là chủ sở hữu.");
                        return;
                    }



                    // =========================
                    // 12.3. Định vị vật lý và kiểm tra file trên server
                    // =========================

                    // 12.1.8. Hệ thống lấy đường dẫn vật lý tuyệt đối đến thư mục lưu trữ /uploads.
                    String uploadDir = getServletContext().getRealPath("/uploads");

                    // 12.1.9. Hệ thống khởi tạo đối tượng File dựa trên đường dẫn gốc và thuộc tính filePath của ảnh.
                    File file = new File(uploadDir, img.getFilePath());

                    // =========================
                    // [Sequence Diagram - Bước 2: Kiểm tra vật lý]
                    // =========================

                    // 12.1.10. Hệ thống xác minh file vật lý thực sự tồn tại trên ổ cứng.
                    if (file.exists()) {

                        // =========================
                        // 12.4. Cấu hình HTTP Response Header
                        // [Sequence Diagram - Bước 3: Cấu hình Header và Đuôi file]
                        // =========================

                        // 12.1.11. Hệ thống mã hóa tên tệp tin gốc sang UTF-8 (URLEncoder.encode).
                        String encodedFileName = URLEncoder.encode(
                                img.getFileName(),
                                StandardCharsets.UTF_8.toString()
                        ).replaceAll("\\+", "%20");

                        // 12.1.12. Hệ thống kiểm tra tham số format. Nếu người dùng yêu cầu đổi đuôi, hệ thống tiến hành cắt chuỗi và cập nhật lại phần mở rộng (extension) của tên file tải về.
                        String format = "jpg"; // Mặc định
                        String fileNameLower = img.getFileName().toLowerCase();
                        if (fileNameLower.endsWith(".png")) format = "png";
                        else if (fileNameLower.endsWith(".gif")) format = "gif";
                        
                        boolean isFormatChanged = false;
                        if (reqFormat != null && !reqFormat.equals("original") && !reqFormat.equals(format)) {
                            format = reqFormat;
                            isFormatChanged = true;
                            
                            int lastDot = encodedFileName.lastIndexOf('.');
                            if (lastDot > 0) {
                                encodedFileName = encodedFileName.substring(0, lastDot) + "." + format;
                            } else {
                                encodedFileName += "." + format;
                            }
                        }

                        // 12.1.13. Hệ thống thiết lập Content-Type: application/octet-stream.
                        response.setContentType("application/octet-stream");

                        // 12.1.14. Hệ thống thiết lập thuộc tính Content-Disposition với tham số attachment kèm theo tên file đã được chuẩn hóa.
                        response.setHeader(
                                "Content-Disposition",
                                "attachment; filename=\"" + encodedFileName
                                        + "\"; filename*=UTF-8''" + encodedFileName
                        );

                        // =========================
                        // 12.5. Xử lý chất lượng ảnh và Stream truyền tải
                        // [Sequence Diagram - Bước 4: Xử lý Quality & Format]
                        // =========================

                        // 12.1.15. Hệ thống đánh giá tham số quality và format. Nếu không thay đổi, truyền luồng gốc (FileInputStream ra OutputStream) và kết thúc nhánh hệ thống.
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
                            // 12.1.16. Nếu có thay đổi (Resize/Format), hệ thống tải file gốc vào bộ nhớ đồ họa (BufferedImage), tính toán kích thước mới giữ đúng tỷ lệ khung hình.
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
                            
                            // 12.1.17. Hệ thống sử dụng Graphics2D (chất lượng tối đa Bicubic, đổ nền trắng nếu xuất JPG) để vẽ lại ảnh.
                            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            
                            if (format.equals("jpg")) {
                                g2d.setColor(java.awt.Color.WHITE);
                                g2d.fillRect(0, 0, targetWidth, targetHeight);
                            }
                            
                            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
                            g2d.dispose();

                            // 12.1.18. Hệ thống ghi ảnh ra ByteArrayOutputStream (ép chất lượng 100% nếu là JPG), cấu hình Content-Length và đẩy byte ra HTTP Response. Đóng luồng.
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            
                            if (format.equals("jpg")) {
                                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                                if (writers.hasNext()) {
                                    ImageWriter writer = writers.next();
                                    ImageWriteParam param = writer.getDefaultWriteParam();
                                    if (param.canWriteCompressed()) {
                                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                                        param.setCompressionQuality(1.0f);
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
                        // 12.2. Alternative Flow: File ảnh không tồn tại vật lý trên ổ đĩa Server
                        // 12.2.1. Tại bước 12.1.10, nếu file gốc trong thư mục /uploads đã bị xóa hoặc mất.
                        // 12.2.2. Hệ thống ngừng luồng xử lý thiết lập Header và truyền tải file.
                        // =========================
                        // 12.2.3. Hệ thống trả về mã lỗi HTTP 404 cùng thông báo: File không tồn tại trên server!. Kết thúc luồng.
                        response.sendError(404, "File không tồn tại trên server!");
                    }
                }

            } catch (NumberFormatException e) {

                // =========================
                // 12.4. Exception: Lỗi định dạng tham số ID
                // 12.4.1. Tại bước 12.1.4, nếu tham số "id" không hợp lệ gây ra lỗi NumberFormatException.
                // =========================

                // 12.4.2. Hệ thống nhảy vào khối catch và ghi log lỗi. Luồng tải ảnh bị hủy bỏ. Kết thúc luồng.
                e.printStackTrace();
            }
        }
    }
}