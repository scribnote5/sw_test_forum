<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="동적시험 미달성 코드 분석" :paths="['나머지 자료', '동적시험 미달성 코드 분석 보기']" title=""/>

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
              <h2 class="mobile-title">{{ dynamicTest.title }}</h2>
              <Like :likeDto="dynamicTest.likeDto"></Like>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ dynamicTest.createdByUser.department }} {{ dynamicTest.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ dynamicTest.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ dynamicTest.lastModifiedByUser.department }} {{ dynamicTest.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ dynamicTest.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ dynamicTest.views }}</span>
              </div>
            </td>
          </tr>

          <tr>
            <th>해시태그</th>
            <td>
              <HashTags pageInformation="read" :hash-tags="hashTags"></HashTags>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>프로젝트 정보</th>
            <td>
              {{ dynamicTest.projectName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>가이드라인 결과</th>
            <td>
              <span v-if="dynamicTest.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="dynamicTest.guidelineResult == 'SOFTWARE_DEPENSIVE_CODE'">방어 코드</span>
              <span v-if="dynamicTest.guidelineResult == 'HARDWARE_FAILURE'">하드웨어 고장</span>
              <span v-if="dynamicTest.guidelineResult == 'INFINITE_LOOP'">무한 반복문</span>
              <span v-if="dynamicTest.guidelineResult == 'SPECIAL_HARDWARE_VALUE'">특수한 하드웨어 값</span>
              <span v-if="dynamicTest.guidelineResult == 'INVALID_HARDWARE_VALUE'">잘못된 하드웨어 값</span>
              <span v-if="dynamicTest.guidelineResult == 'SPECIAL_ENVIRONMENT'">특수한 시험 환경</span>
              <span v-if="dynamicTest.guidelineResult == 'REAL_TIME_DEGRADATION'">탐침 코드로 실시간성 저하</span>
              <span v-if="dynamicTest.guidelineResult == 'TOOL_ERROR'">도구 오류</span>
              <span v-if="dynamicTest.guidelineResult == 'OTHER_EXCEPTION'">기타 예외 처리</span> <br>
              {{ dynamicTest.guidelineResultNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ dynamicTest.toolName }}<br>
              {{ dynamicTest.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ dynamicTest.compilerName }}<br>
              {{ dynamicTest.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>프로젝트 정보: </strong> {{ dynamicTest.projectName }}<br>
              <strong>가이드라인 결과: </strong>
              <span v-if="dynamicTest.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="dynamicTest.guidelineResult == 'SOFTWARE_DEPENSIVE_CODE'">방어 코드</span>
              <span v-if="dynamicTest.guidelineResult == 'HARDWARE_FAILURE'">하드웨어 고장</span>
              <span v-if="dynamicTest.guidelineResult == 'INFINITE_LOOP'">무한 반복문</span>
              <span v-if="dynamicTest.guidelineResult == 'SPECIAL_HARDWARE_VALUE'">특수한 하드웨어 값</span>
              <span v-if="dynamicTest.guidelineResult == 'INVALID_HARDWARE_VALUE'">잘못된 하드웨어 값</span>
              <span v-if="dynamicTest.guidelineResult == 'SPECIAL_ENVIRONMENT'">특수한 시험 환경</span>
              <span v-if="dynamicTest.guidelineResult == 'REAL_TIME_DEGRADATION'">탐침 코드로 실시간성 저하</span>
              <span v-if="dynamicTest.guidelineResult == 'TOOL_ERROR'">도구 오류</span>
              <span v-if="dynamicTest.guidelineResult == 'OTHER_EXCEPTION'">기타 예외 처리, </span> {{ dynamicTest.guidelineResultNote }} <br>
              <strong>도구 정보: </strong> {{ dynamicTest.toolName }}, {{ dynamicTest.toolNote }} <br>
              <strong>컴파일러: </strong> {{ dynamicTest.compilerName }}, {{ dynamicTest.compilerNote }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="read" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-c++src"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="dynamicTest.content"></div>
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

      <Comment path="dynamic-test-comments" idxName="dynamicTestIdx" :idx="dynamicTest.idx" :commentList="dynamicTest.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/dynamic-test/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="dynamicTest.access">
          <router-link :to="'/dynamic-test/update/' + idx" class="me-2">
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
import Like from '@/components/common/Like.vue'
import HashTags from '@/components/common/HashTags.vue'
import CodeMirror from '@/components/common/CodeMirror.vue'
import FileUpload from '@/components/common/FileUpload.vue'
import Comment from '@/components/common/Comment.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {confirm, fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Like,
    HashTags,
    CodeMirror,
    FileUpload,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let dynamicTest = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}, likeDto: {likeCount: '', like: ''}});
    // hashTags
    let hashTags = ref("");
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("dynamic-test");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/dynamic-tests/read/" + idx,
          {},
      )
          .then((response) => {
            dynamicTest.value = response.data;
            dynamicTest.value.content = dynamicTest.value.content;

            // hashTags 설정
            hashTags.value = dynamicTest.value.hashTags;

            // codeMirror 설정
            nonCompliantExample.value = dynamicTest.value.nonCompliantExample;
            compliantExample.value = dynamicTest.value.compliantExample;
            badCasePositionList.value = dynamicTest.value.badCasePositionList;
            goodCasePositionList.value = dynamicTest.value.goodCasePositionList;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = dynamicTest.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of dynamicTest.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 좋아요 설정
            dynamicTest.value.likeDto.guidelineIdx = dynamicTest.value.idx;
            dynamicTest.value.likeDto.link = "/api/dynamic-test-likes/";

            // 공통 데이터 설정
            dynamicTest.value.createdDate = dayjs(dynamicTest.value.createdDate).format("YYYY.MM.DD. HH:mm");
            dynamicTest.value.lastModifiedDate = dayjs(dynamicTest.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/dynamic-tests/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/dynamic-test/delete-success");
                router.push("/dynamic-test/list");
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
      dynamicTest, idx, hashTags, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>