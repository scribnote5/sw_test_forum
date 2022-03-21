/* URI 생성 */
const createUri = (uri, params) => {
    Object.keys(params).forEach(function (key, index) {
        // +, &, % 와 같은 특수 문자가 get 방식으로 전달 될 때, encodeURIComponent를 사용하지 않으면 특수 문자를 제외하여 전달
        uri += (index === 0 ? "?" : "&") + key + "=" + encodeURIComponent(params[key]);
    });

    return uri;
}

/* URI 파라미터 반환 */
const getUriParams = () => {
    var params = {};
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (str, key, value) {
        params[key] = value;
    });
    return params;
}

export {createUri, getUriParams};