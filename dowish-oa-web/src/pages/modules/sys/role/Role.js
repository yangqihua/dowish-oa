/**
 * Created by yangqihua on 2017/7/10.
 */


import * as api from "../../../../utils/api"
import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'

export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {
      //list部分
      currentRow: {},
      dialogVisible: false,
      dialogLoading: false,
      defaultProps: {
        children: 'children',
        label: 'name',
        id: "id",
      },
      roleTree: [],


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

      // edit 部分
      form: {
        id: null,
        username: '',
        email: '',
        mobile: '',
        status: '',
        boolStatus: false,
        userType: '1',
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, message: '用户名大于 3 个字符', trigger: 'blur'}
        ],
      }

    }
  },
  methods: {

    handleSelectionChange(val){

    },
    handleRoleConfig(index, row){
      this.currentRow = row;
      this.dialogVisible = true;
      if (this.roleTree.length <= 0) {
        this.$http.get(api.TEST_DATA + "?selectChildren=true")
          .then(res => {
            this.roleTree = res.data.roleList
          })
      }
      this.$http.get(api.SYS_USER_ROLE + "?id=" + row.id)
        .then(res => {
          this.$refs.roleTree.setCheckedKeys(res.data);
        })
    },
    configUserRoles(){
      let checkedKeys = this.$refs.roleTree.getCheckedKeys();
      this.$http.get(api.SYS_SET_USER_ROLE + "?userId=" + this.currentRow.id + "&roleIds=" + checkedKeys.join(','))
        .then(res => {
          this.$message('修改成功');
          this.dialogVisible = false;
        })
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
      this.form = row
      this.form['boolStatus'] = this.form.status === 1
      // this.$router.push({path: 'userAdd', query: {id: row.id}})

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
          console.log("response", response)
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
          this.form.status = this.form.boolStatus==true?'1':'0'
          console.log(this.form)
          // this.$http.post(api.SYS_USER_ADD, this.form)
          //   .then(res => {
          //     this.form = res.data;
          //     this.$confirm('添加成功, 是否返回列表?', '提示', {
          //       confirmButtonText: '确定',
          //       cancelButtonText: '取消',
          //       type: 'success'
          //     }).then(() => {
          //       this.$router.push({path: 'userList'})
          //     })
          //   })
        } else {

        }
      })
    },
    //edit
    onEditSubmit(){
      this.$http.post(api.SYS_USER_UPDATE, this.form)
        .then(res => {
          this.form = res.data;
          this.$confirm('修改成功, 是否返回列表?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'success'
          }).then(() => {
            this.$router.push({path: 'userList'})
          })
        })
    },
    resetForm() {
      console.log('改变前：',this.form);
      stringUtils.resetObject(this.form)
      console.log('改变后：',this.form);
    }
    // loadData(){
    //   if (this.$route.query && this.$route.query != null && this.$route.query.id && this.$route.query.id != null) {
    //     this.form.id = this.$route.query.id;
    //     this.$http.get(api.SYS_USER_GET + "?id=" + this.form.id)
    //       .then(res => {
    //         this.form = res.data;
    //       })
    //   }
    // }
  },
  created(){
    this.loadData();
  }
}
