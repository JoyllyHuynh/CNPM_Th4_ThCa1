<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>LensVault | Trang chủ</title>
                <script src="https://cdn.tailwindcss.com"></script>
                <script src="https://unpkg.com/lucide@latest"></script>
                <link
                    href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Cormorant+Garamond:ital,wght@1,600&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="assets/css/style.css">
            </head>

            <body class="bg-white text-slate-900">

                <!-- Navigation -->
                <nav class="flex items-center justify-between px-10 py-8 fixed top-0 w-full z-50 glass">
                    <div class="flex items-center gap-3">
                        <div
                            class="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center text-white shadow-xl shadow-blue-200">
                            <i data-lucide="camera" class="w-6 h-6"></i>
                        </div>
                        <span class="text-2xl font-bold tracking-tighter text-slate-900">LensVault</span>
                    </div>
                    <div class="flex items-center gap-4">
                        <a href="login.jsp"
                            class="px-6 py-2 text-sm font-bold text-slate-700 hover:text-blue-600 transition-all">Đăng
                            nhập</a>
                        <a href="login.jsp"
                            class="px-8 py-3 bg-slate-900 text-white rounded-full text-sm font-bold hover:shadow-2xl hover:scale-105 transition-all">Bắt
                            đầu ngay</a>
                    </div>
                </nav>

                <main class="pt-40 pb-20 px-6">
                    <div class="max-w-7xl mx-auto text-center relative">
                        <h1 class="text-7xl md:text-8xl font-bold mb-10 tracking-tighter leading-[0.9] text-slate-900">
                            Ký ức của bạn, <br>
                            <span
                                style="font-family: 'Cormorant Garamond', serif; font-style: italic; font-weight: 600;"
                                class="text-blue-600">bất tử với thời gian.</span>
                        </h1>
                        <p class="text-xl text-slate-500 max-w-2xl mx-auto mb-16 font-light leading-relaxed">
                            LensVault là nền tảng quản lý hình ảnh chuyên nghiệp dành cho các nhiếp ảnh gia. Bảo mật
                            tuyệt đối, tổ chức thông minh.
                        </p>

                        <div class="flex items-center justify-center gap-8 mb-32">
                            <a href="login.jsp"
                                class="px-12 py-5 bg-blue-600 text-white rounded-full text-lg font-bold hover:bg-blue-700 hover:shadow-2xl hover:scale-105 transition-all flex items-center gap-3 group">
                                Bắt đầu lưu trữ <i data-lucide="chevron-right"></i>
                            </a>
                        </div>

                        <!-- Hero Section Bento -->
                        <div class="grid grid-cols-1 md:grid-cols-12 gap-6 h-[600px] max-w-6xl mx-auto">
                            <div class="md:col-span-8 bg-slate-900 rounded-[50px] overflow-hidden shadow-2xl relative">
                                <img src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&q=80"
                                    class="w-full h-full object-cover opacity-80" alt="Hero">
                            </div>
                            <div class="md:col-span-4 flex flex-col gap-6">
                                <div
                                    class="flex-1 bg-blue-600 text-white rounded-[50px] p-12 text-left flex flex-col justify-end">
                                    <h3 class="text-3xl font-bold mb-4">Đồng bộ Cloud</h3>
                                    <p class="text-blue-100 font-light">Truy cập mọi lúc mọi nơi.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>

                <script>lucide.createIcons();</script>
            </body>

            </html>