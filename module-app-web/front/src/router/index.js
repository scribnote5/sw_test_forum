// vue.js
import {nextTick} from 'vue'
import {createRouter, createWebHistory} from 'vue-router'
// sweetalert2
import {error, warning} from '@/assets/plugins/sweetalert2/sweetalert2'
// vueCookies
import vueCookies from 'vue-cookies';

// component
import Login from '../components/login/Login.vue'
import Dashboard from "../components/dashboard/Dashboard.vue";

import MisraCList from '../components/misra_c/misra_c/MisraCList.vue'
import MisraCWrite from '../components/misra_c/misra_c/MisraCWrite.vue'
import MisraCUpdate from '../components/misra_c/misra_c/MisraCUpdate.vue'
import MisraCRead from '../components/misra_c/misra_c/MisraCRead.vue'

import MisraCExampleList from '../components/misra_c/misra_c_example/MisraCExampleList.vue'
import MisraCExampleWrite from '../components/misra_c/misra_c_example/MisraCExampleWrite.vue'
import MisraCExampleUpdate from '../components/misra_c/misra_c_example/MisraCExampleUpdate.vue'
import MisraCExampleRead from '../components/misra_c/misra_c_example/MisraCExampleRead.vue'

import MisraCGuidelineList from '../components/misra_c/misra_c_guideline/MisraCGuidelineList.vue'
import MisraCGuidelineWrite from '../components/misra_c/misra_c_guideline/MisraCGuidelineWrite.vue'
import MisraCGuidelineUpdate from '../components/misra_c/misra_c_guideline/MisraCGuidelineUpdate.vue'
import MisraCGuidelineRead from '../components/misra_c/misra_c_guideline/MisraCGuidelineRead.vue'

import MisraCppList from '../components/misra_cpp/misra_cpp/MisraCppList.vue'
import MisraCppWrite from '../components/misra_cpp/misra_cpp/MisraCppWrite.vue'
import MisraCppUpdate from '../components/misra_cpp/misra_cpp/MisraCppUpdate.vue'
import MisraCppRead from '../components/misra_cpp/misra_cpp/MisraCppRead.vue'

import MisraCppExampleList from '../components/misra_cpp/misra_cpp_example/MisraCppExampleList.vue'
import MisraCppExampleWrite from '../components/misra_cpp/misra_cpp_example/MisraCppExampleWrite.vue'
import MisraCppExampleUpdate from '../components/misra_cpp/misra_cpp_example/MisraCppExampleUpdate.vue'
import MisraCppExampleRead from '../components/misra_cpp/misra_cpp_example/MisraCppExampleRead.vue'

import MisraCppGuidelineList from '../components/misra_cpp/misra_cpp_guideline/MisraCppGuidelineList.vue'
import MisraCppGuidelineWrite from '../components/misra_cpp/misra_cpp_guideline/MisraCppGuidelineWrite.vue'
import MisraCppGuidelineUpdate from '../components/misra_cpp/misra_cpp_guideline/MisraCppGuidelineUpdate.vue'
import MisraCppGuidelineRead from '../components/misra_cpp/misra_cpp_guideline/MisraCppGuidelineRead.vue'

import JavaCodeConventionsList from '../components/java_code_conventions/java_code_conventions/JavaCodeConventionsList.vue'
import JavaCodeConventionsWrite from '../components/java_code_conventions/java_code_conventions/JavaCodeConventionsWrite.vue'
import JavaCodeConventionsUpdate from '../components/java_code_conventions/java_code_conventions/JavaCodeConventionsUpdate.vue'
import JavaCodeConventionsRead from '../components/java_code_conventions/java_code_conventions/JavaCodeConventionsRead.vue'

import JavaCodeConventionsExampleList from '../components/java_code_conventions/java_code_conventions_example/JavaCodeConventionsExampleList.vue'
import JavaCodeConventionsExampleWrite from '../components/java_code_conventions/java_code_conventions_example/JavaCodeConventionsExampleWrite.vue'
import JavaCodeConventionsExampleUpdate from '../components/java_code_conventions/java_code_conventions_example/JavaCodeConventionsExampleUpdate.vue'
import JavaCodeConventionsExampleRead from '../components/java_code_conventions/java_code_conventions_example/JavaCodeConventionsExampleRead.vue'

import JavaCodeConventionsGuidelineList from '../components/java_code_conventions/java_code_conventions_guideline/JavaCodeConventionsGuidelineList.vue'
import JavaCodeConventionsGuidelineWrite from '../components/java_code_conventions/java_code_conventions_guideline/JavaCodeConventionsGuidelineWrite.vue'
import JavaCodeConventionsGuidelineUpdate from '../components/java_code_conventions/java_code_conventions_guideline/JavaCodeConventionsGuidelineUpdate.vue'
import JavaCodeConventionsGuidelineRead from '../components/java_code_conventions/java_code_conventions_guideline/JavaCodeConventionsGuidelineRead.vue'

import CweList from '../components/cwe/cwe/CweList.vue'
import CweWrite from '../components/cwe/cwe/CweWrite.vue'
import CweUpdate from '../components/cwe/cwe/CweUpdate.vue'
import CweRead from '../components/cwe/cwe/CweRead.vue'

import CweExampleList from '../components/cwe/cwe_example/CweExampleList.vue'
import CweExampleWrite from '../components/cwe/cwe_example/CweExampleWrite.vue'
import CweExampleUpdate from '../components/cwe/cwe_example/CweExampleUpdate.vue'
import CweExampleRead from '../components/cwe/cwe_example/CweExampleRead.vue'

import CweGuidelineList from '../components/cwe/cwe_guideline/CweGuidelineList.vue'
import CweGuidelineWrite from '../components/cwe/cwe_guideline/CweGuidelineWrite.vue'
import CweGuidelineUpdate from '../components/cwe/cwe_guideline/CweGuidelineUpdate.vue'
import CweGuidelineRead from '../components/cwe/cwe_guideline/CweGuidelineRead.vue'

import StyleCopList from '../components/style_cop/style_cop/StyleCopList.vue'
import StyleCopWrite from '../components/style_cop/style_cop/StyleCopWrite.vue'
import StyleCopUpdate from '../components/style_cop/style_cop/StyleCopUpdate.vue'
import StyleCopRead from '../components/style_cop/style_cop/StyleCopRead.vue'

import StyleCopExampleList from '../components/style_cop/style_cop_example/StyleCopExampleList.vue'
import StyleCopExampleWrite from '../components/style_cop/style_cop_example/StyleCopExampleWrite.vue'
import StyleCopExampleUpdate from '../components/style_cop/style_cop_example/StyleCopExampleUpdate.vue'
import StyleCopExampleRead from '../components/style_cop/style_cop_example/StyleCopExampleRead.vue'

import StyleCopGuidelineList from '../components/style_cop/style_cop_guideline/StyleCopGuidelineList.vue'
import StyleCopGuidelineWrite from '../components/style_cop/style_cop_guideline/StyleCopGuidelineWrite.vue'
import StyleCopGuidelineUpdate from '../components/style_cop/style_cop_guideline/StyleCopGuidelineUpdate.vue'
import StyleCopGuidelineRead from '../components/style_cop/style_cop_guideline/StyleCopGuidelineRead.vue'

import FxCopList from '../components/fx_cop/fx_cop/FxCopList.vue'
import FxCopWrite from '../components/fx_cop/fx_cop/FxCopWrite.vue'
import FxCopUpdate from '../components/fx_cop/fx_cop/FxCopUpdate.vue'
import FxCopRead from '../components/fx_cop/fx_cop/FxCopRead.vue'

import FxCopExampleList from '../components/fx_cop/fx_cop_example/FxCopExampleList.vue'
import FxCopExampleWrite from '../components/fx_cop/fx_cop_example/FxCopExampleWrite.vue'
import FxCopExampleUpdate from '../components/fx_cop/fx_cop_example/FxCopExampleUpdate.vue'
import FxCopExampleRead from '../components/fx_cop/fx_cop_example/FxCopExampleRead.vue'

import FxCopGuidelineList from '../components/fx_cop/fx_cop_guideline/FxCopGuidelineList.vue'
import FxCopGuidelineWrite from '../components/fx_cop/fx_cop_guideline/FxCopGuidelineWrite.vue'
import FxCopGuidelineUpdate from '../components/fx_cop/fx_cop_guideline/FxCopGuidelineUpdate.vue'
import FxCopGuidelineRead from '../components/fx_cop/fx_cop_guideline/FxCopGuidelineRead.vue'

import MetricList from '../components/metric/metric/MetricList.vue'
import MetricWrite from '../components/metric/metric/MetricWrite.vue'
import MetricUpdate from '../components/metric/metric/MetricUpdate.vue'
import MetricRead from '../components/metric/metric/MetricRead.vue'

import MetricExampleList from '../components/metric/metric_example/MetricExampleList.vue'
import MetricExampleWrite from '../components/metric/metric_example/MetricExampleWrite.vue'
import MetricExampleUpdate from '../components/metric/metric_example/MetricExampleUpdate.vue'
import MetricExampleRead from '../components/metric/metric_example/MetricExampleRead.vue'

import MetricGuidelineList from '../components/metric/metric_guideline/MetricGuidelineList.vue'
import MetricGuidelineWrite from '../components/metric/metric_guideline/MetricGuidelineWrite.vue'
import MetricGuidelineUpdate from '../components/metric/metric_guideline/MetricGuidelineUpdate.vue'
import MetricGuidelineRead from '../components/metric/metric_guideline/MetricGuidelineRead.vue'

import StaticToolList from '../components/trouble_shooting/static_tool/StaticToolList.vue'
import StaticToolWrite from '../components/trouble_shooting/static_tool/StaticToolWrite.vue'
import StaticToolUpdate from '../components/trouble_shooting/static_tool/StaticToolUpdate.vue'
import StaticToolRead from '../components/trouble_shooting/static_tool/StaticToolRead.vue'

import CoverToolList from '../components/trouble_shooting/cover_tool/CoverToolList.vue'
import CoverToolWrite from '../components/trouble_shooting/cover_tool/CoverToolWrite.vue'
import CoverToolUpdate from '../components/trouble_shooting/cover_tool/CoverToolUpdate.vue'
import CoverToolRead from '../components/trouble_shooting/cover_tool/CoverToolRead.vue'

import ControllerTesterToolList from '../components/trouble_shooting/controller_tester_tool/ControllerTesterToolList.vue'
import ControllerTesterToolWrite from '../components/trouble_shooting/controller_tester_tool/ControllerTesterToolWrite.vue'
import ControllerTesterToolUpdate from '../components/trouble_shooting/controller_tester_tool/ControllerTesterToolUpdate.vue'
import ControllerTesterToolRead from '../components/trouble_shooting/controller_tester_tool/ControllerTesterToolRead.vue'

import ToolchainList from '../components/trouble_shooting/toolchain/ToolchainList.vue'
import ToolchainWrite from '../components/trouble_shooting/toolchain/ToolchainWrite.vue'
import ToolchainUpdate from '../components/trouble_shooting/toolchain/ToolchainUpdate.vue'
import ToolchainRead from '../components/trouble_shooting/toolchain/ToolchainRead.vue'

import NoticeList from '../components/admin_page/notice/NoticeList.vue'
import NoticeWrite from '../components/admin_page/notice/NoticeWrite.vue'
import NoticeUpdate from '../components/admin_page/notice/NoticeUpdate.vue'
import NoticeRead from '../components/admin_page/notice/NoticeRead.vue'

import UserList from '../components/admin_page/user/UserList.vue'
import UserWrite from '../components/admin_page/user/UserWrite.vue'
import UserUpdate from '../components/admin_page/user/UserUpdate.vue'
import UserRead from '../components/admin_page/user/UserRead.vue'

import LoginHistoryList from '../components/admin_page/login_history/LoginHistoryList.vue'
import LoginHistoryRead from '../components/admin_page/login_history/LoginHistoryRead.vue'

import DataHistoryList from '../components/admin_page/data_history/DataHistoryList.vue'
import DataHistoryRead from '../components/admin_page/data_history/DataHistoryRead.vue'

import StorageList from '../components/storage/StorageList.vue'
import StorageWrite from '../components/storage/StorageWrite.vue'
import StorageUpdate from '../components/storage/StorageUpdate.vue'
import StorageRead from '../components/storage/StorageRead.vue'

import SettingUpdate from '../components/admin_page/setting/SettingUpdate.vue'

import {isEmpty} from "@/utils/empty-util";

const routes = [
    {
        path: '/',
        redirect: {name: 'Login'}
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {title: 'SW Test Forum - 로그인', layoutView: false, authRequired: false}
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: {title: 'SW Test Forum - 대시보드', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c/list',
        name: 'MisraCList',
        component: MisraCList,
        meta: {title: 'SW Test Forum - MISRA C 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c/write',
        name: 'MisraCWrite',
        component: MisraCWrite,
        meta: {title: 'SW Test Forum - MISRA C 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c/read/:idx',
        name: 'MisraCRead',
        component: MisraCRead,
        meta: {title: 'SW Test Forum - MISRA C 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c/update/:idx',
        name: 'MisraCUpdate',
        component: MisraCUpdate,
        meta: {title: 'SW Test Forum - MISRA C 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-example/list',
        name: 'MisraCExampleList',
        component: MisraCExampleList,
        meta: {title: 'SW Test Forum - MISRA C 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-example/list/:misraCIdx',
        name: 'MisraCExampleListByMisraCIdx',
        component: MisraCExampleList,
        meta: {title: 'SW Test Forum - MISRA C 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-example/write/:misraCIdx',
        name: 'MisraCExampleWrite',
        component: MisraCExampleWrite,
        meta: {title: 'SW Test Forum - MISRA C 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-example/read/:idx',
        name: 'MisraCExampleRead',
        component: MisraCExampleRead,
        meta: {title: 'SW Test Forum - MISRA C 예제 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-example/read/from-rule-list/:idx',
        name: 'MisraCExampleReadFromRuleList',
        component: MisraCExampleRead,
        meta: {title: 'SW Test Forum - MISRA C 예제 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/misra-c-example/update/:idx',
        name: 'MisraCExampleUpdate',
        component: MisraCExampleUpdate,
        meta: {title: 'SW Test Forum - MISRA C 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-guideline/list',
        name: 'MisraCGuidelineList',
        component: MisraCGuidelineList,
        meta: {title: 'SW Test Forum - MISRA C 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-guideline/list/:misraCIdx',
        name: 'MisraCGuidelineListByMisraCIdx',
        component: MisraCGuidelineList,
        meta: {title: 'SW Test Forum - MISRA C 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-guideline/write/:misraCIdx',
        name: 'MisraCGuidelineWrite',
        component: MisraCGuidelineWrite,
        meta: {title: 'SW Test Forum - MISRA C 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-guideline/read/:idx',
        name: 'MisraCGuidelineRead',
        component: MisraCGuidelineRead,
        meta: {title: 'SW Test Forum - MISRA C 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-c-guideline/read/from-rule-list/:idx',
        name: 'MisraCGuidelineReadFromRuleList',
        component: MisraCGuidelineRead,
        meta: {title: 'SW Test Forum - MISRA C 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/misra-c-guideline/update/:idx',
        name: 'MisraCGuidelineUpdate',
        component: MisraCGuidelineUpdate,
        meta: {title: 'SW Test Forum - MISRA C 가이드라인 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp/list',
        name: 'MisraCppList',
        component: MisraCppList,
        meta: {title: 'SW Test Forum - MISRA C++ 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp/write',
        name: 'MisraCppWrite',
        component: MisraCppWrite,
        meta: {title: 'SW Test Forum - MISRA C++ 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp/read/:idx',
        name: 'MisraCppRead',
        component: MisraCppRead,
        meta: {title: 'SW Test Forum - MISRA C++ 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp/update/:idx',
        name: 'MisraCppUpdate',
        component: MisraCppUpdate,
        meta: {title: 'SW Test Forum - MISRA C++ 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-example/list',
        name: 'MisraCppExampleList',
        component: MisraCppExampleList,
        meta: {title: 'SW Test Forum - MISRA C++ 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-example/list/:misraCppIdx',
        name: 'MisraCppExampleListByMisraCppIdx',
        component: MisraCppExampleList,
        meta: {title: 'SW Test Forum - MISRA C++ 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-example/write/:misraCppIdx',
        name: 'MisraCppExampleWrite',
        component: MisraCppExampleWrite,
        meta: {title: 'SW Test Forum - MISRA C++ 예제 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-example/read/:idx',
        name: 'MisraCppExampleRead',
        component: MisraCppExampleRead,
        meta: {title: 'SW Test Forum - MISRA C++ 예제 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-example/read/from-rule-list/:idx',
        name: 'MisraCppExampleReadFromRuleList',
        component: MisraCppExampleRead,
        meta: {title: 'SW Test Forum - MISRA C++ 예제 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/misra-cpp-example/update/:idx',
        name: 'MisraCppExampleUpdate',
        component: MisraCppExampleUpdate,
        meta: {title: 'SW Test Forum - MISRA C++ 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-guideline/list',
        name: 'MisraCppGuidelineList',
        component: MisraCppGuidelineList,
        meta: {title: 'SW Test Forum - MISRA C++ 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-guideline/list/:misraCppIdx',
        name: 'MisraCppGuidelineListByMisraCppIdx',
        component: MisraCppGuidelineList,
        meta: {title: 'SW Test Forum - MISRA C++ 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-guideline/write/:misraCppIdx',
        name: 'MisraCppGuidelineWrite',
        component: MisraCppGuidelineWrite,
        meta: {title: 'SW Test Forum - MISRA C++ 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-guideline/read/:idx',
        name: 'MisraCppGuidelineRead',
        component: MisraCppGuidelineRead,
        meta: {title: 'SW Test Forum - MISRA C++ 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/misra-cpp-guideline/read/from-rule-list/:idx',
        name: 'MisraCppGuidelineReadFromRuleList',
        component: MisraCppGuidelineRead,
        meta: {title: 'SW Test Forum - MISRA C++ 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/misra-cpp-guideline/update/:idx',
        name: 'MisraCppGuidelineUpdate',
        component: MisraCppGuidelineUpdate,
        meta: {title: 'SW Test Forum - MISRA C++ 가이드라인 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions/list',
        name: 'JavaCodeConventionsList',
        component: JavaCodeConventionsList,
        meta: {title: 'SW Test Forum - Java Code Conventions 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions/write',
        name: 'JavaCodeConventionsWrite',
        component: JavaCodeConventionsWrite,
        meta: {title: 'SW Test Forum - Java Code Conventions 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions/read/:idx',
        name: 'JavaCodeConventionsRead',
        component: JavaCodeConventionsRead,
        meta: {title: 'SW Test Forum - Java Code Conventions 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions/update/:idx',
        name: 'JavaCodeConventionsUpdate',
        component: JavaCodeConventionsUpdate,
        meta: {title: 'SW Test Forum - Java Code Conventions 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-example/list',
        name: 'JavaCodeConventionsExampleList',
        component: JavaCodeConventionsExampleList,
        meta: {title: 'SW Test Forum - Java Code Conventions 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-example/list/:javaCodeConventionsIdx',
        name: 'JavaCodeConventionsExampleListByJavaCodeConventionsIdx',
        component: JavaCodeConventionsExampleList,
        meta: {title: 'SW Test Forum - Java Code Conventions 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-example/write/:javaCodeConventionsIdx',
        name: 'JavaCodeConventionsExampleWrite',
        component: JavaCodeConventionsExampleWrite,
        meta: {title: 'SW Test Forum - Java Code Conventions 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-example/read/:idx',
        name: 'JavaCodeConventionsExampleRead',
        component: JavaCodeConventionsExampleRead,
        meta: {title: 'SW Test Forum - Java Code Conventions 예제 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-example/read/from-rule-list/:idx',
        name: 'JavaCodeConventionsExampleReadFromRuleList',
        component: JavaCodeConventionsExampleRead,
        meta: {title: 'SW Test Forum - Java Code Conventions 예제 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/java-code-conventions-example/update/:idx',
        name: 'JavaCodeConventionsExampleUpdate',
        component: JavaCodeConventionsExampleUpdate,
        meta: {title: 'SW Test Forum - Java Code Conventions 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-guideline/list',
        name: 'JavaCodeConventionsGuidelineList',
        component: JavaCodeConventionsGuidelineList,
        meta: {title: 'SW Test Forum - Java Code Conventions 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-guideline/list/:javaCodeConventionsIdx',
        name: 'JavaCodeConventionsGuidelineListByJavaCodeConventionsIdx',
        component: JavaCodeConventionsGuidelineList,
        meta: {title: 'SW Test Forum - Java Code Conventions 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-guideline/write/:javaCodeConventionsIdx',
        name: 'JavaCodeConventionsGuidelineWrite',
        component: JavaCodeConventionsGuidelineWrite,
        meta: {title: 'SW Test Forum - Java Code Conventions 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-guideline/read/:idx',
        name: 'JavaCodeConventionsGuidelineRead',
        component: JavaCodeConventionsGuidelineRead,
        meta: {title: 'SW Test Forum - Java Code Conventions 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/java-code-conventions-guideline/read/from-rule-list/:idx',
        name: 'JavaCodeConventionsGuidelineReadFromRuleList',
        component: JavaCodeConventionsGuidelineRead,
        meta: {title: 'SW Test Forum - Java Code Conventions 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/java-code-conventions-guideline/update/:idx',
        name: 'JavaCodeConventionsGuidelineUpdate',
        component: JavaCodeConventionsGuidelineUpdate,
        meta: {title: 'SW Test Forum - Java Code Conventions 가이드라인 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe/list',
        name: 'CweList',
        component: CweList,
        meta: {title: 'SW Test Forum - CWE 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe/write',
        name: 'CweWrite',
        component: CweWrite,
        meta: {title: 'SW Test Forum - CWE 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe/read/:idx',
        name: 'CweRead',
        component: CweRead,
        meta: {title: 'SW Test Forum - CWE 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe/update/:idx',
        name: 'CweUpdate',
        component: CweUpdate,
        meta: {title: 'SW Test Forum - CWE 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-example/list',
        name: 'CweExampleList',
        component: CweExampleList,
        meta: {title: 'SW Test Forum - CWE 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-example/list/:cweIdx',
        name: 'CweExampleListByCweIdx',
        component: CweExampleList,
        meta: {title: 'SW Test Forum - CWE 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-example/write/:cweIdx',
        name: 'CweExampleWrite',
        component: CweExampleWrite,
        meta: {title: 'SW Test Forum - CWE 예제 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-example/read/:idx',
        name: 'CweExampleRead',
        component: CweExampleRead,
        meta: {title: 'SW Test Forum - CWE 예제 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-example/read/from-rule-list/:idx',
        name: 'CweExampleReadFromRuleList',
        component: CweExampleRead,
        meta: {title: 'SW Test Forum - CWE 예제 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/cwe-example/update/:idx',
        name: 'CweExampleUpdate',
        component: CweExampleUpdate,
        meta: {title: 'SW Test Forum - CWE 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-guideline/list',
        name: 'CweGuidelineList',
        component: CweGuidelineList,
        meta: {title: 'SW Test Forum - CWE 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-guideline/list/:cweIdx',
        name: 'CweGuidelineListByCweIdx',
        component: CweGuidelineList,
        meta: {title: 'SW Test Forum - CWE 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-guideline/write/:cweIdx',
        name: 'CweGuidelineWrite',
        component: CweGuidelineWrite,
        meta: {title: 'SW Test Forum - CWE 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-guideline/read/:idx',
        name: 'CweGuidelineRead',
        component: CweGuidelineRead,
        meta: {title: 'SW Test Forum - CWE 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/cwe-guideline/read/from-rule-list/:idx',
        name: 'CweGuidelineReadFromRuleList',
        component: CweGuidelineRead,
        meta: {title: 'SW Test Forum - CWE 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/cwe-guideline/update/:idx',
        name: 'CweGuidelineUpdate',
        component: CweGuidelineUpdate,
        meta: {title: 'SW Test Forum - CWE 가이드라인 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop/list',
        name: 'StyleCopList',
        component: StyleCopList,
        meta: {title: 'SW Test Forum - StyleCop 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop/write',
        name: 'StyleCopWrite',
        component: StyleCopWrite,
        meta: {title: 'SW Test Forum - StyleCop 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop/read/:idx',
        name: 'StyleCopRead',
        component: StyleCopRead,
        meta: {title: 'SW Test Forum - StyleCop 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop/update/:idx',
        name: 'StyleCopUpdate',
        component: StyleCopUpdate,
        meta: {title: 'SW Test Forum - StyleCop 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-example/list',
        name: 'StyleCopExampleList',
        component: StyleCopExampleList,
        meta: {title: 'SW Test Forum - StyleCop 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-example/list/:styleCopIdx',
        name: 'StyleCopExampleListByStyleCopIdx',
        component: StyleCopExampleList,
        meta: {title: 'SW Test Forum - StyleCop 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-example/write/:styleCopIdx',
        name: 'StyleCopExampleWrite',
        component: StyleCopExampleWrite,
        meta: {title: 'SW Test Forum - StyleCop 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-example/read/:idx',
        name: 'StyleCopExampleRead',
        component: StyleCopExampleRead,
        meta: {title: 'SW Test Forum - StyleCop 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-example/read/from-rule-list/:idx',
        name: 'StyleCopExampleReadFromRuleList',
        component: StyleCopExampleRead,
        meta: {title: 'SW Test Forum - StyleCop 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/style-cop-example/update/:idx',
        name: 'StyleCopExampleUpdate',
        component: StyleCopExampleUpdate,
        meta: {title: 'SW Test Forum - StyleCop 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-guideline/list',
        name: 'StyleCopGuidelineList',
        component: StyleCopGuidelineList,
        meta: {title: 'SW Test Forum - StyleCop 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-guideline/list/:styleCopIdx',
        name: 'StyleCopGuidelineListByStyleCopIdx',
        component: StyleCopGuidelineList,
        meta: {title: 'SW Test Forum - StyleCop 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-guideline/write/:styleCopIdx',
        name: 'StyleCopGuidelineWrite',
        component: StyleCopGuidelineWrite,
        meta: {title: 'SW Test Forum - StyleCop 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-guideline/read/:idx',
        name: 'StyleCopGuidelineRead',
        component: StyleCopGuidelineRead,
        meta: {title: 'SW Test Forum - StyleCop 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/style-cop-guideline/read/from-rule-list/:idx',
        name: 'StyleCopGuidelineReadFromRuleList',
        component: StyleCopGuidelineRead,
        meta: {title: 'SW Test Forum - StyleCop 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/style-cop-guideline/update/:idx',
        name: 'StyleCopGuidelineUpdate',
        component: StyleCopGuidelineUpdate,
        meta: {title: 'SW Test Forum - StyleCop 가이드라인 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop/list',
        name: 'FxCopList',
        component: FxCopList,
        meta: {title: 'SW Test Forum - FxCop 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop/write',
        name: 'FxCopWrite',
        component: FxCopWrite,
        meta: {title: 'SW Test Forum - FxCop 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop/read/:idx',
        name: 'FxCopRead',
        component: FxCopRead,
        meta: {title: 'SW Test Forum - FxCop 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop/update/:idx',
        name: 'FxCopUpdate',
        component: FxCopUpdate,
        meta: {title: 'SW Test Forum - FxCop 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-example/list',
        name: 'FxCopExampleList',
        component: FxCopExampleList,
        meta: {title: 'SW Test Forum - FxCop 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-example/list/:fxCopIdx',
        name: 'FxCopExampleListByFxCopIdx',
        component: FxCopExampleList,
        meta: {title: 'SW Test Forum - FxCop 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-example/write/:fxCopIdx',
        name: 'FxCopExampleWrite',
        component: FxCopExampleWrite,
        meta: {title: 'SW Test Forum - FxCop 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-example/read/:idx',
        name: 'FxCopExampleRead',
        component: FxCopExampleRead,
        meta: {title: 'SW Test Forum - FxCop 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-example/read/from-rule-list/:idx',
        name: 'FxCopExampleReadFromRuleList',
        component: FxCopExampleRead,
        meta: {title: 'SW Test Forum - FxCop 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/fx-cop-example/update/:idx',
        name: 'FxCopExampleUpdate',
        component: FxCopExampleUpdate,
        meta: {title: 'SW Test Forum - FxCop 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-guideline/list',
        name: 'FxCopGuidelineList',
        component: FxCopGuidelineList,
        meta: {title: 'SW Test Forum - FxCop 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-guideline/list/:fxCopIdx',
        name: 'FxCopGuidelineListByFxCopIdx',
        component: FxCopGuidelineList,
        meta: {title: 'SW Test Forum - FxCop 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-guideline/write/:fxCopIdx',
        name: 'FxCopGuidelineWrite',
        component: FxCopGuidelineWrite,
        meta: {title: 'SW Test Forum - FxCop 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-guideline/read/:idx',
        name: 'FxCopGuidelineRead',
        component: FxCopGuidelineRead,
        meta: {title: 'SW Test Forum - FxCop 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/fx-cop-guideline/read/from-rule-list/:idx',
        name: 'FxCopGuidelineReadFromRuleList',
        component: FxCopGuidelineRead,
        meta: {title: 'SW Test Forum - FxCop 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/fx-cop-guideline/update/:idx',
        name: 'FxCopGuidelineUpdate',
        component: FxCopGuidelineUpdate,
        meta: {title: 'SW Test Forum - FxCop 가이드라인 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/metric/list',
        name: 'MetricList',
        component: MetricList,
        meta: {title: 'SW Test Forum - 메트릭 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/metric/write',
        name: 'MetricWrite',
        component: MetricWrite,
        meta: {title: 'SW Test Forum - 메트릭 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/metric/read/:idx',
        name: 'MetricRead',
        component: MetricRead,
        meta: {title: 'SW Test Forum - 메트릭 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/metric/update/:idx',
        name: 'MetricUpdate',
        component: MetricUpdate,
        meta: {title: 'SW Test Forum - 메트릭 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-example/list',
        name: 'MetricExampleList',
        component: MetricExampleList,
        meta: {title: 'SW Test Forum - 메트릭 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-example/list/:metricIdx',
        name: 'MetricExampleListByMetricIdx',
        component: MetricExampleList,
        meta: {title: 'SW Test Forum - 메트릭 예제 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-example/write/:metricIdx',
        name: 'MetricExampleWrite',
        component: MetricExampleWrite,
        meta: {title: 'SW Test Forum - 메트릭 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-example/read/:idx',
        name: 'MetricExampleRead',
        component: MetricExampleRead,
        meta: {title: 'SW Test Forum - 메트릭 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-example/read/from-rule-list/:idx',
        name: 'MetricExampleReadFromRuleList',
        component: MetricExampleRead,
        meta: {title: 'SW Test Forum - 메트릭 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/metric-example/update/:idx',
        name: 'MetricExampleUpdate',
        component: MetricExampleUpdate,
        meta: {title: 'SW Test Forum - 메트릭 예제 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-guideline/list',
        name: 'MetricGuidelineList',
        component: MetricGuidelineList,
        meta: {title: 'SW Test Forum - 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-guideline/list/:metricIdx',
        name: 'MetricGuidelineListByMetricIdx',
        component: MetricGuidelineList,
        meta: {title: 'SW Test Forum - 가이드라인 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-guideline/write/:metricIdx',
        name: 'MetricGuidelineWrite',
        component: MetricGuidelineWrite,
        meta: {title: 'SW Test Forum - 가이드라인 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-guideline/read/:idx',
        name: 'MetricGuidelineRead',
        component: MetricGuidelineRead,
        meta: {title: 'SW Test Forum - 가이드라인 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/metric-guideline/read/from-rule-list/:idx',
        name: 'MetricGuidelineReadFromRuleList',
        component: MetricGuidelineRead,
        meta: {title: 'SW Test Forum - 가이드라인 보기', layoutView: true, authRequired: true, fromRuleList: true}
    },
    {
        path: '/metric-guideline/update/:idx',
        name: 'MetricGuidelineUpdate',
        component: MetricGuidelineUpdate,
        meta: {title: 'SW Test Forum - 가이드라인 수정', layoutView: true, authRequired: true}
    },

    {
        path: '/static-tool/list',
        name: 'StaticToolList',
        component: StaticToolList,
        meta: {title: 'SW Test Forum - STATIC 트러블 슈팅 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/static-tool/write',
        name: 'StaticToolWrite',
        component: StaticToolWrite,
        meta: {title: 'SW Test Forum - STATIC 트러블 슈팅 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/static-tool/read/:idx',
        name: 'StaticToolRead',
        component: StaticToolRead,
        meta: {title: 'SW Test Forum - STATIC 트러블 슈팅 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/static-tool/update/:idx',
        name: 'StaticToolUpdate',
        component: StaticToolUpdate,
        meta: {title: 'SW Test Forum - STATIC 트러블 슈팅 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/cover-tool/list',
        name: 'CoverToolList',
        component: CoverToolList,
        meta: {title: 'SW Test Forum - COVER 트러블 슈팅 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/cover-tool/write',
        name: 'CoverToolWrite',
        component: CoverToolWrite,
        meta: {title: 'SW Test Forum - COVER 트러블 슈팅 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/cover-tool/read/:idx',
        name: 'CoverToolRead',
        component: CoverToolRead,
        meta: {title: 'SW Test Forum - COVER 트러블 슈팅 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/cover-tool/update/:idx',
        name: 'CoverToolUpdate',
        component: CoverToolUpdate,
        meta: {title: 'SW Test Forum - COVER 트러블 슈팅 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/controller-tester-tool/list',
        name: 'ControllerTesterToolList',
        component: ControllerTesterToolList,
        meta: {title: 'SW Test Forum - Controller Tester 트러블 슈팅 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/controller-tester-tool/write',
        name: 'ControllerTesterToolWrite',
        component: ControllerTesterToolWrite,
        meta: {title: 'SW Test Forum - Controller Tester 트러블 슈팅 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/controller-tester-tool/read/:idx',
        name: 'ControllerTesterToolRead',
        component: ControllerTesterToolRead,
        meta: {title: 'SW Test Forum - Controller Tester 트러블 슈팅 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/controller-tester-tool/update/:idx',
        name: 'ControllerTesterToolUpdate',
        component: ControllerTesterToolUpdate,
        meta: {title: 'SW Test Forum - Controller Tester 트러블 슈팅 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/toolchain/list',
        name: 'ToolchainList',
        component: ToolchainList,
        meta: {title: 'SW Test Forum - 툴체인 트러블 슈팅 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/toolchain/write',
        name: 'ToolchainWrite',
        component: ToolchainWrite,
        meta: {title: 'SW Test Forum - 툴체인 트러블 슈팅 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/toolchain/read/:idx',
        name: 'ToolchainRead',
        component: ToolchainRead,
        meta: {title: 'SW Test Forum - 툴체인 트러블 슈팅 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/toolchain/update/:idx',
        name: 'ToolchainUpdate',
        component: ToolchainUpdate,
        meta: {title: 'SW Test Forum - 툴체인 트러블 슈팅 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/notice/list',
        name: 'NoticeList',
        component: NoticeList,
        meta: {title: 'SW Test Forum - 공지사항 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/notice/write',
        name: 'NoticeWrite',
        component: NoticeWrite,
        meta: {title: 'SW Test Forum - 공지사항 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/notice/read/:idx',
        name: 'NoticeRead',
        component: NoticeRead,
        meta: {title: 'SW Test Forum - 공지사항 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/notice/update/:idx',
        name: 'NoticeUpdate',
        component: NoticeUpdate,
        meta: {title: 'SW Test Forum - 공지사항 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/user/list',
        name: 'userList',
        component: UserList,
        meta: {title: 'SW Test Forum - 사용자 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/user/write',
        name: 'userWrite',
        component: UserWrite,
        meta: {title: 'SW Test Forum - 사용자 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/user/read/:idx',
        name: 'userRead',
        component: UserRead,
        meta: {title: 'SW Test Forum - 사용자 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/user/update/:idx',
        name: 'userUpdate',
        component: UserUpdate,
        meta: {title: 'SW Test Forum - 사용자 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/login-history/list',
        name: 'loginHistoryList',
        component: LoginHistoryList,
        meta: {title: 'SW Test Forum - 로그인 기록 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/login-history/read/:idx',
        name: 'loginHistoryRead',
        component: LoginHistoryRead,
        meta: {title: 'SW Test Forum - 로그인 기록 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/data-history/list',
        name: 'dataHistoryList',
        component: DataHistoryList,
        meta: {title: 'SW Test Forum - 데이터 기록 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/data-history/read/:idx',
        name: 'dataHistoryRead',
        component: DataHistoryRead,
        meta: {title: 'SW Test Forum - 데이터 기록 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/storage/list',
        name: 'StorageList',
        component: StorageList,
        meta: {title: 'SW Test Forum - 저장소 리스트', layoutView: true, authRequired: true}
    },
    {
        path: '/storage/write',
        name: 'StorageWrite',
        component: StorageWrite,
        meta: {title: 'SW Test Forum - 저장소 작성', layoutView: true, authRequired: true}
    },
    {
        path: '/storage/read/:idx',
        name: 'StorageRead',
        component: StorageRead,
        meta: {title: 'SW Test Forum - 저장소 보기', layoutView: true, authRequired: true}
    },
    {
        path: '/storage/update/:idx',
        name: 'StorageUpdate',
        component: StorageUpdate,
        meta: {title: 'SW Test Forum - 저장소 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/setting/update',
        name: 'SettingUpdate',
        component: SettingUpdate,
        meta: {title: 'SW Test Forum - 설정 수정', layoutView: true, authRequired: true}
    },
    {
        path: '/:pathMatch(.*)',
        name: 'Error404',
        meta: {title: 'SW Test Forum - 에러 페이지', layoutView: false, authRequired: false}
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
    scrollBehavior(to, from, savedPosition, popstate) {
        //브러우저 뒤로가기 버튼을 클릭하는 경우, scroll 위치를 변경하지 않음
        if (isEmpty(savedPosition)) { // savedPosition 객체가 비어있는 경우 최상단으로 scroll 이동
            return new Promise((resolve, reject) => {
                resolve({left: 0, top: 0})
            })
        } else {
            return new Promise((resolve, reject) => {
                behavior: 'smooth', resolve({left: savedPosition.left, top: savedPosition.top})
            })
        }
    }
})

router.beforeEach(async function (to, from, next) {
    // page title 설정
    //nextTick은 DOM이 업데이트 된 후 실행
    nextTick(() => {
        document.title = to.meta.title;
    });

    // 잘못된 URI로 매핑되는 경우, Error404
    if (to.name === 'Error404') {
        await error.fire({
            text: "페이지를 찾을 수 없습니다.",
        });

        router.go(-1);
    }
    // to: 이동할 url에 해당하는 라우팅 객체
    else if (
        to.matched.some((routeInfo) => {
            return routeInfo.meta.authRequired;
        })) {
        if (vueCookies.get('isHasToken')) {
            next();
        }
        // 권한이 없는 경우, Error401
        else {
            if (from.href !== "/login") {
                await error.fire({
                    text: "로그인이 필요합니다.",
                });

                next('/login');
                //router.go(-1);
            }
        }
    } else {
        if (vueCookies.get('isHasToken')) {
            await warning.fire({
                text: "로그아웃을 하신 다음, 다른 계정으로 로그인 해주세요.",
            });

            next('/dashboard');
        } else {
            next();
        }
    }
});

export default router
