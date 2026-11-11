package vn.edu.vhu.ltdd.a11dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Lab A11 — nâng cấp app A9 bằng Menu, Context menu, Dialog, Toast và Snackbar. */
public class MainActivity extends AppCompatActivity {

    // TODO: thay 2201234567 bằng MSSV của bạn
    private static final String TAG = "A11_241A010638";

    private final List<MonAn> danhSach = new ArrayList<>();
    private final List<MonAn> danhSachGoc = new ArrayList<>();
    private int mucGiaChon = 0;
    private MonAnAdapter adapter;
    private ListView lvMonAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        lvMonAn = findViewById(R.id.lvMonAn);
        TextView tvTrong = findViewById(R.id.tvTrong);

        taoDuLieuMau();
        adapter = new MonAnAdapter(this, danhSach);
        lvMonAn.setAdapter(adapter);
        lvMonAn.setEmptyView(tvTrong);

        // Đăng ký context menu: từ đây, giữ lâu một dòng sẽ mở menu ngữ cảnh
        registerForContextMenu(lvMonAn);

        lvMonAn.setOnItemClickListener((parent, view, position, id) ->
                hienChiTiet(danhSach.get(position)));
    }

    private void taoDuLieuMau() {
        danhSachGoc.add(new MonAn("Phở bò tái", "Nước dùng ninh xương 8 giờ", 45000, R.drawable.mon1));
        danhSachGoc.add(new MonAn("Bún chả Hà Nội", "Chả nướng than hoa", 50000, R.drawable.mon2));
        danhSachGoc.add(new MonAn("Cơm tấm sườn", "Sườn nướng mật ong", 40000, R.drawable.mon3));
        danhSachGoc.add(new MonAn("Bánh mì thịt nguội", "Pate nhà làm", 25000, R.drawable.mon4));
        danhSach.addAll(danhSachGoc);
    }

    // ===================================================================
    // 1. OPTIONS MENU — menu của cả màn hình, nằm trên thanh tiêu đề
    // ===================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;    // true = hiện menu; trả false thì menu không xuất hiện
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Dùng if–else, KHÔNG dùng switch: từ AGP 8, R.id không còn là hằng số
        if (id == R.id.action_them) {
            hienDialogThemSua(-1);
            return true;
        } else if (id == R.id.action_sap_xep_ten) {
            Collections.sort(danhSach, Comparator.comparing(MonAn::getTen));
            adapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.da_sap_xep_ten, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_sap_xep_gia) {
            Collections.sort(danhSach, (a, b) -> Integer.compare(a.getGia(), b.getGia()));
            adapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.da_sap_xep_gia, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_loc_gia) {
            hienDialogChonMucGia();
            return true;
        } else if (id == R.id.action_xoa_het) {
            xacNhanXoaHet();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===================================================================
    // 2. CONTEXT MENU — menu của MỘT dòng, mở bằng cách giữ lâu
    // ===================================================================

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, v, info);
        if (v.getId() == R.id.lvMonAn) {
            AdapterView.AdapterContextMenuInfo i = (AdapterView.AdapterContextMenuInfo) info;
            menu.setHeaderTitle(danhSach.get(i.position).getTen());
            getMenuInflater().inflate(R.menu.menu_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Vị trí dòng được giữ lâu nằm trong ContextMenuInfo, không phải trong item
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);

        int viTri = info.position;
        int id = item.getItemId();
        if (id == R.id.ctx_sua) {
            hienDialogThemSua(viTri);
            return true;
        } else if (id == R.id.ctx_xoa) {
            xacNhanXoaMot(viTri);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // ===================================================================
    // 3. DIALOG
    // ===================================================================

    private void hienDialogChonMucGia() {
        String[] cacMuc = {"Tất cả mức giá", "Dưới 40.000 đ", "Từ 40.000 đ trở lên"};
        final int[] tamChon = {mucGiaChon};
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.loc_muc_gia)
                .setSingleChoiceItems(cacMuc, mucGiaChon, (d, which) -> tamChon[0] = which)
                .setPositiveButton(R.string.luu, (d, w) -> {
                    mucGiaChon = tamChon[0];
                    apDungLocGia();
                })
                .setNegativeButton(R.string.huy, null)
                .show();
    }

    private void apDungLocGia() {
        danhSach.clear();
        for (MonAn m : danhSachGoc) {
            if (mucGiaChon == 0
                    || (mucGiaChon == 1 && m.getGia() < 40000)
                    || (mucGiaChon == 2 && m.getGia() >= 40000)) {
                danhSach.add(m);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /** Hộp thoại thông báo đơn giản: chỉ có nội dung và một nút OK. */
    private void hienChiTiet(MonAn mon) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(mon.getTen())
                .setMessage(getString(R.string.chi_tiet_format,
                        mon.getMoTa(), MonAn.dinhDangGia(mon.getGia())))
                .setPositiveButton(R.string.dong, null)   // null = chỉ đóng hộp thoại
                .show();
    }

    /** Hộp thoại xác nhận: việc xóa chỉ xảy ra khi người dùng bấm Xóa. */
    private void xacNhanXoaMot(int viTri) {
        MonAn mon = danhSach.get(viTri);
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.xac_nhan_xoa)
                .setMessage(getString(R.string.hoi_xoa, mon.getTen()))
                .setPositiveButton(R.string.xoa, (d, w) -> xoaCoHoanTac(viTri))
                .setNegativeButton(R.string.huy, null)
                .show();
    }

    private void xacNhanXoaHet() {
        if (danhSach.isEmpty()) {
            Toast.makeText(this, R.string.danh_sach_trong, Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.xac_nhan_xoa)
                .setMessage(getString(R.string.hoi_xoa_het, danhSach.size()))
                .setPositiveButton(R.string.xoa_het, (d, w) -> {
                    List<MonAn> banSaoLuu = new ArrayList<>(danhSach);
                    List<MonAn> banSaoGoc = new ArrayList<>(danhSachGoc);
                    danhSach.clear();
                    danhSachGoc.clear();
                    adapter.notifyDataSetChanged();
                    Snackbar.make(lvMonAn, R.string.da_xoa_het, Snackbar.LENGTH_LONG)
                            .setAction(R.string.hoan_tac, v -> {
                                danhSachGoc.addAll(banSaoGoc);
                                danhSach.addAll(banSaoLuu);
                                adapter.notifyDataSetChanged();
                            })
                            .show();
                })
                .setNegativeButton(R.string.huy, null)
                .show();
    }

    /** Xóa một món nhưng cho phép hoàn tác trong vài giây. */
    private void xoaCoHoanTac(int viTri) {
        MonAn daXoa = danhSach.remove(viTri);
        int viTriGoc = danhSachGoc.indexOf(daXoa);
        danhSachGoc.remove(daXoa);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "Đã xóa " + daXoa.getTen() + " tại vị trí " + viTri);

        Snackbar.make(lvMonAn, getString(R.string.da_xoa, daXoa.getTen()), Snackbar.LENGTH_LONG)
                .setAction(R.string.hoan_tac, v -> {
                    if (viTriGoc >= 0 && viTriGoc <= danhSachGoc.size()) {
                        danhSachGoc.add(viTriGoc, daXoa);
                    } else {
                        danhSachGoc.add(daXoa);
                    }
                    danhSach.add(viTri, daXoa);      // trả về đúng vị trí cũ
                    adapter.notifyDataSetChanged();
                    lvMonAn.smoothScrollToPosition(viTri);
                })
                .show();
    }

    /**
     * Hộp thoại tự thiết kế: dùng setView để nhét một layout riêng vào.
     * viTri == -1 nghĩa là thêm mới, ngược lại là sửa món tại vị trí đó.
     */
    private void hienDialogThemSua(int viTri) {
        boolean themMoi = (viTri < 0);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_mon_an, null, false);
        EditText edtTen = v.findViewById(R.id.edtTen);
        EditText edtMoTa = v.findViewById(R.id.edtMoTa);
        EditText edtGia = v.findViewById(R.id.edtGia);

        if (!themMoi) {
            MonAn mon = danhSach.get(viTri);
            edtTen.setText(mon.getTen());
            edtMoTa.setText(mon.getMoTa());
            edtGia.setText(String.valueOf(mon.getGia()));
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(themMoi ? R.string.them_mon : R.string.sua_mon)
                .setView(v)
                .setPositiveButton(R.string.luu, null)   // gán null rồi ghi đè ở dưới
                .setNegativeButton(R.string.huy, null)
                .create();

        dialog.show();

        // Mẹo quan trọng: gán lại OnClickListener SAU khi show() thì hộp thoại
        // sẽ KHÔNG tự đóng khi dữ liệu nhập chưa hợp lệ.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(btn -> {
            String ten = edtTen.getText().toString().trim();
            String moTa = edtMoTa.getText().toString().trim();
            String giaStr = edtGia.getText().toString().trim();

            if (ten.isEmpty()) {
                edtTen.setError(getString(R.string.err_ten));
                return;      // không đóng hộp thoại
            }
            int gia;
            try {
                gia = Integer.parseInt(giaStr);
            } catch (NumberFormatException e) {
                edtGia.setError(getString(R.string.err_gia));
                return;
            }
            if (gia <= 0) {
                edtGia.setError(getString(R.string.err_gia_duong));
                return;
            }

            if (themMoi) {
                MonAn moi = new MonAn(ten, moTa, gia, R.drawable.mon1);
                danhSachGoc.add(0, moi);
                danhSach.add(0, moi);
                lvMonAn.smoothScrollToPosition(0);
            } else {
                MonAn mon = danhSach.get(viTri);
                mon.setTen(ten);
                mon.setMoTa(moTa);
                mon.setGia(gia);
            }
            adapter.notifyDataSetChanged();
            dialog.dismiss();   // tự tay đóng khi mọi thứ đã hợp lệ
            Toast.makeText(this, themMoi ? R.string.da_them : R.string.da_sua,
                    Toast.LENGTH_SHORT).show();
        });
    }
}
