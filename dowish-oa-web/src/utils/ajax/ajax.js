import Vue from 'vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import ElementUI from 'element-ui';

Vue.use(VueAxios, axios);

import settings from '../../config/settings.js';

Vue.axios.defaults.baseURL = settings.baseUrl;

const ajax = ({type = 'get', url, data = {}, showLoading = true, loadingDom = 'main-container', scb, ecb}) => {
    loadingDom = document.getElementById(loadingDom);
    let loadingInstance;
    if (showLoading) {
      loadingInstance = ElementUI.Loading.service({text: '加载中...', fullscreen: true, target: loadingDom});
    }
    let params = data;
    if (type === 'get') {
      params = {
        params: data
      };
    }
    Vue.axios[type](url, params).then((response) => {
      console.log(response.data);
      if (response.data.code == 200) {
        if (scb) {
          scb(response.data);
        }
      } else {
        ElementUI.Message({message: response.data.msg, type: 'warning', showClose: true});
        if (ecb) {
          ecb(response.data);
        }
      }
      if (showLoading) {
        loadingInstance.close();
      }
    }).catch((err) => {
      //处理错误
      ElementUI.Message({message: "网络异常，异常原因："+err, type: 'warning', showClose: true});
      if (showLoading) {
        loadingInstance.close();
      }
    });
  }
  ;

export default ajax;
