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
import qltv.web.security.SecurityUtil;
import qltv.web.services.ThanhVienService;
import qltv.web.services.ThongTinSuDungService;
import qltv.web.services.XuLyService;

/**
 *
 * @author Acer
 */
@Controller
public class KhuHocTapController {

    XuLyService xuLyService;
    ThongTinSuDungService thongTinSuDungService;
    ThanhVienService thanhVienService;

    @Autowired
    public KhuHocTapController(ThanhVienService thanhVienService, XuLyService xuLyService, ThongTinSuDungService thongTinSuDungService) {
        this.xuLyService = xuLyService;
        this.thongTinSuDungService = thongTinSuDungService;
        this.thanhVienService = thanhVienService;
    }

    @GetMapping("/khuhoctap")
    public String loadKhuHocTapPage(Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        List<ThongTinSuDungDTO> ttsds = thongTinSuDungService.getTTSDVaoKhuHocTapTrongNgay();
        model.addAttribute("user", user);
        model.addAttribute("ttsds", ttsds);
        return "khu-hoc-tap";
    }

    @GetMapping("/khuhoctap/{maTV}/vao")
    @ResponseBody
    public String vaoKhuHocTap(@PathVariable("maTV") int maTV, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "Ban chưa đăng nhập";
        }
        int maTV1 = Integer.parseInt(username);
        if (maTV1 > 10) {
            return "Bạn không phải admin";
        }

        if (xuLyService.thanhVienDangBiXuLy(maTV)) {
            return "Thành viên đang bị xử lý không thể tham gia khu học tập";
        }
        thongTinSuDungService.vaoKhuHocTap(maTV);
        return "success";
    }
}
