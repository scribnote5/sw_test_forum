<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C 규칙" :paths="['MISRA C', 'MISRA C 규칙 수정']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C 규칙 정보</th>
          </tr>
          <tr>
            <th>규칙명<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <div class="autoComplete_wrapper">
                <input type="text" name="title" v-model="title" class="form-control" placeholder="[02_01] 미달성 코드(unreachable code) 제거">
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
              <Priority pageInformation="update" :priority=Number(priority) :maxPriority=Number(6) :priorityArray=priorityArray></Priority>
            </td>
          </tr>
          <tr>
            <th>위배 빈도<span class="required-field">*</span></th>
            <td>
              <Frequency pageInformation="update" :frequency=frequency></Frequency>
            </td>
          </tr>
          <tr>
            <th>해시태그<span class="recommended-field">*</span><span class="auto-completed-field">*</span></th>
            <td style="overflow: visible">
              <HashTags pageInformation="update" :hash-tags="hashTags"></HashTags>
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
              <input type="checkbox" id="C90" v-model="appliesToC90" value="C90" class="form-check-input">
              <label for="C90" class="form-check-label ms-1 me-3">C90</label>
              <input type="checkbox" id="C99" v-model="appliesToC99" value="C99" class="form-check-input">
              <label for="C99" class="form-check-label ms-1 me-3">C99</label>
              <input type="checkbox" id="C11" v-model="appliesToC11" value="C11" class="form-check-input">
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
              <FileUpload pageInformation="update" :uploadedAttachedFileList="uploadedAttachedFileList"></FileUpload>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">MISRA C 규칙 부가정보</th>
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
          <router-link :to="'/misra-c/list'">
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
import Frequency from '@/components/common/Frequency.vue'
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
import {updateFrequencyValue} from "@/assets/plugins/frequency/frequency";
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
    Frequency,
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
    let originalTitle = ref("");
    let priority = ref(0);
    let frequency = ref("");
    let hashTags = ref("");
    let hashTagsIdx = ref(0);
    let category = ref("");
    let scope = ref("");
    let decidability = ref("");
    let appliesTo = ref([]);
    let qacTitle = ref("");
    let activeStatus = ref("");
    let createdByUser = ref([]);
    let createdDate = ref("");
    let lastModifiedByUser = ref([]);
    let lastModifiedDate = ref("");
    // applies to
    let appliesToC90 = ref([]);
    let appliesToC99 = ref([]);
    let appliesToC11 = ref([]);
    // priority array
    let priorityArray = ref([]);
    // uploadedFileList
    let uploadedAttachedFileList = ref([]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/form/" + idx,
          {},
      )
          .then((response) => {
            title.value = response.data.title;
            originalTitle.value = response.data.originalTitle;
            priority.value = response.data.priority;
            frequency.value = response.data.frequency;
            hashTags.value = response.data.hashTags;
            hashTagsIdx.value = response.data.hashTagsIdx;
            category.value = response.data.category;
            scope.value = response.data.scope;
            decidability.value = response.data.decidability;
            appliesTo.value = response.data.appliesTo;
            qacTitle.value = response.data.qacTitle;
            vueEditorData.value = response.data.content;

            for (const standard of appliesTo.value.split(", ")) {
              switch (standard) {
                case "C90":
                  appliesToC90.value.push("C90");
                  break;
                case "C99":
                  appliesToC99.value.push("C99");
                  break;
                case "C11":
                  appliesToC11.value.push("C11");
                  break;
              }
            }

            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteTitle, "title", title);
            createHashTagsAutoComplete(response.data.autoCompleteHashTags, "hashTag");

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

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/priority-list-update/" + idx,
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

    /* 수정 */
    const updatePost = async () => {
      let isExit;

      // priority 값 초기화
      priority.value = updatePriorityValue();

      // frequency 값 초기화
      frequency.value = updateFrequencyValue();

      // hashTags 값 초기화
      hashTags.value = updateHashTagsValue();

      // appliesTo 값 초기화
      appliesTo.value = [];
      if(!isEmpty(appliesToC90.value[0])) appliesTo.value.push(appliesToC90.value[0]);
      if(!isEmpty(appliesToC99.value[0])) appliesTo.value.push(appliesToC99.value[0]);
      if(!isEmpty(appliesToC11.value[0])) appliesTo.value.push(appliesToC11.value[0]);

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

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/" + idx,
          {
            title: title.value,
            originalTitle: originalTitle.value,
            priority: priority.value,
            frequency: frequency.value,
            hashTags: hashTags.value,
            hashTagsIdx: hashTagsIdx.value,
            category: category.value,
            scope: scope.value,
            decidability: decidability.value,
            appliesTo: appliesTo.value.join(", "),
            qacTitle: qacTitle.value,
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
        isExit = await axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/attached-file/", {
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
        formData.append("misraCIdx", idx);

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
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/misra-c/update-success");
      router.push("/misra-c/read/" + idx);
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/misra-c/delete-success");
                router.push("/misra-c/list");
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
      title, originalTitle, priority, frequency, hashTags, category, scope, decidability, appliesTo, qacTitle,
      appliesToC90, appliesToC99, appliesToC11,
      priorityArray,

      // function
      onEditorReady, validateEditor,
      updatePost, deletePost
    }
  }
}
;
</script>