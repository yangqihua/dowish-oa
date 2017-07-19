<template>


  <imp-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-button type="primary" icon="plus" @click="newAdd">新增</el-button>
    </h3>
    <el-row slot="body">
      <el-col :span="6">
        <h3 class="listHead">角色列表</h3>
        <ul class="list-group" v-if="roleList.length>0">
          <a v-for="(role,index) in roleList" @click="loadRoleInfo(role)"
             href="javascript:;"
             class="list-group-item"
             :class="activeRole.roleId==role.roleId?'list-group-item-active':''">{{role.roleName}}</a>
        </ul>
        <div v-else class="noMesg">
          暂无角色信息
          <!--<el-button type="text" @click="newAdd">点击新增</el-button>-->
        </div>
      </el-col>

      <el-col :span="18">

        <h3 class="listHead">{{title}}</h3>
        <el-card class="box-card" style="margin: 15px 15px;">
          <div class="text item">
            <el-form :model="form" ref="form" :label-width="formLabelWidth">
              <el-form-item label="角色名称" >
                <el-input v-model="form.roleName" auto-complete="off" placeholder="请输入内容"></el-input>
              </el-form-item>
              <el-form-item label="角色描述" >
                <el-input
                  type="textarea"
                  autosize
                  placeholder="请输入内容"
                  v-model="form.remark">
                </el-input>
              </el-form-item>

              <el-form-item label="权限">
                <el-tree v-if="menuTree"
                         :data="menuTree"
                         ref="menuTree"
                         show-checkbox
                         highlight-current
                         clearable
                         :default-expanded-keys="menuTreeExpandedKeys"
                         :default-checked-keys="menuTreeCheckedKeys"
                         :render-content="renderContent"
                         node-key="menuId"
                         :props="menuProps">

                </el-tree>
              </el-form-item>

              <el-form-item label="数据范围" >
                <el-tree v-if="deptTree"
                         :data="deptTree"
                         ref="deptTree"
                         show-checkbox
                         highlight-current
                         clearable
                         :default-expanded-keys="deptTreeExpandedKeys"
                         :default-checked-keys="deptTreeCheckedKeys"
                         node-key="deptId"
                         :props="deptProps">

                </el-tree>
              </el-form-item>

              <el-form-item label="" >
                <el-button type="primary" @click="onSubmit" v-if="!form.roleId">新增</el-button>
                <template v-else>
                  <el-button type="success" @click="onUpdate">保存</el-button>
                  <el-button type="danger" @click="deleteRole" icon="delete">删除</el-button>
                </template>
              </el-form-item>
            </el-form>
          </div>
        </el-card>

      </el-col>
    </el-row>
  </imp-panel>

</template>
<script>
  import RoleJs from './Role.js'
  export default RoleJs
</script>

<style>
  @import "Role.css";
</style>
