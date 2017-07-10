/**
 * Created by yangqihua on 2017/7/7.
 */
export default {
  menuList: state => state.menuList,
  hasPermission: (state, permission) => {
    if (window.parent.permissions.indexOf(permission) > -1) {
      return true;
    } else {
      return false;
    }
  },

  loading: state => state.loading,
  sidebar: state => state.sidebar,
  userInfo: state => state.userInfo,

}
