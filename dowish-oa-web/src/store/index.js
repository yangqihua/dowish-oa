import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import common from './common/';
const debug = process.env.NODE_ENV !== 'production';

const store = new Vuex.Store({
  modules: {
    common,
  },
  strict: debug,
});
export default store;
