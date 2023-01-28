<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Java Code Conventions 가이드라인" :subPage="javaCodeConventionsRule" :paths="['Java Code Conventions', 'Java Code Conventions 가이드라인 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">Java Code Conventions 가이드라인 정보</th>
          </tr>
          <tr>
            <th>제목<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="title" v-model="title" class="form-control">
              <p id="titleErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>해시태그<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <HashTags pageInformation="write"></HashTags>
            </td>
          </tr>
          <tr>
            <th>프로젝트 정보<span class="recommended-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="projectName" v-model="projectName" class="form-control" placeholder="FFX B3 MFR 신호처리">
              <p id="projectNameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>가이드라인 결과<span class="required-field">*</span></th>
            <td>
              <select v-model="guidelineResult" class="form-select">
                <option value="COMPLETED">수정 완료</option>
                <option value="EXCLUDE">사전 제외</option>
                <option value="EXCEPTION">예외 처리</option>
                <option value="FALSE_ALARM">도구 오탐</option>
                <option value="FALSE_ALARM_PATCHED">도구 오탐 패치 완료</option>
              </select>
              <p class="error-message"></p>

              <input type="text" name="guidelineResultNote" v-model="guidelineResultNote" class="form-control" placeholder="STATIC 4.3.2 패치 예정">
              <p id="guidelineResultNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>도구 정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="toolName" v-model="toolName" class="form-control" placeholder="STATIC 4.3.2">
              <p id="toolNameErrorMessage" class="error-message"></p>

              <input type="text" name="toolNote" v-model="toolNote" class="form-control" placeholder="STATIC Analysis Agent v1.2.2 p4">
              <p id="toolNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>컴파일러<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="compilerName" v-model="compilerName" class="form-control" placeholder="jdk-17.0.3">
              <p id="compilerNameErrorMessage" class="error-message"></p>

              <input type="text" name="compilerNote" v-model="compilerNote" class="form-control" placeholder="Android 11.0.0_r56">
              <p id="compilerNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="write" mode="text/x-java"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <ckeditor id="content" :editor="vueEditor" v-model="vueEditorData" :config="vueEditorConfig" @ready="onEditorReady" @blur="validateEditor"></ckeditor>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <FileUpload pageInformation="write"></FileUpload>
            </td>
          </tr>

          <input type="hidden" v-model="activeStatus"/>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/java-code-conventions-guideline/list/' + javaCodeConventionsIdx">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex">
          <button @click="writePost(vueEditorData)" class="btn btn-main-blue">작성<img :src="require(`@/assets/images/write-white.svg`)" class="ms-2"></button>
        </div>
      </div>
    </div>
  </section>
</template>

<style lang="scss">
@import '~@/assets/css/ckeditor.css';
@import '~@/assets/css/auto-complete.css';
</style>

<script>
// components
import Loading from '@/components/common/Loading.vue'
import Breadcrumb from '@/components/common/Breadcrumb.vue'
import HashTags from '@/components/common/HashTags.vue'
import CodeMirror from '@/components/common/CodeMirror.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// plugins
import {createAutoComplete, createHashTagsAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updateHashTagsValue} from "@/assets/plugins/hash-tags/hash-tags";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorGuidelineData} from "@/assets/plugins/ckeditor/ckeditor-init"
import {codeMirror} from "@/assets/plugins/code-mirror/code-mirror";
import {fileUpload} from '@/assets/plugins/file-upload/file-upload';
// utils
import {deleteArrayIndexIsEmpty, parseApiErrorMsg, onEditorReady, validateEditor, validateLength, validateLengthAndIsEmpty, validateSizeByEditor} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    HashTags,
    CodeMirror,
    FileUpload,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorGuidelineData;
    const vueEditorConfig = editorConfig;
    // variable
    const javaCodeConventionsIdx = ref(route.params.javaCodeConventionsIdx);
    const title = ref("");
    const hashTags = ref("");
    const projectName = ref("");
    const guidelineResult = ref("COMPLETED");
    const guidelineResultNote = ref("");
    const toolName = ref("");
    const toolNote = ref("");
    const compilerName = ref("");
    const compilerNote = ref("");
    const nonCompliantExample = ref("");
    const compliantExample = ref("");
    const badCasePositionList = ref("");
    const goodCasePositionList = ref("");
    const activeStatus = ref("ACTIVE");
    // misra c rule
    let javaCodeConventionsRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createHashTagsAutoComplete(response.data.autoCompleteHashTags, "hashTag");
            createAutoComplete(response.data.autoCompleteProjectName, "projectName", projectName);
            createAutoComplete(response.data.autoCompleteToolName, "toolName", toolName);
            createAutoComplete(response.data.autoCompleteToolNote, "toolNote", toolNote);
            createAutoComplete(response.data.autoCompleteCompilerName, "compilerName", compilerName);
            createAutoComplete(response.data.autoCompleteCompilerNote, "compilerNote", compilerNote);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/java-code-conventions-guideline/list/");
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions/java-code-conventions-rule/" + javaCodeConventionsIdx.value,
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
    });

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/" + idx,
          {},
      )
          .then((response) => {
          })
          .catch((error) => {
          })
          .then(() => {
          });
    }

    /* 작성 */
    const writePost = async (vueEditorData) => {
      let javaCodeConventionsGuidelineIdx;
      let isExit;

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      // CodeMirror 값 초기화
      nonCompliantExample.value = codeMirror.getNonCompliantExample();
      compliantExample.value = codeMirror.getCompliantExample();
      badCasePositionList.value = JSON.stringify(codeMirror.getBadCasePositionList());
      goodCasePositionList.value = JSON.stringify(codeMirror.getGoodCasePositionList());

      if (!(validateLengthAndIsEmpty("title", title.value, 255)
          && validateLength("hashTags", hashTags.value, 255)
          && validateLength("projectName", projectName.value, 255)
          && validateLength("guidelineResultNote", guidelineResultNote.value, 255)
          && validateLength("toolName", toolName.value, 255)
          && validateLength("toolNote", toolNote.value, 255)
          && validateLength("compilerName", compilerName.value, 255)
          && validateLength("compilerNote", compilerNote.value, 255)
          && validateSizeByEditor("nonCompliantExample", nonCompliantExample.value)
          && validateSizeByEditor("compliantExample", compliantExample.value)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines",
          {
            javaCodeConventionsIdx: javaCodeConventionsIdx.value,
            title: title.value,
            hashTags: hashTags.value,
            projectName: projectName.value,
            guidelineResult: guidelineResult.value,
            guidelineResultNote: guidelineResultNote.value,
            toolName: toolName.value,
            toolNote: toolNote.value,
            compilerName: compilerName.value,
            compilerNote: compilerNote.value,
            content: vueEditorData,
            nonCompliantExample: nonCompliantExample.value,
            compliantExample: compliantExample.value,
            badCasePositionList: badCasePositionList.value,
            goodCasePositionList: goodCasePositionList.value,
            activeStatus: activeStatus.value
          },
      )
          .then((response) => {
            javaCodeConventionsGuidelineIdx = response.data;
            isExit = false;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response.data);
            isExit = true;
          })
          .then(() => {
            return isExit;
          });

      if (isExit) return false;

      // insertFileArray 배열 요소가 null인 경우 삭제
      fileUpload.insertFileArray = deleteArrayIndexIsEmpty(fileUpload.insertFileArray);

      // 첨부 파일 업로드
      if (!isEmpty(fileUpload.insertFileArray)) {
        const formData = new FormData();
        for (const insertFile of fileUpload.insertFileArray) {
          formData.append("files", insertFile);
        }
        formData.append("javaCodeConventionsGuidelineIdx", javaCodeConventionsGuidelineIdx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/attached-file",
            formData, {
              headers: {
                "Content-Type": "multipart/form-data"
              }
            }
        )
            .then((response) => {
              isExit = false;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
              fileUpload.initFileUpload();
              document.getElementById("attachedFileList").innerHTML = "";
              document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.totalFileSize);
              deletePost(javaCodeConventionsGuidelineIdx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/java-code-conventions-guideline/write-success");
      router.push("/java-code-conventions-guideline/read/from-rule-list/" + javaCodeConventionsGuidelineIdx);
    }
    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      javaCodeConventionsIdx, title, hashTags, projectName, guidelineResult, guidelineResultNote, toolName, toolNote, compilerName, compilerNote, nonCompliantExample, compliantExample, activeStatus,
      javaCodeConventionsRule,

      // function
      validateLengthAndIsEmpty, onEditorReady, validateEditor,
      writePost
    }
  }
};
</script>