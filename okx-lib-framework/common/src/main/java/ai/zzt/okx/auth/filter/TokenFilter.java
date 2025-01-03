package ai.zzt.okx.auth.filter;

import ai.zzt.okx.auth.context.AuthContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/api/*")  // 设置过滤器作用于所有请求
public class TokenFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化时执行
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取请求头中的 Token
        String token = httpRequest.getHeader("Authorization");
        // 校验 Token
        if (!isValidToken(token)) {
            // 如果 Token 不合法，返回 401 Unauthorized
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: Invalid Token");
            return;
        }
        try {
            // 将 Token 存储在 ThreadLocal 中
            AuthContext.setToken(token);
            // Token 校验通过，继续请求
            chain.doFilter(request, response);
        } finally {
            AuthContext.clearToken();
        }
    }

    private boolean isValidToken(String token) {
        // 假设这里通过某种方式校验 Token，比如解析 JWT，或者通过数据库等方式验证
        // 这里只是一个简单的示例
        return "valid-token".equals(token);
    }

    @Override
    public void destroy() {
        // 销毁时执行
    }
}
