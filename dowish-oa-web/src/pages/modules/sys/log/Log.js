/**
 * Created by yangqihua on 2017/7/10.
 */
import panel from "../../../../components/panel.vue"
import ajax from '../../../../utils/ajax/ajax.js'
import stringUtils from '../../../../utils/string-utils.js'
//深拷贝
import merge from 'element-ui/src/utils/merge';
import {mapGetters, mapActions, mapMutations} from 'vuex'
import perms from '../../../../config/permissions'
export default {
  components: {
    'content-panel': panel
  },
  data(){
    return {
      //list部分
      multipleSelection: [],

      searchForm: {
        username: '',
        operation: '',
      },
      tableData: {
        pagination: {
          total: 0,
          pageNo: 1,
          pageSize: 10,
        },
        rows: []
      },
    }
  },
  methods: {
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    handleSizeChange(val) {
      this.tableData.pagination.pageSize = val;
      this.loadData();
    },
    handleCurrentChange(val) {
      this.tableData.pagination.pageNo = val;
      this.loadData();
    },

    loadData(){
      this.searchForm.page = this.tableData.pagination.pageNo
      this.searchForm.limit = this.tableData.pagination.pageSize
      let params = {
        url: '/sys/log/list',
        data: this.searchForm,
        scb: (response) => {
          this.tableData.rows = response.page.list
          this.tableData.pagination.total = response.page.totalCount
        }
      }
      ajax(params)
    },
    resetSearchForm() {
      stringUtils.resetObject(this.searchForm)
    }
  },
  computed: {
    ...mapGetters({
      user:'user',userPerms:'permissions'
    }),
    permissions(){
      return {
        list: stringUtils.hasPermission(this.userPerms,perms.SYS_LOG_LIST),
      }
    },
  },
  created(){
    this.loadData();
  },
}
