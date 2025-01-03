package ai.zzt.okx.web.service;

import ai.zzt.okx.web.dto.UserLoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@Service
@Slf4j
public class UserService {

    public void login(UserLoginDTO req) {
        log.info("login req: {}", req);
    }

    public void logout() {
        log.info("logout");
    }


}
