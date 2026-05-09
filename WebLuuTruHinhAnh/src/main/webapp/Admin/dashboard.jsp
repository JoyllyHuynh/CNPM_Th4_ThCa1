<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <style>

        *{
            font-family: 'Inter', sans-serif;
        }

        body{
            background: #eef3f8;
            overflow-x: hidden;
        }

        .sidebar{
            width: 260px;
            min-height: 100vh;
            background: linear-gradient(180deg,#0f172a,#1e293b);
            position: fixed;
            left: 0;
            top: 0;
            color: white;
        }

        .logo{
            background: #38bdf8;
            padding: 22px;
            font-size: 32px;
            font-weight: 800;
            text-align: center;
        }

        .admin-info{
            text-align: center;
            padding: 30px 20px;
            border-bottom: 1px solid rgba(255,255,255,0.08);
        }

        .admin-avatar{
            width: 85px;
            height: 85px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #38bdf8;
            margin-bottom: 10px;
        }

        .online{
            color: #22c55e;
            font-size: 14px;
        }

        .menu-title{
            padding: 18px 25px 10px;
            font-size: 12px;
            color: #94a3b8;
            text-transform: uppercase;
            letter-spacing: 2px;
            font-weight: 700;
        }

        .menu-item{
            display: flex;
            align-items: center;
            gap: 14px;
            color: #e2e8f0;
            text-decoration: none;
            padding: 16px 25px;
            transition: .25s;
            font-weight: 500;
        }

        .menu-item:hover{
            background: rgba(56,189,248,0.15);
            color: white;
            padding-left: 32px;
        }

        .menu-item.active{
            background: #38bdf8;
            color: white;
            font-weight: 700;
        }

        .main{
            margin-left: 260px;
            min-height: 100vh;
        }

        .topbar{
            background: white;
            padding: 22px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 20px rgba(0,0,0,0.05);
        }

        .top-title h2{
            margin: 0;
            font-size: 34px;
            font-weight: 800;
            color: #0f172a;
        }

        .top-title p{
            color: #94a3b8;
            margin-top: 5px;
        }

        .content{
            padding: 40px;
        }

        .stats-card{
            border-radius: 24px;
            color: white;
            padding: 30px;
            position: relative;
            overflow: hidden;
            transition: .3s;
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
        }

        .stats-card:hover{
            transform: translateY(-6px);
        }

        .stats-card i{
            position: absolute;
            right: 20px;
            top: 20px;
            font-size: 70px;
            opacity: .15;
        }

        .stats-card h1{
            font-size: 56px;
            font-weight: 800;
            margin-bottom: 5px;
        }

        .stats-card p{
            font-size: 18px;
            margin: 0;
        }

        .blue{
            background: linear-gradient(135deg,#0ea5e9,#38bdf8);
        }

        .green{
            background: linear-gradient(135deg,#16a34a,#4ade80);
        }

        .red{
            background: linear-gradient(135deg,#dc2626,#f87171);
        }

        .action-card{
            background: white;
            border-radius: 24px;
            padding: 35px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.06);
            transition: .3s;
            height: 100%;
            text-decoration: none;
            display: block;
        }

        .action-card:hover{
            transform: translateY(-5px);
        }

        .action-icon{
            width: 90px;
            height: 90px;
            border-radius: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 40px;
            margin-bottom: 25px;
        }

        .bg-blue-soft{
            background: #dbeafe;
            color: #2563eb;
        }

        .bg-red-soft{
            background: #fee2e2;
            color: #dc2626;
        }

        .action-card h3{
            color: #0f172a;
            font-size: 30px;
            font-weight: 800;
            margin-bottom: 10px;
        }

        .action-card p{
            color: #64748b;
            line-height: 1.8;
            margin: 0;
        }

    </style>
</head>
<body>

<!-- SIDEBAR -->
<div class="sidebar">

    <div class="logo">
        Admin
    </div>

    <div class="admin-info">

        <img src="https://i.pravatar.cc/150?img=12"
             class="admin-avatar">

        <h5 class="fw-bold mt-2 mb-1">
            System Admin
        </h5>

        <div class="online">
            ● Online
        </div>
    </div>

    <div class="menu-title">
        MENU ADMIN
    </div>

    <a href="${pageContext.request.contextPath}/admin/dashboard"
       class="menu-item active">

        <i class="fa-solid fa-house"></i>
        Dashboard
    </a>

    <a href="${pageContext.request.contextPath}/admin/users"
       class="menu-item">

        <i class="fa-solid fa-users"></i>
        Quản lý người dùng
    </a>

    <a href="${pageContext.request.contextPath}/admin/images"
       class="menu-item">

        <i class="fa-solid fa-image"></i>
        Quản lý ảnh
    </a>

    <a href="${pageContext.request.contextPath}/logout"
       class="menu-item">

        <i class="fa-solid fa-right-from-bracket"></i>
        Đăng xuất
    </a>

</div>


<!-- MAIN -->
<div class="main">

    <!-- TOPBAR -->
    <div class="topbar">

        <div class="top-title">
            <h2>Tổng quan hệ thống</h2>
            <p>Trang quản trị LensVault</p>
        </div>

        <div class="d-flex align-items-center gap-3">
            <i class="fa-solid fa-user-shield fs-3 text-info"></i>

            <div>
                <div class="fw-bold">
                    Administrator
                </div>

                <small class="text-secondary">
                    ADMIN
                </small>
            </div>
        </div>
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

