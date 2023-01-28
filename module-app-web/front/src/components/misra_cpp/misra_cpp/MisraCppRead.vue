<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C++ 규칙" :paths="['MISRA C++ 규칙', 'MISRA C++ 규칙 보기']" :title="misraCpp.title"/>

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
              <h2 class="mobile-title">{{ misraCpp.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ misraCpp.createdByUser.department }} {{ misraCpp.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ misraCpp.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ misraCpp.lastModifiedByUser.department }} {{ misraCpp.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ misraCpp.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ misraCpp.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="misraCpp.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>위배 빈도</th>
            <td>
              <Frequency pageInformation="read" :frequency="misraCpp.frequency"></Frequency>
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
                <span v-if="misraCpp.category == 'REQUIRED'" class="tooltip-right">Required</span>
                <span v-if="misraCpp.category == 'REQUIRED'" class="tooltip-text">필수적으로 준수해야 규칙, 정당한 사유가 있으면 예외를 허용</span>
                <span v-if="misraCpp.category == 'ADVISORY'" class="tooltip-right">Advisory</span>
                <span v-if="misraCpp.category == 'ADVISORY'" class="tooltip-text">준수하는 것을 권고하는 규칙, 예외가 필요 없지만 필요한 경우 작성</span>
                <span v-if="misraCpp.category == 'DOCUMENT'" class="tooltip-right">Document</span>
                <span v-if="misraCpp.category == 'DOCUMENT'" class="tooltip-text">코드 내에서 관련 기능을 사용할 때 필수로 준수해야 하는 규칙, 예외를 허용하지 않음</span>
              </span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>
              QAC 규칙
              <img :src="require(`@/assets/images/chevron-down-main-black.svg`)" id="qacTitleChevronDown"  class="ms-2" @click="qacTitleDisplay(true)">
              <img :src="require(`@/assets/images/chevron-up-main-black.svg`)" id="qacTitleChevronUp" class="ms-2" @click="qacTitleDisplay(false)" style="display: none">
            </th>
            <td>
              <span id="qacTitle" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{ misraCpp.qacTitle }}</span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="misraCpp.priority"></Priority>
              <strong>위배 빈도: </strong>
              <Frequency pageInformation="read" :frequency="misraCpp.frequency"></Frequency>
              <strong>Category: </strong>
              <span v-if="misraCpp.category == 'REQUIRED'">Required</span>
              <span v-if="misraCpp.category == 'ADVISORY'">Advisory</span>
              <span v-if="misraCpp.category == 'DOCUMENT'">DOCUMENT</span><br>
              <span style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
                <strong>QAC 규칙: </strong>
                {{ misraCpp.qacTitle }}
              </span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="misraCpp.content"></div>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">MISRA C++ 규칙 예제</th>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="rule-page-read" :link="'/misra-cpp-example/list/' + idx" :exampleList="misraCppCodeMirror" mode="text/x-c++src"></CodeMirror>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C++ 가이드라인</th>
          </tr>
          <tr>
            <td colspan="2">
              <GuidelineList :link="'/misra-cpp-guideline/list/' + idx" :guidelineList="misraCppGuidelineList"></GuidelineList>
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

      <Comment path="misra-cpp-comments" idxName="misraCppIdx" :idx="misraCpp.idx" :commentList="misraCpp.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/misra-cpp/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="misraCpp.access">
          <router-link :to="'/misra-cpp/update/' + idx" class="me-2">
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
    let misraCpp = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    let misraCppCodeMirror = ref([]);
    let misraCppGuidelineList = ref([]);
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("misra-cpp");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp/read/" + idx,
          {},
      )
          .then((response) => {
            misraCpp.value = response.data;
            misraCpp.value.content = misraCpp.value.content;

            // hashTags 설정
            hashTags.value = misraCpp.value.hashTags;

            // misra cpp example, misra cpp guideline 설정
            misraCppCodeMirror.value = misraCpp.value.misraCppExampleDtoList;
            for (let i = 0; i < misraCppCodeMirror.value.length; i++) {
              misraCppCodeMirror.value[i].link = '/misra-cpp-example/read/from-rule-list/' + misraCppCodeMirror.value[i].idx;
              misraCppCodeMirror.value[i].nonCompliantExample = misraCppCodeMirror.value[i].nonCompliantExample;
              misraCppCodeMirror.value[i].compliantExample = misraCppCodeMirror.value[i].compliantExample;
              misraCppCodeMirror.value[i].badCasePositionList = JSON.parse(misraCppCodeMirror.value[i].badCasePositionList);
              misraCppCodeMirror.value[i].goodCasePositionList = JSON.parse(misraCppCodeMirror.value[i].goodCasePositionList);
            }

            misraCppGuidelineList.value = misraCpp.value.misraCppGuidelineDtoList;
            for (let i = 0; i < misraCppGuidelineList.value.length; i++) {
              misraCppGuidelineList.value[i].createdDate = dayjs(misraCppGuidelineList.value[i].createdDate).format("YYYY.MM.DD.");
              misraCppGuidelineList.value[i].link = "/misra-cpp-guideline/read/from-rule-list/" + misraCppGuidelineList.value[i].idx;
            }

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = misraCpp.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of misraCpp.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            misraCpp.value.createdDate = dayjs(misraCpp.value.createdDate).format("YYYY.MM.DD. HH:mm");
            misraCpp.value.lastModifiedDate = dayjs(misraCpp.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

    })

    /* qacTitle 보여주기 설정 */
    const qacTitleDisplay =(isDisplay) => {
      if(isDisplay) {
        document.getElementById("qacTitleChevronDown").style.display = "none";
        document.getElementById("qacTitleChevronUp").style.display = "inline";
        document.getElementById("qacTitle").style.whiteSpace = "normal";
      } else {
        document.getElementById("qacTitleChevronDown").style.display = "inline";
        document.getElementById("qacTitleChevronUp").style.display = "none";
        document.getElementById("qacTitle").style.whiteSpace = "nowrap";
      }
    }

    /* 삭제 */
    const deletePost = () => {
      confirm.fire({
        title: "삭제하시겠습니까?",
        text: "게시글과 관련된 모든 데이터는 삭제됩니다.",
        confirmButtonText: '네',
        cancelButtonText: '아니오'
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/misra-cpp/delete-success");
                router.push("/misra-cpp/list");
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
      misraCpp, misraCppCodeMirror, misraCppGuidelineList, idx, hashTags, uploadedAttachedFileList,

      // function
      qacTitleDisplay, deletePost
    }
  }
}
</script>