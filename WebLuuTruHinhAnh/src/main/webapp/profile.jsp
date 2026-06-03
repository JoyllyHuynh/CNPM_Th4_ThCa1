<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>LensVault - Hồ sơ của tôi</title>
                <link rel="preconnect" href="https://fonts.googleapis.com" />
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet" />
                <link
                    href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap"
                    rel="stylesheet" />
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/header.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
            </head>

            <body>
                <c:set var="activeTopNav" value="profile" />
                <jsp:include page="/user/menu.jsp" />
                <jsp:include page="/user/header.jsp" />

                <div class="main-wrapper">
                    <main class="main-canvas">

                        <div class="page-header-box">
                            <div class="left-title">
                                <h1 class="page-title">My Profile</h1>
                                <p class="page-subtitle">View and manage your account information.</p>
                            </div>
                        </div>

                        <div class="profile-layout">

                            <!-- Avatar card -->
                            <div class="profile-avatar-card">
                                <div class="avatar-circle">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.user.fullName}">
                                            ${fn:toUpperCase(fn:substring(sessionScope.user.fullName, 0, 1))}
                                        </c:when>
                                        <c:otherwise>U</c:otherwise>
                                    </c:choose>
                                </div>
                                <h2 class="avatar-name">${sessionScope.user.fullName}</h2>
                                <p class="avatar-email">${sessionScope.user.email}</p>
                                <span class="role-badge ${sessionScope.user.role == 'ADMIN' ? 'admin' : 'user'}">
                                    ${sessionScope.user.role == 'ADMIN' ? 'Quản trị viên' : 'Thành viên'}
                                </span>
                            </div>

                            <!-- Info card -->
                            <div class="profile-info-card">
                                <h3 class="info-card-title">Thông tin chi tiết</h3>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">person</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Họ và tên</p>
                                        <p class="info-value">${sessionScope.user.fullName}</p>
                                    </div>
                                </div>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">email</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Email</p>
                                        <p class="info-value">${sessionScope.user.email}</p>
                                    </div>
                                </div>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">shield_person</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Vai trò</p>
                                        <p class="info-value">${sessionScope.user.role == 'ADMIN' ? 'Quản trị viên' :
                                            'Thành viên'}</p>
                                    </div>
                                </div>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">verified_user</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Trạng thái tài khoản</p>
                                        <p class="info-value">
                                            <span
                                                class="status-dot ${sessionScope.user.status == 'ACTIVE' ? 'active' : 'inactive'}"></span>
                                            ${sessionScope.user.status == 'ACTIVE' ? 'Đang hoạt động' :
                                            sessionScope.user.status}
                                        </p>
                                    </div>
                                </div>

                                <c:if test="${not empty sessionScope.user.createdAt}">
                                    <div class="info-row">
                                        <div class="info-icon-wrap">
                                            <span class="material-symbols-outlined">calendar_month</span>
                                        </div>
                                        <div class="info-detail">
                                            <p class="info-label">Ngày tham gia</p>
                                            <p class="info-value">${sessionScope.user.createdAt}</p>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                    </main>
                </div>
            </body>

            </html>