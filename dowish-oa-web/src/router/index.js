import index from "../pages/common/index.vue";
import NotFoundView from "../components/404.vue";
import menuList from "../pages/modules/sys/menu.vue";
import role from "../pages/modules/sys/role/";
import resource from "../pages/modules/sys/resource.vue";
import login from "../pages/common/login.vue";
import frame from "../pages/frame.vue";
import User from "../pages/modules/sys/user/";
import userAdd from "../pages/modules/sys/userAdd.vue";
import resetPwd from "../pages/common/resetPwd.vue";
// Routes
const routes = [
  {
    name:'login',path:'/login', component: login
  },
  {
    name:'test',path: '/test', component: frame,
    children: [
      {
        path: '*', component: NotFoundView
      }
    ]
  },
  {
    path: '', component: frame, children: [
      {
        path: '/resetPwd', component: resetPwd
      },
      {
        path: '/index', component: index
      },
      {
        path: '/sys/menu', component: menuList
      },
      {
        path: '/sys/role', component: role
      },
      {
        path: '/sys/user', component: User
      },
      {
        path: '/sys/user/add', component: userAdd
      },
      {
        path: '/sys/resource', component: resource
      }
    ]
  },
  {
    path: '*', component: NotFoundView
  }
]


export default routes

