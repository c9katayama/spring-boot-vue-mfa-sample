import Vue from 'vue';
import Router from 'vue-router';
import Home from './pages/Home.vue';
import Login from './pages/Login.vue';
import Logout from './pages/Logout.vue';
import RegisterMFA from './pages/RegisterMFA.vue';

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    { name:'login', path: '/', component: Login },
    { name:'logout', path: '/logout', component: Logout },
    { name:'home', path: '/home', component: Home },
    { name:'register-mfa', path: '/register', component: RegisterMFA },
  ]
});