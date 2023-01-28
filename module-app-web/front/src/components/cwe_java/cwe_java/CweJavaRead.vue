<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="CWE Java 규칙" :paths="['CWE Java 규칙', 'CWE Java 규칙 보기']" :title="cweJava.title"/>

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
              <h2 class="mobile-title">{{ cweJava.title }}</h2>
              <h5 class="mobile-original-title">{{ cweJava.originalTitle }}</h5>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ cweJava.createdByUser.department }} {{ cweJava.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ cweJava.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ cweJava.lastModifiedByUser.department }} {{ cweJava.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ cweJava.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ cweJava.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="cweJava.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>위배 빈도</th>
            <td>
              <Frequency pageInformation="read" :frequency="cweJava.frequency"></Frequency>
            </td>
          </tr>
          <tr>
            <th>해시태그</th>
            <td>
              <HashTags pageInformation="read" :hash-tags="hashTags"></HashTags>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>STATIC 규칙</th>
            <td>
              {{ cweJava.staticTitle }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Sparrow 규칙</th>
            <td>
              {{ cweJava.sparrowTitle }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="cweJava.priority"></Priority>
              <strong>위배 빈도: </strong>
              <Frequency pageInformation="read" :frequency="cweJava.frequency"></Frequency>
              <strong>STATIC 규칙: </strong>
              {{ cweJava.staticTitle }}<br>
              <strong>Sparrow 규칙: </strong>
              {{ cweJava.sparrowTitle }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="cweJava.content"></div>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">CWE Java 규칙 예제</th>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="rule-page-read" :link="'/cwe-java-example/list/' + idx" :exampleList="cweJavaCodeMirror" mode="text/x-java"></CodeMirror>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">CWE Java 가이드라인</th>
          </tr>
          <tr>
            <td colspan="2">
              <GuidelineList :link="'/cwe-java-guideline/list/' + idx" :guidelineList="cweJavaGuidelineList"></GuidelineList>
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

      <Comment path="cwe-java-comments" idxName="cweJavaIdx" :idx="cweJava.idx" :commentList="cweJava.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/cwe-java/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="cweJava.access">
          <router-link :to="'/cwe-java/update/' + idx" class="me-2">
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
    let cweJava = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    let cweJavaCodeMirror = ref([]);
    let cweJavaGuidelineList = ref([]);
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("cwe-java");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-java/read/" + idx,
          {},
      )
          .then((response) => {
            cweJava.value = response.data;
            cweJava.value.content = cweJava.value.content;

            // hashTags 설정
            hashTags.value = cweJava.value.hashTags;

            // cwe java example, cwe java guideline 설정
            cweJavaCodeMirror.value = cweJava.value.cweJavaExampleDtoList;
            for (let i = 0; i < cweJavaCodeMirror.value.length; i++) {
              cweJavaCodeMirror.value[i].link = '/cwe-java-example/read/from-rule-list/' + cweJavaCodeMirror.value[i].idx;
              cweJavaCodeMirror.value[i].nonCompliantExample = cweJavaCodeMirror.value[i].nonCompliantExample;
              cweJavaCodeMirror.value[i].compliantExample = cweJavaCodeMirror.value[i].compliantExample;
              cweJavaCodeMirror.value[i].badCasePositionList = JSON.parse(cweJavaCodeMirror.value[i].badCasePositionList);
              cweJavaCodeMirror.value[i].goodCasePositionList = JSON.parse(cweJavaCodeMirror.value[i].goodCasePositionList);
            }

            cweJavaGuidelineList.value = cweJava.value.cweJavaGuidelineDtoList;
            for (let i = 0; i < cweJavaGuidelineList.value.length; i++) {
              cweJavaGuidelineList.value[i].createdDate = dayjs(cweJavaGuidelineList.value[i].createdDate).format("YYYY.MM.DD.");
              cweJavaGuidelineList.value[i].link = "/cwe-java-guideline/read/from-rule-list/" + cweJavaGuidelineList.value[i].idx;
            }

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = cweJava.value.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 댓글 설정
            for (let comment of cweJava.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            cweJava.value.createdDate = dayjs(cweJava.value.createdDate).format("YYYY.MM.DD. HH:mm");
            cweJava.value.lastModifiedDate = dayjs(cweJava.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-java/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/cwe-java/delete-success");
                router.push("/cwe-java/list");
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
      cweJava, cweJavaCodeMirror, cweJavaGuidelineList, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>