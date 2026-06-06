
# CHƯƠNG VII. Implement

## 1. Giới thiệu

Phần này mô tả quá trình hiện thực hệ thống Web Lưu Trữ Hình Ảnh dựa trên các phân tích và thiết kế đã trình bày ở các chương trước. Hệ thống được xây dựng theo kiến trúc phân tầng (Layered Architecture) kết hợp mô hình MVC nhằm đảm bảo tính rõ ràng, dễ bảo trì và dễ mở rộng.

Toàn bộ hệ thống đã được triển khai và chạy thử nghiệm thành công trên môi trường cục bộ (Localhost).

---

## 2. Công nghệ sử dụng

Hệ thống được phát triển với các công nghệ sau:

- **Ngôn ngữ lập trình:** Java
- **Backend:** Jakarta Servlet
- **Frontend:** JSP, HTML5, CSS3, JavaScript
- **Cơ sở dữ liệu:** MySQL
- **Kết nối cơ sở dữ liệu:** JDBC
- **Web Server:** Apache Tomcat 10
- **Quản lý cấu hình:** file `db.properties`
- **IDE:** IntelliJ IDEA

---

## 3. Kiến trúc triển khai hệ thống

Hệ thống được tổ chức theo mô hình 4 tầng:

1. Presentation Layer (JSP)
2. Controller Layer (Servlet)
3. Service Layer (Business Logic)
4. Data Access Layer (DAO)

**Luồng xử lý tổng quát:**

```
Người dùng
→ Gửi yêu cầu từ giao diện JSP
→ Controller nhận và xử lý request
→ Service thực hiện logic nghiệp vụ
→ DAO thao tác với cơ sở dữ liệu
→ Trả kết quả về giao diện
```

---

## 4. Cấu trúc thư mục triển khai

### 4.1. Cấu trúc mã nguồn chính

```
src/main/java/
├── controller/
│   ├── Auth/
│   │   ├── LoginServlet.java
│   │   └── RegisterServlet.java
│   ├── Image/
│   │   ├── SortImageServlet.java
│   │   ├── SearchImageServlet.java
│   │   ├── DownloadServlet.java
│   │   └── RenameImageServlet.java
│   └── service/
│       ├── AuthService.java
│       └── ImageService.java
├── dao/
│   ├── UserDao.java
│   └── ImageeDao.java
└── model/
    ├── User.java
    └── Image.java
```

### 4.2. Tài nguyên và giao diện

```
src/main/webapp/
├── image.jsp
├── detail.jsp
├── login.jsp
├── register.jsp
└── assets/
    ├── css/
    └── js/
```

---

## 5. Hiện thực tầng Model

Tầng Model ánh xạ trực tiếp với các bảng trong cơ sở dữ liệu:

- Bảng `users` → Lớp `User`
- Bảng `albums` → Lớp `Album`
- Bảng `images` → Lớp `Image`
- Bảng `album_images` → Lớp quan hệ `AlbumImage`
- Bảng `logs` → Lớp `Log`
- Bảng `reports` → Lớp `Report`

---

## 6. Hiện thực tầng DAO (Data Access Layer)

Tầng DAO chịu trách nhiệm kết nối cơ sở dữ liệu, thực hiện các truy vấn SQL và trả dữ liệu về tầng Service. DAO sử dụng `PreparedStatement` nhằm ngăn chặn SQL Injection và tăng độ an toàn cho hệ thống.

---

## 7. Hiện thực tầng Service (Business Logic Layer)

Tầng Service đóng vai trò xử lý toàn bộ nghiệp vụ của hệ thống trước khi thao tác cơ sở dữ liệu, bao gồm: kiểm tra tính hợp lệ dữ liệu đầu vào, xử lý điều kiện nghiệp vụ, phối hợp nhiều DAO khi cần thiết và đảm bảo tính toàn vẹn dữ liệu.

---

## 8. Hiện thực các Use Case chính (Controller Layer)

### 8.1. UC-01 – Sắp xếp ảnh (`SortImageServlet.java`)

`SortImageServlet` ánh xạ tới endpoint `/Photos` (HTTP GET). Servlet kiểm tra session, đọc tham số `sortBy` từ URL, gọi `ImageService.getImagesSorted()` và forward dữ liệu sang `image.jsp`.

**Điểm nổi bật:** Cơ chế **Fallback** — nếu người dùng truy cập `/Photos` mà không truyền tham số `sortBy`, hệ thống tự động gán tiêu chí mặc định là `"newest"` để giao diện không bao giờ bị lỗi hiển thị.

```java
// SortImageServlet.java — UC-01: Sắp xếp ảnh

// 1.1.2. Tiếp nhận request và kiểm tra Session
HttpSession session = request.getSession(false);
User user = (session != null) ? (User) session.getAttribute("user") : null;

// 1.1.4. Kiểm tra đối tượng User — Exception 1.3: Chưa đăng nhập
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}
int userId = user.getId();

// 1.1.5. Thu thập tham số sortBy từ URL
String sortBy = request.getParameter("sortBy");

// 1.1.6 → 1.1.9. Gọi Service xử lý sắp xếp và trả về List<Image>
List<Image> images = imageService.getImagesSorted(userId, sortBy);

// 1.1.10. Đính kèm danh sách ảnh vào Request
request.setAttribute("images", images);

// 1.1.11. Alternative Flow 1.2: Fallback — tự động gán "newest" nếu sortBy == null
request.setAttribute("currentSort", sortBy != null ? sortBy : "newest");

// 1.1.12. Thiết lập trạng thái menu
request.setAttribute("activeTopNav", "photos");

// 1.1.13. Forward sang trang hiển thị
request.getRequestDispatcher("/image.jsp").forward(request, response);
```

---

### 8.2. UC-03 – Tìm kiếm ảnh (`SearchImageServlet.java`)

`SearchImageServlet` ánh xạ tới endpoint `/search` (HTTP GET). Servlet xác thực session, đọc tham số `keyword`, gọi `ImageService.searchByKW()` và forward kết quả sang `image.jsp`.

**Điểm nổi bật:** Hệ thống xử lý hai luồng rõ ràng — nếu `keyword` hợp lệ thì truy vấn DB; nếu rỗng thì trả về `List.of()` (danh sách trống) mà không cần gọi DB. Ngoài ra, tính năng **Gợi ý tìm kiếm thời gian thực** (AJAX Debounce 300ms) được thực hiện qua `SearchSuggestionsServlet` phối hợp với `header.jsp`.

```java
// SearchImageServlet.java — UC-03: Tìm kiếm ảnh

// 3.1.2 → 3.1.3. Lấy Session và User
HttpSession session = request.getSession();
User user = (User) session.getAttribute("user");

// 3.1.4. Kiểm tra User — Exception 3.4: Session hết hạn
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}
Integer userId = user.getId();

// 3.1.5. Đọc tham số keyword từ Request
String keyword = request.getParameter("keyword");

List<Image> images;

// 3.1.6. Kiểm tra tính hợp lệ của keyword
if (keyword != null && !keyword.trim().isEmpty()) {
    // 3.1.7 → 3.1.10. Gọi Service tìm kiếm, lấy danh sách ảnh
    images = imageService.searchByKW(userId, keyword.trim());
    // 3.1.11. Gắn keyword vào Request để hiển thị lại ô tìm kiếm
    request.setAttribute("searchKeyword", keyword.trim());
} else {
    // Alternative Flow 3.2: keyword rỗng → trả danh sách trống
    images = List.of();
}

// 3.1.12 → 3.1.14. Thiết lập dữ liệu và trạng thái hiển thị
request.setAttribute("images", images);
request.setAttribute("isSearchResult", true);
request.setAttribute("activeTopNav", "photos");

// 3.1.15. Forward sang image.jsp
request.getRequestDispatcher("image.jsp").forward(request, response);
```

---

### 8.3. UC-12 – Tải ảnh (`DownloadServlet.java`)

`DownloadServlet` ánh xạ tới endpoint `/DownloadServlet` (HTTP GET). Đây là servlet phức tạp nhất, thực hiện chuỗi: xác thực session → kiểm tra quyền sở hữu (chống IDOR) → kiểm tra file vật lý → xử lý đồ họa Java 2D (resize + đổi định dạng) → stream file về trình duyệt.

**Điểm nổi bật:** Cơ chế **Authorization** — so sánh `img.getUserId()` với `loggedInUser.getId()` trước khi cho phép tải, ngăn chặn hoàn toàn việc lách quyền tải ảnh (IDOR). Thuật toán **Bicubic** được dùng để đảm bảo chất lượng ảnh khi resize.

```java
// DownloadServlet.java — UC-12: Tải ảnh

// 12.1.2. Đọc tham số id, quality, format
String idStr = request.getParameter("id");
String quality = request.getParameter("quality");
String reqFormat = request.getParameter("format");

// 12.1.3. Kiểm tra Session — Exception 12.3: Chưa đăng nhập
HttpSession session = request.getSession(false);
User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;
if (loggedInUser == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}

try {
    int id = Integer.parseInt(idStr); // Exception 12.4: NumberFormatException

    // 12.1.5. Gọi Service lấy thông tin ảnh
    Image img = imageService.getImageById(id);
    if (img == null) return; // Exception 12.5: Không tìm thấy ảnh

    // 12.1.7. Kiểm tra quyền sở hữu — Exception 12.6: IDOR
    if (img.getUserId() != loggedInUser.getId()) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
            "Bạn không có quyền tải bức ảnh này.");
        return;
    }

    // 12.1.8 → 12.1.10. Kiểm tra file vật lý
    File file = new File(getServletContext().getRealPath("/uploads"), img.getFilePath());
    if (!file.exists()) {
        response.sendError(404, "File không tồn tại trên server!"); // Alternative 12.2
        return;
    }

    // 12.1.11 → 12.1.14. Cấu hình HTTP Header
    String encodedFileName = URLEncoder.encode(img.getFileName(),
        StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition",
        "attachment; filename=\"" + encodedFileName + "\"");

    // 12.1.15. Nếu không đổi quality/format → stream file gốc
    if ((quality == null || quality.equals("original")) && !isFormatChanged) {
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
        }
    } else {
        // 12.1.16 → 12.1.18. Xử lý đồ họa Java 2D (Bicubic Resize + Format)
        BufferedImage original = ImageIO.read(file);
        // ... tính toán kích thước, vẽ lại bằng Graphics2D Bicubic,
        // ghi ra ByteArrayOutputStream và stream về Response
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }
} catch (NumberFormatException e) {
    e.printStackTrace(); // Exception 12.4: ID không hợp lệ
}
```

---

### 8.4. UC-13 – Chỉnh sửa ảnh (`RenameImageServlet.java`)

`RenameImageServlet` ánh xạ tới endpoint `/RenameImage` (HTTP POST). Servlet sử dụng cơ chế AJAX bất đồng bộ từ phía frontend, trả về chuỗi tên mới dạng `text/plain` thay vì forward/redirect, giúp giao diện cập nhật tên ảnh mà không cần reload trang.

**Điểm nổi bật:** Cơ chế **Extension Auto-Append** — hệ thống tự động kiểm tra và nối đuôi `.png` vào tên file nếu chưa có, đảm bảo tính nhất quán dù người dùng nhập bất kỳ chuỗi nào.

```java
// RenameImageServlet.java — UC-13: Chỉnh sửa ảnh

// 13.1.3. Thiết lập UTF-8 cho cả input và output
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

// 13.1.4. Thu thập tham số id và newName
String idStr = request.getParameter("id");
String newName = request.getParameter("newName");

// 13.1.5. Kiểm tra điều kiện rỗng — Exception 13.2: Bad Request
if (idStr == null || newName == null || newName.trim().isEmpty()) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return;
}

// 13.1.6. Trim khoảng trắng
newName = newName.trim();

// 13.1.7 → 13.1.8. Extension Auto-Append: tự động nối .png nếu chưa có
if (!newName.toLowerCase().endsWith(".png")) {
    newName += ".png";
}

// 13.1.9. Parse id sang int — Exception 13.3: NumberFormatException
int id;
try {
    id = Integer.parseInt(idStr);
} catch (NumberFormatException e) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return;
}

// 13.1.10 → 13.1.12. Gọi Service cập nhật tên vào DB
boolean isSuccess = imageService.renameImage(id, newName);

if (isSuccess) {
    // 13.1.13 → 13.1.14. Phản hồi tên mới dạng text/plain + HTTP 200
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(newName);
    response.setStatus(HttpServletResponse.SC_OK);
    // 13.1.15. Frontend JS nhận chuỗi và cập nhật tên ảnh realtime
} else {
    // Exception 13.4: DB Error — HTTP 500
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}
```

**Phía Frontend (`detail.jsp`)** — hàm `saveImageName()` gửi AJAX POST và cập nhật giao diện không cần reload:

```javascript
// detail.jsp — saveImageName() — UC-13: Frontend AJAX

// 13.1.1. Người dùng nhấn nút "Đổi tên" và nhập tên mới
// 13.1.2. Gửi HTTP POST bất đồng bộ đến /RenameImage
fetch('${pageContext.request.contextPath}/RenameImage', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: formData.toString()
})
.then(res => {
    if (res.ok) {
        // 13.1.15. Nhận tên đã chuẩn hóa, cập nhật UI không cần reload
        return res.text().then(updatedName => {
            document.getElementById('currentFileName').innerText = updatedName;
            document.getElementById('headerFileName').innerText = updatedName;
            document.getElementById('pageTitle').innerText = 'LensVault - ' + updatedName;
            toggleEditName();
        });
    } else if (res.status === 400) {
        alert('Dữ liệu không hợp lệ!'); // Exception 13.2 / 13.3
    } else {
        alert('Lỗi máy chủ!');           // Exception 13.4
    }
});
```

---

### 8.5. UC-06 – Đăng ký tài khoản (`RegisterServlet.java`)

`RegisterServlet` ánh xạ tới endpoint `/register` (HTTP POST). Servlet thực hiện kiểm tra đầu vào nghiêm ngặt bằng **Regex** trước khi gọi `AuthService`, đảm bảo dữ liệu sạch trước khi chạm đến tầng nghiệp vụ.

**Điểm nổi bật:** Hệ thống áp dụng 3 tầng kiểm tra dữ liệu: (1) Kiểm tra rỗng/null, (2) Kiểm tra định dạng Regex (email, họ tên, mật khẩu phức tạp), (3) Kiểm tra trùng email qua DB — tạo thành một lớp bảo vệ đa tầng vững chắc.

```java
// RegisterServlet.java — UC-06: Đăng ký tài khoản

// 6.1.3. Thu thập tham số từ Request
String email    = request.getParameter("email");
String password = request.getParameter("password");
String fullName = request.getParameter("fullName");

// 6.1.4. Trim email và fullName
if (email != null)    email    = email.trim();
if (fullName != null) fullName = fullName.trim();

// 6.1.5. Kiểm tra điều kiện rỗng — Exception 6.2
if (email == null || password == null || fullName == null ||
        email.isEmpty() || password.isBlank() || fullName.isEmpty()) {
    request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
    request.getRequestDispatcher("/register.jsp").forward(request, response);
    return;
}

// 6.1.6. Kiểm tra Regex — Exception 6.3
String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
String nameRegex  = "^[a-zA-ZÀ-ỹ\\s]+$";
String passRegex  = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

if (!email.matches(emailRegex)) {
    request.setAttribute("error", "Định dạng email không hợp lệ!");
    request.getRequestDispatcher("/register.jsp").forward(request, response);
    return;
}
if (!fullName.matches(nameRegex)) {
    request.setAttribute("error", "Họ tên không được chứa ký tự đặc biệt hoặc chữ số!");
    request.getRequestDispatcher("/register.jsp").forward(request, response);
    return;
}
if (!password.matches(passRegex)) {
    request.setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt!");
    request.getRequestDispatcher("/register.jsp").forward(request, response);
    return;
}

// 6.1.7 → 6.1.9. Gọi AuthService đăng ký (kiểm tra trùng email + hash mật khẩu + INSERT)
boolean success = userService.register(email, password, fullName);

if (success) {
    // 6.1.11 → 6.1.12. Forward sang login.jsp kèm thông báo thành công
    request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
    request.getRequestDispatcher("/login.jsp").forward(request, response);
} else {
    // Alternative Flow 6.4: Email đã tồn tại
    request.setAttribute("error", "Email đã tồn tại!");
    request.getRequestDispatcher("/register.jsp").forward(request, response);
}
```

---

## 9. Triển khai và vận hành

Hệ thống được triển khai trên:

- **Apache Tomcat 10**
- **MySQL** chạy trên môi trường Localhost

Người dùng truy cập hệ thống thông qua trình duyệt với địa chỉ:

```
http://localhost:8080/webluutruhinhanh
```

Sau khi triển khai, các chức năng chính của hệ thống đã được kiểm tra và hoạt động đúng theo yêu cầu đã đặc tả.
