package cn.anlper.wiki.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器：Spring框架特有的，常用于登陆校验，权限校验，请求日志打印
 */
@Component
public class LogInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("------------- LogInterceptor 开始 ----------------");
        LOG.info("请求地址：{} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("远程地址：{}", request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long startTime = (Long) request.getAttribute("requestStartTime");
        LOG.info("--------------- LogInterceptor 结束 耗时：{} ms ---------------", System.currentTimeMillis() - startTime);
    }
}
