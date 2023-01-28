<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="질문 답변" :paths="['질문 답변', '질문 답변 보기']" :title="questionAnswer.title"/>

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
              <h2 class="mobile-title">{{ questionAnswer.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ questionAnswer.createdByUser.department }} {{ questionAnswer.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ questionAnswer.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ questionAnswer.lastModifiedByUser.department }} {{ questionAnswer.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ questionAnswer.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ questionAnswer.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>유형</th>
            <td>
              <span v-if="questionAnswer.type == 'STATIC_TEST_QUESTION'">정적시험 질문</span>
              <span v-if="questionAnswer.type == 'DYNAMIC_TEST_QUESTION'">동적시험 질문</span>
              <span v-if="questionAnswer.type == 'TOOL_QUESTION'">도구 질문</span>
              <span v-if="questionAnswer.type == 'ETC_QUESTION'">기타 질문</span>
              <span v-if="questionAnswer.type == 'ANSWER'">답변</span>
              <span v-if="questionAnswer.type == 'COMPLETE'">완료</span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>유형: </strong>
              <span v-if="questionAnswer.type == 'STATIC_TEST_QUESTION'">정적시험 질문</span>
              <span v-if="questionAnswer.type == 'DYNAMIC_TEST_QUESTION'">동적시험 질문</span>
              <span v-if="questionAnswer.type == 'TOOL_QUESTION'">도구 질문</span>
              <span v-if="questionAnswer.type == 'ETC_QUESTION'">기타 질문</span>
              <span v-if="questionAnswer.type == 'ANSWER'">답변</span>
              <span v-if="questionAnswer.type == 'COMPLETE'">완료</span>
            </td>
          </tr>
          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="questionAnswer.content"></div>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <FileUpload pageInformation="read" :uploadedAttachedFileList="uploadedAttachedFileList"></FileUpload>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">과거 질문 답변 정보</th>
          </tr>
          <tr v-for="(questionAnswer, i) in questionAnswerList" :key="i">
            <td colspan="2">
              <div>
                <div class="d-flex">
                  <span v-if="questionAnswer.type == 'STATIC_TEST_QUESTION'" class="question-answer-test-type" style="font-size: 16px">정적시험 질문</span>
                  <span v-if="questionAnswer.type == 'DYNAMIC_TEST_QUESTION'" class="question-test-type" style="font-size: 16px">동적시험 질문</span>
                  <span v-if="questionAnswer.type == 'TOOL_QUESTION'" class="question-tool-type" style="font-size: 16px">도구 질문</span>
                  <span v-if="questionAnswer.type == 'ETC_QUESTION'" class="question-etc-type" style="font-size: 16px">기타 질문</span>
                  <span v-if="questionAnswer.type == 'ANSWER'" class="question-answer-answer-type" style="font-size: 16px"> 답변 </span>
                  <span v-if="questionAnswer.type == 'COMPLETE'" class="question-answer-complete-type" style="font-size: 16px">완료</span>&nbsp;&nbsp;
                  <span style="font-size: 16px">{{ questionAnswer.title }}</span>
                </div>

                <div class="d-flex justify-content-end">
                  <strong class="additional-information-title">작성자:&nbsp;</strong><span class="additional-information-content">{{ questionAnswer.createdByUser.department }} {{ questionAnswer.createdByUser.name }},&nbsp;</span>
                  <strong class="additional-information-title">작성일:&nbsp;</strong><span class="additional-information-content">{{ questionAnswer.createdDate }}</span>
                </div>
              </div>

              <div class="content ck-content mt-2" v-html="questionAnswer.content"></div>

              <hr>
              <FileUpload pageInformation="read" :uploadedAttachedFileList="questionAnswer.attachedFileList"></FileUpload>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <Comment path="question-answer-comments" idxName="questionAnswerIdx" :idx="questionAnswer.idx" :commentList="questionAnswer.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/question-answer/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="questionAnswer.access">
          <!-- 답변 -->
          <router-link v-if="questionAnswer.type != 'COMPLETE'" :to="'/question-answer/answer/write/' + questionAnswer.groupIdx" class="me-2">
            <button class="btn btn-main-navy d-flex align-items-center">답변 등록<img :src="require(`@/assets/images/corner-down-right-white.svg`)" class="ms-2"></button>
          </router-link>
          <!-- 질문 일 때 -->
          <router-link v-if="questionAnswer.type != 'ANSWER'" :to="'/question-answer/question/update/' + idx" class="me-2">
            <button class="btn btn-main-blue d-flex align-items-center">질문 수정<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
          </router-link>
          <!-- 답변 일 때 -->
          <router-link v-else :to="'/question-answer/answer/update/' + idx" class="me-2">
            <button class="btn btn-main-blue d-flex align-items-center">답변 수정<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
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
import FileUpload from '@/components/common/FileUpload.vue'
import Comment from '@/components/common/Comment.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {confirm, fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";

export default {
  components: {
    Loading,
    Breadcrumb,
    FileUpload,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let questionAnswerList = ref({});
    let questionAnswer = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("question-answer");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/read/" + idx,
          {},
      )
          .then((response) => {
            questionAnswer.value = response.data;
            questionAnswer.value.content = questionAnswer.value.content;

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = questionAnswer.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of questionAnswer.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            questionAnswer.value.createdDate = dayjs(questionAnswer.value.createdDate).format("YYYY.MM.DD. HH:mm");
            questionAnswer.value.lastModifiedDate = dayjs(questionAnswer.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      //
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/read/" + idx + "/" + questionAnswer.value.groupIdx,
          {},
      )
          .then((response) => {
            questionAnswerList.value = response.data;

            for (const questionAnswer of questionAnswerList.value) {
              // 업로드 된 첨부 파일 설정
              uploadedAttachedFileList = questionAnswer.attachedFileList;
              uploadedAttachedFileList.totalFileSize = 0;
              uploadedAttachedFileList.convertedTotalFileSize = 0;
              for (const uploadedAttachedFile of uploadedAttachedFileList) {
                uploadedAttachedFileList.totalFileSize += uploadedAttachedFile.fileSize;
                uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
              }
              uploadedAttachedFileList.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.totalFileSize);

              // dayjs
              questionAnswer.createdDate = dayjs(questionAnswer.createdDate).format("YYYY.MM.DD.");
            }
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/question-answer/delete-success");
                router.push("/question-answer/list");
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
      questionAnswerList, idx, questionAnswer, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>