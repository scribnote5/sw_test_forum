<template>
  <div>
    <table class="table table-hover mobile-table-list guideline-table-list mt-2">
      <thead>
      <tr class="d-none d-lg-table-row">
        <th style="width: 5%" class="text-center">번호</th>
        <th style="width: 45%" class="text-center">제목</th>
        <th style="width: 12.5%" class="text-center">작성자</th>
        <th style="width: 10%" class="text-center">해시태그</th>
        <th style="width: 10%" class="text-center">가이드라인 결과</th>
        <th style="width: 12.5%" class="text-center">작성일</th>
        <th style="width: 5%" class="text-center">조회수</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(guideline, i) in guidelineList" :key="i">
        <!-- Desktop 번호 -->
        <td class="d-none d-lg-table-cell text-center">{{ guidelineList.length - i }}</td>
        <td>
          <!-- Mobile -->
          <span class="d-inline d-lg-none mobile-number">{{ guidelineList.length - i }}. </span>
          <!-- 공통 -->
          <router-link :to="'' + guideline.link"> {{ guideline.title }}</router-link>
          <span class="comment-count">{{ guideline.commentDtoCount }}</span>
          <span v-if="guideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ guideline.likeCountInList }}</span></span>
          <img v-if="guideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
          <!-- Mobile -->
          <div class="d-inline d-lg-none">
            <div>
              <span class="mobile-content">{{ guideline.createdByUser.department }} {{ guideline.createdByUser.name }} </span> <br>
              <span class="mobile-content">{{ guideline.createdDate }}</span> &nbsp;
              <span class="mobile-content"> 조회수: {{ guideline.views }}</span> &nbsp;
              <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="guideline.hashTags"></HashTags></span> &nbsp;
              <span class="mobile-content" v-if="guideline.guidelineResult == 'COMPLETED'">수정 완료</span>
              <span class="mobile-content" v-if="guideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
              <span class="mobile-content" v-if="guideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
              <span class="mobile-content" v-if="guideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
              <span class="mobile-content" v-if="guideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
            </div>
          </div>
        </td>
        <!-- Desktop -->
        <td class="d-none d-lg-table-cell">{{ guideline.createdByUser.department }} &nbsp; {{ guideline.createdByUser.name }}</td>
        <td class="d-none d-lg-table-cell text-start">
          <HashTags pageInformation="list" :hash-tags="guideline.hashTags"></HashTags>
        </td>
        <td class="d-none d-lg-table-cell text-center">
          <span v-if="guideline.guidelineResult == 'COMPLETED'">수정 완료</span>
          <span v-if="guideline.guidelineResult == 'EXCLUDE'">사전 제외</span>
          <span v-if="guideline.guidelineResult == 'EXCEPTION'">예외 처리</span>
          <span v-if="guideline.guidelineResult == 'FALSE_ALARM'">도구 오탐</span>
          <span v-if="guideline.guidelineResult == 'FALSE_ALARM_PATCHED'">도구 오탐 패치 완료</span>
        </td>
        <td class="d-none d-lg-table-cell text-center">{{ guideline.createdDate }}</td>
        <td class="d-none d-lg-table-cell text-center">{{ guideline.views }}</td>
      </tr>

      <tr v-if="guidelineList.length === 0">
        <td colspan="10" class="no-posts-table">
          등록된 가이드라인이 없습니다.
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <div class="d-flex justify-content-end me-4 my-4">
    <router-link :to="link">
      <button class="btn btn-main-orange d-flex align-items-center">더보기<img :src="require(`@/assets/images/more-horizontal-white.svg`)" class="ms-2"></button>
    </router-link>
  </div>
</template>

<style lang="scss">
.guideline-table-list td {
  white-space: nowrap !important;
}
</style>

<script>
// components
import HashTags from '@/components/common/HashTags.vue'

export default {
  components: {
    HashTags
  },
  name: "GuidelineList",
  props: {
    guidelineList: Object,
    link: String
  }, setup(props) {

  }
}
</script>

