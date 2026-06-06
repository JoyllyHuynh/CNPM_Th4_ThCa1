# USE CASE SPECIFICATION
## USECASE 03: Tìm kiếm ảnh 

### 1. Introduction
Use case này mô tả quy trình người dùng thực hiện tìm kiếm các hình ảnh cá nhân đã lưu trữ trong hệ thống bằng cách nhập các từ khóa liên quan (như tên ảnh, thẻ tag, hoặc mô tả).

### 2. Use Case Description
- **Tên Use Case**: Tìm kiếm ảnh
- **Số hiệu Use Case**: 03
- **Mô tả**: Cho phép người dùng nhập từ khóa tìm kiếm trên giao diện. Hệ thống sẽ tiếp nhận từ khóa, xác thực quyền truy cập của người dùng (thông qua Session), truy vấn cơ sở dữ liệu để lọc ra danh sách các hình ảnh phù hợp và trả về kết quả hiển thị lên màn hình.
- **Tác nhân chính**: Người dùng (User đã đăng nhập)
- **Tác nhân phụ**: Hệ thống (Database)

### 3. Pre-Conditions
- Người dùng đã đăng nhập thành công và có một phiên làm việc (Session) chứa thông tin thực thể `User` hợp lệ.
- Hệ thống đã có kết nối với Cơ sở dữ liệu.

### 4. Trigger
- Người dùng nhập từ khóa vào ô tìm kiếm trên thanh điều hướng hoặc trang quản lý ảnh và nhấn nút "Tìm kiếm" (hoặc nhấn phím Enter), gửi một HTTP Request GET đến endpoint `/search` kèm tham số `keyword`.

### 5. Post-Conditions
- Hệ thống trả về danh sách các hình ảnh có thông tin trùng khớp với từ khóa tìm kiếm thuộc sở hữu của riêng người dùng đó.
- Giao diện người dùng (`image.jsp`) hiển thị danh sách kết quả lọc, cập nhật ô tìm kiếm bằng từ khóa đã nhập và thiết lập trạng thái đây là giao diện kết quả tìm kiếm (`isSearchResult = true`).

---

### 6. Normal Flow <Tìm kiếm ảnh> : UseCase 03

**3.1. Kiểm tra quyền truy cập và thông tin người dùng (Session Validation)**
- **3.1.1.** Lớp điều khiển (`SearchImageServlet`) tiếp nhận request và lấy Session hiện tại của người dùng.
- **3.1.2.** Hệ thống trích xuất đối tượng `User` từ thuộc tính `"user"` trong Session.
- **3.1.3.** Hệ thống lấy thông tin định danh `userId` từ đối tượng `User` và xác nhận mã định danh hợp lệ (đối tượng `User` khác null và `userId` khác null).

**3.2. Tiếp nhận và chuẩn hóa từ khóa tìm kiếm**
- **3.2.1.** Hệ thống trích xuất chuỗi ký tự từ tham số `"keyword"` trong HTTP Request.
- **3.2.2.** Hệ thống kiểm tra tính hợp lệ của từ khóa: Từ khóa khác `null` và không phải là chuỗi rỗng sau khi đã loại bỏ khoảng trắng ở hai đầu (thông qua hàm `trim()`).

**3.3. Xử lý nghiệp vụ truy vấn dữ liệu (Business Logic)**
- **3.3.1.** Lớp điều khiển gọi phương thức `searchByKW` của tầng nghiệp vụ `ImageService`.
- **3.3.2.** Tầng `ImageService` tiếp nhận mã định danh `userId` và từ khóa `keyword` đã được chuẩn hóa (loại bỏ khoảng trắng).
- **3.3.3.** Tầng nghiệp vụ gọi đến tầng truy cập dữ liệu (`ImageDao`) thực hiện câu lệnh truy vấn lọc trong cơ sở dữ liệu để tìm ra các bản ghi ảnh thỏa mãn điều kiện: Thuộc sở hữu của `userId` và chứa thông tin khớp với `keyword`.
- **3.3.4.** Hệ thống gán danh sách kết quả trả về từ DB vào biến tập hợp `List<Image> images`.

**3.4. Thiết lập dữ liệu và trạng thái hiển thị (Data Binding)**
- **3.4.1.** Hệ thống đính kèm lại từ khóa đã chuẩn hóa vào Request (thuộc tính `"searchKeyword"`) để hiển thị tại ô tìm kiếm trên giao diện.
- **3.4.2.** Hệ thống đính kèm danh sách ảnh tìm được (`images`) vào Request (thuộc tính `"images"`).
- **3.4.3.** Hệ thống thiết lập cờ đánh dấu trạng thái hiển thị là trang kết quả tìm kiếm (thuộc tính `"isSearchResult" = true`).
- **3.4.4.** Hệ thống cấu hình thuộc tính menu để làm sáng mục "Photos" trên thanh điều hướng (thuộc tính `"activeTopNav" = "photos"`).

**3.5. Chuyển tiếp giao diện kết quả (Forwarding Response)**
- **3.5.1.** Hệ thống sử dụng `RequestDispatcher` để chuyển tiếp (forward) Request hiện tại kèm theo toàn bộ dữ liệu sang trang giao diện `image.jsp`.
- **3.5.2.** Trang `image.jsp` nhận diện các dữ liệu và cấu hình trạng thái, render danh sách ảnh tìm kiếm được lên màn hình cho người dùng.

---

### 7. Alternate Flows

**7.1. Alternative Flow: 7.1 – Từ khóa tìm kiếm trống hoặc rỗng**
- Nếu người dùng nhấn nút tìm kiếm nhưng không nhập từ khóa, hoặc chỉ nhập các ký tự khoảng trắng (dấu cách).
- Hệ thống phát hiện chuỗi từ khóa rỗng hoặc chỉ chứa khoảng trắng sau khi `trim()`.
- Hệ thống bỏ qua toàn bộ bước truy vấn cơ sở dữ liệu ở tầng `ImageService` (Bỏ qua bước 3.3).
- Hệ thống tự động khởi tạo và gán một danh sách rỗng cho kết quả: `images = List.of()`.
- Hệ thống bỏ qua bước thiết lập thuộc tính `"searchKeyword"` (Bỏ qua bước 3.4.1).
- Luồng xử lý chuyển tiếp trực tiếp đến bước **3.4.2** để nạp danh sách trống lên Request và tiếp tục chu trình trả về giao diện.

**7.2. Alternative Flow: 7.2 – Gợi ý tìm kiếm thời gian thực (Real-time Search Suggestions)**
- **7.2.1.** Khi người dùng nhập ký tự vào ô tìm kiếm (trên giao diện `header.jsp`), sự kiện input được kích hoạt sau khoảng thời gian debounce (300ms) để tránh gửi quá nhiều yêu cầu dồn dập.
- **7.2.2.** Giao diện gửi request AJAX dạng bất đồng bộ (GET) đến API `/search/suggestions?keyword=...`.
- **7.2.3.** Bộ điều khiển `SearchSuggestionsServlet` tiếp nhận request.
- **7.2.4. [Xác thực]** Bộ điều khiển kiểm tra sự tồn tại của session chứa thông tin `user` (Tương tự Exception 8.1). Nếu session bị null hoặc userId bị null, servlet trả về status code `401 Unauthorized` và danh sách gợi ý rỗng `[]`.
- **7.2.5.** Bộ điều khiển trích xuất tham số `keyword`, nếu trống thì trả về danh sách rỗng `[]`.
- **7.2.6.** Bộ điều khiển gọi phương thức `getSearchSuggestions(userId, keyword)` ở tầng `ImageService`.
- **7.2.7.** Tầng `ImageService` tiếp nhận và chuyển tiếp yêu cầu đến tầng `ImageDao` (gọi phương thức `getSearchSuggestions`).
- **7.2.8.** Tầng `ImageDao` thực hiện truy vấn cơ sở dữ liệu để tìm kiếm danh sách các tên hình ảnh (file name) của người dùng đó có chứa chuỗi ký tự khớp với keyword (giới hạn tối đa 7 kết quả, sử dụng `DISTINCT` để loại bỏ trùng lặp).
- **7.2.9.** Danh sách gợi ý được trả về từ DB qua DAO, Service và quay lại Servlet.
- **7.2.10.** Servlet chuyển đổi danh sách các chuỗi gợi ý thành định dạng JSON bằng thư viện `Gson` và ghi trực tiếp vào luồng phản hồi (Response Writer) trả về phía Client.
- **7.2.11.** Client (Javascript) nhận dữ liệu JSON, phân tích và render thành danh sách dropdown hiển thị ngay phía dưới thanh tìm kiếm.
- **7.2.12.** Nếu người dùng click vào một mục gợi ý cụ thể, từ khóa đó sẽ được tự động điền vào ô tìm kiếm và form tìm kiếm sẽ được tự động submit để bắt đầu chạy **Normal Flow (UseCase 03)** bắt đầu từ bước 3.1.

---

### 8. Exceptions
**8.1. Exception: 8.1 – Mã định danh người dùng không hợp lệ (Session không tồn tại/Hết hạn)**
- Tại bước **3.1.3** hoặc **7.2.4**, hệ thống phát hiện đối tượng `User` bị `null` hoặc giá trị `userId` bằng `null` (do session đã bị hủy, hết hạn, hoặc người dùng chưa đăng nhập hợp lệ).
- Hệ thống hủy bỏ toàn bộ tiến trình xử lý tìm kiếm ảnh hoặc gợi ý hiện tại.
- Hệ thống thực hiện chuyển hướng trình duyệt (Redirect) của người dùng quay lại trang đăng nhập (`/login.jsp`).
- Kết thúc Use Case.

---

### 9. Includes
- **Đăng nhập**: Để đảm bảo có đối tượng User và mã định danh userId hợp lệ trong Session trước khi thực hiện các truy vấn dữ liệu cá nhân (được đảm bảo bằng Exception flow).

### 10. Special Requirements
- Hệ thống cần sử dụng cơ chế tìm kiếm không phân biệt chữ hoa chữ thường (Case-insensitive) và hỗ trợ tốt tìm kiếm từ khóa tiếng Việt có dấu.
- Thời gian phản hồi tìm kiếm từ khi nhấn nút hoặc nhập phím gợi ý đến khi có kết quả cần được tối ưu dưới 1.5 giây.

### 11. Assumptions
- Từ khóa tìm kiếm được truyền tải qua phương thức GET của HTTP Request.
- Hệ thống đã có cơ chế phòng chống các lỗ hổng như SQL Injection trong câu lệnh truy vấn của tầng DAO.

---

### Sơ đồ Sequence Tích hợp: Gợi ý và Tìm kiếm ảnh (Unified Flow)

```mermaid
sequenceDiagram
    actor User as User<br/>(Đã đăng nhập)
    participant FE as FE: header.jsp / image.jsp
    participant SugServlet as SearchSuggestionsServlet<br/>(/search/suggestions)
    participant SearchServlet as SearchImageServlet<br/>(/search)
    participant Service as ImageService
    participant DAO as ImageDao (DAO)
    participant DB as Database

    %% --- PHẦN 1: GỢI Ý TÌM KIẾM THỜI GIAN THỰC (AJAX) ---
    rect rgb(240, 248, 255)
    note right of User: LUỒNG GỢI Ý TÌM KIẾM THỜI GIAN THỰC (Alternative Flow 7.2)
    User->>FE: Gõ ký tự vào ô tìm kiếm (7.2.1)
    note over FE: Chờ 300ms debounce
    FE->>SugServlet: Gửi request AJAX (GET /search/suggestions?keyword=...) (7.2.2)
        SugServlet->>SugServlet: 7.2.5 Lấy tham số 'keyword' từ request
    alt user == null hoặc user.getId() == null (Exception 8.1)
        SugServlet-->>FE: Trả về status 401 & danh sách gợi ý rỗng "[]"
    else user hợp lệ
        SugServlet->>Service: 7.2.6 Gọi getSearchSuggestions(userId, keyword)
        Service->>DAO: 7.2.7 Gọi getSearchSuggestions(userId, keyword)
        DAO->>DB: 7.2.8 Truy vấn SELECT DISTINCT file_name (LIMIT 7)
        DB-->>DAO: Trả về danh sách tên ảnh gợi ý (List<String>)
        DAO-->>Service: Trả về List<String>
        Service-->>SugServlet: Trả về List<String>
        SugServlet->>SugServlet: 7.2.10 Chuyển đổi sang JSON bằng Gson
        SugServlet-->>FE: Trả về dữ liệu JSON
    end
    FE->>FE: 7.2.11 Render danh sách gợi ý lên Dropdown dưới ô tìm kiếm
    end

    %% --- PHẦN 2: THỰC HIỆN TÌM KIẾM CHÍNH ---
    rect rgb(255, 250, 240)
    note right of User: LUỒNG TÌM KIẾM ẢNH CHÍNH (Normal Flow 6.0)
    alt Người dùng nhấn Enter / nút tìm kiếm HOẶC click chọn từ gợi ý (7.2.12)
        User->>FE: Kích hoạt tìm kiếm (Chọn gợi ý hoặc tự gõ rồi Enter)
        FE->>SearchServlet: Gửi request GET /search?keyword=... (3.1.1)
        
        rect rgb(255, 248, 220)
        note right of FE: 3.1. Xác thực Session người dùng
        SearchServlet->>SearchServlet: 3.1.1 Lấy Session hiện tại
        SearchServlet->>SearchServlet: 3.1.2 Trích xuất User từ Session
        SearchServlet->>SearchServlet: 3.1.3 Lấy userId = user.getId() & Xác thực hợp lệ
        alt user == null hoặc userId == null (Exception 8.1)
            SearchServlet-->>FE: Redirect /login.jsp
            FE-->>User: (Kết thúc Use Case)
        end
        end

        SearchServlet->>SearchServlet: 3.2.1 Lấy tham số "keyword" từ Request
        SearchServlet->>SearchServlet: 3.2.2 Kiểm tra keyword != null & không rỗng

        alt keyword null hoặc chỉ chứa khoảng trắng (Alternative Flow 7.1)
            SearchServlet->>SearchServlet: images = List.of() (Bỏ qua gọi Service)
        else keyword hợp lệ
            SearchServlet->>Service: 3.3.1 Gọi searchByKW(userId, keyword.trim())
            Service->>Service: 3.3.2 Tiếp nhận userId & keyword (đã trim)
            Service->>DAO: 3.3.3 Gọi imgd.searchByKW(userId, keyword)
            DAO->>DB: Truy vấn DB tìm ảnh thuộc userId & chứa keyword
            DB-->>DAO: Trả về danh sách ảnh phù hợp (List<Image>)
            DAO-->>Service: Trả về List<Image>
            Service-->>SearchServlet: 3.3.4 Nhận List<Image> images
        end

        rect rgb(245, 245, 255)
        note right of FE: 3.4. Thiết lập dữ liệu và trạng thái hiển thị
        opt keyword hợp lệ
            SearchServlet->>SearchServlet: 3.4.1 Đính kèm searchKeyword vào request
        end
        SearchServlet->>SearchServlet: 3.4.2 Đính kèm images vào request
        SearchServlet->>SearchServlet: 3.4.3 Đính kèm isSearchResult = true
        SearchServlet->>SearchServlet: 3.4.4 Đính kèm activeTopNav = "photos"
        end

        SearchServlet->>FE: 3.5.1 Forward tới image.jsp
        FE-->>User: 3.5.2 Hiển thị danh sách ảnh & trạng thái kết quả
    end
    end
```
