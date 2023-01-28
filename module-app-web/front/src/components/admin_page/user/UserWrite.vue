<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="사용자" :paths="['사용자 페이지', '사용자 작성']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">사용자 정보</th>
          </tr>
          <tr>
            <th>아이디<span class="required-field">*</span></th>
            <td>
              <input type="text" name="username" v-model="username" class="form-control" @change="validateUsername('username', username)">
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
            <th>부서<span class="required-field">*</span><span class="auto-completed-field">*</span></th>
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
                <option value="C_SENIOR_RESEARCH_ENGINEER">책임연구원</option>
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
              <select v-model="authorityType" class="form-select">
                <option v-if="accessorAuthorityType === 'ROOT'" value="MANAGER">관리자</option>
                <option v-if="accessorAuthorityType === 'ROOT' || accessorAuthorityType === 'MANAGER'" value="GENERAL">일반 회원</option>
                <option value="READER">읽기 회원</option>
                <option value="NON_USER">비회원</option>
              </select>
              <p id="authorityErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <ImageFileUpload itemName="사진" pageInformation="write"></ImageFileUpload>
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
import ImageFileUpload from '@/components/common/ImageFileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from "axios";
// CKEditor
import CKEditor from '@ckeditor/ckeditor5-vue'
import {editor, editorConfig, editorGeneralData} from "@/assets/plugins/ckeditor/ckeditor-init"
// file upload
import {fileUpload} from '@/assets/plugins/file-upload/file-upload';
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";
// utils
import {deleteArrayIndexIsEmpty, parseApiErrorMsg, validateContact, onEditorReady, validateEditor, validateEmail, validateLength, validateLengthAndIsEmpty, validatePassword, validateUsername} from "@/utils/validation-util";
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";

export default {
  components: {
    ImageFileUpload,
    Loading,
    Breadcrumb,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorGeneralData;
    const vueEditorConfig = editorConfig;
    // variable
    const username = ref("");
    const password = ref("");
    const checkPassword = ref("");
    const name = ref("");
    const department = ref("");
    const position = ref("E_ASSOCIATE_RESEARCH_ENGINEER");
    const userStatus = ref("IN_OFFICE");
    const authorityType = ref("GENERAL");
    const contact = ref("");
    const email = ref("");
    const privateEmail = ref("");
    const introduction = ref("");
    const activeStatus = ref("ACTIVE");
    // accessorAuthorityType
    let accessorAuthorityType = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/form/0",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteDepartment, "department", department);

            // 접근하는 사용자의 접근 권한 설정
            accessorAuthorityType.value = response.data.accessorAuthorityType;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    });

    /* 삭제 */
    const deletePost = (idx) => {
      axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/" + idx,
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

      if (!(await validateUsername("username", username.value)
          && validatePassword(false, 'password', 'checkPassword', password.value, checkPassword.value)
          && validateLengthAndIsEmpty("name", name.value, 255)
          && validateLengthAndIsEmpty("department", department.value, 255)
          && validateLength("position", position.value, 255)
          && validateContact("contact", contact.value, 255)
          && validateEmail("email", email.value, 255)
          && validateEmail("privateEmail", privateEmail.value, 255)
      )) {
        return false;
      }

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users",
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
            introduction: vueEditorData
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
              document.getElementById("totalFileSize").innerHTML = convertFileSize(fileUpload.getTotalFileSize());
              deletePost(idx);
              isExit = true;
            })
            .then(() => {
              return isExit;
            });
      }

      if (isExit) return false;

      localStorage.setItem("result", "/user/write-success");
      router.push("/user/read/" + idx);
    }

    return {
      // variable
      vueEditor, vueEditorData, vueEditorConfig,
      username, password, checkPassword, name, department, position, userStatus, authorityType, introduction, contact, email, privateEmail,
      activeStatus,
      accessorAuthorityType,

      // function
      onEditorReady, validateEditor, validateUsername, validatePassword, validateEmail, validateContact,
      writePost
    }
  }
};
</script>