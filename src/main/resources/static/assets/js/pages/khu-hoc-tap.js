document.addEventListener("DOMContentLoaded", function () {
  btnVaoKhuHocTap = document.getElementById("btn-Vao");
  txtMaTV = document.getElementById("input-MaThanhVien");
  txtHoTen = document.getElementById("input-HoTen");
  txtKhoa = document.getElementById("input-Khoa");
  txtNganh = document.getElementById("input-Nganh");
  txtSDT = document.getElementById("input-SDT");
  txtEmail = document.getElementById("input-Email");

  btnVaoKhuHocTap.addEventListener("click", function () {
    let maTV = txtMaTV.value;
    if (maTV.trim() == "") {
      Swal.fire({
        title: "Vui lòng nhập mã thành viên",
        text: "Mã thành viên không được để trống",
        icon: "error",
      });
      return;
    }
    fetch(`/thanhvien/getbyid?query=${maTV}`)
      .then((response) => response.json())
      .then((data) => {
        if (data == null || data.hoTen == undefined) {
          Swal.fire({
            title: "Dữ liệu không hợp lệ",
            text: "Thành Viên không tồn tại",
            icon: "error",
          });
          return;
        }
        txtHoTen.value = data.hoTen;
        txtKhoa.value = data.khoa;
        txtNganh.value = data.nganh;
        txtSDT.value = data.sdt;
        txtEmail.value = data.email;
      });
    const url = "/khuhoctap/"+maTV+"/vao";
    $.ajax({
      type: "GET",
      url: url,
      success: function (data) {
        if (data == "success") {
          Swal.fire({
            title: "Vào khi học tập thành công",
            icon: "success",
          })
        } else {
          Swal.fire({
            title: "Vào khu học tập thất bại",
            icon: "error",
            text: data,
          });
        }
      },
      error: function (e) {
        Swal.fire({
          title: "Vào khu hoc tập thất bại",
          icon: "error",
        });
      },
    });
  });
});
