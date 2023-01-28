/* ckeditor custom image upload */
function getCookie(cookieName){
    var cookieValue=null;
    if(document.cookie){
        var array=document.cookie.split((escape(cookieName)+'='));
        if(array.length >= 2){
            var arraySub=array[1].split(';');
            cookieValue=unescape(arraySub[0]);
        }
    }
    return cookieValue;
}

class CustomUploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }

    upload() {
        return this.loader.file.then(file => new Promise(((resolve, reject) => {
            this._initRequest();
            this._initListeners(resolve, reject, file);
            this._sendRequest(file);
        })))
    }

    _initRequest() {
        const xhr = this.xhr = new XMLHttpRequest();
        xhr.open("POST", process.env.VUE_APP_MODULE_APP_API_URL + "/api/attached-files/upload", true); // 이미지 파일을 업로드하는 파일 주소

        // csrf 토큰 설정
        xhr.setRequestHeader("X-XSRF-TOKEN", getCookie("XSRF-TOKEN"));
        // xhr.setRequestHeader("Access-Control-Allow-Origin", '*');
        xhr.withCredentials = true;
        xhr.responseType = "json";
    }

    _initListeners(resolve, reject, file) {
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = "파일을 업로드 할 수 없습니다. 관리자에게 문의하세요."

        xhr.addEventListener('error', () => {
            reject(genericErrorText)
        })
        xhr.addEventListener("abort", () => reject())
        xhr.addEventListener("load", () => {
            const response = xhr.response
            if (!response || response.error) {
                return reject(response && response.error ? response.error.message : genericErrorText);
            }

            resolve({
                default: response.url
            })
        })
    }

    _sendRequest(file) {
        const data = new FormData()
        data.append("files", file)
        this.xhr.send(data)
    }
}

function CustomUploadAdapterPlugin(editor) {
    editor.plugins.get("FileRepository").createUploadAdapter = (loader) => {
        return new CustomUploadAdapter(loader)
    }
}

export {CustomUploadAdapterPlugin}
