<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="사용 방법" :paths="['도구', '사용 방법 보기']" :title="toolUsageMethod.title"/>

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
              <h2 class="mobile-title">{{ toolUsageMethod.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ toolUsageMethod.createdByUser.department }} {{ toolUsageMethod.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ toolUsageMethod.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ toolUsageMethod.lastModifiedByUser.department }} {{ toolUsageMethod.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ toolUsageMethod.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ toolUsageMethod.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="toolUsageMethod.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr>
            <th>해시태그</th>
            <td>
              <HashTags pageInformation="read" :hash-tags="hashTags"></HashTags>
            </td>
          </tr>
          <tr>
            <th>도구 유형</th>
            <td>
              <span v-if="toolUsageMethod.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="toolUsageMethod.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="toolUsageMethod.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="toolUsageMethod.toolCategory == 'ETC'">기타</span>
            </td>
          </tr>
          <tr>
            <th>대상 도구</th>
            <td>
              <span v-if="toolUsageMethod.targetToolName == 'STATIC'">STATIC</span>
              <span v-if="toolUsageMethod.targetToolName == 'COVER'">COVER</span>
              <span v-if="toolUsageMethod.targetToolName == 'CONTROLLER_TESTER'">Controller Tester</span>
              <span v-if="toolUsageMethod.targetToolName == 'VPES'">VPES</span>
              <span v-if="toolUsageMethod.targetToolName == 'TOOLCHAIN'">툴체인</span>
            </td>
          </tr>


          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>도구 유형: </strong>
              <span v-if="toolUsageMethod.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="toolUsageMethod.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="toolUsageMethod.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="toolUsageMethod.toolCategory == 'ETC'">기타</span> <br>
              <strong>대상 도구: </strong>
              <span v-if="toolUsageMethod.targetToolName == 'STATIC'">STATIC</span>
              <span v-if="toolUsageMethod.targetToolName == 'COVER'">COVER</span>
              <span v-if="toolUsageMethod.targetToolName == 'CONTROLLER_TESTER'">Controller Tester</span>
              <span v-if="toolUsageMethod.targetToolName == 'VPES'">VPES</span>
              <span v-if="toolUsageMethod.targetToolName == 'TOOLCHAIN'">툴체인</span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="toolUsageMethod.content"></div>
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

      <Comment path="tool-usage-method-comments" idxName="toolUsageMethodIdx" :idx="toolUsageMethod.idx" :commentList="toolUsageMethod.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/tool-usage-method/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="toolUsageMethod.access">
          <router-link :to="'/tool-usage-method/update/' + idx" class="me-2">
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
import HashTags from '@/components/common/HashTags.vue'
import FileUpload from '@/components/common/FileUpload.vue'
import Comment from '@/components/common/Comment.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import {confirm, fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    HashTags,
    FileUpload,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let toolUsageMethod = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("tool-usage-method");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-usage-methods/read/" + idx,
          {},
      )
          .then((response) => {
            toolUsageMethod.value = response.data;
            toolUsageMethod.value.content = toolUsageMethod.value.content;

            // hashTags 설정
            hashTags.value = toolUsageMethod.value.hashTags;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = toolUsageMethod.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of toolUsageMethod.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            toolUsageMethod.value.createdDate = dayjs(toolUsageMethod.value.createdDate).format("YYYY.MM.DD. HH:mm");
            toolUsageMethod.value.lastModifiedDate = dayjs(toolUsageMethod.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-usage-methods/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/tool-usage-method/delete-success");
                router.push("/tool-usage-method/list");
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
      toolUsageMethod, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>