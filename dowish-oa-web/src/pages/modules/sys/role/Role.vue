<template>


  <imp-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-button type="primary" icon="plus" @click="newAdd" style="margin-left: 6px">新增</el-button>
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

        <h3 class="roleListHead">{{title}}</h3>
        <el-card class="box-card" style="margin: 15px 15px;">
          <div class="text item">
            <el-form :model="form" ref="form">
              <el-form-item label="角色名称" :label-width="formLabelWidth">
                <el-input v-model="form.roleName" auto-complete="off" placeholder="请输入内容"></el-input>
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
                <el-button type="primary" @click="onSubmit" v-if="!form.roleId">新增</el-button>
                <template v-else>
                  <el-button type="success" @click="onUpdate">保存</el-button>
                  <el-button type="danger" @click="deleteRole" icon="delete">删除</el-button>
                </template>
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

  import RoleJs from './Role.js'
  export default RoleJs

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
