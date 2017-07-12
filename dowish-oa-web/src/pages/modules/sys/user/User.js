/**
 * Created by yangqihua on 2017/7/10.
 */


import * as api from "../../../../utils/api"
import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'
//深拷贝
import merge from 'element-ui/src/utils/merge';

export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {
      //list部分

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

      resetPwdFormVisible:false,
      pwdForm:{
        userId:'',
        password:'',
        newPassword:'',
      },

      // edit 部分
      roleList: [],
      boolStatus: true,
      form: {
        userId: null,
        roleName: '',
        email: '',
        mobile: '',
        status: '',
        roleIdList: [],
        password: null
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, message: '用户名大于 3 个字符', trigger: 'blur'}
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
    handleResetPwd(index,row){
      this.pwdForm.userId = row.userId
      this.resetPwdFormVisible = true
    },

    addNew(){
      this.showList = !this.showList;
      this.resetForm()
      this.loadRoleList()
      console.log('boolStatus :=======', this.boolStatus)
    },

    handleSelectionChange(val){

    },
    handleSizeChange(val) {
      this.tableData.pagination.pageSize = val;
      this.loadData();
    },
    handleCurrentChange(val) {
      this.tableData.pagination.pageNo = val;
      this.loadData();
    },


    handleEdit(index, row){
      this.showList = !this.showList
      let params = {
        url: 'sys/user/info/' + row.userId,
        showLoading: false,
        scb: (response) => {
          this.form = merge({}, response.user)
          this.boolStatus = response.user.status === 1
          this.loadRoleList()
        }
      }
      ajax(params)

    },
    handleDelete(index, row){
      this.$http.get(api.SYS_USER_DELETE + "?userIds=" + row.id).then(res => {
        this.loadData();
      });
    },
    loadData(){
      let params = {
        url: 'sys/user/list',
        data: {
          username: this.searchKey,
          page: this.tableData.pagination.pageNo,
          limit: this.tableData.pagination.pageSize
        },
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
          console.log("response", response)
          this.roleList = response.list
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
          let params = {
            url: 'sys/user/save?password='+this.form.password,
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
    }
  },
  created(){
    this.loadData();
  }
}
