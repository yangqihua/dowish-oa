/**
 * Created by yangqihua on 2017/7/10.
 */
import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'
//深拷贝
import merge from 'element-ui/src/utils/merge';
import {mapGetters, mapActions, mapMutations} from 'vuex'
export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {
      //list部分
      //显示列表时为true，显示form时为false
      showList: true,
      // 搜索条件
      searchForm: {
        id:'',
        name:'',
        address:'',
        createUserId:'',
      },
      // 表格相关数据
      tableData: {
        pagination: {
          total: 0,
          page:1,     // 传给后台的参数：第page页，从1开始
          limit:10,   // 传给后台的参数：每页多少条
        },
        rows: [],
        multipleSelection: [],
      },

      // edit 部分
      form: {
        id:'',
        name:'',
        address:'',
        createUserId:'',
      },

      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
        ],
        address: [
          {required: true, message: '请输入地址', trigger: 'blur'},
        ],
      },
    }
  },
  methods: {
    onEdit(){
      this.showList = !this.showList
      let params = {
        url: 'student/student/info/' + this.tableData.multipleSelection[0].id,
        showLoading: false,
        scb: (response) => {
          this.form = merge({}, response.student)
        }
      }
      ajax(params)
    },
    onDelete(){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let studentIds = this.tableData.multipleSelection.map(item => {
          return item.id
        })
        let params = {
          url: 'student/student/delete',
          type: 'post',
          data: studentIds,
          showLoading: false,
          scb: (response) => {
            this.loadStudentList()
          }
        }
        ajax(params)
      }).catch(() => {
      })
    },
    onAdd(){
      this.showList = !this.showList;
      this.resetForm()
    },

    loadStudentList(){
      this.searchForm.page = this.tableData.pagination.page
      this.searchForm.limit = this.tableData.pagination.limit
      let params = {
        url: 'student/student/list',
        data: this.searchForm,
        showLoading: false,
        scb: (response) => {
          this.tableData.rows = response.page.list
          this.tableData.pagination.total = response.page.totalCount
        }
      }
      ajax(params)
    },

    //edit部分
    onSubmit(){
      this.$refs['form']['validate'](v=>{
        if (v) {
          this.form["createUserId"] = this.user.userId
          let params = {
            url: 'student/student/save',
            type: 'post',
            data: this.form,
            scb: (res) => {
              this.$message.success('新增成功')
              this.loadStudentList()
              this.showList = !this.showList
            }
          }
          ajax(params)
        } else {}
      })
    },
    onUpdate(){
      this.$refs['form']['validate'](v => {
        if (v) {
          let params = {
            url: 'student/student/update',
            type: 'post',
            data: this.form,
            scb: (res) => {
              this.$message.success('更新成功')
              this.loadStudentList()
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
    },
    onSelectionChange(val){
      this.tableData.multipleSelection = val
    },
    onSizeChange(val) {
      this.tableData.pagination.limit = val;
      this.loadStudentList();
    },
    onCurrentChange(val) {
      this.tableData.pagination.page = val;
      this.loadStudentList();
    },
  },
  computed: {
    ...mapGetters({
      user: 'user', userPerms: 'permissions'
    }),
    permissions(){
      return {
        list: stringUtils.hasPermission(this.userPerms, "student:student:list"),
        add: stringUtils.hasPermission(this.userPerms, "student:student:save"),
        edit: stringUtils.hasPermission(this.userPerms, "student:student:info")
        && stringUtils.hasPermission(this.userPerms, "student:student:update"),
        delete: stringUtils.hasPermission(this.userPerms, "student:student:delete"),
      }
    },
    btnEnable(){
      return {
        edit: !(this.tableData.multipleSelection.length == 1),
        delete: !(this.tableData.multipleSelection.length >= 1),
      }
    },
  },
  created(){
    this.loadStudentList();
  }
}
