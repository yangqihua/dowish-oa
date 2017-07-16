/**
 * Created by yangqihua on 2017/7/10.
 */

import panel from "../../../../components/panel.vue"
import treeter from "../../../../components/treeter"

//深拷贝
import merge from 'element-ui/src/utils/merge';

import * as api from "../../../../utils/api"

import icons from "../../../../config/icon"
import ajax from "../../../../utils/ajax/ajax"
import stringUtils from '../../../../utils/string-utils.js'


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
        id: "deptId",
      },
      maxId: 7000000,


      cascaderProps: {
        value: 'deptId',
        label: 'name',
        children: 'list',
      },

      options: {},
      title: '新增',
      defaultExpandedKeys: [],
      deptTree: [],
      form: {
        parentIds: [],
        icon: null,
        list: null,
        deptId: null,
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
        <span><i class={data.icon}></i>&nbsp;{node.label}</span>
      </span>
      </span>);
    },
    newAdd(){
      stringUtils.resetObject(this.form)
      this.title = "新增"
    },
    deleteDept(){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          url: 'sys/dept/delete',
          type: 'post',
          data: {deptId: this.form.deptId},
          scb: (res) => {
            this.$message.success('删除成功')
            this.newAdd()
            this.loadData()
          }
        }
        ajax(params)
      }).catch(() => {})
    },
    handleNodeClick(data){
      this.form = merge({}, data);
      let rootDept = {list: this.deptTree, deptId: -1}
      let path = new Set()
      stringUtils.setParentDeptId(this.form.parentId, rootDept, path)
      path.delete(-1)  //构造的root节点要删除掉
      this.form.parentIds = Array.from(path)

      this.title = "修改"
    },
    onUpdate(){
      if (this.form.parentIds.length > 0) {
        this.form.parentId = this.form.parentIds[this.form.parentIds.length - 1]
      }
      let params = {
        url: 'sys/dept/update',
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
        url: 'sys/dept/save',
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
        url: '/sys/dept/list',
        showLoading: false,
        scb: (res) => {
          this.deptTree = stringUtils.arrayToTree(res.deptList,{id:"deptId",parentId:"parentId",childrenKey:"list"})
          console.log(this.deptTree);
          if (this.deptTree.length > 0) {
            this.defaultExpandedKeys.push(this.deptTree[0].deptId)
          }
        }
      }
      ajax(params)
    }
  },

  computed: {
    icons(){
      return icons
    }
  },
  created(){
    this.loadData();
  }
}
