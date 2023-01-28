<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="동적시험 미달성 코드 분석" :paths="['나머지 자료', '동적시험 미달성 코드 분석 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="HASH_TAGS">해시태그</option>
            <option value="PROJECT_INFORMATION">프로젝트 정보</option>
            <option value="GUIDELINE_RESULT">가이드라인 결과</option>
            <option value="TOOL_INFORMATION">도구 정보</option>
            <option value="COMPILER_INFORMATION">컴파일러</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-if="searchType !== 'GUIDELINE_RESULT'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <select v-if="searchType === 'GUIDELINE_RESULT'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="COMPLETED">수정 완료</option>
            <option value="SOFTWARE_DEPENSIVE_CODE">방어 코드</option>
            <option value="HARDWARE_FAILURE">하드웨어 고장</option>
            <option value="INFINITE_LOOP">무한 반복문</option>
            <option value="SPECIAL_HARDWARE_VALUE">특수한 하드웨어 값</option>
            <option value="INVALID_HARDWARE_VALUE">잘못된 하드웨어 값</option>
            <option value="SPECIAL_ENVIRONMENT">특수한 시험 환경</option>
            <option value="REAL_TIME_DEGRADATION">탐침 코드로 실시간성 저하</option>
            <option value="TOOL_ERROR">도구 오류</option>
            <option value="OTHER_EXCEPTION">기타 예외 처리</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>

        <div v-if="searchType === 'GUIDELINE_RESULT'" class="d-flex justify-content-end mt-2">
          <select v-model="searchType2" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="HASH_TAGS">해시태그</option>
            <option value="PROJECT_INFORMATION">프로젝트 정보</option>
            <option value="TOOL_INFORMATION">도구 정보</option>
            <option value="COMPILER_INFORMATION">컴파일러</option>
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
            <th style="width: 40%" class="text-center">제목</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 15%" class="text-center">가이드라인 결과</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- dynamicTestList -->
          <tr v-for="(dynamicTest, i) in dynamicTestList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ dynamicTestList.totalElements - dynamicTestList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ dynamicTestList.totalElements - dynamicTestList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/dynamic-test/read/' + dynamicTest.idx"> {{ dynamicTest.title }}</router-link>
              <span class="comment-count">{{ dynamicTest.commentDtoCount }}</span>
              <span v-if="dynamicTest.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ dynamicTest.likeCountInList }}</span></span>
              <img v-if="dynamicTest.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ dynamicTest.createdByUser.department }} {{ dynamicTest.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ dynamicTest.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ dynamicTest.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="dynamicTest.hashTags"></HashTags></span> &nbsp;
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'COMPLETED'">수정 완료</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'SOFTWARE_DEPENSIVE_CODE'">방어 코드</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'HARDWARE_FAILURE'">하드웨어 고장</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'INFINITE_LOOP'">무한 반복문</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'SPECIAL_HARDWARE_VALUE'">특수한 하드웨어 값</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'INVALID_HARDWARE_VALUE'">잘못된 하드웨어 값</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'SPECIAL_ENVIRONMENT'">특수한 시험 환경</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'REAL_TIME_DEGRADATION'">탐침 코드로 실시간성 저하</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'TOOL_ERROR'">도구 오류</span>
                  <span class="mobile-content" v-if="dynamicTest.guidelineResult == 'OTHER_EXCEPTION'">기타 예외 처리</span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ dynamicTest.createdByUser.department }} {{ dynamicTest.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="dynamicTest.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="dynamicTest.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="dynamicTest.guidelineResult == 'SOFTWARE_DEPENSIVE_CODE'">방어 코드</span>
              <span v-if="dynamicTest.guidelineResult == 'HARDWARE_FAILURE'">하드웨어 고장</span>
              <span v-if="dynamicTest.guidelineResult == 'INFINITE_LOOP'">무한 반복문</span>
              <span v-if="dynamicTest.guidelineResult == 'SPECIAL_HARDWARE_VALUE'">특수한 하드웨어 값</span>
              <span v-if="dynamicTest.guidelineResult == 'INVALID_HARDWARE_VALUE'">잘못된 하드웨어 값</span>
              <span v-if="dynamicTest.guidelineResult == 'SPECIAL_ENVIRONMENT'">특수한 시험 환경</span>
              <span v-if="dynamicTest.guidelineResult == 'REAL_TIME_DEGRADATION'">탐침 코드로 실시간성 저하</span>
              <span v-if="dynamicTest.guidelineResult == 'TOOL_ERROR'">도구 오류</span>
              <span v-if="dynamicTest.guidelineResult == 'OTHER_EXCEPTION'">기타 예외 처리</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ dynamicTest.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ dynamicTest.views }}</td>
          </tr>

          <tr v-if="dynamicTestList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="dynamicTestList.first" :last="dynamicTestList.last" :totalPages="dynamicTestList.totalPages"
                  :number="dynamicTestList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/dynamic-test/write'">
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
import HashTags from '@/components/common/HashTags.vue'
import Pagination from '@/components/common/Pagination.vue'

// vue.js
import {onBeforeMount, onBeforeUnmount, ref} from 'vue'
import {useStore} from "vuex";
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {createUri} from "@/utils/uri-util";
import {parseApiErrorMsg} from "@/utils/validation-util";
import {isEmpty} from "@/utils/empty-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    HashTags,
    Pagination
  },
  setup() {
    // vue.js
    const store = useStore();
    // variable
    let dynamicTestList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let searchType2 = ref("TITLE");
    let searchKeyword2 = ref("");
    let pageParam = {"page": 1};
    let access = ref("");
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("dynamic-test");

      // 검색 정보 불러오기
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 가져야 함
      // 리스트만 검색 정보 불러오기 가능
      if (store.state.pageInfo.pageName === "DynamicTestGuidelineList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "DynamicTestGuidelineList") {
        dynamicTestList.value.number = dynamicTestList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/dynamic-tests/list-access-authority",
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
      if (e.target.value === 'GUIDELINE_RESULT') {
        searchKeyword.value = 'COMPLETED';
      } else if (previousSearchType === 'GUIDELINE_RESULT' && e.target.value !== 'GUIDELINE_RESULT') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value,
        "searchType2": searchType2.value,
        "searchKeyword2": searchKeyword2.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/dynamic-tests/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // dynamicTestList.value.content.length = 0;
            dynamicTestList.value = response.data;
            startNumber.value = Math.floor(dynamicTestList.value.number / 10) * 10 + 1;
            endNumber.value = (dynamicTestList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (dynamicTestList.value.totalPages == 0 ? 1 : dynamicTestList.value.totalPages);

            // dayjs
            for (const dynamicTest of dynamicTestList.value.content) {
              dynamicTest.createdDate = dayjs(dynamicTest.createdDate).format("YYYY.MM.DD.");
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
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 저장해야 함
      // 리스트만 검색 정보 저장 가능
      store.commit("pageInfo/setPageInfo",
          {
            searchType: searchType.value,
            searchKeyword: searchKeyword.value,
            previousSearchType: previousSearchType,
            page: dynamicTestList.value.number,
            pageName: 'DynamicTestGuidelineList'
          }
      );
    });

    return {
      // variable
      dynamicTestList, searchType, searchKeyword, searchType2, searchKeyword2, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>
