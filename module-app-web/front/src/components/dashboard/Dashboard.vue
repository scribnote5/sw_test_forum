<template>
  <section>
    <!-- Loading -->
    <Loading></Loading>

    <div class="container-fluid">
      <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <div class=" ps-1">
          <h2>대시보드</h2>
        </div>

        <div class="pe-1">
          <div class="link-wrapper mt-lg-n5 mt-md-n0 ">
            <a :href="'mailto:' + setting.developerEmail">
              <img :src="require(`@/assets/images/mail.svg`)" class="me-1"/>관리자에게 메일 보내기
            </a> <br>

            초기 배포일: {{ initialReleaseDate }}, 최신 배포일: {{ lastReleaseDate }}
          </div>
        </div>

      </div>

      <div class="row mt-4">
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #4e73df; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <img :src="require(`@/assets/images/users.svg`)" class="me-1"/>
                가입 회원 수
              </h5>
              <div class="text-center">
                <span class="card-count">{{ usersCount }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #1cc88a; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <img :src="require(`@/assets/images/posts.svg`)" class="me-1"/>
                등록된 게시글 수
              </h5>
              <div class="text-center">
                <span class="card-count">{{ postsCount }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #36b9cc; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <div class="dropdown">
                  <img :src="require(`@/assets/images/calendar.svg`)"/>
                  <button class="btn btn-sm dropdown-toggle" type="button" id="dropdownStaticsButton" data-bs-toggle="dropdown" aria-expanded="false">
                    하루 동안 접속한 회원 수
                  </button>
                  <ul class="dropdown-menu" aria-labelledby="dropdownStaticsButton" @click="changeStatics($event)">
                    <li><span class="dropdown-item">하루 동안 접속한 회원 수</span></li>
                    <li><span class="dropdown-item">7일간 접속한 회원 수</span></li>
                    <li><span class="dropdown-item">28일간 접속한 회원 수</span></li>
                    <li><span class="dropdown-item">하루 동안 등록된 게시글 수</span></li>
                    <li><span class="dropdown-item">7일간 등록된 게시글 수</span></li>
                    <li><span class="dropdown-item">28일간 등록된 게시글 수</span></li>
                  </ul>
                </div>
              </h5>
              <div class="text-center">
                <span class="card-count" id="staticsCount">{{ loginUsersCountBeforeYesterday }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="col-xl-3 col-md-6 mb-4">
          <div class="card shadow h-100 py-2" style="border-left: .5rem solid #f6c23e; border-radius: .75rem;">
            <div class="card-body static-card-body">
              <h5 class="card-title">
                <img :src="require(`@/assets/images/hard-drive.svg`)" class="me-1"/>
                메모리 공간
              </h5>
              <div v-for="(diskUtil, i) in diskUtilList" :key="i">
                <span class="memory-name me-1">{{ diskUtil.driverName }}</span>
                <span class="memory-space">{{ diskUtil.usedSize }} / {{ diskUtil.totalSize }}</span> 사용
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-6">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0">공지사항</h5>
              <router-link to="/notice/list">
                <img :src="require(`@/assets/images/more-vertical.svg`)"/>
              </router-link>
            </div>

            <div class="card-body py-1">
              <table class="table table-hover mobile-table-list">
                <thead>
                <tr class="d-none d-lg-table-row">
                  <th style="width: 80%" class="text-center">제목</th>
                  <th style="width: 20%" class="text-center">작성일</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(notice, i) in noticeList" :key="i">
                  <td>
                    <!-- 공통 -->
                    <img v-if="notice.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                    <router-link :to="'/notice/read/' + notice.idx"> {{ notice.title }}</router-link>
                    <span class="comment-count">{{ notice.commentDtoCount }}</span>
                    <img v-if="notice.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                    <!-- Mobile -->
                    <div class="d-inline d-lg-none">
                      <div>
                        <span class="mobile-content">{{ notice.createdDate }}</span>
                      </div>
                    </div>
                  </td>
                  <!-- Desktop -->
                  <td class="d-none d-lg-table-cell text-center">{{ notice.createdDate }}</td>
                </tr>

                <tr v-if="noticeList.length === 0">
                  <td colspan="10" class="no-posts-table">
                    등록된 게시글이 없습니다.
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="col-lg-6">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0">정적시험 규칙 현황</h5>
            </div>
            <div class="card-body py-1">
              <div class="my-2">
                <h6>작성된 MISRA C 규칙: {{ misraCPostsCount }} / {{ setting.totalMisraCRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredMisraCPostsRatio" class="progress-bar bg-primary progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredMisraCPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-2">
                <h6>작성된 MISRA C++ 규칙: {{ misraCppPostsCount }} / {{ setting.totalMisraCppRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredMisraCppPostsRatio" class="progress-bar bg-secondary progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredMisraCppPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-2">
                <h6>작성된 CWE C/C++ 규칙: {{ cwePostsCount }} / {{ setting.totalCweRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredCwePostsRatio" class="progress-bar bg-success progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredCwePostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-2">
                <h6>작성된 Java Code Conventions 규칙: {{ javaCodeConventionsPostsCount }} / {{ setting.totalJavaCodeConventionsRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredJavaCodeConventionsPostsRatio" class="progress-bar bg-danger progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredJavaCodeConventionsPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-2">
                <h6>작성된 CWE Java 규칙: {{ cweJavaPostsCount }} / {{ setting.totalCweJavaRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredCweJavaPostsRatio" class="progress-bar bg-warning progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredCweJavaPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-2">
                <h6>작성된 C# Coding Conventions 규칙: {{ styleCopPostsCount }} / {{ setting.totalStyleCopRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredStyleCopPostsRatio" class="progress-bar bg-info progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredStyleCopPostsRatio }}%
                  </div>
                </div>
              </div>
              <div class="my-2">
                <h6>작성된 .NET Framework Design Guideline 규칙: {{ fxCopPostsCount }} / {{ setting.totalFxCopRuleNumber }} </h6>
                <div class="progress">
                  <div id="registeredFxCopPostsRatio" class="progress-bar bg-dark progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">
                    {{ registeredFxCopPostsRatio }}%
                  </div>
                </div>
              </div>
              <hr>
              <div class="my-2">
                <h6>작성된 MISRA C 가이드라인 개수: {{ misraCGuidelinePostsCount }}</h6>
              </div>
              <div class="my-2">
                <h6>작성된 MISRA C++ 가이드라인 개수: {{ misraCppGuidelinePostsCount }}</h6>
              </div>
              <div class="my-2">
                <h6>작성된 CWE C/C++ 가이드라인 개수: {{ cweGuidelinePostsCount }}</h6>
              </div>
              <div class="my-2">
                <h6>작성된 Java Code Conventions 가이드라인 개수: {{ javaCodeConventionsGuidelinePostsCount }}</h6>
              </div>
              <div class="my-2">
                <h6>작성된 CWE Java 가이드라인 개수: {{ cweJavaGuidelinePostsCount }}</h6>
              </div>
              <div class="my-2">
                <h6>작성된 C# Coding Conventions 가이드라인 개수: {{ styleCopGuidelinePostsCount }}</h6>
              </div>
              <div class="my-2">
                <h6>작성된 .NET Framework Design Guideline 가이드라인 개수: {{ fxCopGuidelinePostsCount }}</h6>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-12">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <nav>
                <h5>가장 많이 본 규칙</h5>
                <div class="nav nav-tabs" id="navRuleTab" role="tablist">
                  <button class="nav-link active" id="navMisraRuleCTab" data-bs-toggle="tab" data-bs-target="#navMisraCRule" type="button" role="tab" aria-controls="navMisraCRule" aria-selected="true">MISRA C</button>
                  <button class="nav-link" id="navMisraCppRuleTab" data-bs-toggle="tab" data-bs-target="#navMisraCppRule" type="button" role="tab" aria-controls="navMisraCppRule" aria-selected="false">MISRA C++</button>
                  <button class="nav-link" id="navCweRuleTab" data-bs-toggle="tab" data-bs-target="#navCweRule" type="button" role="tab" aria-controls="navCweRule" aria-selected="false">CWE C/C++</button>
                  <button class="nav-link" id="navStyleCopRuleTab" data-bs-toggle="tab" data-bs-target="#navStyleCopRule" type="button" role="tab" aria-controls="navStyleCopRule" aria-selected="false">C# Coding Convention</button>
                </div>
              </nav>
            </div>
            <div class="tab-content" id="navTabRuleContent">
              <div id="navMisraCRule" class="tab-pane fade show active card-body py-1" role="tabpanel" aria-labelledby="navMisraRuleCTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(misraC, i) in misraCList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="misraC.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/misra-c/read/' + misraC.idx"> {{ misraC.title }}</router-link>
                      <span class="comment-count">{{ misraC.commentDtoCount }}</span>
                      <img v-if="misraC.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ misraC.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ misraC.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="misraC.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="misraC.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraC.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraC.views }}</td>
                  </tr>
                  <tr v-if="misraCList.length === 0" class="tab-pane fade show active" id="navMisraCRule" role="tabpanel" aria-labelledby="navMisraCRuleTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div id="navMisraCppRule" class="tab-pane fade card-body py-1" role="tabpanel" aria-labelledby="navMisraCppRuleTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(misraCpp, i) in misraCppList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="misraCpp.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/misra-cpp/read/' + misraCpp.idx"> {{ misraCpp.title }}</router-link>
                      <span class="comment-count">{{ misraCpp.commentDtoCount }}</span>
                      <img v-if="misraCpp.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ misraCpp.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ misraCpp.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="misraCpp.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="misraCpp.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraCpp.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraCpp.views }}</td>
                  </tr>
                  <tr v-if="misraCppList.length === 0" class="tab-pane fade show active" id="navMisraCppRule" role="tabpanel" aria-labelledby="navMisraCppRuleTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div id="navCweRule" class="tab-pane fade card-body py-1" role="tabpanel" aria-labelledby="navCweRuleTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(cwe, i) in cweList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="cwe.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/cwe/read/' + cwe.idx"> {{ cwe.title }}</router-link>
                      <span class="comment-count">{{ cwe.commentDtoCount }}</span>
                      <img v-if="cwe.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ cwe.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ cwe.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="cwe.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="cwe.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ cwe.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ cwe.views }}</td>
                  </tr>
                  <tr v-if="cweList.length === 0" class="tab-pane fade show active" id="navCweRule" role="tabpanel" aria-labelledby="navCweRuleTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div id="navStyleCopRule" class="tab-pane fade card-body py-1" role="tabpanel" aria-labelledby="navStyleCopRuleTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(styleCop, i) in styleCopList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="styleCop.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/style-cop/read/' + styleCop.idx"> {{ styleCop.title }}</router-link>
                      <span class="comment-count">{{ styleCop.commentDtoCount }}</span>
                      <img v-if="styleCop.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ styleCop.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ styleCop.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="styleCop.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="styleCop.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ styleCop.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ styleCop.views }}</td>
                  </tr>
                  <tr v-if="styleCopList.length === 0" class="tab-pane fade show active" id="navStyleCopRule" role="tabpanel" aria-labelledby="navStyleCopRuleTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-12">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <nav>
                <h5>가장 많이 도움이 된 가이드라인</h5>
                <div class="nav nav-tabs" id="navGuidelineTab" role="tablist">
                  <button class="nav-link active" id="navMisraGuidelineCTab" data-bs-toggle="tab" data-bs-target="#navMisraCGuideline" type="button" role="tab" aria-controls="navMisraCGuideline" aria-selected="true">MISRA C</button>
                  <button class="nav-link" id="navMisraCppGuidelineTab" data-bs-toggle="tab" data-bs-target="#navMisraCppGuideline" type="button" role="tab" aria-controls="navMisraCppGuideline" aria-selected="false">MISRA C++</button>
                  <button class="nav-link" id="navCweGuidelineTab" data-bs-toggle="tab" data-bs-target="#navCweGuideline" type="button" role="tab" aria-controls="navCweGuideline" aria-selected="false">CWE C/C++</button>
                  <button class="nav-link" id="navStyleCopGuidelineTab" data-bs-toggle="tab" data-bs-target="#navStyleCopGuideline" type="button" role="tab" aria-controls="navStyleCopGuideline" aria-selected="false">C# Coding Convention
                  </button>
                </div>
              </nav>
            </div>
            <div class="tab-content" id="navTabGuidelineContent">
              <div id="navMisraCGuideline" class="tab-pane fade show active card-body py-1" role="tabpanel" aria-labelledby="navMisraGuidelineCTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(misraCGuideline, i) in misraCGuidelineList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="misraCGuideline.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/misra-c-guideline/read/' + misraCGuideline.idx"> {{ misraCGuideline.title }}</router-link>
                      <span class="comment-count">{{ misraCGuideline.commentDtoCount }}</span>
                      <span v-if="misraCGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ misraCGuideline.likeCountInList }}</span></span>
                      <img v-if="misraCGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ misraCGuideline.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ misraCGuideline.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="misraCGuideline.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="misraCGuideline.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraCGuideline.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraCGuideline.views }}</td>
                  </tr>
                  <tr v-if="misraCGuidelineList.length === 0" class="tab-pane fade show active" id="navMisraCGuideline" role="tabpanel" aria-labelledby="navMisraCGuidelineTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div id="navMisraCppGuideline" class="tab-pane fade card-body py-1" role="tabpanel" aria-labelledby="navMisraCppGuidelineTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(misraCppGuideline, i) in misraCppGuidelineList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="misraCppGuideline.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/misra-cpp-guideline/read/' + misraCppGuideline.idx"> {{ misraCppGuideline.title }}</router-link>
                      <span class="comment-count">{{ misraCppGuideline.commentDtoCount }}</span>
                      <span v-if="misraCppGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ misraCppGuideline.likeCountInList }}</span></span>
                      <img v-if="misraCppGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ misraCppGuideline.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ misraCppGuideline.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="misraCppGuideline.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="misraCppGuideline.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraCppGuideline.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ misraCppGuideline.views }}</td>
                  </tr>
                  <tr v-if="misraCppGuidelineList.length === 0" class="tab-pane fade show active" id="navMisraCppGuideline" role="tabpanel" aria-labelledby="navMisraCppGuidelineTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div id="navCweGuideline" class="tab-pane fade card-body py-1" role="tabpanel" aria-labelledby="navCweGuidelineTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(cweGuideline, i) in cweGuidelineList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="cweGuideline.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/cwe-guideline/read/' + cweGuideline.idx"> {{ cweGuideline.title }}</router-link>
                      <span class="comment-count">{{ cweGuideline.commentDtoCount }}</span>
                      <span v-if="cweGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ cweGuideline.likeCountInList }}</span></span>
                      <img v-if="cweGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ cweGuideline.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ cweGuideline.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="cweGuideline.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="cweGuideline.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ cweGuideline.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ cweGuideline.views }}</td>
                  </tr>
                  <tr v-if="cweGuidelineList.length === 0" class="tab-pane fade show active" id="navCweGuideline" role="tabpanel" aria-labelledby="navCweGuidelineTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div id="navStyleCopGuideline" class="tab-pane fade card-body py-1" role="tabpanel" aria-labelledby="navStyleCopGuidelineTab">
                <table class="table table-hover mobile-table-list">
                  <thead>
                  <tr class="d-none d-lg-table-row">
                    <th style="width: 60%" class="text-center">제목</th>
                    <th style="width: 15%" class="text-center">해시태그</th>
                    <th style="width: 17.5%" class="text-center">작성일</th>
                    <th style="width: 7.5%" class="text-center">조회수</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(styleCopGuideline, i) in styleCopGuidelineList" :key="i">
                    <td>
                      <!-- 공통 -->
                      <img v-if="styleCopGuideline.priority <= 5" :src="require(`@/assets/images/speaker.jpg`)" class="speaker-icon"/>
                      <router-link :to="'/style-cop-guideline/read/' + styleCopGuideline.idx"> {{ styleCopGuideline.title }}</router-link>
                      <span class="comment-count">{{ styleCopGuideline.commentDtoCount }}</span>
                      <span v-if="styleCopGuideline.likeCountInList > 0"><img :src="require(`@/assets/images/red-heart.svg`)" class="like-icon"><span class="like-count">{{ styleCopGuideline.likeCountInList }}</span></span>
                      <img v-if="styleCopGuideline.newIcon" :src="require(`@/assets/images/new_post.svg`)" class="new-icon"/>
                      <!-- Mobile -->
                      <div class="d-inline d-lg-none">
                        <div>
                          <span class="mobile-content">{{ styleCopGuideline.createdDate }}</span> &nbsp;
                          <span class="mobile-content"> 조회수: {{ styleCopGuideline.views }}</span> &nbsp;
                          <span class="mobile-content"><HashTags pageInformation="list" :hash-tags="styleCopGuideline.hashTags"></HashTags></span>
                        </div>
                      </div>
                    </td>
                    <!-- Desktop -->
                    <td class="d-none d-lg-table-cell text-start">
                      <HashTags pageInformation="list" :hash-tags="styleCopGuideline.hashTags"></HashTags>
                    </td>
                    <td class="d-none d-lg-table-cell text-center">{{ styleCopGuideline.createdDate }}</td>
                    <td class="d-none d-lg-table-cell text-center">{{ styleCopGuideline.views }}</td>
                  </tr>
                  <tr v-if="styleCopGuidelineList.length === 0" class="tab-pane fade show active" id="navStyleCopGuideline" role="tabpanel" aria-labelledby="navStyleCopGuidelineTab">
                    <td colspan="10" class="no-posts-table">
                      등록된 게시글이 없습니다.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="loginUserInfo.authorityType === 'ROOT' || loginUserInfo.authorityType === 'MANAGER'" class="row">
        <div class="col-lg-12">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0">로그인 기록</h5>
              <router-link to="/login-history/list">
                <img :src="require(`@/assets/images/more-vertical.svg`)"/>
              </router-link>
            </div>

            <div class="card-body py-1">
              <table class="table table-hover mobile-table-list">
                <thead>
                <tr class="d-none d-lg-table-row">
                  <th style="width: 55%" class="text-center">메시지</th>
                  <th style="width: 17.5%" class="text-center">지역</th>
                  <th style="width: 12.5%" class="text-center">로그인 결과</th>
                  <th style="width: 15%" class="text-center">작성일</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(loginHistory, i) in loginHistoryList" :key="i">
                  <td>
                    <!-- 공통 -->
                    <router-link :to="'/login-history/read/' + loginHistory.idx"> {{ loginHistory.message }}</router-link>
                    <!-- Mobile -->
                    <div class="d-inline d-lg-none">
                      <div>
                        <span class="mobile-content">{{ loginHistory.location }} </span> <br>
                        <span v-if="loginHistory.loginResultType == 'SUCCESS'" class="mobile-content">성공</span>
                        <span v-if="loginHistory.loginResultType == 'FAIL'" class="mobile-content">실패</span> &nbsp;
                        <span class="mobile-content">{{ loginHistory.createdDate }}</span> &nbsp;
                      </div>
                    </div>
                  </td>
                  <!-- Desktop -->
                  <td class="d-none d-lg-table-cell">{{ loginHistory.location }}</td>
                  <td class="d-none d-lg-table-cell text-center">
                    <span v-if="loginHistory.loginResultType == 'SUCCESS'">성공</span>
                    <span v-if="loginHistory.loginResultType == 'FAIL'">실패</span>
                  </td>
                  <td class="d-none d-lg-table-cell text-center">{{ loginHistory.createdDate }}</td>
                </tr>
                </tbody>

                <tr v-if="loginHistoryList.length === 0">
                  <td colspan="10" class="no-posts-table">
                    등록된 게시글이 없습니다.
                  </td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div v-if="loginUserInfo.authorityType === 'ROOT' || loginUserInfo.authorityType === 'MANAGER'" class="row">
        <div class="col-lg-12">
          <div class="card shadow mb-4">
            <div class="card-header py-2 d-flex flex-row align-items-center justify-content-between">
              <h5 class="m-0 font-weight-bold">데이터 기록</h5>
              <div class="dropdown no-arrow">
                <router-link to="/data-history/list">
                  <img :src="require(`@/assets/images/more-vertical.svg`)"/>
                </router-link>
              </div>
            </div>

            <div class="card-body py-1">
              <table class="table table-hover mobile-table-list">
                <thead>
                <tr class="d-none d-lg-table-row">
                  <th style="width: 55%" class="text-center">메시지</th>
                  <th style="width: 17.5%" class="text-center">audit 게시판</th>
                  <th style="width: 12.5%" class="text-center">audit 유형</th>
                  <th style="width: 15%" class="text-center">작성일</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(dataHistory, i) in dataHistoryList" :key="i">
                  <td>
                    <!-- 공통 -->
                    <router-link :to="'/data-history/read/' + dataHistory.idx"> {{ dataHistory.message }}</router-link>
                    <!-- Mobile -->
                    <div class="d-inline d-lg-none">
                      <div>
                        <span class="mobile-content">{{ dataHistory.auditBoard }} </span> <br>
                        <span v-if="dataHistory.auditType == 'SELECT'" class="mobile-content">조회</span>
                        <span v-if="dataHistory.auditType == 'INSERT'" class="mobile-content">등록</span>
                        <span v-if="dataHistory.auditType == 'UPDATE'" class="mobile-content">수정</span>
                        <span v-if="dataHistory.auditType == 'DELETE'" class="mobile-content">삭제</span>
                        <span v-if="dataHistory.auditType == 'LIKE'" class="mobile-content">좋아요</span>
                        <span v-if="dataHistory.auditType == 'CANCEL_LIKE'" class="mobile-content">좋아요 취소</span>&nbsp;
                        <span class="mobile-content">{{ dataHistory.createdDate }}</span> &nbsp;
                      </div>
                    </div>
                  </td>
                  <!-- Desktop -->
                  <td class="d-none d-lg-table-cell">{{ dataHistory.auditBoard }}</td>
                  <td class="d-none d-lg-table-cell text-center">
                    <span v-if="dataHistory.auditType == 'SELECT'">조회</span>
                    <span v-if="dataHistory.auditType == 'INSERT'">등록</span>
                    <span v-if="dataHistory.auditType == 'UPDATE'">수정</span>
                    <span v-if="dataHistory.auditType == 'DELETE'">삭제</span>
                    <span v-if="dataHistory.auditType == 'LIKE'">좋아요</span>
                    <span v-if="dataHistory.auditType == 'CANCEL_LIKE'">좋아요 취소</span>
                  </td>
                  <td class="d-none d-lg-table-cell text-center">{{ dataHistory.createdDate }}</td>
                </tr>

                <tr v-if="dataHistoryList.length === 0">
                  <td colspan="10" class="no-posts-table">
                    등록된 게시글이 없습니다.
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style lang="scss">
.link-wrapper {
  a {
    color: $main-blue !important;
  }
}

.dropdown-toggle::after {
  color: black;
}

.card-count {
  color: $darkest-main-grey;
  text-align: center;
  font-size: $largest-font-size;
}

.memory-name {
  font-weight: $strong-font-weight;
}

.memory-space {
  margin-left: 5px;
  color: $darker-main-grey;
}
</style>

<script>
// components
import Loading from '@/components/common/Loading.vue'
import HashTags from '@/components/common/HashTags.vue'

// vue.js
import {onBeforeMount, ref} from "vue";

import axios from "axios";
// day.js
import dayjs from 'dayjs'
// utils
import {parseApiErrorMsg} from "@/utils/validation-util";
import vueCookies from "vue-cookies";

export default {
  components: {
    Loading,
    HashTags
  },
  setup() {
    // vue.js
    const loginUserInfo = vueCookies.get('loginUserInfo');

    // variable
    let usersCount = ref(0);
    let postsCount = ref(0);
    let loginUsersCountBeforeYesterday = ref(0);
    let loginUsersCountBeforeWeek = ref(0);
    let loginUsersCountBeforeMonth = ref(0);
    let postsCountBeforeYesterday = ref(0);
    let postsCountBeforeWeek = ref(0);
    let postsCountBeforeMonth = ref(0);
    let misraCPostsCount = ref(0);
    let misraCppPostsCount = ref(0);
    let cwePostsCount = ref(0);
    let javaCodeConventionsPostsCount = ref(0);
    let cweJavaPostsCount = ref(0);
    let styleCopPostsCount = ref(0);
    let fxCopPostsCount = ref(0);
    let misraCGuidelinePostsCount = ref(0);
    let misraCppGuidelinePostsCount = ref(0);
    let cweGuidelinePostsCount = ref(0);
    let javaCodeConventionsGuidelinePostsCount = ref(0);
    let cweJavaGuidelinePostsCount = ref(0);
    let styleCopGuidelinePostsCount = ref(0);
    let fxCopGuidelinePostsCount = ref(0);

    let misraCList = ref([]);
    let misraCppList = ref([]);
    let cweList = ref([]);
    let styleCopList = ref([]);
    let misraCGuidelineList = ref([]);
    let misraCppGuidelineList = ref([]);
    let cweGuidelineList = ref([]);
    let styleCopGuidelineList = ref([]);

    let initialReleaseDate = ref();
    let lastReleaseDate = ref();
    let setting = ref([]);
    let diskUtilList = ref([]);
    let noticeList = ref([]);
    let loginHistoryList = ref([]);
    let dataHistoryList = ref([]);

    let registeredMisraCPostsRatio = ref(0);
    let registeredMisraCppPostsRatio = ref(0);
    let registeredCwePostsRatio = ref(0);
    let registeredJavaCodeConventionsPostsRatio = ref(0);
    let registeredCweJavaPostsRatio = ref(0);
    let registeredStyleCopPostsRatio = ref(0);
    let registeredFxCopPostsRatio = ref(0);


    // onBeforeMount, init
    onBeforeMount(async () => {
      await axios.get(process.env.VUE_APP_MODULE_APP_API_URL + "/api/dashboards",
          {},
      )
          .then((response) => {
            usersCount.value = response.data.usersCount;
            postsCount.value = response.data.postsCount;
            loginUsersCountBeforeYesterday.value = response.data.loginUsersCountBeforeYesterday;
            loginUsersCountBeforeWeek.value = response.data.loginUsersCountBeforeWeek;
            loginUsersCountBeforeMonth.value = response.data.loginUsersCountBeforeMonth;
            postsCountBeforeYesterday.value = response.data.postsCountBeforeYesterday;
            postsCountBeforeWeek.value = response.data.postsCountBeforeWeek;
            postsCountBeforeMonth.value = response.data.postsCountBeforeMonth;
            misraCPostsCount.value = response.data.misraCPostsCount;
            misraCppPostsCount.value = response.data.misraCppPostsCount;
            cwePostsCount.value = response.data.cwePostsCount;
            javaCodeConventionsPostsCount.value = response.data.javaCodeConventionsPostsCount;
            cweJavaPostsCount.value = response.data.cweJavaPostsCount;
            styleCopPostsCount.value = response.data.styleCopPostsCount;
            fxCopPostsCount.value = response.data.fxCopPostsCount;
            misraCGuidelinePostsCount.value = response.data.misraCGuidelinePostsCount;
            misraCppGuidelinePostsCount.value = response.data.misraCppGuidelinePostsCount;
            cweGuidelinePostsCount.value = response.data.cweGuidelinePostsCount;
            javaCodeConventionsGuidelinePostsCount.value = response.data.javaCodeConventionsGuidelinePostsCount;
            cweJavaGuidelinePostsCount.value = response.data.cweJavaGuidelinePostsCount;
            styleCopGuidelinePostsCount.value = response.data.styleCopGuidelinePostsCount;
            fxCopGuidelinePostsCount.value = response.data.fxCopGuidelinePostsCount;

            misraCList.value = response.data.misraCDtoList;
            misraCppList.value = response.data.misraCppDtoList;
            cweList.value = response.data.cweDtoList;
            styleCopList.value = response.data.styleCopDtoList;
            misraCGuidelineList.value = response.data.misraCGuidelineDtoList;
            misraCppGuidelineList.value = response.data.misraCppGuidelineDtoList;
            cweGuidelineList.value = response.data.cweGuidelineDtoList;
            styleCopGuidelineList.value = response.data.styleCopGuidelineDtoList;

            initialReleaseDate.value = dayjs(response.data.settingDto.initialReleaseDate).format("YYYY.MM.DD.");
            lastReleaseDate.value = dayjs(response.data.settingDto.lastReleaseDate).format("YYYY.MM.DD.");
            setting.value = response.data.settingDto;
            diskUtilList.value = response.data.diskUtilList;
            noticeList.value = response.data.noticeDtoList;
            loginHistoryList.value = response.data.loginHistoryDtoList;
            dataHistoryList.value = response.data.dataHistoryDtoList;

            // 정적시험 규칙 비율 계산
            registeredMisraCPostsRatio.value = (misraCPostsCount.value / setting.value.totalMisraCRuleNumber * 100).toFixed(1);
            registeredMisraCppPostsRatio.value = (misraCppPostsCount.value / setting.value.totalMisraCppRuleNumber * 100).toFixed(1);
            registeredCwePostsRatio.value = (cwePostsCount.value / setting.value.totalCweRuleNumber * 100).toFixed(1);
            registeredJavaCodeConventionsPostsRatio.value = (javaCodeConventionsPostsCount.value / setting.value.totalJavaCodeConventionsRuleNumber * 100).toFixed(1);
            registeredCweJavaPostsRatio.value = (cweJavaPostsCount.value / setting.value.totalCweJavaRuleNumber * 100).toFixed(1);
            registeredStyleCopPostsRatio.value = (styleCopPostsCount.value / setting.value.totalStyleCopRuleNumber * 100).toFixed(1);
            registeredFxCopPostsRatio.value = (fxCopPostsCount.value / setting.value.totalFxCopRuleNumber * 100).toFixed(1);

            document.getElementById("registeredMisraCPostsRatio").style.width = registeredMisraCPostsRatio.value + "%";
            document.getElementById("registeredMisraCppPostsRatio").style.width = registeredMisraCppPostsRatio.value + "%";
            document.getElementById("registeredCwePostsRatio").style.width = registeredCwePostsRatio.value + "%";
            document.getElementById("registeredJavaCodeConventionsPostsRatio").style.width = registeredJavaCodeConventionsPostsRatio.value + "%";
            document.getElementById("registeredCweJavaPostsRatio").style.width = registeredCweJavaPostsRatio.value + "%";
            document.getElementById("registeredStyleCopPostsRatio").style.width = registeredStyleCopPostsRatio.value + "%";
            document.getElementById("registeredFxCopPostsRatio").style.width = registeredFxCopPostsRatio.value + "%";

            // dayjs
            for (const notice of noticeList.value) {
              notice.createdDate = dayjs(notice.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const misraC of misraCList.value) {
              misraC.createdDate = dayjs(misraC.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const misraCpp of misraCppList.value) {
              misraCpp.createdDate = dayjs(misraCpp.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const cwe of cweList.value) {
              cwe.createdDate = dayjs(cwe.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const styleCop of styleCopList.value) {
              styleCop.createdDate = dayjs(styleCop.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const misraCGuideline of misraCGuidelineList.value) {
              misraCGuideline.createdDate = dayjs(misraCGuideline.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const misraCppGuideline of misraCppGuidelineList.value) {
              misraCppGuideline.createdDate = dayjs(misraCppGuideline.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const cweGuideline of cweGuidelineList.value) {
              cweGuideline.createdDate = dayjs(cweGuideline.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const styleCopGuideline of styleCopGuidelineList.value) {
              styleCopGuideline.createdDate = dayjs(styleCopGuideline.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const loginHistory of loginHistoryList.value) {
              loginHistory.createdDate = dayjs(loginHistory.createdDate).format("YYYY.MM.DD.");
            }

            // dayjs
            for (const dataHistory of dataHistoryList.value) {
              dataHistory.createdDate = dayjs(dataHistory.createdDate).format("YYYY.MM.DD.");
            }
          })
          .catch((error) => {
            parseApiErrorMsg(error.response);
          })
          .then(() => {
          });
    })

    /* 통계 dropdown 변경 */
    const changeStatics = (e) => {
      switch (e.target.textContent) {
        case "하루 동안 접속한 회원 수":
          document.getElementById("staticsCount").innerText = loginUsersCountBeforeYesterday.value;
          document.getElementById("dropdownStaticsButton").innerText = "하루 동안 접속한 회원 수";
          break;
        case "7일간 접속한 회원 수":
          document.getElementById("staticsCount").innerText = loginUsersCountBeforeWeek.value;
          document.getElementById("dropdownStaticsButton").innerText = "7일간 접속한 회원 수";
          break;
        case"28일간 접속한 회원 수":
          document.getElementById("staticsCount").innerText = loginUsersCountBeforeMonth.value;
          document.getElementById("dropdownStaticsButton").innerText = "28일간 접속한 회원 수";
          break;
        case"하루 동안 등록된 게시글 수":
          document.getElementById("staticsCount").innerText = postsCountBeforeYesterday.value;
          document.getElementById("dropdownStaticsButton").innerText = "하루 동안 등록된 게시글 수";
          break;
        case"7일간 등록된 게시글 수":
          document.getElementById("staticsCount").innerText = postsCountBeforeWeek.value;
          document.getElementById("dropdownStaticsButton").innerText = "7일간 등록된 게시글 수";
          break;
        case"28일간 등록된 게시글 수":
          document.getElementById("staticsCount").innerText = postsCountBeforeMonth.value;
          document.getElementById("dropdownStaticsButton").innerText = "28일간 등록된 게시글 수";
          break;
      }
    };

    return {
      // variable
      loginUserInfo,

      usersCount,
      postsCount,
      loginUsersCountBeforeYesterday,
      loginUsersCountBeforeWeek,
      loginUsersCountBeforeMonth,
      postsCountBeforeYesterday,
      postsCountBeforeWeek,
      postsCountBeforeMonth,
      misraCPostsCount,
      misraCppPostsCount,
      cwePostsCount,
      javaCodeConventionsPostsCount,
      cweJavaPostsCount,
      styleCopPostsCount,
      fxCopPostsCount,
      misraCGuidelinePostsCount,
      misraCppGuidelinePostsCount,
      cweGuidelinePostsCount,
      javaCodeConventionsGuidelinePostsCount,
      cweJavaGuidelinePostsCount,
      styleCopGuidelinePostsCount,
      fxCopGuidelinePostsCount,

      misraCList,
      misraCppList,
      cweList,
      styleCopList,
      misraCGuidelineList,
      misraCppGuidelineList,
      cweGuidelineList,
      styleCopGuidelineList,

      initialReleaseDate,
      lastReleaseDate,
      setting,
      diskUtilList,
      noticeList,
      loginHistoryList,
      dataHistoryList,
      registeredMisraCPostsRatio,
      registeredMisraCppPostsRatio,
      registeredCwePostsRatio,
      registeredJavaCodeConventionsPostsRatio,
      registeredCweJavaPostsRatio,
      registeredStyleCopPostsRatio,
      registeredFxCopPostsRatio,

      // function
      changeStatics
    }
  }
}
</script>

