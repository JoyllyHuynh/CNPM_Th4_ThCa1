<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    String adminPath = request.getRequestURI();
    String contextPath = request.getContextPath();
    if (adminPath != null && contextPath != null && adminPath.startsWith(contextPath)) {
        adminPath = adminPath.substring(contextPath.length());
    }
    if (adminPath == null) {
        adminPath = "";
    }
    boolean isDashboard = adminPath.startsWith("/admin/dashboard");
    boolean isUsers = adminPath.startsWith("/admin/users");
    boolean isImages = adminPath.startsWith("/admin/images");
%>

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
       class="menu-item <%= isDashboard ? "active" : "" %>">
        <i class="fa-solid fa-house"></i>
        Dashboard
    </a>

    <a href="${pageContext.request.contextPath}/admin/users"
       class="menu-item <%= isUsers ? "active" : "" %>">
        <i class="fa-solid fa-users"></i>
        Quản lý người dùng
    </a>

    <a href="${pageContext.request.contextPath}/admin/images"
       class="menu-item <%= isImages ? "active" : "" %>">
        <i class="fa-solid fa-image"></i>
        Quản lý ảnh
    </a>

    <a href="${pageContext.request.contextPath}/logout"
       class="menu-item">
        <i class="fa-solid fa-right-from-bracket"></i>
        Đăng xuất
    </a>
</div>

<script>
    (function () {
        var path = window.location.pathname || "";
        var context = "<%= request.getContextPath() %>";
        if (context && path.indexOf(context) === 0) {
            path = path.slice(context.length);
        }

        var links = document.querySelectorAll(".sidebar .menu-item");
        links.forEach(function (link) {
            var linkPath = new URL(link.href, window.location.origin).pathname;
            if (context && linkPath.indexOf(context) === 0) {
                linkPath = linkPath.slice(context.length);
            }
            if (path.indexOf(linkPath) === 0) {
                link.classList.add("active");
            }
        });
    })();
</script>
