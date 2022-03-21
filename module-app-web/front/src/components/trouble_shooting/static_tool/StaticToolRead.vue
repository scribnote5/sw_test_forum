<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="STATIC 트러블슈팅" :paths="['STATIC', 'STATIC 트러블슈팅 보기']" :title="staticTool.title"/>

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
              <h2 class="mobile-title">{{ staticTool.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ staticTool.createdByUser.department }} {{ staticTool.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ staticTool.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ staticTool.lastModifiedByUser.department }} {{ staticTool.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ staticTool.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ staticTool.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="staticTool.priority" :maxPriority=Number(6)></Priority>
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
              <span v-if="staticTool.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="staticTool.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="staticTool.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="staticTool.toolCategory == 'ETC'">기타</span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ staticTool.toolName }}<br>
              {{ staticTool.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>IDE 정보</th>
            <td>
              {{ staticTool.ideName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ staticTool.compilerName }}<br>
              {{ staticTool.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>도구 유형: </strong>
              <span v-if="staticTool.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="staticTool.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="staticTool.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="staticTool.toolCategory == 'ETC'">기타</span> <br>
              <strong>도구 정보: </strong> {{ staticTool.toolName }}, {{ staticTool.toolNote }} <br>
              <strong>IDE 정보: </strong> {{ staticTool.ideName }} <br>
              <strong>컴파일러: </strong> {{ staticTool.compilerName }}, {{ staticTool.compilerNote }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="staticTool.content"></div>
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

      <Comment path="static-tool-comments" idxName="staticToolIdx" :idx="staticTool.idx" :commentList="staticTool.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-4 my-5">
        <div class="d-flex">
          <router-link :to="'/static-tool/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="staticTool.access">
          <router-link :to="'/static-tool/update/' + idx" class="me-2">
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
import {parseErrorMsg} from "@/utils/validation-util";
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
    let staticTool = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("static-tool");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/static-tools/read/" + idx,
          {},
      )
          .then((response) => {
            staticTool.value = response.data;
            staticTool.value.content = staticTool.value.content;

            // hashTags 설정
            hashTags.value = staticTool.value.hashTags;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = staticTool.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of staticTool.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            staticTool.value.createdDate = dayjs(staticTool.value.createdDate).format("YYYY.MM.DD. HH:mm");
            staticTool.value.lastModifiedDate = dayjs(staticTool.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseErrorMsg(error.response);
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/static-tools/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/static-tool/delete-success");
                router.push("/static-tool/list");
              })
              .catch((error) => {
                parseErrorMsg(error.response);
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
      staticTool, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>