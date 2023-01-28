<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page=".NET Framework Design Guideline 가이드라인" :subPage="fxCopRule" :paths="['.NET Framework Design Guideline', '.NET Framework Design Guideline 가이드라인 보기']" :title="fxCopGuideline.title"/>

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
              <h2 class="mobile-title">{{ fxCopGuideline.title }}</h2>
              <Like :likeDto="fxCopGuideline.likeDto"></Like>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ fxCopGuideline.createdByUser.department }} {{ fxCopGuideline.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ fxCopGuideline.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ fxCopGuideline.lastModifiedByUser.department }} {{ fxCopGuideline.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ fxCopGuideline.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ fxCopGuideline.views }}</span>
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
              {{ fxCopGuideline.projectName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>가이드라인 결과</th>
            <td>
              <span v-if="fxCopGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="fxCopGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="fxCopGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="fxCopGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="fxCopGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span> <br>
              {{ fxCopGuideline.guidelineResultNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ fxCopGuideline.toolName }}<br>
              {{ fxCopGuideline.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ fxCopGuideline.compilerName }}<br>
              {{ fxCopGuideline.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>FxCop 규칙: </strong> {{ fxCopRule }} <br>
              <strong>프로젝트 정보: </strong> {{ fxCopGuideline.projectName }}<br>
              <strong>가이드라인 결과: </strong>
              <span v-if="fxCopGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="fxCopGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="fxCopGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="fxCopGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="fxCopGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료, </span> {{ fxCopGuideline.guidelineResultNote }} <br>
              <strong>도구 정보: </strong> {{ fxCopGuideline.toolName }}, {{ fxCopGuideline.toolNote }} <br>
              <strong>컴파일러: </strong> {{ fxCopGuideline.compilerName }}, {{ fxCopGuideline.compilerNote }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="read" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-csharp"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="fxCopGuideline.content"></div>
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

      <Comment path="fx-cop-guideline-comments" idxName="fxCopGuidelineIdx" :idx="fxCopGuideline.idx" :commentList="fxCopGuideline.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/fx-cop-guideline/list/' + fxCopGuideline.fxCopIdx : '/fx-cop-guideline/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/fx-cop/read/' + fxCopGuideline.fxCopIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="fxCopGuideline.access">
          <router-link :to="'/fx-cop-guideline/update/' + idx" class="me-2">
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
    let fxCopGuideline = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}, likeDto: {likeCount: '', like: ''}});
    // hashTags
    let hashTags = ref("");
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);
    // fxCop rule
    let fxCopRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("fx-cop-guideline");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop-guidelines/read/" + idx,
          {},
      )
          .then((response) => {
            fxCopGuideline.value = response.data;
            fxCopGuideline.value.content = fxCopGuideline.value.content;

            // hashTags 설정
            hashTags.value = fxCopGuideline.value.hashTags;

            // codeMirror 설정
            nonCompliantExample.value = fxCopGuideline.value.nonCompliantExample;
            compliantExample.value = fxCopGuideline.value.compliantExample;
            badCasePositionList.value = fxCopGuideline.value.badCasePositionList;
            goodCasePositionList.value = fxCopGuideline.value.goodCasePositionList;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = fxCopGuideline.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of fxCopGuideline.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 좋아요 설정
            fxCopGuideline.value.likeDto.guidelineIdx = fxCopGuideline.value.idx;
            fxCopGuideline.value.likeDto.link = "/api/fx-cop-guideline-likes/";

            // 공통 데이터 설정
            fxCopGuideline.value.createdDate = dayjs(fxCopGuideline.value.createdDate).format("YYYY.MM.DD. HH:mm");
            fxCopGuideline.value.lastModifiedDate = dayjs(fxCopGuideline.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/fx-cop-rule/" + fxCopGuideline.value.fxCopIdx,
          {},
      )
          .then((response) => {
            fxCopRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop-guidelines/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/fx-cop-guideline/delete-success");
                router.push("/fx-cop-guideline/list");
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
      fxCopGuideline, idx, hashTags, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, uploadedAttachedFileList, fxCopRule,

      // function
      deletePost
    }
  }
}
</script>