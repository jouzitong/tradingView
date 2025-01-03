package ai.zzt.okx.common;

import ai.zzt.okx.common.event.EventType;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.utils.EfficiencyMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * 主要是解决服务器不能访问https的问题, 和代理问题
 *
 * @author zhouzhitong
 * @since 2024/6/20
 **/
@Slf4j
@SpringBootApplication(scanBasePackages = "ai.zzt.okx")
public class Application {

//    static {
//        disableSSLVerification();
//    }

    public static void main(String[] args) {
        EfficiencyMonitor.start(args, EventType.SYSTEM_START);
        SpringApplication.run(Application.class, args);
        SystemContext.open();
        long end = EfficiencyMonitor.end();
        log.info("初始化完成, 系统已启动. 耗时 {} ms【{} 秒】", end, (end / 1000));
    }

    public static void disableSSLVerification() {
        // TODO 临时加上解决服务器不能访问https的问题, 还没研究明白.
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
        log.info("init ssl context. disable ssl verification.");
        log.info("proxy settings. http.proxyHost=127.0.0.1, http.proxyPort=7890, https.proxyHost=127.0.0.1, https.proxyPort=7890");
        System.setProperty("https.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create an all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
