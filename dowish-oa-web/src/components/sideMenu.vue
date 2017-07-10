<template>
  <aside class="main-sidebar animated" :class="{ slideInLeft: show, slideOutLeft: !show }">
    <div class="sidebar">
      <el-menu :default-active="onRoutes"
               :default-openeds="onRouteKeys"
               class="el-menu-vertical-demo"
               theme="dark" router>
        <template v-for="item in menuList">
          <sub-menu :param="item"></sub-menu>
        </template>
      </el-menu>
    </div>
  </aside>
</template>
<script>
  import subMenu from "./subMenu.vue"
  import {mapGetters, mapActions, mapMutations} from 'vuex'


  export default {
    props: {
      show: Boolean
    },
    data() {
      return {}
    },
    components: {
      subMenu
    },
    computed: {
      onRoutes(){
        return this.$route.path;
      },
      onRouteKeys(){
//        当前打开的submenu的 key 数组
        const getParentArray = (path, menuList, kas = []) => {
          if (menuList && menuList.length > 0) {
            for (let k = 0, length = menuList.length; k < length; k++) {
              if (path == menuList[k].url) {
                kas.push(menuList[k].url);
                break;
              }
              let i = kas.length;
              if (menuList[k].list && menuList[k].list.length > 0) {
                getParentArray(path, menuList[k].list, kas);
              }
              //如果是递归添加到子menu，则将父menu加入路径中
              if (i < kas.length) {
                kas.push(menuList[k].url);
              }
            }
          }
          return kas.reverse();
        }

//        console.log(this.$route.path,getParentArray(this.$route.path, this.menuList))
        return getParentArray(this.$route.path, this.menuList)
      },
      // 使用对象展开运算符将 getters 混入 computed 对象中
      ...mapGetters([
        'menuList'
      ])
    },
    mounted () {
      let route = this.$route
//      console.log(this.onRouteKeys)
//      if (route.name) {
//        this.shouldExpandMatchItem(route)
//      }
    },
    created: function () {
      this.getMenuList({url:'/sys/menu/user',showLoading:false});
    },
    methods: {
      ...mapActions({
        getMenuList: 'getMenuListAction'
      })
    }
  }
</script>



<style>
  @media (max-width: 800px) {
    .main-sidebar {
      transform: translate3d(-230px, 0, 0);
    }
  }

  .slideInLeft {
    animation-duration: .377s;
    animation-name: slideInLeft;
  }

  .slideOutLeft {
    animation-duration: .377s;
    animation-name: slideOutLeft;
  }

  .main-sidebar {
    background-color: #324157;
    position: fixed;
    top: 50px;
    left: 0;
    bottom: 0;
    min-height: 100%;
    width: 230px;
    z-index: 810;
    -webkit-transition: -webkit-transform 0.3s ease-in-out, width 0.3s ease-in-out;
    -moz-transition: -moz-transform 0.3s ease-in-out, width 0.3s ease-in-out;
    -o-transition: -o-transform 0.3s ease-in-out, width 0.3s ease-in-out;
    transition: transform 0.3s ease-in-out, width 0.3s ease-in-out;
  }

  .el-menu-vertical-demo .el-submenu .el-menu-item {
    height: 45px;
    line-height: 45px;
  }

  .el-menu-vertical-demo .el-menu-item, .el-menu-vertical-demo .el-submenu__title {
    height: 45px;
    line-height: 45px;
  }

</style>
