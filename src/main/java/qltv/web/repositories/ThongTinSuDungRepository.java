package qltv.web.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import qltv.web.models.ThongTinSuDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ThongTinSuDungRepository extends JpaRepository<ThongTinSuDung, Long> {

    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE ttsd.thietBi.maTB = :maTB AND DATE(ttsd.tgDatCho) = DATE(:tgMuon)")
    public List<ThongTinSuDung> findAllByMaTBAndTgDatCho(int maTB, Date tgMuon);

    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE (CAST(ttsd.thanhVien.maTV AS string) LIKE %:query% OR CAST(ttsd.thietBi.maTB AS string) LIKE %:query%) AND ttsd.thietBi.maTB IS NOT NULL")
    List<ThongTinSuDung> searchThongTinSuDung(@Param("query") String query);

    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE ttsd.tgVao IS NULL")
    List<ThongTinSuDung> findAllttsdMuonTra();

    // Hàm này lấy tất cả thông tin sử dụng có thiết bị đang được mượn hoặc được đặt
    // chổ trong ngày hiện tại
    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE (DATE(ttsd.tgDatCho) = CURRENT_DATE OR (ttsd.tgMuon IS NOT NULL AND ttsd.tgTra IS NULL)) AND ttsd.thietBi.maTB IS NOT NULL")
    List<ThongTinSuDung> findTtsdSoHuuThietBiDangBan();

    // hàm này tìm kiêm các thông tin sử dụng có thiet bị đang được mượn, hoặc đặt
    // chổ trong ngày hiện tại
    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE (DATE(ttsd.tgDatCho) = CURRENT_DATE OR (ttsd.tgMuon IS NOT NULL AND ttsd.tgTra IS NULL)) AND ttsd.thietBi.maTB IS NOT NULL AND CAST(ttsd.thietBi.maTB AS string) LIKE %:query%")
    List<ThongTinSuDung> SearchTtsdSoHuuThietBiDangBan(@Param("query") String query);

    @Query("SELECT MAX(ttsd.maTT) FROM ThongTinSuDung ttsd")
    Long getMaxMaTT();

    // Ham nay lay so luong thiet bi dang duoc muon, kiểm tra xem thiết bị có đang
    // được mượn không
    @Query("SELECT COUNT(ttsd) FROM ThongTinSuDung ttsd WHERE ttsd.thietBi.maTB = :maTB AND ttsd.tgMuon IS NOT NULL AND ttsd.tgTra IS NULL")
    int thietBiDangDuocMuon(@Param("maTB") int maTB);

    // hàm này lấy số lượng thiết bị được đặt chỗ trong ngày, kiểm tra thiết bị có
    // đang được đặt chổ không
    @Query("SELECT COUNT(ttsd) FROM ThongTinSuDung ttsd WHERE ttsd.thietBi.maTB = :maTB AND DATE(ttsd.tgDatCho) = CURRENT_DATE")
    int thietBiDuocDatChoTrongNgay(@Param("maTB") int maTB);

    // hàm này lấy tất cả các thông tin sử dụng có tgDatCho < hiện tại
    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE ttsd.tgDatCho < CURRENT_TIMESTAMP")
    List<ThongTinSuDung> findAllttsdDatChoQuaHan();

    // hàm này lấy tất cả các thông tin sử dụng có tgMuon != null và tgTra = null
    // tức là chưa trả
    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE ttsd.tgMuon IS NOT NULL AND ttsd.tgTra IS NULL")
    List<ThongTinSuDung> findAllttsdChuaTra();

    // hàm này lấy tất cả các thông tin sử dụng có tgMuon != null và tgTra = null
    // tức là chưa trả và có thietBi.maTB = :maTB
    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE ttsd.tgMuon IS NOT NULL AND ttsd.tgTra IS NULL AND CAST(ttsd.thietBi.maTB AS string) LIKE %:query%")
    List<ThongTinSuDung> SearchTtsdSoHuuThietBiDangMuon(@Param("query") String query);

    ThongTinSuDung findByMaTT(long maTT);

    @Query(value = """
            SELECT ttsd
            FROM ThongTinSuDung ttsd
                JOIN ttsd.thietBi tb
            WHERE ttsd.thanhVien.maTV = :maTV
                AND ttsd.tgDatCho IS NOT NULL
                AND tb.tenTB LIKE CONCAT('%', :tenTB, '%')
            ORDER BY ttsd.tgDatCho DESC
            """)
    Page<ThongTinSuDung> findThietBiDatChoUser(long maTV, String tenTB, Pageable pageable);

    @Query("SELECT ttsd FROM ThongTinSuDung ttsd RIGHT JOIN ttsd.thietBi tb WHERE tb.maTB = :maTB")
    List<ThongTinSuDung> getTtsdByMaTB(long maTB);

    @Query("SELECT MAX(ttsd.maTT) + 1 FROM ThongTinSuDung ttsd")
    int getMaxIdFlusOne();

    // hàm này lấy tất cả các thông tin sử dụng có tgMuon != null và tgTra = null
    // tức là chưa trả của một thành viên
    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE ttsd.tgMuon IS NOT NULL AND ttsd.tgTra IS NULL AND ttsd.thanhVien.maTV = :maTV")
    List<ThongTinSuDung> findAllttsdChuaTraTheoMaTV(@Param("maTV") long maTV);

    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE (CAST(ttsd.thanhVien.maTV AS string) LIKE %:query% OR CAST(ttsd.thietBi.maTB AS string) LIKE %:query%) AND ttsd.thietBi.maTB IS NOT NULL")
    Page<ThongTinSuDung> getTTSDMuonTra(String query, Pageable pageable);

    @Query("SELECT ttsd FROM ThongTinSuDung ttsd WHERE DATE(ttsd.tgVao) = CURRENT_DATE")
    List<ThongTinSuDung> getTTSDVaoKhuHocTapTrongNgay();
}
