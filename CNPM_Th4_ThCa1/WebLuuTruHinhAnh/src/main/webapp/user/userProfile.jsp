<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bộ sưu tập của ${profileUser.fullName}</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/user/variables.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/user/menu.css">

    <style>

        body{
            margin:0;
            background:#f4f6fb;
            font-family:Inter,sans-serif;
        }

        .main-content{
            margin-left:250px;
            min-height:100vh;
            padding:30px;
        }

        /* ==========================
           PROFILE HEADER
        ========================== */

        .profile-banner{
            background:white;
            border-radius:24px;
            padding:30px;
            display:flex;
            align-items:center;
            gap:24px;
            box-shadow:0 8px 25px rgba(0,0,0,.06);
            margin-bottom:30px;
        }

        .avatar-circle{
            width:100px;
            height:100px;
            border-radius:50%;
            background:linear-gradient(
                    135deg,
                    #2563eb,
                    #7c3aed
            );

            color:white;
            display:flex;
            align-items:center;
            justify-content:center;

            font-size:42px;
            font-weight:700;
        }

        .profile-info h1{
            margin:0;
            font-size:32px;
            color:#1f2937;
        }

        .profile-info p{
            margin:8px 0;
            color:#6b7280;
        }

        .stats{
            display:flex;
            gap:15px;
            margin-top:15px;
        }

        .stat-box{
            background:#eef4ff;
            padding:12px 18px;
            border-radius:14px;
            min-width:110px;
        }

        .stat-box strong{
            display:block;
            font-size:24px;
            color:#2563eb;
        }

        .stat-box span{
            color:#666;
            font-size:13px;
        }

        /* ==========================
           GALLERY TITLE
        ========================== */

        .section-title{
            margin-bottom:20px;
            font-size:22px;
            font-weight:600;
            color:#1f2937;
        }

        /* ==========================
           GALLERY
        ========================== */

        .gallery{
            display:grid;
            grid-template-columns:repeat(4, 1fr);
            gap:20px;
        }

        @media(max-width:1400px){
            .gallery{
                grid-template-columns:repeat(3,1fr);
            }
        }

        @media(max-width:1000px){
            .gallery{
                grid-template-columns:repeat(2,1fr);
            }
        }

        @media(max-width:700px){
            .gallery{
                grid-template-columns:1fr;
            }
        }

        .photo-card{
            background:white;
            border-radius:18px;
            overflow:hidden;
            box-shadow:0 4px 12px rgba(0,0,0,.08);
            transition:.25s;
        }

        .photo-card:hover{
            transform:translateY(-6px);
            box-shadow:0 12px 24px rgba(0,0,0,.12);
        }

        .photo-card img{
            width:100%;
            height:240px;
            object-fit:cover;
            display:block;
        }

        .card-content{
            padding:16px;
        }

        .file-name{
            margin:0;
            font-weight:600;
            color:#1f2937;
            white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis;
        }

        .meta{
            margin-top:10px;
            display:flex;
            justify-content:space-between;
            color:#6b7280;
            font-size:13px;
        }

        /* ==========================
           EMPTY
        ========================== */

        .empty-state{
            background:white;
            border-radius:20px;
            padding:80px 20px;
            text-align:center;
            box-shadow:0 4px 12px rgba(0,0,0,.05);
        }

        .empty-state h3{
            margin:0;
            color:#374151;
        }

        .empty-state p{
            color:#6b7280;
        }

    </style>
</head>

<body>

<jsp:include page="/user/menu.jsp"/>

<div class="main-content">

    <!-- PROFILE -->

    <div class="profile-banner">

        <div class="avatar-circle">
            ${profileUser.fullName.substring(0,1)}
        </div>

        <div class="profile-info">

            <h1>
                ${profileUser.fullName}
            </h1>

            <div class="stats">

                <div class="stat-box">
                    <strong>${images.size()}</strong>
                    <span>Ảnh đã tải lên</span>
                </div>

            </div>

        </div>

    </div>

    <!-- GALLERY -->

    <div class="section-title">
        📸 Ảnh đã tải lên
    </div>

    <c:choose>

        <c:when test="${not empty images}">

            <div class="gallery">

                <c:forEach var="img" items="${images}">

                    <div class="photo-card">

                        <a href="${pageContext.request.contextPath}/ImageDetail?id=${img.id}">

                            <img
                                    src="${pageContext.request.contextPath}/uploads/${img.filePath}"
                                    alt="${img.fileName}">

                        </a>

                        <div class="card-content">

                            <p class="file-name">
                                    ${img.fileName}
                            </p>

                            <div class="meta">

                                <span>
                                    📅 ${img.uploadDate}
                                </span>

                                <span>
                                    👁 ${img.viewCount}
                                </span>

                            </div>

                        </div>

                    </div>

                </c:forEach>

            </div>

        </c:when>

        <c:otherwise>

            <div class="empty-state">

                <h3>
                    Chưa có ảnh nào
                </h3>

                <p>
                    Người dùng này chưa tải lên ảnh.
                </p>

            </div>

        </c:otherwise>

    </c:choose>

</div>

</body>
</html>
```
