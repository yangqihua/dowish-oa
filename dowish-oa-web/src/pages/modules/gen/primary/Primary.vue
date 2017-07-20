<template>

  <content-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">

      <div style="display: flex;justify-content: center">
        <el-steps :active="activeStep" style="width:80%;" :center="true">
          <el-step title="步骤 1" description=""></el-step>
          <el-step title="步骤 2" description=""></el-step>
          <el-step title="步骤 3" description=""></el-step>
        </el-steps>
      </div>

      <h4 class="listHead" style="margin: 20px 0 0 0">{{stepTitle}}</h4>

      <el-row style="width: 100%;margin-top: 0px" v-if="activeStep==0">
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
      <div v-if="activeStep==1">
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
            prop="comments"
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
                @click="handleMapper(scope.$index, scope.row)">配置Mapper到下一步
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

      <div v-if="activeStep==2">
        <div style="margin: 18px 150px 0 130px;">
          <el-form label-width="120px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="表名">
                  <el-input v-model="genTable.tableName" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成类型">
                  <el-select v-model="genTable.category" filterable placeholder="现在生成类型"
                             style="display: block">
                    <el-option
                      v-for="item in genConfig.categoryList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="类名">
                  <el-input v-model="genTable.className" placeholder="类名"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="包名">
                  <el-input v-model="genTable.packageName" placeholder="包名"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="英文模块名">
                  <el-input v-model="genTable.moduleName" placeholder="英文模块名，如：sys"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="模块名">
                  <el-input v-model="genTable.comments" placeholder="中文模块名"></el-input>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row v-if="(genTable.category!='dao' && genTable.category!='curd')">
              <el-col :span="12">
                <el-form-item label="父表名称">
                  <el-select v-model="genTable.parentTableName" filterable placeholder="请选择父表名" style="display: block">
                    <el-option
                      v-for="item in allTables"
                      :key="item.tableName"
                      :label="item.tableName"
                      :value="item.tableName">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="父表关联外键">
                  <el-select v-model="genTable.parentTableFkName" filterable placeholder="父表关联外键"
                             style="display: block">
                    <el-option
                      v-for="item in genTable.columnList"
                      :key="item.columnName"
                      :label="item.columnName"
                      :value="item.columnName">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>


            <el-row>
              <el-col :span="12">
                <el-form-item label="父级菜单">
                  <el-cascader
                    :props="cascaderProps"
                    :options="menuTree"
                    v-model="parentMenuIds"
                    change-on-select
                    style="display: block"
                  >
                  </el-cascader>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="作者">
                  <el-input v-model="genTable.functionAuthor" placeholder="作者"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>


        </div>

        <div style="margin: 16px 10px;text-align: center;line-height: 40px">
          <el-row type="flex" justify="space-between">
            <el-col :span="2">列名</el-col>
            <el-col :span="3">说明</el-col>
            <el-col :span="2">物理类型</el-col>
            <el-col :span="2">Java类型</el-col>
            <el-col :span="2">Java属性</el-col>
            <el-col :span="1">主键</el-col>
            <el-col :span="1">可空</el-col>
            <el-col :span="1">编辑</el-col>
            <el-col :span="1">列表</el-col>
            <el-col :span="1">查询</el-col>
            <el-col :span="3">查询匹配方式</el-col>
            <el-col :span="3">表单类型</el-col>
            <el-col :span="2">排序</el-col>
          </el-row>

          <el-row type="flex" justify="space-between"
                  :key="item.columnName"
                  v-for="item in genTable.columnList">
            <el-col :span="2">
              <el-input
                size="small"
                v-model="item.columnName" :disabled="true">
              </el-input>
            </el-col>
            <el-col :span="3">
              <el-input
                size="small"
                placeholder="说明"
                v-model="item.comments">
              </el-input>
            </el-col>
            <el-col :span="2">
              <el-input
                size="small"
                v-model="item.jdbcType" :disabled="true">
              </el-input>
            </el-col>
            <el-col :span="2">
              <el-select v-model="item.javaType" placeholder="表单类型">
                <el-option
                  v-for="item in genConfig.javaTypeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="2">
              <el-input
                size="small"
                placeholder="Java属性"
                v-model="item.javaField">
              </el-input>
            </el-col>
            <el-col :span="1">
              <el-checkbox v-model="item.isPk" :disabled="true"></el-checkbox>
            </el-col>
            <el-col :span="1">
              <el-checkbox v-model="item.isNull"></el-checkbox>
            </el-col>
            <el-col :span="1">
              <el-checkbox v-model="item.isEdit"></el-checkbox>
            </el-col>
            <el-col :span="1">
              <el-checkbox v-model="item.isList"></el-checkbox>
            </el-col>
            <el-col :span="1">
              <el-checkbox v-model="item.isQuery"></el-checkbox>
            </el-col>
            <el-col :span="3">
              <el-select v-model="item.queryType" placeholder="查询类型">
                <el-option
                  v-for="item in genConfig.queryTypeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <el-select v-model="item.showType" placeholder="表单类型">
                <el-option
                  v-for="item in genConfig.showTypeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="2">
              <el-input
                size="small"
                placeholder="排序"
                v-model="item.sort" :disabled="true">
              </el-input>
            </el-col>
          </el-row>
        </div>

        <div style="float: right;margin: 30px 20px 20px 0;">

          <el-button @click.native.prevent="backStep" type="primary">上一步</el-button>
          <el-button @click.native.prevent="genCode" type="success">生成代码
          </el-button>

        </div>
      </div>

      <div v-if="activeStep==3">
        <div style="float: right;margin: 30px 20px 20px 0;">
          <el-button @click.native.prevent="goStep1" type="primary">返回第一步</el-button>
          <el-button @click.native.prevent="backStep" type="info">返回上一步</el-button>
          </el-button>

        </div>
      </div>
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
