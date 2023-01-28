import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import axios from 'axios'

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';

// axios 설정
axios.defaults.xsrfCookieName = 'XSRF-TOKEN' // csrf 기본 설정을 명시적으로 선언
axios.defaults.xsrfHeaderName = 'X-XSRF-TOKEN' // csrf 기본 설정을 명시적으로 선언
axios.defaults.withCredentials = true; // 다른 origin에 JWT를 전달하기 위한  설정
// axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*'; // cors 설정
// axios.defaults.headers.common['Access-Control-Allow-Headers'] = 'Origin, Content-Type, X-Auth-Token'; // cors 설정

// 요청 인터셉터 추가
axios.interceptors.request.use(
    (config) => {
        // 요청을 보내기 전에 수행할 일
        document.getElementById("loading-wrapper").style.visibility = "visible";
        return config;
    },
    (error) => {
        // 오류 요청을 보내기전 수행할 일
        document.getElementById("loading-wrapper").style.visibility = "hidden";
        return Promise.reject(error);
    }
);

// 응답 인터셉터 추가
axios.interceptors.response.use(
    (response) => {
        document.getElementById("loading-wrapper").style.visibility = "hidden";
        // 응답 데이터를 가공
        return response;
    },
    (error) => {
        // 오류 응답을 처리
        document.getElementById("loading-wrapper").style.visibility = "hidden";
        return Promise.reject(error);
    }
);

const app = createApp(App);

app.use(store);
app.use(router, axios);

// plugins
import {toast} from '@/assets/plugins/sweetalert2/sweetalert2'

// vue 전역 에러 처리
app.config.errorHandler = (err, instance, info) => {
    console.error("error!")
    console.error(err)

    toast.fire({
        icon: 'error',
        title: '오류가 발생하였습니다. 관리자에게 문의하세요.',
    });
}

app.mount('#app');
