<template>

  <content-panel v-if="showList">
    <h3 class="box-title" slot="header" style="width: 100%;">

      <div class="title-search">
        <el-form label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="参数名称">
                <el-input v-model="searchForm.key" placeholder="参数名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="参数值">
                <el-input v-model="searchForm.value" placeholder="参数值"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="btn-group">
            <el-button v-if="permissions.delete" @click.native.prevent="handleDelete" icon="delete" type="danger" :disabled="btnEnable.multi">删除</el-button>
            <el-button v-if="permissions.edit" @click.native.prevent="handleEdit" icon="edit" type="info" :disabled="btnEnable.edit">编辑</el-button>
            <el-button v-if="permissions.add" @click.native.prevent="addNew" icon="plus" type="success">新增</el-button>
            <el-button v-if="permissions.list"  type="info" @click="resetSearchForm()" class="el-icon-circle-close"> 重置条件</el-button>
            <el-button v-if="permissions.list" @click.native.prevent="loadData" icon="search" type="primary">搜索</el-button>
          </div>
        </el-form>
      </div>
    </h3>
    <div slot="body">
      <el-table
        :data="tableData.rows"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange">

        <el-table-column
          prop="id"
          type="selection"
          width="45">
        </el-table-column>

        <el-table-column
          prop="key"
          label="参数名称">
        </el-table-column>

        <el-table-column
          prop="value"
          label="参数值">
        </el-table-column>

        <el-table-column
          prop="remark"
          label="备注">
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

    </div>

  </content-panel>


  <!--编辑/添加用户start-->
  <content-panel v-else>
    <div style="margin: 30px 50px 20px 10px">
      <el-form :model="form" ref="form" :rules="rules" label-width="180px">
        <el-form-item label="参数名称" prop="key">
          <el-input v-model="form.key"></el-input>
        </el-form-item>
        <el-form-item label="参数值">
          <el-input v-model="form.value"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="onUpdate" v-if="form.id" class="el-icon-circle-check"> 保存</el-button>
          <el-button type="success" @click="onSubmit" class="el-icon-circle-check" v-else> 立即创建</el-button>
          <el-button type="primary" @click="resetForm()" class="el-icon-circle-close"> 重置</el-button>
          <el-button type="primary" @click="showList = !showList;" class="el-icon-arrow-left"> 返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </content-panel>
  <!--编辑/添加用户end-->
</template>

<script>
  import Dict from './Dict'
  export default Dict
</script>

<style>
  .el-pagination {
    float: right;
    margin-top: 15px;
  }
</style>
