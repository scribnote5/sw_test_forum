<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="환경 설정" :paths="['도구', '환경 설정 보기']" :title="toolConfiguration.title"/>

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
              <h2 class="mobile-title">{{ toolConfiguration.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ toolConfiguration.createdByUser.department }} {{ toolConfiguration.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ toolConfiguration.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ toolConfiguration.lastModifiedByUser.department }} {{ toolConfiguration.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ toolConfiguration.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ toolConfiguration.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="toolConfiguration.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr>
            <th>해시태그</th>
            <td>
              <HashTags pageInformation="read" :hash-tags="hashTags"></HashTags>

            </td>
          </tr>
          <tr>
            <th>대상 도구</th>
            <td>
              <span v-if="toolConfiguration.targetToolName == 'STATIC'">STATIC</span>
              <span v-if="toolConfiguration.targetToolName == 'COVER'">COVER</span>
              <span v-if="toolConfiguration.targetToolName == 'CONTROLLER_TESTER'">Controller Tester</span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ toolConfiguration.toolName }}<br>
              {{ toolConfiguration.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>개발 환경</th>
            <td>
              {{ toolConfiguration.developmentEnvironmentName }}<br>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>개발 언어</th>
            <td>
              {{ toolConfiguration.programmingLanguage }}<br>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>IDE 정보</th>
            <td>
              {{ toolConfiguration.ideName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ toolConfiguration.compilerName }}<br>
              {{ toolConfiguration.compilerNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>시험 환경</th>
            <td>
              <span v-if="toolConfiguration.operationEnvironment == 'HOST'">호스트 환경</span>
              <span v-if="toolConfiguration.operationEnvironment == 'TARGET'">타겟 환경</span>
            </td>
          </tr>
          <tr v-if="toolConfiguration.operationEnvironment === 'TARGET'" class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>타겟 환경</th>
            <td>
              {{ toolConfiguration.targetEnvironmentName }}
            </td>
          </tr>
          <tr v-if="toolConfiguration.operationEnvironment === 'TARGET'" class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>타겟 보드</th>
            <td>
              보드명: {{ toolConfiguration.boardName }}<br>
              아키텍처: {{ toolConfiguration.architecture }}
            </td>
          </tr>
          <tr v-if="toolConfiguration.operationEnvironment === 'TARGET'" class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>타겟 통신</th>
            <td>
              인터페이스: {{ toolConfiguration.interfaceMethod }}<br>
              디버거명: {{ toolConfiguration.debuggerName }}
            </td>
          </tr>
          <tr v-if="toolConfiguration.operationEnvironment === 'TARGET'" class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>개발 소프트웨어 정보</th>
            <td>
              실행 파일 크기: {{ toolConfiguration.executableSize }}<br>
              비트: {{ toolConfiguration.bit }}<br>
              RAM 사용량: {{ toolConfiguration.ramUsage }}
            </td>
          </tr>
          <tr v-if="toolConfiguration.operationEnvironment === 'TARGET'" class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>소프트웨어 탑재 후 여유 공간</th>
            <td>
              RAM 여유 공간: {{ toolConfiguration.ramFreeSize }}<br>
              FLASH 여유 공간: {{ toolConfiguration.flashFreeSize }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>대상 도구: </strong>
              <span v-if="toolConfiguration.targetToolName == 'STATIC'">STATIC</span>
              <span v-if="toolConfiguration.targetToolName == 'COVER'">COVER</span>
              <span v-if="toolConfiguration.targetToolName == 'CONTROLLER_TESTER'">Controller Tester</span><br>
              <strong>도구 정보: </strong> {{ toolConfiguration.targetToolName }}, {{ toolConfiguration.toolNote }} <br>
              <strong>개발 환경: </strong> {{ toolConfiguration.developmentEnvironmentName }} <br>
              <strong>개발 언어: </strong> {{ toolConfiguration.programmingLanguage }} <br>
              <strong>IDE 정보: </strong> {{ toolConfiguration.ideName }} <br>
              <strong>컴파일러: </strong> {{ toolConfiguration.compilerName }}, {{ toolConfiguration.compilerNote }} <br>
              <strong>시험 환경:</strong>
              <span v-if="toolConfiguration.operationEnvironment == 'HOST'">호스트 환경</span>
              <span v-if="toolConfiguration.operationEnvironment == 'TARGET'">타겟 환경</span>
              <span v-if="toolConfiguration.operationEnvironment === 'TARGET'">
                <br>
                <strong>타겟 환경: </strong> {{ toolConfiguration.targetEnvironmentName }}<br>
                <strong>타겟 보드: </strong> 보드명: {{ toolConfiguration.targetEnvironmentName }}, 아키텍처: {{ toolConfiguration.architecture }}<br>
                <strong>타겟 통신: </strong> 인터페이스: {{ toolConfiguration.interfaceMethod }}, 디버거명: {{ toolConfiguration.debuggerName }}<br>
                <strong>개발 소프트웨어 정보: </strong> 실행 파일 크기: {{ toolConfiguration.executableSize }}, 비트: {{ toolConfiguration.bit }}, RAM 사용량: {{ toolConfiguration.ramUsage }}<br>
                <strong>소프트웨어 탑재 후 여유 공간: </strong> RAM 여유 공간: {{ toolConfiguration.ramFreeSize }}, FLASH 여유 공간: {{ toolConfiguration.flashFreeSize }}
              </span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="toolConfiguration.content"></div>
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

      <Comment path="tool-configuration-comments" idxName="toolConfigurationIdx" :idx="toolConfiguration.idx" :commentList="toolConfiguration.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/tool-configuration/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="toolConfiguration.access">
          <router-link :to="'/tool-configuration/update/' + idx" class="me-2">
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
    let toolConfiguration = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("tool-configuration");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/read/" + idx,
          {},
      )
          .then((response) => {
            toolConfiguration.value = response.data;
            toolConfiguration.value.content = toolConfiguration.value.content;

            // hashTags 설정
            hashTags.value = toolConfiguration.value.hashTags;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = toolConfiguration.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of toolConfiguration.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            toolConfiguration.value.createdDate = dayjs(toolConfiguration.value.createdDate).format("YYYY.MM.DD. HH:mm");
            toolConfiguration.value.lastModifiedDate = dayjs(toolConfiguration.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/tool-configuration/delete-success");
                router.push("/tool-configuration/list");
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
      toolConfiguration, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>