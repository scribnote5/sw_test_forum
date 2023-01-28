<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="지식 저장소" :paths="['나머지 자료', '지식 저장소 보기']" :title="storage.title"/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <td colspan="2">
              <h2 class="mobile-title">{{ storage.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ storage.createdByUser.department }} {{ storage.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ storage.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ storage.lastModifiedByUser.department }} {{ storage.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ storage.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ storage.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="storage.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>

          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>유형</th>
            <td>
              <span v-if="storage.category == 'STUDY'">스터디</span>
              <span v-if="storage.category == 'FINAL_TEST'">실사 후기</span>
              <span v-if="storage.category == 'EDUCATION'">교육 자료</span>
              <span v-if="storage.category == 'CHECK_LIST'">체크 리스트</span>
              <span v-if="storage.category == 'REFERENCE'">참고 자료(가이드라인)</span>
              <span v-if="storage.category == 'ETC'">기타 자료</span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <Priority pageInformation="read" :priority="storage.priority" :maxPriority=Number(6)></Priority>
              <strong>유형: </strong>
              <span v-if="storage.category == 'STUDY'">스터디</span>
              <span v-if="storage.category == 'FINAL_TEST'">실사 후기</span>
              <span v-if="storage.category == 'EDUCATION'">교육 자료</span>
              <span v-if="storage.category == 'CHECK_LIST'">체크 리스트</span>
              <span v-if="storage.category == 'REFERENCE'">참고 자료(가이드라인)</span>
              <span v-if="storage.category == 'ETC'">기타 자료</span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="storage.content"></div>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <FileUpload pageInformation="read" :uploadedAttachedFileList="uploadedAttachedFileList"></FileUpload>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <Comment path="storage-comments" idxName="storageIdx" :idx="storage.idx" :commentList="storage.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/storage/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="storage.access">
          <router-link :to="'/storage/update/' + idx" class="me-2">
            <button class="btn btn-main-blue d-flex align-items-center">수정<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
          </router-link>
          <button @click="deletePost()" class="btn btn-main-red d-flex align-items-center">삭제<img :src="require(`@/assets/images/delete-white.svg`)" class="ms-2"></button>
        </div>
      </div>
    </div>
  </section>
</template>

<style lang="scss">
@import '~@/assets/css/ckeditor.css';
</style>

<script>
// components
import Loading from '@/components/common/Loading.vue'
import Breadcrumb from '@/components/common/Breadcrumb.vue'
import Priority from '@/components/common/Priority.vue'
import FileUpload from '@/components/common/FileUpload.vue'
import Comment from '@/components/common/Comment.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {confirm, fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    FileUpload,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let storage = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("storage");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/read/" + idx,
          {},
      )
          .then((response) => {
            storage.value = response.data;
            storage.value.content = storage.value.content;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = storage.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of storage.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            storage.value.createdDate = dayjs(storage.value.createdDate).format("YYYY.MM.DD. HH:mm");
            storage.value.lastModifiedDate = dayjs(storage.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    /* 삭제 */
    const deletePost = () => {
      confirm.fire({
        title: "삭제하시겠습니까?",
        text: "게시글과 관련된 모든 데이터는 삭제됩니다.",
        confirmButtonText: '네',
        cancelButtonText: '아니오'
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/storage/delete-success");
                router.push("/storage/list");
              })
              .catch((error) => {
                parseApiErrorMsg(error.response);
              })
              .then(() => {
              });
        } else {
          return false;
        }
      })
    }

    return {
      // variable
      idx, storage, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>