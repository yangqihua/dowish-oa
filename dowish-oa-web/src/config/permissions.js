/**
 * Created by yangqihua on 2017/7/19.
 */


const permissions = {

  //USER 部分
  SYS_USER_LIST:'sys:user:list',
  SYS_USER_INFO:'sys:user:info',
  SYS_USER_DELETE:'sys:user:delete',
  SYS_USER_UPDATE:'sys:user:update',
  SYS_USER_SAVE:'sys:user:save',
  USER_RESET_PWD:'sys:user:password',
  USER_SEARCH:'sys:user:list',

  // ROLE 部分
  SYS_ROLE_LIST:'sys:role:list',
  SYS_ROLE_INFO:'sys:role:info',
  SYS_ROLE_DELETE:'sys:role:delete',
  SYS_ROLE_UPDATE:'sys:role:update',
  SYS_ROLE_SAVE:'sys:role:save',
  SYS_ROLE_SELECT:'sys:role:select',

  // DEPT 部分
  SYS_DEPT_SELECT:'sys:dept:select',
  SYS_DEPT_LIST:'sys:dept:list',
  SYS_DEPT_SAVE:'sys:dept:save',
  SYS_DEPT_UPDATE:'sys:dept:update',
  SYS_DEPT_DELETE:'sys:dept:delete',

  // MENU 部分
  SYS_MENU_LIST:'sys:menu:list',
  SYS_MENU_INFO:'sys:menu:info',
  SYS_MENU_DELETE:'sys:menu:delete',
  SYS_MENU_UPDATE:'sys:menu:update',
  SYS_MENU_SAVE:'sys:menu:save',
  SYS_MENU_SELECT:'sys:menu:select',
  SYS_MENU_PERMS:'sys:menu:perms',

  // SCHEDULE 部分
  SYS_SCHEDULE_LIST:'sys:schedule:list',
  SYS_SCHEDULE_INFO:'sys:schedule:info',
  SYS_SCHEDULE_DELETE:'sys:schedule:delete',
  SYS_SCHEDULE_UPDATE:'sys:schedule:update',
  SYS_SCHEDULE_SAVE:'sys:schedule:save',
  SYS_SCHEDULE_PAUSE:'sys:schedule:pause',
  SYS_SCHEDULE_RESUME:'sys:schedule:resume',
  SYS_SCHEDULE_RUN:'sys:schedule:run',
  SYS_SCHEDULE_LOG:'sys:schedule:log',

  // DICT 部分
  SYS_DICT_LIST:'sys:dict:list',
  SYS_DICT_INFO:'sys:dict:info',
  SYS_DICT_DELETE:'sys:dict:delete',
  SYS_DICT_UPDATE:'sys:dict:update',
  SYS_DICT_SAVE:'sys:dict:save',
};


export default permissions
