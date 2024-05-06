document.addEventListener("DOMContentLoaded", function (event) {
  const btnExport = document.getElementById("btn-export");
  const btnImport = document.getElementById("btn-import");
  const fileInput = document.getElementById("file-input");

  btnImport.addEventListener("click", function () {
    fileInput.click();
  });

  fileInput.addEventListener("change", function () {
    // Kiểm tra xem có tệp nào đã được chọn hay không
    if (fileInput.files.length > 0) {
      const files = fileInput.files[0];
      
      if(!files.name.endsWith('.xlsx')){
          alert("Vui lòng chọn file excel!");
          return;
      }
      // Tạo một FormData object và thêm tệp tin vào đó
      const file = new FormData();
      file.append("file", files);

      // Gửi yêu cầu import khi có tệp tin được chọn
      fetch("/thanhvien/import-excel", {
        method: "POST",
        body: file, // Sử dụng FormData chứa dữ liệu tệp tin
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          // Nếu import thành công, làm gì đó nếu cần thiết
          console.log("Import successful");
          window.location.href = "/thanhvien";
        })
        .catch((error) => {
          console.error("There was an error with the fetch operation:", error);
        });
    } else {
      console.log("Không có tệp nào được chọn");
    }
  });

  btnExport.addEventListener("click", function () {
    // Gửi yêu cầu xuất Excel khi nút được nhấp
    fetch("/thanhvien/export-excel", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        // Trả về file excel từ response
        return response.blob();
      })
      .then((blob) => {
        // Tạo một đường dẫn URL từ dữ liệu blob
        const url = window.URL.createObjectURL(new Blob([blob]));
        // Tạo một thẻ a để tải xuống file excel
        const a = document.createElement("a");
        a.href = url;
        a.download = "thanhvien.xlsx"; // Tên file khi tải xuống
        // Thêm thẻ a vào DOM và kích hoạt sự kiện click để bắt đầu tải xuống
        document.body.appendChild(a);
        a.click();
        // Sau khi tải xuống, loại bỏ thẻ a
        window.URL.revokeObjectURL(url);
      })
      .catch((error) => {
        console.error("There was an error with the fetch operation:", error);
      });
  });
});

// Pagination
const paginationNav = document.querySelector(".pagination-container");
const curPage = Number.parseInt(paginationNav.dataset.pageNo);
const totalPages = Number.parseInt(paginationNav.dataset.totalPages);
const query = paginationNav.dataset.query;
const urlArgs = { query };
const pagination = new Pagination(
  "/thanhvien/search",
  urlArgs,
  curPage,
  totalPages
);
