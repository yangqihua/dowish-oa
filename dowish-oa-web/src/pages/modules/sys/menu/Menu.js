/**
 * Created by yangqihua on 2017/7/10.
 */

import panel from "../../../../components/panel.vue"
import treeter from "../../../../components/treeter"

//深拷贝
import merge from 'element-ui/src/utils/merge';

import icons from "../../../../config/icon"
import ajax from "../../../../utils/ajax/ajax"
import stringUtils from '../../../../utils/string-utils.js'
import {mapGetters, mapActions, mapMutations} from 'vuex'
import perms from '../../../../config/permissions'


export default {
  mixins: [treeter],
  components: {
    'content-panel': panel,
  },
  data(){
    return {
      selectIconDialog: false,
      formLabelWidth: '100px',
      defaultProps: {
        children: 'list',
        label: 'name',
        id: "menuId",
      },

      cascaderProps: {
        value: 'menuId',
        label: 'name',
        children: 'list',
      },

      options: {},
      title: '新增',
      defaultExpandedKeys: [],
      menuTree: [],
      form: {
        parentIds: [],
        icon: null,
        list: null,
        menuId: null,
        name: '',
        open: null,
        orderNum: null,
        parentId: null,
        parentName: null,
        perms: null,
        type: null,
        url: null,
      }
    }
  },
  methods: {
    selectIcon(event){
      this.form.icon = event.target.className;
      this.selectIconDialog = false;
    },
    renderContent(h, {node, data, store}) {
      return (
        <span>
        <span>
        <span> <i class= {data.icon}></i> &nbsp;{node.label} </span >
        </span>
        </span>
      );
    },
    newAdd(){
      stringUtils.resetObject(this.form)
      this.title = "新增"
    },
    deleteMenu(){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          url: 'sys/menu/delete',
          type: 'post',
          data: {menuId: this.form.menuId},
          scb: (res) => {
            this.$message.success('删除成功')
            this.newAdd()
            this.loadData()
          }
        }
        ajax(params)
      }).catch(() => {
      })
    },
    handleNodeClick(data){
      this.form = merge({}, data);
      let path = new Set()
      stringUtils.addParentId(this.form.parentId, "menuId", "parentId", this.menuTree, path)
      this.form.parentIds = Array.from(path)

      this.title = "修改"
    },
    onUpdate(){
      if (this.form.parentIds.length > 0) {
        this.form.parentId = this.form.parentIds[this.form.parentIds.length - 1]
      }
      let params = {
        url: 'sys/menu/update',
        type: 'post',
        data: this.form,
        scb: (res) => {
          this.$message.success('修改成功')
          this.loadData()
        }
      }
      ajax(params)
    },
    onSubmit(){
      if (this.form.parentIds.length > 0) {
        this.form.parentId = this.form.parentIds[this.form.parentIds.length - 1]
      }
      let params = {
        url: 'sys/menu/save',
        type: 'post',
        data: this.form,
        scb: (res) => {
          this.$message.success('创建成功')
          this.loadData()
        }
      }
      ajax(params)
    },
    loadData(){
      let params = {
        url: '/sys/menu/perms',
        showLoading: false,
        scb: (res) => {
          this.menuTree = this.menuTree = stringUtils.arrayToTree(res.menuList, {
            id: 'menuId',
            parentId: 'parentId',
            childrenKey: 'list'
          })
          if (this.menuTree.length > 0) {
            this.defaultExpandedKeys.push(this.menuTree[0].menuId)
          }
        }
      }
      ajax(params)
    }
  },

  computed: {
    ...mapGetters({
      user: 'user', userPerms: 'permissions'
    }),
    permissions(){
      return {
        list: stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_PERMS),
        add: stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_PERMS)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_SAVE),
        edit: stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_PERMS)
        && stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_UPDATE),
        delete: stringUtils.hasPermission(this.userPerms, perms.SYS_MENU_DELETE),
      }
    },
    icons(){
      return icons
    }
  },

  created(){
    this.loadData();
  }
}
