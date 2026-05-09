<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${not empty album ? album.albumName : 'Album Detail'} - LensVault</title>

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap"
          rel="stylesheet"/>

    <!-- CSS: dùng chung -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/header.css"/>
    <!-- CSS: riêng trang này -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/album-detail.css"/>
</head>
<body>

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

                    <%--                    <c:if test="${not empty album.description}">--%>
                    <%--                        <p class="album-description">${album.description}</p>--%>
                    <%--                    </c:if>--%>

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
                    <%--                    <button class="btn-edit-album"--%>
                    <%--                            onclick="location.href='${pageContext.request.contextPath}/albums/${album.id}/edit'"--%>
                    <%--                            aria-label="Edit album">--%>
                    <%--                        <span class="material-symbols-outlined" aria-hidden="true">edit</span>--%>
                    <%--                        Edit Album--%>
                    <%--                    </button>--%>

                    <button type="button"
                            class="btn-add-photos"
                            onclick="openAddPhotoModal()"
                            aria-label="Add photos to album">
                        <span class="material-symbols-outlined" aria-hidden="true">add_photo_alternate</span>
                        Add Photos
                    </button>
                    <input type="file" id="addPhotosInput" multiple accept="image/*,video/*"
                           style="display:none"
                           onchange="handleAddPhotos(this)"/>
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

                    <%-- Sort --%>
                    <%--                    <button class="btn-sort-gallery"--%>
                    <%--                            onclick="toggleGallerySort()"--%>
                    <%--                            aria-haspopup="true"--%>
                    <%--                            aria-expanded="false"--%>
                    <%--                            id="gallerySortBtn">--%>
                    <%--                        <span class="material-symbols-outlined" aria-hidden="true">sort</span>--%>
                    <%--                        <span id="gallerySortLabel">Date Added</span>--%>
                    <%--                    </button>--%>

                    <%-- Sort dropdown --%>
                    <%--                    <div id="gallerySortMenu" role="menu"--%>
                    <%--                         style="display:none; position:absolute; margin-top:8px;--%>
                    <%--                                    background:var(--color-surface);--%>
                    <%--                                    border:1px solid var(--color-outline-variant);--%>
                    <%--                                    border-radius:var(--radius-lg);--%>
                    <%--                                    box-shadow:0 8px 24px rgba(0,0,0,.1);--%>
                    <%--                                    min-width:160px; z-index:200;">--%>
                    <%--                        <c:forEach var="opt" items="${['Date Added','Date Taken','Name','Size']}">--%>
                    <%--                            <button role="menuitem"--%>
                    <%--                                    style="width:100%; text-align:left; padding:10px 16px;--%>
                    <%--                                               font-size:var(--font-size-label-sm); font-family:var(--font-family);--%>
                    <%--                                               color:var(--color-on-surface); background:none; border:none;--%>
                    <%--                                               cursor:pointer; transition:background .1s;"--%>
                    <%--                                    onmouseover="this.style.background='var(--color-surface-container-low)'"--%>
                    <%--                                    onmouseout="this.style.background='none'"--%>
                    <%--                                    onclick="applyGallerySort('${opt}')">--%>
                    <%--                                    ${opt}--%>
                    <%--                            </button>--%>
                    <%--                        </c:forEach>--%>
                    <%--                    </div>--%>
                </div>

                <%-- Bulk actions (active khi có ảnh được chọn) --%>
                <div class="bulk-actions" id="bulkActions" aria-label="Bulk actions">
                    <%--                    <button class="bulk-action-btn" title="Share selected" aria-label="Share selected photos">--%>
                    <%--                        <span class="material-symbols-outlined">share</span>--%>
                    <%--                    </button>--%>
                    <%--                    <button class="bulk-action-btn" title="Move selected" aria-label="Move selected photos">--%>
                    <%--                        <span class="material-symbols-outlined">drive_file_move</span>--%>
                    <%--                    </button>--%>
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

                                <img src="${photo.filePath}"
                                     alt="${photo.fileName}"
                                     loading="${st.index < 8 ? 'eager' : 'lazy'}"/>

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
                                        <p class="photo-meta">
                                                ${photo.uploadDate}
                                        </p>
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
                                style="margin: 0 auto;"
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
addPhotoModal
<!-- ================= ADD PHOTO MODAL ================= -->
<div id="addPhotoModal" class="ap-overlay" style="display:none;">
    <div class="ap-dialog">

        <!-- HEADER -->
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

        <!-- SEARCH + SORT -->
        <div class="ap-toolbar">
            <div class="ap-search">
                <span class="material-symbols-outlined">search</span>
                <input type="text"
                       placeholder="Search photos..."
                       oninput="debouncedSearchPhotos(this.value)">
            </div>

            <div class="ap-sort">
                <button class="ap-sort-btn active" onclick="setSort('newest', event)">Newest</button>
                <button class="ap-sort-btn" onclick="setSort('oldest', event)">Oldest</button>
            </div>
        </div>

        <!-- GRID -->
        <div class="ap-grid" id="ap_grid"></div>

        <!-- FOOTER -->
        <div class="ap-footer">
            <button class="ap-btn ap-cancel" onclick="closeAddPhotoModal()">Cancel</button>
            <button class="ap-btn ap-confirm" onclick="confirmAddPhotos()">
                Add Selected
            </button>
        </div>

    </div>
</div>
<div id="deleteAlbumModal" class="lv-modal-overlay">
    <div class="lv-modal">
        <div class="lv-modal-icon danger">
            <span class="material-symbols-outlined">delete</span>
        </div>

        <h2 class="lv-modal-title">Delete album?</h2>
        <p class="lv-modal-text" id="deleteAlbumText">
            Are you sure you want to delete this album?
        </p>

        <div class="lv-modal-actions">
            <button class="btn-cancel" onclick="closeDeleteModal()">Cancel</button>
            <button class="btn-danger" onclick="confirmDeleteAlbum()">Delete</button>
        </div>
    </div>
</div>
<div id="photoViewer" class="viewer-overlay" style="display:none;" onclick="closePhotoViewer()">
    <img id="viewerImg" src="" alt="preview"/>
</div>
<script>
    const contextPath = "${pageContext.request.contextPath}";
    const albumId = "${album.id}";


    let selectedPhotos = [];
    let pendingDelete = false;

    /* mở popup */
    document.querySelector(".btn-add-photos").addEventListener("click", openAddPhotoModal);

    function openAddPhotoModal() {
        document.getElementById("addPhotoModal").style.display = "flex";

        if (!filteredPhotos.length) {
            filteredPhotos = [...allPhotos];
            applySort();
        }

        renderLibrary(filteredPhotos);
    }

    /* render ảnh trong library */
    function renderLibrary(photos) {
        const container = document.getElementById("ap_grid");
        const count = document.getElementById("ap_selectedCount");

        container.innerHTML = "";

        photos.forEach(p => {
            const item = document.createElement("div");
            item.className = "ap-item";

            if (addSelectedPhotos.includes(p.id)) {
                item.classList.add("selected");
            }

            item.innerHTML =
                '<img class="ap-img" src="' + p.url + '" />' +
                '<div class="ap-name">' + p.fileName + '</div>';

            item.onclick = () => {
                const index = addSelectedPhotos.indexOf(p.id);

                if (index > -1) {
                    addSelectedPhotos.splice(index, 1);
                } else {
                    addSelectedPhotos.push(p.id);
                }

                renderLibrary(filteredPhotos);
            };

            container.appendChild(item);
        });

        count.innerText = addSelectedPhotos.length;
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
    function deleteSelected() {
        if (selectedPhotos.length === 0) {
            showToast("Chưa chọn ảnh nào", false);
            return;
        }

        document.getElementById("deleteAlbumText").innerText =
            `Delete ${selectedPhotos.length} photos from this album?`;

        // 🔥 QUAN TRỌNG: phải mở modal
        document.getElementById("deleteAlbumModal").style.display = "flex";

        pendingDelete = true;
    }
    function updateBulkUI() {
        const bulk = document.getElementById("bulkActions");

        if (selectedPhotos.length > 0) {
            bulk.classList.add("active");
        } else {
            bulk.classList.remove("active");
        }

        const allItems = document.querySelectorAll(".photo-item").length;
        const box = document.getElementById("selectAllBox");

        box.classList.toggle(
            "checked",
            selectedPhotos.length === allItems && allItems > 0
        );
    }
    function toggleSelectAll(event) {
        event?.stopPropagation();

        const items = document.querySelectorAll(".photo-item");

        const isAllSelected = selectedPhotos.length === items.length;

        if (isAllSelected) {
            selectedPhotos = [];
            items.forEach(i => {
                const id = parseInt(i.dataset.photoId);
                renderPhotoState(id, false);
            });
        } else {
            selectedPhotos = [];
            items.forEach(i => {
                const id = parseInt(i.dataset.photoId);
                selectedPhotos.push(id);
                renderPhotoState(id, true);
            });
        }

        updateBulkUI();
    }
    function renderPhotoState(id, isSelected) {
        const item = document.getElementById("photo-" + id);
        if (!item) return;

        const box = item.querySelector(".photo-checkbox");

        if (isSelected) {
            box.classList.add("checked");
            box.setAttribute("aria-checked", "true");
        } else {
            box.classList.remove("checked");
            box.setAttribute("aria-checked", "false");
        }
    }
    function confirmDeleteAlbum() {

        if (pendingDelete) {
            // ===== DELETE PHOTOS =====
            fetch("${pageContext.request.contextPath}/RemoveImg", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
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
                        showToast("Đã xóa ảnh", "success");
                        setTimeout(() =>{
                            location.reload();
                        },1500);
                    } else {
                        showToast("Xóa thất bại", "error");
                    }

                    closeDeleteModal();
                    pendingDelete = false;
                })
                .catch(err => console.log(err));

        } else {
            // fallback (nếu sau này bạn reuse modal cho album)
            closeDeleteModal();
        }
    }
    function closeDeleteModal() {
        document.getElementById("deleteAlbumModal").style.display = "none";
        pendingDelete = false;
    }

    function openPhoto(id) {
        const item = document.getElementById("photo-" + id);
        if (!item) return;

        const img = item.querySelector("img");

        const viewer = document.getElementById("photoViewer");
        const viewerImg = document.getElementById("viewerImg");

        viewerImg.src = img.src;
        viewer.style.display = "flex";
    }

    function closePhotoViewer() {
        document.getElementById("photoViewer").style.display = "none";
    }

    function showToast(message, type = "info", duration = 3000) {
        const container = document.getElementById("toastContainer");

        const toast = document.createElement("div");
        toast.classList.add("lv-toast", type);

        let icon = "info";
        if (type === "success") icon = "check_circle";
        else if (type === "error") icon = "error";
        else if (type === "warning") icon = "warning";

        toast.innerHTML = `
    <span class="material-symbols-outlined" style="font-size:18px">
        \${icon}
    </span>
    <span>\${message}</span>
`;

        container.appendChild(toast);

        setTimeout(() => {
            toast.classList.add("lv-toast-hide");
            setTimeout(() => toast.remove(), 300);
        }, duration);
    }



    // ===== ADD PHOTO STATE (tách biệt hoàn toàn delete) =====
    let addSelectedPhotos = [];
    const allPhotos = [
        <c:forEach var="img" items="${imageListOfUser}" varStatus="st">
        {
            id: ${img.id},
            fileName: "${img.fileName}",
            url: "${img.filePath}",
            uploadDate: "${img.uploadDate}"
        }<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];
    let filteredPhotos = [...allPhotos];
    let currentSort = "newest";


    function debouncedSearchPhotos(keyword) {
        keyword = keyword.toLowerCase().trim();

        filteredPhotos = allPhotos.filter(p =>
            p.fileName.toLowerCase().includes(keyword)
        );

        applySort();
        renderLibrary(filteredPhotos);
    }
    function parseDate(str) {
        const d = new Date(str);
        return isNaN(d) ? 0 : d.getTime();
    }

    function applySort() {
        filteredPhotos.sort((a, b) => {
            return currentSort === "newest"
                ? parseDate(b.uploadDate) - parseDate(a.uploadDate)
                : parseDate(a.uploadDate) - parseDate(b.uploadDate);
        });
    }

    function setSort(type, e) {
        currentSort = type;

        document.querySelectorAll(".ap-sort-btn").forEach(btn => {
            btn.classList.remove("active");
        });

        e.currentTarget.classList.add("active");

        applySort();
        renderLibrary(filteredPhotos);
    }
    function closeAddPhotoModal() {
        document.getElementById("addPhotoModal").style.display = "none";
        addSelectedPhotos = [];
    }
    function confirmAddPhotos() {
        if (addSelectedPhotos.length === 0) {
            showToast("Chưa chọn ảnh nào", "warning");
            return;
        }

        fetch("${pageContext.request.contextPath}/add-photos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                albumId: parseInt(albumId),
                photoIds: addSelectedPhotos
            })
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    showToast("Thêm ảnh vào album thành công", "success");
                    closeAddPhotoModal();
                    setTimeout(() => {
                        location.reload();
                    }, 1500);
                } else {
                    closeAddPhotoModal();
                    showToast(data.message || "Thêm thất bại", "error");
                }
            })
            .catch(err => {
                console.error(err);
                closeAddPhotoModal();
                showToast("Lỗi server", "error");
            });
    }

</script>

</body>
</html>
