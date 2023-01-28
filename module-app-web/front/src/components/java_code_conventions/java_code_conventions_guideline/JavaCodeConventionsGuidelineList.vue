<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Java Code Conventions 가이드라인" :subPage="javaCodeConventionsRule" :paths="['Java Code Conventions', 'Java Code Conventions 가이드라인 리스트']" title=""/>

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
              <span v-if="javaCodeConventionsIdx === 0">규칙 및 제목</span>
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
          <!-- javaCodeConventionsGuidelineList -->
          <tr v-for="(javaCodeConventions, i) in javaCodeConventionsGuidelineList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ javaCodeConventionsGuidelineList.totalElements - javaCodeConventionsGuidelineList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ javaCodeConventionsGuidelineList.totalElements - javaCodeConventionsGuidelineList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="javaCodeConventionsIdx === 0 ? ('/java-code-conventions-guideline/read/' + javaCodeConventions.idx) : ('/java-code-conventions-guideline/read/from-rule-list/' + javaCodeConventions.idx)">
                <span v-if="javaCodeConventionsIdx === 0">{{ javaCodeConventions.javaCodeConventionsRule }} - </span>{{ javaCodeConventions.title }}
              </router-link>
              <span class="comment-count">{{ javaCodeConventions.commentDtoCount }}</span>
              <span v-if="javaCodeConventions.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ javaCodeConventions.likeCountInList }}</span></span>
              <img v-if="javaCodeConventions.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ javaCodeConventions.createdByUser.department }} {{ javaCodeConventions.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ javaCodeConventions.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ javaCodeConventions.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="javaCodeConventions.hashTags"></HashTags></span> &nbsp;
                  <span class="mobile-content" v-if="javaCodeConventions.guidelineResult == 'COMPLETED'">수정 완료</span>
                  <span class="mobile-content" v-if="javaCodeConventions.guidelineResult == 'EXCLUDE'">사전 제외</span>
                  <span class="mobile-content" v-if="javaCodeConventions.guidelineResult == 'EXCEPTION'">예외 처리</span>
                  <span class="mobile-content" v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
                  <span class="mobile-content" v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ javaCodeConventions.createdByUser.department }} {{ javaCodeConventions.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="javaCodeConventions.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="javaCodeConventions.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span v-if="javaCodeConventions.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="javaCodeConventions.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="javaCodeConventions.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ javaCodeConventions.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ javaCodeConventions.views }}</td>
          </tr>

          <tr v-if="javaCodeConventionsGuidelineList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="javaCodeConventionsGuidelineList.first" :last="javaCodeConventionsGuidelineList.last" :totalPages="javaCodeConventionsGuidelineList.totalPages"
                  :number="javaCodeConventionsGuidelineList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div v-if="javaCodeConventionsIdx > 0" class="d-flex justify-content-between mx-4 mb-4">
        <router-link :to="'/java-code-conventions/read/' + javaCodeConventionsIdx">
          <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
        </router-link>

        <router-link :to="'/java-code-conventions-guideline/write/' + javaCodeConventionsIdx" v-if="access">
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
    const javaCodeConventionsIdx = isEmpty(route.params.javaCodeConventionsIdx) ? 0 : route.params.javaCodeConventionsIdx;
    // variable
    let javaCodeConventionsGuidelineList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");
    // misra c rule
    let javaCodeConventionsRule = ref("");
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("java-code-conventions-guideline");

      // javaCodeConventionsIdx가 유효한 경우(0은 유효하지 않음)
      if (javaCodeConventionsIdx > 0) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions/java-code-conventions-rule/" + javaCodeConventionsIdx,
            {},
        )
            .then((response) => {
              javaCodeConventionsRule.value = response.data;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        javaCodeConventionsRule.value = "Java Code Conventions 규칙 페이지에서만 가이드라인을 등록할 수 있습니다.";
      }

      // 검색 정보 불러오기
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 가져야 함
      // 리스트만 검색 정보 불러오기 가능
      if (javaCodeConventionsIdx == 0 && store.state.pageInfo.pageName === "JavaCodeConventionsGuidelineList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (javaCodeConventionsIdx == 0 && store.state.pageInfo.pageName === "JavaCodeConventionsGuidelineList") {
        javaCodeConventionsGuidelineList.value.number = javaCodeConventionsGuidelineList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/list-access-authority",
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
        // javaCodeConventionsIdx에 유효한 값이 아닌 경우 0을 전달
        "javaCodeConventionsIdx": javaCodeConventionsIdx
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/java-code-conventions-guidelines/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // javaCodeConventionsGuidelineList.value.content.length = 0;
            javaCodeConventionsGuidelineList.value = response.data;
            startNumber.value = Math.floor(javaCodeConventionsGuidelineList.value.number / 10) * 10 + 1;
            endNumber.value = (javaCodeConventionsGuidelineList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (javaCodeConventionsGuidelineList.value.totalPages == 0 ? 1 : javaCodeConventionsGuidelineList.value.totalPages);

            // dayjs
            for (const javaCodeConventions of javaCodeConventionsGuidelineList.value.content) {
              javaCodeConventions.createdDate = dayjs(javaCodeConventions.createdDate).format("YYYY.MM.DD.");
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
      if (javaCodeConventionsIdx == 0) {
        // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 저장해야 함
        // 리스트만 검색 정보 저장 가능
        store.commit("pageInfo/setPageInfo",
            {
              searchType: searchType.value,
              searchKeyword: searchKeyword.value,
              previousSearchType: previousSearchType,
              page: javaCodeConventionsGuidelineList.value.number,
              pageName: 'JavaCodeConventionsGuidelineList'
            }
        );
      }
    });

    return {
      // variable
      javaCodeConventionsIdx, javaCodeConventionsGuidelineList, searchType, searchKeyword, startNumber, endNumber, access,
      javaCodeConventionsRule,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>
