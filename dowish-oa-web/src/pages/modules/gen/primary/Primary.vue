<template>

  <content-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-row style="width: 100%;">
        <el-col :span="24">
          <div class="el-input" style="width: 300px; float: right;">
            <i class="el-input__icon el-icon-search"></i>
            <input type="text" placeholder="输入数据表名称" v-model="searchKey" @keyup.enter="loadData"
                   class="el-input__inner">
          </div>
        </el-col>
      </el-row>
    </h3>

    <div slot="body">
      <el-table
        :data="rows"
        border
        style="width: 100%"
        >
        <el-table-column
          prop="tableName"
          label="数据表名">
        </el-table-column>
        <el-table-column
          prop="tableComment"
          label="数据表注释">
        </el-table-column>
        <el-table-column
          prop="engine"
          label="数据库引擎">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
        >
        </el-table-column>
        <el-table-column label="操作" width="285">
          <template scope="scope">
            <el-button
              size="small"
              type="primary"
              icon="edit"
              @click="handleMapper(scope.$index, scope.row)">配置业务表
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNo"
        :page-sizes="[10, 20, 40]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>

    </div>

  </content-panel>


</template>

<script>
    import Primary from './Primary'
    export default Primary
</script>

<style>
  .el-pagination {
    float: right;
    margin-top: 15px;
  }
</style>
