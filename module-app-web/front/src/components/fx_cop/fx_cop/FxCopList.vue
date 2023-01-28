<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page=".NET Framework Design Guideline 규칙" :paths="['.NET Framework Design Guideline', '.NET Framework Design Guideline 규칙 리스트']" title=""/>

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
            <th style="width: 50%" class="text-center">규칙명</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- fxCopListByPriority -->
          <tr v-for="(fxCop, i) in fxCopListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <Frequency page-information="list" :frequency="fxCop.frequency"></Frequency>
              <router-link :to="'/fx-cop/read/' + fxCop.idx">{{ fxCop.title }}</router-link>
              <span class="comment-count">{{ fxCop.commentDtoCount }}</span>
              <img v-if="fxCop.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ fxCop.createdByUser.department }} {{ fxCop.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ fxCop.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ fxCop.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="fxCop.hashTags"></HashTags></span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ fxCop.createdByUser.department }} {{ fxCop.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="fxCop.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ fxCop.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ fxCop.views }}</td>
          </tr>

          <!-- fxCopList -->
          <tr v-for="(fxCop, i) in fxCopList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ fxCopList.totalElements - fxCopList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ fxCopList.totalElements - fxCopList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <Frequency page-information="list" :frequency="fxCop.frequency"></Frequency>
              <router-link :to="'/fx-cop/read/' + fxCop.idx"> {{ fxCop.title }}</router-link>
              <span class="comment-count">{{ fxCop.commentDtoCount }}</span>
              <img v-if="fxCop.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ fxCop.createdByUser.department }} {{ fxCop.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ fxCop.createdDate }}</span> &nbsp;
                  <span class="mobile-content">조회수: {{ fxCop.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="fxCop.hashTags"></HashTags></span>
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ fxCop.createdByUser.department }} {{ fxCop.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="fxCop.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ fxCop.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ fxCop.views }}</td>
          </tr>

          <tr v-if="fxCopList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="fxCopList.first" :last="fxCopList.last" :totalPages="fxCopList.totalPages"
                  :number="fxCopList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/fx-cop/write'">
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
    let fxCopListByPriority = ref([]);
    let fxCopList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("fx-cop");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "FxCopList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "FxCopList") {
        fxCopList.value.number = fxCopList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/list-access-authority",
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
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/high-priority-list",
            {},
        )
            .then((response) => {
              fxCopListByPriority.value = response.data;
              // dayjs
              for (const fxCop of fxCopListByPriority.value) {
                fxCop.createdDate = dayjs(fxCop.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        fxCopListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // fxCopList.value.content.length = 0;
            fxCopList.value = response.data;
            startNumber.value = Math.floor(fxCopList.value.number / 10) * 10 + 1;
            endNumber.value = (fxCopList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (fxCopList.value.totalPages == 0 ? 1 : fxCopList.value.totalPages);

            // dayjs
            for (const fxCop of fxCopList.value.content) {
              fxCop.createdDate = dayjs(fxCop.createdDate).format("YYYY.MM.DD.");
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
            page: fxCopList.value.number,
            pageName: 'FxCopList'
          }
      );
    });

    return {
      // variable
      fxCopListByPriority, fxCopList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList
    }
  }
}
</script>