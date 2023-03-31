import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 防止多次点击标题报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
    // 后端路由
  {
    path: '/',
    name: 'Manage',
    component: () => import("../views/Manage.vue"),
    redirect: "/front",
    children: [
      { path: 'home', name: 'Home', component: () => import("../views/Home.vue") },
      { path: 'user', name: 'User', component: () => import("../views/User.vue") },
      { path: 'student', name: 'Student', component: () => import("../views/Student.vue")},
      { path: 'school', name: 'School', component: () => import("../views/School.vue") },
      { path: 'schoolSpecialty', name: 'SchoolSpecialty', component: () => import("../views/SchoolSpecialty.vue") },
      { path: 'schoolScore', name: 'Score', component: () => import("../views/Score.vue") },
      { path: 'schoolDescription', name: 'SchoolDescription', component: () => import("../views/SchoolDescription.vue") },
      { path: 'specialty', name: 'Specialty', component: () => import("../views/Specialty.vue") },
      { path: 'application', name: 'Application', component: () => import("../views/Application.vue") },
      { path: 'personinfo', name: 'PersonInfo', component: () => import("../views/PersonInfo.vue") },
      { path: 'changePSW', name: 'ChangePassword', component: () => import("../views/ChangePassword.vue")},
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("../views/Login.vue")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("../views/Register.vue")
  },

//  前端路由
  {
    path: '/front',
    name: 'Front',
    component: () => import("../views/front/Front.vue"),
    redirect: "/front/home",
    children: [
      { path: 'home', name: 'FrontHome', component: () => import("../views/front/Home.vue") },
      { path: 'school', name: 'FrontSchool', component: () => import("../views/front/School.vue") },
      { path: 'speciality', name: 'Speciality', component: () => import("../views/front/Specialty.vue") },
      { path: 'details', name: 'Details', component: () => import("../views/front/Details.vue") },
      { path: 'collection', name: 'Collection', component: () => import("../views/front/Collection.vue") },
      { path: 'application', name: 'Application', component: () => import("../views/front/Application.vue") },
      { path: 'recommend', name: 'Recommend', component: () => import("../views/front/Recommend.vue") },
      { path: 'report', name: 'Report', component: () => import("../views/front/Report.vue") },
    ]
  },
  {
    path: '/front/login',
    name: 'userLogin',
    component: () => import("../views/front/Login.vue")
  },
  {
    path: '/front/register',
    name: 'userRegister',
    component: () => import("../views/front/Register.vue")
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
