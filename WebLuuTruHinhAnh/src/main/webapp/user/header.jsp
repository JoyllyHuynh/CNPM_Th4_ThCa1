
<%@ taglib prefix="c" uri="jakarta.tags.core" %>



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
        <a href="${pageContext.request.contextPath}/Photos"
           class="topbar-nav-link ${activeTopNav eq 'photos' ? 'active' : ''}">
            Photos
        </a>

        <a href="${pageContext.request.contextPath}/album"
           class="topbar-nav-link ${activeTopNav eq 'albums' ? 'active' : ''}">
            Albums
        </a>

        <a href="${pageContext.request.contextPath}/Profile"
           class="topbar-nav-link ${activeTopNav eq 'profile' ? 'active' : ''}">
            Profile
        </a>

    </nav>

    <!-- Actions -->
    <div class="topbar-actions">

        <!-- Search -->
        <!-- 3.1.1. Người dùng nhập từ khóa vào ô tìm kiếm và nhấn nút "Tìm kiếm" (hoặc nhấn Enter). -->
        <form class="search-box" role="search" method="GET" action="${pageContext.request.contextPath}/search" style="position: relative;">
            <span class="material-symbols-outlined search-icon" aria-hidden="true">search</span>
            <input class="search-input"
                   type="search"
                   name="keyword"
                   placeholder="Search LensVault"
                   aria-label="Search LensVault"
                   id="globalSearch"
                   autocomplete="off" />
            <div id="searchSuggestions" class="search-suggestions-dropdown" style="display: none;"></div>
        </form>


        <style>
            #settingsBtn { display:none; }
            @media(min-width:640px){
                #settingsBtn{ display:flex; }
            }
            .search-suggestions-dropdown {
                position: absolute;
                top: 100%;
                left: 0;
                right: 0;
                background-color: var(--color-surface, #ffffff);
                border: 1px solid var(--color-outline-variant, #e0e0e0);
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                margin-top: 8px;
                max-height: 250px;
                overflow-y: auto;
                z-index: 1000;
            }
            .search-suggestion-item {
                padding: 10px 16px;
                font-size: 14px;
                color: var(--color-on-surface, #333333);
                cursor: pointer;
                transition: background-color 0.15s;
                text-align: left;
                display: block;
                width: 100%;
                border: none;
                background: none;
            }
            .search-suggestion-item:hover {
                background-color: var(--color-surface-container-high, #f5f5f5);
            }
        </style>

        <script>
        document.addEventListener("DOMContentLoaded", function() {
            // [Bước 7.2.2] Gửi AJAX request tới /search/suggestions (frontend)
            const searchInput = document.getElementById("globalSearch");
            const suggestionsDropdown = document.getElementById("searchSuggestions");
            let debounceTimer;
            
            if (searchInput && suggestionsDropdown) {
                searchInput.addEventListener("input", function() {
                    clearTimeout(debounceTimer);
                    const query = searchInput.value.trim();

                    if (query.length < 1) {
                        suggestionsDropdown.style.display = "none";
                        suggestionsDropdown.innerHTML = "";
                        return;
                    }

                    // [3.3.1] Sự kiện input được kích hoạt sau debounce 300ms
                    debounceTimer = setTimeout(() => {
                        // [3.3.2] Giao diện gửi AJAX GET đến /search/suggestions?keyword=...
                        fetch("${pageContext.request.contextPath}/search/suggestions?keyword=" + encodeURIComponent(query))
                            .then(response => {
                                if (!response.ok) throw new Error("Network response was not ok");
                                return response.json();
                            })
                            .then(data => {
                                // [3.3.11] Client nhận JSON, render danh sách dropdown
                                suggestionsDropdown.innerHTML = "";
                                if (data.length > 0) {
                                    data.forEach(item => {
                                        const div = document.createElement("div");
                                        div.className = "search-suggestion-item";
                                        div.textContent = item;
                                        // [3.3.12] Khi click gợi ý, điền vào ô và submit form
                                        div.addEventListener("click", function() {
                                            searchInput.value = item;
                                            suggestionsDropdown.style.display = "none";
                                            searchInput.closest("form").submit();
                                        });
                                        suggestionsDropdown.appendChild(div);
                                    });
                                    suggestionsDropdown.style.display = "block";
                                } else {
                                    suggestionsDropdown.style.display = "none";
                                }
                            })
                            .catch(err => console.error("Error fetching search suggestions:", err));
                    }, 300); // Debounce 300ms
                });

                // Hide suggestions when clicking outside
                document.addEventListener("click", function(e) {
                    if (!searchInput.contains(e.target) && !suggestionsDropdown.contains(e.target)) {
                        suggestionsDropdown.style.display = "none";
                    }
                });

                // Show suggestions when clicking back into input if there's text
                searchInput.addEventListener("focus", function() {
                    if (suggestionsDropdown.children.length > 0 && searchInput.value.trim().length > 0) {
                        suggestionsDropdown.style.display = "block";
                    }
                });
            }
        });
        </script>

<%--        <!-- Upload -->--%>
<%--        <button class="btn-upload"--%>
<%--                onclick="document.getElementById('fileUploadInput').click()"--%>
<%--                aria-label="Upload photos">--%>
<%--            <span class="material-symbols-outlined" style="font-size:18px" aria-hidden="true">--%>
<%--                upload--%>
<%--            </span>--%>
<%--            Upload--%>
<%--        </button>--%>

<%--        <input type="file"--%>
<%--               id="fileUploadInput"--%>
<%--               multiple--%>
<%--               accept="image/*,video/*"--%>
<%--               style="display:none"--%>
<%--               onchange="handleUpload(this)" />--%>
    </div>
    <div id="toastContainer" class="lv-toast-container"></div>
</header>