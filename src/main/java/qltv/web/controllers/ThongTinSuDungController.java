/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltv.web.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qltv.web.dto.ThanhVienDTO;
import qltv.web.dto.ThongTinSuDungDTO;
import qltv.web.dto.ThongTinSuDungResponse;
import qltv.web.security.SecurityUtil;
import qltv.web.services.ThanhVienService;
import qltv.web.services.ThietBiService;
import qltv.web.services.ThongTinSuDungService;
import qltv.web.services.XuLyService;

/**
 *
 * @author Acer
 */
@Controller
public class ThongTinSuDungController {

    ThietBiService thietBiService;
    ThanhVienService thanhVienService;
    XuLyService xuLyService;
    ThongTinSuDungService thongTinSuDungService;

    @Autowired
    public ThongTinSuDungController(ThietBiService thietBiService, ThanhVienService thanhVienService, XuLyService xuLyService, ThongTinSuDungService thongTinSuDungService) {
        this.thietBiService = thietBiService;
        this.thanhVienService = thanhVienService;
        this.xuLyService = xuLyService;
        this.thongTinSuDungService = thongTinSuDungService;
        this.thongTinSuDungService.xoaDatChoQuaHan();
    }

    @GetMapping("/thongtinsudung")
    public String listThongTinSuDung(Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        ThongTinSuDungResponse ttsdResponse = thongTinSuDungService.getTTSDMuonTra(0, 10, "");
        model.addAttribute("user", user);
        model.addAttribute("ttsdResponse", ttsdResponse);
        return "muon-tra-list";
    }

    @GetMapping("/thongtinsudung/search")
    public String searchThongTinSuDung(@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "query", defaultValue = "", required = false) String query, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        ThongTinSuDungResponse ttsdResponse = thongTinSuDungService.getTTSDMuonTra(pageNo - 1, pageSize, query);
        model.addAttribute("user", user);
        model.addAttribute("ttsdResponse", ttsdResponse);
        model.addAttribute("query", query);
        return "muon-tra-list";
    }

    @GetMapping("/thongtinsudung/muon")
    public String loadMuonThietBiPage(Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        List<ThongTinSuDungDTO> ttsds = thongTinSuDungService.findTtsdSoHuuThietBiDangBan();
        Long nextMaTT = thongTinSuDungService.getMaxMaTT() + 1;
        model.addAttribute("user", user);
        model.addAttribute("ttsds", ttsds);
        model.addAttribute("nextMaTT", nextMaTT);
        return "muon-thiet-bi";
    }

    @PostMapping("/thongtinsudung/muon")
    @ResponseBody
    public String saveThongTinSuDung(
            @RequestBody ThongTinSuDungDTO ttsdDTO,
            Model model) {

        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        if (thongTinSuDungService.thietBiDangDuocMuon(ttsdDTO.getThietBi().getMaTB())) {
            return "Thiết bị đang được người khác mượn";
        }
        if (thongTinSuDungService.thietBiDangDuocDatCho(ttsdDTO.getThietBi().getMaTB())) {
            return "Thiết bị đã được đặt chổ trong hôm nay";
        }
        if (xuLyService.thanhVienDangBiXuLy(ttsdDTO.getThanhVien().getMaTV())) {
            return "Thành viên đang bị xử lý vi phạm";
        }
        try {
            thongTinSuDungService.saveThongTinSuDung(ttsdDTO);
            return "success";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping("/thongtinsudung/muon/search")
    @ResponseBody
    public List<ThongTinSuDungDTO> searchThongTinSuDungDangBan(@RequestParam(value = "query") String query, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return null;
        }
        Long maTV = Long.parseLong(username);
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        List<ThongTinSuDungDTO> ttsds = thongTinSuDungService.searchTtsdSoHuuThietBiDangBan(query);
        return ttsds;
    }

    @GetMapping("/thongtinsudung/tra")
    public String loadTraThietBiPage(Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) { // kiểm tra đăng nhập
            return "redirect:/login";
        }
        Long maTV = Long.parseLong(username);
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        List<ThongTinSuDungDTO> ttsds = thongTinSuDungService.getAllThongTinSuDungChuaTra();
        model.addAttribute("user", user);
        model.addAttribute("ttsds", ttsds);
        return "tra-thiet-bi";
    }

    @GetMapping("/thongtinsudung/tra/search")
    @ResponseBody
    public List<ThongTinSuDungDTO> searchThongTinSuDungDangMuon(@RequestParam(value = "query") String query, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return null;
        }
        Long maTV = Long.parseLong(username);
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        List<ThongTinSuDungDTO> ttsds = thongTinSuDungService.searchTtsdSoHuuThietBiDangMuon(query);
        return ttsds;
    }

    @GetMapping("/thongtinsudung/{maTT}/tra")
    public String traThietBi(@PathVariable("maTT") int maTT) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        thongTinSuDungService.traThietBi(maTT);
        return "redirect:/thongtinsudung/tra";
    }
}
