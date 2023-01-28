<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="MISRA C++ 예제 코드" :subPage="misraCppRule" :paths="['MISRA C++', 'MISRA C++ 예제 코드 보기']" :title="misraCppExample.title"/>

    <div class="container-fluid">
      <div class="page-content">
        <table class="table mobile-table-read mt-4">
          <thead>
          <th style="width: 17.5%;" class="text-center"></th>
          <th style="width: 82.5%" class="text-center"></th>
          </thead>
          <tbody>
          <tr>
            <td colspan="2">
              <h2 class="mobile-title">{{ misraCppExample.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ misraCppExample.createdByUser.department }} {{ misraCppExample.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ misraCppExample.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ misraCppExample.lastModifiedByUser.department }} {{ misraCppExample.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ misraCppExample.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ misraCppExample.views }}</span>
              </div>
            </td>
          </tr>

          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="misraCppExample.priority" :maxPriority=Number(4)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ misraCppExample.toolName }}<br>
              {{ misraCppExample.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ misraCppExample.compilerName }}<br>
              {{ misraCppExample.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>MISRA C++ 규칙: </strong> {{ misraCppRule }} <br>
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="misraCppExample.priority" :maxPriority=Number(4)></Priority>
              <strong>도구 정보: </strong> {{ misraCppExample.toolName }}, {{ misraCppExample.toolNote }} <br>
              <strong>컴파일러: </strong> {{ misraCppExample.compilerName }}, {{ misraCppExample.compilerNote }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="read" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-c++src"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="misraCppExample.content"></div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <Comment path="misra-cpp-example-comments" idxName="misraCppExampleIdx" :idx="misraCppExample.idx" :commentList="misraCppExample.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/misra-cpp-example/list/' + misraCppExample.misraCppIdx : '/misra-cpp-example/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/misra-cpp/read/' + misraCppExample.misraCppIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="misraCppExample.access">
          <router-link :to="'/misra-cpp-example/update/' + idx" class="me-2">
            <button class="btn btn-main-blue d-flex align-items-center">수정<img :src="require(`@/assets/images/update-white.svg`)" class="ms-2"></button>
          </router-link>
          <button @click="deletePost()" class="btn btn-main-red d-flex align-items-center">삭제<img :src="require(`@/assets/images/delete-white.svg`)" class="ms-2"></button>
        </div>
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
import Priority from '@/components/common/Priority.vue'
import CodeMirror from '@/components/common/CodeMirror.vue'
import Comment from '@/components/common/Comment.vue'

// vue.js
import {onBeforeMount, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from "axios";
// day.js
import dayjs from 'dayjs'
// plugins
import {confirm, fireSuccessToast} from '@/assets/plugins/sweetalert2/sweetalert2'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";

export default {
  components: {
    Loading,
    Breadcrumb,
    Priority,
    CodeMirror,
    Comment
  },
  setup() {
    // vue.js
    const route = useRoute();
    const router = useRouter();
    const idx = route.params.idx;
    // variable
    let misraCppExample = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // misra cpp rule
    let misraCppRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("misra-cpp-example");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-examples/read/" + idx,
          {},
      )
          .then((response) => {
            misraCppExample.value = response.data;
            misraCppExample.value.content = misraCppExample.value.content;

            // codeMirror 설정
            nonCompliantExample.value = misraCppExample.value.nonCompliantExample;
            compliantExample.value = misraCppExample.value.compliantExample;
            badCasePositionList.value = misraCppExample.value.badCasePositionList;
            goodCasePositionList.value = misraCppExample.value.goodCasePositionList;

            // 댓글 설정
            for (let comment of misraCppExample.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            misraCppExample.value.createdDate = dayjs(misraCppExample.value.createdDate).format("YYYY.MM.DD. HH:mm");
            misraCppExample.value.lastModifiedDate = dayjs(misraCppExample.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp/misra-cpp-rule/" + misraCppExample.value.misraCppIdx,
          {},
      )
          .then((response) => {
            misraCppRule.value = response.data;
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    /* 삭제 */
    const deletePost = () => {
      confirm.fire({
        title: "삭제하시겠습니까?",
        text: "게시글과 관련된 모든 데이터는 삭제됩니다.",
        confirmButtonText: '네',
        cancelButtonText: '아니오'
      }).then((result) => {
        if (result.isConfirmed) {
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/misra-cpp-examples/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/misra-cpp-example/delete-success");
                router.push("/misra-cpp-example/list/" + misraCppExample.value.misraCppIdx);
              })
              .catch((error) => {
                parseApiErrorMsg(error.response);
              })
              .then(() => {
              });
        } else {
          return false;
        }
      })
    }

    return {
      // variable
      misraCppExample, idx, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, misraCppRule,

      // function
      deletePost
    }
  }
}
</script>