/**
 * 去除首尾空格
 * @param  {[string]} string [字符串]
 * @return {string}        [返回处理后数据]
 */
import urls from '../config/unUseTokenUrl'

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
  var reg = /^((\w+):\/\/([^\/:]*)(?::(\d+))?)(.*)/;
  reg.exec(url);
  return RegExp.$1;
}
const createUniqueString = () => {
  const timestamp = +new Date() + '';
  const randomNum = parseInt((1 + Math.random()) * 65536) + '';
  return (+(randomNum + timestamp)).toString(32);
}
const useTokenApi = (url)=>{
  return !urls.find(u=>u===url);
}


const isEmptyObject= (obj)=>{
  let t;
  for (t in obj)
    return !1;
  return !0
}

export default {
  trim, subString, getBaseUrl,createUniqueString,useTokenApi,isEmptyObject
}
