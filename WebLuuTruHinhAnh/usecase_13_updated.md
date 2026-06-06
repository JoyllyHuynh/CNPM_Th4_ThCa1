# USE CASE SPECIFICATION: Chỉnh sửa ảnh (Cập nhật)

## 1. Introduction
Use case này mô tả quy trình người dùng cập nhật thông tin tên hiển thị của một tệp tin hình ảnh đã lưu trữ trong hệ thống. Quy trình này xử lý dữ liệu đầu vào qua phương thức HTTP POST, thực hiện chuẩn hóa phần mở rộng của tệp tin theo quy định, và cập nhật bất đồng bộ (AJAX) xuống cơ sở dữ liệu.

## 2. Use Case Description
Tên Use Case: Chỉnh sửa ảnh (Đổi tên)
Số hiệu Use Case: 13
Mô tả: Cho phép người dùng nhập tên mới cho một bức ảnh thông qua giao diện. Hệ thống tiếp nhận mã ảnh và chuỗi tên mới, tự động chuẩn hóa định dạng đuôi file (.png), thực thi cập nhật vào cơ sở dữ liệu, và trả lại phản hồi thành công kèm theo chuỗi tên mới để giao diện cập nhật theo thời gian thực mà không cần tải lại toàn bộ trang.
Tác nhân chính: Người dùng (User)
Tác nhân phụ: Hệ thống (Database)

## 3. Pre-Conditions
Người dùng phải sở hữu hình ảnh cần chỉnh sửa hoặc có quyền can thiệp hợp lệ vào tài nguyên này.
Hình ảnh cần chỉnh sửa phải tồn tại sẵn trong cơ sở dữ liệu.

## 4. Trigger
Người dùng nhấn vào nút "Đổi tên" hoặc "Chỉnh sửa" tại một bức ảnh, nhập tên mới vào ô nhập liệu và nhấn "Xác nhận" (hoặc "Lưu").

## 5. Post-Conditions
Tên của hình ảnh được thay đổi thành công trong cơ sở dữ liệu (đã bao gồm phần mở rộng mặc định .png).
Giao diện người dùng hiển thị tên mới của bức ảnh ngay lập tức mà không cần tải lại trang.

## 6. Normal Flow <Chỉnh sửa ảnh> : UseCase 13
13.1.1. Người dùng nhấn vào nút "Đổi tên" tại một bức ảnh, nhập tên mới vào ô nhập liệu và nhấn "Xác nhận".
13.1.2. Hệ thống (RenameImageServlet) tiếp nhận yêu cầu gửi lên thông qua phương thức HTTP POST đến đường dẫn /RenameImage.
13.1.3. Hệ thống thiết lập bảng mã ký tự đầu vào và đầu ra là UTF-8 nhằm hỗ trợ các ký tự đa ngôn ngữ và tiếng Việt có dấu.
13.1.4. Hệ thống thu thập hai tham số từ Request Parameter bao gồm: Mã định danh ảnh ("id") và chuỗi tên mới cần thay đổi ("newName").
13.1.5. Hệ thống kiểm tra điều kiện rỗng: Xác định các tham số dữ liệu thu được phải khác null và chuỗi tên mới sau khi cắt bỏ khoảng trắng đầu cuối không được trống.
13.1.6. Hệ thống tiến hành cắt bỏ hoàn toàn các ký tự khoảng trắng thừa ở đầu và cuối chuỗi nhập liệu.
13.1.7. Hệ thống kiểm tra chuỗi tên mới xem đã kết thúc bằng phần mở rộng tệp tin .png hay chưa (không phân biệt chữ hoa hay chữ thường).
13.1.8. Do chuỗi tên người dùng nhập chưa kết thúc bằng đuôi mở rộng, hệ thống tự động nối thêm chuỗi định dạng .png vào sau tên tệp tin.
13.1.9. Hệ thống thực hiện chuyển đổi tham số chuỗi mã định danh ảnh sang kiểu số nguyên.
13.1.10. Lớp điều khiển gọi hàm nghiệp vụ xử lý đổi tên thuộc tầng dịch vụ ImageService.
13.1.11. ImageService kết nối cơ sở dữ liệu, thực thi lệnh UPDATE cập nhật trường tên hiển thị của bản ghi ảnh có ID tương ứng thành giá trị newName mới.
13.1.12. Cơ sở dữ liệu ghi nhận thay đổi thành công và trả về giá trị trạng thái isSuccess = true.
13.1.13. Hệ thống thiết lập kiểu dữ liệu trả về cho client là dạng văn bản thuần túy (text/plain).
13.1.14. Hệ thống ghi chuỗi tên mới đã được chuẩn hóa (newName) vào luồng xuất dữ liệu và thiết lập trạng thái phản hồi thành công chuẩn HTTP 200 OK.
13.1.15. Phía Frontend nhận được chuỗi phản hồi, dùng Javascript cập nhật thẻ text hiển thị tên ảnh trên màn hình mà không cần reload trang. Kết thúc Use Case.

## 7. Alternate Flows
(Không có luồng rẽ nhánh nghiệp vụ thành công nào khác trong đoạn mã nguồn hiện tại)

## 8. Exceptions
13.2. Exception: Dữ liệu đầu vào bị rỗng hoặc không hợp lệ
13.2.1. Tại bước 13.1.5, nếu tham số "id" hoặc "newName" bị thiếu (null), hoặc người dùng chỉ nhập toàn dấu cách vào ô tên mới (newName.trim().isEmpty()).
13.2.2. Hệ thống ngừng xử lý nghiệp vụ ngay lập tức.
13.2.3. Hệ thống thiết lập mã trạng thái lỗi yêu cầu không hợp lệ HTTP 400 Bad Request và kết thúc xử lý. Kết thúc luồng.

13.3. Exception: Sai định dạng mã hình ảnh (NumberFormatException)
13.3.1. Tại bước 13.1.9, nếu tham số "id" truyền lên chứa ký tự chữ hoặc không thể chuyển đổi thành số nguyên hợp lệ, tiến trình Integer.parseInt(idStr) ném ra ngoại lệ NumberFormatException.
13.3.2. Hệ thống bắt lấy ngoại lệ tại khối catch, dừng toàn bộ tiến trình xử lý.
13.3.3. Hệ thống thiết lập mã trạng thái lỗi HTTP 400 Bad Request và kết thúc xử lý. Kết thúc luồng.

13.4. Exception: Lỗi cập nhật dữ liệu từ Server (Database Error)
13.4.1. Tại bước 13.1.12, nếu quá trình gọi hàm imageService.renameImage(id, newName) trả về kết quả thất bại (isSuccess = false) do lỗi kết nối cơ sở dữ liệu, xung đột bản ghi hoặc mất tín hiệu mạng server.
13.4.2. Hệ thống bỏ qua bước ghi dữ liệu tên mới ra response.
13.4.3. Hệ thống thiết lập mã trạng thái HTTP 500 Internal Server Error để thông báo lỗi cho Client. Kết thúc luồng.

## 9. Includes
(Không có) - Mã nguồn hiện tại xử lý trực tiếp request tham số độc lập qua API, các điều kiện như kiểm tra phiên đăng nhập hiện tại không nằm trong phạm vi đoạn code logic này.

## 10. Special Requirements
Hệ thống phải phản hồi kết quả trạng thái đổi tên bằng cơ chế Asynchronous (AJAX) để duy trì trải nghiệm mượt mà, không gián đoạn giao diện.
Việc tự động bổ sung đuôi hệ thống .png phải đảm bảo hoạt động nhất quán, chính xác kể cả khi người dùng cố tình nhập các chuỗi đuôi tệp khác (ví dụ: nhập abc.jpg thì hệ thống xử lý thành abc.jpg.png).

## 11. Assumptions
Phía giao diện Client có mã kịch bản (Javascript) để bắt các mã trạng thái HTTP trả về (200, 400, 500) nhằm hiển thị thông báo tương ứng cho người dùng.

## 12. Associated Features or Functional Requirements
RF13.1: Cho phép người dùng chỉnh sửa thông tin tên tệp tin hình ảnh một cách nhanh chóng.
RF13.2: Tự động chuẩn hóa định dạng mở rộng hình ảnh an toàn theo quy chuẩn hệ thống lưu trữ.

---

## 13. Sequence Diagram (Biểu đồ tuần tự)

```mermaid
sequenceDiagram
    autonumber
    actor User as Người dùng
    participant JSP as Browser (Frontend JS)
    participant Servlet as RenameImageServlet
    participant Service as ImageService
    participant DB as Database

    User->>JSP: [13.1.1] Nhập tên mới & nhấn "Xác nhận"
    JSP->>Servlet: [13.1.2] HTTP POST /RenameImage (id, newName)

    Servlet->>Servlet: [13.1.3] Thiết lập UTF-8 encoding (request & response)
    Servlet->>Servlet: [13.1.4] Thu thập tham số id, newName từ Request Parameter
    Servlet->>Servlet: [13.1.5] Kiểm tra điều kiện rỗng (null / isBlank)

    alt Tham số bị thiếu hoặc rỗng (Exception 13.2)
        Servlet->>Servlet: [13.2.2] Ngừng xử lý nghiệp vụ
        Servlet-->>JSP: [13.2.3] HTTP 400 Bad Request
    else Dữ liệu đầu vào hợp lệ
        Servlet->>Servlet: [13.1.6] newName.trim()
        Servlet->>Servlet: [13.1.7] Kiểm tra endsWith(".png")
        Servlet->>Servlet: [13.1.8] Tự động nối thêm ".png" nếu chưa có

        Servlet->>Servlet: [13.1.9] Integer.parseInt(idStr)

        alt Lỗi định dạng số (Exception 13.3)
            Servlet->>Servlet: [13.3.2] Bắt NumberFormatException
            Servlet-->>JSP: [13.3.3] HTTP 400 Bad Request
        else Chuyển đổi thành công
            Servlet->>Service: [13.1.10] renameImage(id, newName)
            Service->>DB: [13.1.11] UPDATE tên hiển thị WHERE id = ?
            DB-->>Service: Kết quả cập nhật

            alt Lỗi cập nhật Database (Exception 13.4)
                Service-->>Servlet: [13.4.1] Trả về isSuccess = false
                Servlet->>Servlet: [13.4.2] Bỏ qua ghi dữ liệu ra response
                Servlet-->>JSP: [13.4.3] HTTP 500 Internal Server Error
            else Cập nhật thành công
                Service-->>Servlet: [13.1.12] Trả về isSuccess = true
                Servlet->>Servlet: [13.1.13] setContentType("text/plain; charset=UTF-8")
                Servlet->>Servlet: [13.1.14] setStatus(SC_OK)
                Servlet-->>JSP: [13.1.14] Phản hồi chuỗi newName
                JSP-->>User: [13.1.15] Javascript cập nhật tên hiển thị ảnh realtime
            end
        end
    end
```
