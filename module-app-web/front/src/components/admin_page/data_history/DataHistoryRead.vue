<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="데이터 기록" :paths="['데이터 페이지', '데이터 기록 보기']" title=""/>

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
              <h2 class="mobile-title">{{ dataHistory.message }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ dataHistory.createdByUser.department }} {{ dataHistory.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ dataHistory.createdDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ dataHistory.views }}</span>
              </div>
            </td>
          </tr>

          <!-- Desktop -->
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>audit 클래스</th>
            <td>
              {{ dataHistory.auditBoard }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>audit 유형</th>
            <td>
              <span v-if="dataHistory.auditType == 'SELECT'">조회</span>
              <span v-if="dataHistory.auditType == 'INSERT'">등록</span>
              <span v-if="dataHistory.auditType == 'UPDATE'">수정</span>
              <span v-if="dataHistory.auditType == 'DELETE'">삭제</span>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>메시지</th>
            <td>
              {{ dataHistory.message }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>상세 메시지</th>
            <td>
              {{ dataHistory.detailedMessage }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>audit 클래스: </strong>{{ dataHistory.auditBoard }}<br>
              <strong>audit 유형: </strong>
              <span v-if="dataHistory.auditType == 'SELECT'">조회</span>
              <span v-if="dataHistory.auditType == 'INSERT'">등록</span>
              <span v-if="dataHistory.auditType == 'UPDATE'">수정</span>
              <span v-if="dataHistory.auditType == 'DELETE'">삭제</span><br>
              <strong>메시지: </strong>{{ dataHistory.message }}<br>
              <strong>상세 메시지: </strong>{{ dataHistory.detailedMessage }}<br>
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="dataHistory.content"></div>
            </td>
          </tr>

          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-between mx-3 my-5">
        <div class="d-flex">
          <router-link :to="'/data-history/list'">
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
    let dataHistory = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("dataHistory");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/data-historys/read/" + idx,
          {},
      )
          .then((response) => {
            dataHistory.value = response.data;
            dataHistory.value.content = dataHistory.value.content;

            // 공통 데이터 설정
            dataHistory.value.createdDate = dayjs(dataHistory.value.createdDate).format("YYYY.MM.DD. HH:mm");
            dataHistory.value.lastModifiedDate = dayjs(dataHistory.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    return {
      // variable
      idx, dataHistory
    }
  }
}
</script>