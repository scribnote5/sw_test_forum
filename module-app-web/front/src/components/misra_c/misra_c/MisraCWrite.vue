<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C 규칙" :paths="['MISRA C', 'MISRA C 규칙 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C 규칙 정보</th>
          </tr>
          <tr>
            <th>규칙명<span class="required-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <div class="autoComplete_wrapper">
                <input type="text" name="title" id="title" v-model="title" class="form-control" placeholder="[02_01] 미달성 코드(unreachable code) 제거">
                <p id="titleErrorMessage" class="error-message"></p>
              </div>
            </td>
          </tr>
          <tr>
            <th>영어 규칙명<span class="required-field">*</span></th>
            <td>
              <input type="text" name="originalTitle" v-model="originalTitle" class="form-control" placeholder="[02_01] A project shall not contain unreachable code">
              <p id="originalTitleErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>우선순위<span class="required-field">*</span></th>
            <td>
              <Priority pageInformation="write" :priority=Number(priority) :maxPriority=Number(6) :priorityArray=priorityArray></Priority>
            </td>
          </tr>
          <tr>
            <th>위배 빈도<span class="required-field">*</span></th>
            <td>
              <Frequency :frequency=frequency pageInformation="write"></Frequency>
            </td>
          </tr>
          <tr>
            <th>해시태그<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <HashTags pageInformation="write"></HashTags>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C 표기 방식</th>
          </tr>
          <tr>
            <th>Category<span class="recommended-field">*</span></th>
            <td>
              <select v-model="category" class="form-select">
                <option value="MANDATORY">Mandatory</option>
                <option value="REQUIRED">Required</option>
                <option value="ADVISORY">Advisory</option>
              </select>
              <p id="categoryErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>Scope<span class="recommended-field">*</span></th>
            <td>
              <select v-model="scope" class="form-select">
                <option value="SYSTEM">System</option>
                <option value="TRANSLATION_UNIT">Translation Unit</option>
              </select>
              <p id="scopeErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>Decidability<span class="recommended-field">*</span></th>
            <td>
              <select v-model="decidability" class="form-select">
                <option value="DECIDEABLE">Decidable</option>
                <option value="UNDECIDEABLE">Undecidable</option>
              </select>
              <p id="decidabilityErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>Applies to<span class="recommended-field">*</span></th>
            <td>
              <input type="checkbox" id="C90" v-model="appliesTo" value="C90" class="form-check-input">
              <label for="C90" class="form-check-label ms-1 me-3">C90</label>
              <input type="checkbox" id="C99" v-model="appliesTo" value="C99" class="form-check-input">
              <label for="C99" class="form-check-label ms-1 me-3">C99</label>
              <input type="checkbox" id="C11" v-model="appliesTo" value="C11" class="form-check-input">
              <label for="C11" class="form-check-label ms-1 me-3">C11</label>
              <p id="appliesToErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>QAC 규칙<span class="recommended-field">*</span></th>
            <td>
              <input type="text" name="qacTitle" v-model="qacTitle" class="form-control" placeholder="MISRA C 규칙과 매핑되는 QAC 규칙을 작성해주세요.">
              <p id="qacTitleErrorMessage" class="error-message"></p>
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
          <router-link :to="'/misra-c/list'">
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
import Frequency from '@/components/common/Frequency.vue'
import HashTags from '@/components/common/HashTags.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from "axios";
// plugins
import {createAutoComplete, createHashTagsAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {updatePriorityValue} from "@/assets/plugins/priority/priority";
import {updateFrequencyValue} from "@/assets/plugins/frequency/frequency";
import {updateHashTagsValue} from "@/assets/plugins/hash-tags/hash-tags";
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorRuleData} from "@/assets/plugins/ckeditor/ckeditor-init"
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
    Frequency,
    HashTags,
    FileUpload,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorRuleData;
    const vueEditorConfig = editorConfig;
    // variable
    const title = ref("");
    const originalTitle = ref("");
    const priority = ref(6);
    const frequency = ref("AVERAGE");
    const hashTags = ref("");
    const category = ref("MANDATORY");
    const scope = ref("SYSTEM");
    const decidability = ref("DECIDEABLE");
    const appliesTo = ref([]);
    const qacTitle = ref("");
    const activeStatus = ref("ACTIVE");
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createHashTagsAutoComplete(response.data.autoCompleteHashTags, "hashTag");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/misra-c/list/");
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/priority-list-write",
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

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/" + idx,
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
      let misraCIdx;
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // frequency 값 초기화
      frequency.value = updateFrequencyValue();

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      if (!(validateLengthAndIsEmpty("title", title.value, 255)
          && validateLengthAndIsEmpty("originalTitle", originalTitle.value, 255)
          && validateLength("hashTags", hashTags.value, 255)
          && validateLength("category", category.value, 255)
          && validateLength("scope", scope.value, 255)
          && validateLength("decidability", decidability.value, 255)
          && validateLength("qacTitle", qacTitle.value, 2048)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c",
          {
            title: title.value,
            originalTitle: originalTitle.value,
            priority: priority.value,
            frequency: frequency.value,
            hashTags: hashTags.value,
            category: category.value,
            scope: scope.value,
            decidability: decidability.value,
            appliesTo: appliesTo.value.join(", "),
            qacTitle: qacTitle.value,
            content: vueEditorData,
            activeStatus: activeStatus.value
          },
      )
          .then((response) => {
            misraCIdx = response.data;
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
        formData.append("misraCIdx", misraCIdx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/attached-file",
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
              deletePost(misraCIdx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/misra-c/write-success");
      router.push("/misra-c/read/" + misraCIdx);
    }
    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      title, originalTitle, priority, frequency, hashTags, category, scope, decidability, appliesTo, qacTitle, activeStatus,
      priorityArray,

      // function
      validateLengthAndIsEmpty, onEditorReady, validateEditor,
      writePost
    }
  }
};
</script>