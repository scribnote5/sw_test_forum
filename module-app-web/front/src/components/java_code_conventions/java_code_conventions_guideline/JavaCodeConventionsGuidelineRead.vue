<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Java Code Conventions 가이드라인" :subPage="javaCodeConventionsRule" :paths="['Java Code Conventions', 'Java Code Conventions 가이드라인 보기']" :title="javaCodeConventions.title"/>

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
              <h2 class="mobile-title">{{ javaCodeConventions.title }}</h2>
              <Like :likeDto="javaCodeConventions.likeDto"></Like>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ javaCodeConventions.createdByUser.department }} {{ javaCodeConventions.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ javaCodeConventions.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ javaCodeConventions.lastModifiedByUser.department }} {{ javaCodeConventions.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ javaCodeConventions.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ javaCodeConventions.views }}</span>
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
              {{ javaCodeConventions.projectName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>가이드라인 결과</th>
            <td>
              <span v-if="javaCodeConventions.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="javaCodeConventions.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="javaCodeConventions.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span> <br>
              {{ javaCodeConventions.guidelineResultNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ javaCodeConventions.toolName }}<br>
              {{ javaCodeConventions.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ javaCodeConventions.compilerName }}<br>
              {{ javaCodeConventions.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>Java Code Conventions 규칙: </strong> {{ javaCodeConventionsRule }} <br>
              <strong>프로젝트 정보: </strong> {{ javaCodeConventions.projectName }}<br>
              <strong>가이드라인 결과: </strong>
              <span v-if="javaCodeConventions.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="javaCodeConventions.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="javaCodeConventions.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료, </span> {{ javaCodeConventions.guidelineResultNote }} <br>
              <strong>도구 정보: </strong> {{ javaCodeConventions.toolName }}, {{ javaCodeConventions.toolNote }} <br>
              <strong>컴파일러: </strong> {{ javaCodeConventions.compilerName }}, {{ javaCodeConventions.compilerNote }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="read" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-java"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="javaCodeConventions.content"></div>
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

      <Comment path="java-code-conventions-guideline-comments" idxName="javaCodeConventionsGuidelineIdx" :idx="javaCodeConventions.idx" :commentList="javaCodeConventions.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/java-code-conventions-guideline/list/' + javaCodeConventions.javaCodeConventionsIdx : '/java-code-conventions-guideline/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/java-code-conventions/read/' + javaCodeConventions.javaCodeConventionsIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="javaCodeConventions.access">
          <router-link :to="'/java-code-conventions-guideline/update/' + idx" class="me-2">
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
    let javaCodeConventions = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}, likeDto: {likeCount: '', like: ''}});
    // hashTags
    let hashTags = ref("");
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);
    // misra c rule
    let javaCodeConventionsRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("java-code-conventions-guideline");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/read/" + idx,
          {},
      )
          .then((response) => {
            javaCodeConventions.value = response.data;
            javaCodeConventions.value.content = javaCodeConventions.value.content;

            // hashTags 설정
            hashTags.value = javaCodeConventions.value.hashTags;

            // codeMirror 설정
            nonCompliantExample.value = javaCodeConventions.value.nonCompliantExample;
            compliantExample.value = javaCodeConventions.value.compliantExample;
            badCasePositionList.value = javaCodeConventions.value.badCasePositionList;
            goodCasePositionList.value = javaCodeConventions.value.goodCasePositionList;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = javaCodeConventions.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of javaCodeConventions.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 좋아요 설정
            javaCodeConventions.value.likeDto.guidelineIdx = javaCodeConventions.value.idx;
            javaCodeConventions.value.likeDto.link = "/api/java-code-conventions-guideline-likes/";

            // 공통 데이터 설정
            javaCodeConventions.value.createdDate = dayjs(javaCodeConventions.value.createdDate).format("YYYY.MM.DD. HH:mm");
            javaCodeConventions.value.lastModifiedDate = dayjs(javaCodeConventions.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions/java-code-conventions-rule/" + javaCodeConventions.value.javaCodeConventionsIdx,
          {},
      )
          .then((response) => {
            javaCodeConventionsRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/java-code-conventions-guideline/delete-success");
                router.push("/java-code-conventions-guideline/list");
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
      javaCodeConventions, idx, hashTags, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, uploadedAttachedFileList, javaCodeConventionsRule,

      // function
      deletePost
    }
  }
}
</script>