
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="activeTopNav" value="${empty activeTopNav ? 'albums' : activeTopNav}" />

<header class="topbar" id="topbar" role="banner">

    <!-- Mobile: hamburger + brand -->
    <div class="topbar-mobile-brand">
        <button class="topbar-menu-btn icon-btn"
                id="menuToggleBtn"
                aria-label="Open navigation menu"
                aria-expanded="false"
                aria-controls="sidebar">
            <span class="material-symbols-outlined" aria-hidden="true">menu</span>
        </button>
        <span class="topbar-mobile-title">LensVault</span>
    </div>

    <!-- Navigation -->
    <nav class="topbar-nav" aria-label="Top navigation">
        <a href="${pageContext.request.contextPath}/explore"
           class="topbar-nav-link ${activeTopNav eq 'home' ? 'active' : ''}">
            Home
        </a>

        <a href="${pageContext.request.contextPath}/album"
           class="topbar-nav-link ${activeTopNav eq 'albums' ? 'active' : ''}">
            Albums
        </a>

    </nav>

    <!-- Actions -->
    <div class="topbar-actions">

        <!-- Search -->
        <form class="search-box" role="search" method="GET" action="${pageContext.request.contextPath}/search">
            <span class="material-symbols-outlined search-icon" aria-hidden="true">search</span>
            <input class="search-input"
                   type="search"
                   name="keyword"
                   placeholder="Search LensVault"
                   aria-label="Search LensVault"
                   id="globalSearch"
                   autocomplete="off" />
        </form>


        <style>
            #settingsBtn { display:none; }
            @media(min-width:640px){
                #settingsBtn{ display:flex; }
            }
        </style>

        <!-- Upload -->
        <button class="btn-upload"
                onclick="document.getElementById('fileUploadInput').click()"
                aria-label="Upload photos">
            <span class="material-symbols-outlined" style="font-size:18px" aria-hidden="true">
                upload
            </span>
            Upload
        </button>

        <input type="file"
               id="fileUploadInput"
               multiple
               accept="image/*,video/*"
               style="display:none"
               onchange="handleUpload(this)" />
    </div>
    <div id="toastContainer" class="lv-toast-container"></div>
</header>