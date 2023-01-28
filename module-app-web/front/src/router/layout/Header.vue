<!-- https://getbootstrap.com/docs/5.0/examples/headers/ -->
<template>
  <header id="header" class="p-1 border-bottom d-flex align-items-center">
    <div class="container-fluid d-flex justify-content-between ">
      <div class="d-flex align-items-center">
        <div class="logo-wrapper border-end d-flex">
          <router-link to="/dashboard"><img :src="require(`@/assets/images/logo.svg`)" class="logo"/></router-link>
        </div>
        <div class="d-flex align-items-center ms-1">
          <div id="showSidebar" class="icon-wrapper ms-2 d-sm-flex d-md-none">
            <img @click="showSidebar()" :src="require(`@/assets/images/chevron-right-main-blue.svg`)" class="cursor-pointer" style="height: 24px;">
          </div>
          <div id="hideSidebar" class="icon-wrapper ms-2 d-none d-md-flex">
            <img @click="hideSidebar()" :src="require(`@/assets/images/chevron-left-main-blue.svg`)" class="cursor-pointer ">
          </div>
        </div>
      </div>

      <div class="d-flex align-items-center">
        <span class="login-user-info d-none d-lg-flex">{{ loginUserInfo.department }} {{ loginUserInfo.name }}, {{ loginUserInfo.username }}</span>
        <div class="dropdown-wrapper">
            <span class="dropdown">
              <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                <img :src="require(`@/assets/images/user-white.svg`)" class="cursor-pointer">
              </a>
              <ul class="dropdown-menu" aria-labelledby="dropdownUser1" style="color: white">
                <li><router-link :to="'/user/read/' + loginUserInfo.idx" class="dropdown-item">프로필</router-link></li>
                <li>
                  <hr class="dropdown-divider">
                </li>
                <li><span class="dropdown-item" @click="deAuthenticate()">로그아웃</span></li>
              </ul>
            </span>
        </div>
      </div>
    </div>
  </header>
</template>

<style lang="scss">
#header {
  background-color: $dark-main-white;
  height: 55px;

  // fixed header
  position: fixed;
  z-index: 1000;
  top: 0px;
  left: 0;
  width: 100%;
}

.logo-wrapper {
  width: 200px;

  .logo {
    height: 40px;
  }
}

.icon-wrapper {
  padding: 3px;
  border: 0px;
  border-radius: 50%;
  box-shadow: rgba(60, 64, 67, 0.1) 0px 1px 2px 0px, rgba(60, 64, 67, 0.25) 0px 2px 6px 2px;
}

.login-user-info {
  color: $darker-custom-grey;
  margin-right: 7px;
}

.dropdown-wrapper {
  background-color: $dark-custom-grey;
  padding: 5px;
  border-radius: 25%;

  .dropdown-toggle {
    outline: 0;
  }

  .dropdown-toggle::after {
    color: white;
  }
}
</style>

<script>
// vue.js
import {useRouter} from "vue-router";

// utils
import vueCookies from "vue-cookies";
import {confirm} from "@/assets/plugins/sweetalert2/sweetalert2";

export default {
  setup() {
    // vue.js
    const router = useRouter();
    const loginUserInfo = vueCookies.get('loginUserInfo');

    const showSidebar = () => {
      document.getElementById("sidebar").setAttribute("style", "display: flex !important");
      document.getElementById("showSidebar").setAttribute("style", "display: none !important");
      document.getElementById("hideSidebar").setAttribute("style", "display: flex !important");
      document.getElementsByTagName("main").item(0).setAttribute("style", "margin-left: 275px !important");
    }

    const hideSidebar = () => {
      document.getElementById("sidebar").setAttribute("style", "display: none !important");
      document.getElementById("showSidebar").setAttribute("style", "display: flex !important");
      document.getElementById("hideSidebar").setAttribute("style", "display: none !important");
      document.getElementsByTagName("main").item(0).setAttribute("style", "margin-left: 0px !important");
    }

    const deAuthenticate = () => {
      confirm.fire({
        title: "로그아웃 하시겠습니까?",
        confirmButtonText: '네',
        cancelButtonText: '아니오'
      }).then((result) => {
        if (result.isConfirmed) {
          vueCookies.remove('isHasToken');
          vueCookies.remove('loginUserInfo');

          router.push("/login");
        } else {
          return false;
        }
      })
    };

    return {
      loginUserInfo,

      // function
      showSidebar, hideSidebar, deAuthenticate
    }
  }
}
</script>
