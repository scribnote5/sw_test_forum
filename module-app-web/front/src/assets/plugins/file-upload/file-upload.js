let fileUpload = {
    oriTotalFileSize: 0,
    totalFileSize: 0,
    insertFileArray: [],
    deleteFileArray: [],
    uploadFileId: 0,
    uploadedFileId: 0,

    setOriTotalFileSize(oriTotalFileSize) {
        this.oriTotalFileSize = oriTotalFileSize;
    },
    getOriTotalFileSize() {
        return this.oriTotalFileSize;
    },

    setTotalFileSize(totalFileSize) {
        this.totalFileSize = totalFileSize;
    },
    getTotalFileSize() {
        return this.totalFileSize;
    },

    setUploadFileId(uploadFileId) {
        this.uploadFileId = uploadFileId;
    },
    getUploadFileId() {
        return this.uploadFileId;
    },

    setUploadedFileId(uploadedFileId) {
        this.uploadedFileId = uploadedFileId;
    },
    getUploadedFileId() {
        return this.uploadedFileId;
    },

    initFileUpload() {
        this.oriTotalFileSize = 0;
        this.totalFileSize = 0;
        this.insertFileArray.splice(0, this.insertFileArray.length);
        this.deleteFileArray.splice(0, this.deleteFileArray.length);
        this.uploadFileId = 0;
        this.uploadedFileId = 0;
    }
}

export {fileUpload};