package top.lrshuai.common.security.interceptor;

import cn.dev33.satoken.same.SaSameUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 登录校验拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 仅处理HandlerMethod类型的处理器（控制器方法）
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true; // 非控制器方法直接放行
        }
        // 验证同源
        SaSameUtil.checkCurrentRequestToken();
        return true;
    }


}
