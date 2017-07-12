<template>

  <content-panel id="userListId" v-if="showList">
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-row style="width: 100%;">
        <el-col :span="12">
          <el-button type="primary" icon="plus" @click.once="addNew()">新增</el-button>
        </el-col>
        <el-col :span="12">
          <div class="el-input" style="width: 300px; float: right;">
            <i class="el-input__icon el-icon-search"></i>
            <input type="text" placeholder="输入用户名称" v-model="searchKey" @keyup.enter="loadData"
                   class="el-input__inner">
          </div>
        </el-col>
      </el-row>
    </h3>
    <div slot="body">
      <el-table
        :data="tableData.rows"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange" id="userTableId">

        <el-table-column
          prop="id"
          type="selection"
          width="45">
        </el-table-column>

        <el-table-column
          prop="username"
          label="用户名">
        </el-table-column>
        <el-table-column
          prop="mobile"
          label="手机号">
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
        >
          <template scope="scope">
            <el-tag
              :type="scope.row.status == '1' ? '' : 'danger'"
              close-transition>{{scope.row.status==1?'正常':'禁用'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="285">
          <template scope="scope">
            <el-button
              size="small"
              type="default"
              icon="edit"
              @click="handleEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="small"
              type="info"
              icon="setting"
              @click="handleResetPwd(scope.$index, scope.row)">重置密码
            </el-button>
            <el-button
              size="small"
              type="danger"
              icon="delete"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="tableData.pagination.pageNo"
        :page-sizes="[10, 20, 40]"
        :page-size="tableData.pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableData.pagination.total">
      </el-pagination>

      <el-dialog title="重置密码" v-model="resetPwdFormVisible">
        <el-form :model="pwdForm" ref="resetPwdForm" :rules="resetPwdRules" label-width="120px">
          <el-form-item label="旧密码" >
            <el-input type="password" v-model="pwdForm.password" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input type="password" v-model="pwdForm.newPassword" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="resetPwdFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="onResetPwd">确 定</el-button>
        </div>
      </el-dialog>
    </div>

  </content-panel>


  <!--编辑/添加用户start-->
  <content-panel id="userEditId" v-else>
    <div style="margin: 30px 50px 20px 10px">
      <el-form :model="form" ref="form" :rules="rules" label-width="180px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" v-if="!form.userId">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.mobile"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>

        <el-form-item label="状态">
          <el-switch
            v-model="boolStatus"
            on-color="#13ce66"
            off-color="#ff4949"
            on-text="正常"
            off-text="禁用"
          >
          </el-switch>
        </el-form-item>

        <el-form-item label="用户角色">
          <el-checkbox-group v-model="form.roleIdList">
            <el-checkbox v-for="item in roleList" :key="item.roleId" :label="item.roleId">{{item.roleName}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="onUpdate" v-if="form.userId" class="el-icon-circle-check"> 保存</el-button>
          <el-button type="success" @click="onSubmit" class="el-icon-circle-check" v-else> 立即创建</el-button>
          <el-button type="primary" @click="resetForm()" class="el-icon-circle-close"> 重置</el-button>
          <el-button type="primary" @click="showList = !showList;resetForm()" class="el-icon-arrow-left"> 返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </content-panel>
  <!--编辑/添加用户end-->
</template>

<script>
  import UserJs from './User'
  export default UserJs
</script>

<style>
  .el-pagination {
    float: right;
    margin-top: 15px;
  }
</style>
