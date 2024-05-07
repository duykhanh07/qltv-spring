package qltv.web.services.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qltv.web.dto.ThongTinSuDungDTO;
import qltv.web.dto.ThongTinSuDungResponse;
import qltv.web.mappers.ThanhVienMapper;
import qltv.web.mappers.ThongTinSuDungMapper;
import qltv.web.models.ThongTinSuDung;
import qltv.web.repositories.ThanhVienRepository;
import qltv.web.repositories.ThietBiRepository;
import qltv.web.repositories.ThongTinSuDungRepository;
import qltv.web.services.ThongTinSuDungService;

@Service
public class ThongTinSuDungServiceImpl implements ThongTinSuDungService {

    private ThongTinSuDungRepository thongTinSuDungRepository;
    private ThanhVienRepository thanhVienReponsitory;
    private ThietBiRepository thietBiReponsitory;

    public ThongTinSuDungServiceImpl(ThongTinSuDungRepository thongTinSuDungRepository, ThanhVienRepository thanhVienReponsitory, ThietBiRepository thietBiReponsitory) {
        this.thongTinSuDungRepository = thongTinSuDungRepository;
        this.thanhVienReponsitory = thanhVienReponsitory;
        this.thietBiReponsitory = thietBiReponsitory;
    }

    @Override
    public List<ThongTinSuDungDTO> getAllThongTinSuDung() {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.findAllttsdMuonTra();
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }

    @Override
    public ThongTinSuDung saveThongTinSuDung(ThongTinSuDungDTO ttsdDTO) {
        ThongTinSuDung ttsd = ThongTinSuDungMapper.mapToThongTinSuDung(ttsdDTO);
        return thongTinSuDungRepository.save(ttsd);
    }

    @Override
    public void updateThongTinSuDung(ThongTinSuDungDTO ttsdDTO) {
        ThongTinSuDung ttsd = ThongTinSuDungMapper.mapToThongTinSuDung(ttsdDTO);
        thongTinSuDungRepository.save(ttsd);
    }

    @Override
    public void deleteThongTinSuDung(long maTTSD) {
        thongTinSuDungRepository.deleteById(maTTSD);
    }

    @Override
    public List<ThongTinSuDungDTO> searchThongTinSuDung(String query) {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.searchThongTinSuDung(query);
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }
    @Override
    public List<ThongTinSuDungDTO> getTtsdByMaTB(long maTB){
        List<ThongTinSuDung> ttsds = thongTinSuDungRepository.getTtsdByMaTB(maTB);
        return ttsds.stream().map(ttsd -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(ttsd)).collect(Collectors.toList());
    }
    @Override
    public int getMaxIdFlusOne(){
        return thongTinSuDungRepository.getMaxIdFlusOne();
    }
    @Override
    public List<ThongTinSuDungDTO> findTtsdSoHuuThietBiDangBan() {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.findTtsdSoHuuThietBiDangBan();
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }

    @Override
    public List<ThongTinSuDungDTO> searchTtsdSoHuuThietBiDangBan(String query) {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.SearchTtsdSoHuuThietBiDangBan(query);
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }

    @Override
    public Long getMaxMaTT() {
        return thongTinSuDungRepository.getMaxMaTT();
    }

    @Override
    public boolean thietBiDangDuocMuon(int maTB) {
        return thongTinSuDungRepository.thietBiDangDuocMuon(maTB) > 0;
    }

    @Override
    public boolean thietBiDangDuocDatCho(int maTB) {
        return thongTinSuDungRepository.thietBiDuocDatChoTrongNgay(maTB) > 0;
    }

    @Override
    public void xoaDatChoQuaHan() {
        List<ThongTinSuDung> thongTinSuDungs = thongTinSuDungRepository.findAllttsdDatChoQuaHan();

        // Lọc ra các thông tin sử dụng có thời gian đặt chỗ trước thời điểm hiện tại một giờ
        List<ThongTinSuDung> thongTinSuDungsQuaHan = new ArrayList<>();
        Date oneHourAgo = Date.from(Instant.now().minus(Duration.ofHours(1)));
        for (ThongTinSuDung ttsd : thongTinSuDungs) {
            if (ttsd.getTgDatCho().before(oneHourAgo)) {
                thongTinSuDungsQuaHan.add(ttsd);
            }
        }
        List<ThongTinSuDungDTO> thongTinSuDungDTOs = thongTinSuDungsQuaHan.stream()
                .map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung))
                .collect(Collectors.toList());

        // Xóa các thông tin sử dụng đã quá hạn
        for (ThongTinSuDungDTO ttsdDTO : thongTinSuDungDTOs) {
            deleteThongTinSuDung(ttsdDTO.getMaTT());
        }
    }

    @Override
    public List<ThongTinSuDungDTO> getAllThongTinSuDungChuaTra() {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.findAllttsdChuaTra();
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }

    @Override
    public List<ThongTinSuDungDTO> searchTtsdSoHuuThietBiDangMuon(String query) {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.SearchTtsdSoHuuThietBiDangMuon(query);
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }

    @Override
    public void traThietBi(long maTTSD) {
        ThongTinSuDung ttsd = thongTinSuDungRepository.findByMaTT(maTTSD);
        ThongTinSuDungDTO ttsdDTO = ThongTinSuDungMapper.mapToThongTinSuDungDTO(ttsd);
        ttsdDTO.setTgTra(new Date());
        updateThongTinSuDung(ttsdDTO);
    }

    @Override
    public ThongTinSuDungResponse findThietBiDatChoUser(int pageNo, int pageSize, long maTV, String query) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ThongTinSuDung> result = thongTinSuDungRepository.findThietBiDatChoUser(maTV, query, pageable);
        List<ThongTinSuDung> listTTSD = result.getContent();
        List<ThongTinSuDungDTO> content = listTTSD.stream()
                .map(ttsd -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(ttsd))
                .collect(Collectors.toList());

        ThongTinSuDungResponse response = new ThongTinSuDungResponse();
        response.setContent(content);
        response.setPageNo(pageNo + 1);
        response.setPageSize(pageSize);
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        return response;
    }

    @Override
    public List<ThongTinSuDungDTO> getThongTinSuDungChuaTraTheoMaTV(long maTV) {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.findAllttsdChuaTraTheoMaTV(maTV);
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }

    @Override
    public ThongTinSuDungResponse getTTSDMuonTra(int pageNo, int pageSize, String query) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ThongTinSuDung> result = thongTinSuDungRepository.getTTSDMuonTra(query, pageable);
        List<ThongTinSuDungDTO> content = result.getContent().stream()
                .map(ttsd -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(ttsd))
                .collect(Collectors.toList());

        ThongTinSuDungResponse response = new ThongTinSuDungResponse();
        response.setContent(content);
        response.setPageNo(pageNo + 1);
        response.setPageSize(pageSize);
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        return response;
    }

    @Override
    public void vaoKhuHocTap(long maTV) {
        ThongTinSuDungDTO ttsdDTO = new ThongTinSuDungDTO();
        ttsdDTO.setMaTT(getMaxIdFlusOne());
        ttsdDTO.setTgVao(new Date());
        ttsdDTO.setTgMuon(null);
        ttsdDTO.setTgTra(null);
        ttsdDTO.setTgDatCho(null);
        ttsdDTO.setThanhVien(ThanhVienMapper.mapToThanhVienDto(thanhVienReponsitory.findByMaTV(maTV)));
        ttsdDTO.setThietBi(null);
        saveThongTinSuDung(ttsdDTO);
    }

    @Override
    public List<ThongTinSuDungDTO> getTTSDVaoKhuHocTapTrongNgay() {
        List<ThongTinSuDung> thongTinSuDungs = (ArrayList) thongTinSuDungRepository.getTTSDVaoKhuHocTapTrongNgay();
        return thongTinSuDungs.stream().map(thongTinSuDung -> ThongTinSuDungMapper.mapToThongTinSuDungDTO(thongTinSuDung)).collect(Collectors.toList());
    }
}
