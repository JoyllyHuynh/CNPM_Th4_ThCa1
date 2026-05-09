<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LensVault | Chi tiết tài sản</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/lucide@latest"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body class="bg-slate-950 text-white overflow-hidden">

<div class="flex h-screen">
    <!-- Main Image Area -->
    <div class="flex-1 flex flex-col relative">
        <div class="absolute top-8 left-8 z-50">
            <button onclick="history.back()" class="flex items-center gap-3 px-6 py-3 bg-white/10 backdrop-blur-xl border border-white/10 rounded-2xl text-white font-bold hover:bg-white/20 transition-all border-none">
                <i data-lucide="arrow-left" class="w-5 h-5"></i> Quay lại
            </button>
        </div>

        <div class="flex-1 flex items-center justify-center p-12">
            <img id="detail-image" src="" class="max-w-full max-h-full object-contain shadow-[0_50px_100px_-20px_rgba(0,0,0,1)] rounded-xl" alt="Loading...">
        </div>

        <div class="h-32 bg-slate-900/60 backdrop-blur-3xl px-12 border-t border-white/5 flex items-center justify-between">
            <div class="flex items-center gap-8">
                <div id="detail-thumb" class="w-20 h-20 rounded-2xl border border-white/10 bg-slate-800"></div>
                <div class="text-left">
                    <h3 id="detail-title" class="text-3xl font-bold mb-1 text-white">Đang tải...</h3>
                    <p id="detail-meta" class="text-slate-500 text-lg font-light"></p>
                </div>
            </div>
            <button onclick="alert('Đang tải xuống bản RAW...')" class="px-10 py-5 bg-blue-600 text-white rounded-2xl font-bold flex items-center gap-3 hover:scale-105 transition-all border-none">
                <i data-lucide="download" class="w-6 h-6"></i> Xuất bản RAW
            </button>
        </div>
    </div>

    <!-- Sidebar Info Area -->
    <div class="w-[450px] bg-white text-slate-900 p-16 overflow-y-auto flex flex-col shadow-2xl">
        <h2 id="detail-info-title" class="text-4xl font-bold mb-8 tracking-tight text-left">Chi tiết</h2>
        <div class="space-y-12">
            <div class="flex items-center gap-8">
                <div class="w-16 h-16 rounded-[24px] bg-slate-50 flex items-center justify-center text-blue-600"><i data-lucide="calendar" class="w-7 h-7"></i></div>
                <div class="text-left">
                    <p class="text-[10px] font-bold text-slate-400 uppercase tracking-[0.2em] mb-1">Ngày ghi hình</p>
                    <p id="detail-date" class="font-bold text-xl"></p>
                </div>
            </div>
            <div class="flex items-center gap-8">
                <div class="w-16 h-16 rounded-[24px] bg-slate-50 flex items-center justify-center text-blue-600"><i data-lucide="map-pin" class="w-7 h-7"></i></div>
                <div class="text-left">
                    <p class="text-[10px] font-bold text-slate-400 uppercase tracking-[0.2em] mb-1">Địa điểm</p>
                    <p id="detail-location" class="font-bold text-xl"></p>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="assets/js/app.js"></script>
<script>
    window.onload = () => {
        const urlParams = new URLSearchParams(window.location.search);
        const photoId = urlParams.get('id') || 1;
        loadDetail(photoId);
    };
</script>
</body>
</html>