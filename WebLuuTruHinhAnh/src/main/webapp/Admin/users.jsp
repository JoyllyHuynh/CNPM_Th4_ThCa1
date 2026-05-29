<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.User" %>

<%
    List<User> users = (List<User>) request.getAttribute("users");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
          rel="stylesheet">

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
                            <img src="<%= user.getAvatar() != null ? user.getAvatar() : "https://i.pravatar.cc/100" %>"
                                 class="avatar">
                        </td>

                        <td>
                            <%= user.getEmail() %>
                        </td>

                        <td>
                            <strong><%= user.getFullName() %></strong>
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
                            <a href="${pageContext.request.contextPath}/admin/delete-user?id=<%= user.getId() %>">
                                <button class="btn-delete">
                                    <i class="fa-solid fa-trash"></i>
                                    Xóa
                                </button>
                            </a>
                        </td>
                    </tr>

                    <% } %>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>
</html>

