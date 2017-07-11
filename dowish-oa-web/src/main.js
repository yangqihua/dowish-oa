// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import app from "./App.vue";
import VueRouter from "vue-router";
import routeConfig from "./router";
// import NProgress from "vue-nprogress";
import {sync} from "vuex-router-sync";
import store from "./store";
import axios from "axios";
import filters from "./utils/string-utils";
import VueProgressBar from "vue-progressbar";
import {TOGGLE_SIDEBAR} from "./store/common/mutation-types";
import VueLazyload from "vue-lazyload";
import auth from "./utils/auth";
import Element from "element-ui";
import "element-ui/lib/theme-default/index.css";
import ImpPanel from "./components/panel.vue";

import stringUtils from './utils/string-utils';
import localStore from './utils/local-store'
import * as types from './store/common/mutation-types'

// import interceptor from './utils/ajax/interceptor';

Vue.use(Element);

// or with options
// Vue.use(VueLazyload, {
//   preLoad: 1.3,
//   attempt: 1
// })

//加载路由中间件
Vue.use(VueRouter)

Vue.component(ImpPanel.name, ImpPanel);

// const options = {
//   color: '#eeeeee',
//   failedColor: '#874b4b',
//   thickness: '2px',
//   transition: {
//     speed: '0.2s',
//     opacity: '0.6s'
//   },
//   autoRevert: true,
//   location: 'top',
//   inverse: false
// }

// Vue.use(VueProgressBar, options)


//定义路由
const router = new VueRouter({
  routes: routeConfig,
})


// sync(store, router)

const {state} = store
router.beforeEach((route, redirect, next) => {

  if (state.common.device.isMobile && state.common.sidebar.opened) {
    store.commit(TOGGLE_SIDEBAR, false)
  }

  let user = state.common.user;
  if (stringUtils.isEmptyObject(user)) {
    user = localStore.getSession("user");
  }
  if (!user && route.path !== '/login') {
    next({
      path: '/login',
      query: {redirect: route.fullPath}
    })
  } else {
    store.commit(types.SET_USER,user);
    next()
  }

});


axios.interceptors.request.use(
  config => {
    let url = config.url.substring(config.baseURL.length, config.url.length);
    if (stringUtils.useTokenApi(url)) {
      // 没有设置token
      if (!(config.headers.hasOwnProperty('Authorization') && config.headers.Authorization.indexOf("Bearer") > -1)) {
        let user = state.common.user;
        if (stringUtils.isEmptyObject(user)) {
          user = localStore.getSession("user");
        }
        if (user) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
          // config.headers.Authorization = `Bearer ${user.token}`;
          store.commit(types.SET_USER,user);
          config.headers.Authorization = `Bearer ${user.token}`;
        } else {
          localStore.deleteSession("user");
          router.replace('sys/login');
        }
      }
    }
    return config;
  },
  err => {
    // console.log(err); // for debug
    return Promise.reject(err);
  });

// http response 拦截器
axios.interceptors.response.use(
  response => {
    if (response.data && response.data.code) {
      if (response.data.code === '500') {
        Message({message: '服务器异常！', type: 'warning', showClose: true});
      }
    }
    return response;
  },
  error => {
    try {
      if (error.response.status) {
        if (error.response.status == 403) {
          localStore.deleteSession("user");
          router.replace('login');
        } else if (error.response.status == 400 || error.response.status == 401) {
          Message({message: '请求参数错误！', type: 'warning', showClose: true});
        } else if (error.response.status == 404) {
          // router.push('404');
        } else if (error.response.status == 500) {
          Message({message: '服务器异常！', type: 'warning', showClose: true});
        }
      }
      return Promise.reject(error.response.data);   // 返回接口返回的错误信息
    } catch (e) {
      localStore.deleteSession("user");
      router.replace('login');
    }
  });

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

new Vue({
  store,
  // nprogress,
  router,
  el: "#root",
  render: h => h(app)
})
