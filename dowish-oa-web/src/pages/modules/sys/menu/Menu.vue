<template>

  <content-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-button type="primary" icon="plus" @click="newAdd">新增</el-button>
    </h3>
    <el-row slot="body">
      <el-col :span="6">
        <h3 class="listHead">角色列表</h3>
        <el-tree v-if="menuTree"
                 ref="menuTree"
                 :data="menuTree"
                 show-checkbox
                 highlight-current
                 :render-content="renderContent"
                 @node-click="handleNodeClick"
                 clearable node-key="menuId"
                 :default-expanded-keys="defaultExpandedKeys"
                 :props="defaultProps">
        </el-tree>
      </el-col>
      <el-col :span="18">
        <h3 class="listHead">{{title}}</h3>
        <el-card class="box-card" style="margin-left: 15px">
          <div class="text item">
            <el-form :model="form" ref="form">

              <el-form-item label="父级" :label-width="formLabelWidth">
                <el-cascader
                  :props="cascaderProps"
                  :options="menuTree"
                  v-model="form.parentIds"
                  change-on-select>
                </el-cascader>
              </el-form-item>
              <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="form.name" auto-complete="off" placeholder="请填写名称"></el-input>
              </el-form-item>
              <el-form-item label="链接" :label-width="formLabelWidth">
                <el-input v-model="form.url" auto-complete="off" placeholder="请填写链接"></el-input>
              </el-form-item>
              <el-form-item label="权限标志" :label-width="formLabelWidth">
                <el-input v-model="form.perms" auto-complete="off"
                          placeholder="权限标志格式,多个用,分隔，如:user:list,user:create"></el-input>
              </el-form-item>
              <el-form-item label="图标" :label-width="formLabelWidth">
                <i :class="form.icon" v-model="form.icon"></i>
                <el-button type="text" @click="selectIconDialog=true">选择</el-button>
              </el-form-item>
              <el-form-item label="类型" :label-width="formLabelWidth">
                <el-radio-group v-model="form.type">
                  <el-radio :label="0">目录</el-radio>
                  <el-radio :label="1">菜单</el-radio>
                  <el-radio :label="2">按钮</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="排序" :label-width="formLabelWidth">
                <el-slider v-model="form.orderNum" show-input></el-slider>
              </el-form-item>
              <el-form-item label="" :label-width="formLabelWidth">
                <el-button type="primary" @click="onSubmit" v-if="!form.menuId">新增</el-button>
                <template v-else>
                  <el-button type="success" @click="onUpdate">保存</el-button>
                  <el-button type="danger" @click="deleteMenu" icon="delete">删除</el-button>
                </template>
              </el-form-item>
            </el-form>
          </div>
          <el-dialog title="选择图标" v-model="selectIconDialog" size="tiny">
            <div class="select-tree">
              <el-scrollbar
                tag="div"
                class='is-empty'
                wrap-class="el-select-dropdown__wrap"
                view-class="el-select-dropdown__list">
                <div class="icons-wrapper"><i v-for="item in icons" :class="item" @click="selectIcon($event)"></i></div>
              </el-scrollbar>
            </div>
            <span slot="footer" class="dialog-footer">
          <el-button @click="selectIconDialog = false">取 消</el-button>
          <el-button type="primary" @click="selectIconDialog = false">确 定</el-button>
          </span>
          </el-dialog>
        </el-card>
      </el-col>
    </el-row>
  </content-panel>

</template>
<script type="text/babel">

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
          id: "menuId",
        },
        maxId: 7000000,


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
              <span><i class={data.icon}></i>&nbsp;{node.label}</span>
            </span>
          </span>);
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
        }).catch(() => {})
      },
      handleNodeClick(data){
        this.form = merge({}, data);
        let rootMenu = {list: this.menuTree, menuId: -1}
        let path = new Set()
        stringUtils.setParentMenuId(this.form.parentId, rootMenu, path)
        path.delete(-1)  //构造的root节点要删除掉
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
            this.menuTree = res.menuList
            if (this.menuTree.length > 0) {
              this.defaultExpandedKeys.push(this.menuTree[0].menuId)
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
</script>

<style>
  .select-tree .icons-wrapper {
    display: block;
  }

  .select-tree .is-empty i {
    width: 30px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    display: inline-block;
    cursor: pointer;
  }

  .select-tree .is-empty i:hover {
    background-color: #0d6aad;
    color: #ffffff;
  }

</style>
