<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="C# Coding Convention 예제 코드" :subPage="styleCopRule" :paths="['C# Coding Convention', 'C# Coding Convention 예제 코드 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
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
            <th style="width: 65%" class="text-center">
              <span v-if="styleCopIdx === 0">규칙 및 제목</span>
              <span v-else>규칙</span>
            </th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>

          <!-- styleCopExampleList -->
          <tr v-for="(styleCopExample, i) in styleCopExampleList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">
              <img v-if="styleCopExample.priority <= 3" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
              <span v-else>{{ styleCopExampleList.totalElements - styleCopExampleList.pageable.offset - i }}</span></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">
                <img v-if="styleCopExample.priority <= 3" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                <span v-else>{{ styleCopExampleList.totalElements - styleCopExampleList.pageable.offset - i }}. </span>
              </span>
              <!-- 공통 -->
              <router-link :to="styleCopIdx === 0 ? ('/style-cop-example/read/' + styleCopExample.idx) : ('/style-cop-example/read/from-rule-list/' + styleCopExample.idx)">
                <span v-if="styleCopIdx === 0">{{ styleCopExample.styleCopRule }} - </span>{{ styleCopExample.title }}
              </router-link>
              <span class="comment-count">{{ styleCopExample.commentDtoCount }}</span>
              <img v-if="styleCopExample.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ styleCopExample.createdByUser.department }} {{ styleCopExample.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ styleCopExample.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ styleCopExample.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ styleCopExample.createdByUser.department }} {{ styleCopExample.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ styleCopExample.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ styleCopExample.views }}</td>
          </tr>

          <tr v-if="styleCopExampleList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="styleCopExampleList.first" :last="styleCopExampleList.last" :totalPages="styleCopExampleList.totalPages"
                  :number="styleCopExampleList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div v-if="styleCopIdx > 0" class="d-flex justify-content-between mx-4 mb-4">
        <router-link :to="'/style-cop/read/' + styleCopIdx">
          <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
        </router-link>

        <router-link :to="'/style-cop-example/write/' + styleCopIdx" v-if="access">
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
    Pagination
  },
  setup() {
    // vue.js
    const route = useRoute();
    const styleCopIdx = isEmpty(route.params.styleCopIdx) ? 0 : route.params.styleCopIdx;
    const store = useStore();
    // variable
    let styleCopExampleListByPriority = ref([]);
    let styleCopExampleList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");
    // styleCop rule
    let styleCopRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("style-cop-example");

      // styleCopIdx가 유효한 경우(0은 유효하지 않음)
      if (styleCopIdx > 0) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop/style-cop-rule/" + styleCopIdx,
            {},
        )
            .then((response) => {
              styleCopRule.value = response.data;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        styleCopRule.value = "C# Coding Convention 규칙 페이지에서만 예제 코드를 등록할 수 있습니다.";
      }

      // 검색 정보 불러오기
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 가져야 함
      // 리스트만 검색 정보 불러오기 가능
      if (styleCopIdx == 0 && store.state.pageInfo.pageName === "StyleCopExampleList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (styleCopIdx == 0 && store.state.pageInfo.pageName === "StyleCopExampleList") {
        styleCopExampleList.value.number = styleCopExampleList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop-examples/list-access-authority",
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
        "searchKeyword": searchKeyword.value,
        // styleCopIdx에 유효한 값이 아닌 경우 0을 전달
        "styleCopIdx": styleCopIdx
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/style-cop-examples/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // styleCopExampleList.value.content.length = 0;
            styleCopExampleList.value = response.data;
            startNumber.value = Math.floor(styleCopExampleList.value.number / 10) * 10 + 1;
            endNumber.value = (styleCopExampleList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (styleCopExampleList.value.totalPages == 0 ? 1 : styleCopExampleList.value.totalPages);

            // dayjs
            for (const styleCopExample of styleCopExampleList.value.content) {
              styleCopExample.createdDate = dayjs(styleCopExample.createdDate).format("YYYY.MM.DD.");
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
      if (styleCopIdx == 0) {
        // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 저장해야 함
        // 리스트만 검색 정보 저장 가능
        store.commit("pageInfo/setPageInfo",
            {
              searchType: searchType.value,
              searchKeyword: searchKeyword.value,
              page: styleCopExampleList.value.number,
              pageName: 'StyleCopExampleList'
            }
        );
      }
    });

    return {
      // variable
      styleCopIdx, styleCopExampleListByPriority, styleCopExampleList, searchType, searchKeyword, startNumber, endNumber, access,
      styleCopRule,

      // function
      searchList
    }
  }
}
</script>
