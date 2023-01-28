<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="보고서 및 회의 자료" :paths="['나머지 자료', '보고서 및 회의 자료 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="CONTENT">내용</option>
            <option value="CATEGORY">유형</option>
            <option value="CREATED_BY">작성자</option>
          </select>


          <input v-if="searchType !== 'CATEGORY'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <select v-if="searchType === 'CATEGORY'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="PROPOSAL">제안서</option>
            <option value="BUSINESS_REPORT">사업 계획서</option>
            <option value="RELIABILITY_TEST_REPORT">신뢰성시험 보고서</option>
            <option value="CONFERENCE">회의 자료</option>
            <option value="ETC">기타 자료</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>

        <div v-if="searchType === 'CATEGORY'" class="d-flex justify-content-end mt-2">
          <select v-model="searchType2" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-on:keyup.enter="searchList" v-model="searchKeyword2" name="inputSearchKeyword2" type="search" class="custom-search-input mx-2" placeholder="검색어">
        </div>
      </div>

      <div class="page-content">
        <table class="table table-hover mobile-table-list mt-4">
          <thead>
          <tr class="d-none d-lg-table-row">
            <th style="width: 5%" class="text-center">번호</th>
            <th style="width: 50%" class="text-center">제목</th>
            <th style="width: 15%" class="text-center">유형</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- reportListByPriority -->
          <tr v-for="(report, i) in reportListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <router-link :to="'/report/read/' + report.idx"> {{ report.title }}</router-link>
              <span class="comment-count">{{ report.commentDtoCount }}</span>
              <img v-if="report.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ report.createdByUser.department }} {{ report.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ report.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ report.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="report.category == 'PROPOSAL'">제안서</span>
              <span v-if="report.category == 'BUSINESS_REPORT'">사업 계획서</span>
              <span v-if="report.category == 'RELIABILITY_TEST_REPORT'">신뢰성시험 보고서</span>
              <span v-if="report.category == 'CONFERENCE'">회의 자료</span>
              <span v-if="report.category == 'ETC'">기타 자료</span>
            </td>
            <td class="d-none d-lg-table-cell">{{ report.createdByUser.department }} {{ report.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ report.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ report.views }}</td>
          </tr>

          <!-- reportList -->
          <tr v-for="(report, i) in reportList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ reportList.totalElements - reportList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ reportList.totalElements - reportList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/report/read/' + report.idx"> {{ report.title }}</router-link>
              <span class="comment-count">{{ report.commentDtoCount }}</span>
              <img v-if="report.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ report.createdByUser.department }} {{ report.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ report.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ report.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="report.category == 'PROPOSAL'">제안서</span>
              <span v-if="report.category == 'BUSINESS_REPORT'">사업 계획서</span>
              <span v-if="report.category == 'RELIABILITY_TEST_REPORT'">신뢰성시험 보고서</span>
              <span v-if="report.category == 'CONFERENCE'">회의 자료</span>
              <span v-if="report.category == 'ETC'">기타 자료</span>
            </td>
            <td class="d-none d-lg-table-cell">{{ report.createdByUser.department }} {{ report.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ report.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ report.views }}</td>
          </tr>

          <tr v-if="reportList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="reportList.first" :last="reportList.last" :totalPages="reportList.totalPages"
                  :number="reportList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/report/write'">
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
import {isEmpty} from "@/utils/empty-util";

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
    let reportListByPriority = ref([]);
    let reportList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let searchType2 = ref("TITLE");
    let searchKeyword2 = ref("");
    let access = ref("");
    let pageParam = {"page": 1};
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("report");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "ReportList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "ReportList") {
        reportList.value.number = reportList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/reports/list-access-authority",
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

    /* searchType 변경될 때 */
    const searchTypeChange = (e) => {
      if (e.target.value === 'CATEGORY') {
        searchKeyword.value = 'PROPOSAL';
      } else if (previousSearchType === 'CATEGORY' && e.target.value !== 'CATEGORY') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 있는 list
      if (pageParam.page === 1 && isEmpty(searchKeyword.value)) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/reports/high-priority-list",
            {},
        )
            .then((response) => {
              reportListByPriority.value = response.data;
              // dayjs
              for (const report of reportListByPriority.value) {
                report.createdDate = dayjs(report.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        reportListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value,
        "searchType2": searchType2.value,
        "searchKeyword2": searchKeyword2.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/reports/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // reportList.value.content.length = 0;
            reportList.value = response.data;
            startNumber.value = Math.floor(reportList.value.number / 10) * 10 + 1;
            endNumber.value = (reportList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (reportList.value.totalPages == 0 ? 1 : reportList.value.totalPages);

            // dayjs
            for (const report of reportList.value.content) {
              report.createdDate = dayjs(report.createdDate).format("YYYY.MM.DD.");
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
            page: reportList.value.number,
            pageName: 'ReportList'
          }
      );
    });

    return {
      // variable
      reportListByPriority, reportList, searchType, searchKeyword, searchType2, searchKeyword2, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>