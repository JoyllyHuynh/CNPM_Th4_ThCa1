<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LensVault | Đăng nhập</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/lucide@latest"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body class="bg-slate-50 min-h-screen flex items-center justify-center p-6 text-slate-900">

<div class="flex bg-white rounded-[50px] shadow-2xl overflow-hidden max-w-5xl w-full h-[700px]">
    <!-- Phần hình ảnh bên trái -->
    <div class="hidden lg:block w-1/2 relative">
        <img src="https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&q=80"
             class="absolute inset-0 w-full h-full object-cover" alt="LoginBG">
        <div class="absolute inset-0 bg-blue-600/20 mix-blend-multiply"></div>
    </div>

    <!-- Phần form đăng nhập -->
    <div class="w-full lg:w-1/2 p-16 flex flex-col justify-center items-center">
        <div class="w-full max-w-sm">
            <a href="index.jsp" class="flex items-center gap-2 mb-12 text-slate-400 hover:text-blue-600 transition-colors">
                <i data-lucide="arrow-left" class="w-4 h-4"></i> Quay lại trang chủ
            </a>

            <h2 class="text-4xl font-bold mb-2">Đăng nhập</h2>
            <p class="text-slate-400 mb-10">Bắt đầu quản lý tài sản số của bạn.</p>

            <!-- Form Đăng nhập -->
            <form class="space-y-6" action="${pageContext.request.contextPath}/login" method="POST">
                <div class="space-y-2">
                    <label class="text-[10px] font-bold uppercase tracking-widest text-slate-400">Email Address</label>
                    <input type="email" name="email" placeholder="alex@lensvault.com"
                           class="w-full px-6 py-4 rounded-2xl bg-slate-50 border-2 border-transparent focus:ring-2 focus:ring-blue-400 outline-none transition-all font-medium" required>
                </div>

                <div class="space-y-2">
                    <label class="text-[10px] font-bold uppercase tracking-widest text-slate-400">Secure Password</label>
                    <input type="password" name="password" placeholder="••••••••"
                           class="w-full px-6 py-4 rounded-2xl bg-slate-50 border-2 border-transparent focus:ring-2 focus:ring-blue-400 outline-none transition-all font-medium" required>
                </div>

                <button type="submit"
                        class="w-full py-5 bg-blue-600 text-white rounded-2xl font-bold shadow-xl shadow-blue-100 hover:shadow-2xl hover:scale-[1.02] transition-all border-none cursor-pointer">
                    Đăng nhập vào Vault
                </button>
            </form>

            <!-- Link chuyển sang trang đăng ký -->
            <p class="text-center text-slate-500 mt-8">
                Chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register.jsp"
                   class="text-blue-600 font-semibold hover:underline">
                    Đăng ký ngay
                </a>
            </p>
        </div>
    </div>
</div>

<script>lucide.createIcons();</script>
</body>
</html>