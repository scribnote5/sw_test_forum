<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="지식 저장소" :paths="['나머지 자료', '지식 저장소 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">나머지 저장소 정보</th>
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
            <th>유형<span class="required-field">*</span></th>
            <td>
              <select v-model="category" @change="searchTypeChange($event)" class="form-select">
                <option value="STUDY">스터디</option>
                <option value="FINAL_TEST">실사 후기</option>
                <option value="EDUCATION">교육 자료</option>
                <option value="CHECK_LIST">체크 리스트</option>
                <option value="REFERENCE">참고 자료(가이드라인)</option>
                <option value="ETC">기타 자료</option>
              </select>
              <p id="categoryErrorMessage" class="error-message"></p>
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
          <router-link :to="'/storage/list'">
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
import Priority from '@/components/common/Priority.vue'
import FileUpload from '@/components/common/FileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from "axios";
// CKEditor
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorGeneralData, editorFinalTestData, editorEducationData} from "@/assets/plugins/ckeditor/ckeditor-init"
// file upload
import {updatePriorityValue} from '@/assets/plugins/priority/priority';
// file upload
import {fileUpload} from '@/assets/plugins/file-upload/file-upload';
// utils
import {deleteArrayIndexIsEmpty, parseApiErrorMsg, onEditorReady, validateEditor, validateLength, validateLengthAndIsEmpty} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {confirm} from "@/assets/plugins/sweetalert2/sweetalert2";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    FileUpload,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    let vueEditorData = ref(editorGeneralData);
    const vueEditorConfig = editorConfig;
    // variable
    const title = ref("");
    const priority = ref(6);
    const category = ref("STUDY");
    const activeStatus = ref("ACTIVE");
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/priority-list-write",
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

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/storage/list");
          })
          .then(() => {
          });
    });

    /* searchType 변경될 때 */
    const searchTypeChange = (e) => {
      confirm.fire({
        title: "에디터 템플릿을 변경하시겠습니까?",
        text: "에디터 내용이 초기화 됩니다.",
        confirmButtonText: '네',
        cancelButtonText: '아니오'
      }).then((result) => {
        if (result.isConfirmed) {
          if (e.target.value === 'FINAL_TEST') {
            vueEditorData.value = editorFinalTestData;
          } else if (e.target.value === 'EDUCATION') {
            vueEditorData.value = editorEducationData;
          }
          else {
            vueEditorData.value = editorGeneralData;
          }
        } else {
          return false;
        }
      });
    }

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/" + idx,
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

      // priority 값 초기화
      priority.value = updatePriorityValue();

      if (!validateLengthAndIsEmpty("title", title.value, 255)
          && validateLength("category", category.value, 255)
      ) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages",
          {
            title: title.value,
            priority: Number(priority.value),
            category: category.value,
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
        formData.append("storageIdx", idx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/attached-file",
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

      localStorage.setItem("result", "/storage/write-success");
      router.push("/storage/read/" + idx);
    }

    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      title, priority, category, activeStatus,
      priorityArray,

      // function
      searchTypeChange, validateLengthAndIsEmpty, onEditorReady, validateEditor,
      writePost
    }
  }
};
</script>