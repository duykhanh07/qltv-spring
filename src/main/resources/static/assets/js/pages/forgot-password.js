/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
// Gửi mã OTP qua Email
$("#btnRecover").click(function (e) {
    e.preventDefault();

    let email = $("#reminder-credential").val();

    function validate() {
        // Biểu thức chính quy để kiểm tra định dạng email
        let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        error = "";
        flag = true;
        if (email === "") {
            error += "Vui lòng nhập email!";
            flag = false;
        } else if (!emailRegex.test(email)) {
            error += "Email không đúng định dạng\n";
            flag = false;
        }
        if (error != "") {
            Swal.fire({
                title: "Dữ liệu không hợp lệ",
                text: error,
                icon: "error",
            });
        }
        return flag;
    }
    console.log(email);
    if (validate()) {
        $.ajax({
            url: `/forgot-password`, // Thay thế "url-xu-ly-du-lieu.php" bằng URL xử lý dữ liệu trên máy chủ của bạn
            type: "POST",
            data: {
                email: email,
            },
            success: function (response) {
                // Xử lý phản hồi thành công từ máy chủ
                if (response === "true") {
//                Swal.fire({
//                    title: "Thông báo!",
//                    text: "Cập nhật hồ sơ thành công",
//                    icon: "success",
//                    confirmButtonText: "Ok"
//                });
                    window.location.href = "/forgot-password/confirm-otp";
                } else {
                    Swal.fire({
                        title: "Thông báo!",
                        text: "Tài khoản không tồn tại!",
                        icon: "error",
                        confirmButtonText: "Ok"
                    });
                }
                console.log(response);
            },
            error: function (xhr, status, error) {
                // Xử lý lỗi
                console.log("Lỗi kìa mẹ mày");
            }
        });
    }

});

// Kiểm tra mã OTP
$("#opt").click(function (e) {
    e.preventDefault();
    let otp = $("#txtOpt").val();
    $.ajax({
        type: "POST",
        url: `/forgot-password/confirm-otp`,
        data: {
            otp: otp,
        },
        success: function (response) {
            console.log(response);
            if (response === true) {
                window.location.href = "/forgot-password/confirm-otp/change-pass";
            } else {
                Swal.fire({
                    title: "Thông báo!",
                    text: "Vui lòng nhập lại mã OTP!",
                    icon: "warning",
                    confirmButtonText: "Ok"
                });
            }
        },
    });
});

// Change Password
$("#btnChange").click(function (e) {
    e.preventDefault();
    let passwordNew = $("#passwordNew").val();
    let passwordConfirm = $("#confirm").val();
    console.log(passwordNew);
    console.log(passwordConfirm);
    let check = passwordConfirm != "" && passwordNew != "";
    let checkConfirmPass = passwordConfirm === passwordNew;

    if (check) {
        if (checkConfirmPass) {
            $.ajax({
                type: "POST",
                url: `/forgot-password/confirm-otp/change-pass`,
                data: {
                    newPassword: passwordNew,
                },
                success: function (response) {
                    if (response === "true") {
                        setTimeout(function () {
                            location.href = `/login`;
                        }, 3000);
                    }
                    console.log(response);
                },
            });
        } else {
            Swal.fire({
                title: "Mật khẩu xác nhận chưa chính xác!",
                text: "Vui lòng nhập lại trường xác nhận mật khẩu!",
                icon: "error",
                confirmButtonText: "Ok"
            });
        }
    } else {
        Swal.fire({
            title: "Lưu ý!",
            text: "Vui lòng nhập đầy đủ!",
            icon: "warning",
            confirmButtonText: "Ok"
        });
    }

});