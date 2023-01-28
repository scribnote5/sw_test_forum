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
            <td>
              <input type="number" name="totalMisraCRuleNumber" v-model="totalMisraCRuleNumber" class="form-control">
              <p id="totalMisraCRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 MISRA C++ 규칙 개수<span class="required-field">*</span></th>
            <td>
              <input type="number" name="totalMisraCppRuleNumber" v-model="totalMisraCppRuleNumber" class="form-control">
              <p id="totalMisraCppRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 CWE C/C++ 규칙 개수<span class="required-field">*</span></th>
            <td>
              <input type="number" name="totalCweRuleNumber" v-model="totalCweRuleNumber" class="form-control">
              <p id="totalCweRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 Java Code Conventions 규칙 개수<span class="required-field">*</span></th>
            <td>
              <input type="number" name="totalJavaCodeConventionsRuleNumber" v-model="totalJavaCodeConventionsRuleNumber" class="form-control">
              <p id="totalJavaCodeConventionsRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 CWE Java 규칙 개수<span class="required-field">*</span></th>
            <td>
              <input type="number" name="totalCweJavaRuleNumber" v-model="totalCweJavaRuleNumber" class="form-control">
              <p id="totalCweJavaRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 C# Coding Convention 규칙 개수<span class="required-field">*</span></th>
            <td>
              <input type="number" name="totalStyleCopRuleNumber" v-model="totalStyleCopRuleNumber" class="form-control">
              <p id="totalStyleCopRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>총 .NET Framework Design Guideline 규칙 개수<span class="required-field">*</span></th>
            <td>
              <input type="number" name="totalFxCopRuleNumber" v-model="totalFxCopRuleNumber" class="form-control">
              <p id="totalFxCopRuleNumberErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>초기 배포일<span class="required-field">*</span></th>
            <td>
              <input type="date" name="initialReleaseDate" v-model="initialReleaseDate" class="form-control">
              <p id="initialReleaseDateErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>최신 배포일<span class="required-field">*</span></th>
            <td>
              <input type="date" name="lastReleaseDate" v-model="lastReleaseDate" class="form-control">
              <p id="lastReleaseDateErrorMessage" class="error-message"></p>
            </td>
          </tr>
          <tr>
            <th>담당자 이메일<span class="required-field">*</span></th>
            <td>
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

      <div class="d-flex justify-content-end mx-3 my-5">
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
import {parseApiErrorMsg, validateEmail, validatePositiveNumber} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb
  },
  setup() {
    // vue.js
    const router = useRouter();

    // variable
    let totalMisraCRuleNumber = ref(0);
    let totalMisraCppRuleNumber = ref(0);
    let totalCweRuleNumber = ref(0);
    let totalJavaCodeConventionsRuleNumber = ref(0);
    let totalCweJavaRuleNumber = ref(0);
    let totalStyleCopRuleNumber = ref(0);
    let totalFxCopRuleNumber = ref(0);
    let initialReleaseDate = ref("");
    let lastReleaseDate = ref("");
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
            totalJavaCodeConventionsRuleNumber.value = response.data.totalJavaCodeConventionsRuleNumber;
            totalCweJavaRuleNumber.value = response.data.totalCweJavaRuleNumber;
            totalStyleCopRuleNumber.value = response.data.totalStyleCopRuleNumber;
            totalFxCopRuleNumber.value = response.data.totalFxCopRuleNumber;
            initialReleaseDate.value = response.data.initialReleaseDate;
            lastReleaseDate.value = response.data.lastReleaseDate;
            developerEmail.value = response.data.developerEmail;

            // 공통 데이터 설정
            activeStatus.value = response.data.activeStatus;
            createdByUser.value = response.data.createdByUser;
            createdDate.value = dayjs(response.data.createdDate).format("YYYY.MM.DD. HH:mm");
            lastModifiedByUser.value = response.data.lastModifiedByUser;
            lastModifiedDate.value = dayjs(response.data.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
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

      if (!validatePositiveNumber("totalMisraCRuleNumber", totalMisraCRuleNumber.value)
          && validatePositiveNumber("totalMisraCppRuleNumber", totalMisraCppRuleNumber.value)
          && validatePositiveNumber("totalCweRuleNumber", totalCweRuleNumber.value)
          && validatePositiveNumber("totalJavaCodeConventionsRuleNumber", totalJavaCodeConventionsRuleNumber.value)
          && validatePositiveNumber("totalCweJavaRuleNumber", totalCweJavaRuleNumber.value)
          && validatePositiveNumber("totalStyleCopRuleNumber", totalStyleCopRuleNumber.value)
          && validatePositiveNumber("totalFxCopRuleNumber", totalFxCopRuleNumber.value)
          && validateEmail("developerEmail", developerEmail.value, 255)) {
        return false;
      }

      axios.put(process.env.VUE_APP_MODULE_APP_API_URL + "/api/settings",
          {
            totalMisraCRuleNumber: totalMisraCRuleNumber.value,
            totalMisraCppRuleNumber: totalMisraCppRuleNumber.value,
            totalCweRuleNumber: totalCweRuleNumber.value,
            totalJavaCodeConventionsRuleNumber: totalJavaCodeConventionsRuleNumber.value,
            totalCweJavaRuleNumber: totalCweJavaRuleNumber.value,
            totalStyleCopRuleNumber: totalStyleCopRuleNumber.value,
            totalFxCopRuleNumber: totalFxCopRuleNumber.value,
            initialReleaseDate: initialReleaseDate.value,
            lastReleaseDate: lastReleaseDate.value,
            developerEmail: developerEmail.value,
            activeStatus: activeStatus.value,
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

      toast.fire({
        icon: 'success',
        title: '수정되었습니다.',
      });

      router.push("/setting/update");
    }

    return {
      // variable
      createdByUser, createdDate, lastModifiedByUser, lastModifiedDate, activeStatus,
      totalMisraCRuleNumber, totalMisraCppRuleNumber, totalCweRuleNumber, totalJavaCodeConventionsRuleNumber, totalCweJavaRuleNumber, totalStyleCopRuleNumber, totalFxCopRuleNumber, initialReleaseDate, lastReleaseDate, developerEmail,

      // function
      validateEmail,
      updatePost
    }
  }
}
;
</script>