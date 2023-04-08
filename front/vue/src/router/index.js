import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404')
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front'),
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('../views/front/Home')
      },
      {
        path: 'reserve',
        name: 'FrontReserve',
        component: () => import('../views/front/Reserve')
      },
      {
        path: 'pay',
        name: 'FrontPay',
        component: () => import('../views/front/Pay')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

export const setRouters = () => {
  const menus = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).menus : null

  if (menus){
    const currentRouteNames = router.getRoutes().map(value => value.name)
    if (!currentRouteNames.includes('manage')){

      const manageRoute = {path: '/', name: 'manage', component: () => import( '../views/Manage.vue'), redirect: "/home",
        children: [
          { path: '/person', name: '个人信息', component: () => import('../views/Person')},
          { path: '/modifyPassword', name: '修改密码', component: () => import('../views/ModifyPassword')}
        ]}
      //拼装动态路由
      menus.forEach( item =>{

        if(item.path){  //path有值时才添加路由
          let menu = { path: item.path.replace("/", ""), name: item.name, component: () => import( '../views/' + item.pagePath) }
          manageRoute.children.push(menu)
        }else if (item.children.length){
          item.children.forEach( item =>{ //否则直接添加其孩子路由
            if(item.path){
              let menu = { path: item.path.replace("/", ""), name: item.name, component: () => import( '../views/' + item.pagePath) }
              manageRoute.children.push(menu)
            }
          })
        }
      })
      //动态添加路由
      router.addRoute(manageRoute)
    }
  }
}

//重置我就set一次
setRouters()

router.beforeEach((to, from, next) => {
  //若路由找不到
  if (!to.matched.length){
    const menus = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).menus : null
    if (menus) {  //  已登录
      next("404")
    } else {  //  未登录
      next("login")
    }
  }
  //其他情况直接放行
  next()
})

export default router
