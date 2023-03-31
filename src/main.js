import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css'
import request from "@/utils/request";

Vue.config.productionTip = false

Vue.use(ElementUI, {size:"mini"});

Vue.prototype.request=request

// 全页面自动回滚顶部
router.afterEach((to,from,next) => {
  window.scrollTo(0,0);
});

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
