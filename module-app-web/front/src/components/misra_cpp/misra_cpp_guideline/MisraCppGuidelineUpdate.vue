<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C++ 가이드라인" :subPage="misraCppRule" :paths="['MISRA C++', 'MISRA C++ 가이드라인 수정']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C++ 가이드라인 정보</th>
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
              <HashTags pageInformation="update" :hash-tags="hashTags"></HashTags>
            </td>
          </tr>
          <tr>
            <th>프로젝트 정보<span class="recommended-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="projectName" v-model="projectName" class="form-control" placeholder="소나센서체계">
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

              <input type="text" name="guidelineResultNote" v-model="guidelineResultNote" class="form-control" placeholder="가이드라인 결과 비고">
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
              <input type="text" name="compilerName" v-model="compilerName" class="form-control" placeholder="G++ 4.3">
              <p id="compilerNameErrorMessage" class="error-message"></p>

              <input type="text" name="compilerNote" v-model="compilerNote" class="form-control" placeholder="Qt 5.15">
              <p id="compilerNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="update" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-c++src"></CodeMirror>
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
            <th colspan="2" class="sub-item-title">MISRA C++ 가이드라인 부가정보</th>
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
          <router-link :to="'/misra-cpp-guideline/list/' + misraCppIdx">
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
import HashTags from '@/components/common/HashTags.vue'
import CodeMirror from '@/components/common/CodeMirror.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {createAutoComplete, createHashTagsAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updateHashTagsValue} from "@/assets/plugins/hash-tags/hash-tags";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig} from "@/assets/plugins/ckeditor/ckeditor-init"
import {codeMirror} from "@/assets/plugins/code-mirror/code-mirror";
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import {confirm} from '@/assets/plugins/sweetalert2/sweetalert2';
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
    const idx = route.params.idx;
    // CKEditor
    const vueEditor = editor;
    let vueEditorData = ref();
    const vueEditorConfig = editorConfig;
    // variable
    let misraCppIdx = ref("");
    let title = ref("");
    let hashTags = ref("");
    let hashTagsIdx = ref(0);
    let projectName = ref("");
    let projectInformationIdx = ref(0);
    let guidelineResult = ref("");
    let guidelineResultNote = ref("");
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
    // misra cpp rule
    let misraCppRule = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/form/" + idx,
          {},
      )
          .then((response) => {
            misraCppIdx.value = response.data.misraCppIdx;
            title.value = response.data.title;
            hashTags.value = response.data.hashTags;
            hashTagsIdx.value = response.data.hashTagsIdx;
            projectName.value = response.data.projectName;
            projectInformationIdx.value = response.data.projectInformationIdx;
            guidelineResult.value = response.data.guidelineResult;
            guidelineResultNote.value = response.data.guidelineResultNote;
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
            createHashTagsAutoComplete(response.data.autoCompleteHashTags, "hashTag");
            createAutoComplete(response.data.autoCompleteProjectName, "projectName", projectName);
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

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = response.data.attachedFileList;
            uploadedAttachedFileList.value.totalFileSize = 0;
            for (const uploadedAttachedFile of uploadedAttachedFileList.value) {
              uploadedAttachedFileList.value.totalFileSize += uploadedAttachedFile.fileSize;
              uploadedAttachedFile.convertedFileSize = convertFileSize(uploadedAttachedFile.fileSize);
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp/misra-cpp-rule/" + misraCppIdx.value,
          {},
      )
          .then((response) => {
            misraCppRule.value = response.data;
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

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      // CodeMirror 값 초기화
      nonCompliantExample.value = isEmpty(codeMirror.getNonCompliantExample()) ? nonCompliantExample.value : codeMirror.getNonCompliantExample();
      compliantExample.value = isEmpty(codeMirror.getCompliantExample()) ? compliantExample.value : codeMirror.getCompliantExample();
      badCasePositionList.value = isEmpty(codeMirror.getBadCasePositionList()) ? badCasePositionList.value : JSON.stringify(codeMirror.getBadCasePositionList());
      goodCasePositionList.value = isEmpty(codeMirror.getGoodCasePositionList()) ? goodCasePositionList.value : JSON.stringify(codeMirror.getGoodCasePositionList());

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

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/" + idx,
          {
            misraCppIdx: misraCppIdx.value,
            title: title.value,
            hashTags: hashTags.value,
            hashTagsIdx: hashTagsIdx.value,
            projectName: projectName.value,
            projectInformationIdx: projectInformationIdx.value,
            guidelineResult: guidelineResult.value,
            guidelineResultNote: guidelineResultNote.value,
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

      // deleteFileArray 배열 요소가 null인 경우 삭제
      fileUpload.deleteFileArray = deleteArrayIndexIsEmpty(fileUpload.deleteFileArray);

      // 첨부 파일 삭제
      if (!isEmpty(fileUpload.deleteFileArray)) {
        isExit = await axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/attached-file/", {
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
        formData.append("misraCppGuidelineIdx", idx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/attached-file",
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

      localStorage.setItem("result", "/misra-cpp-guideline/update-success");
      router.push("/misra-cpp-guideline/read/from-rule-list/" + idx);
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/misra-cpp-guideline/delete-success");
                router.push("/misra-cpp-guideline/list/" + misraCppIdx);
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
      misraCppIdx, title, hashTags, projectName, guidelineResult, guidelineResultNote, toolName, toolNote, compilerName, compilerNote, nonCompliantExample, compliantExample, badCasePositionList, goodCasePositionList,
      misraCppRule,

      // function
      onEditorReady, validateEditor,
      updatePost, deletePost
    }
  }
}
;
</script>