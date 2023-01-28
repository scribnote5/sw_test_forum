<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="질문 답변" :paths="['질문 답변', '질문 답변 리스트']" title=""/>

    <div class="container-fluid">
      <div class="page-search">
        <div class="d-flex justify-content-end mt-4">
          <select v-model="searchType" @change="searchTypeChange($event)" class="form-select form-select-sm" style="width: auto">
            <option value="TITLE">제목</option>
            <option value="TYPE">상태</option>
            <option value="CONTENT">내용</option>
            <option value="CREATED_BY">작성자</option>
          </select>
          <input v-if="searchType !== 'TYPE'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="inputSearchKeyword" type="search" class="custom-search-input mx-2" placeholder="검색어">
          <select v-if="searchType === 'TYPE'" v-on:keyup.enter="searchList" v-model="searchKeyword" name="selectSearchKeyword" class="form-select form-select-sm mx-2" style="width: auto">
            <option value="STATIC_TEST_QUESTION">정적시험 질문</option>
            <option value="DYNAMIC_TEST_QUESTION">동적시험 질문</option>
            <option value="TOOL_QUESTION">도구 질문</option>
            <option value="ETC_QUESTION">기타 질문</option>
            <option value="COMPLETE">완료</option>
            <option value="ANSWER">답변</option>
          </select>
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

          <!-- questionAnswerList -->
          <tr v-for="(questionAnswer, i) in questionAnswerList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ questionAnswerList.totalElements - questionAnswerList.pageable.offset - i }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ questionAnswerList.totalElements - questionAnswerList.pageable.offset - i }}. </span>
              <!-- 공통 -->
              <router-link :to="'/question-answer/read/' + questionAnswer.idx">
                <span v-if="questionAnswer.type == 'STATIC_TEST_QUESTION'" class="question-answer-test-type">정적시험 질문</span>
                <span v-if="questionAnswer.type == 'DYNAMIC_TEST_QUESTION'" class="question-answer-test-type">동적시험 질문</span>
                <span v-if="questionAnswer.type == 'TOOL_QUESTION'" class="question-answer-tool-type">도구 질문</span>
                <span v-if="questionAnswer.type == 'ETC_QUESTION'" class="question-answer-etc-type">기타 질문</span>
                <span v-if="questionAnswer.type == 'COMPLETE'" class="question-answer-complete-type">완료</span>
                <span v-if="questionAnswer.type == 'ANSWER'">&nbsp;&nbsp;<img :src="require(`@/assets/images/corner-down-right-main-black.svg`)" class="answer-icon"/></span>
                {{ questionAnswer.title }}
              </router-link>
              <span class="comment-count">{{ questionAnswer.commentDtoCount }}</span>
              <img v-if="questionAnswer.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ questionAnswer.createdByUser.department }} {{ questionAnswer.createdByUser.name }} </span> <br>
                  <span class="mobile-content">{{ questionAnswer.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ questionAnswer.views }}</span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ questionAnswer.createdByUser.department }} {{ questionAnswer.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ questionAnswer.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ questionAnswer.views }}</td>
          </tr>

          <tr v-if="questionAnswerList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="questionAnswerList.first" :last="questionAnswerList.last" :totalPages="questionAnswerList.totalPages"
                  :number="questionAnswerList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/question-answer/question/write'">
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
    let questionAnswerList = ref({content: {}});
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
      fireSuccessToast("questionAnswer");

      // 검색 정보 불러오기
      if (store.state.pageInfo.pageName === "QuestionAnswerList") {
        searchType.value = store.state.pageInfo.searchType;
        previousSearchType = store.state.pageInfo.searchType;
        searchKeyword.value = store.state.pageInfo.searchKeyword;
        pageParam.page = store.state.pageInfo.page + 1;
      }

      await searchList(pageParam);

      // 페이지 정보 불러오기
      if (store.state.pageInfo.pageName === "QuestionAnswerList") {
        questionAnswerList.value.number = questionAnswerList.value.number < endNumber.value ? store.state.pageInfo.page : 0;
      }

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/list-access-authority",
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
      if (e.target.value === 'TYPE') {
        searchKeyword.value = 'STATIC_TEST_QUESTION';
      } else if (previousSearchType === 'TYPE' && e.target.value !== 'TYPE') {
        searchKeyword.value = '';
      }

      previousSearchType = e.target.value;
    }

    /* 검색 */
    const searchList = async (pageParam) => {
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/question-answers/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            questionAnswerList.value = response.data;
            startNumber.value = Math.floor(questionAnswerList.value.number / 10) * 10 + 1;
            endNumber.value = (questionAnswerList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (questionAnswerList.value.totalPages == 0 ? 1 : questionAnswerList.value.totalPages);

            // dayjs
            for (const questionAnswer of questionAnswerList.value.content) {
              questionAnswer.createdDate = dayjs(questionAnswer.createdDate).format("YYYY.MM.DD.");
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
            page: questionAnswerList.value.number,
            pageName: 'QuestionAnswerList'
          }
      );
    });

    return {
      // variable
      questionAnswerList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList, searchTypeChange
    }
  }
}
</script>