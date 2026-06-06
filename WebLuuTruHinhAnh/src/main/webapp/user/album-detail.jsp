<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${not empty album ? album.albumName : 'Album Detail'} - LensVault</title>

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap"
          rel="stylesheet"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/header.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/album-detail.css"/>
</head>
<body>

<%-- Vùng chứa thông báo Toast toàn cục --%>
<div id="toastContainer" style="position: fixed; top: 20px; right: 20px; z-index: 9999; display: flex; flex-direction: column; gap: 10px;"></div>

<%-- ==================== SIDEBAR ==================== --%>
<c:set var="activeTopNav" value="${empty activeTopNav ? 'albums' : activeTopNav}" />
<jsp:include page="/user/menu.jsp"/>

<%-- ==================== MAIN WRAPPER ==================== --%>
<div class="main-wrapper">

    <%-- ==================== HEADER ==================== --%>
    <jsp:include page="/user/header.jsp"/>
    <%-- ==================== MAIN CONTENT ==================== --%>
    <main class="main-canvas">
        <div class="main-inner">

            <%-- Breadcrumb / Back --%>
            <a href="${pageContext.request.contextPath}/albums"
               class="back-nav"
               aria-label="Back to Albums">
                <span class="material-symbols-outlined">arrow_back</span>
                Back to Albums
            </a>

            <%-- ===== ALBUM HEADER ===== --%>
            <header class="album-header">
                <div class="album-header-info">
                    <h1 class="album-title">${album.albumName}</h1>
                    <div class="album-meta">
                        <span class="album-meta-item">
                            <span class="material-symbols-outlined" aria-hidden="true">calendar_month</span>
                            ${album.createdAt}
                        </span>
                        <span class="album-meta-sep" aria-hidden="true">•</span>
                        <span class="album-meta-item">
                            <span class="material-symbols-outlined" aria-hidden="true">photo_library</span>
                            ${album.itemCount} Items
                        </span>
                    </div>
                </div>

                <%-- Action buttons --%>
                <div class="album-header-actions">
                    <button type="button"
                            class="btn-add-photos"
                            onclick="openAddPhotoModal()"
                            aria-label="Add photos to album">
                        <span class="material-symbols-outlined" aria-hidden="true">add_photo_alternate</span>
                        Add Photos
                    </button>
                </div>
            </header>

            <%-- ===== GALLERY CONTROLS ===== --%>
            <div class="gallery-controls" id="galleryControls">
                <div class="gallery-controls-left">
                    <%-- Select All --%>
                    <label class="select-all-label"
                           aria-label="Select all photos"
                           onclick="toggleSelectAll(event)">
                        <div class="checkbox-box" id="selectAllBox" role="checkbox" aria-checked="false"></div>
                        <span class="select-all-text">Select All</span>
                    </label>
                    <div class="controls-divider" aria-hidden="true"></div>
                </div>

                <%-- Bulk actions --%>
                <div class="bulk-actions" id="bulkActions" aria-label="Bulk actions">
                    <button class="bulk-action-btn danger"
                            title="Delete selected"
                            aria-label="Delete selected photos"
                            onclick="deleteSelected()">
                        <span class="material-symbols-outlined">delete</span>
                    </button>
                </div>
            </div>

            <%-- ===== MASONRY PHOTO GRID ===== --%>
            <c:choose>
                <c:when test="${not empty imageList}">
                    <div class="masonry-grid" id="photoGrid">
                        <c:forEach var="photo" items="${imageList}" varStatus="st">
                            <article class="photo-item"
                                     id="photo-${photo.id}"
                                     data-photo-id="${photo.id}"
                                     data-filename="${photo.fileName}"
                                     aria-label="${photo.fileName}"
                                     onclick="openPhoto(${photo.id})">

                                <img src="${pageContext.request.contextPath}/uploads/${photo.filePath}"
                                     alt="${photo.fileName}"
                                     loading="${st.index < 8 ? 'eager' : 'lazy'}"
                                     onerror="this.onerror=null; this.style.background='#eee';"/>

                                    <%-- Hover overlay --%>
                                <div class="photo-overlay">
                                    <div class="photo-overlay-top">
                                            <%-- Checkbox --%>
                                        <div class="photo-checkbox"
                                             role="checkbox"
                                             aria-checked="false"
                                             aria-label="Select ${photo.fileName}"
                                             onclick="togglePhotoSelect(event, ${photo.id})">
                                        </div>
                                            <%-- More options --%>
                                        <button class="photo-more-btn"
                                                aria-label="More options for ${photo.fileName}"
                                                onclick="openPhotoMenu(event, ${photo.id})">
                                            <span class="material-symbols-outlined">more_vert</span>
                                        </button>
                                    </div>

                                    <div class="photo-overlay-bottom">
                                        <p class="photo-filename">${photo.fileName}</p>
                                        <p class="photo-meta">${photo.uploadDate}</p>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="gallery-empty">
                        <span class="material-symbols-outlined" aria-hidden="true">add_photo_alternate</span>
                        <p>This album has no photos yet.</p>
                        <button type="button"
                                class="btn-add-photos"
                                style="margin: 20px auto 0 auto; display: flex;"
                                onclick="openAddPhotoModal()"
                                aria-label="Add photos to album">
                            <span class="material-symbols-outlined" aria-hidden="true">add_photo_alternate</span>
                            Add Photos
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </main>
</div>

<%-- Add Photos Modal --%>
<div id="addPhotoModal" class="ap-overlay" style="display:none;">
    <div class="ap-dialog">
        <div class="ap-header">
            <div>
                <h2 class="ap-title">Add Photos</h2>
                <p class="ap-subtitle">
                    <span id="ap_selectedCount">0</span> selected
                </p>
            </div>
            <button class="ap-close" onclick="closeAddPhotoModal()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>

        <div class="ap-toolbar">
            <div class="ap-search">
                <span class="material-symbols-outlined">search</span>
                <input type="text"
                       placeholder="Search photos..."
                       oninput="searchPhotos(this.value)">
            </div>
        </div>

        <div class="ap-grid" id="ap_grid"></div>

        <div class="ap-footer">
            <button class="ap-btn ap-cancel" onclick="closeAddPhotoModal()">Cancel</button>
            <button class="ap-btn ap-confirm" onclick="confirmAddPhotos()">Add Selected</button>
        </div>
    </div>
</div>

<%-- Delete Confirmation Modal --%>
<div id="deleteAlbumModal" class="lv-modal-overlay" style="display:none;">
    <div class="lv-modal">
        <div class="lv-modal-icon danger">
            <span class="material-symbols-outlined">delete</span>
        </div>
        <h2 class="lv-modal-title">Remove Photos?</h2>
        <p class="lv-modal-text" id="deleteAlbumText">
            Are you sure you want to remove selected photos from this album?
        </p>
        <div class="lv-modal-actions">
            <button class="btn-cancel" onclick="closeDeleteModal()">Cancel</button>
            <button class="btn-danger" onclick="confirmDeleteAlbum()">Delete</button>
        </div>
    </div>
</div>

<%-- Photo Viewer Preview Modal --%>
<div id="photoViewer" class="viewer-overlay" style="display:none;" onclick="closePhotoViewer()">
    <img id="viewerImg" src="" alt="preview"/>
</div>

<script>
    const contextPath = "${pageContext.request.contextPath}";
    const albumId = "${album.id}";

    let selectedPhotos = [];
    let addSelectedPhotos = [];
    let pendingDelete = false;

    // ĐỔ DỮ LIỆU TỪ JAVA SANG JAVASCRIPT ĐỂ PHỤC VỤ MODAL
    let allPhotos = [
        <c:forEach var="img" items="${imageListOfUser}" varStatus="status">
        {
            id: ${img.id},
            fileName: "<c:out value='${img.fileName}' />",
            url: "${pageContext.request.contextPath}/uploads/${img.filePath}"
        }${!status.last ? ',' : ''}
        </c:forEach>
    ];
    let filteredPhotos = [...allPhotos];

    function openAddPhotoModal() {
        document.getElementById("addPhotoModal").style.display = "flex";
        filteredPhotos = [...allPhotos];
        renderLibrary(filteredPhotos);
    }

    function closeAddPhotoModal() {
        document.getElementById("addPhotoModal").style.display = "none";
        addSelectedPhotos = [];
        const count = document.getElementById("ap_selectedCount");
        if (count) count.innerText = "0";
    }

    function renderLibrary(photos) {
        const container = document.getElementById("ap_grid");
        const count = document.getElementById("ap_selectedCount");
        if (!container) return;

        container.innerHTML = "";

        if (photos.length === 0) {
            container.innerHTML = `<div style="grid-column: 1/-1; text-align: center; color: #888; padding: 40px 0;">Không tìm thấy ảnh phù hợp.</div>`;
            return;
        }

        photos.forEach(p => {
            const item = document.createElement("div");
            item.className = "ap-item";

            if (addSelectedPhotos.includes(p.id)) {
                item.classList.add("selected");
            }

            item.innerHTML = `
                <img class="ap-img" src="\${p.url}" alt="\${p.fileName}" loading="lazy" />
                <div class="ap-name">\${p.fileName}</div>
            `;

            item.onclick = () => {
                const index = addSelectedPhotos.indexOf(p.id);
                if (index > -1) {
                    addSelectedPhotos.splice(index, 1);
                    item.classList.remove("selected");
                } else {
                    addSelectedPhotos.push(p.id);
                    item.classList.add("selected");
                }
                if (count) count.innerText = addSelectedPhotos.length;
            };
            container.appendChild(item);
        });

        if (count) count.innerText = addSelectedPhotos.length;
    }

    function searchPhotos(keyword) {
        const cleanKeyword = keyword.trim().toLowerCase();
        filteredPhotos = allPhotos.filter(p => p.fileName.toLowerCase().includes(cleanKeyword));
        renderLibrary(filteredPhotos);
    }

    function togglePhotoSelect(event, id) {
        event.stopPropagation();
        const index = selectedPhotos.indexOf(id);
        const isSelected = index === -1;

        if (isSelected) {
            selectedPhotos.push(id);
        } else {
            selectedPhotos.splice(index, 1);
        }

        renderPhotoState(id, isSelected);
        updateBulkUI();
    }

    function updateBulkUI() {
        const bulk = document.getElementById("bulkActions");
        if (!bulk) return;

        if (selectedPhotos.length > 0) {
            bulk.classList.add("active");
        } else {
            bulk.classList.remove("active");
        }

        const allItems = document.querySelectorAll(".photo-item").length;
        const box = document.getElementById("selectAllBox");
        if (box) {
            box.classList.toggle("checked", selectedPhotos.length === allItems && allItems > 0);
        }
    }

    function toggleSelectAll(event) {
        event?.stopPropagation();
        const items = document.querySelectorAll(".photo-item");
        const isAllSelected = selectedPhotos.length === items.length;

        selectedPhotos = [];
        items.forEach(i => {
            const id = parseInt(i.dataset.photoId);
            if (!isAllSelected) {
                selectedPhotos.push(id);
            }
            renderPhotoState(id, !isAllSelected);
        });

        updateBulkUI();
    }

    function renderPhotoState(id, isSelected) {
        const item = document.getElementById("photo-" + id);
        if (!item) return;

        const box = item.querySelector(".photo-checkbox");
        if (!box) return;

        if (isSelected) {
            box.classList.add("checked");
            box.setAttribute("aria-checked", "true");
        } else {
            box.classList.remove("checked");
            box.setAttribute("aria-checked", "false");
        }
    }

    function deleteSelected() {
        if (selectedPhotos.length === 0) {
            showToast("Vui lòng chọn ít nhất một ảnh để xóa.", "warning");
            return;
        }

        document.getElementById("deleteAlbumText").innerText =
            `Bạn có chắc chắn muốn gỡ bỏ \${selectedPhotos.length} ảnh khỏi album này không?`;

        document.getElementById("deleteAlbumModal").style.display = "flex";
        pendingDelete = true;
    }

    function confirmDeleteAlbum() {
        if (!pendingDelete) {
            closeDeleteModal();
            return;
        }

        fetch(`\${contextPath}/RemoveImg`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                albumId: parseInt(albumId),
                photoIds: selectedPhotos
            })
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    selectedPhotos.forEach(id => {
                        const el = document.getElementById("photo-" + id);
                        if (el) el.remove();
                    });

                    selectedPhotos = [];
                    updateBulkUI();
                    showToast("Đã gỡ ảnh khỏi album thành công.", "success");

                    if (document.querySelectorAll(".photo-item").length === 0) {
                        setTimeout(() => location.reload(), 1000);
                    }
                } else {
                    showToast(data.message || "Xóa ảnh thất bại.", "error");
                }
                closeDeleteModal();
            })
            .catch(err => {
                console.error("Lỗi hệ thống khi xóa ảnh:", err);
                showToast("Không thể kết nối đến máy chủ.", "error");
                closeDeleteModal();
            });
    }

    function closeDeleteModal() {
        document.getElementById("deleteAlbumModal").style.display = "none";
        pendingDelete = false;
    }

    function confirmAddPhotos() {
        if (addSelectedPhotos.length === 0) {
            showToast("Chưa có bức ảnh nào được chọn.", "warning");
            return;
        }

        fetch(`\${contextPath}/add-photos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                albumId: parseInt(albumId),
                photoIds: addSelectedPhotos
            })
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    showToast("Thêm ảnh vào album thành công!", "success");
                    closeAddPhotoModal();
                    setTimeout(() => location.reload(), 1000);
                } else {
                    showToast(data.message || "Thêm ảnh thất bại.", "error");
                }
            })
            .catch(err => {
                console.error("Lỗi hệ thống khi thêm ảnh:", err);
                showToast("Lỗi máy chủ, vui lòng thử lại sau.", "error");
            });
    }

    function openPhoto(id) {
        const item = document.getElementById("photo-" + id);
        if (!item) return;

        const img = item.querySelector("img");
        const viewer = document.getElementById("photoViewer");
        const viewerImg = document.getElementById("viewerImg");

        if (viewer && viewerImg && img) {
            viewerImg.src = img.src;
            viewer.style.display = "flex";
        }
    }

    function closePhotoViewer() {
        document.getElementById("photoViewer").style.display = "none";
    }

    function openPhotoMenu(event, id) {
        event.stopPropagation();
        console.log("Mở tùy chọn ảnh ID:", id);
    }

    function showToast(message, type = "info", duration = 3000) {
        const container = document.getElementById("toastContainer");
        if (!container) return;

        const toast = document.createElement("div");
        toast.className = "lv-toast " + type;

        let iconName = "info";
        if (type === "success") iconName = "check_circle";
        else if (type === "error") iconName = "error";
        else if (type === "warning") iconName = "warning";

        toast.innerHTML = `
            <span class="material-symbols-outlined" style="font-size:18px">\${iconName}</span>
            <span>\${message}</span>
        `;

        container.appendChild(toast);

        setTimeout(() => {
            toast.classList.add("lv-toast-hide");
            setTimeout(() => toast.remove(), 300);
        }, duration);
    }

    // Đóng modal khi click ra ngoài vùng overlay
    window.addEventListener("click", function(e) {
        const addModal = document.getElementById("addPhotoModal");
        const delModal = document.getElementById("deleteAlbumModal");
        if (e.target === addModal) closeAddPhotoModal();
        if (e.target === delModal) closeDeleteModal();
    });
</script>
</body>
</html>