<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="메트릭 규칙" :paths="['메트릭', '메트릭 규칙 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">메트릭 규칙 정보</th>
          </tr>
          <tr>
            <th>규칙명<span class="required-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <div class="autoComplete_wrapper">
                <input type="text" name="title" id="title" v-model="title" class="form-control" placeholder="함수 내 분기문 개수(Cyclomatic Complexity, FUCYC)">
                <p id="titleErrorMessage" class="error-message"></p>
              </div>
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
          <router-link :to="'/metric/list'">
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
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
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
    const priority = ref(6);
    const frequency = ref("AVERAGE");
    const hashTags = ref("");
    const activeStatus = ref("ACTIVE");
    // priority array
    let priorityArray = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createAutoComplete(response.data.autoCompleteHashTags, "hashTag", hashTag);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
            router.push("/metric/list/");
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/priority-list-write",
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
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/" + idx,
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
      let metricIdx;
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // frequency 값 초기화
      frequency.value = updateFrequencyValue();

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      if (!(validateLengthAndIsEmpty("title", title.value, 255)
          && validateLength("hashTags", hashTags.value, 255)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric",
          {
            title: title.value,
            priority: priority.value,
            frequency: frequency.value,
            hashTags: hashTags.value,
            content: vueEditorData,
            activeStatus: activeStatus.value
          },
      )
          .then((response) => {
            metricIdx = response.data;
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
        formData.append("metricIdx", metricIdx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/attached-file",
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
              deletePost(metricIdx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/metric/write-success");
      router.push("/metric/read/" + metricIdx);
    }

    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      title, priority, frequency, hashTags, activeStatus,
      priorityArray,

      // function
      validateLengthAndIsEmpty, onEditorReady, validateEditor,
      writePost
    }
  }
};
</script>