<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/images.css">
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
