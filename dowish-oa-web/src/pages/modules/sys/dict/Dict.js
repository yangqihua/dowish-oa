/**
 * Created by yangqihua on 2017/7/10.
 */


import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'
//深拷贝
import merge from 'element-ui/src/utils/merge';
import {mapGetters, mapActions, mapMutations} from 'vuex'
import perms from '../../../../config/permissions'
export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {
      //list部分
      multipleSelection: [],

      searchForm: {
        key: '',
        value: '',
      },
      showList: true,  //显示用户列表div
      tableData: {
        pagination: {
          total: 0,
          pageNo: 1,
          pageSize: 10,
        },
        rows: []
      },

      // edit 部分
      form: {
        key: null,
        value: null,
        remark:null,
      },
      rules: {
        key: [
          {required: true, message: '参数名称不能为空', trigger: 'blur'},
          {min: 1, message: '参数名称大于 1 个字符', trigger: 'blur'}
        ],
      },

    }
  },
  methods: {

    handleEdit(){
      this.form = merge({},this.multipleSelection[0]);
      this.showList = !this.showList
    },
    handleDelete(){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = this.multipleSelection.map(item => {
          return item.id
        })
        let params = {
          url: '/sys/dict/delete',
          type: 'post',
          data: ids,
          scb: (response) => {
            this.loadData()
          }
        }
        ajax(params)
      }).catch(() => {
      })
    },
    addNew(){
      this.showList = !this.showList;
      this.resetForm()
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    handleSizeChange(val) {
      this.tableData.pagination.pageSize = val;
      this.loadData();
    },
    handleCurrentChange(val) {
      this.tableData.pagination.pageNo = val;
      this.loadData();
    },

    loadData(){
      this.searchForm.page = this.tableData.pagination.pageNo
      this.searchForm.limit = this.tableData.pagination.pageSize
      let params = {
        url: '/sys/dict/list',
        data: this.searchForm,
        scb: (response) => {
          this.tableData.rows = response.page.list
          this.tableData.pagination.total = response.page.totalCount
        }
      }
      ajax(params)
    },
    //edit部分

    //save
    onSubmit(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          let params = {
            url: '/sys/dict/save',
            type: 'post',
            data: this.form,
            scb: (res) => {
              this.$message.success('新增成功')
              this.loadData()
              this.showList = !this.showList
            }
          }
          ajax(params)
        } else {}
      })
    },
    //edit
    onUpdate(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          let params = {
            url: '/sys/dict/update',
            type: 'post',
            data: this.form,
            scb: (res) => {
              this.$message.success('更新成功')
              this.loadData()
              this.showList = !this.showList
            }
          }
          ajax(params)
        } else {}
      })
    },
    resetForm() {
      stringUtils.resetObject(this.form)
    },
    resetSearchForm() {
      stringUtils.resetObject(this.searchForm)
    }
  },
  computed: {
    ...mapGetters({
      user:'user',userPerms:'permissions'
    }),
    permissions(){
      return {
        list:     stringUtils.hasPermission(this.userPerms,perms.SYS_DICT_LIST),
        add:      stringUtils.hasPermission(this.userPerms,perms.SYS_DICT_SAVE),
        edit:     stringUtils.hasPermission(this.userPerms,perms.SYS_DICT_UPDATE),
        delete:   stringUtils.hasPermission(this.userPerms,perms.SYS_DICT_DELETE),

      }
    },
    btnEnable(){
      return {
        edit: !(this.multipleSelection.length == 1),
        multi: !(this.multipleSelection.length >= 1),
      }
    },
  },
  created(){
    this.loadData();
  },
  watch:{
    showList(newVal,oldVal) {
      console.log("new :",newVal)
      if(newVal){
        this.multipleSelection = []
      }
    }
  }
}
