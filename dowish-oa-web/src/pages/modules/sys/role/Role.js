
import panel from "../../../../components/panel.vue"
import selectTree from "../../../../components/selectTree.vue"
import treeter from "../../../../components/treeter"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'

export default {
  mixins: [treeter],
  components: {
    'imp-panel': panel,
    'el-select-tree': selectTree,
  },
  data(){
    return {
      title: '新增',
      dialogLoading: false,
      dialogVisible: false,
      formLabelWidth: '100px',

      roleTree: [],
      resourceTree: [],
      maxId: 700000,


      roleList: [],

      //tree相关
      defaultExpandedKeys: [],
      defaultCheckedKeys: [],
      menuList: [],
      defaultProps: {
        children: 'list',
        label: 'name',
        id: "menuId",
      },

      roleInfo: {},
      activeRole: {},

      menuIdList: [],
      form: {
        createTime: null,
        createUserId: null,
        menuIdList: [],
        remark: null,
        roleId: null,
        roleName: null,
      }
    }
  },
  methods: {
    configRoleResources(){
      let checkedKeys = this.$refs.resourceTree.getCheckedKeys();
      this.$http.get(api.SYS_SET_ROLE_RESOURCE + "?roleId=" + this.form.id + "&resourceIds=" + checkedKeys.join(','))
        .then(res => {
          this.$message('修改成功');
          this.dialogVisible = false;
        })
    },
    newAdd(){
      stringUtils.resetObject(this.form)
      this.menuIdList = []
      this.$refs.roleTree.setCheckedKeys(this.menuIdList)
      this.activeRole = {}
      this.title = '新增'
    },

    deleteRole(id){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          url: 'sys/role/delete',
          type: 'post',
          data: [this.form.roleId],
          scb: (res) => {
            this.$message.success('删除成功');
            this.newAdd()
            this.loadData()
          }
        }
        ajax(params)
      }).catch(()=>{})
    },

    handleNodeClick(data){
      this.form = data;
      console.log("data = ", data)
    },
    settingResource(event, id){
      event.stopPropagation();
      this.dialogVisible = true;
      if (this.resourceTree == null || this.resourceTree.length <= 0) {
        this.dialogLoading = true;
        this.$http.get(api.TEST_DATA)
          .then(res => {
            this.dialogLoading = false;
            this.resourceTree = res.data.resourceList;
          }).catch((error) => {
          console.log(error)
          this.dialogLoading = false;
        })
      }
      this.$http.get(api.SYS_ROLE_RESOURCE + "?id=" + id)
        .then(res => {
          this.$refs.resourceTree.setCheckedKeys(res.data);
        })
    },

    onUpdate(){
      let leafMenuIdList = this.$refs.roleTree.getCheckedKeys()
      let menuIdList = new Set()
      let rootMenu = {list: this.menuList, menuId: -1}
      let path = new Set()
      leafMenuIdList.forEach(leafMenuId => {
        this.setParentMenuId(leafMenuId, rootMenu, path)
        path.delete(-1)  //构造的root节点要删除掉
        path.forEach(p => menuIdList.add(p))
        path.clear()
      })
      this.form['menuIdList'] = menuIdList
      let params = {
        url: 'sys/role/update',
        type: 'post',
        data: this.form,
        scb: (res) => {
          this.$message.success('修改成功');
        }
      }
      ajax(params)

    },

    onSubmit(){
      let leafMenuIdList = this.$refs.roleTree.getCheckedKeys()
      let menuIdList = new Set()
      let rootMenu = {list: this.menuList, menuId: -1}
      let path = new Set()
      leafMenuIdList.forEach(leafMenuId => {
        this.setParentMenuId(leafMenuId, rootMenu, path)
        path.delete(-1)  //构造的root节点要删除掉
        path.forEach(p => menuIdList.add(p))
        path.clear()
      })
      this.form['menuIdList'] = menuIdList
      let params = {
        url: 'sys/role/save',
        type: 'post',
        data: this.form,
        scb: (res) => {
          this.$message.success('添加成功');
          this.loadData()
        }
      }
      ajax(params)

    },

    loadData(){
      let params = {
        url: 'sys/role/list',
        data: {
          page: 1, limit: 100
        },
        scb: (response) => {
          this.roleList = response.page.list
        }
      }
      ajax(params)
    },
    loadMenuList(){
      let params = {
        url: 'sys/menu/perms',
        showLoading: false,
        scb: (response) => {
          this.menuList = response.menuList
          if (this.menuList.length > 0) {
            this.defaultExpandedKeys.push(this.menuList[0].menuId)
          }
        }
      }
      ajax(params)
    },
    loadRoleInfo(role){
      this.title = '加载中...'
      this.activeRole = role
      let params = {
        url: 'sys/role/info/' + role.roleId,
        showLoading: false,
        scb: (response) => {
          this.title = '修改'
          this.form = response.role
          this.menuIdList = [];
          this.form.menuIdList.forEach(menuId => {
            if (!this.isParentMenuId(menuId, this.menuList)) {
              this.menuIdList.push(menuId)
            }
          })
          this.$refs.roleTree.setCheckedKeys(this.menuIdList);
        }
      }
      ajax(params)
    },
    isParentMenuId(menuId, menuList){
      for (let key in menuList) {
        if (menuList[key].parentId == menuId) {
          return true
        } else if (menuList[key].list != null) {
          return this.isParentMenuId(menuId, menuList[key].list);
        } else {
          return false
        }
      }
    },
    setParentMenuId(menuId, rootMenu, path = new Set()){
      path.add(rootMenu.menuId)
      if (rootMenu.menuId == menuId) {
        return true
      }
      if (rootMenu.list != null) {
        for (let key in rootMenu.list) {
          if (this.setParentMenuId(menuId, rootMenu.list[key], path)) {
            return true
          }
        }
      }
      path.delete(rootMenu.menuId)
      return false
    },
  },
  created(){
    this.loadData();
    this.loadMenuList();
  }
}
