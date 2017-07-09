/**
 * Created by yangqihua on 2017/7/7.
 */
import { Message } from 'element-ui';

import axios from 'axios';
import router from '../../router'
import settings from '../../config/settings'

const interceptor = () => {
  axios.interceptors.request.use(
    config => {
      console.log(Vue.$router);
      //   let user = JSON.parse(sessionStorage.getItem('user'));
      //   if (user) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
      //     token = `Bearer ${user.token}`;
      //     // store.common.state.user = user;
      //   } else {
      //     sessionStorage.removeItem('user');
      //     Vue.$router.replace("login");
      //   }
      // } else {
      //   token = user.token;
      // }
      // config.headers.Authorization = `Bearer ${user.token}`;
      //
      // let user = JSON.parse(sessionStorage.getItem('user'));
      // store.state.user = user;
      // if (config.url.substring(config.url.length - 5) != "login") {
      //   if (user) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
      //     config.headers.Authorization = `Bearer ${user.token}`;
      //   } else {
      //     sessionStorage.removeItem('user');
      //     router.replace('login');
      //   }
      // }
      return config;
    },
    err => {
      console.log(err); // for debug
      return Promise.reject(err);
    });

// http response 拦截器
  axios.interceptors.response.use(
    response => {
      // if (response.data && response.data.code) {
      //   if (response.data.code === '2001') {
      //     auth.logout()
      //   }
      // }
      return response;
    },
    error => {
      try {
        if (error.response.status) {
          if (error.response.status == 403) {
            sessionStorage.removeItem('user');
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
        sessionStorage.removeItem('user');
        router.replace('login');
      }
    });
}

export default interceptor;
