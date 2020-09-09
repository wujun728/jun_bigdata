import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import dataV from "@jiaminghi/data-view";
Vue.use(dataV);

// 引入vue-awesome
import Icon from "vue-awesome/components/Icon";
import "vue-awesome/icons/index.js";
// 全局注册图标
Vue.component("icon", Icon);

// 适配flex
import "@/common/flexible.js";

// 引入全局css
import "./assets/scss/style.scss";

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
