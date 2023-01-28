/* hash tag 값 설정 */
const updateHashTagsValue = () => {
    let hashTagContentArray = document.querySelectorAll('*[id^="hashTagContent"]');
    let hashTagsValue = "";

    for (const hashTagContent of hashTagContentArray) {
        hashTagsValue += hashTagContent.innerText;
    }

    return hashTagsValue.substr(1);
}

export { updateHashTagsValue };