import Vue from 'vue';
import ViewUI from 'view-design';
import Util from '../libs/util';
import VueRouter from 'vue-router';
import Cookies from 'js-cookie';
import { routers, otherRouter } from './router';

Vue.use(VueRouter);

// 路由配置
const RouterConfig = {
    mode: 'history',
    routes: routers
};

export const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => { // to 是即将要去的页面
    ViewUI.LoadingBar.start();
    Util.title(to.meta.title);
    var name = to.name;
    // 当前锁定 访问其他页面   还是锁定页面
    if (Cookies.get('locking') == '1' && name !== 'locking') {
        next({
            replace: true,
            name: 'locking'
        });
    } 
    // 当前未锁定 访问锁定页面
    else if (Cookies.get('locking') == '0' && name == 'locking') {
        next(false);
    } 
    else {
        var whiteList = name != 'login' && name != 'regist' && name != 'regist-result' && name != 'relate' && name != 'reset' && name != 'authorize';
        // 未登入 拒绝非法访问
        if (!Cookies.get('userInfo') && whiteList) {
            next({
                name: 'login'
            });
        }
        // 已登入 自动登入系统
        else if (Cookies.get('userInfo') && name == 'login') {
            Util.title();
            next({
                name: 'home_index'
            });
        }
        // 其余情况放行
        else {
            Util.toDefaultPage([...routers], name, router, next);
        }
    }
});

router.afterEach((to) => {
    Util.openNewPage(router.app, to.name, to.params, to.query);
    ViewUI.LoadingBar.finish();
    window.scrollTo(0, 0);
});
