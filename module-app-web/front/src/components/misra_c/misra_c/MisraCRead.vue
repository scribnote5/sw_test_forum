<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C 규칙" :paths="['MISRA C 규칙', 'MISRA C 규칙 보기']" :title="misraC.title"/>

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
              <h2 class="mobile-title">{{ misraC.title }}</h2>
              <h5 class="mobile-original-title">{{ misraC.originalTitle }}</h5>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ misraC.createdByUser.department }} {{ misraC.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ misraC.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ misraC.lastModifiedByUser.department }} {{ misraC.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ misraC.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ misraC.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="misraC.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>위배 빈도</th>
            <td>
              <Frequency pageInformation="read" :frequency="misraC.frequency"></Frequency>
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
                <span v-if="misraC.category == 'MANDATORY'" class="tooltip-right">Mandatory</span>
                <span v-if="misraC.category == 'MANDATORY'" class="tooltip-text">필수적으로 준수해야 규칙, 예외를 허용하지 않음</span>
                <span v-if="misraC.category == 'REQUIRED'" class="tooltip-right">Required</span>
                <span v-if="misraC.category == 'REQUIRED'" class="tooltip-text">필수적으로 준수해야 하는 규칙, 정당한 사유가 있으면 예외를 허용</span>
                <span v-if="misraC.category == 'ADVISORY'" class="tooltip-right">Advisory</span>
                <span v-if="misraC.category == 'ADVISORY'" class="tooltip-text">준수하는 것을 권고하는 규칙, 선택적으로 적용</span>
              </span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Scope</th>
            <td style="overflow: visible">
              <span class="custom-tooltip me-1">
                <span v-if="misraC.scope == 'SYSTEM'" class="tooltip-right">System</span>
                <span v-if="misraC.scope == 'SYSTEM'" class="tooltip-text">전처리된 하나의 C언어 파일에서만 영향을 미침</span>
                <span v-if="misraC.scope == 'TRANSLATION_UNIT'" class="tooltip-right">Translation Unit</span>
                <span v-if="misraC.scope == 'TRANSLATION_UNIT'" class="tooltip-text">시스템 전체 범위에 영향을 미침</span>
              </span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Decidability</th>
            <td style="overflow: visible">
              <span class="custom-tooltip me-1">
                <span v-if="misraC.decidability == 'DECIDEABLE'" class="tooltip-right">Decidable</span>
                <span v-if="misraC.decidability == 'DECIDEABLE'" class="tooltip-text">
                  가이드라인 결과를 항상 보장할 수 있다.<br>
                  즉, 가이드라인 결과가 항상 '예' 또는'아니오'로 나오는 경우
                </span>
                <span v-if="misraC.decidability == 'UNDECIDEABLE'" class="tooltip-right">Undecidable</span>
                <span v-if="misraC.decidability == 'UNDECIDEABLE'" class="tooltip-text">
                  가이드라인 결과를 항상 보장할 수 없다.<br>
                  즉, 가이드라인 결과를 '예' 또는 '아니오'로 보장할 수 없음
                </span>
              </span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Applies To</th>
            <td style="overflow: visible">
              <span class="custom-tooltip me-1">
                <span class="tooltip-right">{{ misraC.appliesTo }}</span>
                <span class="tooltip-text">
                  해당 규칙이 적용되는 C언어 표준<br>
                  C90, C99, C11 표준이 존재함.
                </span>
              </span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>
              QAC 규칙
              <img :src="require(`@/assets/images/chevron-down-main-black.svg`)" id="qacTitleChevronDown" class="ms-2" @click="qacTitleDisplay(true)">
              <img :src="require(`@/assets/images/chevron-up-main-black.svg`)" id="qacTitleChevronUp" class="ms-2" @click="qacTitleDisplay(false)" style="display: none">
            </th>
            <td>
              <span id="qacTitle" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">{{ misraC.qacTitle }}</span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="misraC.priority"></Priority>
              <strong>위배 빈도: </strong>
              <Frequency pageInformation="read" :frequency="misraC.frequency"></Frequency>
              <strong>Category: </strong>
              <span v-if="misraC.category == 'MANDATORY'">Mandatory</span>
              <span v-if="misraC.category == 'REQUIRED'">Required</span>
              <span v-if="misraC.category == 'ADVISORY'">Advisory</span><br>
              <strong>Scope: </strong>
              <span v-if="misraC.scope == 'SYSTEM'">System</span>
              <span v-if="misraC.scope == 'TRANSLATION_UNIT'">Translation Unit</span><br>
              <strong>Decidability: </strong>
              <span v-if="misraC.decidability == 'DECIDEABLE'">Decidable</span>
              <span v-if="misraC.decidability == 'UNDECIDEABLE'">Undecidable</span>
              <span style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap"><br>
              <strong>Applies To</strong>:
              {{ misraC.appliesTo }}<br>
              <strong>QAC 규칙: </strong>
              {{ misraC.qacTitle }}
              </span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="misraC.content"></div>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">MISRA C 규칙 예제</th>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="rule-page-read" :link="'/misra-c-example/list/' + idx" :exampleList="misraCCodeMirror" mode="text/x-csrc"></CodeMirror>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C 가이드라인</th>
          </tr>
          <tr>
            <td colspan="2">
              <GuidelineList :link="'/misra-c-guideline/list/' + idx" :guidelineList="misraCGuidelineList"></GuidelineList>
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

      <Comment path="misra-c-comments" idxName="misraCIdx" :idx="misraC.idx" :commentList="misraC.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/misra-c/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="misraC.access">
          <router-link :to="'/misra-c/update/' + idx" class="me-2">
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
    let misraC = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    let misraCCodeMirror = ref([]);
    let misraCGuidelineList = ref([]);
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("misra-c");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/read/" + idx,
          {},
      )
          .then((response) => {
            misraC.value = response.data;
            misraC.value.content = misraC.value.content;

            // hashTags 설정
            hashTags.value = misraC.value.hashTags;

            // misra c example, misra c guideline 설정
            misraCCodeMirror.value = misraC.value.misraCExampleDtoList;
            for (let i = 0; i < misraCCodeMirror.value.length; i++) {
              misraCCodeMirror.value[i].link = '/misra-c-example/read/from-rule-list/' + misraCCodeMirror.value[i].idx;
              misraCCodeMirror.value[i].nonCompliantExample = misraCCodeMirror.value[i].nonCompliantExample;
              misraCCodeMirror.value[i].compliantExample = misraCCodeMirror.value[i].compliantExample;
              misraCCodeMirror.value[i].badCasePositionList = JSON.parse(misraCCodeMirror.value[i].badCasePositionList);
              misraCCodeMirror.value[i].goodCasePositionList = JSON.parse(misraCCodeMirror.value[i].goodCasePositionList);
            }

            misraCGuidelineList.value = misraC.value.misraCGuidelineDtoList;
            for (let i = 0; i < misraCGuidelineList.value.length; i++) {
              misraCGuidelineList.value[i].createdDate = dayjs(misraCGuidelineList.value[i].createdDate).format("YYYY.MM.DD.");
              misraCGuidelineList.value[i].link = "/misra-c-guideline/read/from-rule-list/" + misraCGuidelineList.value[i].idx;
            }

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = misraC.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of misraC.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            misraC.value.createdDate = dayjs(misraC.value.createdDate).format("YYYY.MM.DD. HH:mm");
            misraC.value.lastModifiedDate = dayjs(misraC.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

    })

    /* qacTitle 보여주기 설정 */
    const qacTitleDisplay = (isDisplay) => {
      if (isDisplay) {
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/misra-c/delete-success");
                router.push("/misra-c/list");
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
      misraC, misraCCodeMirror, misraCGuidelineList, idx, hashTags, uploadedAttachedFileList,

      // function
      qacTitleDisplay, deletePost
    }
  }
}
</script>