<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="메트릭 가이드라인" :subPage="metricRule" :paths="['메트릭', '메트릭 가이드라인 보기']" :title="metricGuideline.title"/>

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
              <h2 class="mobile-title">{{ metricGuideline.title }}</h2>
              <Like :likeDto="metricGuideline.likeDto"></Like>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ metricGuideline.createdByUser.department }} {{ metricGuideline.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ metricGuideline.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ metricGuideline.lastModifiedByUser.department }} {{ metricGuideline.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ metricGuideline.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ metricGuideline.views }}</span>
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
              {{ metricGuideline.projectName }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>가이드라인 결과</th>
            <td>
              <span v-if="metricGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="metricGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="metricGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="metricGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="metricGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span> <br>
              {{ metricGuideline.guidelineResultNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ metricGuideline.toolName }}<br>
              {{ metricGuideline.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ metricGuideline.compilerName }}<br>
              {{ metricGuideline.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>메트릭 규칙: </strong> {{ metricRule }} <br>
              <strong>프로젝트 정보: </strong> {{ metricGuideline.projectName }}<br>
              <strong>가이드라인 결과: </strong>
              <span v-if="metricGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="metricGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="metricGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="metricGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="metricGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료, </span> {{ metricGuideline.guidelineResultNote }} <br>
              <strong>도구 정보: </strong> {{ metricGuideline.toolName }}, {{ metricGuideline.toolNote }} <br>
              <strong>컴파일러: </strong> {{ metricGuideline.compilerName }}, {{ metricGuideline.compilerNote }}
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
              <div class="content ck-content" v-html="metricGuideline.content"></div>
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

      <Comment path="metric-guideline-comments" idxName="metricGuidelineIdx" :idx="metricGuideline.idx" :commentList="metricGuideline.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/metric-guideline/list/' + metricGuideline.metricIdx : '/metric-guideline/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/metric/read/' + metricGuideline.metricIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="metricGuideline.access">
          <router-link :to="'/metric-guideline/update/' + idx" class="me-2">
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
    let metricGuideline = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}, likeDto: {likeCount: '', like: ''}});
    // hashTags
    let hashTags = ref("");
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);
    // metric rule
    let metricRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("metric-guideline");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric-guidelines/read/" + idx,
          {},
      )
          .then((response) => {
            metricGuideline.value = response.data;
            metricGuideline.value.content = metricGuideline.value.content;

            // hashTags 설정
            hashTags.value = metricGuideline.value.hashTags;

            // codeMirror 설정
            nonCompliantExample.value = metricGuideline.value.nonCompliantExample;
            compliantExample.value = metricGuideline.value.compliantExample;
            badCasePositionList.value = metricGuideline.value.badCasePositionList;
            goodCasePositionList.value = metricGuideline.value.goodCasePositionList;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = metricGuideline.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of metricGuideline.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 좋아요 설정
            metricGuideline.value.likeDto.guidelineIdx = metricGuideline.value.idx;
            metricGuideline.value.likeDto.link = "/api/metric-guideline-likes/";

            // 공통 데이터 설정
            metricGuideline.value.createdDate = dayjs(metricGuideline.value.createdDate).format("YYYY.MM.DD. HH:mm");
            metricGuideline.value.lastModifiedDate = dayjs(metricGuideline.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/metric-rule/" + metricGuideline.value.metricIdx,
          {},
      )
          .then((response) => {
            metricRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric-guidelines/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/metric-guideline/delete-success");
                router.push("/metric-guideline/list");
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
      metricGuideline, idx, hashTags, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, uploadedAttachedFileList, metricRule,

      // function
      deletePost
    }
  }
}
</script>