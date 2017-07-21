import index from "../pages/common/index.vue";
import NotFoundView from "../components/404.vue";
import Menu from "../pages/modules/sys/menu/";
import Dept from "../pages/modules/sys/dept/";
import role from "../pages/modules/sys/role/";
import login from "../pages/common/login.vue";
import frame from "../pages/frame.vue";
import User from "../pages/modules/sys/user/";
import Primary from "../pages/modules/gen/primary/";
import Student from "../pages/modules/student/student/";

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
      path: '/sys/dept', component: Dept
    },
    {
      path: '/sys/role', component: role
    },
    {
      path: '/sys/user', component: User
    },
    {
      path: '/gen/primary', component: Primary
    },
    {
      path: '/student/student', component: Student
    },
  ]
  },
  {
    path: '*', component: NotFoundView
  }
]


export default routes

