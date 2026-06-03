<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
    <%@ page import="java.util.List" %>
        <%@ page import="model.User" %>

            <% List<User> users = (List<User>) request.getAttribute("users");
                    %>

                    <!DOCTYPE html>
                    <html lang="vi">

                    <head>
                        <meta charset="UTF-8">
                        <title>Quản lý người dùng</title>

                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                            rel="stylesheet">

                        <link rel="stylesheet"
                            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

                        <link
                            href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
                            rel="stylesheet">

                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/sidebar.css?v=2">
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/users.css">
                    </head>

                    <body>

                        <div class="wrapper">

                            <!-- SIDEBAR -->
                            <%@ include file="/Admin/sidebar.jsp" %>


                                <!-- MAIN -->
                                <div class="main">

                                    <div class="topbar">
                                        <h2>Quản lý người dùng</h2>
                                    </div>

                                    <div class="content">

                                        <div class="table-box">

                                            <table class="table align-middle">

                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Avatar</th>
                                                        <th>Email</th>
                                                        <th>Họ tên</th>
                                                        <th>Role</th>
                                                        <th>Trạng thái</th>
                                                        <th>Ngày tạo</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>

                                                <tbody>

                                                    <% for(User user : users){ %>

                                                        <tr>
                                                            <td>
                                                                <strong>#<%= user.getId() %></strong>
                                                            </td>

                                                            <td>
                                                                <% String avatar=user.getAvatar(); if (avatar !=null &&
                                                                    !avatar.trim().isEmpty()) { %>
                                                                    <img src="<%= avatar %>" class="avatar">
                                                                    <% } else { String name=user.getFullName(); String
                                                                        letter="U" ; if (name !=null &&
                                                                        !name.trim().isEmpty()) {
                                                                        letter=name.trim().substring(0,
                                                                        1).toUpperCase(); } %>
                                                                        <div class="avatar-letter">
                                                                            <%= letter %>
                                                                        </div>
                                                                        <% } %>
                                                            </td>

                                                            <td>
                                                                <%= user.getEmail() %>
                                                            </td>

                                                            <td>
                                                                <strong>
                                                                    <%= user.getFullName() %>
                                                                </strong>
                                                            </td>

                                                            <td>
                                                                <% if("ADMIN".equalsIgnoreCase(user.getRole())){ %>

                                                                    <span class="badge-admin">
                                                                        ADMIN
                                                                    </span>

                                                                    <% } else { %>

                                                                        <span class="badge-user">
                                                                            USER
                                                                        </span>

                                                                        <% } %>
                                                            </td>

                                                            <td>
                                                                <%= user.getStatus() %>
                                                            </td>

                                                            <td>
                                                                <%= user.getCreatedAt() %>
                                                            </td>

                                                            <td>
                                                                <div style="display: flex; gap: 8px;">
                                                                    <% if ("ACTIVE".equalsIgnoreCase(user.getStatus()))
                                                                        { %>
                                                                        <button class="btn-lock"
                                                                            onclick="showConfirmModal('Bạn có chắc chắn muốn khóa người dùng này không?', '${pageContext.request.contextPath}/admin/lock-user?id=<%= user.getId() %>', 'btn-warning')">
                                                                            <i class="fa-solid fa-lock"></i>
                                                                            Khóa
                                                                        </button>
                                                                        <% } else { %>
                                                                            <button class="btn-unlock"
                                                                                onclick="showConfirmModal('Bạn có chắc chắn muốn mở khóa người dùng này không?', '${pageContext.request.contextPath}/admin/unlock-user?id=<%= user.getId() %>', 'btn-success')">
                                                                                <i class="fa-solid fa-unlock"></i>
                                                                                Mở
                                                                            </button>
                                                                            <% } %>
                                                                                <button class="btn-delete"
                                                                                    onclick="showConfirmModal('Bạn có chắc chắn muốn xóa người dùng này không?', '${pageContext.request.contextPath}/admin/delete-user?id=<%= user.getId() %>', 'btn-danger')">
                                                                                    <i class="fa-solid fa-trash"></i>
                                                                                    Xóa
                                                                                </button>
                                                                </div>
                                                            </td>
                                                        </tr>

                                                        <% } %>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                        </div>

                        <!-- Modal Xác Nhận -->
                        <div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel"
                            aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="confirmModalLabel">Xác nhận thao tác</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body" id="confirmModalMessage">
                                        Bạn có chắc chắn muốn thực hiện hành động này?
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">Hủy</button>
                                        <a href="#" id="confirmModalActionBtn" class="btn btn-primary">Xác nhận</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
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