<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="C# Coding Convention 규칙" :paths="['C# Coding Convention 규칙', 'C# Coding Convention 규칙 보기']" :title="styleCop.title"/>

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
              <h2 class="mobile-title">{{ styleCop.title }}</h2>
              <h5 class="mobile-original-title">{{ styleCop.originalTitle }}</h5>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ styleCop.createdByUser.department }} {{ styleCop.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ styleCop.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ styleCop.lastModifiedByUser.department }} {{ styleCop.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ styleCop.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ styleCop.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="styleCop.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>위배 빈도</th>
            <td>
              <Frequency pageInformation="read" :frequency="styleCop.frequency"></Frequency>
            </td>
          </tr>
          <tr>
            <th>해시태그</th>
            <td>
              <HashTags pageInformation="read" :hash-tags="hashTags"></HashTags>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Category</th>
            <td style="overflow: visible">
              <span class="custom-tooltip me-1">
                <span v-if="styleCop.category == 'SPECIAL_RULES'" class="tooltip-right">Special Rules</span>
                <span v-if="styleCop.category == 'SPECIAL_RULES'" class="tooltip-text">해결 방법, 구성 오류 등과 같은 특수 기능을 제공하는 규칙</span>
                <span v-if="styleCop.category == 'SPACING_RULES'" class="tooltip-right">Spacing Rules</span>
                <span v-if="styleCop.category == 'SPACING_RULES'" class="tooltip-text">코드에서 키워드 및 symbol 주위에 간격 요구 사항을 적용하는 규칙</span>
                <span v-if="styleCop.category == 'READABILITY_RULES'" class="tooltip-right">Readability Rules</span>
                <span v-if="styleCop.category == 'READABILITY_RULES'" class="tooltip-text">코드가 올바르게 포맷되고 읽을 수 있도록 하는 규칙</span>
                <span v-if="styleCop.category == 'ORDERING_RULES'" class="tooltip-right">Ordering Rules</span>
                <span v-if="styleCop.category == 'ORDERING_RULES'" class="tooltip-text">코드 내용에 대해 표준 순서 체계를 적용하는 규칙</span>
                <span v-if="styleCop.category == 'NAMING_RULES'" class="tooltip-right">Naming Rules</span>
                <span v-if="styleCop.category == 'NAMING_RULES'" class="tooltip-text">멤버, 자료형 및 변수에 대한 이름을 지정하는 규칙</span>
                <span v-if="styleCop.category == 'MAINTAINABILITY_RULES'" class="tooltip-right">Maintainability Rules</span>
                <span v-if="styleCop.category == 'MAINTAINABILITY_RULES'" class="tooltip-text">코드 유지 관리성을 향상시키는 규칙</span>
                <span v-if="styleCop.category == 'LAYOUT_RULES'" class="tooltip-right">Layout Rules</span>
                <span v-if="styleCop.category == 'LAYOUT_RULES'" class="tooltip-text">코드 레이아웃 및 줄 간격을 확인하는 규칙</span>
                <span v-if="styleCop.category == 'DOCUMENTATION_RULES'" class="tooltip-right">Documentation Rules</span>
                <span v-if="styleCop.category == 'DOCUMENTATION_RULES'" class="tooltip-text">코드 문서 내용과 형식을 확인하는 규칙</span>
                <span v-if="styleCop.category == 'ALTERNATIVE_RULES'" class="tooltip-right">Alternative Rules</span>
                <span v-if="styleCop.category == 'ALTERNATIVE_RULES'" class="tooltip-text">StyleCop 규칙과 충돌하는 표준이 아닌 규칙</span>
              </span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="styleCop.priority"></Priority>
              <strong>위배 빈도: </strong>
              <Frequency pageInformation="read" :frequency="styleCop.frequency"></Frequency>
              <strong>Category: </strong>
              <span v-if="styleCop.category == 'SPECIAL_RULES'">Special Rules</span>
              <span v-if="styleCop.category == 'SPACING_RULES'">Spacing Rules</span>
              <span v-if="styleCop.category == 'READABILITY_RULES'">Readability Rules</span>
              <span v-if="styleCop.category == 'ORDERING_RULES'">Ordering Rules</span>
              <span v-if="styleCop.category == 'NAMING_RULES'">Naming Rules</span>
              <span v-if="styleCop.category == 'MAINTAINABILITY_RULES'">Maintainability Rules</span>
              <span v-if="styleCop.category == 'LAYOUT_RULES'">Layout Rules</span>
              <span v-if="styleCop.category == 'DOCUMENTATION_RULES'">Documentation Rules</span>
              <span v-if="styleCop.category == 'ALTERNATIVE_RULES'">Alternative Rules</span><br>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="styleCop.content"></div>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">C# Coding Convention 규칙 예제</th>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="rule-page-read" :link="'/style-cop-example/list/' + idx" :exampleList="styleCopCodeMirror" mode="text/x-csharp"></CodeMirror>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">C# Coding Convention 가이드라인</th>
          </tr>
          <tr>
            <td colspan="2">
              <GuidelineList :link="'/style-cop-guideline/list/' + idx" :guidelineList="styleCopGuidelineList"></GuidelineList>
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

      <Comment path="style-cop-comments" idxName="styleCopIdx" :idx="styleCop.idx" :commentList="styleCop.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/style-cop/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="styleCop.access">
          <router-link :to="'/style-cop/update/' + idx" class="me-2">
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
import Frequency from '@/components/common/Frequency.vue'
import Priority from '@/components/common/Priority.vue'
import HashTags from '@/components/common/HashTags.vue'
import CodeMirror from '@/components/common/CodeMirror.vue'
import GuidelineList from '@/components/common/GuidelineList.vue'
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
    Frequency,
    HashTags,
    CodeMirror,
    GuidelineList,
    FileUpload,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let styleCop = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    let styleCopCodeMirror = ref([]);
    let styleCopGuidelineList = ref([]);
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("style-cop");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop/read/" + idx,
          {},
      )
          .then((response) => {
            styleCop.value = response.data;
            styleCop.value.content = styleCop.value.content;

            // hashTags 설정
            hashTags.value = styleCop.value.hashTags;

            // styleCop example, styleCop guideline 설정
            styleCopCodeMirror.value = styleCop.value.styleCopExampleDtoList;
            for (let i = 0; i < styleCopCodeMirror.value.length; i++) {
              styleCopCodeMirror.value[i].link = '/style-cop-example/read/from-rule-list/' + styleCopCodeMirror.value[i].idx;
              styleCopCodeMirror.value[i].nonCompliantExample = styleCopCodeMirror.value[i].nonCompliantExample;
              styleCopCodeMirror.value[i].compliantExample = styleCopCodeMirror.value[i].compliantExample;
              styleCopCodeMirror.value[i].badCasePositionList = JSON.parse(styleCopCodeMirror.value[i].badCasePositionList);
              styleCopCodeMirror.value[i].goodCasePositionList = JSON.parse(styleCopCodeMirror.value[i].goodCasePositionList);
            }

            styleCopGuidelineList.value = styleCop.value.styleCopGuidelineDtoList;
            for (let i = 0; i < styleCopGuidelineList.value.length; i++) {
              styleCopGuidelineList.value[i].createdDate = dayjs(styleCopGuidelineList.value[i].createdDate).format("YYYY.MM.DD.");
              styleCopGuidelineList.value[i].link = "/style-cop-guideline/read/from-rule-list/" + styleCopGuidelineList.value[i].idx;
            }

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = styleCop.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of styleCop.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            styleCop.value.createdDate = dayjs(styleCop.value.createdDate).format("YYYY.MM.DD. HH:mm");
            styleCop.value.lastModifiedDate = dayjs(styleCop.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/style-cop/delete-success");
                router.push("/style-cop/list");
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
      styleCop, styleCopCodeMirror, styleCopGuidelineList, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>