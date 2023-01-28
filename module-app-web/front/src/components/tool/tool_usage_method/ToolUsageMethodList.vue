<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="사용 방법" :paths="['도구', '사용 방법 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="HASH_TAGS">해시태그</option>
            <option value="TARGET_TOOL_NAME">대상 도구</option>
            <option value="TOOL_CATEGORY">도구 유형</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-if="searchType !== 'TARGET_TOOL_NAME' && searchType !== 'TOOL_CATEGORY'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <select v-if="searchType === 'TARGET_TOOL_NAME'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="STATIC">STATIC</option>
            <option value="COVER">COVER</option>
            <option value="CONTROLLER_TESTER">Controller Tester</option>
            <option value="VPES">VPES</option>
            <option value="TOOLCHAIN">툴체인</option>
          </select>
          <select v-if="searchType === 'TOOL_CATEGORY'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="INSTALL_SETTING">설치 및 설정</option>
            <option value="FUNCTION">기능</option>
            <option value="INFORMATION">정보</option>
            <option value="ETC">기타</option>
          </select>
          <img :src="require(`@/assets/images/search-main-black.svg`)" @click="searchList({page: 1})" class="cursor-pointer">
        </div>

        <div v-if="searchType === 'TARGET_TOOL_NAME' || searchType === 'TOOL_CATEGORY'" class="d-flex justify-content-end mt-2">
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
            <th style="width: 45%" class="text-center">제목</th>
            <th style="width: 12.5%" class="text-center">작성자</th>
            <th style="width: 10%" class="text-center">해시태그</th>
            <th style="width: 10%" class="text-center">도구 유형</th>
            <th style="width: 12.5%" class="text-center">작성일</th>
            <th style="width: 5%" class="text-center">조회수</th>
          </tr>
          </thead>
          <tbody>
          <!-- toolUsageMethodListByPriority -->
          <tr v-for="(toolUsageMethod, i) in toolUsageMethodListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number"><img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/></span>
              <!-- 공통 -->
              <router-link :to="'/tool-usage-method/read/' + toolUsageMethod.idx">
                <span v-if="toolUsageMethod.targetToolName == 'STATIC'" class="question-answer-test-type">STATIC</span>
                <span v-if="toolUsageMethod.targetToolName == 'COVER'" class="question-answer-tool-type">COVER</span>
                <span v-if="toolUsageMethod.targetToolName == 'CONTROLLER_TESTER'" class="question-answer-etc-type">Controller Tester</span>
                <span v-if="toolUsageMethod.targetToolName == 'VPES'" class="question-answer-answer-type">VPES</span>
                <span v-if="toolUsageMethod.targetToolName == 'TOOLCHAIN'" class="question-answer-complete-type">툴체인</span>
                {{ toolUsageMethod.title }}
              </router-link>
              <span class="comment-count">{{ toolUsageMethod.commentDtoCount }}</span>
              <img v-if="toolUsageMethod.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ toolUsageMethod.createdByUser.department }} {{ toolUsageMethod.createdByUser.name }} </span> <br>
                  <span class="mobile-content">
                    <span v-if="toolUsageMethod.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
                    <span v-if="toolUsageMethod.toolCategory == 'FUNCTION'">기능</span>
                    <span v-if="toolUsageMethod.toolCategory == 'INFORMATION'">정보</span>
                    <span v-if="toolUsageMethod.toolCategory == 'ETC'">기타</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ toolUsageMethod.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ toolUsageMethod.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="toolUsageMethod.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ toolUsageMethod.createdByUser.department }} {{ toolUsageMethod.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="toolUsageMethod.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="toolUsageMethod.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="toolUsageMethod.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="toolUsageMethod.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="toolUsageMethod.toolCategory == 'ETC'">기타</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ toolUsageMethod.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ toolUsageMethod.views }}</td>
          </tr>

          <!-- toolUsageMethodList -->
          <tr v-for="(toolUsageMethod, i) in toolUsageMethodList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ toolUsageMethodList.totalElements - toolUsageMethodList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ toolUsageMethodList.totalElements - toolUsageMethodList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/tool-usage-method/read/' + toolUsageMethod.idx">
                <span v-if="toolUsageMethod.targetToolName == 'STATIC'" class="question-answer-test-type">STATIC</span>
                <span v-if="toolUsageMethod.targetToolName == 'COVER'" class="question-answer-tool-type">COVER</span>
                <span v-if="toolUsageMethod.targetToolName == 'CONTROLLER_TESTER'" class="question-answer-etc-type">Controller Tester</span>
                <span v-if="toolUsageMethod.targetToolName == 'VPES'" class="question-answer-answer-type">VPES</span>
                <span v-if="toolUsageMethod.targetToolName == 'TOOLCHAIN'" class="question-answer-complete-type">툴체인</span>
                {{ toolUsageMethod.title }}
              </router-link>
              <span class="comment-count">{{ toolUsageMethod.commentDtoCount }}</span>
              <img v-if="toolUsageMethod.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ toolUsageMethod.createdByUser.department }} {{ toolUsageMethod.createdByUser.name }} </span> <br>
                  <span class="mobile-content">
                    <span v-if="toolUsageMethod.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
                    <span v-if="toolUsageMethod.toolCategory == 'FUNCTION'">기능</span>
                    <span v-if="toolUsageMethod.toolCategory == 'INFORMATION'">정보</span>
                    <span v-if="toolUsageMethod.toolCategory == 'ETC'">기타</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ toolUsageMethod.createdDate }}</span> &nbsp;
                  <span class="mobile-content">조회수: {{ toolUsageMethod.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="toolUsageMethod.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ toolUsageMethod.createdByUser.department }} {{ toolUsageMethod.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="toolUsageMethod.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="toolUsageMethod.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="toolUsageMethod.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="toolUsageMethod.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="toolUsageMethod.toolCategory == 'ETC'">기타</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ toolUsageMethod.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ toolUsageMethod.views }}</td>
          </tr>

          <tr v-if="toolUsageMethodList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="toolUsageMethodList.first" :last="toolUsageMethodList.last" :totalPages="toolUsageMethodList.totalPages"
                  :number="toolUsageMethodList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/tool-usage-method/write'" v-if="access">
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
    let toolUsageMethodListByPriority = ref([]);
    let toolUsageMethodList = ref({content: {}});
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
      fireSuccessToast("tool-usage-method");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "ToolUsageMethodList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "ToolUsageMethodList") {
        toolUsageMethodList.value.number = toolUsageMethodList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-usage-methods/list-access-authority",
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
      } else if (e.target.value === 'TOOL_CATEGORY') {
        searchKeyword.value = 'INSTALL_SETTING';
      } else if (previousSearchType === 'TARGET_TOOL_NAME' && e.target.value !== 'TARGET_TOOL_NAME') {
        searchKeyword.value = '';
      } else if (previousSearchType === 'TOOL_CATEGORY' && e.target.value !== 'TOOL_CATEGORY') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      //우선순위가 있는 list
      if (pageParam.page === 1 && isEmpty(searchKeyword.value)) {
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-usage-methods/high-priority-list",
            {},
        )
            .then((response) => {
              toolUsageMethodListByPriority.value = response.data;
              // dayjs
              for (const toolUsageMethod of toolUsageMethodListByPriority.value) {
                toolUsageMethod.createdDate = dayjs(toolUsageMethod.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseApiErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        toolUsageMethodListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value,
        "searchType2": searchType2.value,
        "searchKeyword2": searchKeyword2.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/tool-usage-methods/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // toolUsageMethodList.value.content.length = 0;
            toolUsageMethodList.value = response.data;
            startNumber.value = Math.floor(toolUsageMethodList.value.number / 10) * 10 + 1;
            endNumber.value = (toolUsageMethodList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (toolUsageMethodList.value.totalPages == 0 ? 1 : toolUsageMethodList.value.totalPages);

            // dayjs
            for (const toolUsageMethod of toolUsageMethodList.value.content) {
              toolUsageMethod.createdDate = dayjs(toolUsageMethod.createdDate).format("YYYY.MM.DD.");
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
            page: toolUsageMethodList.value.number,
            pageName: 'ToolUsageMethodList'
          }
      );
    });

    return {
      // variable
      toolUsageMethodListByPriority, toolUsageMethodList, searchType, searchKeyword, searchType2, searchKeyword2, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>
