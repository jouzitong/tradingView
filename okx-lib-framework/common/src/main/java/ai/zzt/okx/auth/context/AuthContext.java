package ai.zzt.okx.auth.context;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
public class AuthContext {

    private static final ThreadLocal<Object> tokenLocal = new ThreadLocal<>();

    public static void setToken(Object token) {
        tokenLocal.set(token);
    }

    public static Object getToken() {
        return tokenLocal.get();
    }

    public static void clearToken() {
        tokenLocal.remove();
    }

}
