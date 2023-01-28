import swal from 'sweetalert2'

const toast = swal.mixin({
    toast: true,
    width: 600,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3500,

    didOpen: (toast) => {
        toast.addEventListener('mouseenter', swal.stopTimer)
        toast.addEventListener('mouseleave', swal.resumeTimer)
    }
})

const confirm = swal.mixin({
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: 'rgb(12, 84, 149)',
    cancelButtonColor: 'rgb(158, 37, 41)',
})

const error = swal.mixin({
    icon: "error",
    title: "에러가 발생하였습니다.",
    confirmButtonColor: 'rgb(158, 37, 41)',
})

const warning = swal.mixin({
    icon: "warning",
    title: "확인이 필요합니다.",
    confirmButtonColor: 'rgb(250, 212, 179)',
})

const fireSuccessToast = (index) => {
    /* sweetalert2 */
    switch (localStorage.getItem("result")) {
        case "/" + index + "/write-success" :
            toast.fire({
                icon: 'success',
                title: '작성되었습니다.',
            });
            break;
        case "/" + index + "/update-success":
            toast.fire({
                icon: 'success',
                title: '수정되었습니다.',
            });
            break;
        case "/" + index + "/delete-success":
            toast.fire({
                icon: 'success',
                title: '삭제되었습니다.',
            });
            break;
        case "/join-success":
            toast.fire({
                icon: 'success',
                title: '회원 가입에 성공하였습니다.\n' + '* 관리자가 승인해야 회원 가입이 완료 됩니다.',
            });
            break;
    }

    localStorage.removeItem("result");
}

export {
    toast, confirm, error, warning, fireSuccessToast
};