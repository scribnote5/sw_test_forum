/* 파일 크기 변환 */
const convertFileSize = (fileSize) => {
    let retFormat = "0";
    let s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
    let e = Math.floor(Math.log(fileSize) / Math.log(1024));

    if (fileSize != 0) {
        retFormat = (fileSize / Math.pow(1024, e)).toFixed(2) + "" + s[e];
    } else {
        retFormat = fileSize + " " + s[0];
    }

    return retFormat;
};

const replaceBrToNewline = (str) => {
    return str.replace(/<br>/g, "\n");
}

/* unescapeHtml */
const unescapeHtml = (str) =>
    str.replace(
        /&amp;|&lt;|&gt;|&#39;|&quot;/g,
        tag =>
            ({
                '&amp;': '&',
                '&lt;': '<',
                '&gt;': '>',
                '&#39;': "'",
                '&quot;': '"'
            }[tag] || tag)
    );

export { convertFileSize, replaceBrToNewline, unescapeHtml };