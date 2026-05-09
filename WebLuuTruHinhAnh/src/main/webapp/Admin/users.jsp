<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <style>

        *{
            font-family: 'Inter', sans-serif;
        }

        body{
            background: #eef3f8;
        }

        .wrapper{
            display: flex;
        }

        .sidebar{
            width: 260px;
            min-height: 100vh;
            background: linear-gradient(180deg,#0f172a,#1e293b);
            position: fixed;
            color: white;
        }

        .logo{
            background: #38bdf8;
            padding: 22px;
            text-align: center;
            font-size: 30px;
            font-weight: 800;
        }

        .menu-item{
            display: flex;
            align-items: center;
            gap: 14px;
            color: #e2e8f0;
            text-decoration: none;
            padding: 16px 25px;
            transition: .3s;
            font-weight: 500;
        }

        .menu-item:hover,
        .menu-item.active{
            background: #38bdf8;
            color: white;
            padding-left: 32px;
        }

        .main{
            margin-left: 260px;
            width: calc(100% - 260px);
        }

        .topbar{
            background: white;
            padding: 24px 40px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
        }

        .topbar h2{
            font-size: 34px;
            font-weight: 800;
            margin: 0;
        }

        .content{
            padding: 40px;
        }

        .table-box{
            background: white;
            border-radius: 24px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0,0,0,0.06);
        }

        table{
            margin: 0 !important;
        }

        thead{
            background: #38bdf8;
            color: white;
        }

        th{
            padding: 18px !important;
            font-size: 15px;
            font-weight: 700;
        }

        td{
            vertical-align: middle;
            padding: 18px !important;
        }

        tbody tr:hover{
            background: #f8fbff;
        }

        .avatar{
            width: 60px;
            height: 60px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #dbeafe;
        }

        .badge-admin{
            background: #dbeafe;
            color: #2563eb;
            padding: 8px 16px;
            border-radius: 999px;
            font-size: 13px;
            font-weight: 700;
        }

        .badge-user{
            background: #dcfce7;
            color: #16a34a;
            padding: 8px 16px;
            border-radius: 999px;
            font-size: 13px;
            font-weight: 700;
        }

        .btn-delete{
            background: #ef4444;
            color: white;
            border: none;
            border-radius: 12px;
            padding: 10px 16px;
            font-weight: 600;
            transition: .3s;
        }

        .btn-delete:hover{
            background: #dc2626;
        }

    </style>
</head>
<body>

<div class="wrapper">

    <!-- SIDEBAR -->
    <div class="sidebar">

        <div class="logo">
            Admin
        </div>

        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="menu-item">
            <i class="fa-solid fa-house"></i>
            Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/admin/users"
           class="menu-item active">
            <i class="fa-solid fa-users"></i>
            Người dùng
        </a>

        <a href="${pageContext.request.contextPath}/admin/images"
           class="menu-item">
            <i class="fa-solid fa-image"></i>
            Hình ảnh
        </a>
    </div>


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

