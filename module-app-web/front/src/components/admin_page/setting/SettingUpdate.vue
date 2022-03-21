<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <Breadcrumb page="설정" :paths="['관리자 페이지', '설정 수정']" title=""/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <th colspan="2" class="sub-item-title">설정 정보</th>
          </tr>
          <tr>
            <th>총 MISRA C 규칙 개수<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="number" name="totalMisraCRuleNumber" v-model="totalMisraCRuleNumber" class="form-control">
              <p id="totalMisraCRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 MISRA C++ 규칙 개수<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="number" name="totalMisraCppRuleNumber" v-model="totalMisraCppRuleNumber" class="form-control">
              <p id="totalMisraCppRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 CWE 규칙 개수<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="number" name="totalCweRuleNumber" v-model="totalCweRuleNumber" class="form-control">
              <p id="totalCweRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>개발자 이메일<span class="required-field">*</span></th>
            <td style="overflow: visible">
              <input type="text" name="developerEmail" v-model="developerEmail" class="form-control" @change="validateEmail('developerEmail', developerEmail)">
              <p id="developerEmailErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="sub-item-title">설정 부가정보</th>
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

      <div class="d-flex justify-content-end mx-4 my-5">
        <div class="d-flex">
          <button @click="updatePost()" class="btn btn-main-blue d-flex align-items-center me-2">수정 완료<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
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

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {toast} from '@/assets/plugins/sweetalert2/sweetalert2';
// utils
import {parseErrorMsg, validateEmail, validatePositiveNumber} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb
  },
  setup() {
    // vue.js
    const router = useRouter();

    // variable
    let totalMisraCRuleNumber = ref("");
    let totalMisraCppRuleNumber = ref(0);
    let totalCweRuleNumber = ref("");
    let developerEmail = ref("");
    let activeStatus = ref("");
    let createdByUser = ref([]);
    let createdDate = ref("");
    let lastModifiedByUser = ref([]);
    let lastModifiedDate = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/settings/form",
          {},
      )
          .then((response) => {
            totalMisraCRuleNumber.value = response.data.totalMisraCRuleNumber;
            totalMisraCppRuleNumber.value = response.data.totalMisraCppRuleNumber;
            totalCweRuleNumber.value = response.data.totalCweRuleNumber;
            developerEmail.value = response.data.developerEmail;

            // 공통 데이터 설정
            activeStatus.value = response.data.activeStatus;
            createdByUser.value = response.data.createdByUser;
            createdDate.value = dayjs(response.data.createdDate).format("YYYY.MM.DD. HH:mm");
            lastModifiedByUser.value = response.data.lastModifiedByUser;
            lastModifiedDate.value = dayjs(response.data.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseErrorMsg(error.response);
          })
          .then(() => {
          });
    });

    /* 수정 */
    const updatePost = async () => {
      let isExit;

      if (!validatePositiveNumber("totalMisraCRuleNumber", totalMisraCRuleNumber.value)
          && validatePositiveNumber("totalMisraCppRuleNumber", totalMisraCppRuleNumber.value)
          && validatePositiveNumber("totalCweRuleNumber", totalCweRuleNumber.value)
          && validateEmail("developerEmail", developerEmail.value)) {
        return false;
      }

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/settings",
          {
            totalMisraCRuleNumber: totalMisraCRuleNumber.value,
            totalMisraCppRuleNumber: totalMisraCppRuleNumber.value,
            totalCweRuleNumber: totalCweRuleNumber.value,
            developerEmail: developerEmail.value,
            activeStatus: activeStatus.value,
          },
      )
          .then((response) => {
            isExit = false;
          })
          .catch((error) => {
            parseErrorMsg(error.response.data);
            isExit = true;
          })
          .then(() => {
          });

      toast.fire({
        icon: 'success',
        title: '수정되었습니다.',
      });

      router.push("/setting/update");
    }

    return {
      // variable
      createdByUser, createdDate, lastModifiedByUser, lastModifiedDate, activeStatus,
      totalMisraCRuleNumber, totalMisraCppRuleNumber, totalCweRuleNumber, developerEmail,

      // function
      validateEmail,
      updatePost
    }
  }
}
;
</script>