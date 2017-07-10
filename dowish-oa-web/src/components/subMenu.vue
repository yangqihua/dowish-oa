<template>
  <el-submenu :index="item.url" v-if="item.list && item.list.length>0">
    <template slot="title"><i :class="item.icon"></i>{{item.name}}</template>
    <template v-for="child in item.list">
      <sub-menu v-if="child.list && child.list.length>0" :param="child"></sub-menu>
      <el-menu-item :index="child.url" v-else><i :class="child.icon"></i>{{child.name}}</el-menu-item>
    </template>
  </el-submenu>
  <el-menu-item :index="item.url" v-else><i :class="item.icon"></i>{{item.name}}</el-menu-item>
</template>
<script>
  import subMenu from "./subMenu.vue"

  export default {
    name: 'subMenu',
    props: ['param'],
    data: function () {
      return {
      	item: this.param,
      }
    },
    computed:{
//      defaultMenu(){
//      	if(this.item.length>0&&this.item[0].hasOwnProperty("url")){
//      		return this.item[0].url
//        }
//      	return ""
//      }
    },
    components: {
      subMenu
    }
  }
</script>
<style>
  .el-menu .fa {
    margin-right: 10px;
  }
</style>
