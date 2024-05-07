package qltv.web.services;

import java.util.List;
import qltv.web.dto.ThongTinSuDungDTO;
import qltv.web.dto.ThongTinSuDungResponse;
import qltv.web.models.ThongTinSuDung;

public interface ThongTinSuDungService {

    public List<ThongTinSuDungDTO> getAllThongTinSuDung();

    public ThongTinSuDung saveThongTinSuDung(ThongTinSuDungDTO ttsdDTO);

    public void updateThongTinSuDung(ThongTinSuDungDTO ttsdDTO);

    public void deleteThongTinSuDung(long maTTSD);

    public List<ThongTinSuDungDTO> searchThongTinSuDung(String query);
    
    public List<ThongTinSuDungDTO> getTtsdByMaTB(long maTB);
    
    public int getMaxIdFlusOne();

    public List<ThongTinSuDungDTO> findTtsdSoHuuThietBiDangBan();

    public List<ThongTinSuDungDTO> searchTtsdSoHuuThietBiDangBan(String query);

    public Long getMaxMaTT();

    public boolean thietBiDangDuocMuon(int maTB);

    public boolean thietBiDangDuocDatCho(int maTB);


    public void xoaDatChoQuaHan();

    public List<ThongTinSuDungDTO> getAllThongTinSuDungChuaTra();

    public List<ThongTinSuDungDTO> searchTtsdSoHuuThietBiDangMuon(String query);

    public void traThietBi(long maTTSD);

    public ThongTinSuDungResponse findThietBiDatChoUser(int pageNo, int pageSize, long maTV, String query);

    public List<ThongTinSuDungDTO> getThongTinSuDungChuaTraTheoMaTV(long maTV);
    
    public ThongTinSuDungResponse getTTSDMuonTra(int pageNo, int pageSize, String query);
    
    public void vaoKhuHocTap(long maTV);

    public List<ThongTinSuDungDTO> getTTSDVaoKhuHocTapTrongNgay();

}
