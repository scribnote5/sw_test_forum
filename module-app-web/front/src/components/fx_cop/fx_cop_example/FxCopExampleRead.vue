<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <!-- Breadcrumb -->
    <Breadcrumb page=".NET Framework Design Guideline 예제 코드" :subPage="fxCopRule" :paths="['.NET Framework Design Guideline', '.NET Framework Design Guideline 예제 코드 보기']" :title="fxCopExample.title"/>

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
              <h2 class="mobile-title">{{ fxCopExample.title }}</h2>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="float-end">
                <strong class="additional-information-title">작성자: </strong><span class="additional-information-content">{{ fxCopExample.createdByUser.department }} {{ fxCopExample.createdByUser.name }}, </span>
                <strong class="additional-information-title">작성일: </strong><span class="additional-information-content">{{ fxCopExample.createdDate }}</span><br>

                <strong class="additional-information-title">최종 수정자: </strong><span class="additional-information-content">{{ fxCopExample.lastModifiedByUser.department }} {{ fxCopExample.lastModifiedByUser.name }}, </span>
                <strong class="additional-information-title">최종 수정일: </strong><span class="additional-information-content">{{ fxCopExample.lastModifiedDate }}, </span>
                <strong class="additional-information-title">조회수: </strong> <span class="additional-information-content">{{ fxCopExample.views }}</span>
              </div>
            </td>
          </tr>

          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>우선순위</th>
            <td>
              <Priority pageInformation="read" :priority="fxCopExample.priority" :maxPriority=Number(4)></Priority>
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>도구 정보</th>
            <td>
              {{ fxCopExample.toolName }}<br>
              {{ fxCopExample.toolNote }}
            </td>
          </tr>
          <tr class="d-none d-sm-none d-md-none d-lg-table-row">
            <th>컴파일러</th>
            <td>
              {{ fxCopExample.compilerName }}<br>
              {{ fxCopExample.compilerNote }}
            </td>
          </tr>

          <!-- Mobile -->
          <tr class="mobile-only-visible d-md-table-row d-lg-none">
            <td colspan="2">
              <strong>FxCop 규칙: </strong> {{ fxCopRule }} <br>
              <strong>우선순위: </strong>
              <Priority pageInformation="read" :priority="fxCopExample.priority" :maxPriority=Number(4)></Priority>
              <strong>도구 정보: </strong> {{ fxCopExample.toolName }}, {{ fxCopExample.toolNote }} <br>
              <strong>컴파일러: </strong> {{ fxCopExample.compilerName }}, {{ fxCopExample.compilerNote }}
            </td>
          </tr>

          <!-- 공통 -->
          <tr>
            <td colspan="2">
              <CodeMirror pageInformation="read" :nonCompliantExampleValue="nonCompliantExample" :compliantExampleValue="compliantExample" :badCasePositionList="badCasePositionList" :goodCasePositionList="goodCasePositionList"
                          mode="text/x-csharp"></CodeMirror>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="content ck-content" v-html="fxCopExample.content"></div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <Comment path="fx-cop-example-comments" idxName="fxCopExampleIdx" :idx="fxCopExample.idx" :commentList="fxCopExample.commentDtoList"></Comment>

      <div class="d-flex justify-content-between flex-column flex-md-row mx-3 my-5">
        <div class="d-flex">
          <router-link :to="this.$route.meta.fromRuleList ? '/fx-cop-example/list/' + fxCopExample.fxCopIdx : '/fx-cop-example/list'" class="me-2">
            <button class="btn btn-main-grey d-flex align-items-center">목록<img :src="require(`@/assets/images/list-white.svg`)" class="ms-2"></button>
          </router-link>
          <router-link :to="'/fx-cop/read/' + fxCopExample.fxCopIdx" class="ms-2">
            <button class="btn btn-main-dark-cyan d-flex align-items-center">규칙 이동<img :src="require(`@/assets/images/rewind-white.svg`)" class="ms-2"></button>
          </router-link>
        </div>
        <div class="d-flex mt-2 mt-md-0" v-if="fxCopExample.access">
          <router-link :to="'/fx-cop-example/update/' + idx" class="me-2">
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
    let fxCopExample = ref({createdByUser: {department: '', name: ''}, lastModifiedByUser: {department: '', name: ''}});
    // codeMirror
    let compliantExample = ref("");
    let nonCompliantExample = ref("");
    let badCasePositionList = ref("");
    let goodCasePositionList = ref("");
    // fxCop rule
    let fxCopRule = ref("");

    // onBeforeMount, init
    onBeforeMount(async () => {
      fireSuccessToast("fx-cop-example");

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop-examples/read/" + idx,
          {},
      )
          .then((response) => {
            fxCopExample.value = response.data;
            fxCopExample.value.content = fxCopExample.value.content;

            // codeMirror 설정
            nonCompliantExample.value = fxCopExample.value.nonCompliantExample;
            compliantExample.value = fxCopExample.value.compliantExample;
            badCasePositionList.value = fxCopExample.value.badCasePositionList;
            goodCasePositionList.value = fxCopExample.value.goodCasePositionList;

            // 댓글 설정
            for (let comment of fxCopExample.value.commentDtoList) {
              comment.content = comment.content.replace(/\n/g, "<br>");
              comment.createdDate = dayjs(comment.createdDate).format("YYYY.MM.DD. HH:mm");
            }

            // 공통 데이터 설정
            fxCopExample.value.createdDate = dayjs(fxCopExample.value.createdDate).format("YYYY.MM.DD. HH:mm");
            fxCopExample.value.lastModifiedDate = dayjs(fxCopExample.value.lastModifiedDate).format("YYYY.MM.DD. HH:mm");
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });

      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop/fx-cop-rule/" + fxCopExample.value.fxCopIdx,
          {},
      )
          .then((response) => {
            fxCopRule.value = response.data;
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
          axios.delete(process.env.VUE_APP_MODULE_APP_API_URL + "/api/fx-cop-examples/" + idx,
              {},
          )
              .then((response) => {
                localStorage.setItem("result", "/fx-cop-example/delete-success");
                router.push("/fx-cop-example/list/" + fxCopExample.value.fxCopIdx);
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
      fxCopExample, idx, compliantExample, nonCompliantExample, badCasePositionList, goodCasePositionList, fxCopRule,

      // function
      deletePost
    }
  }
}
</script>