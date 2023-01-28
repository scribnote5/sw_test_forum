<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="환경 설정" :paths="['도구', '환경 설정 수정']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>

          <tr>
            <th colspan="2" class="sub-item-title">환경 설정 정보</th>
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
              <Priority pageInformation="update" :priority=Number(priority) :maxPriority=Number(6) :priorityArray=priorityArray></Priority>
            </td>
          </tr>
          <tr>
            <th>해시태그<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <HashTags pageInformation="update" :hash-tags="hashTags"></HashTags>
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
              <input type="text" name="toolName" v-model="toolName" class="form-control" placeholder="STATIC 4.3.2">
              <p id="toolNameErrorMessage" class="error-message"></p>

              <input type="text" name="toolNote" v-model="toolNote" class="form-control" placeholder="STATIC Analysis Agent v1.2.2 p4">
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
              <input type="text" name="ideName" v-model="ideName" class="form-control" placeholder="Visual Studio 2017">
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
              <strong>호스트 환경:</strong> <input type="radio" v-model="operationEnvironment" value="HOST" @change="environmentChange()"> &nbsp;&nbsp;
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
              <FileUpload pageInformation="update" :uploadedAttachedFileList="uploadedAttachedFileList"></FileUpload>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">도구 설정 부가정보</th>
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
          <router-link :to="'/tool-configuration/list'">
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
import HashTags from '@/components/common/HashTags.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {createAutoComplete, createHashTagsAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updatePriorityValue} from "@/assets/plugins/priority/priority";
import {updateHashTagsValue} from "@/assets/plugins/hash-tags/hash-tags";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig} from "@/assets/plugins/ckeditor/ckeditor-init"
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import {confirm} from '@/assets/plugins/sweetalert2/sweetalert2';
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
    const idx = route.params.idx;
    // CKEditor
    const vueEditor = editor;
    let vueEditorData = ref();
    const vueEditorConfig = editorConfig;
    // variable
    let title = ref("");
    let hashTags = ref("");
    let hashTagsIdx = ref(0);
    let priority = ref(0);
    let targetToolName = ref("");
    let toolName = ref("");
    let toolNote = ref("");
    let developmentEnvironmentName = ref("");
    let developmentEnvironmentInformationIdx = ref(0);
    let programmingLanguage = ref("");
    let toolInformationIdx = ref(0);
    let ideName = ref("");
    let ideInformationIdx = ref(0);
    let compilerName = ref("");
    let compilerNote = ref("");
    let compilerInformationIdx = ref(0);
    let operationEnvironment = ref("");
    let targetEnvironmentName = ref("");
    let targetEnvironmentInformationIdx = ref(0);
    let boardName = ref("");
    let architecture = ref("");
    let interfaceMethod = ref("");
    let debuggerName = ref("");
    let executableSize = ref("");
    let bit = ref("64BIT");
    let ramUsage = ref("");
    let ramFreeSize = ref("");
    let flashFreeSize = ref("");
    let targetInformationIdx = ref(0);
    let activeStatus = ref("");
    let createdByUser = ref([]);
    let createdDate = ref("");
    let lastModifiedByUser = ref([]);
    let lastModifiedDate = ref("");
    // priority array
    let priorityArray = ref([]);
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/form/" + idx,
          {},
      )
          .then((response) => {
            title.value = response.data.title;
            priority.value = response.data.priority;
            hashTags.value = response.data.hashTags;
            hashTagsIdx.value = response.data.hashTagsIdx;
            targetToolName.value = response.data.targetToolName;
            toolName.value = response.data.toolName;
            toolNote.value = response.data.toolNote;
            developmentEnvironmentName.value = response.data.developmentEnvironmentName;
            developmentEnvironmentInformationIdx.value = response.data.developmentEnvironmentInformationIdx;
            programmingLanguage.value = response.data.programmingLanguage;
            toolInformationIdx.value = response.data.toolInformationIdx;
            ideName.value = response.data.ideName;
            ideInformationIdx.value = response.data.ideInformationIdx;
            compilerName.value = response.data.compilerName;
            compilerNote.value = response.data.compilerNote;
            compilerInformationIdx.value = response.data.compilerInformationIdx;
            operationEnvironment.value = response.data.operationEnvironment;
            targetEnvironmentName.value = response.data.targetEnvironmentName;
            targetEnvironmentInformationIdx.value = response.data.targetEnvironmentInformationIdx;
            boardName.value = response.data.boardName;
            architecture.value = response.data.architecture;
            interfaceMethod.value = response.data.interfaceMethod;
            debuggerName.value = response.data.debuggerName;
            executableSize.value = response.data.executableSize;
            bit.value = response.data.bit;
            ramUsage.value = response.data.ramUsage;
            ramFreeSize.value = response.data.ramFreeSize;
            flashFreeSize.value = response.data.flashFreeSize;
            targetInformationIdx.value = response.data.targetInformationIdx;
            vueEditorData.value = response.data.content;

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

            // 공통 데이터 설정
            activeStatus.value = response.data.activeStatus;
            createdByUser.value = response.data.createdByUser;
            createdDate.value = dayjs(response.data.createdDate).format("YYYY.MM.DD. HH:mm");
            lastModifiedByUser.value = response.data.lastModifiedByUser;
            lastModifiedDate.value = dayjs(response.data.lastModifiedDate).format("YYYY.MM.DD. HH:mm");

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = response.data.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);

            // 시험 환경 값에 따른 데이터 출력 여부부
            environmentChange()
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/priority-list-update/" + idx,
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
      }
    }

    /* 수정 */
    const updatePost = async () => {
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

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/" + idx,
          {
            title: title.value,
            priority: priority.value,
            hashTags: hashTags.value,
            hashTagsIdx: hashTagsIdx.value,
            targetToolName: targetToolName.value,
            toolName: toolName.value,
            toolNote: toolNote.value,
            toolInformationIdx: toolInformationIdx.value,
            developmentEnvironmentName: developmentEnvironmentName.value,
            developmentEnvironmentInformationIdx: developmentEnvironmentInformationIdx.value,
            programmingLanguage: programmingLanguage.value,
            ideName: ideName.value,
            ideInformationIdx: ideInformationIdx.value,
            compilerName: compilerName.value,
            compilerNote: compilerNote.value,
            compilerInformationIdx: compilerInformationIdx.value,
            operationEnvironment: operationEnvironment.value,
            targetEnvironmentName: targetEnvironmentName.value,
            targetEnvironmentInformationIdx: targetEnvironmentInformationIdx.value,
            boardName: boardName.value,
            architecture: architecture.value,
            interfaceMethod: interfaceMethod.value,
            debuggerName: debuggerName.value,
            executableSize: executableSize.value,
            bit: bit.value,
            ramUsage: ramUsage.value,
            ramFreeSize: ramFreeSize.value,
            flashFreeSize: flashFreeSize.value,
            targetInformationIdx: targetInformationIdx.value,
            content: vueEditorData.value,
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

      // deleteFileArray 배열 요소가 null인 경우 삭제
      fileUpload.deleteFileArray = deleteArrayIndexIsEmpty(fileUpload.deleteFileArray);

      // 첨부 파일 삭제
      if (!isEmpty(fileUpload.deleteFileArray)) {
        isExit = await axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/attached-file/", {
              data: fileUpload.deleteFileArray
            }
        )
            .then((response) => {
              isExit = false;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      // insertFileArray 배열 요소가 null인 경우 삭제
      fileUpload.insertFileArray = deleteArrayIndexIsEmpty(fileUpload.insertFileArray);

      // 첨부 파일 업로드
      if (!isEmpty(fileUpload.insertFileArray)) {
        const formData = new FormData();
        for (const insertFile of fileUpload.insertFileArray) {
          formData.append("files", insertFile);
        }
        formData.append("toolConfigurationIdx", idx);

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
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/tool-configuration/update-success");
      router.push("/tool-configuration/read/" + idx);
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/tool-configuration/delete-success");
                router.push("/tool-configuration/read/" + idx);
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
      createdByUser, createdDate, lastModifiedByUser, lastModifiedDate, activeStatus, uploadedAttachedFileList,
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
      priorityArray,

      // function
      environmentChange,
      validateEditor,
      updatePost, deletePost
    }
  }
}
;
</script>