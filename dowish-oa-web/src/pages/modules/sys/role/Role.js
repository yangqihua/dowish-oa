
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
    newAdd(){
      stringUtils.resetObject(this.form)
      this.menuIdList = []
      this.$refs.roleTree.setCheckedKeys(this.menuIdList)
      this.activeRole = {}
      this.title = '新增'
    },

    deleteRole(){
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

    onUpdate(){
      let leafMenuIdList = this.$refs.roleTree.getCheckedKeys()
      let menuIdList = new Set()
      let rootMenu = {list: this.menuList, menuId: -1}
      let path = new Set()
      leafMenuIdList.forEach(leafMenuId => {
        stringUtils.setParentId(leafMenuId,"menuId","parentId", rootMenu, path)
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
        stringUtils.setParentId(leafMenuId,"menuId","parentId", rootMenu, path)
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
        showLoading:false,
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
            if (!stringUtils.isParentId(menuId,"menuId","parentId", this.menuList)) {
              this.menuIdList.push(menuId)
            }
          })
          this.$refs.roleTree.setCheckedKeys(this.menuIdList);
        }
      }
      ajax(params)
    },
  },
  created(){
    this.loadData();
    this.loadMenuList();
  }
}
