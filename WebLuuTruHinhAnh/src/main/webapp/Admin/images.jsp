<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Image" %>

<%
    List<Image> images = (List<Image>) request.getAttribute("images");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý ảnh</title>

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

        .image-card{
            background: white;
            border-radius: 24px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0,0,0,0.06);
            transition: .3s;
            height: 100%;
        }

        .image-card:hover{
            transform: translateY(-5px);
        }

        .image-thumb{
            width: 100%;
            height: 240px;
            object-fit: cover;
        }

        .image-body{
            padding: 24px;
        }

        .image-title{
            font-size: 22px;
            font-weight: 800;
            color: #0f172a;
            margin-bottom: 12px;
        }

        .image-info{
            color: #64748b;
            margin-bottom: 8px;
        }

        .btn-delete{
            width: 100%;
            margin-top: 20px;
            background: #ef4444;
            border: none;
            padding: 14px;
            border-radius: 14px;
            color: white;
            font-weight: 700;
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
           class="menu-item">
            <i class="fa-solid fa-users"></i>
            Người dùng
        </a>

        <a href="${pageContext.request.contextPath}/admin/images"
           class="menu-item active">
            <i class="fa-solid fa-image"></i>
            Hình ảnh
        </a>
    </div>


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

                        <img src="${pageContext.request.contextPath}/uploads/<%= image.getFilePath() %>"
                             class="image-thumb">

                        <div class="image-body">

                            <div class="image-title">
                                <%= image.getFileName() %>
                            </div>

                            <div class="image-info">
                                <i class="fa-solid fa-eye"></i>
                                Lượt xem: <%= image.getViewCount() %>
                            </div>

                            <div class="image-info">
                                <i class="fa-solid fa-calendar"></i>
                                <%= image.getUploadDate() %>
                            </div>

                            <div class="image-info">
                                <i class="fa-solid fa-align-left"></i>
                                <%= image.getDescription() %>
                            </div>

                            <a href="${pageContext.request.contextPath}/admin/delete-image?id=<%= image.getId() %>">

                                <button class="btn-delete">
                                    <i class="fa-solid fa-trash"></i>
                                    Xóa ảnh vi phạm
                                </button>
                            </a>

                        </div>
                    </div>
                </div>

                <% } %>

            </div>
        </div>
    </div>
</div>

</body>
</html>
