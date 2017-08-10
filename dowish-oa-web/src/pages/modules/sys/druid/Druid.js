/**
 * Created by yangqihua on 2017/7/10.
 */

import panel from "../../../../components/panel.vue"
import treeter from "../../../../components/treeter"

//深拷贝
import merge from 'element-ui/src/utils/merge';
import {mapGetters, mapActions, mapMutations} from 'vuex'
import perms from '../../../../config/permissions'
import ajax from "../../../../utils/ajax/ajax"
import stringUtils from '../../../../utils/string-utils.js'


export default {
  components: {
    'content-panel': panel,
  },
  data(){
    return {
    }
  },
  methods: {
    loadData(){
      let params = {
        url: '/sys/druid/info',
        showLoading: false,
        scb: (res) => {
          window.open(res.path)
        }
      }
      ajax(params)
    }
  },
  created(){
    this.loadData();
  }
}
