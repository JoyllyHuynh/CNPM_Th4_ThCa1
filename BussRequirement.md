BUSINESS REQUIREMENTS DOCUMENT (BRD)
Project: Game Caro (Gomoku)
1. Introduction
1.1 Purpose

Tài liệu này mô tả các yêu cầu nghiệp vụ của hệ thống Game Caro, bao gồm mục tiêu kinh doanh, các chức năng hệ thống và các yêu cầu chất lượng nhằm đảm bảo hệ thống đáp ứng nhu cầu người dùng.

1.2 Scope

Game Caro là một ứng dụng giải trí cho phép người dùng:

Chơi offline (2 người hoặc với AI)
Chơi online với người khác
Lưu trữ điểm số và lịch sử trận đấu
Xem bảng xếp hạng

Hệ thống có thể triển khai dưới dạng Web hoặc Mobile.

2. Business Requirements
2.1 Business Objectives

BR-BO-01: Cung cấp một trò chơi Caro dễ sử dụng và mang tính giải trí cao.
BR-BO-02: Tăng sự tương tác giữa người dùng thông qua chế độ chơi online.
BR-BO-03: Xây dựng nền tảng có khả năng mở rộng trong tương lai (giải đấu, sự kiện).
BR-BO-04: Thu hút tối thiểu 500 người dùng trong 3 tháng đầu triển khai.

2.2 Stakeholders
Người chơi
Admin hệ thống
Nhóm phát triển
Chủ dự án
2.3 Business Constraints

BC-01: Dự án hoàn thành trong thời gian học kỳ.
BC-02: Sử dụng công nghệ phù hợp với năng lực nhóm.
BC-03: Hạn chế về ngân sách và tài nguyên server.

3. Functional Requirements
3.1 Quản lý tài khoản

FR-01: Hệ thống phải cho phép người dùng đăng ký tài khoản.
FR-02: Hệ thống phải cho phép đăng nhập và đăng xuất.
FR-03: Hệ thống phải lưu trữ thông tin hồ sơ và điểm số.

3.2 Chế độ chơi

FR-04: Hệ thống phải hỗ trợ chế độ chơi offline (2 người).
FR-05: Hệ thống phải hỗ trợ chơi với AI (ít nhất 3 mức độ).
FR-06: Hệ thống phải hỗ trợ chế độ chơi online.
FR-07: Hệ thống phải cho phép tạo và tham gia phòng chơi.
FR-08: Hệ thống phải đồng bộ lượt đi giữa hai người chơi online.

3.3 Luật chơi

FR-09: Hệ thống phải hiển thị bàn cờ 15x15.
FR-10: Không cho phép đặt quân vào ô đã có quân.
FR-11: Hệ thống phải tự động xác định thắng/thua/hòa (5 quân liên tiếp).
FR-12: Hệ thống phải hiển thị thông báo kết quả trận đấu.

3.4 Bảng xếp hạng

FR-13: Hệ thống phải tính điểm khi thắng trận online.
FR-14: Hệ thống phải hiển thị bảng xếp hạng người chơi.
FR-15: Hệ thống phải lưu lịch sử trận đấu.

4. Non-Functional Requirements
4.1 Performance

NFR-01: Thời gian phản hồi khi đặt quân ≤ 2 giây.
NFR-02: Hệ thống phải hỗ trợ tối thiểu 1000 người chơi đồng thời.
NFR-03: AI phải xử lý nước đi trong ≤ 3 giây.

4.2 Reliability

NFR-04: Hệ thống phải hoạt động ổn định 99% thời gian.
NFR-05: Khi mất kết nối, hệ thống phải thông báo và xử lý an toàn.

4.3 Security

NFR-06: Mật khẩu người dùng phải được mã hóa.
NFR-07: Hệ thống phải xác thực người dùng trước khi chơi online.
NFR-08: Điểm số không được chỉnh sửa từ phía client.

4.4 Usability

NFR-09: Giao diện phải đơn giản và dễ sử dụng.
NFR-10: Người dùng mới có thể hiểu cách chơi trong vòng 2 phút.
NFR-11: Hệ thống phải hiển thị rõ lượt chơi hiện tại.

5. Success Criteria

SC-01: Người dùng có thể hoàn thành trận đấu mà không gặp lỗi nghiêm trọng.
SC-02: Hệ thống đáp ứng các yêu cầu hiệu năng và bảo mật đã đề ra.
SC-03: Các chức năng chính hoạt động đúng như mô tả.
