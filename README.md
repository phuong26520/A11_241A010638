Lab A11 - Dialog, Toast và Menu - Thực đơn có Menu & Dialog

Thông tin sinh viên: Nguyễn Hoài Phương - MSSV 241A010638 - Lớp LTTTBDD - Môn INT4211.

Mô tả ngắn gọn

App Thực đơn quán ăn được nâng cấp từ Lab A9: có Options menu trên thanh tiêu đề (Thêm món, Sắp xếp theo tên, Sắp xếp theo giá, Lọc theo mức giá, Xóa tất cả), Context menu khi giữ lâu một dòng (Sửa món, Xóa), hộp thoại xác nhận trước khi xóa, hộp thoại tự thiết kế bằng setView để thêm/sửa món không tự đóng khi dữ liệu sai, cùng Snackbar có nút HOÀN TÁC.

Cấu trúc chính

app/src/main/java/vn/edu/vhu/ltdd/a11dialog/: gồm MainActivity.java, MonAn.java và MonAnAdapter.java.
app/src/main/res/menu/: gồm menu_main.xml (options menu) và menu_context.xml (context menu).
app/src/main/res/layout/: gồm activity_main.xml, item_mon_an.xml và dialog_mon_an.xml.
app/src/main/res/drawable/: ảnh món ăn mon1.jpg đến mon5.jpg.

Các chức năng đã làm

- Tạo Options menu từ menu_main.xml và xử lý lựa chọn trong onOptionsItemSelected bằng if-else.
- Đăng ký Context menu cho ListView và lấy đúng vị trí dòng qua AdapterContextMenuInfo.position.
- Hiển thị hộp thoại chi tiết món ăn khi chạm vào một dòng.
- Hỏi xác nhận bằng hộp thoại có nêu rõ tên món trước khi xóa một món hoặc xóa tất cả.
- Hộp thoại Thêm/Sửa tự thiết kế dùng setView và ghi đè listener của BUTTON_POSITIVE sau dialog.show() để không đóng khi nhập sai.
- Xóa kèm Snackbar có nút HOÀN TÁC khôi phục món về đúng vị trí cũ bằng danhSach.add(viTri, daXoa).

Bài nâng cao đã làm

NC2 - Dialog chọn mức giá bằng setSingleChoiceItems

Bài yêu cầu tạo hộp thoại lọc thực đơn theo mức giá bằng danh sách có nút chọn tròn (radio). Mình thêm mục Lọc theo mức giá vào menu_main.xml và viết hàm hienDialogChonMucGia() trong MainActivity. Hộp thoại truyền mảng 3 lựa chọn ("Tất cả mức giá", "Dưới 40.000 đ", "Từ 40.000 đ trở lên") và chỉ số đang chọn mucGiaChon vào setSingleChoiceItems.

Khi người dùng bấm nút chọn tròn, lựa chọn tạm thời được lưu vào biến tamChon[0]; chỉ khi bấm nút Lưu (PositiveButton) thì chương trình mới gán vào mucGiaChon và gọi apDungLocGia() để lọc từ danhSachGoc sang danhSach rồi cập nhật adapter.

NC3 - Dùng MaterialAlertDialogBuilder thay cho AlertDialog.Builder

Bài yêu cầu đổi sang MaterialAlertDialogBuilder của thư viện Material 3 và nêu khác biệt. Toàn bộ các hộp thoại trong MainActivity (xem chi tiết, xác nhận xóa, lọc mức giá, thêm/sửa món) được khởi tạo bằng MaterialAlertDialogBuilder(this) thay cho AlertDialog.Builder(this).

Cách gọi các hàm setTitle, setMessage, setView, setSingleChoiceItems và setPositiveButton giữ nguyên hoàn toàn, nhưng hộp thoại hiển thị theo chuẩn Material 3: bốn góc bo tròn lớn hơn, màu nền và màu chữ của nút tự động ăn theo bảng màu Material 3 của theme ứng dụng.

Cách chạy và kiểm thử

Mở project A11_241A010638 trong Android Studio rồi bấm Run trên máy ảo. Kiểm tra nút + và menu ba chấm trên thanh tiêu đề, giữ lâu một dòng để mở context menu Sửa/Xóa, thử để trống tên khi Lưu và thử bấm HOÀN TÁC trên Snackbar.
