/**
 * Created by yangqihua on 2017/7/9.
 */


// 设置cookie
const setCookie = (c_name, value, expiremMinutes) => {
  let time = new Date();
  time.setTime(time.getTime() + expiremMinutes * 60 * 1000);
  document.cookie = c_name + "=" + escape(value) + ((expiremMinutes == null) ? "" : ";expires=" + time.toGMTString());
}

// 读取cookie
const getCookie = (c_name) => {
  if (document.cookie.length > 0) {
    let c_start = document.cookie.indexOf(c_name + "=");
    if (c_start != -1) {
      c_start = c_start + c_name.length + 1;
      let c_end = document.cookie.indexOf(";", c_start);
      if (c_end == -1)
        c_end = document.cookie.length;
      return unescape(document.cookie.substring(c_start, c_end))
    }
  }
  return ""
}
//
// // 删除cookie
const deleteCookie = (c_name) => {
  let exp = new Date();
  exp.setTime(exp.getTime() - 1);
  let cval = getCookie(c_name);
  if (cval != null) {
    document.cookie = c_name + "=" + cval + ";expires=" + exp.toGMTString();
  }
}

const setSession = (key,value)=>{
  window.sessionStorage.setItem(key,JSON.stringify(value));
}

const getSession = (key)=>{
  return JSON.parse(window.sessionStorage.getItem(key));
}

const deleteSession = (key)=>{
  window.sessionStorage.removeItem(key);
}

export default{
  setCookie,getCookie,deleteCookie,setSession,getSession,deleteSession,
}
