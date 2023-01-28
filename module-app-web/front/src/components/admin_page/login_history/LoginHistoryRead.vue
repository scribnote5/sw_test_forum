<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="로그인 기록" :paths="['관리자 페이지', '로그인 기록 보기']" title=""/>

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
              <h2 class="mobile-title">{{ loginHistory.message }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ loginHistory.createdByUser.department }} {{ loginHistory.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ loginHistory.createdDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ loginHistory.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>접속 시도 아이디</th>
            <td>
              {{ loginHistory.loginUsername }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>아이피</th>
            <td>
              {{ loginHistory.ip }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>지역</th>
            <td>
              {{ loginHistory.location }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>로그인 결과</th>
            <td>
              <span v-if="loginHistory.loginResultType == 'SUCCESS'">성공</span>
              <span v-if="loginHistory.loginResultType == 'FAIL'">실패</span>
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>접속 시도 아이디: </strong>{{ loginHistory.loginUsername }}<br>
              <strong>ip: </strong>{{ loginHistory.ip }}<br>
              <strong>지역: </strong>{{ loginHistory.location }}<br>
              <strong>로그인 결과: </strong>
              <span v-if="loginHistory.loginResultType == 'SUCCESS'">성공</span>
              <span v-if="loginHistory.loginResultType == 'FAIL'">실패</span>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="login-content ck-content" v-html="loginHistory.detailedMessage"></div>
            </td>
          </tr>
          <tr>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/login-history/list'">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
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

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const idx = route.params.idx;
    // variable
    let loginHistory = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("loginHistory");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/login-historys/read/" + idx,
          {},
      )
          .then((response) => {
            loginHistory.value = response.data;

            // 공통 데이터 설정
            loginHistory.value.createdDate = dayjs(loginHistory.value.createdDate).format("YYYY.MM.DD. HH:mm");
            loginHistory.value.lastModifiedDate = dayjs(loginHistory.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    return {
      // variable
      idx, loginHistory
    }
  }
}
</script>