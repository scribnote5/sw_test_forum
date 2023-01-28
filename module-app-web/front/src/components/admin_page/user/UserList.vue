<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="사용자" :paths="['사용자 페이지', '사용자 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="CONTENT">내용</option>
          </select>
          <input v-model="searchKeyword" v-on:keyup.enter="searchList" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>
      </div>

      <div class="page-content">
        <table class="table table-hover mobile-table-list mt-4">
          <thead>
          <tr class="d-none d-lg-table-row">
            <th style="width: 5%" class="text-center">번호</th>
            <th style="width: 12.5%" class="text-center">이름</th>
            <th style="width: 15%" class="text-center">직위</th>
            <th style="width: 25%" class="text-center">부서</th>
            <th style="width: 12.5%" class="text-center">권한</th>
            <th style="width: 12.5%" class="text-center">재직 여부</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- userList -->
          <tr v-for="(user, i) in userList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ userList.totalElements - userList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ userList.totalElements - userList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/user/read/' + user.idx"> {{ user.name }}</router-link>
              <img v-if="user.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">
                    {{ user.department }}
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
                  </span> <br>
                  <span class="mobile-content">
                    <span v-if="user.authorityType == 'ROOT'">최고 관리자</span>
                    <span v-if="user.authorityType == 'MANAGER'">관리자</span>
                    <span v-if="user.authorityType == 'GENERAL'">일반 회원</span>
                    <span v-if="user.authorityType == 'READER'">읽기 회원</span>
                    <span v-if="user.authorityType == 'NON_USER'">비회원</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ user.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ user.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">
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
            </td>
            <td class="d-none d-lg-table-cell">{{ user.department }}</td>
            <td class="d-none d-lg-table-cell">
              <span v-if="user.authorityType == 'ROOT'">최고 관리자</span>
              <span v-if="user.authorityType == 'MANAGER'">관리자</span>
              <span v-if="user.authorityType == 'GENERAL'">일반 회원</span>
              <span v-if="user.authorityType == 'READER'">읽기 회원</span>
              <span v-if="user.authorityType == 'NON_USER'">비회원</span>
            </td>
            <td class="d-none d-lg-table-cell">
              <span v-if="user.userStatus == 'IN_OFFICE'">재직중</span>
              <span v-if="user.userStatus == 'RETIREE'">퇴사</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ user.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ user.views }}</td>
          </tr>

          <tr v-if="userList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="userList.first" :last="userList.last" :totalPages="userList.totalPages"
                  :number="userList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/user/write'">
          <button class="btn btn-main-blue d-flex align-items-center">작성<img :src="require(`@/assets/images/write-white.svg`)" class="ms-2"></button>
        </router-link>
      </div>
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
import {onBeforeMount,onBeforeUnmount, ref} from 'vue'
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
    let userListByPriority = ref([]);
    let userList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("user");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "UserList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "UserList") {
        userList.value.number = userList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/list-access-authority",
          {},
      )
          .then((response) => {
            access.value = response.data;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/users/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // userList.value.content.length = 0;
            userList.value = response.data;
            startNumber.value = Math.floor(userList.value.number / 10) * 10 + 1;
            endNumber.value = (userList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (userList.value.totalPages == 0 ? 1 : userList.value.totalPages);

            // dayjs
            for (const user of userList.value.content) {
              user.createdDate = dayjs(user.createdDate).format("YYYY.MM.DD.");
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
            page: userList.value.number,
            pageName: 'UserList'
          }
      );
    });


    return {
      // variable
      userListByPriority, userList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList
    }
  }
}
</script>