<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Toolchain 트러블슈팅" :paths="['Toolchain', 'Toolchain 트러블슈팅 리스트']" title=""/>

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
            <th style="width: 45%" class="text-center">제목</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 10%" class="text-center">유형</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- toolchainListByPriority -->
          <tr v-for="(toolchain, i) in toolchainListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <router-link :to="'/toolchain/read/' + toolchain.idx">{{ toolchain.title }}</router-link>
              <span class="comment-count">{{ toolchain.commentDtoCount }}</span>
              <img v-if="toolchain.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ toolchain.createdByUser.department }} {{ toolchain.createdByUser.name }} </span> <br>
                  <span class="mobile-content">
                    <span v-if="toolchain.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
                    <span v-if="toolchain.toolCategory == 'FUNCTION'">기능</span>
                    <span v-if="toolchain.toolCategory == 'INFORMATION'">정보</span>
                    <span v-if="toolchain.toolCategory == 'ETC'">기타</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ toolchain.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ toolchain.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="toolchain.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ toolchain.createdByUser.department }} {{ toolchain.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="toolchain.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="toolchain.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="toolchain.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="toolchain.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="toolchain.toolCategory == 'ETC'">기타</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ toolchain.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ toolchain.views }}</td>
          </tr>

          <!-- toolchainList -->
          <tr v-for="(toolchain, i) in toolchainList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ toolchainList.totalElements - toolchainList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ toolchainList.totalElements - toolchainList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/toolchain/read/' + toolchain.idx"> {{ toolchain.title }}</router-link>
              <span class="comment-count">{{ toolchain.commentDtoCount }}</span>
              <img v-if="toolchain.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ toolchain.createdByUser.department }} {{ toolchain.createdByUser.name }} </span> <br>
                  <span class="mobile-content">
                    <span v-if="toolchain.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
                    <span v-if="toolchain.toolCategory == 'FUNCTION'">기능</span>
                    <span v-if="toolchain.toolCategory == 'INFORMATION'">정보</span>
                    <span v-if="toolchain.toolCategory == 'ETC'">기타</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ toolchain.createdDate }}</span> &nbsp;
                  <span class="mobile-content">조회수: {{ toolchain.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="toolchain.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ toolchain.createdByUser.department }} {{ toolchain.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="toolchain.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="toolchain.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="toolchain.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="toolchain.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="toolchain.toolCategory == 'ETC'">기타</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ toolchain.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ toolchain.views }}</td>
          </tr>

          <tr v-if="toolchainList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="toolchainList.first" :last="toolchainList.last" :totalPages="toolchainList.totalPages"
                  :number="toolchainList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/toolchain/write'" v-if="access">
          <button class="btn btn-main-blue d-flex align-items-center">작성<img :src="require(`@/assets/images/write-white.svg`)" class="ms-2"></button>
        </router-link>
      </div>
    </div>
  </section>
</template>

<style lang="scss">
@import '~@/assets/css/ckeditor.css';
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
    const store = useStore();
    // variable
    let toolchainListByPriority = ref([]);
    let toolchainList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let pageParam = {"page": 1};
    let access = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("toolchain");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "ToolchainList") {
        searchType.value = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "ToolchainList") {
        toolchainList.value.number = toolchainList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/list-access-authority",
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
      //우선순위가 있는 list
      if (pageParam.page === 1 && isEmpty(searchKeyword.value)) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/high-priority-list",
            {},
        )
            .then((response) => {
              toolchainListByPriority.value = response.data;
              // dayjs
              for (const toolchain of toolchainListByPriority.value) {
                toolchain.createdDate = dayjs(toolchain.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        toolchainListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/toolchains/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // toolchainList.value.content.length = 0;
            toolchainList.value = response.data;
            startNumber.value = Math.floor(toolchainList.value.number / 10) * 10 + 1;
            endNumber.value = (toolchainList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (toolchainList.value.totalPages == 0 ? 1 : toolchainList.value.totalPages);

            // dayjs
            for (const toolchain of toolchainList.value.content) {
              toolchain.createdDate = dayjs(toolchain.createdDate).format("YYYY.MM.DD.");
            }
          })
          .catch((error) => {
            parseErrorMsg(error.response);
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
            page: toolchainList.value.number,
            pageName: 'ToolchainList'
          }
      );
    });

    return {
      // variable
      toolchainListByPriority, toolchainList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList
    }
  }
}
</script>
