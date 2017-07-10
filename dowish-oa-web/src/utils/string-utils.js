/*
* Created by yangqihua on 2017/7/7.
*/

/**
 * 去除首尾空格
 * @param  {[string]} string [字符串]
 * @return {string}        [返回处理后数据]
 */
import urls from '../config/un-use-token-urls'

const trim = (string) => {
  return string.toString().replace();
}

const subString = (value, length = 10) => {
  if (value && value != null && value.length > length) {
    return value.substring(0, length)
  }
  return value;
}

const getBaseUrl = (url) => {
  let reg = /^((\w+):\/\/([^\/:]*)(?::(\d+))?)(.*)/;
  reg.exec(url);
  return RegExp.$1;
}
const createUniqueString = () => {
  const timestamp = +new Date() + '';
  const randomNum = parseInt((1 + Math.random()) * 65536) + '';
  return (+(randomNum + timestamp)).toString(32);
}
const useTokenApi = (url) => {
  return !urls.find(u => u === url);
}


const isEmptyObject = (obj) => {
  let t;
  for (t in obj)
    return !1;
  return !0
};

const resetObject = (obj) => {
  for (let k in obj) {
    if (obj[k] instanceof Object) {
      resetObject(obj[k])
    } else if (obj[k] instanceof Boolean) {
      obj[k] = false
    } else if (obj[k] instanceof Number) {
      obj[k] = 0;
    }else if(obj[k] == null || obj[k] == 'undefined'){
      continue;
    }
    else {
      obj[k] = '';
    }
  }
}
const getBeforeDate = (n) => {
  let list = [];
  let d = new Date(); // 这个算法能自动处理闰年和非闰年。2012年是闰年，所以2月有29号
  let s = '';
  let i = 0;
  while (i < n) {
    d.setDate(d.getDate() - 1);
    let year = d.getFullYear();
    let mon = d.getMonth() + 1;
    let day = d.getDate();
    list.push(year + "-" + (mon < 10 ? ('0' + mon) : mon) + "-" + (day < 10 ? ('0' + day) : day));
    i++;
  }
  return list.reverse();
}

export default {
  trim, subString, getBaseUrl, createUniqueString, useTokenApi, isEmptyObject, resetObject, getBeforeDate
}
