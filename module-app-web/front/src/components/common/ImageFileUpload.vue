<template>
  <div>
    <div v-if="pageInformation === 'write' || pageInformation === 'update'" class="my-2">
      <div class="d-flex justify-content-between">
        <strong>{{ itemName }}</strong>
        <input @change="inputFileUploadEvent" type="file" name="file" id="file" class="form-control w100-100px">
      </div>
      <div class="d-flex justify-content-between mt-3">
        <div @drop="fileDropEvent($event)" id="fileDrop" class="d-flex justify-content-center image-file-drop">
          <div class="d-flex align-items-center px-1">
            <strong>드래그 앤 드랍으로 업로드 할 수 있습니다.</strong>
          </div>
        </div>
        <div>
          <img v-if="pageInformation === 'write' || uploadedAttachedFileList.totalFileSize === 0" id="imgPreview" class="img-preview img-thumbnail" :src="require(`@/assets/images/avatar.png`)"/>
          <img v-else id="imgPreview" class="img-preview img-thumbnail float-end" :src="uploadedAttachedFileList[0].uri"/>
        </div>
      </div>
    </div>

    <div v-if="pageInformation === 'read'" class="my-2">
      <img v-if="uploadedAttachedFileList.totalFileSize === 0" class="img-preview img-thumbnail float-end" :src="require(`@/assets/images/avatar.png`)"/>
      <img v-else class="img-preview img-thumbnail float-end" :src="uploadedAttachedFileList[0].uri"/>
    </div>

    <div v-if="pageInformation === 'write' || pageInformation === 'update'" class="my-2">
      <strong>파일 크기: </strong> &nbsp;
      <span id="totalFileSize">
        <span v-if="pageInformation === 'update'">{{ uploadedAttachedFileList.convertedTotalFileSize }}</span>
        <span v-else>0MB</span>
      </span>, Up to 10MB
    </div>

    <div v-if="pageInformation === 'write' || pageInformation === 'update'" class="my-2">
      <strong>업로드 하는 첨부 파일: </strong> &nbsp;
      <div id="attachedFileList" class="mt-1"></div>
    </div>

    <div v-if="pageInformation === 'update'" class="my-2">
      <strong>업로드 된 첨부 파일: </strong> &nbsp;
      <div id="uploadedAttachedFileList" class="mt-1">
        <div v-for="(uploadedAttachedFile, i) in uploadedAttachedFileList" :key="i">
          <div :id="'uploadedFileId' + i" class="d-flex download-attached-file">
            <span @click="downloadFile(uploadedAttachedFile.savedFileName)" class="d-flex align-items-center">
              {{ uploadedAttachedFile.fileName }}, 파일 크기: {{ uploadedAttachedFile.convertedFileSize }}
            </span>
            <img v-if="pageInformation === 'update'" :src="require(`@/assets/images/x-circle-main-black.svg`)" @click="deleteFile(i, uploadedAttachedFile.idx, uploadedAttachedFile.fileSize)" class="ms-2"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
strong {
  font-color: $main-black;
  font-size: $large-font-size;
}

.img-preview {
  max-width: 200px !important;
  max-height: 150px;
}

.img-thumbnail:hover {
  box-shadow: 0 0 2px 1px $main-red;
}

.image-file-drop {
  height: 150px;
  border: 1px dashed grey;
  border-radius: 10px;
  width: calc(100% - 175px) !important;
  overflow: hidden;

  strong {
    color: $the-darkest-main-grey;
    font-size: $larger-font-size;
  }
}

.download-attached-file {
  cursor: pointer;
}
</style>

<script>
import {onBeforeMount, onBeforeUnmount, ref} from "vue";
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import {validateByLessOneFileExist, validateImageFile} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";

export default {
  name: "ImageFileUpload",
  props: {
    itemName: String,
    pageInformation: String,
    uploadedAttachedFileList: Object
  }, setup(props) {
    let uploadedFileId = ref(0);

    onBeforeMount(async () => {
      fileUpload.initFileUpload();

      if (props.pageInformation === 'write' || props.pageInformation === 'update') {
        document.addEventListener("dragenter", dragenter);
        document.addEventListener("dragover", dragover);
        document.addEventListener("dragleave", dragleave);
      }
    });

    onBeforeUnmount(async () => {
      if (props.pageInformation === 'write' || props.pageInformation === 'update') {
        document.removeEventListener("dragenter", dragenter);
        document.removeEventListener("dragover", dragover);
        document.removeEventListener("dragleave", dragleave);
      }
    });

    /* dragenter event */
    const dragenter = (event) => {
      event.preventDefault();
    }

    /* dragover event */
    const dragover = (event) => {
      event.stopPropagation();
      event.preventDefault();

      document.getElementById("fileDrop").style.opacity = 0.4;
    }

    /* dragleave event */
    const dragleave = (event) => {
      event.stopPropagation();
      event.preventDefault();

      document.getElementById("fileDrop").style.opacity = 1;
    }

    const readImageFile = (input) => {
      const reader = new FileReader();

      reader.onload = e => {
        const previewImage = document.getElementById("imgPreview")
        previewImage.src = e.target.result
      }

      reader.readAsDataURL(input);
    }

    /* input file event */
    const inputFileUploadEvent = () => {
      let files = document.getElementsByName("file")[0].files;

      if (!validateByLessOneFileExist(isEmpty(props.uploadedAttachedFileList) ? 0 : props.uploadedAttachedFileList.length, files.length)) {
        return false;
      }

      for (const file of files) {
        if (validateImageFile(file, fileUpload.getTotalFileSize())) {
          const tempUploadFileId = fileUpload.getUploadFileId();
          fileUpload.setTotalFileSize(fileUpload.getTotalFileSize() + file.size);
          const tag = '<div id="uploadFileId' + tempUploadFileId + '" + class="d-flex">'
              + '<span class="d-flex align-items-center">'
              + file.name + ",&nbsp; 파일 크기: " + convertFileSize(file.size)
              + '</span>'
              + '<img id="cancelFileIcon' + tempUploadFileId + '" src="/x-circle-main-black.svg" class="ms-2">'
              + '</div>';

          document.getElementById("attachedFileList").insertAdjacentHTML("beforeend", tag);
          document.getElementById("cancelFileIcon" + tempUploadFileId).onclick = function () {
            cancelFileEvent(tempUploadFileId);
          }

          document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
          fileUpload.insertFileArray.push(file);
          fileUpload.setUploadFileId(fileUpload.getUploadFileId() + 1);

          readImageFile(file);
        } else {
          break;
        }
      }

      // file 초기화
      document.getElementsByName("file")[0].value = '';
    }

    /* Drop event */
    const fileDropEvent = (event) => {
      event.preventDefault(); // 기본 효과를 막음
      document.getElementById("fileDrop").style.opacity = 1;

      // 드래그된 파일의 정보
      let files = event.dataTransfer.files;

      if (!validateByLessOneFileExist(isEmpty(props.uploadedAttachedFileList) ? 0 : props.uploadedAttachedFileList.length, files.length)) {
        return false;
      }

      for (const file of files) {
        if (validateImageFile(file, fileUpload.getTotalFileSize())) {
          const tempUploadFileId = fileUpload.getUploadFileId();
          fileUpload.setTotalFileSize(fileUpload.getTotalFileSize() + file.size);
          const tag = '<div id="uploadFileId' + tempUploadFileId + '" + class="d-flex">'
              + '<span class="d-flex align-items-center">'
              + file.name + ",&nbsp; 파일 크기: " + convertFileSize(file.size)
              + '</span>'
              + '<img id="cancelFileIcon' + tempUploadFileId + '" src="/x-circle-main-black.svg" class="ms-2">'
              + '</div>';

          document.getElementById("attachedFileList").insertAdjacentHTML("beforeend", tag);
          document.getElementById("cancelFileIcon" + tempUploadFileId).onclick = function () {
            cancelFileEvent(tempUploadFileId);
          }

          document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
          fileUpload.insertFileArray.push(file);
          fileUpload.setUploadFileId(fileUpload.getUploadFileId() + 1);

          readImageFile(file);
        } else {
          break;
        }
      }

      // file 초기화
      document.getElementsByName("file")[0].value = '';
    }

    /* 업로드 하는 파일을 취소하는 경우 */
    const cancelFileEvent = (uploadFileId) => {
      document.getElementById("uploadFileId" + uploadFileId).remove();
      fileUpload.setTotalFileSize(fileUpload.getTotalFileSize() - fileUpload.insertFileArray[uploadFileId].size);
      fileUpload.insertFileArray[uploadFileId] = null;
      document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
      document.getElementById("imgPreview").src = "/avatar.png";
    }

    /* 업로드 된 파일 초기화, 사용하지 않음 */
    /* const uploadedFileInit = () => {
      for (let attachedFile of props.uploadedAttachedFileList) {
        const tempUploadedFileId = fileUpload.getUploadedFileId();
        const uri = encodeURI(process.env.VUE_APP_MODULE_APP_API_URL + '/api/attached-files/download/' + attachedFile.savedFileName);
        fileUpload.setTotalFileSize(fileUpload.getTotalFileSize() + attachedFile.fileSize);
        const tag = '<div id="uploadedFileId' + fileUpload.getUploadedFileId() + '">'
            + '<span onclick="location.href=\'' + uri + '\'">'
            + attachedFile.fileName + ",&nbsp; File Size: " + convertFileSize(attachedFile.fileSize) + "&nbsp;"
            + '</span>' + "&nbsp;"
            + '<img id="deleteFileIcon' + tempUploadedFileId + '" src="/x-circle-main-black.svg">'
            + '</div>';

        document.getElementById("uploadedAttachedFileList").insertAdjacentHTML("afterend", tag);
        document.getElementById("deleteFileIcon" + tempUploadedFileId).onclick = function () {
          deleteFile(tempUploadedFileId, attachedFile.idx, attachedFile.fileSize);
        }

        document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
        fileUpload.setUploadedFileId(fileUpload.getUploadedFileId() + 1);
      }

      fileUpload.setOriTotalFileSize(fileUpload.getTotalFileSize());
    } */

    /* 기존 업로드한 파일을 삭제하는 경우 */
    const deleteFile = (uploadedFileId, idx, fileSize) => {
      document.getElementById("uploadedFileId" + uploadedFileId).remove();
      fileUpload.setTotalFileSize(fileUpload.getTotalFileSize() - fileSize);
      fileUpload.deleteFileArray.push(idx);
      document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
      document.getElementById("imgPreview").src = "/avatar.png";
    }

    /* 업로드한 파일을 다운로드 하는 경우 */
    const downloadFile = (savedFileName) => {
      const uri = encodeURI(process.env.VUE_APP_MODULE_APP_API_URL + '/api/attached-files/download/' + savedFileName);
      location.href = uri;
    }

    return {
      // variable
      uploadedFileId,

      // function
      readImageFile, inputFileUploadEvent, fileDropEvent, deleteFile, downloadFile,
      isEmpty
    }
  }
}
</script>