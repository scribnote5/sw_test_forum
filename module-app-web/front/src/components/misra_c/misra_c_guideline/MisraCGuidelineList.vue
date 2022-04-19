<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C 가이드라인" :subPage="misraCRule" :paths="['MISRA C', 'MISRA C 가이드라인 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="HASH_TAGS">해시태그</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-model="searchKeyword" v-on:keyup.enter="searchList" type="search" class="custom-search-input ms-2" placeholder="검색어">
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>
      </div>

      <div class="page-content">
        <table class="table table-hover mobile-table-list mt-4">
          <thead>
          <tr class="d-none d-lg-table-row">
            <th style="width: 5%" class="text-center">번호</th>
            <th style="width: 45%" class="text-center">
              <span v-if="misraCIdx === 0">규칙 및 제목</span>
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
          <!-- misraCGuidelineList -->
          <tr v-for="(styleCopGuideline, i) in misraCGuidelineList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ misraCGuidelineList.totalElements - misraCGuidelineList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ misraCGuidelineList.totalElements - misraCGuidelineList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to= "misraCIdx === 0 ? ('/misra-c-guideline/read/' + styleCopGuideline.idx) : ('/misra-c-guideline/read/from-rule-list/' + styleCopGuideline.idx)">
                <span v-if="misraCIdx === 0">{{ styleCopGuideline.misraCRule }} - </span>{{ styleCopGuideline.title }}
              </router-link>
              <span class="comment-count">{{ styleCopGuideline.commentDtoCount }}</span>
              <span v-if="styleCopGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ styleCopGuideline.likeCountInList }}</span></span>
              <img v-if="styleCopGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ styleCopGuideline.createdByUser.department }} {{ styleCopGuideline.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ styleCopGuideline.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ styleCopGuideline.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="styleCopGuideline.hashTags"></HashTags></span> &nbsp;
                  <span class="mobile-content" v-if="styleCopGuideline.guidelineResult == 'COMPLETED'">완료</span>
                  <span class="mobile-content" v-if="styleCopGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
                  <span class="mobile-content" v-if="styleCopGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
                  <span class="mobile-content" v-if="styleCopGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
                  <span class="mobile-content" v-if="styleCopGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ styleCopGuideline.createdByUser.department }} {{ styleCopGuideline.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="styleCopGuideline.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="styleCopGuideline.guidelineResult == 'COMPLETED'">완료</span>
              <span v-if="styleCopGuideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span v-if="styleCopGuideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span v-if="styleCopGuideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span v-if="styleCopGuideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ styleCopGuideline.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ styleCopGuideline.views }}</td>
          </tr>

          <tr v-if="misraCGuidelineList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="misraCGuidelineList.first" :last="misraCGuidelineList.last" :totalPages="misraCGuidelineList.totalPages"
                  :number="misraCGuidelineList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div v-if="misraCIdx > 0" class="d-flex justify-content-between mx-4 mb-4">
        <router-link :to="'/misra-c/read/' + misraCIdx">
          <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
        </router-link>

        <router-link :to="'/misra-c-guideline/write/' + misraCIdx" v-if="access">
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
import {onBeforeMount, ref} from 'vue'
import {useRoute} from "vue-router";
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {createUri} from "@/utils/uri-util";
import {parseErrorMsg} from "@/utils/validation-util";
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
    const misraCIdx = isEmpty(route.params.misraCIdx) ? 0 : route.params.misraCIdx;
    // variable
    let misraCGuidelineList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let access = ref("");
    // misra c rule
    let misraCRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("misra-c-guideline");

      // misraCIdx가 유효한 경우(0은 유효하지 않음)
      if (misraCIdx > 0) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c/misra-c-rule/" + misraCIdx,
            {},
        )
            .then((response) => {
              misraCRule.value = response.data;
            })
            .catch((error) => {
              parseErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        misraCRule.value = "MISRA C 규칙 페이지에서만 가이드라인을 등록할 수 있습니다.";
      }

      await searchList({"page": 1});

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c-guidelines/list-access-authority",
          {},
      )
          .then((response) => {
            access.value = response.data;
          })
          .catch((error) => {
            parseErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value,
        // misraCIdx에 유효한 값이 아닌 경우 0을 전달
        "misraCIdx": misraCIdx
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-c-guidelines/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // misraCGuidelineList.value.content.length = 0;
            misraCGuidelineList.value = response.data;
            startNumber.value = Math.floor(misraCGuidelineList.value.number / 10) * 10 + 1;
            endNumber.value = (misraCGuidelineList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (misraCGuidelineList.value.totalPages == 0 ? 1 : misraCGuidelineList.value.totalPages);

            // dayjs
            for (const styleCopGuideline of misraCGuidelineList.value.content) {
              styleCopGuideline.createdDate = dayjs(styleCopGuideline.createdDate).format("YYYY.MM.DD.");
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
      misraCIdx, misraCGuidelineList, searchType, searchKeyword, startNumber, endNumber, access,
      misraCRule,

      // function
      searchList
    }
  }
}
</script>
