/**
 * Created by yangqihua on 2017/7/7.
 */

import * as types from "./mutation-types";
import localStore from '../../utils/localStore'
const mutations = {

  [types.LOGIN](state,response){
    console.log("res:",response);
    state.user = response.user;
    state.user['token'] = response.token;
    localStore.setSession("user",state.user);
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
