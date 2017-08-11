<template>

  <content-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">

      <div class="title-search">
        <el-form label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户名">
                <el-input v-model="searchForm.username" placeholder="用户名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="操作">
                <el-input v-model="searchForm.operation" placeholder="操作"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <div class="btn-group">
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
          prop="username"
          label="用户名">
        </el-table-column>

        <el-table-column
          prop="operation"
          label="操作">
        </el-table-column>

        <el-table-column
          prop="method"
          label="请求方法">
        </el-table-column>

        <el-table-column
          prop="params"
          width="500px"
          label="请求参数">
        </el-table-column>

        <el-table-column
          prop="ip"
          label="请求ip">
        </el-table-column>

        <el-table-column
          prop="createDate"
          label="请求时间">
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
</template>

<script>
  import Log from './Log'
  export default Log
</script>

<style>
  .el-pagination {
    float: right;
    margin-top: 15px;
  }
</style>
