<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeMenu"   value="${empty activeMenu ? 'albums' : activeMenu}" />
<c:set var="storageUsed"  value="${empty storageUsedGB ? 42.5 : storageUsedGB}" />
<c:set var="storageTotal" value="${empty storageTotalGB ? 50 : storageTotalGB}" />
<c:set var="storagePct" value="${(storageUsed * 100) / storageTotal}" />
<c:set var="storagePct" value="${storagePct - (storagePct % 1)}" />

<!-- Mobile overlay (click để đóng sidebar) -->
<div class="sidebar-overlay" id="sidebarOverlay"></div>

<!-- Sidebar -->
<nav class="sidebar" id="sidebar" aria-label="Main navigation">

    <!-- Brand -->
    <div class="sidebar-brand">
        <div class="sidebar-brand-logo" aria-hidden="true">LV</div>
        <div>
            <div class="sidebar-brand-name">LensVault</div>
            <div class="sidebar-brand-sub">Premium Storage</div>
        </div>
    </div>

    <!-- Main nav links -->
    <div class="sidebar-nav" role="menubar">

        <a href="${pageContext.request.contextPath}/photos"
           class="sidebar-link ${activeMenu == 'photos' ? 'active' : ''}"
           role="menuitem">
            <span class="material-symbols-outlined" aria-hidden="true">photo_library</span>
            Photos
        </a>

        <a href="${pageContext.request.contextPath}/albums"
           class="sidebar-link ${activeMenu == 'albums' ? 'active' : ''}"
           role="menuitem">
            <span class="material-symbols-outlined"
                  style="${activeMenu == 'albums' ? \"font-variation-settings: 'FILL' 1\" : ''}"
                  aria-hidden="true">auto_stories</span>
            Albums
        </a>

        <a href="${pageContext.request.contextPath}/shared"
           class="sidebar-link ${activeMenu == 'shared' ? 'active' : ''}"
           role="menuitem">
            <span class="material-symbols-outlined" aria-hidden="true">group</span>
            Shared
        </a>

        <a href="${pageContext.request.contextPath}/archive"
           class="sidebar-link ${activeMenu == 'archive' ? 'active' : ''}"
           role="menuitem">
            <span class="material-symbols-outlined" aria-hidden="true">archive</span>
            Archive
        </a>

        <a href="${pageContext.request.contextPath}/trash"
           class="sidebar-link ${activeMenu == 'trash' ? 'active' : ''}"
           role="menuitem">
            <span class="material-symbols-outlined" aria-hidden="true">delete</span>
            Trash
        </a>
    </div>

    <!-- Storage indicator + upgrade CTA -->
    <div class="sidebar-storage">
        <div class="storage-info">
            <div class="storage-header">
                <span class="storage-label">Storage</span>
                <span class="storage-percent">${storagePct}% full</span>
            </div>
            <div class="storage-bar-track" role="progressbar"
                 aria-valuenow="${storagePct}" aria-valuemin="0" aria-valuemax="100"
                 aria-label="Storage usage">
                <div class="storage-bar-fill" style="width: ${storagePct}%"></div>
            </div>
            <span class="storage-used-text">${storageUsed} GB of ${storageTotal} GB used</span>
        </div>

        <button class="btn-upgrade"
                onclick="location.href='${pageContext.request.contextPath}/upgrade'">
            Upgrade Storage
        </button>
    </div>

    <!-- Footer links -->
    <div class="sidebar-footer" role="menubar">
        <a href="${pageContext.request.contextPath}/help"
           class="sidebar-link" role="menuitem">
            <span class="material-symbols-outlined" aria-hidden="true">help</span>
            Help
        </a>
        <a href="${pageContext.request.contextPath}/privacy"
           class="sidebar-link" role="menuitem">
            <span class="material-symbols-outlined" aria-hidden="true">shield</span>
            Privacy
        </a>
    </div>
</nav>
