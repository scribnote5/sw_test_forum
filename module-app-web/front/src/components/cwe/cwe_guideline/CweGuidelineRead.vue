<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="CWE C/C++ 가이드라인" :subPage="cweRule" :paths="['CWE C/C++', 'CWE C/C++ 가이드라인 보기']" :title="cweGuideline.title"/>

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
              <h2 class="mobile-title">{{ cweGuideline.title }}</h2>
              <Like :likeDto="cweGuideline.likeDto"></Like>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ cweGuideline.createdByUser.department }} {{ cweGuideline.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ cweGuideline.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ cweGuideline.lastModifiedByUser.department }} {{ cweGuideline.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ cweGuideline.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ cweGuideline.views }}</span>
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
              {{ cweGuideline.projectName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>가이드라인 결과</th>
            <td>
              <span v-if="cweGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="cweGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="cweGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="cweGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="cweGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span> <br>
              {{ cweGuideline.guidelineResultNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ cweGuideline.toolName }}<br>
              {{ cweGuideline.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ cweGuideline.compilerName }}<br>
              {{ cweGuideline.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>CWE C/C++ 규칙: </strong> {{ cweRule }} <br>
              <strong>프로젝트 정보: </strong> {{ cweGuideline.projectName }}<br>
              <strong>가이드라인 결과: </strong>
              <span v-if="cweGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="cweGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="cweGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="cweGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="cweGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료, </span> {{ cweGuideline.guidelineResultNote }} <br>
              <strong>도구 정보: </strong> {{ cweGuideline.toolName }}, {{ cweGuideline.toolNote }} <br>
              <strong>컴파일러: </strong> {{ cweGuideline.compilerName }}, {{ cweGuideline.compilerNote }}
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
              <div class="content ck-content" v-html="cweGuideline.content"></div>
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

      <Comment path="cwe-guideline-comments" idxName="cweGuidelineIdx" :idx="cweGuideline.idx" :commentList="cweGuideline.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/cwe-guideline/list/' + cweGuideline.cweIdx : '/cwe-guideline/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/cwe/read/' + cweGuideline.cweIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="cweGuideline.access">
          <router-link :to="'/cwe-guideline/update/' + idx" class="me-2">
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
    let cweGuideline = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}, likeDto: {likeCount: '', like: ''}});
    // hashTags
    let hashTags = ref("");
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);
    // cwe rule
    let cweRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("cwe-guideline");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-guidelines/read/" + Number(idx),
          {},
      )
          .then((response) => {
            cweGuideline.value = response.data;
            cweGuideline.value.content = cweGuideline.value.content;

            // hashTags 설정
            hashTags.value = cweGuideline.value.hashTags;

            // codeMirror 설정
            nonCompliantExample.value = cweGuideline.value.nonCompliantExample;
            compliantExample.value = cweGuideline.value.compliantExample;
            badCasePositionList.value = cweGuideline.value.badCasePositionList;
            goodCasePositionList.value = cweGuideline.value.goodCasePositionList;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = cweGuideline.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of cweGuideline.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 좋아요 설정
            cweGuideline.value.likeDto.guidelineIdx = cweGuideline.value.idx;
            cweGuideline.value.likeDto.link = "/api/cwe-guideline-likes/";

            // 공통 데이터 설정
            cweGuideline.value.createdDate = dayjs(cweGuideline.value.createdDate).format("YYYY.MM.DD. HH:mm");
            cweGuideline.value.lastModifiedDate = dayjs(cweGuideline.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe/cwe-rule/" + cweGuideline.value.cweIdx,
          {},
      )
          .then((response) => {
            cweRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-guidelines/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/cwe-guideline/delete-success");
                router.push("/cwe-guideline/list");
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
      cweGuideline, idx, hashTags, compliantExample, nonCompliantExample, uploadedAttachedFileList, badCasePositionList, goodCasePositionList, cweRule,

      // function
      deletePost
    }
  }
}
</script>