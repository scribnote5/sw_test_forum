<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page="메트릭 예제 코드" :subPage="metricRule" :paths="['메트릭', '메트릭 예제 코드 보기']" :title="metricExample.title"/>

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
              <h2 class="mobile-title">{{ metricExample.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ metricExample.createdByUser.department }} {{ metricExample.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ metricExample.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ metricExample.lastModifiedByUser.department }} {{ metricExample.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ metricExample.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ metricExample.views }}</span>
              </div>
            </td>
          </tr>

          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="metricExample.priority" :maxPriority=Number(4)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ metricExample.toolName }}<br>
              {{ metricExample.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ metricExample.compilerName }}<br>
              {{ metricExample.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>메트릭 규칙: </strong> {{ metricRule }} <br>
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="metricExample.priority" :maxPriority=Number(4)></Priority>
              <strong>도구 정보: </strong> {{ metricExample.toolName }}, {{ metricExample.toolNote }} <br>
              <strong>컴파일러: </strong> {{ metricExample.compilerName }}, {{ metricExample.compilerNote }}
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
              <div class="content ck-content" v-html="metricExample.content"></div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <Comment path="metric-example-comments" idxName="metricExampleIdx" :idx="metricExample.idx" :commentList="metricExample.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/metric-example/list/' + metricExample.metricIdx : '/metric-example/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/metric/read/' + metricExample.metricIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="metricExample.access">
          <router-link :to="'/metric-example/update/' + idx" class="me-2">
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
    let metricExample = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // metric rule
    let metricRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("metric-example");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric-examples/read/" + idx,
          {},
      )
          .then((response) => {
            metricExample.value = response.data;
            metricExample.value.content = metricExample.value.content;

            // codeMirror 설정
            nonCompliantExample.value = metricExample.value.nonCompliantExample;
            compliantExample.value = metricExample.value.compliantExample;
            badCasePositionList.value = metricExample.value.badCasePositionList;
            goodCasePositionList.value = metricExample.value.goodCasePositionList;

            // 댓글 설정
            for (let comment of metricExample.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            metricExample.value.createdDate = dayjs(metricExample.value.createdDate).format("YYYY.MM.DD. HH:mm");
            metricExample.value.lastModifiedDate = dayjs(metricExample.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric/metric-rule/" + metricExample.value.metricIdx,
          {},
      )
          .then((response) => {
            metricRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/metric-examples/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/metric-example/delete-success");
                router.push("/metric-example/list/" + metricExample.value.metricIdx);
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
      metricExample, idx, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, metricRule,

      // function
      deletePost
    }
  }
}
</script>