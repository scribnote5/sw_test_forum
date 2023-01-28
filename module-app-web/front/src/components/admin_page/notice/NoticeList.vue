<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="공지사항" :paths="['관리자 페이지', '공지사항 리스트']" title=""/>

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
            <th style="width: 65%" class="text-center">제목</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- noticeListByPriority -->
          <tr v-for="(notice, i) in noticeListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/> </span>
              <!-- 공통 -->
              <router-link :to="'/notice/read/' + notice.idx"> {{ notice.title }}</router-link>
              <span class="comment-count">{{ notice.commentDtoCount }}</span>
              <img v-if="notice.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ notice.createdByUser.department }} {{ notice.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ notice.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ notice.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ notice.createdByUser.department }} {{ notice.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ notice.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ notice.views }}</td>
          </tr>

          <!-- noticeList -->
          <tr v-for="(notice, i) in noticeList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ noticeList.totalElements - noticeList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ noticeList.totalElements - noticeList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/notice/read/' + notice.idx"> {{ notice.title }}</router-link>
              <span class="comment-count">{{ notice.commentDtoCount }}</span>
              <img v-if="notice.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ notice.createdByUser.department }} {{ notice.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ notice.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ notice.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ notice.createdByUser.department }} {{ notice.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ notice.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ notice.views }}</td>
          </tr>

          <tr v-if="noticeList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="noticeList.first" :last="noticeList.last" :totalPages="noticeList.totalPages"
                  :number="noticeList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/notice/write'">
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
import {onBeforeMount,onBeforeUnmount, ref} from 'vue'
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
    let noticeListByPriority = ref([]);
    let noticeList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("notice");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "NoticeList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "NoticeList") {
        noticeList.value.number = noticeList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/notices/list-access-authority",
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
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/notices/high-priority-list",
            {},
        )
            .then((response) => {
              noticeListByPriority.value = response.data;
              // dayjs
              for (const notice of noticeListByPriority.value) {
                notice.createdDate = dayjs(notice.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        noticeListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/notices/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // noticeList.value.content.length = 0;
            noticeList.value = response.data;
            startNumber.value = Math.floor(noticeList.value.number / 10) * 10 + 1;
            endNumber.value = (noticeList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (noticeList.value.totalPages == 0 ? 1 : noticeList.value.totalPages);

            // dayjs
            for (const notice of noticeList.value.content) {
              notice.createdDate = dayjs(notice.createdDate).format("YYYY.MM.DD.");
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
            page: noticeList.value.number,
            pageName: 'NoticeList'
          }
      );
    });

    return {
      // variable
      noticeListByPriority, noticeList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList
    }
  }
}
</script>