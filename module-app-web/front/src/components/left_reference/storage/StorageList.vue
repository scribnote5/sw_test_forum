<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="지식 저장소" :paths="['나머지 자료', '지식 저장소 리스트']" title=""/>

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
            <option value="STUDY">스터디</option>
            <option value="FINAL_TEST">실사 후기</option>
            <option value="EDUCATION">교육 자료</option>
            <option value="CHECK_LIST">체크 리스트</option>
            <option value="REFERENCE">참고 자료(가이드라인)</option>
            <option value="ETC">기타 자료</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
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
          <!-- storageListByPriority -->
          <tr v-for="(storage, i) in storageListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <router-link :to="'/storage/read/' + storage.idx"> {{ storage.title }}</router-link>
              <span class="comment-count">{{ storage.commentDtoCount }}</span>
              <img v-if="storage.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ storage.createdByUser.department }} {{ storage.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ storage.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ storage.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="storage.category == 'STUDY'">스터디</span>
              <span v-if="storage.category == 'FINAL_TEST'">실사 후기</span>
              <span v-if="storage.category == 'EDUCATION'">교육 자료</span>
              <span v-if="storage.category == 'CHECK_LIST'">체크 리스트</span>
              <span v-if="storage.category == 'REFERENCE'">참고 자료(가이드라인)</span>
              <span v-if="storage.category == 'ETC'">기타 자료</span>
            </td>
            <td class="d-none d-lg-table-cell">{{ storage.createdByUser.department }} {{ storage.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ storage.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ storage.views }}</td>
          </tr>

          <!-- storageList -->
          <tr v-for="(storage, i) in storageList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ storageList.totalElements - storageList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ storageList.totalElements - storageList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/storage/read/' + storage.idx"> {{ storage.title }}</router-link>
              <span class="comment-count">{{ storage.commentDtoCount }}</span>
              <img v-if="storage.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ storage.createdByUser.department }} {{ storage.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ storage.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ storage.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="storage.category == 'STUDY'">스터디</span>
              <span v-if="storage.category == 'FINAL_TEST'">실사 후기</span>
              <span v-if="storage.category == 'EDUCATION'">교육 자료</span>
              <span v-if="storage.category == 'CHECK_LIST'">체크 리스트</span>
              <span v-if="storage.category == 'REFERENCE'">참고 자료(가이드라인)</span>
              <span v-if="storage.category == 'ETC'">기타 자료</span>
            </td>
            <td class="d-none d-lg-table-cell">{{ storage.createdByUser.department }} {{ storage.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ storage.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ storage.views }}</td>
          </tr>

          <tr v-if="storageList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="storageList.first" :last="storageList.last" :totalPages="storageList.totalPages"
                  :number="storageList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/storage/write'">
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
    let storageListByPriority = ref([]);
    let storageList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let access = ref("");
    let pageParam = {"page": 1};
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("storage");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "StorageList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "StorageList") {
        storageList.value.number = storageList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/list-access-authority",
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
        searchKeyword.value = 'STUDY';
      } else if (previousSearchType === 'CATEGORY' && e.target.value !== 'CATEGORY') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 있는 list
      if (pageParam.page === 1 && isEmpty(searchKeyword.value)) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/high-priority-list",
            {},
        )
            .then((response) => {
              storageListByPriority.value = response.data;
              // dayjs
              for (const storage of storageListByPriority.value) {
                storage.createdDate = dayjs(storage.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        storageListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/storages/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // storageList.value.content.length = 0;
            storageList.value = response.data;
            startNumber.value = Math.floor(storageList.value.number / 10) * 10 + 1;
            endNumber.value = (storageList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (storageList.value.totalPages == 0 ? 1 : storageList.value.totalPages);

            // dayjs
            for (const storage of storageList.value.content) {
              storage.createdDate = dayjs(storage.createdDate).format("YYYY.MM.DD.");
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
            page: storageList.value.number,
            pageName: 'StorageList'
          }
      );
    });

    return {
      // variable
      storageListByPriority, storageList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>