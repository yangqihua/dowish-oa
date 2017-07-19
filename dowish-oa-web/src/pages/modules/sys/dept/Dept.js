/**
 * Created by yangqihua on 2017/7/10.
 */

import panel from "../../../../components/panel.vue"
import treeter from "../../../../components/treeter"

//深拷贝
import merge from 'element-ui/src/utils/merge';

import ajax from "../../../../utils/ajax/ajax"
import stringUtils from '../../../../utils/string-utils.js'


export default {
  mixins: [treeter],
  components: {
    'content-panel': panel,
  },
  data(){
    return {
      //标题
      title: '新增',
      //树结构属性定义
      defaultProps: {
        children: 'list',
        label: 'name',
        id: "deptId",
      },
      //树结构默认展开的key，默认设置为数组的第一个
      defaultExpandedKeys: [],

      //级联选择属性定义
      cascaderProps: {
        value: 'deptId',
        label: 'name',
        children: 'list',
      },

      //级联选择数据源
      deptTree: [],

      //右边的属性表单
      form: {
        //父级的idList
        parentIds: [],
        list: null,
        deptId: null,
        name: '',
        orderNum: null,
        parentId: null,
      }
    }
  },
  methods: {
    //树的渲染方式
    renderContent(h, {node, data, store}) {
      return (
        <span>
        <span>
        <span>&nbsp;{node.label}</span>
        </span>
        </span>);
    },
    //点击新增，重置表单数据
    newAdd(){
      stringUtils.resetObject(this.form)
      this.title = "新增"
    },

    //删除树结构中的一个元素
    deleteDept(){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          url: 'sys/dept/delete?deptId='+this.form.deptId,
          type: 'post',
          scb: (res) => {
            this.$message.success('删除成功')
            this.newAdd()
            this.loadData()
          }
        }
        ajax(params)
      }).catch(() => {})
    },
    //点击树结构中某个元素的回调
    onNodeClick(data){
      this.form = merge({}, data);
      let rootDept = {list: this.deptTree, deptId: -1}
      let path = new Set()
      console.log("this.deptTree:",this.deptTree)
      stringUtils.setParentId(this.form.parentId,"deptId","parentId", rootDept, path)
      path.delete(-1)  //构造的root节点要删除掉
      this.form.parentIds = Array.from(path)
      console.log("this.form.parentIds:",this.form.parentIds)
      this.title = "修改"
    },
    //修改某个树节点的属性回调
    onUpdate(){
      this.$confirm('确定更改信息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
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
      }).catch(() => {})
    },
    //新增某个树节点的回调
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
    //初始化获取树的数据源
    loadData(){
      let params = {
        url: '/sys/dept/list',
        showLoading: false,
        scb: (res) => {
          this.deptTree = stringUtils.arrayToTree(
            res.deptList,{
              id:"deptId",
              parentId:"parentId",
              childrenKey:"list",
            }
            )
          console.log("this.deptTree:",this.deptTree)
          if (this.deptTree.length > 0) {
            this.defaultExpandedKeys.push(this.deptTree[0].deptId)
          }
        }
      }
      ajax(params)
    }
  },
  created(){
    this.loadData();
  }
}
