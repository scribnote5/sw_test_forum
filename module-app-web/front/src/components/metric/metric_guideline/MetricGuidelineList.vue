<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="메트릭 가이드라인" :subPage="metricRule" :paths="['메트릭', '메트릭 가이드라인 리스트']" title=""/>

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
              <span v-if="metricIdx === 0">규칙 및 제목</span>
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
          <!-- metricGuidelineList -->
          <tr v-for="(metricGuideline, i) in metricGuidelineList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ metricGuidelineList.totalElements - metricGuidelineList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ metricGuidelineList.totalElements - metricGuidelineList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="metricIdx === 0 ? ('/metric-guideline/read/' + metricGuideline.idx) : ('/metric-guideline/read/from-rule-list/' + metricGuideline.idx)">
                <span v-if="metricIdx === 0">{{ metricGuideline.metricRule }} - </span>{{ metricGuideline.title }}
              </router-link>
              <span class="comment-count">{{ metricGuideline.commentDtoCount }}</span>
              <span v-if="metricGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ metricGuideline.likeCountInList }}</span></span>
              <img v-if="metricGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ metricGuideline.createdByUser.department }} {{ metricGuideline.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ metricGuideline.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ metricGuideline.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="metricGuideline.hashTags"></HashTags></span> &nbsp;
                  <span class="mobile-content" v-if="metricGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
                  <span class="mobile-content" v-if="metricGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
                  <span class="mobile-content" v-if="metricGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
                  <span class="mobile-content" v-if="metricGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
                  <span class="mobile-content" v-if="metricGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ metricGuideline.createdByUser.department }} {{ metricGuideline.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="metricGuideline.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="metricGuideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="metricGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="metricGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="metricGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="metricGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ metricGuideline.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ metricGuideline.views }}</td>
          </tr>

          <tr v-if="metricGuidelineList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="metricGuidelineList.first" :last="metricGuidelineList.last" :totalPages="metricGuidelineList.totalPages"
                  :number="metricGuidelineList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div v-if="metricIdx > 0" class="d-flex justify-content-between mx-4 mb-4">
        <router-link :to="'/metric/read/' + metricIdx">
          <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
        </router-link>

        <router-link :to="'/metric-guideline/write/' + metricIdx" v-if="access">
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
    const metricIdx = isEmpty(route.params.metricIdx) ? 0 : route.params.metricIdx;
    // variable
    let metricGuidelineList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");
    // metric rule
    let metricRule = ref("");
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("metric-guideline");

      // metricIdx가 유효한 경우(0은 유효하지 않음)
      if (metricIdx > 0) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/metric-rule/" + metricIdx,
            {},
        )
            .then((response) => {
              metricRule.value = response.data;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        metricRule.value = "메트릭 규칙 페이지에서만 가이드라인을 등록할 수 있습니다.";
      }

      // 검색 정보 불러오기
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 가져야 함
      // 리스트만 검색 정보 불러오기 가능
      if (metricIdx == 0 && store.state.pageInfo.pageName === "MetricGuidelineList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (metricIdx == 0 && store.state.pageInfo.pageName === "MetricGuidelineList") {
        metricGuidelineList.value.number = metricGuidelineList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric-guidelines/list-access-authority",
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
        // metricIdx에 유효한 값이 아닌 경우 0을 전달
        "metricIdx": metricIdx
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric-guidelines/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // metricGuidelineList.value.content.length = 0;
            metricGuidelineList.value = response.data;
            startNumber.value = Math.floor(metricGuidelineList.value.number / 10) * 10 + 1;
            endNumber.value = (metricGuidelineList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (metricGuidelineList.value.totalPages == 0 ? 1 : metricGuidelineList.value.totalPages);

            // dayjs
            for (const metricGuideline of metricGuidelineList.value.content) {
              metricGuideline.createdDate = dayjs(metricGuideline.createdDate).format("YYYY.MM.DD.");
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
      if (metricIdx == 0) {
        // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 저장해야 함
        // 리스트만 검색 정보 저장 가능
        store.commit("pageInfo/setPageInfo",
            {
              searchType: searchType.value,
              searchKeyword: searchKeyword.value,
              previousSearchType: previousSearchType,
              page: metricGuidelineList.value.number,
              pageName: 'MetricGuidelineList'
            }
        );
      }
    });


    return {
      // variable
      metricIdx, metricGuidelineList, searchType, searchKeyword, startNumber, endNumber, access,
      metricRule,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>
