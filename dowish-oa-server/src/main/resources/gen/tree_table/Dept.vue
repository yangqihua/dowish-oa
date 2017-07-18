<template>

  <content-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-button type="primary" icon="plus" @click="newAdd">新增</el-button>
    </h3>
    <el-row slot="body">
      <el-col :span="6">
        <h3 class="listHead">部门列表</h3>
        <el-tree v-if="deptTree"
                 ref="deptTree"
                 :data="deptTree"
                 show-checkbox
                 highlight-current
                 :render-content="renderContent"
                 @node-click="onNodeClick"
                 clearable node-key="deptId"
                 :default-expanded-keys="defaultExpandedKeys"
                 :props="defaultProps">
        </el-tree>
      </el-col>
      <el-col :span="18">
        <h3 class="listHead">{{title}}</h3>
        <el-card class="box-card" style="margin-left: 15px">
          <div class="text item">
            <el-form :model="form" ref="form" label-width="100px">

              <el-form-item label="父级">
                <el-cascader
                  :props="cascaderProps"
                  :options="deptTree"
                  v-model="form.parentIds"
                  change-on-select>
                </el-cascader>
              </el-form-item>
              <el-form-item label="名称">
                <el-input v-model="form.name" auto-complete="off" placeholder="请填写名称"></el-input>
              </el-form-item>
              <el-form-item label="排序" >
                <el-slider v-model="form.orderNum" show-input></el-slider>
              </el-form-item>
              <el-form-item label="" >
                <el-button type="primary" @click="onSubmit" v-if="!form.deptId">新增</el-button>
                <template v-else>
                  <el-button type="success" @click="onUpdate">保存</el-button>
                  <el-button type="danger" @click="deleteDept" icon="delete">删除</el-button>
                </template>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </content-panel>

</template>
<script type="text/babel">
  import DeptJs from './Dept'
  export default DeptJs
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
