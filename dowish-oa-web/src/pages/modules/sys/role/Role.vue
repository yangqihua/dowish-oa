<template>


  <imp-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-button type="primary" icon="plus" @click="newAdd">新增</el-button>
      <el-button type="danger" icon="delete" @click="batchDelete">删除</el-button>

    </h3>
    <el-row slot="body">
      <el-col :span="6">
        <h3 class="roleListHead">角色列表</h3>
        <ul class="list-group" v-if="roleList.length>0">
          <a v-for="(role,index) in roleList" @click="loadRoleInfo(role)"
             href="javascript:;"
             class="list-group-item"
             :class="activeRole.roleId==role.roleId?'list-group-item-active':''">{{role.roleName}}</a>
        </ul>
        <div v-else class="noMesg">
          暂无角色信息
          <el-button type="text" @click="newAdd">点击新增</el-button>
        </div>
      </el-col>

      <el-col :span="18">

        <el-card class="box-card" style="margin: 40px 15px;">
          <div class="text item">
            <el-form :model="form" ref="form">
              <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="form.name" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="角色描述" :label-width="formLabelWidth">
                <el-input
                  type="textarea"
                  autosize
                  placeholder="请输入内容"
                  v-model="form.remark">
                </el-input>
              </el-form-item>

              <el-form-item label="权限" :label-width="formLabelWidth">
                <el-tree v-if="menuList"
                         :data="menuList"
                         ref="roleTree"
                         show-checkbox
                         highlight-current
                         @node-click="handleNodeClick"
                         clearable
                         :default-expanded-keys="defaultExpandedKeys"
                         :default-checked-keys="defaultCheckedKeys"
                         node-key="menuId"
                         :props="defaultProps">

                </el-tree>
              </el-form-item>

              <el-form-item label="" :label-width="formLabelWidth">
                <el-button type="primary" @click="onSubmit" v-text="form.id?'修改':'新增'"></el-button>
                <el-button type="danger" @click="deleteSelected" icon="delete" v-show="form.id && form.id!=null">删除
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>

        <el-dialog title="配置资源" v-model="dialogVisible" size="tiny">
          <div class="select-tree">
            <el-scrollbar
              tag="div"
              class='is-empty'
              wrap-class="el-select-dropdown__wrap"
              view-class="el-select-dropdown__list">
              <el-tree
                :data="resourceTree"
                ref="resourceTree"
                show-checkbox
                check-strictly
                node-key="id"
                v-loading="dialogLoading"
                :props="defaultProps">
              </el-tree>
            </el-scrollbar>
          </div>
          <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="configRoleResources">确 定</el-button>
          </span>
        </el-dialog>
      </el-col>
    </el-row>
  </imp-panel>

</template>
<script>

  import panel from "../../../../components/panel.vue"
  import selectTree from "../../../../components/selectTree.vue"
  import treeter from "../../../../components/treeter"

  import ajax from '../../../../utils/ajax/ajax.js'
  import stringUtils from '../../../../utils/string-utils.js'


  import * as api from "../../../../utils/api"

  export default {
    mixins: [treeter],
    components: {
      'imp-panel': panel,
      'el-select-tree': selectTree,
    },
    data(){
      return {
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
        activeRole:{},

        form: {
          roleId: null,
          roleName: null,
          remark: '',
          menuIdList: [],
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
        this.form = {
          id: null,
          parentId: null,
          name: '',
          enName: '',
          sort: 0,
          usable: '1',
          remarks: ''
        };
      },
      batchDelete(){
        var checkKeys = this.$refs.roleTree.getCheckedKeys();
        if (checkKeys == null || checkKeys.length <= 0) {
          this.$message.warning('请选择要删除的资源');
          return;
        }
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.get(api.SYS_ROLE_DELETE + "?roleIds=" + checkKeys.join(','))
            .then(res => {
              this.$message('操作成功');
              this.loadData();
            }).catch(e => {
            this.$message('操作成功');
            console.log(checkKeys);
            this.batchDeleteFromTree(this.roleTree, checkKeys);
          })
        });

      },
      onSubmit(){
        this.form.parentId = this.form.parentId;
        this.$http.post(api.SYS_ROLE_ADD, this.form)
          .then(res => {
            this.form.id = res.data.id;
            this.appendTreeNode(this.roleTree, res.data);
          }).catch(e => {
          this.maxId += 1;
          this.$message('操作成功');
          this.form.id = this.maxId;
          var ddd = {
            id: this.form.id,
            name: this.form.name,
            sort: this.form.sort,
            enName: this.form.enName,
            parentId: this.form.parentId,
            usable: this.form.usable,
            children: []
          }
          this.appendTreeNode(this.roleTree, ddd);
          this.roleTree.push({});
          this.roleTree.pop();
        })
      },
      deleteSelected(id){
        this.$http.get(api.SYS_ROLE_DELETE + "?roleIds=" + id)
          .then(res => {
            this.$message('操作成功');
            this.deleteFromTree(this.roleTree, id);
            this.newAdd();
          }).catch(e => {
          this.$message('操作成功');
          this.deleteFromTree(this.roleTree, id);
          this.newAdd();
        })
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
        this.activeRole =role
        let params = {
          url: 'sys/role/info/' + role.roleId,
          scb: (response) => {
            this.roleInfo = response.role
            let tempIdList = [];
            this.roleInfo.menuIdList.forEach(menuId=>{
            	if(!this.isParentMenuId(menuId,this.menuList)){
                tempIdList.push(menuId)
              }
            })
            this.$refs.roleTree.setCheckedKeys(tempIdList);
          }
        }
        ajax(params)
      },
      isParentMenuId(menuId,menuList){
        for(let key in menuList) {
          if (menuList[key].parentId == menuId) {
            return true
          } else if(menuList[key].list!=null) {
            return this.isParentMenuId(menuId,menuList[key].list);
          }else{
          	return false
          }
        }
      },
    },

    computed:{
    }
    ,
    created(){
      this.loadData();
      this.loadMenuList();
    }
  }
</script>

<style>
  .list-group {
    padding-left: 0;
    margin: 15px 20px 0 10px;
  }

  .list-group-item {
    position: relative;
    display: block;
    padding: 10px 15px;
    margin-bottom: -1px;
    background-color: #fff;
    border: 1px solid #ddd;
    /*text-align: center;*/
  }

  .list-group-item:first-child {
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
  }

  .list-group-item:last-child {
    margin-bottom: 0;
    border-bottom-right-radius: 4px;
    border-bottom-left-radius: 4px;
  }

  a.list-group-item {
    color: #555;
    text-decoration: none;
  }

  a.list-group-item .list-group-item-heading {
    text-decoration: none;
    color: #333;
  }

  a.list-group-item:hover
  /*a.list-group-item:focus*/
  {
    background: #4db3ff;
    border-color: #4db3ff;
    color: #fff;
    /*color: #555;*/
    text-decoration: none;
    /*background-color: #f5f5f5;*/
  }

  a.list-group-item-active {
    background: #4db3ff;
    border-color: #4db3ff;
    color: #fff;
    /*color: #555;*/
    text-decoration: none;
    /*background-color: #f5f5f5;*/
  }

  .roleListHead {
    text-align: center;
    margin: 0 20px 0 10px;
  }

  .noMesg {
    text-align: center;
    margin-top: 25px;
  }

  .render-content {
    float: right;
    margin-right: 20px
  }

  .render-content i.fa {
    margin-left: 10px;
  }

  .render-content i.fa:hover {
    color: #e6000f;
  }

  .select-tree .el-scrollbar.is-empty .el-select-dropdown__list {
    padding: 0;
  }

  .select-tree .el-scrollbar {
    border: 1px solid #d1dbe5;
  }

  .select-tree .el-tree {
    border: 0;
  }

</style>
