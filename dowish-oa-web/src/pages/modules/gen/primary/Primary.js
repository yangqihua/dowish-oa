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

      activeStep: 0,
      //list部分
      searchKey: '',  //搜索的用户名
      pagination: {
        total: 0,
        pageNo: 1,
        pageSize: 10,
      },
      rows: [],

      allTables:[],


      stepTitle:'选择原生数据表',
      genTable:{},
      genConfig:{},

    }
  },
  methods: {

    // step1
    handleMapper(index, row){
      let params = {
        url: 'sys/generator/mapper',
        showLoading: false,
        data: {
          tableName: row.tableName,
        },
        scb: (response) => {
          this.genTable = response.genTable
          this.genConfig = response.genConfig

          this.getAllTable()
          this.next()
        }
      }
      ajax(params)
    },
    next(){
      if (this.activeStep < 3) {
        this.activeStep++
      }
      if(this.activeStep==1){
        this.stepTitle='配置数据表到代码Mapper'
      }else if(this.activeStep==2){
        this.stepTitle = '生成代码完成';
      }else{
        this.stepTitle = '选择原生数据表';
      }
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
        showLoading: false,
        data: {
          tableName: this.searchKey,
          page: this.pagination.pageNo,
          limit: this.pagination.pageSize
        },
        scb: (response) => {
          this.rows = response.page.list
          this.pagination.total = response.page.totalCount
        }
      }
      ajax(params)
    },


    //step2

    genCode(){

    },
    backStep(){
      this.activeStep--
      this.stepTitle = '选择原生数据表';
    },
    getAllTable(){
      let params = {
        url: 'sys/generator/list',
        showLoading: false,
        data: {
          page: 1,
          limit: 1000
        },
        scb: (response) => {
          this.allTables = response.page.list
        }
      }
      ajax(params)
    },

    //step3
  },
  created(){
    this.loadData();
  }
}
