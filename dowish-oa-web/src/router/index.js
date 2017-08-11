import index from "../pages/common/index.vue";
import NotFoundView from "../components/404.vue";
import Menu from "../pages/modules/sys/menu/";
import Dict from "../pages/modules/sys/dict/";
import Druid from "../pages/modules/sys/druid/";
import Log from "../pages/modules/sys/log/";
import Schedule from "../pages/modules/job/schedule/";
import Dept from "../pages/modules/sys/dept/";
import role from "../pages/modules/sys/role/";
import login from "../pages/common/login.vue";
import frame from "../pages/frame.vue";
import User from "../pages/modules/sys/user/";
import Primary from "../pages/modules/gen/primary/";
// import Student from "../pages/modules/student/student/";
// Routes
const routes = [
  {
    name: 'login', path: '/login', component: login
  },
  {
    name: 'test', path: '/test', component: frame,
    children: [
      {
        path: '*', component: NotFoundView
      }
    ]
  },
  {
  path: '', component: frame, children: [
    {
      path: '/index', component: index
    },
    {
      path: '/sys/menu', component: Menu
    },
    {
      path: '/sys/druid', component: Druid
    },
    {
      path: '/sys/dict', component: Dict
    },
    {
      path: '/sys/dept', component: Dept
    },
    {
      path: '/sys/role', component: role
    },
    {
      path: '/sys/user', component: User
    },
    {
      path: '/sys/log', component: Log
    },
    {
      path: '/job/schedule', component: Schedule
    },
    {
      path: '/gen/primary', component: Primary
    },
    // {
    //   path: '/student/student', component: Student
    // },
  ]
  },
  {
    path: '*', component: NotFoundView
  }
]


export default routes



