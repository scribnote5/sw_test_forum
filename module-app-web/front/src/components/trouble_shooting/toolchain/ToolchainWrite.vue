<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Toolchain 트러블슈팅" :paths="['Toolchain', 'Toolchain 트러블슈팅 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">Toolchain 트러블슈팅 정보</th>
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
            <th>유형<span class="required-field">*</span></th>
            <td>
              <select v-model="toolCategory" class="form-select">
                <option value="INSTALL_SETTING">설치 및 설정</option>
                <option value="FUNCTION">기능</option>
                <option value="INFORMATION">정보</option>
                <option value="ETC">기타</option>
              </select>
            </td>
          </tr>
          <tr>
            <th>IDE 정보<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="ideName" v-model="ideName" class="form-control" placeholder="Code Composer Studio 11">
              <p id="ideNameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>컴파일러<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="compilerName" v-model="compilerName" class="form-control" placeholder="c6000">
              <p id="compilerNameErrorMessage" class="error-message"></p>

              <input type="text" name="compilerNote" v-model="compilerNote" class="form-control" placeholder="OpenCV 4.5.4">
              <p id="compilerNoteErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <ckeditor id="content" :editor="vueEditor" v-model="vueEditorData" :config="vueEditorConfig" @blur="validateEditor"></ckeditor>
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
          <router-link :to="'/toolchain/list'">
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
import {editor, editorConfig, editorTroubleShootingData} from "@/assets/plugins/ckeditor/ckeditor-init"
import {fileUpload} from '@/assets/plugins/file-upload/file-upload';
// utils
import {deleteArrayIndexIsEmpty, parseErrorMsg, validateEditor, validateLength, validateLengthAndIsEmpty} from "@/utils/validation-util";
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
    const vueEditorData = editorTroubleShootingData;
    const vueEditorConfig = editorConfig;
    // variable
    const title = ref("");
    const priority = ref(6);
    const hashTags = ref("");
    const toolCategory = ref("INSTALL_SETTING");
    const ideName = ref("");
    const compilerName = ref("");
    const compilerNote = ref("");
    const activeStatus = ref("ACTIVE");
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createHashTagsAutoComplete(response.data.autoCompleteHashTags, "hashTag");
            createAutoComplete(response.data.autoCompleteIdeName, "ideName", ideName);
            createAutoComplete(response.data.autoCompleteCompilerName, "compilerName", compilerName);
            createAutoComplete(response.data.autoCompleteCompilerNote, "compilerNote", compilerNote);
          })
          .catch((error) => {
            parseErrorMsg(error.response);
            router.push("/toolchain/list/");
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/priority-list-write",
          {},
      )
          .then((response) => {
            priorityArray.value = response.data;
          })
          .catch((error) => {
            parseErrorMsg(error.response);
          })
          .then(() => {
          });
    });

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/" + idx,
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
      let toolchainIdx;
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      if (!(validateLengthAndIsEmpty("title", title.value)
          && validateLength("hashTags", hashTags.value)
          && validateLength("ideName", ideName.value)
          && validateLength("compilerName", compilerName.value)
          && validateLength("compilerNote", compilerNote.value)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains",
          {
            title: title.value,
            priority: priority.value,
            hashTags: hashTags.value,
            toolCategory: toolCategory.value,
            ideName: ideName.value,
            compilerName: compilerName.value,
            compilerNote: compilerNote.value,
            content: vueEditorData,
            activeStatus: activeStatus.value
          },
      )
          .then((response) => {
            toolchainIdx = response.data;
            isExit = false;
          })
          .catch((error) => {
            parseErrorMsg(error.response.data);
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
        formData.append("toolchainIdx", toolchainIdx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/attached-file",
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
              parseErrorMsg(error.response);
              fileUpload.initFileUpload();
              document.getElementById("attachedFileList").innerHTML = "";
              document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.totalFileSize);
              deletePost(toolchainIdx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/toolchain/write-success");
      router.push("/toolchain/read/" + toolchainIdx);
    }
    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      title, priority, hashTags, toolCategory, ideName, compilerName, compilerNote, activeStatus,
      priorityArray,

      // function
      validateLengthAndIsEmpty, validateEditor,
      writePost
    }
  }
};
</script>