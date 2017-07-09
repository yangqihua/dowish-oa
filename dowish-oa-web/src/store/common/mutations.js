/**
 * Created by yangqihua on 2017/7/7.
 */

import * as types from "./mutation-types";
import localStore from '../../utils/localStore'

//mutations必须是同步函数

const mutations = {

  [types.LOGIN](state,response){
    state.user = response.user;
    state.user['token'] = response.token;
    localStore.setSession("user",state.user);
  },

  [types.GET_MENU_LIST](state,response){
    state.menuList = response.menuList;
  },

  [types.SET_USER](state,user){
    state.user = user;
  },


  //只能同步的函数
  [types.TOGGLE_DEVICE] (state, isMobile) {
    state.device.isMobile = isMobile
  },
  [types.TOGGLE_LOADING] (state) {
    state.loading = !state.loading
  },
  [types.LOAD_MENU] (state, menu) {
    state.menuList = menu;
  },
  [types.SET_USER_INFO] (state, info) {
    state.userInfo = info;
  },
  [types.TOGGLE_SIDEBAR] (state, open) {
    if (open == null) open = !state.sidebar.opened;
    state.sidebar.opened = open;
  }
};
export default mutations;
