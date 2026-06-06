<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="model.Image" %>
<%@ page import="model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<% 
    Image image = (Image) request.getAttribute("image");
    User uploader = (User) request.getAttribute("uploader");
%>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Chi tiết ảnh - Admin</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/sidebar.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/images.css?v=2">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/image-detail.css?v=1">
</head>

<body>

    <div class="wrapper">
        <!-- SIDEBAR -->
        <%@ include file="/Admin/sidebar.jsp" %>

        <!-- MAIN -->
        <div class="main">
            <div class="topbar">
                <h2>Chi tiết ảnh</h2>
            </div>

            <div class="content">
                <div class="topbar-actions">
                    <a href="${pageContext.request.contextPath}/admin/images" class="btn btn-outline-secondary">
                        <i class="fa-solid fa-arrow-left me-2"></i> Quay lại
                    </a>
                </div>

                <div class="image-detail-container">
                    <div class="image-preview">
                        <img src="${pageContext.request.contextPath}/uploads/<%= image.getFilePath() %>" alt="<%= image.getFileName() %>">
                    </div>
                    
                    <div class="image-info-sidebar">
                        <div class="info-card">
                            <h5 class="mb-4 text-primary"><i class="fa-solid fa-circle-info me-2"></i>Thông tin chi tiết</h5>
                            
                            <div class="info-item">
                                <div class="info-icon"><i class="fa-solid fa-image"></i></div>
                                <div class="info-content">
                                    <div class="label">Tên file</div>
                                    <div class="value"><%= image.getFileName() %></div>
                                </div>
                            </div>
                            
                            <div class="info-item">
                                <div class="info-icon"><i class="fa-solid fa-user"></i></div>
                                <div class="info-content">
                                    <div class="label">Người Upload</div>
                                    <div class="value">
                                        <a href="${pageContext.request.contextPath}/UserProfile?id=<%= uploader.getId() %>" target="_blank" style="text-decoration: none;">
                                            <%= uploader.getFullName() %> (<%= uploader.getEmail() %>)
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-icon"><i class="fa-solid fa-calendar"></i></div>
                                <div class="info-content">
                                    <div class="label">Ngày Upload</div>
                                    <div class="value"><%= image.getUploadDate() %></div>
                                </div>
                            </div>

                            <div class="info-item">
                                <div class="info-icon"><i class="fa-solid fa-hard-drive"></i></div>
                                <div class="info-content">
                                    <div class="label">Dung lượng</div>
                                    <div class="value">
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
                                </div>
                            </div>
                            
                            <div class="info-item">
                                <div class="info-icon"><i class="fa-solid fa-download"></i></div>
                                <div class="info-content">
                                    <div class="label">Lượt tải</div>
                                    <div class="value"><%= image.getDownloadCount() %></div>
                                </div>
                            </div>
                        </div>

                        <a href="${pageContext.request.contextPath}/DownloadServlet?id=<%= image.getId() %>" class="btn-download-lg mt-3">
                            <i class="fa-solid fa-download"></i> Tải ảnh xuống
                        </a>

                        <button class="btn-delete-lg mt-2" onclick="showConfirmModal('Bạn có chắc chắn muốn xóa ảnh này khỏi hệ thống?', '${pageContext.request.contextPath}/admin/delete-image?id=<%= image.getId() %>', 'btn-danger')">
                            <i class="fa-solid fa-trash"></i> Xóa ảnh vi phạm
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Xác Nhận -->
    <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Xác nhận thao tác</h5>
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
            actionBtn.className = 'btn ' + btnClass;
            var myModal = new bootstrap.Modal(document.getElementById('confirmModal'));
            myModal.show();
        }
    </script>
</body>
</html>
