<template>
  <div class="wrapper fixed">
    <!--<vue-progress-bar></vue-progress-bar>-->
    <imp-header></imp-header>
    <side-menu :show="sidebar.opened && !sidebar.hidden"></side-menu>
    <div class="content-wrapper" id="main-container">
      <section class="content">
        <transition mode="out-in" enter-active-class="fadeIn" leave-active-class="fadeOut" appear>
          <router-view></router-view>
        </transition>
      </section>
      <imp-footer></imp-footer>
    </div>
  </div>
</template>

<script>

  import Vue from 'vue'
  import sideMenu from '../components/sideMenu.vue'
  import impHeader from "./layout/header.vue"
  import impFooter from "./layout/footer.vue"
  require("jquery-slimscroll/jquery.slimscroll.js")
  import {mapGetters, mapActions,mapMutations} from 'vuex'
  import * as types from "../store/common/mutation-types"
  import 'animate.css'

  export default {
    name: 'app',
    components: {
      sideMenu,
      impFooter,
      impHeader
    },
    computed: {
        ...mapGetters({
            sidebar: 'sidebar'
        })
    },
    data: function () {
      return {
        active: true,
        headerFixed: true,
        breadcrumb: []
      }
    },
    methods: {
      ...mapMutations({
          toggleDevice: types.TOGGLE_DEVICE,
          toggleSidebar: types.TOGGLE_SIDEBAR
      }),
    },
    watch: {
      '$route': function (to, from) {
      }
    },
    beforeMount () {
      const { body } = document
      const WIDTH = 784
      const handler = () => {
        if (!document.hidden) {
          let rect = body.getBoundingClientRect()
          let isMobile = rect.width < WIDTH
          this.toggleDevice(isMobile);
          this.toggleSidebar(!isMobile)
        }
      }
      document.addEventListener('visibilitychange', handler)
      window.addEventListener('DOMContentLoaded', handler)
      window.addEventListener('resize', handler)
    },
    mounted () {
      //  [frame.vue specific] When frame.vue is finish loading finish the progress bar
//      this.$Progress.finish()
    },
    created () {
    }
  }

  // 在状态改变后和断言 DOM 更新前等待一刻
  Vue.nextTick(() => {
    if (typeof $.fn.slimScroll != 'undefined') {
      //Destroy if it exists
      $(".sidebar").slimScroll({destroy: true}).height("auto");
      //Add slimscroll
      $(".sidebar").slimScroll({
        height: ($(window).height() - $(".main-header").height()) + "px",
        color: "rgba(0,0,0,0.2)",
        size: "3px"
      });
    }
    $(window).on("resize", function () {
      if (typeof $.fn.slimScroll != 'undefined') {
        //Destroy if it exists
        $(".sidebar").slimScroll({destroy: true}).height("auto");
        //Add slimscroll
        $(".sidebar").slimScroll({
          height: ($(window).height() - $(".main-header").height()) + "px",
          color: "rgba(0,0,0,0.2)",
          size: "3px"
        });
      }
    });
  })
</script>

<style>
  @import "style.css";
</style>
