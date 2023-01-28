<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="사용자" :paths="['사용자 페이지', '사용자 수정']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">사용자 정보</th>
          </tr>
          <tr>
            <th>
              아이디<span class="required-field">*</span><br>
              <span class="mention-field">Circle 아이디로 가입 부탁드립니다.</span>
            </th>
            <td>
              <input type="text" name="username" v-model="username" class="form-control" disabled>
              <p id="usernameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>비밀번호<span class="required-field">*</span></th>
            <td>
              <input type="password" name="password" v-model="password" class="form-control" @change="validatePassword(false, 'password', 'checkPassword', password, checkPassword)"/>
            </td>
          </tr>
          <tr>
            <th>비밀번호 확인<span class="required-field">*</span></th>
            <td>
              <input type="password" name="checkPassword" v-model="checkPassword" class="form-control" @change="validatePassword(false, 'password', 'checkPassword', password, checkPassword)"/>
              <p id="passwordErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>이름<span class="required-field">*</span></th>
            <td>
              <input type="text" name="name" v-model="name" class="form-control">
              <p id="nameErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>부서<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="department" v-model="department" class="form-control">
              <p id="departmentErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>직위</th>
            <td>
              <select v-model="position" class="form-select">
                <option value="A_EXECUTIVES">임원</option>
                <option value="B_PRINCIPAL_RESEARCH_ENGINEER">수석연구원</option>
                <option value="C_SENIOR_RESEARCH_ENGINEER">전문연구원</option>
                <option value="D_RESEARCH_ENGINEER">선임연구원</option>
                <option value="E_ASSOCIATE_RESEARCH_ENGINEER">연구원</option>
                <option value="F_GENERAL_MANAGER">부장</option>
                <option value="H_MANAGER">차장</option>
                <option value="G_DEPUTY_GENERAL_MANAGER">과장</option>
                <option value="I_ASSISTANT_MANAGER">대리</option>
                <option value="J_STAFF">사원</option>
                <option value="K_ETC">기타</option>
              </select>
              <p id="positionErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>권한</th>
            <td>
              <select v-if="accessorAuthorityType === 'ROOT' && username === 'root'" v-model="authorityType" class="form-select">
                <option value="ROOT">최고 관리자(root)</option>
              </select>
              <select v-else v-model="authorityType" class="form-select">
                <option v-if="accessorAuthorityType === 'ROOT' || accessorAuthorityType === 'MANAGER'" value="MANAGER">관리자</option>
                <option v-if="accessorAuthorityType === 'ROOT' || accessorAuthorityType === 'MANAGER' || accessorAuthorityType === 'GENERAL'" value="GENERAL">일반 회원</option>
                <option v-if="accessorAuthorityType === 'ROOT' || accessorAuthorityType === 'MANAGER' || accessorAuthorityType === 'GENERAL' || accessorAuthorityType === 'GENERAL'" value="READER">읽기 회원</option>
                <option value="NON_USER">비회원</option>
              </select>
              <p id="authorityErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <ImageFileUpload itemName="사진" pageInformation="update" :uploadedAttachedFileList="uploadedAttachedFileList"></ImageFileUpload>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">연락처 정보</th>
          </tr>
          <tr>
            <th>연락처</th>
            <td>
              <input type="text" name="contact" v-model="contact" class="form-control" placeholder="000-0000-0000" @change="validateContact('contact', contact, 255)">
              <p id="contactErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>이메일</th>
            <td>
              <input type="text" name="email" v-model="email" class="form-control" @change="validateEmail('email', email, 255)">
              <p id="emailErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>개인 이메일</th>
            <td>
              <input type="text" name="privateEmail" v-model="privateEmail" class="form-control" @change="validateEmail('privateEmail', privateEmail, 255)">
              <p id="privateEmailErrorMessage" class="error-message"></p>
            </td>
          </tr>

          <tr>
            <th colspan="2" class="sub-item-title">자기소개</th>
          </tr>
          <tr>
            <td colspan="2">
              <ckeditor id="content" :editor="vueEditor" v-model="vueEditorData" :config="vueEditorConfig" @ready="onEditorReady" @blur="validateEditor"></ckeditor>
            </td>
          </tr>

          <input type="hidden" v-model="activeStatus"/>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/user/list'">
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
</style>

<script>
// components
import Breadcrumb from '@/components/common/Breadcrumb.vue'
import Loading from '@/components/common/Loading.vue'
import Priority from '@/components/common/Priority.vue'
import ImageFileUpload from '@/components/common/ImageFileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// CKEditor
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig} from "@/assets/plugins/ckeditor/ckeditor-init"
// sweetalert2
import {confirm} from '@/assets/plugins/sweetalert2/sweetalert2';
// utils
import {deleteArrayIndexIsEmpty, parseApiErrorMsg, validateContact, onEditorReady, validateEditor, validateEmail, validateLength, validateLengthAndIsEmpty, validatePassword} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    ImageFileUpload,
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
    const username = ref("");
    const password = ref("");
    const checkPassword = ref("");
    const name = ref("");
    const department = ref("");
    const position = ref("");
    const userStatus = ref("");
    const authorityType = ref("");
    const contact = ref("");
    const email = ref("");
    const privateEmail = ref("");
    const introduction = ref("");
    const activeStatus = ref("ACTIVE");
    let createdByUser = ref([]);
    let createdDate = ref("");
    let lastModifiedByUser = ref([]);
    let lastModifiedDate = ref("");
    // accessorAuthorityType
    let accessorAuthorityType = ref("");
    // uploadedFileList
    let uploadedAttachedFileList = ref([{savedFileName: ''}]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/form/" + idx,
          {},
      )
          .then((response) => {
            username.value = response.data.username;
            name.value = response.data.name;
            department.value = response.data.department;
            position.value = response.data.position;
            userStatus.value = response.data.userStatus;
            authorityType.value = response.data.authorityType;
            contact.value = response.data.contact;
            email.value = response.data.email;
            privateEmail.value = response.data.privateEmail;
            vueEditorData.value = response.data.introduction;

            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteDepartment, "department", department);

            // 접근하는 사용자의 접근 권한 설정
            accessorAuthorityType.value = response.data.accessorAuthorityType;

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
              uploadedAttachedFile.uri = process.env.VUE_APP_MODULE_APP_API_URL + '/api/attached-files/view-image/' + uploadedAttachedFile.savedFileName;
            }
            fileUpload.totalFileSize = uploadedAttachedFileList.value.totalFileSize;
            uploadedAttachedFileList.value.convertedTotalFileSize = convertFileSize(uploadedAttachedFileList.value.totalFileSize);
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

      if (!(validatePassword(isEmpty(password.value) && isEmpty(checkPassword.value), 'password', 'checkPassword', password.value, checkPassword.value)
          && validateLengthAndIsEmpty("name", name.value, 255)
          && validateLengthAndIsEmpty("department", department.value, 255)
          && validateLength("position", position.value, 255)
          && validateContact("contact", contact.value, 255)
          && validateEmail("email", email.value, 255)
          && validateEmail("privateEmail", privateEmail.value, 255)
      )) {
        return false;
      }

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/" + idx,
          {
            username: username.value,
            password: password.value,
            name: name.value,
            department: department.value,
            position: position.value,
            userStatus: userStatus.value,
            authorityType: authorityType.value,
            contact: contact.value,
            email: email.value,
            privateEmail: privateEmail.value,
            activeStatus: activeStatus.value,
            introduction: vueEditorData.value
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
        isExit = await axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/attached-file", {
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
        formData.append("userIdx", idx);

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/attached-file",
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
              document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getOriTotalFileSize());
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/user/update-success");
      router.push("/user/read/" + idx);
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/user/delete-success");
                router.push("/user/list");
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
      username, password, checkPassword, name, department, position, userStatus, authorityType, introduction, contact, email, privateEmail,
      accessorAuthorityType,

      // function
      onEditorReady, validateEditor, validatePassword, validateEmail, validateContact,
      updatePost, deletePost
    }
  }
}
;
</script>