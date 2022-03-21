<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="Controller Tester 트러블슈팅" :paths="['Controller Tester', 'Controller Tester 트러블슈팅 리스트']" title=""/>

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
          <!-- controllerTesterToolListByPriority -->
          <tr v-for="(controllerTesterTool, i) in controllerTesterToolListByPriority" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ controllerTesterTool.idx }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ controllerTesterTool.idx }}. </span>
              <!-- 공통 -->
              <img :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
              <router-link :to="'/controller-tester-tool/read/' + controllerTesterTool.idx">{{ controllerTesterTool.title }}</router-link>
              <span class="comment-count">{{ controllerTesterTool.commentDtoCount }}</span>
              <img v-if="controllerTesterTool.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ controllerTesterTool.createdByUser.department }} {{ controllerTesterTool.createdByUser.name }} </span> <br>
                  <span class="mobile-content">
                    <span v-if="controllerTesterTool.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
                    <span v-if="controllerTesterTool.toolCategory == 'FUNCTION'">기능</span>
                    <span v-if="controllerTesterTool.toolCategory == 'INFORMATION'">정보</span>
                    <span v-if="controllerTesterTool.toolCategory == 'ETC'">기타</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ controllerTesterTool.createdDate }}</span> &nbsp;
                  <span class="mobile-content"> 조회수: {{ controllerTesterTool.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="controllerTesterTool.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ controllerTesterTool.createdByUser.department }} {{ controllerTesterTool.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="controllerTesterTool.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="controllerTesterTool.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="controllerTesterTool.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="controllerTesterTool.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="controllerTesterTool.toolCategory == 'ETC'">기타</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ controllerTesterTool.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ controllerTesterTool.views }}</td>
          </tr>

          <!-- controllerTesterToolList -->
          <tr v-for="(controllerTesterTool, i) in controllerTesterToolList.content" :key="i">
            <!-- Desktop 번호 -->
            <td class="d-none d-lg-table-cell text-center">{{ controllerTesterTool.idx }}</td>
            <td>
              <!-- Mobile -->
              <span class="d-inline d-lg-none mobile-number">{{ controllerTesterTool.idx }}. </span>
              <!-- 공통 -->
              <img v-if="controllerTesterTool.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
              <router-link :to="'/controller-tester-tool/read/' + controllerTesterTool.idx"> {{ controllerTesterTool.title }}</router-link>
              <span class="comment-count">{{ controllerTesterTool.commentDtoCount }}</span>
              <img v-if="controllerTesterTool.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
              <!-- Mobile -->
              <div class="d-inline d-lg-none">
                <div>
                  <span class="mobile-content">{{ controllerTesterTool.createdByUser.department }} {{ controllerTesterTool.createdByUser.name }} </span> <br>
                  <span class="mobile-content">
                    <span v-if="controllerTesterTool.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
                    <span v-if="controllerTesterTool.toolCategory == 'FUNCTION'">기능</span>
                    <span v-if="controllerTesterTool.toolCategory == 'INFORMATION'">정보</span>
                    <span v-if="controllerTesterTool.toolCategory == 'ETC'">기타</span>
                  </span> &nbsp;
                  <span class="mobile-content">{{ controllerTesterTool.createdDate }}</span> &nbsp;
                  <span class="mobile-content">조회수: {{ controllerTesterTool.views }}</span> &nbsp;
                  <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="controllerTesterTool.hashTags"></HashTags></span> &nbsp;
                </div>
              </div>
            </td>
            <!-- Desktop -->
            <td class="d-none d-lg-table-cell">{{ controllerTesterTool.createdByUser.department }} {{ controllerTesterTool.createdByUser.name }}</td>
            <td class="d-none d-lg-table-cell text-start">
              <HashTags pageInformation="list" :hash-tags="controllerTesterTool.hashTags"></HashTags>
            </td>
            <td class="d-none d-lg-table-cell text-center">
              <span v-if="controllerTesterTool.toolCategory == 'INSTALL_SETTING'">설치 및 설정</span>
              <span v-if="controllerTesterTool.toolCategory == 'FUNCTION'">기능</span>
              <span v-if="controllerTesterTool.toolCategory == 'INFORMATION'">정보</span>
              <span v-if="controllerTesterTool.toolCategory == 'ETC'">기타</span>
            </td>
            <td class="d-none d-lg-table-cell text-center">{{ controllerTesterTool.createdDate }}</td>
            <td class="d-none d-lg-table-cell text-center">{{ controllerTesterTool.views }}</td>
          </tr>

          <tr v-if="controllerTesterToolList.content.length === 0">
            <td colspan="10" class="no-posts-table">
              등록된 게시글이 없습니다.
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <Pagination :first="controllerTesterToolList.first" :last="controllerTesterToolList.last" :totalPages="controllerTesterToolList.totalPages"
                  :number="controllerTesterToolList.number" :startNumber="startNumber" :endNumber="endNumber"
                  :searchList="searchList"/>

      <div class="d-flex justify-content-end mx-4 mb-4" v-if="access">
        <router-link :to="'/controller-tester-tool/write'" v-if="access">
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
import {onBeforeMount, ref} from 'vue'
import {useRoute} from "vue-router";
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
    // variable
    let controllerTesterToolListByPriority = ref([]);
    let controllerTesterToolList = ref({content: {}});
    let startNumber = ref(0);
    let endNumber = ref(0);
    let searchType = ref("TITLE");
    let searchKeyword = ref("");
    let access = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("controller-tester-tool");

      await searchList({"page": 1});

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/controller-tester-tools/list-access-authority",
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
        await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/controller-tester-tools/high-priority-list",
            {},
        )
            .then((response) => {
              controllerTesterToolListByPriority.value = response.data;
              // dayjs
              for (const controllerTesterTool of controllerTesterToolListByPriority.value) {
                controllerTesterTool.createdDate = dayjs(controllerTesterTool.createdDate).format("YYYY.MM.DD.");
              }
            })
            .catch((error) => {
              parseErrorMsg(error.response);
            })
            .then(() => {
            });
      } else {
        controllerTesterToolListByPriority.value.length = 0;
      }

      //우선순위가 없는 list
      const searchParam = {
        "searchType": searchType.value,
        "searchKeyword": searchKeyword.value
      };
      const params = {...pageParam, ...searchParam};
      const uri = createUri(process.env.VUE_APP_MODULE_APP_API_URL + "/api/controller-tester-tools/list", params);

      await axios.get(uri,
          {},
      )
          .then((response) => {
            // controllerTesterToolList.value.content.length = 0;
            controllerTesterToolList.value = response.data;
            startNumber.value = Math.floor(controllerTesterToolList.value.number / 10) * 10 + 1;
            endNumber.value = (controllerTesterToolList.value.totalPages > startNumber.value + 9) ? startNumber.value + 9 : (controllerTesterToolList.value.totalPages == 0 ? 1 : controllerTesterToolList.value.totalPages);

            // dayjs
            for (const controllerTesterTool of controllerTesterToolList.value.content) {
              controllerTesterTool.createdDate = dayjs(controllerTesterTool.createdDate).format("YYYY.MM.DD.");
            }
          })
          .catch((error) => {
            parseErrorMsg(error.response);
          })
          .then(() => {
          });
    }

    return {
      // variable
      controllerTesterToolListByPriority, controllerTesterToolList, searchType, searchKeyword, startNumber, endNumber, access,

      // function
      searchList
    }
  }
}
</script>
