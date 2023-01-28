import {toast} from "@/assets/plugins/sweetalert2/sweetalert2"
import {isEmpty} from "@/utils/empty-util.js"
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import axios from "axios";
import router from "@/router";

/* 로그인 에러 메시지 검사 */
const parseLoginErrorMsg = (msg) => {
    let alertMsg;

    console.error("backend error!")
    console.error(msg)

    if (!isEmpty(msg.data.errors[0])) {
        console.error("msg.data.errors 처리")
        alertMsg = msg.data.message + "<br>" + msg.data.errors[0].reason;
    } else if (!isEmpty(msg.data.message)) {
        console.error("msg.data.message 처리")
        alertMsg = msg.data.message;
    } else {
        alertMsg = "API 서버에 에러가 발생했습니다.\n"
            + "(NetworkError: Failed to execute 'send' on 'XMLHttpRequest'.)";
    }

    toast.fire({
        icon: "error",
        title: "에러가 발생하였습니다.",
        html: alertMsg
    })
}

/* 에러 메시지 검사 */
const parseApiErrorMsg = (msg) => {
    let alertMsg;

    console.error("backend error!")
    console.error(msg)

    if (!isEmpty(msg)) {
        // 로그인 페이지 에러 처리
        if (!isEmpty(msg.data)) {
            if (!isEmpty(msg.data.errors)) {
                console.error("msg.data.errors 처리")
                alertMsg = msg.data.message + "<br>" + msg.data.errors[0].reason;
            } else if (!isEmpty(msg.data.message)) {
                console.error("msg.data.message 처리")
                alertMsg = msg.data.message;
            } else {
                console.error("else msg.data 처리")
                alertMsg = msg.data;
            }
        } else if (!isEmpty(msg.message)) {
            console.error("msg.message 처리")
            alertMsg = msg.message;
        } else if (!isEmpty(msg.errors)) {
            console.error("msg.errors 처리")
            alertMsg = msg.message;
        } else {
            console.error("나머지 에러 처리")
            alertMsg = "서버와의 연결이 끊어졌습니다.<br>담당자에게 문의 해주세요.";
            // alertMsg = msg.message + "\n" + msg.errors[0].field + ", " + msg.errors[0].reason;
        }
    } else {
        alertMsg = "API 서버에 에러가 발생했습니다.\n"
            + "(NetworkError: Failed to execute 'send' on 'XMLHttpRequest'.)";
    }

    alertMsg += "<br>잠시후 이전 페이지로 돌아갑니다."

    toast.fire({
        icon: "error",
        title: "에러가 발생하였습니다.",
        html: alertMsg
    })

    // 10초 이후, 이전 페이지로 이동
    setTimeout(function () {
        router.go(-1);
    }, 10000);
}

/* 배열 요소가 empty인 경우를 제외하여 배열 길이를 계산 */
const deleteArrayIndexIsEmpty = (fileArray) => {
    fileArray = fileArray.filter((item) => {
        return item !== null && item !== undefined && item !== '';
    });

    return fileArray;
}

/* 바이트 수 반환 */
const getSize = (str) => {
    return str
        .split('')
        .map(s => s.charCodeAt(0))
        .reduce((prev, c) => (prev + ((c === 10) ? 2 : ((c >> 7) ? 2 : 1))), 0); // 계산식에 관한 설명은 위 블로그에 있습니다.
}

/* 공백 validation */
function validateWhiteSpace(value) {
    if (isEmpty(value) || value.trim().length === 0) {
        return false;
    } else {
        return true;
    }
}

/* 길이 validation */
const validateLength = (name, value, length) => {
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");

    if (value.length > length) {
        result = "길이는 " + length + " 보다 작아야 합니다. \n"
            + "(현재 입력된 길이: " + value.length + ")";
        document.getElementsByName(name)[0].focus();
    } else {
        result = "";
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}

/* 길이 및 공백 validation */
const validateLengthAndIsEmpty = (name, value, length) => {
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");

    if (!validateWhiteSpace(value)) {
        result = "공란이 될 수 없습니다.";
        document.getElementsByName(name)[0].focus();
    } else if (value.length > length) {
        result = "길이는 " + length + " 보다 작아야 합니다. \n"
            + "(현재 입력된 길이: " + value.length + ")";
        document.getElementsByName(name)[0].focus();
    } else {
        result = "";
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}

/* 에디터 크기 validation */
const validateSizeByEditor = (name, value) => {
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");
    let size = getSize(value);

    // 16777215Byres(16MB)
    if (size > 16777215) {
        result = "크기는 16777215Bytes(16MB) 보다 작아야 합니다.\n" +
            "(현재 입력된 Bytes: " + size + ")";
        document.getElementsByName(name)[0].focus();
    } else {
        result = "";
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}

/* 에디터 크기 validation */
const validateSizeAndIsEmptyByEditor = (name, value) => {
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");
    let size = getSize(value);

    if (!validateWhiteSpace(value)) {
        result = "공란이 될 수 없습니다.";
        document.getElementsByName(name)[0].focus();
    }
    // 16777215Byres(16MB)
    else if (size > 16777215) {
        result = "크기는 16777215Bytes(16MB) 보다 작아야 합니다.\n" +
            "(현재 입력된 Bytes: " + size + ")";
        document.getElementsByName(name)[0].focus();
    } else {
        result = "";
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}

/* Editor <br> instead of <p> */
// https://stackoverflow.com/questions/54394954/in-ckeditor5-how-can-i-change-entermode-br-instead-of-p
const onEditorReady = (editor) => {
    // editor.editing.view.document.on('enter', (evt, data) => {
    //     if (data.isSoft) {
    //         editor.execute('enter');
    //     } else {
    //         editor.execute('shiftEnter');
    //     }
    //
    //     data.preventDefault();
    //     evt.stop();
    //     editor.editing.view.scrollToTheSelection();
    // }, {priority: 'high'});
}

/* Editor validation */
const validateEditor = (event, editor) => {
    let size = getSize(editor.getData());

    // 16777215Bytes(16MB)
    if (size > 16777215) {
        toast.fire({
            icon: "error",
            html: "크기는 16777215Bytes(16MB) 보다 작아야 합니다.<br>" +
                "(현재 입력된 Bytes: " + size + ")"
        });
        editor.editing.view.focus();
    }
}

/* username validation */
const validateUsername = async (name, value) => {
    let regexp = /^[a-z0-9_.]{4,20}$/g;
    let msg;
    let result = false;
    let errorMessage = document.getElementById(name + "ErrorMessage");

    if (!regexp.test(value)) {
        msg = "아이디는 소문자, '.', '_' 그리고 숫자만 가능하며, 4글자 이상 20글자 이하만 가능 합니다.\n" +
            "(현재 입력된 글자 수: " + value.length + ")";
        errorMessage.style.color = "red";
        document.getElementsByName(name)[0].focus();
    } else {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/validation/username/" + value,
            {},
        )
            .then((response) => {
                if (response.data) {
                    msg = "아이디가 이미 존재합니다.";
                    errorMessage.style.color = "red";
                    document.getElementsByName(name)[0].focus();
                } else {
                    msg = "사용 가능한 아이디 입니다.";
                    result = true;
                    errorMessage.style.color = "blue";
                }
            })
            .catch((error) => {
                parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
    }

    errorMessage.innerText = msg;

    return result;
}

/* 비밀번호 validation */
const validatePassword = (isUpdate, passwordName, checkPasswordName, password, checkPassword) => {
    let msg;
    let result = false;
    let errorMessage = document.getElementById(passwordName + "ErrorMessage");

    // 수정 페이지에서, password를 변경하지 않고 수정 버튼을 클릭하는 경우
    if (isUpdate) {
        result = true;
    } else if (password.length >= 6 && password.length <= 16) {
        if (password !== "" && checkPassword !== "") {
            if (password === checkPassword) {
                msg = "비밀번호가 같습니다.";
                result = true;
                errorMessage.style.color = "blue";
            } else {
                msg = "비밀번호가 다릅니다.";
                document.getElementsByName(passwordName)[0].focus();
                errorMessage.style.color = "red";
            }
        } else if (password !== "" && checkPassword === "") {
            msg = "비밀번호를 확인 해주세요.";
            document.getElementsByName(checkPasswordName)[0].focus();
            errorMessage.style.color = "red";
        } else {
            msg = "비밀번호를 입력 해주세요.";
            document.getElementsByName(passwordName)[0].focus();
            errorMessage.style.color = "red";
        }
    } else {
        msg = "비밀번호 길이는 6글자 보다 크고 16글자 보다 작아야 합니다.";
        document.getElementsByName(passwordName)[0].focus();
        errorMessage.style.color = "red";
    }

    errorMessage.innerText = msg;

    return result;
}


/* 연락처 validation */
const validateContact = (name, value, length) => {
    let regExp = /(01[016789])-([1-9]{1}[0-9]{2,3})-([0-9]{4})$/;
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");

    if (!isEmpty(value) && !regExp.test(value)) {
        result = "연락처 포맷이 틀립니다.";
        document.getElementsByName(name)[0].focus();
    } else if (value.length > length) {
        result = "길이는 " + length + " 보다 작아야 합니다. \n"
            + "(현재 입력된 길이: " + value.length + ")";
        document.getElementsByName(name)[0].focus();
    } else {
        result = "";
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}

/* 양수 validation */
const validatePositiveNumber = (name, value) => {
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");


    if (isNaN(value) || value <= 0) {
        result = "숫자 또는 양수가 아닙니다.";
        document.getElementsByName(name)[0].focus();

    } else {
        result = "";
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}

/* 이메일 validation */
const validateEmail = (name, value, length) => {
    let regExp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    let result;
    let errorMessage = document.getElementById(name + "ErrorMessage");

    if (!isEmpty(value) && !regExp.test(value)) {
        result = "이메일 포맷이 틀립니다.";
        document.getElementsByName(name)[0].focus();
    } else if (value.length > length) {
        result = "길이는 " + length + " 보다 작아야 합니다. \n"
            + "(현재 입력된 길이: " + value.length + ")";
        document.getElementsByName(name)[0].focus();
    } else {
        result = ""
    }

    errorMessage.innerText = result;

    return isEmpty(result);
}


/* 파일 validation - 유효한 파일 확장자 */
const validateFile = (file, totalFileSize) => {
    // 제외 파일 확장자
    let excludeFileNames = [".exe", ".jar", ".js", ".swf", ".swf", ".bin", ".wmf", ".class", ".chm", ".pgm", ".pcx", ".hlp", ".acc", ".css", ".sh", ".com", "bat", "cmd", ".scf", ".lnk", ".inf", ".reg"];
    // 파일 이름
    let fileName = file.name;
    // 파일 확장자명(대문자를 소문자로 변경)
    let extensionName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    // 첨부 파일 크기
    let fileSize = file.size;
    // 업로드 가능한 파일 크기: 200MB
    let maxSize = 50 * 1024 * 1024;

    // 파일이 비어있는지 검사
    if (isEmpty(fileName)) {
        toast.fire({
            icon: "error",
            html: "파일을 업로드 하세요.",
        })

        return false;
    }

    // 확장자명 검사
    for (const excludeFileName of excludeFileNames) {
        if (extensionName === excludeFileName) {
            toast.fire({
                icon: "error",
                html: "[" + extensionName + "] 확장자는 업로드 할 수 없습니다.",
            })

            return false;
        }
    }

    // 파일 크기 검사
    if (fileSize > maxSize) {
        toast.fire({
            icon: "error",
            html: "첨부 파일은 200MB 크기 내로 업로드 가능합니다.",
        })

        return false;
    }

    // 모든 파일 크기 검사
    if (fileSize + totalFileSize > maxSize) {
        toast.fire({
            icon: "error",
            html: "모든 첨부 파일은 200MB 크기 내로 업로드 가능합니다.",
        })

        return false;
    }

    return true;
}

/* 파일 validation - 유효한 파일 확장자 */
const validateImageFile = (file, totalFileSize) => {
    // 포함 파일 확장자
    let includeFileNames = [".webp", ".jpg", ".jpeg", ".png", ".tif", ".tiff", ".svg"];
    // 파일 이름
    let fileName = file.name;
    // 파일 확장자명(대문자를 소문자로 변경)
    let extensionName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    // 첨부 파일 크기
    let fileSize = file.size;
    // 업로드 가능한 파일 크기: 10MB
    let maxSize = 10 * 1024 * 1024;
    // 필수 확장자명 사용 여부 판단
    let result = true;

    // 파일이 비어있는지 검사
    if (isEmpty(fileName)) {
        toast.fire({
            icon: "error",
            html: "파일을 업로드 하세요.",
        })

        return false;
    }

    // 확장자명 검사
    for (const includeFileName of includeFileNames) {
        if (extensionName === includeFileName) {
            result = false;
            break;
        }
    }

    if (result) {
        toast.fire({
            icon: "error",
            html: "[" + extensionName + "] 확장자는 업로드 할 수 없습니다.",
        })

        return false;
    }

    // 파일 크기 검사
    if (fileSize > maxSize) {
        toast.fire({
            icon: "error",
            html: "첨부 파일은 10MB 크기 내로 업로드 가능합니다.",
        })

        return false;
    }

    // 모든 파일 크기 검사
    if (fileSize + totalFileSize > maxSize) {
        toast.fire({
            icon: "error",
            html: "모든 첨부 파일은 200MB 크기 내로 업로드 가능합니다.",
        })

        return false;
    }

    return true;
}

/* 파일 validation - 파일이 하나만 존재 */
const validateByLessOneFileExist = (uploadedFileLength, uploadFileLength) => {
    let insertFileArrayLength = deleteArrayIndexIsEmpty(fileUpload.insertFileArray).length;
    let deleteFileArrayLength = deleteArrayIndexIsEmpty(fileUpload.deleteFileArray).length;

    // console.log("uploadedFileLength: " + uploadedFileLength);
    // console.log("uploadFileLength: " + uploadFileLength)
    // console.log("insertFileArrayLength: " + insertFileArrayLength)
    // console.log("deleteFileArrayLength: " + deleteFileArrayLength)

    if (uploadedFileLength + uploadFileLength + insertFileArrayLength - fileUpload.deleteFileArray.length >= 2) {
        toast.fire({
            icon: "error",
            html: "첨부 파일은 1개만 업로드 가능합니다.",
        })

        // file 초기화
        document.getElementsByName("file")[0].value = '';

        return false;
    } else {
        return true;
    }
}

export {
    parseLoginErrorMsg,
    parseApiErrorMsg,
    deleteArrayIndexIsEmpty,
    validateLength,
    validateLengthAndIsEmpty,
    validateSizeByEditor,
    validateSizeAndIsEmptyByEditor,
    onEditorReady,
    validateEditor,
    validateUsername,
    validatePassword,
    validateEmail,
    validateContact,
    validatePositiveNumber,
    validateFile,
    validateImageFile,
    validateByLessOneFileExist,
};
