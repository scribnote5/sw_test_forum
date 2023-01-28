<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="환경 설정" :paths="['도구', '환경 설정 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">환경 설정 정보</th>
          </tr>
          <tr>
            <th>제목<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="title" id="title" v-model="title" class="form-control">
              <p id="titleErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>우선순위<span class="required-field">*</span></th>
            <td>
              <Priority pageInformation="write" :priority=Number(priority) :maxPriority=Number(6) :priorityArray=priorityArray></Priority>
            </td>
          </tr>
          <tr>
            <th>해시태그<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <HashTags pageInformation="write"></HashTags>
            </td>
          </tr>
          <tr>
            <th>대상 도구<span class="required-field">*</span></th>
            <td>
              <select v-model="targetToolName" class="form-select">
                <option value="STATIC">STATIC</option>
                <option value="COVER">COVER</option>
                <option value="CONTROLLER_TESTER">Controller Tester</option>
              </select>
            </td>
          </tr>
          <tr>
            <th>도구 정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="toolName" id="toolName" v-model="toolName" class="form-control" placeholder="STATIC 4.3.2">
              <p id="toolNameErrorMessage" class="error-message"></p>

              <input type="text" name="toolNote" id="toolNote" v-model="toolNote" class="form-control" placeholder="STATIC Analysis Agent v1.2.2 p4">
              <p id="toolNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>개발 환경<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="developmentEnvironmentName" id="developmentEnvironmentName" v-model="developmentEnvironmentName" class="form-control" placeholder="Windows 10 64bit">
              <p id="developmentEnvironmentNameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>개발 언어<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td>
              <select v-model="programmingLanguage" class="form-select">
                <option value="C">C</option>
                <option value="C_CPP">C++</option>
                <option value="C_SHARP">C#</option>
                <option value="JAVA">Java</option>
              </select>
            </td>
          </tr>
          <tr>
            <th>IDE 정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="ideName" id="ideName" v-model="ideName" class="form-control" placeholder="Visual Studio 2017">
              <p id="ideNameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>컴파일러<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="compilerName" v-model="compilerName" class="form-control" placeholder="Visual Studio 2017">
              <p id="compilerNameErrorMessage" class="error-message"></p>

              <input type="text" name="compilerNote" v-model="compilerNote" class="form-control" placeholder="Qt 5.15">
              <p id="compilerNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>

          <tr>
            <th>
              시험 환경<span class="recommended-field">*</span>
            </th>
            <td>
              <strong>호스트 환경:</strong> <input type="radio" v-model="operationEnvironment" value="HOST" checked @change="environmentChange()"> &nbsp;&nbsp;
              <strong>타겟 환경:</strong> <input type="radio" v-model="operationEnvironment" value="TARGET" @change="environmentChange()">
            </td>
          </tr>
          <tr id="target-information-sub-title" style="display: none">
            <th colspan="2" class="sub-item-title">타겟 환경</th>
          </tr>
          <tr id="target-environment-information" style="display: none">
            <th>타겟 환경<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="targetEnvironmentName" id="targetEnvironmentName" v-model="targetEnvironmentName" class="form-control" placeholder="VxWorks 6.9 64bit">
              <p id="targetEnvironmentNameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr id="target-board-information" style="display: none">
            <th>
              타겟 보드<span class="recommended-field">*</span><span class="auto-completed-field">*</span><br>
              (보드명, 아키텍처)
            </th>
            <td style="overflow: visible">
              <input type="text" name="boardName" id="boardName" v-model="boardName" class="form-control" placeholder="VPX3-1257">
              <p id="boardNameErrorMessage" class="error-message"></p>

              <input type="text" name="architecture" id="architecture" v-model="architecture" class="form-control" placeholder="Intel">
              <p id="architectureErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr id="target-communication-information" style="display: none">
            <th>
              타겟 통신<span class="recommended-field">*</span><span class="auto-completed-field">*</span><br>
              (인터페이스, 디버거명)
            </th>
            <td style="overflow: visible">
              <input type="text" name="interfaceMethod" id="interfaceMethod" v-model="interfaceMethod" class="form-control" placeholder="JTAG">
              <p id="interfaceMethodErrorMessage" class="error-message"></p>

              <input type="text" name="debuggerName" id="debuggerName" v-model="debuggerName" class="form-control" placeholder="ST-Link/V2">
              <p id="debuggerNameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr id="target-software-information" style="display: none">
            <th>
              개발 소프트웨어 정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span><br>
              (실행 파일 크기, 비트, RAM 사용량)
            </th>
            <td style="overflow: visible">
              <input type="text" name="executableSize" id="executableSize" v-model="executableSize" class="form-control" placeholder="1130KB">
              <p id="executableSizeErrorMessage" class="error-message"></p>

              <select v-model="bit" class="form-select mb-1">
                <option value="64BIT">64bit</option>
                <option value="32BIT">32bit</option>
                <option value="16BIT">16bit</option>
                <option value="8BIT">8bit</option>
              </select>

              <input type="text" name="ramUsage" id="ramUsage" v-model="ramUsage" class="form-control" placeholder="1GB">
              <p id="ramUsageErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr id="target-free-size-information" style="display: none">
            <th>
              소프트웨어 탑재 후 여유 공간<span class="recommended-field">*</span><span class="auto-completed-field">*</span><br>
              (RAM 여유 공간, FLASH 여유 공간)
            </th>
            <td style="overflow: visible">
              <input type="text" name="ramFreeSize" id="ramFreeSize" v-model="ramFreeSize" class="form-control" placeholder="7GB">
              <p id="ramFreeSizeErrorMessage" class="error-message"></p>

              <input type="text" name="flashFreeSize" id="flashFreeSize" v-model="flashFreeSize" class="form-control" placeholder="7GB">
              <p id="flashFreeSizeErrorMessage" class="error-message"></p>
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
          <router-link :to="'/tool-configuration/list'">
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
import HashTags from '@/components/common/HashTags.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// plugins
import {createAutoComplete, createHashTagsAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updatePriorityValue} from "@/assets/plugins/priority/priority";
import {updateHashTagsValue} from "@/assets/plugins/hash-tags/hash-tags";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorToolConfigurationData} from "@/assets/plugins/ckeditor/ckeditor-init"
import {fileUpload} from '@/assets/plugins/file-upload/file-upload';
// utils
import {deleteArrayIndexIsEmpty, parseApiErrorMsg, onEditorReady, validateEditor, validateLength, validateLengthAndIsEmpty} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    HashTags,
    FileUpload,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorToolConfigurationData;
    const vueEditorConfig = editorConfig;
    // variable
    const title = ref("");
    const priority = ref(6);
    const hashTags = ref("");
    const targetToolName = ref("STATIC");
    const toolName = ref("");
    const toolNote = ref("");
    const developmentEnvironmentName = ref("");
    const programmingLanguage = ref("C");
    const ideName = ref("");
    const compilerName = ref("");
    const compilerNote = ref("");
    const operationEnvironment = ref("HOST");
    const targetEnvironmentName = ref("");
    const boardName = ref("");
    const architecture = ref("");
    const interfaceMethod = ref("");
    const debuggerName = ref("");
    const executableSize = ref("");
    const bit = ref("64BIT");
    const ramUsage = ref("");
    const ramFreeSize = ref("");
    const flashFreeSize = ref("");
    const activeStatus = ref("ACTIVE");
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createHashTagsAutoComplete(response.data.autoCompleteHashTags, "hashTag");
            createAutoComplete(response.data.autoCompleteToolName, "toolName", toolName);
            createAutoComplete(response.data.autoCompleteToolNote, "toolNote", toolNote);
            createAutoComplete(response.data.autoCompleteDevelopmentEnvironmentName, "developmentEnvironmentName", developmentEnvironmentName);
            createAutoComplete(response.data.autoCompleteIdeName, "ideName", ideName);
            createAutoComplete(response.data.autoCompleteCompilerName, "compilerName", compilerName);
            createAutoComplete(response.data.autoCompleteCompilerNote, "compilerNote", compilerNote);
            createAutoComplete(response.data.autoCompleteTargetEnvironmentName, "targetEnvironmentName", targetEnvironmentName);
            createAutoComplete(response.data.autoCompleteBoardName, "boardName", boardName);
            createAutoComplete(response.data.autoCompleteArchitecture, "architecture", architecture);
            createAutoComplete(response.data.autoCompleteInterfaceMethod, "interfaceMethod", interfaceMethod);
            createAutoComplete(response.data.autoCompleteDebuggerName, "debuggerName", debuggerName);
            createAutoComplete(response.data.autoCompleteExecutableSize, "executableSize", executableSize);
            createAutoComplete(response.data.autoCompleteRamUsage, "ramUsage", ramUsage);
            createAutoComplete(response.data.autoCompleteRamFreeSize, "ramFreeSize", ramFreeSize);
            createAutoComplete(response.data.autoCompleteFlashFreeSize, "flashFreeSize", flashFreeSize);

          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/tool-configuration/list/");
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/priority-list-write",
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
    });

    /* operationEnvironment 변경될 때 */
    const environmentChange = () => {
      if (operationEnvironment.value === 'HOST') {
        document.getElementById("target-information-sub-title").style.display = "none";
        document.getElementById("target-environment-information").style.display = "none";
        document.getElementById("target-board-information").style.display = "none";
        document.getElementById("target-communication-information").style.display = "none";
        document.getElementById("target-software-information").style.display = "none";
        document.getElementById("target-free-size-information").style.display = "none";
      } else {
        document.getElementById("target-information-sub-title").style.display = "table-row";
        document.getElementById("target-environment-information").style.display = "table-row";
        document.getElementById("target-board-information").style.display = "table-row";
        document.getElementById("target-communication-information").style.display = "table-row";
        document.getElementById("target-software-information").style.display = "table-row";
        document.getElementById("target-free-size-information").style.display = "table-row";

        // 값 초기화
        targetEnvironmentName.value = "";
        boardName.value = "";
        architecture.value = "";
        interfaceMethod.value = "";
        debuggerName.value = "";
        executableSize.value = "";
        bit.value = "64BIT";
        ramUsage.value = "";
        ramFreeSize.value = "";
        flashFreeSize.value = "";
      }
    }

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/" + idx,
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
      let toolConfigurationIdx;
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      if (!(validateLengthAndIsEmpty("title", title.value, 255)
          && validateLength("hashTags", hashTags.value, 255)
          && validateLength("toolName", toolName.value, 255)
          && validateLength("toolNote", toolNote.value, 255)
          && validateLength("developmentEnvironmentName", developmentEnvironmentName.value, 255)
          && validateLength("ideName", ideName.value, 255)
          && validateLength("compilerName", compilerName.value, 255)
          && validateLength("compilerNote", compilerNote.value, 255)
          && validateLength("targetEnvironmentName", targetEnvironmentName.value, 255)
          && validateLength("boardName", boardName.value, 255)
          && validateLength("architecture", architecture.value, 255)
          && validateLength("interfaceMethod", interfaceMethod.value, 255)
          && validateLength("debuggerName", debuggerName.value, 255)
          && validateLength("executableSize", executableSize.value, 255)
          && validateLength("ramUsage", ramUsage.value, 255)
          && validateLength("ramFreeSize", ramFreeSize.value, 255)
          && validateLength("flashFreeSize", flashFreeSize.value, 255)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations",
          {
            title: title.value,
            priority: priority.value,
            hashTags: hashTags.value,
            targetToolName: targetToolName.value,
            toolName: toolName.value,
            toolNote: toolNote.value,
            developmentEnvironmentName: developmentEnvironmentName.value,
            programmingLanguage: programmingLanguage.value,
            ideName: ideName.value,
            compilerName: compilerName.value,
            compilerNote: compilerNote.value,
            operationEnvironment: operationEnvironment.value,
            targetEnvironmentName: targetEnvironmentName.value,
            boardName: boardName.value,
            architecture: architecture.value,
            interfaceMethod: interfaceMethod.value,
            debuggerName: debuggerName.value,
            executableSize: executableSize.value,
            bit: bit.value,
            ramUsage: ramUsage.value,
            ramFreeSize: ramFreeSize.value,
            flashFreeSize: flashFreeSize.value,
            content: vueEditorData,
            activeStatus: activeStatus.value
          },
      )
          .then((response) => {
            toolConfigurationIdx = response.data;
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
        formData.append("toolConfigurationIdx", toolConfigurationIdx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/attached-file",
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
              deletePost(toolConfigurationIdx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/tool-configuration/write-success");
      router.push("/tool-configuration/read/" + toolConfigurationIdx);
    }
    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      title,
      priority,
      hashTags,
      targetToolName,
      toolName,
      toolNote,
      developmentEnvironmentName,
      programmingLanguage,
      ideName,
      compilerName,
      compilerNote,
      operationEnvironment,
      targetEnvironmentName,
      boardName,
      architecture,
      interfaceMethod,
      debuggerName,
      executableSize,
      bit,
      ramUsage,
      ramFreeSize,
      flashFreeSize,
      activeStatus,
      priorityArray,

      // function
      environmentChange,
      validateLengthAndIsEmpty,
      validateEditor,
      writePost
    }
  }
};
</script>