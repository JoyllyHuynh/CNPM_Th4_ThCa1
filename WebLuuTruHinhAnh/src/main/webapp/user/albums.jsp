<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en" class="light">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>LensVault - Album Management</title>

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet" />

    <!-- Main stylesheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/albums.css" />
</head>
<body>
<%--&lt;%&ndash; ==================== SIDEBAR ==================== &ndash;%&gt;--%>
<%--<c:import url="/user/menu.jsp"></c:import>--%>

<%-- ==================== MAIN WRAPPER ==================== --%>
<div class="main-wrapper">

    <%-- ==================== HEADER ==================== --%>
<%--        <c:import url="/user/header.jsp"></c:import>--%>

    <%-- ==================== MAIN CONTENT ==================== --%>
    <main class="main-canvas" id="mainContent">

        <%-- Page header --%>
        <div class="page-header">
            <div>
                <h1 class="page-title">My Albums</h1>
                <p class="page-subtitle">Organize and manage your curated collections.</p>
            </div>

            <%-- Sort dropdown (simple JS toggle; có thể thay bằng form GET) --%>
            <div style="display:flex; align-items:center; gap:12px;">
                <button class="btn-sort" id="sortBtn" onclick="toggleSortMenu()" aria-haspopup="true" aria-expanded="false">
                    <span class="material-symbols-outlined" style="font-size:18px" aria-hidden="true">filter_list</span>
                    Sort by: <span id="sortLabel">${not empty sortLabel ? sortLabel : 'Date'}</span>
                </button>

                <%-- Sort dropdown panel --%>
                <div id="sortMenu" role="menu" aria-label="Sort options"
                     style="display:none; position:absolute; margin-top:8px; background:var(--color-surface);
                                border:1px solid var(--color-outline-variant); border-radius:var(--radius-lg);
                                box-shadow:0 8px 24px rgba(0,0,0,.1); min-width:160px; z-index:200; right:var(--grid-margin);">
                    <c:forEach var="option" items="${['Date','Name','Size','Item Count']}">
                        <button role="menuitem"
                                style="width:100%; text-align:left; padding:10px 16px;
                                           font-size:var(--font-size-label-sm); color:var(--color-on-surface);
                                           background:none; border:none; cursor:pointer;
                                           font-family:var(--font-family); transition:background .1s;"
                                onmouseover="this.style.background='var(--color-surface-container-low)'"
                                onmouseout="this.style.background='none'"
                                onclick="applySort('${option}')">
                                ${option}
                        </button>
                    </c:forEach>
                </div>
            </div>
        </div>

        <%-- ==================== ALBUMS GRID ==================== --%>
        <div class="album-grid" id="albumGrid">

            <%-- Create new album card --%>
            <div class="album-card-create"
                 role="button"
                 tabindex="0"
                 aria-label="Create new album"
                 onclick="openCreateAlbumModal()"
                 onkeydown="if(event.key==='Enter'||event.key===' ')openCreateAlbumModal()">
                <div class="create-icon-wrap" aria-hidden="true">
                    <span class="material-symbols-outlined" style="font-size:32px">add</span>
                </div>
                <h3 class="create-title">Create New Album</h3>
                <p class="create-desc">Group photos together to tell a story.</p>
            </div>

            <%-- Dynamic album cards from backend --%>
            <c:choose>
                <c:when test="${not empty albums}">
                    <c:forEach var="album" items="${albums}" varStatus="st">
                        <article class="album-card"
                                 data-album-id="${album.id}"
                                 aria-label="Album: ${album.name}">

                                <%-- Thumbnail --%>
                            <div class="album-thumb">
                                <img src="${not empty album.coverUrl ? album.coverUrl : pageContext.request.contextPath.concat('/assets/img/placeholder.jpg')}"
                                     alt="${album.name} cover photo"
                                     loading="lazy" />
                                <div class="album-thumb-overlay" aria-hidden="true"></div>

                                    <%-- Action buttons (edit / delete) --%>
                                <div class="album-actions" aria-label="Album actions">
                                    <button class="album-action-btn edit"
                                            title="Edit album"
                                            aria-label="Edit ${album.name}"
                                            onclick="editAlbum(${album.id}, event)">
                                        <span class="material-symbols-outlined" style="font-size:18px" aria-hidden="true">edit</span>
                                    </button>
                                    <button class="album-action-btn delete"
                                            title="Delete album"
                                            aria-label="Delete ${album.name}"
                                            onclick="deleteAlbum(${album.id}, '${album.name}', event)">
                                        <span class="material-symbols-outlined" style="font-size:18px" aria-hidden="true">delete</span>
                                    </button>
                                </div>
                            </div>

                                <%-- Card body --%>
                            <div class="album-body">
                                <div>
                                    <h3 class="album-name" title="${album.name}">${album.name}</h3>
                                    <p class="album-date">
                                        <span class="material-symbols-outlined" aria-hidden="true">calendar_today</span>
                                            ${album.dateRange}
                                    </p>
                                </div>

                                <div class="album-footer">
                                    <span class="album-count">${album.itemCount} items</span>

                                        <%-- Collaborator avatars --%>
                                    <c:if test="${not empty album.collaborators}">
                                        <div class="collaborators" aria-label="Collaborators">
                                            <c:forEach var="collab" items="${album.collaborators}" end="2">
                                                <img src="${collab.avatarUrl}"
                                                     alt="${collab.name}"
                                                     title="${collab.name}" />
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <%-- Empty state --%>
                    <div style="grid-column: 1/-1; text-align:center; padding: 64px 0; color: var(--color-secondary);">
                        <span class="material-symbols-outlined" style="font-size:64px; display:block; margin-bottom:16px; opacity:.4">photo_library</span>
                        <p style="font-size: var(--font-size-body-lg);">No albums yet. Create your first one!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </main>
</div>

<%-- ==================== CREATE ALBUM MODAL ==================== --%>
<div id="createAlbumModal" role="dialog" aria-modal="true" aria-labelledby="modalTitle"
     style="display:none; position:fixed; inset:0; z-index:300;
                background:rgba(0,0,0,.45); backdrop-filter:blur(4px);
                align-items:center; justify-content:center;">
    <div style="background:var(--color-surface); border-radius:var(--radius-xl);
                    padding:32px; width:90%; max-width:420px;
                    box-shadow:0 24px 64px rgba(0,0,0,.2);
                    animation: slideUp .2s ease;">
        <h2 id="modalTitle" style="font-size:var(--font-size-h3); font-weight:600; margin-bottom:20px;">Create New Album</h2>

        <form id="createAlbumForm" method="POST" action="${pageContext.request.contextPath}/albums/create" onsubmit="return submitCreateAlbum(event)">
            <div style="margin-bottom:16px;">
                <label for="albumNameInput" style="display:block; font-size:var(--font-size-label-sm); font-weight:500; margin-bottom:6px; color:var(--color-on-surface-variant);">
                    Album name <span aria-hidden="true" style="color:var(--color-error)">*</span>
                </label>
                <input id="albumNameInput" name="albumName" type="text" required
                       placeholder="e.g. Summer 2024"
                       maxlength="100"
                       style="width:100%; padding:10px 14px; border:1px solid var(--color-outline-variant);
                                  border-radius:var(--radius-lg); font-size:var(--font-size-body-md);
                                  font-family:var(--font-family); color:var(--color-on-surface);
                                  background:var(--color-surface-container-low); outline:none;
                                  transition: border-color .2s;"
                       onfocus="this.style.borderColor='var(--color-primary)'"
                       onblur="this.style.borderColor='var(--color-outline-variant)'" />
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /><%-- Spring Security CSRF (optional) --%>

            <div style="display:flex; gap:12px; justify-content:flex-end; margin-top:24px;">
                <button type="button" onclick="closeCreateAlbumModal()"
                        style="padding:10px 20px; border:1px solid var(--color-outline-variant);
                                   border-radius:var(--radius-lg); font-size:var(--font-size-label-sm);
                                   font-weight:500; color:var(--color-secondary); background:none; cursor:pointer;
                                   font-family:var(--font-family); transition:background .15s;"
                        onmouseover="this.style.background='var(--color-surface-container-low)'"
                        onmouseout="this.style.background='none'">
                    Cancel
                </button>
                <button type="submit"
                        style="padding:10px 20px; background:var(--color-primary); color:var(--color-on-primary);
                                   border-radius:var(--radius-lg); font-size:var(--font-size-label-sm);
                                   font-weight:500; cursor:pointer; font-family:var(--font-family);
                                   border:none; transition:opacity .15s;"
                        onmouseover="this.style.opacity='.9'" onmouseout="this.style.opacity='1'">
                    Create Album
                </button>
            </div>
        </form>
    </div>
</div>

<%-- ==================== JAVASCRIPT ==================== --%>
<script>
    /* -------- Mobile sidebar toggle -------- */
    const sidebar        = document.getElementById('sidebar');
    const overlay        = document.getElementById('sidebarOverlay');
    const menuToggleBtn  = document.getElementById('menuToggleBtn');

    function openSidebar() {
        sidebar.classList.add('open');
        overlay.classList.add('open');
        menuToggleBtn.setAttribute('aria-expanded', 'true');
        document.body.style.overflow = 'hidden';
    }

    function closeSidebar() {
        sidebar.classList.remove('open');
        overlay.classList.remove('open');
        menuToggleBtn.setAttribute('aria-expanded', 'false');
        document.body.style.overflow = '';
    }

    menuToggleBtn.addEventListener('click', () =>
        sidebar.classList.contains('open') ? closeSidebar() : openSidebar()
    );
    overlay.addEventListener('click', closeSidebar);

    /* -------- Sort dropdown -------- */
    const sortMenu = document.getElementById('sortMenu');

    function toggleSortMenu() {
        const btn = document.getElementById('sortBtn');
        const isOpen = sortMenu.style.display === 'block';
        sortMenu.style.display = isOpen ? 'none' : 'block';
        btn.setAttribute('aria-expanded', String(!isOpen));
    }

    function applySort(label) {
        document.getElementById('sortLabel').textContent = label;
        sortMenu.style.display = 'none';
        document.getElementById('sortBtn').setAttribute('aria-expanded', 'false');
        // Redirect để server xử lý sort
        const url = new URL(window.location.href);
        url.searchParams.set('sort', label.toLowerCase().replace(' ', '_'));
        window.location.href = url.toString();
    }

    // Đóng sort menu khi click ngoài
    document.addEventListener('click', (e) => {
        if (!e.target.closest('#sortBtn') && !e.target.closest('#sortMenu')) {
            sortMenu.style.display = 'none';
        }
    });

    /* -------- Create album modal -------- */
    const createModal = document.getElementById('createAlbumModal');

    function openCreateAlbumModal() {
        createModal.style.display = 'flex';
        setTimeout(() => document.getElementById('albumNameInput').focus(), 50);
    }

    function closeCreateAlbumModal() {
        createModal.style.display = 'none';
        document.getElementById('createAlbumForm').reset();
    }

    // Đóng modal khi click backdrop
    createModal.addEventListener('click', (e) => {
        if (e.target === createModal) closeCreateAlbumModal();
    });

    // Đóng modal bằng Escape
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') closeCreateAlbumModal();
    });

    async function submitCreateAlbum(e) {
        e.preventDefault();
        const name = document.getElementById('albumNameInput').value.trim();
        if (!name) return false;

        try {
            const res = await fetch('${pageContext.request.contextPath}/albums/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ albumName: name })
            });
            if (res.ok) {
                closeCreateAlbumModal();
                window.location.reload(); // reload để hiển thị album mới
            } else {
                alert('Could not create album. Please try again.');
            }
        } catch (err) {
            console.error(err);
            alert('Network error. Please try again.');
        }
        return false;
    }

    /* -------- Edit album -------- */
    function editAlbum(albumId, event) {
        event.stopPropagation();
        window.location.href = '${pageContext.request.contextPath}/albums/' + albumId + '/edit';
    }

    /* -------- Delete album -------- */
    async function deleteAlbum(albumId, albumName, event) {
        event.stopPropagation();
        if (!confirm('Delete "' + albumName + '"? This cannot be undone.')) return;

        try {
            const res = await fetch('${pageContext.request.contextPath}/albums/' + albumId, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' }
            });
            if (res.ok) {
                const card = document.querySelector('[data-album-id="' + albumId + '"]');
                if (card) {
                    card.style.transition = 'opacity .3s, transform .3s';
                    card.style.opacity = '0';
                    card.style.transform = 'scale(.95)';
                    setTimeout(() => card.remove(), 300);
                }
            } else {
                alert('Could not delete album. Please try again.');
            }
        } catch (err) {
            console.error(err);
            alert('Network error.');
        }
    }

    /* -------- Upload handler -------- */
    function handleUpload(input) {
        if (!input.files.length) return;
        const formData = new FormData();
        Array.from(input.files).forEach(f => formData.append('files', f));
        fetch('${pageContext.request.contextPath}/photos/upload', {
            method: 'POST',
            body: formData
        }).then(r => r.ok ? window.location.reload() : alert('Upload failed.'))
            .catch(() => alert('Network error.'))
            .finally(() => { input.value = ''; });
    }

    /* -------- Slide-up animation for modal -------- */
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideUp {
            from { opacity: 0; transform: translateY(20px); }
            to   { opacity: 1; transform: translateY(0); }
        }`;
    document.head.appendChild(style);
</script>

</body>
</html>
