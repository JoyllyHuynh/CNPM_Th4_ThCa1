USE CASE SPECIFICATION
USECASE 01 Sắp xếp ảnh 
1. Introduction
Use case này mô tả quy trình người dùng thực hiện sắp xếp danh sách các hình ảnh cá nhân đã lưu trữ trên hệ thống theo các tiêu chí khác nhau (như mới nhất, cũ nhất, tên, dung lượng...). Chức năng này thuộc nhóm nghiệp vụ hiển thị, tương tác và tìm kiếm hình ảnh của người dùng.

2. Use Case Description
Tên Use Case: Sắp xếp ảnh
Số hiệu Use Case: 01
Mô tả: Cho phép người dùng lựa chọn một tiêu chí sắp xếp trên giao diện. Hệ thống tiếp nhận tham số, truy vấn cơ sở dữ liệu để lấy danh sách ảnh của chính người dùng đó, thực hiện sắp xếp theo đúng yêu cầu và hiển thị lại lên màn hình giao diện quản lý ảnh.
Tác nhân chính: Người dùng (User đã đăng nhập)
Tác nhân phụ: Hệ thống (Database)

3. Pre-Conditions
Người dùng phải có tài khoản hợp lệ trên hệ thống.
Người dùng đã đăng nhập thành công và có một HttpSession đang hoạt động chứa đối tượng thông tin người dùng (User).

4. Trigger
Người dùng truy cập vào trang quản lý ảnh (/Photos) hoặc thay đổi lựa chọn tại bộ lọc sắp xếp trên giao diện.

5. Post-Conditions
Danh sách hình ảnh của người dùng được sắp xếp theo đúng tiêu chí yêu cầu.
Giao diện người dùng (image.jsp) được cập nhật để hiển thị danh sách ảnh mới kèm theo trạng thái tiêu chí sắp xếp hiện tại.

6. Normal Flow <Sắp xếp ảnh> : UseCase 01
1.1.1. Lớp điều khiển (PhotosServlet) kiểm tra Session hiện tại.
1.1.2. Hệ thống lấy đối tượng User từ thuộc tính "user" trong Session.
1.1.3. Hệ thống xác nhận đối tượng User tồn tại (khác null) và trích xuất thông tin định danh userId = user.getId().
1.1.4. Hệ thống tiếp nhận tham số yêu cầu từ URL/Request Parameter có tên là "sortBy".
1.1.5. Lớp điều khiển gọi tầng nghiệp vụ.
1.1.6. ImageService xử lý logic kiểm tra tham số sortBy (nếu nhận vào giá trị null hoặc rỗng, hệ thống tự động gán tiêu chí mặc định là "newest" - mới nhất).
1.1.7. ImageService kết nối xuống tầng dữ liệu để thực thi truy vấn Database lấy danh sách ảnh thuộc về userId và sắp xếp theo tiêu chí sortBy.
1.1.8. Hệ thống trả về một danh sách thực thể hình ảnh List<Image> images.
1.1.9. Hệ thống đính kèm danh sách ảnh vào Request.
1.1.10. Hệ thống lưu lại tiêu chí vừa chọn để giữ trạng thái hiển thị trên giao diện.
1.1.11. Hệ thống thiết lập thuộc tính điều hướng thanh menu.
1.1.12. Hệ thống thực hiện chuyển tiếp (Forward) toàn bộ dữ liệu sang trang hiển thị.
1.1.13. Trang image.jsp nhận dữ liệu, render danh sách hình ảnh đã được sắp xếp và hiển thị lên màn hình của người dùng.

7. Alternate Flows
1.2. Alternative Flow: Tham số sortBy bị thiếu hoặc bằng null
1.2.1. Trong trường hợp người dùng vừa đăng nhập hoặc click trực tiếp vào link /Photos mà không truyền tham số sắp xếp.
1.2.2. Hệ thống kiểm tra thấy sortBy == null.
1.2.3. Hệ thống sẽ tự động gán giá trị "newest" (Sắp xếp theo ảnh mới nhất) làm tiêu chí mặc định.
1.2.4. Luồng xử lý tiếp tục quay lại bước 1.1.7.

8. Exceptions
1.3. Exception: Người dùng chưa đăng nhập (Session hết hạn / Không hợp lệ)
1.3.1. Tại bước 1.1.3, hệ thống kiểm tra thấy đối tượng User thu được từ Session bằng null.
1.3.2. Hệ thống hủy bỏ tiến trình xử lý sắp xếp ảnh.
1.3.3. Hệ thống thực hiện chuyển hướng người dùng về trang đăng nhập.
1.3.4. Kết thúc Use Case.

9. Includes
Đăng nhập: Để có session hợp lệ thực hiện chức năng này.

10. Special Requirements
Tốc độ phản hồi và truy vấn sắp xếp danh sách ảnh phải tối ưu dưới 1 giây để đảm bảo trải nghiệm người dùng không bị ngắt quãng.
Giao diện image.jsp phải hỗ trợ hiển thị tốt (Responsive) danh sách ảnh sau khi được sắp xếp trên cả thiết bị di động và máy tính.

11. Assumptions
Cơ sở dữ liệu hoạt động ổn định và trường lưu mốc thời gian/tên của hình ảnh không bị rỗng (null).
Dữ liệu userId lưu trong Session luôn đồng bộ với khóa ngoại trong bảng chứa hình ảnh.

12. Associated Features or Functional Requirements
RF1.1: Cho phép người dùng chuyển đổi linh hoạt giữa các chế độ sắp xếp ảnh.
RF1.2: Tự động ghi nhớ hoặc hiển thị rõ tiêu chí sắp xếp hiện tại trên bộ lọc của giao diện.
