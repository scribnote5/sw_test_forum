<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="로그인 기록" :paths="['관리자 페이지', '로그인 기록 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="USERNAME">아이디</option>
            <option value="LOCATION">지역</option>
            <option value="LOGIN_RESULT_TYPE">로그인 결과</option>
          </select>
          <input v-if="searchType !== 'LOGIN_RESULT_TYPE'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <select v-if="searchType === 'LOGIN_RESULT_TYPE'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="SUCCESS">성공</option>
            <option value="FAIL">실패</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>
      </div>

      <div class="page-content">
        <table class="table table-hover mobile-table-list mt-4">
          <thead>
          <tr class="d-none d-lg-table-row">
            <th style="width: 5%" class="text-center">번호</th>
            <th style="width: 40%" class="text-center">메시지</th>
            <th style="width: 17.5%" class="text-center">지역</th>
            <th style="width: 10%" class="text-center">접속 시도 아이디</th>
            <th style="width: 10%" class="text-center">로그인 결과</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- loginHistoryList -->
          <tr v-for="(loginHistory, i) in loginHistoryList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ loginHistoryList.totalElements - loginHistoryList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ loginHistoryList.totalElements - loginHistoryList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/login-history/read/' + loginHistory.idx"> {{ loginHistory.message }}</router-link>
              <img v-if="loginHistory.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ loginHistory.location }} </span> <br>
                  <span v-if="loginHistory.loginResultType == 'SUCCESS'" class="mobile-content">성공</span>
                  <span v-if="loginHistory.loginResultType == 'FAIL'" class="mobile-content">실패</span> &nbsp;
                  <span class="mobile-content">{{ loginHistory.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ loginHistory.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ loginHistory.location }}</td>
            <td class="d-none d-lg-table-cell">{{ loginHistory.loginUsername }}</td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="loginHistory.loginResultType == 'SUCCESS'">성공</span>
              <span v-if="loginHistory.loginResultType == 'FAIL'">실패</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ loginHistory.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ loginHistory.views }}</td>
          </tr>
          <tr v-if="loginHistoryList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="loginHistoryList.first" :last="loginHistoryList.last" :totalPages="loginHistoryList.totalPages"
                  :number="loginHistoryList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>
    </div>
  </section>
</template>

<style lang="scss">
</style>

<script>
// components
import Loading from '@/components/common/Loading.vue'
import Breadcrumb from '@/components/common/Breadcrumb.vue'
import Pagination from '@/components/common/Pagination.vue'

// vue.js
import {onBeforeMount, onBeforeUnmount, ref} from 'vue'
import {useStore} from "vuex";
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {createUri} from "@/utils/uri-util";
import {parseApiErrorMsg} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Pagination
  },
  setup() {
    // vue.js
    const store = useStore();
    // variable
    let loginHistoryList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("USERNAME");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("loginHistory");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "LoginHistoryList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "LoginHistoryList") {
        loginHistoryList.value.number = loginHistoryList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }
    })

    /* searchType 변경될 때 */
    const searchTypeChange = (e) => {
      if (e.target.value === 'LOGIN_RESULT_TYPE') {
        searchKeyword.value = 'SUCCESS';
      } else if (previousSearchType === 'LOGIN_RESULT_TYPE' && e.target.value !== 'LOGIN_RESULT_TYPE') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/login-historys/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            loginHistoryList.value = response.data;
            startNumber.value = Math.floor(loginHistoryList.value.number / 10) * 10 + 1;
            endNumber.value = (loginHistoryList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (loginHistoryList.value.totalPages == 0 ? 1 : loginHistoryList.value.totalPages);

            // dayjs
            for (const loginHistory of loginHistoryList.value.content) {
              loginHistory.createdDate = dayjs(loginHistory.createdDate).format("YYYY.MM.DD.");
            }
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    }

    /* 페이지 검색 및 페이지 정보 저장 */
    onBeforeUnmount(() => {
      store.commit("pageInfo/setPageInfo",
          {
            searchType: searchType.value,
            searchKeyword: searchKeyword.value,
            previousSearchType: previousSearchType,
            page: loginHistoryList.value.number,
            pageName: 'LoginHistoryList'
          }
      );
    });

    return {
      // variable
      loginHistoryList, searchType, searchKeyword, startNumber, endNumber,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>