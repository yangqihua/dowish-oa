/**
 * Created by yangqihua on 2017/7/7.
 */

import Vue from "vue";
import * as types from './mutation-types';
import defaultMenu from "./default-menu";
import * as api from "../../utils/api";

import ajax from '../../utils/ajax/ajax'

const loginAction = ({dispatch,commit, state}, params) => {
  params.scb = response => {
    commit(types.LOGIN, response);
    if(params.hasOwnProperty('cb')){
      dispatch('getMenuList',{url: '/sys/menu/user',cb:params.cb});
    }else {
      dispatch('getMenuList',{url: '/sys/menu/user'});
    }
  };
  ajax(params);
};

const getMenuList = ({dispatch,commit, state}, params) => {
  params.scb = response => {
    commit(types.GET_MENU_LIST, response);
    //回调跳转到首页
    if(params.hasOwnProperty('cb')){
      params.cb(response);
    };
  };
  ajax(params);
};

const toggleSidebar = ({commit}, opened) => commit(types.TOGGLE_SIDEBAR, opened)

const toggleDevice = ({commit}, device) => commit(types.TOGGLE_DEVICE, device)

const expandMenu = ({commit}, menuItem) => {
  if (menuItem) {
    menuItem.expanded = menuItem.expanded || false
    commit(types.EXPAND_MENU, menuItem)
  }
}

const switchEffect = ({commit}, effectItem) => {
  if (effectItem) {
    commit(types.SWITCH_EFFECT, effectItem)
  }
};

//异步的函数
const toggleLoading = ({commit}) => commit(types.TOGGLE_LOADING);

const loadMenuList = ({commit}) => {
  Vue.axios.get(api.TEST_DATA).then(res => {
    commit(types.LOAD_MENU, res.data.menuList);
  }).catch(exp => commit(types.LOAD_MENU, defaultMenu));
};

export default {
  loginAction,
  getMenuList,

  toggleSidebar,
  toggleDevice,
  expandMenu,
  switchEffect,
  toggleLoading,
  loadMenuList,
}

