
import panel from "../../../../components/panel.vue"
import selectTree from "../../../../components/selectTree.vue"
import treeter from "../../../../components/treeter"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'
import {mapGetters, mapActions, mapMutations} from 'vuex'
import perms from '../../../../config/permissions'

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

      //menuTree相关
      menuTreeExpandedKeys: [],
      menuTreeCheckedKeys: [],
      menuTree: [],
      menuProps: {
        children: 'list',
        label: 'name',
        id: "menuId",
      },

      //deotTree相关
      deptTreeExpandedKeys: [],
      deptTreeCheckedKeys: [],
      deptTree: [],
      deptProps: {
        children: 'list',
        label: 'name',
        id: "deptId",
      },

      activeRole: {},

      form: {
        createTime: null,
        createUserId: null,
        menuIdList: [],
        deptIdList: [],
        remark: null,
        roleId: null,
        roleName: null,
      }
    }
  },
  methods: {
    renderContent(h, {node, data, store}) {
      return (
        <span>
        <span>
        <span><i class={data.icon}></i>&nbsp;{node.label}</span>
      </span>
      </span>);
    },
    newAdd(){
      stringUtils.resetObject(this.form)
      this.$refs.menuTree.setCheckedKeys([])
      this.$refs.deptTree.setCheckedKeys([])
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

      this.$confirm('确定修改数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let leafMenuIdList = this.$refs.menuTree.getCheckedKeys()
        let menuIdList = new Set()
        let path = new Set()
        leafMenuIdList.forEach(leafMenuId => {
          stringUtils.addParentId(leafMenuId,"menuId","parentId", this.menuTree, path)
          path.forEach(p => menuIdList.add(p))
          path.clear()
        })
        this.form['menuIdList'] = menuIdList  //加上父菜单

        this.form['deptIdList'] = this.$refs.deptTree.getCheckedKeys()
        let params = {
          url: 'sys/role/update',
          type: 'post',
          data: this.form,
          scb: (res) => {
            this.$message.success('修改成功');
          }
        }
        ajax(params)
      }).catch(()=>{})

    },

    onSubmit(){
      let leafMenuIdList = this.$refs.menuTree.getCheckedKeys()
      let menuIdList = new Set()
      let path = new Set()
      leafMenuIdList.forEach(leafMenuId => {
        stringUtils.addParentId(leafMenuId,"menuId","parentId", this.menuTree, path)
        path.forEach(p => menuIdList.add(p))
        path.clear()
      })
      this.form['menuIdList'] = menuIdList  //加上父菜单

      this.form['deptIdList'] = this.$refs.deptTree.getCheckedKeys()

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
          this.menuTree = stringUtils.arrayToTree(response.menuList,{id:'menuId',parentId:'parentId',childrenKey:'list'})
          if (this.menuTree.length > 0) {
            this.menuTreeExpandedKeys.push(this.menuTree[0].menuId)
          }
        }
      }
      ajax(params)
    },
    loadDeptList(){
      let params = {
        url: 'sys/dept/list',
        showLoading: false,
        scb: (response) => {
          this.deptTree = stringUtils.arrayToTree(response.deptList,{id:'deptId',parentId:'parentId',childrenKey:'list'})
          if (this.deptTree.length > 0) {
            this.deptTreeExpandedKeys.push(this.deptTree[0].deptId)
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
          let menuIdList = []
          this.form.menuIdList.forEach(menuId => {
            if (!stringUtils.isParentId(menuId,"menuId","parentId", this.menuTree)) {
              menuIdList.push(menuId)
            }
          })
          this.form.menuIdList = menuIdList
          this.$refs.menuTree.setCheckedKeys(this.form.menuIdList);

          // 部门tree相关
          let deptIdList = []
          response.role.deptIdList.forEach(deptId => {
            if (!stringUtils.isParentId(deptId,"deptId","parentId", this.deptTree)) {
              deptIdList.push(deptId)
            }
          })
          this.form.deptIdList = deptIdList
          this.$refs.deptTree.setCheckedKeys(this.form.deptIdList);
        }
      }
      ajax(params)
    },
  },
  computed: {
    ...mapGetters({
      user: 'user', userPerms: 'permissions'
    }),
    permissions(){
      return {
        list: stringUtils.hasPermission(this.userPerms, perms.SYS_ROLE_LIST),
        add: stringUtils.hasPermission(this.userPerms, perms.SYS_DEPT_LIST)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_PERMS)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_ROLE_SAVE),
        edit: stringUtils.hasPermission(this.userPerms, perms.SYS_ROLE_INFO)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_PERMS)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_DEPT_LIST)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_ROLE_UPDATE),
        delete: stringUtils.hasPermission(this.userPerms, perms.SYS_ROLE_DELETE),
      }
    },
  },
  created(){
    this.loadData();
    this.loadMenuList();
    this.loadDeptList();
  }
}
