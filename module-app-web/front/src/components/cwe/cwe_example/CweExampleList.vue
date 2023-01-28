<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="CWE C/C++ 예제 코드" :subPage="cweRule" :paths="['CWE C/C++', 'CWE C/C++ 예제 코드 리스트']" title=""/>

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
              <span v-if="cweIdx === 0">규칙 및 제목</span>
              <span v-else>규칙</span>
            </th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>

          <!-- cweExampleList -->
          <tr v-for="(cweExample, i) in cweExampleList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">
              <img v-if="cweExample.priority <= 3" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
              <span v-else>{{ cweExampleList.totalElements - cweExampleList.pageable.offset - i }}</span>
            </td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">
                <img v-if="cweExample.priority <= 3" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                <span v-else>{{ cweExampleList.totalElements - cweExampleList.pageable.offset - i }}. </span>
              </span>
              <!-- 공통 -->
              <router-link :to="cweIdx === 0 ? ('/cwe-example/read/' + cweExample.idx) : ('/cwe-example/read/from-rule-list/' + cweExample.idx)">
                <span v-if="cweIdx === 0">{{ cweExample.cweRule }} - </span>{{ cweExample.title }}
              </router-link>
              <span class="comment-count">{{ cweExample.commentDtoCount }}</span>
              <img v-if="cweExample.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ cweExample.createdByUser.department }} {{ cweExample.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ cweExample.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ cweExample.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ cweExample.createdByUser.department }} {{ cweExample.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ cweExample.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ cweExample.views }}</td>
          </tr>

          <tr v-if="cweExampleList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="cweExampleList.first" :last="cweExampleList.last" :totalPages="cweExampleList.totalPages"
                  :number="cweExampleList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div v-if="cweIdx > 0" class="d-flex justify-content-between mx-4 mb-4">
        <router-link :to="'/cwe/read/' + cweIdx">
          <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
        </router-link>

        <router-link :to="'/cwe-example/write/' + cweIdx" v-if="access">
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
    const store = useStore();
    const cweIdx = isEmpty(route.params.cweIdx) ? 0 : route.params.cweIdx;
    // variable
    let cweExampleListByPriority = ref([]);
    let cweExampleList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");
    // cwe rule
    let cweRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("cwe-example");

      // cweIdx가 유효한 경우(0은 유효하지 않음)
      if (cweIdx > 0) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe/cwe-rule/" + cweIdx,
            {},
        )
            .then((response) => {
              cweRule.value = response.data;
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        cweRule.value = "CWE C/C++ 규칙 페이지에서만 예제 코드를 등록할 수 있습니다.";
      }

      // 검색 정보 불러오기
      // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 가져야 함
      // 리스트만 검색 정보 불러오기 가능
      if (cweIdx == 0 && store.state.pageInfo.pageName === "CweExampleList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (cweIdx == 0 && store.state.pageInfo.pageName === "CweExampleList") {
        cweExampleList.value.number = cweExampleList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-examples/list-access-authority",
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
        // cweIdx에 유효한 값이 아닌 경우 0을 전달
        "cweIdx": cweIdx
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-examples/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // cweExampleList.value.content.length = 0;
            cweExampleList.value = response.data;
            startNumber.value = Math.floor(cweExampleList.value.number / 10) * 10 + 1;
            endNumber.value = (cweExampleList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (cweExampleList.value.totalPages == 0 ? 1 : cweExampleList.value.totalPages);

            // dayjs
            for (const cweExample of cweExampleList.value.content) {
              cweExample.createdDate = dayjs(cweExample.createdDate).format("YYYY.MM.DD.");
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
      if (cweIdx == 0) {
        // 리스트와 규칙 idx로 조회하는 리스트는 서로 다른 검색 정보를 저장해야 함
        // 리스트만 검색 정보 저장 가능
        store.commit("pageInfo/setPageInfo",
            {
              searchType: searchType.value,
              searchKeyword: searchKeyword.value,
              page: cweExampleList.value.number,
              pageName: 'CweExampleList'
            }
        );
      }
    });

    return {
      // variable
      cweIdx, cweExampleListByPriority, cweExampleList, searchType, searchKeyword, startNumber, endNumber, access,
      cweRule,

      // function
      searchList
    }
  }
}
</script>
