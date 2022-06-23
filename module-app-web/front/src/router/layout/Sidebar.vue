<!-- https://getbootstrap.com/docs/5.0/examples/sidebars/ -->
<template>
  <aside id="sidebar" class="flex-shrink-0 justify-content-start p-3 d-none d-sm-none d-md-none d-lg-flex">
    <div style="width: 100%">
      <ul class="list-unstyled ps-0">
        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#misra-c-collapse" aria-expanded="false">
            MISRA C 정리
          </button>
          <div class="collapse" id="misra-c-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/misra-c/list" class="link-dark rounded">MISRA C 규칙</router-link>
              </li>
              <li>
                <router-link to="/misra-c-example/list" class="link-dark rounded">MISRA C 예제 코드</router-link>
              </li>
              <li>
                <router-link to="/misra-c-guideline/list" class="link-dark rounded">MISRA C 가이드라인 사례</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#misra-cpp-collapse" aria-controls="misra-cpp-collapse" aria-expanded="false">
            MISRA C++ 정리
          </button>
          <div class="collapse" id="misra-cpp-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/misra-cpp/list" class="link-dark rounded">MISRA C++ 규칙</router-link>
              </li>
              <li>
                <router-link to="/misra-cpp-example/list" class="link-dark rounded">MISRA C++ 예제 코드</router-link>
              </li>
              <li>
                <router-link to="/misra-cpp-guideline/list" class="link-dark rounded">MISRA C++ 가이드라인 사례</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#cwe-collapse" aria-expanded="false">
            CWE 정리
          </button>
          <div class="collapse" id="cwe-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/cwe/list" class="link-dark rounded">CWE 규칙</router-link>
              </li>
              <li>
                <router-link to="/cwe-example/list" class="link-dark rounded">CWE 예제 코드</router-link>
              </li>
              <li>
                <router-link to="/cwe-guideline/list" class="link-dark rounded">CWE 가이드라인 사례</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#fx-cop-collapse" aria-expanded="false">
            FxCop 정리
          </button>
          <div class="collapse" id="fx-cop-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/fx-cop/list" class="link-dark rounded">FxCop 규칙</router-link>
              </li>
              <li>
                <router-link to="/fx-cop-example/list" class="link-dark rounded">FxCop 예제 코드</router-link>
              </li>
              <li>
                <router-link to="/fx-cop-guideline/list" class="link-dark rounded">FxCop 가이드라인 사례</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#style-cop-collapse" aria-expanded="false">
            StyleCop 정리
          </button>
          <div class="collapse" id="style-cop-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/style-cop/list" class="link-dark rounded">StyleCop 규칙</router-link>
              </li>
              <li>
                <router-link to="/style-cop-example/list" class="link-dark rounded">StyleCop 예제 코드</router-link>
              </li>
              <li>
                <router-link to="/style-cop-guideline/list" class="link-dark rounded">StyleCop 가이드라인 사례</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#metric-collapse" aria-expanded="false">
            메트릭 정리
          </button>
          <div class="collapse" id="metric-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/metric/list" class="link-dark rounded">메트릭 규칙</router-link>
              </li>
              <li>
                <router-link to="/metric-example/list" class="link-dark rounded">메트릭 예제 코드</router-link>
              </li>
              <li>
                <router-link to="/metric-guideline/list" class="link-dark rounded">메트릭 가이드라인 사례</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#troubleShooting" aria-expanded="false">
            도구 트러블 슈팅
          </button>
          <div class="collapse" id="troubleShooting">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/static-tool/list" class="link-dark rounded">STATIC</router-link>
              </li>
              <li>
                <router-link to="/cover-tool/list" class="link-dark rounded">COVER</router-link>
              </li>
              <li>
                <router-link to="/controller-tester-tool/list" class="link-dark rounded">Controller Tester</router-link>
              </li>
              <li>
                <router-link to="/toolchain/list" class="link-dark rounded">툴체인</router-link>
              </li>
            </ul>
          </div>
        </li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center">
            <router-link to="/storage/list">
              지식 저장소
            </router-link>
          </button>
        </li>

        <li class="border-top my-3"></li>

        <li class="mb-1">
          <button class="btn btn-toggle align-items-center rounded collapsed me-1" data-bs-toggle="collapse" data-bs-target="#account-collapse" aria-expanded="false">
            관리자 페이지
          </button>
          <div class="collapse" id="account-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/notice/list" class="link-dark rounded">공지사항</router-link>
              </li>
            </ul>
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/user/list" class="link-dark rounded">사용자</router-link>
              </li>
            </ul>
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/login-history/list" class="link-dark rounded">로그인 기록</router-link>
              </li>
            </ul>
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/data-history/list" class="link-dark rounded">데이터 기록</router-link>
              </li>
            </ul>
            <ul v-if="loginUserInfo.username === 'root'" class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <router-link to="/setting/update" class="link-dark rounded">설정</router-link>
              </li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
  </aside>
</template>

<style lang="scss">
#sidebar {
  width: 275px;
  background-color: $light-main-black;

  .btn-toggle {
    display: inline-flex;
    padding: .4rem .5rem;
    font-size: $larger-font-size;
    color: white;
    background-color: transparent;
    border: 0;
    width: 100%;
  }

  .btn-toggle a {
    color: white;
  }

  .btn-toggle:hover,
  .btn-toggle:focus {
    color: $darkest-main-white;
    background-color: $lighter-main-grey;
    font-weight: $strong-font-weight;
  }

  .btn-toggle::before {
    width: 1.25em;
    line-height: 0;
    content: url("~@/assets/images/chevron-right-white.svg");
    transition: transform .35s ease;
    transform-origin: .5em 50%;
  }

  .btn-toggle[aria-expanded="true"] {
    color: $dark-main-white;
  }

  .btn-toggle[aria-expanded="true"]::before {
    transform: rotate(90deg);
  }

  .btn-toggle-nav a {
    display: inline-flex;
    padding: .1875rem .5rem;
    margin-top: .125rem;
    margin-left: 1.25rem;
    color: $darker-custom-white;
    font-size: $large-font-size;
    width: 90%;
  }

  .btn-toggle-nav a:hover,
  .btn-toggle-nav a:focus {
    color: $darker-main-white;
    background-color: $light-main-grey;
    font-weight: $strong-font-weight;
  }

  .scrollarea {
    overflow-y: auto;
  }

  .fw-semibold {
    font-weight: 600;
  }

  .lh-tight {
    line-height: 1.25;
  }
}
</style>

<script>
// vue.js
import {useRouter} from "vue-router";

// utils
import vueCookies from "vue-cookies";

export default {
  setup() {
    // vue.js
    const router = useRouter();
    const loginUserInfo = vueCookies.get('loginUserInfo');

    return {
      loginUserInfo
    }
  }
}
</script>