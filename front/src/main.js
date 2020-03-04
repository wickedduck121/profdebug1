import Vue from 'vue';
import ElementUi from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import locale from 'element-ui/lib/locale/lang/en'
import App from './App.vue'
import TableWithColour from "./components/TableWithColour";
import FacultyTable from "./components/FacultyTable";
import Router from 'vue-router';

Vue.config.productionTip = false;
Vue.use(ElementUi, { locale });

const routes = [
  { path: '/foo', component: TableWithColour },
  { path: '/bar', component: FacultyTable }
];
const router = new VueRouter({
  routes // short for `routes: routes`
})
new Vue({
  render: h => h(App),
}).$mount('#app');
