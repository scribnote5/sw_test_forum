<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="질문 답변" :paths="['질문 답변', '답변 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">답변 정보</th>
          </tr>
          <tr>
            <th>제목<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="title" v-model="title" class="form-control">
              <p id="titleErrorMessage" class="error-message"></p>
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
          <input type="hidden" v-model="type"/>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/question-answer/list'">
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
</style>

<script>
// components
import Breadcrumb from '@/components/common/Breadcrumb.vue'
import Loading from '@/components/common/Loading.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// CKEditor
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorGeneralData} from "@/assets/plugins/ckeditor/ckeditor-init"
// file upload
import {fileUpload} from '@/assets/plugins/file-upload/file-upload';
// utils
import {deleteArrayIndexIsEmpty, parseApiErrorMsg, onEditorReady, validateEditor, validateLengthAndIsEmpty} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";

export default {
  components: {
    Loading,
    Breadcrumb,
    FileUpload,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorGeneralData;
    const vueEditorConfig = editorConfig;
    // variable
    const groupIdx = route.params.groupIdx;;
    const title = ref("");
    const type = ref("ANSWER");
    const activeStatus = ref("ACTIVE");

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/misra-c/list/");
          })
          .then(() => {
          });
    });

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/" + idx,
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
      let idx;
      let isExit;

      if (!validateLengthAndIsEmpty("title", title.value, 255)) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/answer",
          {
            groupIdx: groupIdx,
            title: title.value,
            type: type.value,
            activeStatus: activeStatus.value,
            content: vueEditorData
          },
      )
          .then((response) => {
            idx = response.data;
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
        formData.append("questionAnswerIdx", idx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/attached-file",
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
              document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
              deletePost(idx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/question-answer/write-success");
      router.push("/question-answer/read/" + idx);
    }

    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      title, type, activeStatus,

      // function
      validateLengthAndIsEmpty, validateEditor,
      writePost
    }
  }
};
</script>