<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
          rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">
</head>
<body>

<!-- SIDEBAR -->
<%@ include file="/Admin/sidebar.jsp" %>


<!-- MAIN -->
<div class="main">

    <!-- TOPBAR -->
    <div class="topbar">
        <h2>Tổng quan hệ thống</h2>
    </div>


    <!-- CONTENT -->
    <div class="content">

        <!-- STATS -->
        <div class="row g-4 mb-5">

            <div class="col-lg-4">
                <div class="stats-card blue">

                    <i class="fa-solid fa-users"></i>

                    <h1>${totalUsers}</h1>

                    <p>Tổng người dùng</p>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="stats-card green">

                    <i class="fa-solid fa-image"></i>

                    <h1>${totalImages}</h1>

                    <p>Ảnh toàn hệ thống</p>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="stats-card red">

                    <i class="fa-solid fa-triangle-exclamation"></i>

                    <h1>${deletedImages}</h1>

                    <p>Ảnh vi phạm</p>
                </div>
            </div>
        </div>


        <!-- ACTIONS -->
        <div class="row g-4">

            <div class="col-lg-6">

                <a href="${pageContext.request.contextPath}/admin/users"
                   class="action-card">

                    <div class="action-icon bg-blue-soft">
                        <i class="fa-solid fa-users"></i>
                    </div>

                    <h3>
                        Quản lý người dùng
                    </h3>

                    <p>
                        Xem danh sách tài khoản, xóa người dùng,
                        quản lý quyền truy cập và kiểm soát hệ thống.
                    </p>
                </a>
            </div>


            <div class="col-lg-6">

                <a href="${pageContext.request.contextPath}/admin/images"
                   class="action-card">

                    <div class="action-icon bg-red-soft">
                        <i class="fa-solid fa-image"></i>
                    </div>

                    <h3>
                        Quản lý ảnh
                    </h3>

                    <p>
                        Kiểm duyệt ảnh, xóa ảnh vi phạm,
                        theo dõi dữ liệu và nội dung toàn hệ thống.
                    </p>
                </a>
            </div>

        </div>
    </div>
</div>

</body>
</html>

