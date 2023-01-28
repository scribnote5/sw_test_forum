<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page=".NET Framework Design Guideline 규칙" :paths="['.NET Framework Design Guideline 규칙', '.NET Framework Design Guideline 규칙 보기']" :title="fxCop.title"/>

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
              <h2 class="mobile-title">{{ fxCop.title }}</h2>
              <h5 class="mobile-original-title">{{ fxCop.originalTitle }}</h5>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ fxCop.createdByUser.department }} {{ fxCop.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ fxCop.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ fxCop.lastModifiedByUser.department }} {{ fxCop.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ fxCop.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ fxCop.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="fxCop.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>위배 빈도</th>
            <td>
              <Frequency pageInformation="read" :frequency="fxCop.frequency"></Frequency>
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
                <span v-if="fxCop.category == 'GLOBALIZATION'" class="tooltip-right">Globalization</span>
                <span v-if="fxCop.category == 'GLOBALIZATION'" class="tooltip-text">전역에서 사용하는 라이브러리 및 애플리케이션을 위한 규칙</span>
                <span v-if="fxCop.category == 'DESIGN'" class="tooltip-right">Design</span>
                <span v-if="fxCop.category == 'DESIGN'" class="tooltip-text">라이브러리 API를 일관되고 간편하게 사용할 수 있도록 지원하는 규칙</span>
                <span v-if="fxCop.category == 'INTEROPERABILITY'" class="tooltip-right">Interoperability</span>
                <span v-if="fxCop.category == 'INTEROPERABILITY'" class="tooltip-text">다양한 플랫폼에서 이식성 향상을 위한 규칙</span>
                <span v-if="fxCop.category == 'MAINTAINABILITY'" class="tooltip-right">Maintainability</span>
                <span v-if="fxCop.category == 'MAINTAINABILITY'" class="tooltip-text">라이브러리 및 애플리케이션 유지 보수성 향상을 위한 규칙</span>
                <span v-if="fxCop.category == 'MOBILITY'" class="tooltip-right">Mobility</span>
                <span v-if="fxCop.category == 'MOBILITY'" class="tooltip-text">전력을 효율적으로 사용하기 위한 규칙</span>
                <span v-if="fxCop.category == 'NAMING'" class="tooltip-right">Naming</span>
                <span v-if="fxCop.category == 'NAMING'" class="tooltip-text">일관된 명명 규칙을 위한 규칙</span>
                <span v-if="fxCop.category == 'PERFORMANCE'" class="tooltip-right">Performance</span>
                <span v-if="fxCop.category == 'PERFORMANCE'" class="tooltip-text">라이브러리 및 애플리케이션의 성능 향상을 위한 규칙</span>
                <span v-if="fxCop.category == 'PORTABILITY'" class="tooltip-right">Portability</span>
                <span v-if="fxCop.category == 'PORTABILITY'" class="tooltip-text">다양한 플랫폼 간의 이식성을 위한 규칙</span>
                <span v-if="fxCop.category == 'RELIABILITY'" class="tooltip-right">Reliability</span>
                <span v-if="fxCop.category == 'RELIABILITY'" class="tooltip-text">라이브러리 및 애플리케이션의 안정성 향상을 위한 규칙</span>
                <span v-if="fxCop.category == 'SECURITY'" class="tooltip-right">Security</span>
                <span v-if="fxCop.category == 'SECURITY'" class="tooltip-text">라이브러리 및 애플리케이션의 보안성 향상을 위한 규칙</span>
                <span v-if="fxCop.category == 'USAGE'" class="tooltip-right">Usage</span>
                <span v-if="fxCop.category == 'USAGE'" class="tooltip-text">.NET Framework를 올바르게 사용하는 방법에 대한 규칙</span>
              </span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Breaking Change</th>
            <td>
              <span class="custom-tooltip me-1">
                <span v-if="fxCop.breakingChange == 'NON_BREAKING'" class="tooltip-right">Non Breaking</span>
                <span v-if="fxCop.breakingChange == 'NON_BREAKING'" class="tooltip-text">규칙 위반에 대한 수정 사항이 프로젝트의 주요 변경 사항이 아님</span>
                <span v-if="fxCop.breakingChange == 'BREAKING'" class="tooltip-right">Breaking</span>
                <span v-if="fxCop.breakingChange == 'BREAKING'" class="tooltip-text">규칙 위반에 대한 수정 사항이 프로젝트의 주요 변경 사항(잘못 수정하는 경우 빌드 실패할 수 있음)</span>
                <span v-if="fxCop.breakingChange == 'CHANGEABLE'" class="tooltip-right">Changeable</span>
                <span v-if="fxCop.breakingChange == 'CHANGEABLE'" class="tooltip-text">상황에 따라 프로젝트 주요 변경 사항 여부가 변경됨</span>
              </span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="fxCop.priority"></Priority>
              <strong>위배 빈도: </strong>
              <Frequency pageInformation="read" :frequency="fxCop.frequency"></Frequency>
              <strong>Category: </strong>
              <span v-if="fxCop.category == 'GLOBALIZATION'">Globalization</span>
              <span v-if="fxCop.category == 'DESIGN'">Design</span>
              <span v-if="fxCop.category == 'INTEROPERABILITY'">Interoperability</span>
              <span v-if="fxCop.category == 'MAINTAINABILITY'">Maintainability</span>
              <span v-if="fxCop.category == 'MOBILITY'">Mobility</span>
              <span v-if="fxCop.category == 'NAMING'">Naming</span>
              <span v-if="fxCop.category == 'PERFORMANCE'">Performance</span>
              <span v-if="fxCop.category == 'PORTABILITY'">Portability</span>
              <span v-if="fxCop.category == 'RELIABILITY'">Reliability</span>
              <span v-if="fxCop.category == 'SECURITY'">Security</span><br>
              <strong>Breaking Change: </strong>
              <span v-if="fxCop.breakingChange == 'NON_BREAKING'">Non Breaking</span>
              <span v-if="fxCop.breakingChange == 'BREAKING'">Breaking</span>
              <span v-if="fxCop.breakingChange == 'CHANGEABLE'">상황에 따라 변경됨</span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="fxCop.content"></div>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">.NET Framework Design Guideline 규칙 예제</th>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="rule-page-read" :link="'/fx-cop-example/list/' + idx" :exampleList="fxCopCodeMirror" mode="text/x-csharp"></CodeMirror>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">.NET Framework Design Guideline 규칙 가이드라인</th>
          </tr>
          <tr>
            <td colspan="2">
              <GuidelineList :link="'/fx-cop-guideline/list/' + idx" :guidelineList="fxCopGuidelineList"></GuidelineList>
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

      <Comment path="fx-cop-comments" idxName="fxCopIdx" :idx="fxCop.idx" :commentList="fxCop.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/fx-cop/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="fxCop.access">
          <router-link :to="'/fx-cop/update/' + idx" class="me-2">
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
    let fxCop = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    let fxCopCodeMirror = ref([]);
    let fxCopGuidelineList = ref([]);
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("fx-cop");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/read/" + idx,
          {},
      )
          .then((response) => {
            fxCop.value = response.data;
            fxCop.value.content = fxCop.value.content;

            // hashTags 설정
            hashTags.value = fxCop.value.hashTags;

            // fxCop example, fxCop guideline 설정
            fxCopCodeMirror.value = fxCop.value.fxCopExampleDtoList;
            for (let i = 0; i < fxCopCodeMirror.value.length; i++) {
              fxCopCodeMirror.value[i].link = '/fx-cop-example/read/from-rule-list/' + fxCopCodeMirror.value[i].idx;
              fxCopCodeMirror.value[i].nonCompliantExample = fxCopCodeMirror.value[i].nonCompliantExample;
              fxCopCodeMirror.value[i].compliantExample = fxCopCodeMirror.value[i].compliantExample;
              fxCopCodeMirror.value[i].badCasePositionList = JSON.parse(fxCopCodeMirror.value[i].badCasePositionList);
              fxCopCodeMirror.value[i].goodCasePositionList = JSON.parse(fxCopCodeMirror.value[i].goodCasePositionList);
            }

            fxCopGuidelineList.value = fxCop.value.fxCopGuidelineDtoList;
            for (let i = 0; i < fxCopGuidelineList.value.length; i++) {
              fxCopGuidelineList.value[i].createdDate = dayjs(fxCopGuidelineList.value[i].createdDate).format("YYYY.MM.DD.");
              fxCopGuidelineList.value[i].link = "/fx-cop-guideline/read/from-rule-list/" + fxCopGuidelineList.value[i].idx;
            }

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = fxCop.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of fxCop.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            fxCop.value.createdDate = dayjs(fxCop.value.createdDate).format("YYYY.MM.DD. HH:mm");
            fxCop.value.lastModifiedDate = dayjs(fxCop.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/fx-cop/delete-success");
                router.push("/fx-cop/list");
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
      fxCop, fxCopCodeMirror, fxCopGuidelineList, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>