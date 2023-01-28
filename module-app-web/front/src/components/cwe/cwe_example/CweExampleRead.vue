<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="CWE C/C++ 예제 코드" :subPage="cweRule" :paths="['CWE C/C++', 'CWE C/C++ 예제 코드 보기']" :title="cweExample.title"/>

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
              <h2 class="mobile-title">{{ cweExample.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ cweExample.createdByUser.department }} {{ cweExample.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ cweExample.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ cweExample.lastModifiedByUser.department }} {{ cweExample.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ cweExample.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ cweExample.views }}</span>
              </div>
            </td>
          </tr>

          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="cweExample.priority" :maxPriority=Number(4)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ cweExample.toolName }}<br>
              {{ cweExample.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ cweExample.compilerName }}<br>
              {{ cweExample.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>CWE C/C++ 규칙: </strong> {{ cweRule }} <br>
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="cweExample.priority" :maxPriority=Number(4)></Priority>
              <strong>도구 정보: </strong> {{ cweExample.toolName }}, {{ cweExample.toolNote }} <br>
              <strong>컴파일러: </strong> {{ cweExample.compilerName }}, {{ cweExample.compilerNote }}
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
              <div class="content ck-content" v-html="cweExample.content"></div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <Comment path="cwe-example-comments" idxName="cweExampleIdx" :idx="cweExample.idx" :commentList="cweExample.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/cwe-example/list/' + cweExample.cweIdx : '/cwe-example/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/cwe/read/' + cweExample.cweIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="cweExample.access">
          <router-link :to="'/cwe-example/update/' + idx" class="me-2">
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
    let cweExample = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // cwe rule
    let cweRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("cwe-example");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-examples/read/" + idx,
          {},
      )
          .then((response) => {
            cweExample.value = response.data;
            cweExample.value.content = cweExample.value.content;

            // codeMirror 설정
            nonCompliantExample.value = cweExample.value.nonCompliantExample;
            compliantExample.value = cweExample.value.compliantExample;
            badCasePositionList.value = cweExample.value.badCasePositionList;
            goodCasePositionList.value = cweExample.value.goodCasePositionList;

            // 댓글 설정
            for (let comment of cweExample.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            cweExample.value.createdDate = dayjs(cweExample.value.createdDate).format("YYYY.MM.DD. HH:mm");
            cweExample.value.lastModifiedDate = dayjs(cweExample.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe/cwe-rule/" + cweExample.value.cweIdx,
          {},
      )
          .then((response) => {
            cweRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/cwe-examples/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/cwe-example/delete-success");
                router.push("/cwe-example/list/" + cweExample.value.cweIdx);
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
      cweExample, idx, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, cweRule,

      // function
      deletePost
    }
  }
}
</script>