package qltv.web.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;

import qltv.web.dto.ThanhVienDTO;

import qltv.web.dto.ThietBiDTO;
import qltv.web.dto.ThietBiResponse;
import qltv.web.dto.ThongTinSuDungDTO;
import qltv.web.security.SecurityUtil;
import qltv.web.services.ThanhVienService;
import qltv.web.services.ThietBiService;
import qltv.web.services.ThongTinSuDungService;
import qltv.web.services.XuLyService;

//@RestController
//@RequestMapping("/thietbi")
@Controller
public class ThietBiController {

    private ThietBiService thietBiService;
    private ThanhVienService thanhVienService;
    private ThongTinSuDungService ttsdService;
    private XuLyService xuLyService;

    @Autowired
    public ThietBiController(ThietBiService thietBiService, ThanhVienService thanhVienService, ThongTinSuDungService ttsdService,XuLyService xuLyService) {
        this.thietBiService = thietBiService;
        this.thanhVienService = thanhVienService;
        this.ttsdService = ttsdService;
        this.xuLyService = xuLyService;
    }

    @GetMapping("/thietbi")
    public String listThietBi(Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        ThietBiResponse thietBiResponse = thietBiService.getListThietBi(0, 10, "");
        model.addAttribute("thietBiResponse", thietBiResponse);
        return "thiet-bi-list";
    }

    @GetMapping("/thietbi/new")
    public String createThietBiForm(Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        ThietBiDTO thietBi = new ThietBiDTO();
        model.addAttribute("thietBi", thietBi);
        return "thiet-bi-create";
    }

    @PostMapping("/thietbi/new")
    public String saveThietBi(@Valid @ModelAttribute("thietBi") ThietBiDTO thietBi, BindingResult result,
            Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            model.addAttribute("thietBi", thietBi);
            return "thiet-bi-create";
        }
        thietBiService.saveThietBi(thietBi);
        return "redirect:/thietbi";
    }

    @GetMapping("/thietbi/{maTB}/edit")
    public String editThietBiForm(@PathVariable("maTB") int maTB, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        ThietBiDTO thietBi = thietBiService.findThietBiById(maTB);
        model.addAttribute("thietBi", thietBi);
        return "thiet-bi-edit";
    }

    @PostMapping("/thietbi/{maTB}/edit")
    public String updateThietBi(@PathVariable("maTB") int maTB,
            @Valid @ModelAttribute("thietBi") ThietBiDTO thietBi,
            BindingResult result, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        
        if (result.hasErrors()) {
            return "thiet-bi-edit";
        }
        thietBiService.updateThietBi(thietBi);
        return "redirect:/thietbi";
    }

    @GetMapping("/thietbi/{maTB}/delete")
    public String deleteThietBi(@PathVariable("maTB") int maTB, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        if (maTV > 10) {
            return "redirect:/";
        }
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        thietBiService.deleteThietBi(maTB);
        return "redirect:/thietbi";
    }

    @GetMapping("/thietbi/search")
    public String searchThietBi(@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
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
        model.addAttribute("user", user);
        ThietBiResponse thietBiResponse = thietBiService.getListThietBi(pageNo - 1, pageSize, query);
        model.addAttribute("thietBiResponse", thietBiResponse);
        model.addAttribute("query", query);
        return "thiet-bi-list";
    }

    @GetMapping("/thietbi/export-excel")
    public ResponseEntity<byte[]> exportExcel(HttpServletResponse response) throws IOException {
        // Tạo một workbook Excel mới
        Workbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Danh sách thiết bị");

        // Tạo dòng header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã");
        headerRow.createCell(1).setCellValue("Tên thiết bị");
        headerRow.createCell(2).setCellValue("Mô tả");

        // Lấy danh sách thiết bị từ service
        List<ThietBiDTO> listThietBi = thietBiService.getAllThietBi();

        // Tạo dữ liệu cho bảng Excel từ danh sách thiết bị
        int rowNum = 1;
        for (ThietBiDTO thietBi : listThietBi) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(thietBi.getMaTB());
            row.createCell(1).setCellValue(thietBi.getTenTB());
            row.createCell(2).setCellValue(thietBi.getMoTaTB());
        }

        // Ghi workbook vào một ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Chuẩn bị dữ liệu để trả về cho người dùng
        byte[] excelBytes = outputStream.toByteArray();

        // Thiết lập các thông tin header cho response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "danh-sach-thiet-bi.xlsx");

        // Trả về response với dữ liệu file Excel và các header tương ứng
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @PostMapping("/thietbi/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Đọc file Excel
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua tiêu đề nếu có
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int maTB = (int) row.getCell(0).getNumericCellValue();
                ThietBiDTO thietBi = thietBiService.findThietBiById(maTB);
                if (thietBi == null) {
                    thietBi = new ThietBiDTO();
                    thietBi.setMaTB((int) row.getCell(0).getNumericCellValue());
                    thietBi.setTenTB(row.getCell(1).getStringCellValue());
                    thietBi.setMoTaTB(row.getCell(2).getStringCellValue());

                    // Lưu thiết bị vào cơ sở dữ liệu
                    thietBiService.saveThietBi(thietBi);
                }
            }
            // Đóng workbook và trả về thông báo thành công
            workbook.close();
            return "redirect:/thietbi";
        } catch (IOException e) {
            // Xử lý lỗi khi đọc file Excel
            e.printStackTrace();
            return "redirect:/thietbi";
        }
    }

    @GetMapping("/datchothietbi")
    public String listThietBiDatCho(Model model) {
        String username = SecurityUtil.getUserSession();
        ThanhVienDTO user = null;
        if (username != null) {
            int maTV = Integer.parseInt(username);
            user = thanhVienService.findMemberById(maTV);
            boolean isViPham = xuLyService.thanhVienDangBiXuLy(maTV);
            model.addAttribute("isViPham", isViPham);
            System.out.println(isViPham);
        }
        List<ThietBiDTO> tblist = thietBiService.getAllThietBi();
        model.addAttribute("user", user);
        model.addAttribute("tblist", tblist);
        return "datchothietbi";
    }

    @GetMapping("/datchothietbi/search")
    public String searchThietBiDatCho(@RequestParam(value = "query") String query, Model model) {
        String username = SecurityUtil.getUserSession();
        ThanhVienDTO user = null;
        if (username != null) {
            int maTV = Integer.parseInt(username);
            user = thanhVienService.findMemberById(maTV);
        }
        model.addAttribute("user", user);
        List<ThietBiDTO> tblist = thietBiService.searchThietBi(query);
        model.addAttribute("tblist", tblist);
        model.addAttribute("query", query);
        return "datchothietbi";

    }

    @GetMapping("/datchothietbi/{maTB}/datcho")
    public String DatchoThietBi(@PathVariable("maTB") long maTB, Model model) {
        String username = SecurityUtil.getUserSession();
        if (username == null) {
            return "redirect:/login";
        }
        int maTV = Integer.parseInt(username);
        ThanhVienDTO user = thanhVienService.findMemberById(maTV);
        model.addAttribute("user", user);
        ThietBiDTO thietbi = thietBiService.findByMaTB(maTB);
        model.addAttribute("thietbi", thietbi);
        List<ThongTinSuDungDTO> ttsds = ttsdService.getTtsdByMaTB(maTB);
        model.addAttribute("ttsds", ttsds);
        return "datcho";
    }

    @PostMapping("/datchothietbi/save")
    public String saveDatCho(@RequestParam("maTV") String maTV, @RequestParam("tenTV") String tenTV, @RequestParam("maTB") String maTB, @RequestParam("tenTB") String tenTB, @RequestParam("thoiGian") String thoiGian) {
        ThongTinSuDungDTO ttsdDTO = new ThongTinSuDungDTO();
        long maTVLong = Long.parseLong(maTV);
        long maTBLong = Long.parseLong(maTB);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date tgDatCho = null;
        try {
            tgDatCho = dateFormat.parse(thoiGian);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ThanhVienDTO tv = thanhVienService.findMemberById(maTVLong);
        ThietBiDTO tb = thietBiService.findByMaTB(maTBLong);
        int id = ttsdService.getMaxIdFlusOne();
        ttsdDTO.setMaTT(id);
        ttsdDTO.setThanhVien(tv);
        ttsdDTO.setThietBi(tb);
        ttsdDTO.setTgDatCho(tgDatCho);
        ttsdService.saveThongTinSuDung(ttsdDTO);
        return "redirect:/";
    }

    // ---------------------------------hàm này của tiến nha đừng có xóa---------------------------------
    @GetMapping("/thietbi/getbyid")
    @ResponseBody
    public ThietBiDTO getThietBiById(@RequestParam(value = "query") String query, Model model) {
        try {
            Long maTB = Long.parseLong(query);
            ThietBiDTO thietBi = thietBiService.findThietBiById(maTB);
            return thietBi;
        } catch (Exception e) {
            return null;
        }
    }
    //--------------------------------------------------------------
}
