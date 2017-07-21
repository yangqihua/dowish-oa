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
        username: '',
        email: '',
        mobile: '',
        deptName: '',
        status: '',
      },

      showList: true,  //显示用户列表div
      searchKey: '',  //搜索的用户名
      tableData: {
        pagination: {
          total: 0,
          pageNo: 1,
          pageSize: 10,
        },
        rows: []
      },

      resetPwdFormVisible: false,
      pwdForm: {
        userId: '',
        password: '',
        newPassword: '',
      },

      // edit 部分
      roleList: [],

      //级联选择数据源
      deptTree: [],

      //级联选择属性定义
      cascaderProps: {
        value: 'deptId',
        label: 'name',
        children: 'list',
      },

      boolStatus: true,

      form: {
        userId: null,
        roleName: '',
        email: '',
        mobile: '',
        status: '',
        roleIdList: [],
        password: null,
        parentIds: [],
        deptId: '',
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 2, message: '用户名大于 2 个字符', trigger: 'blur'}
        ],
      },
      resetPwdRules: {
        newPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, message: '用户名大于 3 个字符', trigger: 'blur'}
        ],
      }

    }
  },
  methods: {
    handleResetPwd(){
      this.pwdForm.userId = this.multipleSelection[0].userId
      this.resetPwdFormVisible = true
    },
    handleEdit(){
      this.showList = !this.showList
      let params = {
        url: 'sys/user/info/' + this.multipleSelection[0].userId,
        showLoading: false,
        scb: (response) => {
          this.form = merge({}, response.user)
          this.boolStatus = response.user.status === 1
          this.loadDeptList()
        }
      }
      ajax(params)

    },
    handleDelete(){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let userIds = this.multipleSelection.map(item => {
          return item.userId
        })
        let params = {
          url: 'sys/user/delete',
          type: 'post',
          data: userIds,
          loadingDom: 'userTableId',
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
      this.loadDeptList()
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
        url: 'sys/user/list',
        data: this.searchForm,
        loadingDom: 'userTableId',
        scb: (response) => {
          this.tableData.rows = response.page.list
          this.tableData.pagination.total = response.page.totalCount
        }
      }
      ajax(params)
    },

    loadRoleList(){
      let params = {
        url: 'sys/role/select',
        showLoading: false,
        scb: (response) => {
          this.roleList = response.list
        }
      }
      ajax(params)
    },

    loadDeptList(){
      let params = {
        url: '/sys/dept/select',
        showLoading: false,
        scb: (res) => {

          this.deptTree = stringUtils.arrayToTree(res.deptList, {
            id: "deptId",
            parentId: "parentId",
            childrenKey: "list"
          })
          // 把空属性去掉，不然获取不到叶子节点的值
          stringUtils.deleteEmptyProperty(this.deptTree)
          let path = new Set()
          stringUtils.addParentId(this.form.deptId, "deptId", "parentId", this.deptTree, path)
          this.form.parentIds = Array.from(path)
          this.loadRoleList()
        }
      }
      ajax(params)
    },

    //edit部分

    //save
    onSubmit(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.form.status = this.boolStatus ? '1' : '0'
          this.form.deptId = this.form.parentIds[this.form.parentIds.length - 1]
          this.form["createUserId"] = this.user.userId
          let params = {
            url: 'sys/user/save?password=' + this.form.password,
            type: 'post',
            data: this.form,
            scb: (res) => {
              this.$message.success('新增成功')
              this.loadData()
              this.showList = !this.showList
            }
          }
          ajax(params)
        } else {
        }
      })
    },
    //edit
    onUpdate(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.form.status = this.boolStatus ? '1' : '0'
          this.form.deptId = this.form.parentIds[this.form.parentIds.length - 1]
          let params = {
            url: 'sys/user/update',
            type: 'post',
            data: this.form,
            scb: (res) => {
              this.$message.success('更新成功')
              this.loadData()
              this.showList = !this.showList
            }
          }
          ajax(params)
        } else {
        }
      })
    },
    onResetPwd(){
      this.$refs['resetPwdForm'].validate((valid) => {
        if (valid) {
          this.resetPwdFormVisible = false
          let params = {
            url: 'sys/user/password',
            type: 'post',
            data: this.pwdForm,
            scb: (res) => {
              this.$message.success('更新成功')
              this.loadData()
            }
          }
          ajax(params)
        } else {
        }
      })
    },
    resetForm() {
      this.boolStatus = true
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
        list: stringUtils.hasPermission(this.userPerms,perms.SYS_USER_LIST),
        add: stringUtils.hasPermission(this.userPerms,perms.SYS_DEPT_SELECT)
        && stringUtils.hasPermission(this.userPerms,perms.SYS_ROLE_SELECT)
        && stringUtils.hasPermission(this.userPerms,perms.SYS_USER_SAVE),
        edit: stringUtils.hasPermission(this.userPerms,perms.SYS_USER_INFO)
        && stringUtils.hasPermission(this.userPerms,perms.SYS_ROLE_SELECT)
        && stringUtils.hasPermission(this.userPerms,perms.SYS_DEPT_SELECT)
        && stringUtils.hasPermission(this.userPerms,perms.SYS_USER_UPDATE),
        resetPwd: stringUtils.hasPermission(this.userPerms,perms.USER_RESET_PWD),
        delete: stringUtils.hasPermission(this.userPerms,perms.SYS_USER_DELETE),
      }
    },
    btnEnable(){
      return {
        edit: !(this.multipleSelection.length == 1),
        delete: !(this.multipleSelection.length >= 1),
        resetPwd: !(this.multipleSelection.length == 1),
      }
    },
  },
  created(){
    this.loadData();
  }
}
