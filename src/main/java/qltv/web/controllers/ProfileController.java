/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltv.web.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import qltv.web.dto.ThanhVienDTO;
import qltv.web.dto.ThanhVienInfoDTO;
import qltv.web.dto.ThanhVienPasswordDTO;
import qltv.web.dto.ThongTinSuDungDTO;
import qltv.web.dto.ThongTinSuDungResponse;
import qltv.web.dto.XuLyDTO;
import qltv.web.repositories.ThanhVienRepository;
import qltv.web.security.SecurityUtil;
import qltv.web.services.ThanhVienService;
import qltv.web.services.ThongTinSuDungService;
import qltv.web.services.XuLyService;

/**
 *
 * @author DELL
 */
@Controller
public class ProfileController {

    private ThanhVienService tvService;
    private ThanhVienRepository tvRepository;
    private XuLyService xuLyService;
    private ThongTinSuDungService thongTinSuDungService;
    @Autowired
    public ProfileController(ThanhVienService tvService, ThanhVienRepository tvRepository, XuLyService xuLyService, ThongTinSuDungService thongTinSuDungService) {
        this.tvService = tvService;
        this.tvRepository = tvRepository;
        this.xuLyService = xuLyService;
        this.thongTinSuDungService = thongTinSuDungService;
    }
    @GetMapping("/profile/{maTV}")
    public String editProfileForm(@PathVariable("maTV") long maTV, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        ThanhVienDTO thanhVien = tvService.findMemberById(maTV);
        model.addAttribute("thanhVien", thanhVien);
        return "profile";
    }

    @GetMapping("/profile/updatePassword/{maTV}")
    public String updatePassword(@PathVariable("maTV") long maTV, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        ThanhVienDTO thanhVien = tvService.findMemberById(maTV);
        model.addAttribute("thanhVien", thanhVien);
        return "updatePassword";
    }

    @PostMapping("/profile/{maTV}")
    @ResponseBody
    public String updateProfile(@PathVariable("maTV") long maTV,
            @Valid @ModelAttribute("thanhVien") ThanhVienInfoDTO thanhVien,
            BindingResult result, Model model, String hoTen, String khoa, String nganh, String soDienThoai, String email, String password) {

        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        tvService.updateInfoThanhVien(maTV, hoTen, khoa, nganh, soDienThoai, email);
        return "Hello" + thanhVien;
    }

    @PostMapping("/profile/updatePassword/{maTV}")
    @ResponseBody
    public String updatePassword(@PathVariable("maTV") long maTV,
            @Valid @ModelAttribute("thanhVien") ThanhVienInfoDTO thanhVien,
            BindingResult result, Model model, String currentPassword, String newPassword, String confirmPassword) {

        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);

        String encryptedPassword = tvService.findPasswordByMaTV(maTV);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(currentPassword, encryptedPassword)) {
            String newPasswordEncode = passwordEncoder.encode(newPassword);
            tvService.updatePassword(maTV, newPasswordEncode);
            return "true";
        } else {
            return "false";
        }
    }
    
    @GetMapping("/profile/vipham/{maTV}")
    public String xemTrangThaiViPham(@PathVariable("maTV") long maTV, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        
        List<XuLyDTO> xuLys = xuLyService.searchXuLy(maTV+"");
        model.addAttribute("xuLys", xuLys);
        return "viPham";
    }
    
    @GetMapping("/profile/danhsachmuon/{maTV}")
    public String xemThietBiDangMuon(@PathVariable("maTV") long maTV, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        
        List<ThongTinSuDungDTO> ttsds = thongTinSuDungService.getThongTinSuDungChuaTraTheoMaTV(maTV);
        model.addAttribute("ttsds", ttsds);
        return "thietBiDangMuon";
    }
    
    @GetMapping("/profile/datcho/{maTV}")
    public String listThietBiDatChoUser(@PathVariable("maTV") long maTV, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        if (userId != maTV) {
            return "redirect:/";
        }
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        
        ThongTinSuDungResponse ttsdResponse = thongTinSuDungService.findThietBiDatChoUser(0, 10, maTV, "");
        model.addAttribute("ttsdResponse", ttsdResponse);
        return "list-thiet-bi-dat-cho";
    }

    @GetMapping("/profile/datcho/{maTV}/search")
    public String searchThietBiDatChoUser(@PathVariable("maTV") long maTV,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        if (userId != maTV) {
            return "redirect:/";
        }
        ThanhVienDTO user = tvService.findMemberById(userId);
        model.addAttribute("user", user);
        ThongTinSuDungResponse ttsdResponse = thongTinSuDungService.findThietBiDatChoUser(pageNo - 1, pageSize, maTV, query);
        model.addAttribute("ttsdResponse", ttsdResponse);
        model.addAttribute("query", query);
        return "list-thiet-bi-dat-cho";
    }

    @PostMapping("/profile/datcho/{maTV}/delete")
    public String deleteDatCho(@PathVariable("maTV") long maTV,
            @RequestParam(value = "maTT", required = true) int maTT) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int userId = Integer.parseInt(username);
        if (userId != maTV) {
            return "redirect:/";
        }
        thongTinSuDungService.deleteThongTinSuDung(maTT);
        return "redirect:/profile/datcho/" + userId;
    }
}
