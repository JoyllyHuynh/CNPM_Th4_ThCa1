BUSINESS REQUIREMENTS DOCUMENT (BRD) – MVP
Project: Game Caro (Initial Version)
1. Introduction
1.1 Purpose

Tài liệu này mô tả các yêu cầu nghiệp vụ cho phiên bản đầu tiên (MVP) của Game Caro.
Mục tiêu là xây dựng một phiên bản cơ bản nhưng hoạt động ổn định.

1.2 Scope

Phiên bản đầu chỉ bao gồm:

Chơi offline (2 người trên cùng thiết bị)
Chơi với AI cơ bản
Hiển thị kết quả thắng/thua
Không bao gồm chế độ online và bảng xếp hạng
2. Business Requirements

BR-01: Cung cấp một trò chơi Caro đơn giản, dễ sử dụng.
BR-02: Cho phép người dùng chơi nhanh mà không cần đăng ký tài khoản.
BR-03: Đảm bảo game hoạt động ổn định trên thiết bị mục tiêu.

3. Functional Requirements

FR-01: Hệ thống phải hiển thị bàn cờ kích thước 15x15.
FR-02: Hệ thống phải cho phép hai người chơi thay phiên đặt quân.
FR-03: Hệ thống không cho phép đặt quân vào ô đã có quân.
FR-04: Hệ thống phải tự động kiểm tra điều kiện thắng (5 quân liên tiếp).
FR-05: Hệ thống phải hiển thị thông báo khi có người thắng.
FR-06: Hệ thống phải cho phép chơi lại sau khi kết thúc trận.
FR-07: Hệ thống phải hỗ trợ chế độ chơi với AI (3 chế độ như dễ, trung bình, khó).

4. Non-Functional Requirements
4.1 Performance

NFR-01: Thời gian phản hồi khi đặt quân ≤ 1 giây.
NFR-02: AI phải đưa ra nước đi trong ≤ 2 giây.

4.2 Reliability

NFR-03: Game không được crash trong quá trình chơi.

4.3 Usability

NFR-04: Giao diện phải đơn giản, dễ hiểu.
NFR-05: Người dùng có thể bắt đầu chơi trong vòng 30 giây sau khi mở ứng dụng.

4.4 Compatibility

NFR-06: Ứng dụng phải chạy được trên hệ điều hành Windows 10 trở lên.
