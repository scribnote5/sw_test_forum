<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Java Code Conventions 규칙" :paths="['Java Code Conventions 규칙', 'Java Code Conventions 규칙 보기']" :title="javaCodeConventions.title"/>

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
              <h5 class="mobile-original-title">{{ javaCodeConventions.originalTitle }}</h5>
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

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="javaCodeConventions.priority" :maxPriority=Number(6)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>위배 빈도</th>
            <td>
              <Frequency pageInformation="read" :frequency="javaCodeConventions.frequency"></Frequency>
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
              {{ javaCodeConventions.staticTitle }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>Sparrow 규칙</th>
            <td>
              {{ javaCodeConventions.sparrowTitle }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="javaCodeConventions.priority"></Priority>
              <strong>위배 빈도: </strong>
              <Frequency pageInformation="read" :frequency="javaCodeConventions.frequency"></Frequency>
              <strong>STATIC 규칙: </strong>
              {{ javaCodeConventions.staticTitle }}<br>
              <strong>Sparrow 규칙: </strong>
              {{ javaCodeConventions.sparrowTitle }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="javaCodeConventions.content"></div>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">Java Code Conventions 규칙 예제</th>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="rule-page-read" :link="'/java-code-conventions-example/list/' + idx" :exampleList="javaCodeConventionsCodeMirror" mode="text/x-java"></CodeMirror>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">Java Code Conventions 가이드라인</th>
          </tr>
          <tr>
            <td colspan="2">
              <GuidelineList :link="'/java-code-conventions-guideline/list/' + idx" :guidelineList="javaCodeConventionsGuidelineList"></GuidelineList>
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

      <Comment path="java-code-conventions-comments" idxName="javaCodeConventionsIdx" :idx="javaCodeConventions.idx" :commentList="javaCodeConventions.commentDtoList"></Comment>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/java-code-conventions/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="javaCodeConventions.access">
          <router-link :to="'/java-code-conventions/update/' + idx" class="me-2">
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
    let javaCodeConventions = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    let javaCodeConventionsCodeMirror = ref([]);
    let javaCodeConventionsGuidelineList = ref([]);
    // hashTags
    let hashTags = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("java-code-conventions");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions/read/" + idx,
          {},
      )
          .then((response) => {
            javaCodeConventions.value = response.data;
            javaCodeConventions.value.content = javaCodeConventions.value.content;

            // hashTags 설정
            hashTags.value = javaCodeConventions.value.hashTags;

            // misra c example, misra c guideline 설정
            javaCodeConventionsCodeMirror.value = javaCodeConventions.value.javaCodeConventionsExampleDtoList;
            for (let i = 0; i < javaCodeConventionsCodeMirror.value.length; i++) {
              javaCodeConventionsCodeMirror.value[i].link = '/java-code-conventions-example/read/from-rule-list/' + javaCodeConventionsCodeMirror.value[i].idx;
              javaCodeConventionsCodeMirror.value[i].nonCompliantExample = javaCodeConventionsCodeMirror.value[i].nonCompliantExample;
              javaCodeConventionsCodeMirror.value[i].compliantExample = javaCodeConventionsCodeMirror.value[i].compliantExample;
              javaCodeConventionsCodeMirror.value[i].badCasePositionList = JSON.parse(javaCodeConventionsCodeMirror.value[i].badCasePositionList);
              javaCodeConventionsCodeMirror.value[i].goodCasePositionList = JSON.parse(javaCodeConventionsCodeMirror.value[i].goodCasePositionList);
            }

            javaCodeConventionsGuidelineList.value = javaCodeConventions.value.javaCodeConventionsGuidelineDtoList;
            for (let i = 0; i < javaCodeConventionsGuidelineList.value.length; i++) {
              javaCodeConventionsGuidelineList.value[i].createdDate = dayjs(javaCodeConventionsGuidelineList.value[i].createdDate).format("YYYY.MM.DD.");
              javaCodeConventionsGuidelineList.value[i].link = "/java-code-conventions-guideline/read/from-rule-list/" + javaCodeConventionsGuidelineList.value[i].idx;
            }

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

            // 공통 데이터 설정
            javaCodeConventions.value.createdDate = dayjs(javaCodeConventions.value.createdDate).format("YYYY.MM.DD. HH:mm");
            javaCodeConventions.value.lastModifiedDate = dayjs(javaCodeConventions.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/java-code-conventions/delete-success");
                router.push("/java-code-conventions/list");
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
      javaCodeConventions, javaCodeConventionsCodeMirror, javaCodeConventionsGuidelineList, idx, hashTags, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>