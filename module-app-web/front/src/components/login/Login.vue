<template>
  <!-- Loading -->
  <Loading></Loading>

  <section class="d-flex align-items-center min-vh-100 py-3 py-md-0">
    <div class="container">
      <form ref="form" v-on:submit.prevent>
        <div class="card login-card">
          <div class="row justify-content-start">
            <div class="col-sm-0 col-md-2 card-side"></div>
            <div class="col-sm-12 col-md-10 card-body">
              <div class="brand-wrapper">
                <img :src="require(`@/assets/images/logo.svg`)" class="logo"/>
              </div>
              <p class="login-card-description">
                <span style="color:red">SW</span> reliability <span style="color:red">I</span>nforma<span style="color:red">T</span>ion <span style="color:red">CH</span>annel<br>
                : SWITCH
              </p>
              <div class="mb-4">
                <input type="text" v-model="loginUsername" id="loginUsername" class="form-control" placeholder="ID">
                <input type="password" v-model="loginPassword" id="loginPassword" class="form-control" placeholder="Password">
              </div>
              <div class="mb-4 text-center">
                <button @click="login()" class="btn btn-block login-btn">로그인</button>
              </div>
              <div class="mb-1 mb-md-2">
                <span data-bs-toggle="modal" data-bs-target="#inquireModal" class="forgot-password-link cursor-pointer">계정 관련 문의</span>
              </div>
              <div class="mb-1 mb-md-2">
                <span class="login-card-register-question-text">계정이 없다면?</span> &nbsp;
                <span data-bs-toggle="modal" data-bs-target="#joinModal" class="login-card-register-text cursor-pointer">회원 가입</span>
              </div>
              <div class="mb-1 mb-md-2">
                <span class="alert-message">* 크로미엄 기반 브라우저(크롬, 엣지)를 사용해주세요.</span>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>

    <!-- 계정 문의 Modal -->
    <div class="modal fade" id="inquireModal" tabindex="-1" aria-labelledby="inquireModal" aria-hidden="true">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="inquireModalLabel">계정 관련 문의</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div v-for="(managerUser, i) in managerUserList" :key="i" class="manager-user-list">
                {{ managerUser.department }} {{ managerUser.name }}
                <span v-if="managerUser.position == 'A_EXECUTIVES'">임원</span>
                <span v-if="managerUser.position == 'B_PRINCIPAL_RESEARCH_ENGINEER'">수석연구원</span>
                <span v-if="managerUser.position == 'C_SENIOR_RESEARCH_ENGINEER'">전문연구원</span>
                <span v-if="managerUser.position == 'D_RESEARCH_ENGINEER'">선임연구원</span>
                <span v-if="managerUser.position == 'E_ASSOCIATE_RESEARCH_ENGINEER'">연구원</span>
                <span v-if="managerUser.position == 'F_GENERAL_MANAGER'">부장</span>
                <span v-if="managerUser.position == 'H_MANAGER'">차장</span>
                <span v-if="managerUser.position == 'G_DEPUTY_GENERAL_MANAGER'">과장</span>
                <span v-if="managerUser.position == 'I_ASSISTANT_MANAGER'">대리</span>
                <span v-if="managerUser.position == 'J_STAFF'">사원</span>
                <span v-if="managerUser.position == 'K_ETC'">기타</span>

                <span class="ms-2">
                  {{ managerUser.email }}
                  <a :href="'mailto:' + managerUser.email" class="ms-1">
                    <img :src="require(`@/assets/images/mail-main-blue.svg`)" class="me-1"/>
                  </a>
                </span>

              </div>

              <div class="d-flex justify-content-end mx-4 mt-4">
                <div class="d-flex">
                  <button class="btn btn-main-red" data-bs-dismiss="modal" aria-label="Close">취소<img :src="require(`@/assets/images/x-circle-main-white.svg`)" class="ms-2"></button>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <span class="alert-message">* 관리자에게 메일로 문의 해주세요.</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 회원 가입 Modal -->
    <div class="modal fade" id="joinModal" tabindex="-1" aria-labelledby="joinModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="joinModalLabel">회원 가입</h4>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="page-content">
                <table class="table mobile-table-read">
                  <thead>
                  <th style="width: 17.5%;" class="text-center"></th>
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

              <div class="d-flex justify-content-between mx-4 mt-4">
                <div class="d-flex">
                  <button class="btn btn-main-red" data-bs-dismiss="modal" aria-label="Close">취소<img :src="require(`@/assets/images/x-circle-main-white.svg`)" class="ms-2"></button>
                </div>
                <div class="d-flex">
                  <button @click="writePost(vueEditorData)" class="btn btn-main-blue">작성<img :src="require(`@/assets/images/write-white.svg`)" class="ms-2"></button>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <span class="alert-message">* 관리자가 승인해야 회원 가입이 완료 됩니다.</span>
          </div>
        </div>
      </div>
    </div>

  </section>
</template>

<style lang="scss">
section {
  background-color: $darker-main-white;
  min-height: 100vh;
}

.container {
  @media (min-width: 992px) {
    max-width: 840px !important;
  }
}

.brand-wrapper {
  margin-bottom: 10px;

  .logo {
    height: 60px;

    @media (max-width: 767px) {
      height: 50px;
    }
  }
}

.login-card {
  border: none !important;
  border-radius: 27.5px !important;
  box-shadow: 0 10px 30px 0 $lightest-main-grey;
  overflow: hidden;

  .login-card-description {
    font-weight: $strong-font-weight;
    font-size: 25px;
    color: black;
    margin-left: 75px;
    margin-bottom: 20px;

    @media (max-width: 767px) {
      font-size: $largest-font-size;
    }
  }

  .card-side {
    background-color: rgb(243, 115, 33);
  }

  .card-body {
    padding: 25px 50px;

    @media (max-width: 767px) {
      padding: 35px 40px;
    }
  }

  form {
    max-width: 326px;
  }

  .form-control {
    border: none;
    border-bottom: 1px solid $lightest-main-grey;
    padding: 10px 15px;
    margin-bottom: 10px;
    min-height: 45px;
    font-weight: normal;

    &::placeholder {
      color: $main-grey;
    }
  }

  .login-btn {
    padding: 13px 20px 12px;
    background-color: $dark-main-black;
    border-radius: 5px;
    font-size: $large-font-size;
    font-weight: bold;
    line-height: 20px;
    color: white;
    width: 100%;

    &:hover {
      background-color: $lighter-main-grey;
      color: $main-black;
    }
  }

  .forgot-password-link {
    font-size: $large-font-size;
    color: $dark-main-grey;

    &:hover {
      color: $lighter-main-grey;
    }
  }

  .login-card-register-question-text {
    font-size: $large-font-size;
    font-weight: $medium-font-weight;
  }

  .login-card-register-text {
    font-size: $large-font-size;
    color: $light-main-blue;

    &:hover {
      color: $lightest-main-blue;
    }
  }
}

.manager-user-list {
  font-size: $larger-font-size;
}

.alert-message {
  margin-left: .25rem;
  color: $lightest-main-red;
  font-weight: var(--strong-font-weight);
  font-size: $larger-font-size;
}
</style>

<script>
// components
import Loading from '@/components/common/Loading.vue'
import ImageFileUpload from "@/components/common/ImageFileUpload";

// CKEditor
import CKEditor from "@ckeditor/ckeditor5-vue";
import {editor, editorConfig, editorGeneralData} from "@/assets/plugins/ckeditor/ckeditor-init";
// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from 'axios'
import vueCookies from 'vue-cookies';
// file upload
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";
import {convertFileSize} from "@/utils/converter-util";
import {isEmpty} from "@/utils/empty-util";
// utils
import {
  deleteArrayIndexIsEmpty,
  parseLoginErrorMsg,
  parseApiErrorMsg,
  validateContact,
  onEditorReady,
  validateEditor,
  validateEmail,
  validateLength,
  validateLengthAndIsEmpty,
  validatePassword,
  validateUsername
} from "@/utils/validation-util";
import {createAutoComplete} from "@/assets/plugins/auto-complete/auto-complete";
import {fireSuccessToast} from "@/assets/plugins/sweetalert2/sweetalert2";

export default {
  components: {
    ImageFileUpload,
    Loading,
    ckeditor: CKEditor.component
  },
  setup() {
    // vue.js
    const router = useRouter();
    // CKEditor
    const vueEditor = editor;
    const vueEditorData = editorGeneralData;
    const vueEditorConfig = editorConfig;
    // variable, login
    const loginUsername = ref("");
    const loginPassword = ref("");
    let loginResultType;
    // variable, inquire
    const managerUserList = ref("");
    // variable, join
    const username = ref("");
    const password = ref("");
    const checkPassword = ref("");
    const name = ref("");
    const department = ref("");
    const position = ref("E_ASSOCIATE_RESEARCH_ENGINEER");
    const userStatus = ref("IN_OFFICE");
    const authorityType = ref("NON_USER");
    const contact = ref("");
    const email = ref("");
    const privateEmail = ref("");
    const introduction = ref("");
    const activeStatus = ref("ACTIVE");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("join-success");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/login/form",
          {},
      )
          .then((response) => {
            // autoComplete 설정
            createAutoComplete(response.data.autoCompleteDepartment, "department", department);
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });


      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/manager-list",
          {},
      )
          .then((response) => {
            managerUserList.value = response.data;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    });

    /* 로그인 */
    const login = async () => {
      // Make a request for a user with a given ID
      await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/auths/authenticate",
          {
            username: loginUsername.value,
            password: loginPassword.value,
            moduleName: 'module-app-web'
          },
      )
          .then((response) => {
            vueCookies.set('isHasToken', true);
            vueCookies.set('loginUserInfo', response.data);
            loginResultType = 'SUCCESS';
          })
          .catch((error) => {
            parseLoginErrorMsg(error.response);
            loginResultType = 'FAIL';
          })
          .then(() => {
          });

      await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/login-historys",
          {
            loginUsername: loginUsername.value,
            loginResultType: loginResultType,
          },
      )
          .then((response) => {
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      if (loginResultType === 'SUCCESS') {
        router.push("/dashboard");
      } else {
        router.push("/login");
      }
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

      isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/login",
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

        isExit = await axios.post(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/login/attached-file",
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

      localStorage.setItem("result", "/join-success");
      location.href = "/login";
    }

    return {
      // variable, login
      loginUsername, loginPassword, login,

      // variable, inquire
      managerUserList,

      // variable, join
      vueEditor, vueEditorData, vueEditorConfig,
      username, password, checkPassword, name, department, position, userStatus, authorityType, introduction, contact, email, privateEmail,
      activeStatus,

      // function
      onEditorReady, validateEditor, validateUsername, validatePassword, validateEmail, validateContact,
      writePost
    }
  }
}

</script>