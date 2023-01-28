<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="사용자" :paths="['사용자 페이지', '사용자 보기']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <td colspan="2">
              <h2 class="mobile-title">
                {{ user.department }} {{ user.name }}
                <span v-if="user.position == 'A_EXECUTIVES'">임원</span>
                <span v-if="user.position == 'B_PRINCIPAL_RESEARCH_ENGINEER'">수석연구원</span>
                <span v-if="user.position == 'C_SENIOR_RESEARCH_ENGINEER'">책임연구원</span>
                <span v-if="user.position == 'D_RESEARCH_ENGINEER'">선임연구원</span>
                <span v-if="user.position == 'E_ASSOCIATE_RESEARCH_ENGINEER'">연구원</span>
                <span v-if="user.position == 'F_GENERAL_MANAGER'">부장</span>
                <span v-if="user.position == 'H_MANAGER'">차장</span>
                <span v-if="user.position == 'G_DEPUTY_GENERAL_MANAGER'">과장</span>
                <span v-if="user.position == 'I_ASSISTANT_MANAGER'">대리</span>
                <span v-if="user.position == 'J_STAFF'">사원</span>
                <span v-if="user.position == 'K_ETC'">기타</span>
              </h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ user.createdByUser.department }} {{ user.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ user.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ user.lastModifiedByUser.department }} {{ user.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ user.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ user.views }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <ImageFileUpload pageInformation="read" :uploadedAttachedFileList="uploadedAttachedFileList"></ImageFileUpload>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>아이디</th>
            <td>
              {{ user.username }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>재직 여부</th>
            <td>
              <span v-if="user.userStatus == 'IN_OFFICE'">재직중</span>
              <span v-if="user.userStatus == 'RETIREE'">퇴사</span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>권한</th>
            <td>
              <span v-if="user.authorityType == 'ROOT'">최고 관리자</span>
              <span v-if="user.authorityType == 'MANAGER'">관리자</span>
              <span v-if="user.authorityType == 'GENERAL'">일반 회원</span>
              <span v-if="user.authorityType == 'READER'">읽기 회원</span>
              <span v-if="user.authorityType == 'NON_USER'">비회원</span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>연락처</th>
            <td>
              {{ user.contact }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>이메일</th>
            <td>
              {{ user.email }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>개인 이메일</th>
            <td>
              {{ user.privateEmail }}
            </td>
          </tr>
          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>아이디: </strong> {{ user.username }} <br>
              <strong>재직여부: </strong>
              <span v-if="user.userStatus == 'IN_OFFICE'">재직중</span>
              <span v-if="user.userStatus == 'RETIREE'">퇴사</span> <br>
              <strong>권한: </strong>
              <span v-if="user.authorityType == 'ROOT'">최고 관리자</span>
              <span v-if="user.authorityType == 'MANAGER'">관리자</span>
              <span v-if="user.authorityType == 'GENERAL'">일반 회원</span>
              <span v-if="user.authorityType == 'READER'">읽기 회원</span>
              <span v-if="user.authorityType == 'NON_USER'">비회원</span> <br>
              <strong>연락처: </strong> {{ user.contact }}<br>
              <strong>이메일: </strong> {{ user.email }}<br>
              <strong>개인 이메일: </strong> {{ user.privateEmail }}<br>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="user.introduction"></div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/user/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex" v-if="user.access">
          <router-link :to="'/user/update/' + idx" class="me-2">
            <button class="btn btn-main-blue d-flex align-items-center">수정<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
          </router-link>
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
import Loading from '@/components/common/Loading.vue'
import Breadcrumb from '@/components/common/Breadcrumb.vue'
import Priority from '@/components/common/Priority.vue'
import ImageFileUpload from '@/components/common/ImageFileUpload.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {confirm, fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";
import {convertFileSize} from "@/utils/converter-util";
import {fileUpload} from "@/assets/plugins/file-upload/file-upload";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    ImageFileUpload
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let user = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // uploadedFileList
    let uploadedAttachedFileList = ref([{uri: ''}]);

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("user");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/read/" + idx,
          {},
      )
          .then((response) => {
            user.value = response.data;
            user.value.introduction = user.value.introduction;

            // 공통 데이터 설정
            user.value.createdDate = dayjs(user.createdDate).format("YYYY.MM.DD. HH:mm");
            user.value.lastModifiedDate = dayjs(user.lastModifiedDate).format("YYYY.MM.DD. HH:mm");

            // 업로드 된 첨부 파일 설정
            uploadedAttachedFileList.value = user.value.attachedFileList;
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
    })

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
      idx, user, uploadedAttachedFileList,

      // function
      deletePost
    }
  }
}
</script>