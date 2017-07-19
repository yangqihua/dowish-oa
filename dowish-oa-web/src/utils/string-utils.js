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
    if (obj[k] === null || obj[k] === 'undefined' || obj[k] === '' || obj[k] === 0 || obj[k] === false) {
      continue;
    } else if (typeof obj[k] == 'object') {
      if (Array.isArray(obj[k])) {
        obj[k] = []
      }
      else {
        resetObject(obj[k])
      }
    } else if (typeof obj[k] == 'boolean') {
      obj[k] = false
    } else if (typeof obj[k] == 'number') {
      obj[k] = 0;
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

const isParentId = (id,idName,parentIdName, root) => {
  for (let key in root) {
    if (root[key][parentIdName] == id) {
      return true
    } else if (root[key].list != null) {
      return isParentId(id,idName,parentIdName, root[key].list);
    } else {
      return false
    }
  }
}
const setParentId = (id,idName,parentIdName, root, path = new Set()) => {
  path.add(root[idName])
  if (root[idName] == id) {
    return true
  }
  if (root.list != null) {
    for (let key in root.list) {
      if (setParentId(id,idName,parentIdName, root.list[key], path)) {
        return true
      }
    }
  }
  path.delete(root[idName])
  return false
}

const arrayToTree = (data, options)=>{
  options = options || {};
  let ID_KEY = options.id || 'id';
  let PARENT_KEY = options.parentId || 'parent';
  let CHILDREN_KEY = options.childrenKey || 'children';
  let ROOT_ID = options.rootId || 0;

  let tree = [];
  let childrenOf = {};
  let item, id, parentId;

  for (let i = 0, length = data.length; i < length; i++) {
    item = data[i];
    id = item[ID_KEY];
    parentId = item[PARENT_KEY] || 0;
    // every item may have children
    childrenOf[id] = childrenOf[id] || [];
    // init its children
    item[CHILDREN_KEY] = childrenOf[id];
    if (parentId != ROOT_ID) {
      // init its parent's children object
      childrenOf[parentId] = childrenOf[parentId] || [];
      // push it into its parent's children object
      childrenOf[parentId].push(item);
    } else {
      tree.push(item);
    }
  }
  ;

  return tree;
}

const deleteEmptyProperty = (object)=>{
  for (let i in object) {
    let value = object[i];
    if (typeof value === 'object') {
      if (Array.isArray(value)) {
        if (value.length == 0) {
          delete object[i];
          console.log('delete Array:', i);
          continue;
        }
      }
      deleteEmptyProperty(value);
      if (isEmptyObject(value)) {
        delete object[i];
        console.log('delete a empty object:',i, value);
      }
    } else {
      if (value === '' || value === null || value === undefined) {
        delete object[i];
        console.log('delete: ', i);
      }
    }
  }
}


export default {
  trim, subString, getBaseUrl, createUniqueString, useTokenApi, isEmptyObject, resetObject, getBeforeDate,
  isParentId, setParentId, arrayToTree,deleteEmptyProperty
}
