package ai.zzt.okx.web.web;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.web.dto.UserLoginDTO;
import ai.zzt.okx.web.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@RestController
@RequestMapping("/pub/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public R<Object> login(UserLoginDTO req) {
        userService.login(req);
        return R.ok(null);
    }

    @GetMapping("/logout")
    public R<Void> logout() {
        return R.ok();
    }

}
