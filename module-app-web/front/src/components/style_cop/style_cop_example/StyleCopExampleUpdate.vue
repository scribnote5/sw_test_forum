<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="C# Coding Convention 예제 코드" :subPage="styleCopRule" :paths="['C# Coding Convention', 'C# Coding Convention 에제 코드 수정']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">C# Coding Convention 예제 코드 정보</th>
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
              <Priority pageInformation="update" :priority=Number(priority) :maxPriority=Number(4) :priorityArray=priorityArray></Priority>
            </td>
          </tr>
          <tr>
            <th>도구 정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="toolName" v-model="toolName" class="form-control" placeholder="STATIC 4.4.2">
              <p id="toolNameErrorMessage" class="error-message"></p>

              <input type="text" name="toolNote" v-model="toolNote" class="form-control" placeholder="STATIC Client for C# v1.2.8 p0">
              <p id="toolNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>컴파일러<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="compilerName" v-model="compilerName" class="form-control" placeholder=".NET Framework 4.7.2(Visual Studio 2017)">
              <p id="compilerNameErrorMessage" class="error-message"></p>

              <input type="text" name="compilerNote" v-model="compilerNote" class="form-control" placeholder="Winform">
              <p id="compilerNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="update" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-csharp"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <ckeditor id="content" :editor="vueEditor" v-model="vueEditorData" :config="vueEditorConfig" @ready="onEditorReady" @blur="validateEditor"></ckeditor>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">C# Coding Convention 가이드라인 부가정보</th>
          </tr>
          <tr>
            <th>작성자</th>
            <td>{{ createdByUser.department }} {{ createdByUser.name }}</td>
          </tr>
          <tr>
            <th>작성일</th>
            <td>{{ createdDate }}</td>
          </tr>
          <tr>
            <th>최종 수정자</th>
            <td>{{ lastModifiedByUser.department }} {{ lastModifiedByUser.name }}</td>
          </tr>
          <tr>
            <th>최종 수정일</th>
            <td>{{ lastModifiedDate }}</td>
          </tr>

          <input type="hidden" v-model="activeStatus"/>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/style-cop-example/list/' + styleCopIdx">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex">
          <button @click="updatePost()" class="btn btn-main-blue d-flex align-items-center me-2">수정 완료<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
          <button @click="deletePost()" class="btn btn-main-red d-flex align-items-center">삭제<img :src="require(`@/assets/images/delete-white.svg`)" class="ms-2"></button>
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
// day.js
import dayjs from 'dayjs'
// plugins
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updatePriorityValue} from "@/assets/plugins/priority/priority";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig} from "@/assets/plugins/ckeditor/ckeditor-init"
import {codeMirror} from "@/assets/plugins/code-mirror/code-mirror";
import {confirm} from '@/assets/plugins/sweetalert2/sweetalert2';
// utils
import {parseApiErrorMsg, onEditorReady, validateEditor, validateLength, validateLengthAndIsEmpty, validateSizeByEditor} from "@/utils/validation-util";
import {isEmpty} from "@/utils/empty-util";

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
    const idx = route.params.idx;
    // CKEditor
    const vueEditor = editor;
    let vueEditorData = ref();
    const vueEditorConfig = editorConfig;
    // variable
    let styleCopIdx = ref("");
    let title = ref("");
    let priority = ref(0);
    let toolName = ref("");
    let toolNote = ref("");
    let toolInformationIdx = ref(0);
    let compilerName = ref("");
    let compilerNote = ref("");
    let compilerInformationIdx = ref(0);
    let nonCompliantExample = ref("");
    let compliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    let activeStatus = ref("");
    let createdByUser = ref([]);
    let createdDate = ref("");
    let lastModifiedByUser = ref([]);
    let lastModifiedDate = ref("");
    // styleCop rule
    let styleCopRule = ref("");
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop-examples/form/" + idx,
          {},
      )
          .then((response) => {
            styleCopIdx.value = response.data.styleCopIdx;
            title.value = response.data.title;
            priority.value = response.data.priority;
            toolName.value = response.data.toolName;
            toolNote.value = response.data.toolNote;
            toolInformationIdx.value = response.data.toolInformationIdx;
            compilerName.value = response.data.compilerName;
            compilerNote.value = response.data.compilerNote;
            compilerInformationIdx.value = response.data.compilerInformationIdx;
            vueEditorData.value = response.data.content;
            nonCompliantExample.value = response.data.nonCompliantExample;
            compliantExample.value = response.data.compliantExample;
            badCasePositionList.value = response.data.badCasePositionList;
            goodCasePositionList.value = response.data.goodCasePositionList;

            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createAutoComplete(response.data.autoCompleteToolName, "toolName", toolName);
            createAutoComplete(response.data.autoCompleteToolNote, "toolNote", toolNote);
            createAutoComplete(response.data.autoCompleteCompilerName, "compilerName", compilerName);
            createAutoComplete(response.data.autoCompleteCompilerNote, "compilerNote", compilerNote);

            // 공통 데이터 설정
            activeStatus.value = response.data.activeStatus;
            createdByUser.value = response.data.createdByUser;
            createdDate.value = dayjs(response.data.createdDate).format("YYYY.MM.DD. HH:mm");
            lastModifiedByUser.value = response.data.lastModifiedByUser;
            lastModifiedDate.value = dayjs(response.data.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop-examples/priority-list-update/" + idx + "/" + styleCopIdx.value,
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

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop/style-cop-rule/" + styleCopIdx.value,
          {},
      )
          .then((response) => {
            styleCopRule.value = response.data;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    });

    /* 수정 */
    const updatePost = async () => {
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // CodeMirror 값 초기화
      nonCompliantExample.value = isEmpty(codeMirror.getNonCompliantExample()) ? nonCompliantExample.value : codeMirror.getNonCompliantExample();
      compliantExample.value = isEmpty(codeMirror.getCompliantExample()) ? compliantExample.value : codeMirror.getCompliantExample();
      badCasePositionList.value = isEmpty(codeMirror.getBadCasePositionList()) ? badCasePositionList.value : JSON.stringify(codeMirror.getBadCasePositionList());
      goodCasePositionList.value = isEmpty(codeMirror.getGoodCasePositionList()) ? goodCasePositionList.value : JSON.stringify(codeMirror.getGoodCasePositionList());

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

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop-examples/" + idx,
          {
            styleCopIdx: styleCopIdx.value,
            title: title.value,
            priority: priority.value,
            toolName: toolName.value,
            toolNote: toolNote.value,
            toolInformationIdx: toolInformationIdx.value,
            compilerName: compilerName.value,
            compilerNote: compilerNote.value,
            compilerInformationIdx: compilerInformationIdx.value,
            content: vueEditorData.value,
            nonCompliantExample: nonCompliantExample.value,
            compliantExample: compliantExample.value,
            badCasePositionList: badCasePositionList.value,
            goodCasePositionList: goodCasePositionList.value,
            activeStatus: activeStatus.value
          },
      )
          .then((response) => {
            isExit = false;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response.data);
            isExit = true;
          })
          .then(() => {
          });

      if (isExit) return false;

      localStorage.setItem("result", "/style-cop-example/update-success");
      router.push("/style-cop-example/read/from-rule-list/" + idx);
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop-examples/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/style-cop-example/delete-success");
                router.push("/style-cop-example/list/" + styleCopIdx.value);
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
      vueEditor, vueEditorData, vueEditorConfig,
      createdByUser, createdDate, lastModifiedByUser, lastModifiedDate, activeStatus,
      styleCopIdx, title, priority, toolName, toolNote, compilerName, compilerNote, nonCompliantExample, compliantExample, badCasePositionList, goodCasePositionList,
      styleCopRule, priorityArray,

      // function
      onEditorReady, validateEditor,
      updatePost, deletePost
    }
  }
}
;
</script>