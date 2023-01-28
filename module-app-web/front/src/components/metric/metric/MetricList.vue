<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="메트릭 규칙" :paths="['메트릭', '메트릭 규칙 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">규칙명</option>
            <option value="HASH_TAGS">해시태그</option>
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
            <th style="width: 55%" class="text-center">규칙명</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- metricListByPriority -->
          <tr v-for="(metric, i) in metricListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <Frequency page-information="list" :frequency="metric.frequency"></Frequency>
              <router-link :to="'/metric/read/' + metric.idx">{{ metric.title }}</router-link>
              <span class="comment-count">{{ metric.commentDtoCount }}</span>
              <img v-if="metric.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ metric.createdByUser.department }} {{ metric.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ metric.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ metric.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="metric.hashTags"></HashTags></span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ metric.createdByUser.department }} {{ metric.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="metric.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ metric.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ metric.views }}</td>
          </tr>

          <!-- metricList -->
          <tr v-for="(metric, i) in metricList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ metricList.totalElements - metricList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ metricList.totalElements - metricList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <Frequency page-information="list" :frequency="metric.frequency"></Frequency>
              <router-link :to="'/metric/read/' + metric.idx"> {{ metric.title }}</router-link>
              <span class="comment-count">{{ metric.commentDtoCount }}</span>
              <img v-if="metric.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ metric.createdByUser.department }} {{ metric.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ metric.createdDate }}</span> &nbsp;
                  <span class="mobile-content">조회수: {{ metric.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="metric.hashTags"></HashTags></span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ metric.createdByUser.department }} {{ metric.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="metric.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ metric.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ metric.views }}</td>
          </tr>

          <tr v-if="metricList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="metricList.first" :last="metricList.last" :totalPages="metricList.totalPages"
                  :number="metricList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/metric/write'">
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
import Frequency from '@/components/common/Frequency.vue'
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
    Frequency,
    HashTags,
    Pagination
  },
  setup() {
    // vue.js
    const store = useStore();
    // variable
    let metricListByPriority = ref([]);
    let metricList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("metric");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "MetricList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "MetricList") {
        metricList.value.number = metricList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/list-access-authority",
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
      //우선순위가 있는 list
      if (pageParam.page === 1 && isEmpty(searchKeyword.value)) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/high-priority-list",
            {},
        )
            .then((response) => {
              metricListByPriority.value = response.data;
              // dayjs
              for (const metric of metricListByPriority.value) {
                metric.createdDate = dayjs(metric.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        metricListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // metricList.value.content.length = 0;
            metricList.value = response.data;
            startNumber.value = Math.floor(metricList.value.number / 10) * 10 + 1;
            endNumber.value = (metricList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (metricList.value.totalPages == 0 ? 1 : metricList.value.totalPages);

            // dayjs
            for (const metric of metricList.value.content) {
              metric.createdDate = dayjs(metric.createdDate).format("YYYY.MM.DD.");
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
            page: metricList.value.number,
            pageName: 'MetricList'
          }
      );
    });

    return {
      // variable
      metricListByPriority, metricList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList
    }
  }
}
</script>