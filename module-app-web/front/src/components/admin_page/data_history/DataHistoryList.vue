<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="데이터 기록" :paths="['관리자 페이지', '데이터 기록 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="MESSAGE">메시지</option>
            <option value="AUDIT_BOARD">audit 게시판</option>
            <option value="AUDIT_TYPE">audit 유형</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-if="searchType !== 'AUDIT_TYPE'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input ms-2" placeholder="검색어">
          <select v-if="searchType === 'AUDIT_TYPE'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm ms-2" style="width: auto">
            <option value="SELECT">조회</option>
            <option value="INSERT">등록</option>
            <option value="UPDATE">수정</option>
            <option value="DELETE">삭제</option>
            <option value="LIKE">좋아요</option>
            <option value="CANCEL LIKE">좋아요 취소</option>
            <option value="ETC">기타</option>
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
            <th style="width: 12.5%" class="text-center">audit 게시판</th>
            <th style="width: 12.5%" class="text-center">audit 유형</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- dataHistoryList -->
          <tr v-for="(dataHistory, i) in dataHistoryList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ dataHistory.idx }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ dataHistory.idx }}. </span>
              <!-- 공통 -->
              <router-link :to="'/data-history/read/' + dataHistory.idx"> {{ dataHistory.message }}</router-link>
              <img v-if="dataHistory.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ dataHistory.createdByUser.department }} {{ dataHistory.createdByUser.name }} </span> <br>
                  <span v-if="dataHistory.auditType == 'SELECT'" class="mobile-content">조회</span>
                  <span v-if="dataHistory.auditType == 'INSERT'" class="mobile-content">등록</span>
                  <span v-if="dataHistory.auditType == 'UPDATE'" class="mobile-content">수정</span>
                  <span v-if="dataHistory.auditType == 'DELETE'" class="mobile-content">삭제</span>
                  <span v-if="dataHistory.auditType == 'LIKE'" class="mobile-content">좋아요</span>
                  <span v-if="dataHistory.auditType == 'CANCEL LIKE'" class="mobile-content">좋아요 취소</span>
                  <span v-if="dataHistory.auditType == 'ETC'" class="mobile-content">기타</span> &nbsp;
                  <span class="mobile-content">{{ dataHistory.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ dataHistory.views }}</span> &nbsp;
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
              <span v-if="dataHistory.auditType == 'LIKE'" class="mobile-content">좋아요</span>
              <span v-if="dataHistory.auditType == 'CANCEL LIKE'" class="mobile-content">좋아요 취소</span>
              <span v-if="dataHistory.auditType == 'ETC'" class="mobile-content">기타</span>
            </td>
            <td class="d-none d-lg-table-cell">{{ dataHistory.createdByUser.department }} {{ dataHistory.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ dataHistory.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ dataHistory.views }}</td>
          </tr>
          <tr v-if="dataHistoryList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="dataHistoryList.first" :last="dataHistoryList.last" :totalPages="dataHistoryList.totalPages"
                  :number="dataHistoryList.number" :startNumber="startNumber" :endNumber="endNumber"
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
import {onBeforeMount, ref} from 'vue'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// sweetalert2
import {fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {createUri} from "@/utils/uri-util";
import {parseErrorMsg} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Pagination
  },
  setup() {
    // variable
    let dataHistoryList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("MESSAGE");
    let searchKeyword = ref("");
    let access = ref("");
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("dataHistory");

      await searchList({"page": 1});
    })

    /* searchType 변경될 때 */
    const searchTypeChange = (e) => {
      if (e.target.value === 'AUDIT_TYPE') {
        searchKeyword.value = 'INSERT';
      } else if (previousSearchType === 'AUDIT_TYPE' && e.target.value !== 'AUDIT_TYPE') {
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
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/data-historys/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            dataHistoryList.value = response.data;
            startNumber.value = Math.floor(dataHistoryList.value.number / 10) * 10 + 1;
            endNumber.value = (dataHistoryList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (dataHistoryList.value.totalPages == 0 ? 1 : dataHistoryList.value.totalPages);

            // dayjs
            for (const dataHistory of dataHistoryList.value.content) {
              dataHistory.createdDate = dayjs(dataHistory.createdDate).format("YYYY.MM.DD.");
            }
          })
          .catch((error) => {
            parseErrorMsg(error.response);
          })
          .then(() => {
          });
    }

    return {
      // variable
      dataHistoryList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>