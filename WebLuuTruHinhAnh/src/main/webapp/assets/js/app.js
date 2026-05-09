/**
 * LensVault JS - Cấu trúc sẵn sàng cho Java Servlet
 * Mỗi trang giờ đây hoạt động độc lập, không dùng view-switcher
 */

const config = {
    API_PHOTOS: '/api/photos',
    API_ALBUMS: '/api/albums',
    CONTEXT_PATH: ''
};

/**
 * Fetch dữ liệu tùy theo yêu cầu của từng trang
 */
async function fetchData(type) {
    try {
        const response = await fetch(config.CONTEXT_PATH + (type === 'photos' ? config.API_PHOTOS : config.API_ALBUMS));
        const data = await response.json();

        if (type === 'photos') renderPhotos(data);
        else renderAlbums(data);
    } catch (err) {
        console.warn('Lỗi API, đang dùng dữ liệu Mock cho ' + type);
        loadMockData(type);
    }
}

function loadMockData(type) {
    const mockPhotos = [
        { id: 1, url: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&q=80', title: 'Hồ trung tâm Alpha', date: '24/10/2023', location: 'Canada', category: 'Thiên nhiên' },
        { id: 2, url: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&q=80', title: 'Rừng thông mờ ảo', date: '22/10/2023', location: 'Portland', category: 'Thiên nhiên' },
        { id: 3, url: 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&q=80', title: 'Tia nắng rạng đông', date: '20/10/2023', location: 'Washington', category: 'Thiên nhiên' },
        { id: 4, url: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&q=80', title: 'Mặt hồ phẳng lặng', date: '18/10/2023', location: 'Italy', category: 'Du lịch' }
    ];

    if (type === 'photos') renderPhotos(mockPhotos);
}

function renderPhotos(photos) {
    const grid = document.getElementById('photo-grid');
    if (!grid) return;

    grid.innerHTML = photos.map(photo => `
        <div onclick="location.href='detail.html?id=${photo.id}'" class="photo-card group">
            <div class="aspect-[4/5] overflow-hidden relative">
                <img src="${photo.url}" class="w-full h-full object-cover transition-transform duration-[1.5s] group-hover:scale-110" alt="${photo.title}">
                <div class="absolute inset-0 bg-blue-600/10 opacity-0 group-hover:opacity-100 transition-opacity flex items-end p-8">
                    <p class="text-white text-[10px] font-bold uppercase tracking-widest translate-y-4 group-hover:translate-y-0 transition-transform bg-blue-600 px-4 py-2 rounded-full">Chi tiết</p>
                </div>
            </div>
            <div class="p-8 text-left">
                <span class="inline-block px-3 py-1 bg-blue-50 text-blue-600 rounded-lg text-[10px] font-bold uppercase tracking-widest mb-3">${photo.category}</span>
                <h4 class="font-bold text-xl mb-1 group-hover:text-blue-600 transition-colors">${photo.title}</h4>
                <p class="text-slate-400 text-[11px] font-bold uppercase tracking-[0.1em]">${photo.date}</p>
            </div>
        </div>
    `).join('');
    lucide.createIcons();
}

/**
 * Chi tiết ảnh - Khi Servlet sẽ trả về 1 object ảnh cụ thể
 */
async function loadDetail(id) {
    // Trong Servlet bạn có thể gọi API: config.API_PHOTO + "?id=" + id
    // Ở đây ta mock tạm:
    const mockPhotos = [
        { id: 1, url: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&q=80', title: 'Hồ trung tâm Alpha', date: '24/10/2023', location: 'Canada', category: 'Thiên nhiên' },
        { id: 2, url: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&q=80', title: 'Rừng thông mờ ảo', date: '22/10/2023', location: 'Portland', category: 'Thiên nhiên' }
    ];

    const photo = mockPhotos.find(p => p.id == id) || mockPhotos[0];

    document.getElementById('detail-image').src = photo.url;
    document.getElementById('detail-title').innerText = photo.title;
    document.getElementById('detail-info-title').innerText = photo.title;
    document.getElementById('detail-meta').innerText = `${photo.location} • ID #${photo.id}`;
    document.getElementById('detail-date').innerText = photo.date;
    document.getElementById('detail-location').innerText = photo.location;
    document.getElementById('detail-thumb').innerHTML = `<img src="${photo.url}" class="w-full h-full object-cover rounded-2xl">`;

    lucide.createIcons();
}

function notify(msg) {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = 'glass px-6 py-4 rounded-2xl shadow-xl flex items-center gap-4 animate-in fade-in slide-in-from-right-10 duration-300';
    toast.innerHTML = `<div class="w-3 h-3 rounded-full bg-blue-600"></div><span class="text-sm font-bold">${msg}</span>`;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}