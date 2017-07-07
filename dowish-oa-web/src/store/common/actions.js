import Vue from "vue";
import * as types from './mutation-types';
import defaultMenu from "./default-menu";
import * as api from "../../utils/api";


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

const loadMenuList= ({commit})=>
{
  Vue.axios.get(api.TEST_DATA).then(res => {
    commit(types.LOAD_MENU, res.data.menuList);
  }).catch(exp => commit(types.LOAD_MENU, defaultMenu));
};

export default {
  toggleSidebar,
  toggleDevice,
  expandMenu,
  switchEffect,
  toggleLoading,
  loadMenuList
}

