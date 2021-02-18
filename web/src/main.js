import Vue from 'vue'
import App from './App.vue'

import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
Vue.use(BootstrapVue)

import router from './router'; 

import VueQriously from 'vue-qriously'
Vue.use(VueQriously)

Vue.config.productionTip = false

new Vue({
  router,
  VueQriously, 
  render: h => h(App),
}).$mount('#app')