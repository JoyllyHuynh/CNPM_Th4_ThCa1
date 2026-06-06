<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
    <%@ page import="java.util.List" %>
        <%@ page import="model.Image" %>

            <% List<Image> images = (List<Image>) request.getAttribute("images");
                    %>

                    <!DOCTYPE html>
                    <html lang="vi">

                    <head>
                        <meta charset="UTF-8">
                        <title>Quản lý ảnh</title>

                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                            rel="stylesheet">

                        <link rel="stylesheet"
                            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

                        <link
                            href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
                            rel="stylesheet">

                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/sidebar.css?v=2">
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/images.css?v=2">
                    </head>

                    <body>

                        <div class="wrapper">

                            <!-- SIDEBAR -->
                            <%@ include file="/Admin/sidebar.jsp" %>


                                <!-- MAIN -->
                                <div class="main">

                                    <div class="topbar">
                                        <h2>Quản lý ảnh hệ thống</h2>
                                    </div>

                                    <div class="content">

                                        <div class="row g-4">

                                            <% for(Image image : images){ %>

                                                <div class="col-lg-4 col-md-6">

                                                    <div class="image-card">

                                                        <a href="${pageContext.request.contextPath}/admin/image-detail?id=<%= image.getId() %>" title="Xem chi tiết ảnh">
                                                            <img src="${pageContext.request.contextPath}/uploads/<%= image.getFilePath() %>"
                                                                class="image-thumb">
                                                        </a>

                                                        <div class="image-body">

                                                            <div class="image-title">
                                                                <%= image.getFileName() %>
                                                            </div>

                                                            <div class="image-info">
                                                                <i class="fa-solid fa-calendar"></i>
                                                                <%= image.getUploadDate() %>
                                                            </div>

                                                            <div class="image-info">
                                                                <i class="fa-solid fa-hard-drive"></i>
                                                                <% long sizeBytes=image.getFileSize(); if(sizeBytes>=
                                                                    1024 * 1024) {
                                                                    out.print(String.format("%.2f MB", (double)sizeBytes
                                                                    / (1024 * 1024)));
                                                                    } else if(sizeBytes >= 1024) {
                                                                    out.print(String.format("%.2f KB", (double)sizeBytes
                                                                    / 1024));
                                                                    } else {
                                                                    out.print(sizeBytes + " Bytes");
                                                                    }
                                                                    %>
                                                            </div>

                                                                <button class="btn-delete" onclick="showConfirmModal('Bạn có chắc chắn muốn xóa ảnh này không?', '${pageContext.request.contextPath}/admin/delete-image?id=<%= image.getId() %>', 'btn-danger')">
                                                                    <i class="fa-solid fa-trash"></i>
                                                                    Xóa ảnh vi phạm
                                                                </button>

                                                        </div>
                                                    </div>
                                                </div>

                                                <% } %>

                                        </div>
                                    </div>
                                </div>
                        </div>

<!-- Modal Xác Nhận -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="confirmModalLabel">Xác nhận thao tác</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body" id="confirmModalMessage">
        Bạn có chắc chắn muốn thực hiện hành động này?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
        <a href="#" id="confirmModalActionBtn" class="btn btn-primary">Xác nhận</a>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function showConfirmModal(message, actionUrl, btnClass) {
        document.getElementById('confirmModalMessage').innerText = message;
        var actionBtn = document.getElementById('confirmModalActionBtn');
        actionBtn.href = actionUrl;
        
        // Cập nhật màu nút (tùy chọn)
        actionBtn.className = 'btn ' + btnClass;
        
        var myModal = new bootstrap.Modal(document.getElementById('confirmModal'));
        myModal.show();
    }
</script>

                    </body>

                    </html>