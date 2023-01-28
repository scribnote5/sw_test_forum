<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C++ 가이드라인" :subPage="misraCppRule" :paths="['MISRA C++', 'MISRA C++ 가이드라인 리스트']" title=""/>

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
            <option value="EXCLUDE">사전 제외</option>
            <option value="EXCEPTION">예외 처리</option>
            <option value="FALSE_ALARM">도구 오탐</option>
            <option value="FALSE_ALARM_PATCHED">도구 오탐 패치 완료</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>
      </div>

      <div class="page-content">
        <table class="table table-hover mobile-table-list mt-4">
          <thead>
          <tr class="d-none d-lg-table-row">
            <th style="width: 5%" class="text-center">번호</th>
            <th style="width: 45%" class="text-center">
              <span v-if="misraCppIdx === 0">규칙 및 제목</span>
              <span v-else>규칙</span>
            </th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 10%" class="text-center">가이드라인 결과</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- misraCppGuidelineList -->
          <tr v-for="(misraCppGuideline, i) in misraCppGuidelineList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ misraCppGuidelineList.totalElements - misraCppGuidelineList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ misraCppGuidelineList.totalElements - misraCppGuidelineList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="misraCppIdx === 0 ? ('/misra-cpp-guideline/read/' + misraCppGuideline.idx) : ('/misra-cpp-guideline/read/from-rule-list/' + misraCppGuideline.idx)">
                <span v-if="misraCppIdx === 0">{{ misraCppGuideline.misraCppRule }} - </span>{{ misraCppGuideline.title }}
              </router-link>
              <span class="comment-count">{{ misraCppGuideline.commentDtoCount }}</span>
              <span v-if="misraCppGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ misraCppGuideline.likeCountInList }}</span></span>
              <img v-if="misraCppGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ misraCppGuideline.createdByUser.department }} {{ misraCppGuideline.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ misraCppGuideline.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ misraCppGuideline.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="misraCppGuideline.hashTags"></HashTags></span> &nbsp;
                  <span class="mobile-content" v-if="misraCppGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
                  <span class="mobile-content" v-if="misraCppGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
                  <span class="mobile-content" v-if="misraCppGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
                  <span class="mobile-content" v-if="misraCppGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
                  <span class="mobile-content" v-if="misraCppGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ misraCppGuideline.createdByUser.department }} {{ misraCppGuideline.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="misraCppGuideline.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="misraCppGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="misraCppGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="misraCppGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="misraCppGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="misraCppGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ misraCppGuideline.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ misraCppGuideline.views }}</td>
          </tr>

          <tr v-if="misraCppGuidelineList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="misraCppGuidelineList.first" :last="misraCppGuidelineList.last" :totalPages="misraCppGuidelineList.totalPages"
                  :number="misraCppGuidelineList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div v-if="misraCppIdx > 0" class="d-flex justify-content-between mx-4 mb-4">
        <router-link :to="'/misra-cpp/read/' + misraCppIdx">
          <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
        </router-link>

        <router-link :to="'/misra-cpp-guideline/write/' + misraCppIdx" v-if="access">
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
import {useRoute} from "vue-router";
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
    const route = useRoute();
    const store = useStore();
    const misraCppIdx = isEmpty(route.params.misraCppIdx) ? 0 : route.params.misraCppIdx;
    // variable
    let misraCppGuidelineList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let access = ref("");
    let pageParam = {"page": 1};
    // misra cpp rule
    let misraCppRule = ref("");
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("misra-cpp-guideline");

      // misraCppIdx가 유효한 경우(0은 유효하지 않음)
      if (misraCppIdx > 0) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp/misra-cpp-rule/" + misraCppIdx,
            {},
        )
            .then((response) => {
              misraCppRule.value = response.data;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        misraCppRule.value = "MISRA C++ 규칙 페이지에서만 가이드라인을 등록할 수 있습니다.";
      }

      // 검색 정보 불러오기
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 가져야 함
      // 리스트만 검색 정보 불러오기 가능
      if (misraCppIdx == 0 && store.state.pageInfo.pageName === "MisraCppGuidelineList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (misraCppIdx == 0 && store.state.pageInfo.pageName === "MisraCppGuidelineList") {
        misraCppGuidelineList.value.number = misraCppGuidelineList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/list-access-authority",
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
        // misraCppIdx에 유효한 값이 아닌 경우 0을 전달
        "misraCppIdx": misraCppIdx
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-guidelines/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // misraCppGuidelineList.value.content.length = 0;
            misraCppGuidelineList.value = response.data;
            startNumber.value = Math.floor(misraCppGuidelineList.value.number / 10) * 10 + 1;
            endNumber.value = (misraCppGuidelineList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (misraCppGuidelineList.value.totalPages == 0 ? 1 : misraCppGuidelineList.value.totalPages);

            // dayjs
            for (const misraCppGuideline of misraCppGuidelineList.value.content) {
              misraCppGuideline.createdDate = dayjs(misraCppGuideline.createdDate).format("YYYY.MM.DD.");
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
      if (misraCppIdx == 0) {
        // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 저장해야 함
        // 리스트만 검색 정보 저장 가능
        store.commit("pageInfo/setPageInfo",
            {
              searchType: searchType.value,
              searchKeyword: searchKeyword.value,
              previousSearchType: previousSearchType,
              page: misraCppGuidelineList.value.number,
              pageName: 'MisraCppGuidelineList'
            }
        );
      }
    });

    return {
      // variable
      misraCppIdx, misraCppGuidelineList, searchType, searchKeyword, startNumber, endNumber, access,
      misraCppRule,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>
