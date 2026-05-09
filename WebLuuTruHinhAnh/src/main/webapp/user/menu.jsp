<%@ taglib prefix="c" uri="jakarta.tags.core" %>

    <c:set var="storageUsed" value="${empty storageUsedGB ? 42.5 : storageUsedGB}" />
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

            <a href="${pageContext.request.contextPath}/Photos"
                class="sidebar-link ${activeTopNav == 'photos' ? 'active' : ''}" role="menuitem">
                <span class="material-symbols-outlined" aria-hidden="true">photo_library</span>
                Photos
            </a>

            <a href="${pageContext.request.contextPath}/album"
                class="sidebar-link ${activeTopNav == 'albums' ? 'active' : ''}" role="menuitem">
                <span class="material-symbols-outlined" style="${activeMenu == 'albums' ? 'font-variation-settings: \"
                    FILL\" 1' : '' }" aria-hidden="true">auto_stories</span>
                Albums
            </a>


        </div>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="sidebar-link logout-link" role="menuitem">

                <span class="material-symbols-outlined" aria-hidden="true">
                    logout
                </span>
                Logout
            </a>
        </div>


    </nav>