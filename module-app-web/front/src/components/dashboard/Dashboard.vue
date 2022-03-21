<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <div class="container-fluid">
      <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <div class="d-flex flex-row ps-1">
          <h3>대시보드</h3>
        </div>

        <div class="d-flex flex-row justify-content-end pe-1">
          <div class="link-wrapper mt-lg-n5 mt-md-n0 ">
            <a :href="'mailto:' + setting.developerEmail">
              <img :src="require(`@/assets/images/mail.svg`)" class="me-1"/>개발자에게 메일 보내기
            </a>
          </div>
        </div>
      </div>

      <div class="row mt-4">
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #4e73df; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <img :src="require(`@/assets/images/users.svg`)" class="me-1"/>
                가입 회원 수
              </h5>
              <div class="text-center">
                <span class="card-count">{{ usersCount }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #1cc88a; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <img :src="require(`@/assets/images/posts.svg`)" class="me-1"/>
                등록된 게시글 수
              </h5>
              <div class="text-center">
                <span class="card-count">{{ postsCount }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #36b9cc; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <div class="dropdown">
                  <img :src="require(`@/assets/images/calendar.svg`)"/>
                  <button class="btn btn-sm dropdown-toggle" type="button" id="dropdownStaticsButton" data-bs-toggle="dropdown" aria-expanded="false">
                    하루 동안 접속한 회원 수
                  </button>
                  <ul class="dropdown-menu" aria-labelledby="dropdownStaticsButton" @click="changeStatics($event)">
                    <li><span class="dropdown-item">하루 동안 접속한 회원 수</span></li>
                    <li><span class="dropdown-item">7일간 접속한 회원 수</span></li>
                    <li><span class="dropdown-item">28일간 접속한 회원 수</span></li>
                    <li><span class="dropdown-item">하루 동안 등록된 게시글 수</span></li>
                    <li><span class="dropdown-item">7일간 등록된 게시글 수</span></li>
                    <li><span class="dropdown-item">28일간 등록된 게시글 수</span></li>
                  </ul>
                </div>
              </h5>
              <div class="text-center">
                <span class="card-count" id="staticsCount">{{ loginUsersCountBeforeYesterday }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #f6c23e; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <img :src="require(`@/assets/images/hard-drive.svg`)" class="me-1"/>
                메모리 공간
              </h5>
              <div v-for="(diskUtil, i) in diskUtilList" :key="i">
                <span class="memory-name me-1">{{ diskUtil.driverName }}</span>
                <span class="memory-space">{{ diskUtil.totalSize }} / {{ diskUtil.usedSize }}</span> 사용
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-6">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0">공지사항</h5>
              <router-link to="/notice/list">
                <img :src="require(`@/assets/images/more-vertical.svg`)"/>
              </router-link>
            </div>

            <div class="card-body py-0">
              <table class="table table-hover mobile-table-list">
                <thead>
                <tr class="d-none d-lg-table-row">
                  <th style="width: 80%" class="text-center">제목</th>
                  <th style="width: 20%" class="text-center">작성일</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(notice, i) in noticeList" :key="i">
                  <td>
                    <!-- 공통 -->
                    <img v-if="notice.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                    <router-link :to="'/notice/read/' + notice.idx"> {{ notice.title }}</router-link>
                    <!-- Mobile -->
                    <div class="d-inline d-lg-none">
                      <div>
                        <span class="mobile-content">{{ notice.createdDate }}</span>
                      </div>
                    </div>
                  </td>
                  <!-- Desktop -->
                  <td class="d-none d-lg-table-cell text-center">{{ notice.createdDate }}</td>
                </tr>

                <tr v-if="noticeList.length === 0">
                  <td colspan="10" class="no-posts-table">
                    등록된 게시글이 없습니다.
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="col-lg-6">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0">정적시험 규칙 현황</h5>
            </div>

            <div class="card-body py-0">
              <div class="my-3">
                <h6>작성된 MISRA C 규칙: {{ misraCPostsCount }} / {{ setting.totalMisraCRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredMisraCPostsRatio" class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredMisraCPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-3">
                <h6>작성된 MISRA C++ 규칙: {{ misraCppPostsCount }} / {{ setting.totalMisraCppRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredMisraCppPostsRatio" class="progress-bar bg-success progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredMisraCppPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-3">
                <h6>작성된 CWE 규칙: {{ cwePostsCount }} / {{ setting.totalCweRuleNumber }} </h6>
                <div class="progress ">
                  <div id="registeredCwePostsRatio" class="progress-bar bg-info progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredCwePostsRatio }}%
                  </div>
                </div>
              </div>

              <hr>

              <div class="my-3">
                <h6>작성된 MISRA C 가이드라인 개수: {{ misraCGuidelinePostsCount }}</h6>
              </div>
              <div class="my-3">
                <h6>작성된 MISRA C++ 가이드라인 개수: {{ misraCppGuidelinePostsCount }}</h6>
              </div>
              <div class="my-3">
                <h6>작성된 CWE 가이드라인 개수: {{ cweGuidelinePostsCount }}</h6>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-12">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0">로그인 기록</h5>
              <router-link to="/login-history/list">
                <img :src="require(`@/assets/images/more-vertical.svg`)"/>
              </router-link>
            </div>

            <div class="card-body py-0">
              <table class="table table-hover mobile-table-list">
                <thead>
                <tr class="d-none d-lg-table-row">
                  <th style="width: 55%" class="text-center">메시지</th>
                  <th style="width: 17.5%" class="text-center">지역</th>
                  <th style="width: 12.5%" class="text-center">로그인 결과</th>
                  <th style="width: 15%" class="text-center">작성일</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(loginHistory, i) in loginHistoryList" :key="i">
                  <td>
                    <!-- 공통 -->
                    <router-link :to="'/login-history/read/' + loginHistory.idx"> {{ loginHistory.message }}</router-link>
                    <!-- Mobile -->
                    <div class="d-inline d-lg-none">
                      <div>
                        <span class="mobile-content">{{ loginHistory.location }} </span> <br>
                        <span v-if="loginHistory.loginResultType == 'SUCCESS'" class="mobile-content">성공</span>
                        <span v-if="loginHistory.loginResultType == 'FAIL'" class="mobile-content">실패</span> &nbsp;
                        <span class="mobile-content">{{ loginHistory.createdDate }}</span> &nbsp;
                      </div>
                    </div>
                  </td>
                  <!-- Desktop -->
                  <td class="d-none d-lg-table-cell">{{ loginHistory.location }}</td>
                  <td class="d-none d-lg-table-cell text-center">
                    <span v-if="loginHistory.loginResultType == 'SUCCESS'">성공</span>
                    <span v-if="loginHistory.loginResultType == 'FAIL'">실패</span>
                  </td>
                  <td class="d-none d-lg-table-cell text-center">{{ loginHistory.createdDate }}</td>
                </tr>
                </tbody>

                <tr v-if="loginHistoryList.length === 0">
                  <td colspan="10" class="no-posts-table">
                    등록된 게시글이 없습니다.
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-12">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0 font-weight-bold">데이터 기록</h5>
              <div class="dropdown no-arrow">
                <router-link to="/data-history/list">
                  <img :src="require(`@/assets/images/more-vertical.svg`)"/>
                </router-link>
              </div>
            </div>

            <div class="card-body py-0">
              <table class="table table-hover mobile-table-list">
                <thead>
                <tr class="d-none d-lg-table-row">
                  <th style="width: 55%" class="text-center">메시지</th>
                  <th style="width: 17.5%" class="text-center">audit 게시판</th>
                  <th style="width: 12.5%" class="text-center">audit 유형</th>
                  <th style="width: 15%" class="text-center">작성일</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(dataHistory, i) in dataHistoryList" :key="i">
                  <td>
                    <!-- 공통 -->
                    <router-link :to="'/data-history/read/' + dataHistory.idx"> {{ dataHistory.message }}</router-link>
                    <!-- Mobile -->
                    <div class="d-inline d-lg-none">
                      <div>
                        <span class="mobile-content">{{ dataHistory.auditBoard }} </span> <br>
                        <span v-if="dataHistory.auditType == 'SELECT'" class="mobile-content">조회</span>
                        <span v-if="dataHistory.auditType == 'INSERT'" class="mobile-content">등록</span>
                        <span v-if="dataHistory.auditType == 'UPDATE'" class="mobile-content">수정</span>
                        <span v-if="dataHistory.auditType == 'DELETE'" class="mobile-content">삭제</span> &nbsp;
                        <span class="mobile-content">{{ dataHistory.createdDate }}</span> &nbsp;
                      </div>
                    </div>
                  </td>
                  <!-- Desktop -->
                  <td class="d-none d-lg-table-cell">{{ dataHistory.auditBoard }}</td>
                  <td class="d-none d-lg-table-cell text-center">
                    <span v-if="dataHistory.auditType == 'SELECT'">조회</span>
                    <span v-if="dataHistory.auditType == 'INSERT'">등록</span>
                    <span v-if="dataHistory.auditType == 'UPDATE'">수정</span>
                    <span v-if="dataHistory.auditType == 'DELETE'">삭제</span>
                  </td>
                  <td class="d-none d-lg-table-cell text-center">{{ dataHistory.createdDate }}</td>
                </tr>

                <tr v-if="dataHistoryList.length === 0">
                  <td colspan="10" class="no-posts-table">
                    등록된 게시글이 없습니다.
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

    </div>
  </section>
</template>

<style lang="scss">
.link-wrapper {
  a {
    color: $main-blue !important;
  }
}

.dropdown-toggle::after {
  color: black;
}

.card-count {
  color: $darkest-main-grey;
  text-align: center;
  font-size: $largest-font-size;
}

.memory-name {
  font-weight: $strong-font-weight;
}

.memory-space {
  color: $darkest-main-grey;
}
</style>

<script>
// components
import Loading from '@/components/common/Loading.vue'

// vue.js
import {onBeforeMount, ref} from "vue";

import axios from "axios";
// day.js
import dayjs from 'dayjs'
// utils
import {parseErrorMsg} from "@/utils/validation-util";

export default {
  components: {
    Loading
  },
  setup() {
    // variable
    let usersCount = ref("");
    let postsCount = ref("");
    let loginUsersCountBeforeYesterday = ref("");
    let loginUsersCountBeforeWeek = ref("");
    let loginUsersCountBeforeMonth = ref("");
    let postsCountBeforeYesterday = ref("");
    let postsCountBeforeWeek = ref("");
    let postsCountBeforeMonth = ref("");
    let misraCPostsCount = ref(0);
    let misraCppPostsCount = ref(0);
    let cwePostsCount = ref(0);
    let misraCGuidelinePostsCount = ref();
    let misraCppGuidelinePostsCount = ref(0);
    let cweGuidelinePostsCount = ref(0);
    let setting = ref([]);
    let diskUtilList = ref([]);
    let noticeList = ref([]);
    let loginHistoryList = ref([]);
    let dataHistoryList = ref([]);

    let registeredMisraCPostsRatio = ref(0);
    let registeredMisraCppPostsRatio = ref(0);
    let registeredCwePostsRatio = ref(0);

    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/dashboards",
          {},
      )
          .then((response) => {
            usersCount.value = response.data.usersCount;
            postsCount.value = response.data.postsCount;
            loginUsersCountBeforeYesterday.value = response.data.loginUsersCountBeforeYesterday;
            loginUsersCountBeforeWeek.value = response.data.loginUsersCountBeforeWeek;
            loginUsersCountBeforeMonth.value = response.data.loginUsersCountBeforeMonth;
            postsCountBeforeYesterday.value = response.data.postsCountBeforeYesterday;
            postsCountBeforeWeek.value = response.data.postsCountBeforeWeek;
            postsCountBeforeMonth.value = response.data.postsCountBeforeMonth;
            misraCPostsCount.value = response.data.misraCPostsCount;
            misraCppPostsCount.value = response.data.misraCppPostsCount;
            cwePostsCount.value = response.data.cwePostsCount;
            misraCGuidelinePostsCount.value = response.data.misraCGuidelinePostsCount;
            misraCppGuidelinePostsCount.value = response.data.misraCppGuidelinePostsCount;
            cweGuidelinePostsCount.value = response.data.cweGuidelinePostsCount;
            setting.value = response.data.settingDto;
            diskUtilList.value = response.data.diskUtilList;
            noticeList.value = response.data.noticeDtoList;
            loginHistoryList.value = response.data.loginHistoryDtoList;
            dataHistoryList.value = response.data.dataHistoryDtoList;

            // 정적시험 규칙 비율 계산
            registeredMisraCPostsRatio.value = (misraCPostsCount.value / setting.value.totalMisraCRuleNumber * 100).toFixed(1);
            registeredMisraCppPostsRatio.value = (misraCppPostsCount.value / setting.value.totalMisraCppRuleNumber * 100).toFixed(1);
            registeredCwePostsRatio.value = (cwePostsCount.value / setting.value.totalCweRuleNumber * 100).toFixed(1);

            document.getElementById("registeredMisraCPostsRatio").style.width = registeredMisraCPostsRatio.value + "%";
            document.getElementById("registeredMisraCppPostsRatio").style.width = registeredMisraCppPostsRatio.value + "%";
            document.getElementById("registeredCwePostsRatio").style.width = registeredCwePostsRatio.value + "%";

            // dayjs
            for (const notice of noticeList.value) {
              notice.createdDate = dayjs(notice.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const loginHistory of loginHistoryList.value) {
              loginHistory.createdDate = dayjs(loginHistory.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const dataHistory of dataHistoryList.value) {
              dataHistory.createdDate = dayjs(dataHistory.createdDate).format("YYYY.MM.DD.");
            }
          })
          .catch((error) => {
            parseErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    /* 통계 dropdown 변경 */
    const changeStatics = (e) => {
      switch (e.target.textContent) {
        case "하루 동안 접속한 회원 수":
          document.getElementById("staticsCount").innerText = loginUsersCountBeforeYesterday.value;
          document.getElementById("dropdownStaticsButton").innerText = "하루 동안 접속한 회원 수";
          break;
        case "7일간 접속한 회원 수":
          document.getElementById("staticsCount").innerText = loginUsersCountBeforeWeek.value;
          document.getElementById("dropdownStaticsButton").innerText = "7일간 접속한 회원 수";
          break;
        case"28일간 접속한 회원 수":
          document.getElementById("staticsCount").innerText = loginUsersCountBeforeMonth.value;
          document.getElementById("dropdownStaticsButton").innerText = "28일간 접속한 회원 수";
          break;
        case"하루 동안 등록된 게시글 수":
          document.getElementById("staticsCount").innerText = postsCountBeforeYesterday.value;
          document.getElementById("dropdownStaticsButton").innerText = "하루 동안 등록된 게시글 수";
          break;
        case"7일간 등록된 게시글 수":
          document.getElementById("staticsCount").innerText = postsCountBeforeWeek.value;
          document.getElementById("dropdownStaticsButton").innerText = "7일간 등록된 게시글 수";
          break;
        case"28일간 등록된 게시글 수":
          document.getElementById("staticsCount").innerText = postsCountBeforeMonth.value;
          document.getElementById("dropdownStaticsButton").innerText = "28일간 등록된 게시글 수";
          break;
      }
    };
    return {
      // variable
      usersCount,
      postsCount,
      loginUsersCountBeforeYesterday,
      loginUsersCountBeforeWeek,
      loginUsersCountBeforeMonth,
      postsCountBeforeYesterday,
      postsCountBeforeWeek,
      postsCountBeforeMonth,
      misraCPostsCount,
      misraCppPostsCount,
      cwePostsCount,
      misraCGuidelinePostsCount,
      misraCppGuidelinePostsCount,
      cweGuidelinePostsCount,
      setting,
      diskUtilList,
      noticeList,
      loginHistoryList,
      dataHistoryList,
      registeredMisraCPostsRatio,
      registeredMisraCppPostsRatio,
      registeredCwePostsRatio,

      // function
      changeStatics
    }
  }
}
</script>

