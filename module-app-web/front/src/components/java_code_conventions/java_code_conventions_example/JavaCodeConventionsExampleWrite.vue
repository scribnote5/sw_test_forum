<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Java Code Conventions 예제 코드" :subPage="javaCodeConventionsRule" :paths="['Java Code Conventions', 'Java Code Conventions 예제 코드 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">Java Code Conventions 예제 코드 정보</th>
          </tr>
          <tr>
            <th>제목<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="title" v-model="title" class="form-control">
              <p id="titleErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>우선순위<span class="required-field">*</span></th>
            <td>
              <Priority pageInformation="write" :priority=Number(priority) :maxPriority=Number(4) :priorityArray=priorityArray></Priority>
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
            <th>컴파일러 및 기타정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
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

          <input type="hidden" v-model="activeStatus"/>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/java-code-conventions-example/list/' + javaCodeConventionsIdx">
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
import Priority from '@/components/common/Priority.vue'
import CodeMirror from '@/components/common/CodeMirror.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// plugins
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updatePriorityValue} from "@/assets/plugins/priority/priority";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorExampleData} from "@/assets/plugins/ckeditor/ckeditor-init"
import {codeMirror} from "@/assets/plugins/code-mirror/code-mirror";
// utils
import {parseApiErrorMsg, onEditorReady, validateEditor, validateLength, validateLengthAndIsEmpty, validateSizeByEditor} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    CodeMirror,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorExampleData;
    const vueEditorConfig = editorConfig;
    // variable
    const javaCodeConventionsIdx = ref(route.params.javaCodeConventionsIdx);
    const title = ref("");
    const priority = ref(4);
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
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-examples/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createAutoComplete(response.data.autoCompleteToolName, "toolName", toolName);
            createAutoComplete(response.data.autoCompleteToolNote, "toolNote", toolNote);
            createAutoComplete(response.data.autoCompleteCompilerName, "compilerName", compilerName);
            createAutoComplete(response.data.autoCompleteCompilerNote, "compilerNote", compilerNote);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/java-code-conventions-example/list/");
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-examples/priority-list-write/" + javaCodeConventionsIdx.value,
          {},
      )
          .then((response) => {
            priorityArray.value = response.data;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
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
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-examples/" + idx,
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
      let javaCodeConventionsExampleIdx;
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // CodeMirror 값 초기화
      nonCompliantExample.value = codeMirror.getNonCompliantExample();
      compliantExample.value = codeMirror.getCompliantExample();
      badCasePositionList.value = JSON.stringify(codeMirror.getBadCasePositionList());
      goodCasePositionList.value = JSON.stringify(codeMirror.getGoodCasePositionList());

      if (!(validateLengthAndIsEmpty("title", title.value, 255)
          && validateLength("toolName", toolName.value, 255)
          && validateLength("toolNote", toolNote.value, 255)
          && validateLength("compilerName", compilerName.value, 255)
          && validateLength("compilerNote", compilerNote.value, 255)
          && validateSizeByEditor("nonCompliantExample", nonCompliantExample.value)
          && validateSizeByEditor("compliantExample", compliantExample.value)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-examples",
          {
            javaCodeConventionsIdx: javaCodeConventionsIdx.value,
            title: title.value,
            priority: priority.value,
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
            javaCodeConventionsExampleIdx = response.data;
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

      localStorage.setItem("result", "/java-code-conventions-example/write-success");
      router.push("/java-code-conventions-example/read/from-rule-list/" + javaCodeConventionsExampleIdx);
    }
    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      javaCodeConventionsIdx, title, priority, toolName, toolNote, compilerName, compilerNote, nonCompliantExample, compliantExample, activeStatus,
      javaCodeConventionsRule, priorityArray,

      // function
      validateLengthAndIsEmpty, onEditorReady, validateEditor,
      writePost
    }
  }
};
</script>