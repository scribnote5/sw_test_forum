<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="환경 설정" :paths="['도구', '환경 설정 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="HASH_TAGS">해시태그</option>
            <option value="TARGET_TOOL_NAME">대상 도구</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-if="searchType !== 'TARGET_TOOL_NAME'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <select v-if="searchType === 'TARGET_TOOL_NAME'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="STATIC">STATIC</option>
            <option value="COVER">COVER</option>
            <option value="CONTROLLER_TESTER">Controller Tester</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>

        <div v-if="searchType === 'TARGET_TOOL_NAME'" class="d-flex justify-content-end mt-2">
          <select v-model="searchType2" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="HASH_TAGS">해시태그</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-on:keyup.enter="searchList" v-model="searchKeyword2" name="inputSearchKeyword2" type="search" class="custom-search-input mx-2" placeholder="검색어">
        </div>
      </div>

      <div class="page-content">
        <table class="table table-hover mobile-table-list mt-4">
          <thead>
          <tr class="d-none d-lg-table-row">
            <th style="width: 5%" class="text-center">번호</th>
            <th style="width: 55%" class="text-center">제목</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- toolConfigurationListByPriority -->
          <tr v-for="(toolConfiguration, i) in toolConfigurationListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <router-link :to="'/tool-configuration/read/' + toolConfiguration.idx">
                <span v-if="toolConfiguration.targetToolName == 'STATIC'" class="question-answer-test-type">STATIC</span>
                <span v-if="toolConfiguration.targetToolName == 'COVER'" class="question-answer-tool-type">COVER</span>
                <span v-if="toolConfiguration.targetToolName == 'CONTROLLER_TESTER'" class="question-answer-etc-type">Controller Tester</span>
                {{ toolConfiguration.title }}
              </router-link>
              <span class="comment-count">{{ toolConfiguration.commentDtoCount }}</span>
              <img v-if="toolConfiguration.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ toolConfiguration.createdByUser.department }} {{ toolConfiguration.createdByUser.name }}</span> <br>
                  <span class="mobile-content">{{ toolConfiguration.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ toolConfiguration.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="toolConfiguration.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ toolConfiguration.createdByUser.department }} {{ toolConfiguration.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="toolConfiguration.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ toolConfiguration.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ toolConfiguration.views }}</td>
          </tr>

          <!-- toolConfigurationList -->
          <tr v-for="(toolConfiguration, i) in toolConfigurationList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ toolConfigurationList.totalElements - toolConfigurationList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ toolConfigurationList.totalElements - toolConfigurationList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/tool-configuration/read/' + toolConfiguration.idx">
                <span v-if="toolConfiguration.targetToolName == 'STATIC'" class="question-answer-test-type">STATIC</span>
                <span v-if="toolConfiguration.targetToolName == 'COVER'" class="question-answer-tool-type">COVER</span>
                <span v-if="toolConfiguration.targetToolName == 'CONTROLLER_TESTER'" class="question-answer-etc-type">Controller Tester</span>
                {{ toolConfiguration.title }}
              </router-link>
              <span class="comment-count">{{ toolConfiguration.commentDtoCount }}</span>
              <img v-if="toolConfiguration.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ toolConfiguration.createdByUser.department }} {{ toolConfiguration.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ toolConfiguration.createdDate }}</span> &nbsp;
                  <span class="mobile-content">조회수: {{ toolConfiguration.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="toolConfiguration.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ toolConfiguration.createdByUser.department }} {{ toolConfiguration.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="toolConfiguration.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ toolConfiguration.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ toolConfiguration.views }}</td>
          </tr>

          <tr v-if="toolConfigurationList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="toolConfigurationList.first" :last="toolConfigurationList.last" :totalPages="toolConfigurationList.totalPages"
                  :number="toolConfigurationList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/tool-configuration/write'" v-if="access">
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
    // variable
    let toolConfigurationListByPriority = ref([]);
    let toolConfigurationList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let searchType2 = ref("TITLE");
    let searchKeyword2 = ref("");
    let access = ref("");
    let pageParam = {"page": 1};
    // previous searchType
    let previousSearchType;

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("tool-configuration");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "ToolConfigurationList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "ToolConfigurationList") {
        toolConfigurationList.value.number = toolConfigurationList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/list-access-authority",
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
      if (e.target.value === 'TARGET_TOOL_NAME') {
        searchKeyword.value = 'STATIC';
      } else if (previousSearchType === 'TARGET_TOOL_NAME' && e.target.value !== 'TARGET_TOOL_NAME') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 있는 list
      if (pageParam.page === 1 && isEmpty(searchKeyword.value)) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/high-priority-list",
            {},
        )
            .then((response) => {
              toolConfigurationListByPriority.value = response.data;
              // dayjs
              for (const toolConfiguration of toolConfigurationListByPriority.value) {
                toolConfiguration.createdDate = dayjs(toolConfiguration.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        toolConfigurationListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value,
        "searchType2": searchType2.value,
        "searchKeyword2": searchKeyword2.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-configurations/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // toolConfigurationList.value.content.length = 0;
            toolConfigurationList.value = response.data;
            startNumber.value = Math.floor(toolConfigurationList.value.number / 10) * 10 + 1;
            endNumber.value = (toolConfigurationList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (toolConfigurationList.value.totalPages == 0 ? 1 : toolConfigurationList.value.totalPages);

            // dayjs
            for (const toolConfiguration of toolConfigurationList.value.content) {
              toolConfiguration.createdDate = dayjs(toolConfiguration.createdDate).format("YYYY.MM.DD.");
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
            page: toolConfigurationList.value.number,
            pageName: 'ConfigurationList'
          }
      );
    });

    return {
      // variable
      toolConfigurationListByPriority, toolConfigurationList, searchType, searchKeyword, searchType2, searchKeyword2, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>
