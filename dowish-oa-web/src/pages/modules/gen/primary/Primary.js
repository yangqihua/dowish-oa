/**
 * Created by yangqihua on 2017/7/10.
 */

import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'
export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {

      activeStep: 1,
      //list部分
      searchKey: '',
      pagination: {
        total: 0,
        pageNo: 1,
        pageSize: 10,
      },
      rows: [],

      allTables:[],

      menuTree:[],
      cascaderProps: {
        value: 'menuId',
        label: 'name',
        children: 'list',
      },
      parentMenuIds:[],


      stepTitle:'选择原生数据表',
      genTable:{},
      genConfig:{},

      // step3
      msgType:'success',
      successMsg:'',
      genTitle:'代码生成提示',
    }
  },
  methods: {

    // step1
    handleMapper(index, row){
      let params = {
        url: 'sys/generator/mapper',
        data: {
          tableName: row.tableName,
        },
        scb: (response) => {
          this.genTable = response.genTable
          this.genConfig = response.genConfig

          this.getAllTable()
          this.loadMenuList()
          this.next()
        }
      }
      ajax(params)
    },
    next(){
      if (this.activeStep < 4) {
        this.activeStep++
      }
      if(this.activeStep==2){
        this.stepTitle='配置数据表到代码Mapper'
      }else if(this.activeStep==3){
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
      this.genTable.parentMenuId = this.parentMenuIds[this.parentMenuIds.length-1]
      let params = {
        url: 'sys/generator/code',
        type:'post',
        data: this.genTable,
        scb: (response) => {
          this.msgType = response.status==1?'success':'warning'
          this.genTitle = response.status==1?'生成成功，请注意提示信息最后四行！':'代码文件已存在,如需重新生成，请手动删除原文件！'
          this.successMsg = response.msg
          this.next();
        }
      }
      ajax(params)
    },
    backStep(){
      this.activeStep--
      if(this.activeStep==2){
        this.stepTitle='配置数据表到代码Mapper'
      }else if(this.activeStep==3){
        this.stepTitle = '生成代码完成';
      }else{
        this.stepTitle = '选择原生数据表';
      }
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
    loadMenuList(){
      let params = {
        url: '/sys/menu/perms',
        showLoading: false,
        scb: (res) => {
          this.menuTree = this.menuTree = stringUtils.arrayToTree(res.menuList, {
            id: 'menuId',
            parentId: 'parentId',
            childrenKey: 'list'
          })

          //父级菜单默认选项
          this.parentMenuIds.push(this.genTable.parentMenuId)
          let pid = []
          pid = stringUtils.setParentId(this.genTable.parentMenuId)
          pid.forEach(p=>{
            this.parentMenuIds.push(p)
          })
        }
      }
      ajax(params)
    },

    //step3
    goStep1(){
      this.activeStep =1;
      this.stepTitle = '选择原生数据表';
    },

  },
  created(){
    this.loadData();
  }
}
