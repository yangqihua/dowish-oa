/**
 * Created by yangqihua on 2017/7/10.
 */

import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'

export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {
      //list部分
      searchKey: '',  //搜索的用户名
      pagination: {
        total: 0,
        pageNo: 1,
        pageSize: 10,
      },
      rows: []
    }
  },
  methods: {
    handleMapper(index,row){

    },
    handleSizeChange(val) {
      this.pagination.pageSize = val;
      this.loadData();
    },
    handleCurrentChange(val) {
      this.pagination.pageNo = val;
      this.loadData();
    },

    loadData(){
      let params = {
        url: 'sys/generator/list',
        showLoading:false,
        data:{
          tableName:this.searchKey,
          page:this.pagination.pageNo,
          limit:this.pagination.pageSize
        },
        scb: (response) => {
          this.rows = response.page.list
          this.pagination.total = response.page.totalCount
        }
      }
      ajax(params)
    },

  },
  created(){
    this.loadData();
  }
}
