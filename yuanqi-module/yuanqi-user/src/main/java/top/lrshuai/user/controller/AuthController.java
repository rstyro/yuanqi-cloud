package top.lrshuai.user.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.common.core.enums.ApiResultEnum;
import top.lrshuai.common.core.exception.ServiceException;
import top.lrshuai.common.core.resp.R;
import top.lrshuai.common.core.web.BaseController;
import top.lrshuai.user.api.dto.LoginDto;
import top.lrshuai.user.api.entity.User;
import top.lrshuai.user.service.IUserService;


/**
 * 登录相关
 */
@Slf4j
@Tag(name = "auth",description = "登录相关")
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    public static final String LOGIN_USER_KEY = "loginUser";

    @Resource
    private IUserService userService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginDto dto){
        // 登录简单查询就行,不用校验密码啥的
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername,dto.getUsername()));
        if(user == null){
            throw new ServiceException(ApiResultEnum.SYSTEM_ACCOUNT_NOT_FOUND);
        }
        StpUtil.login(user.getId());
        SaSession tokenSession = StpUtil.getTokenSession();
        tokenSession.set(LOGIN_USER_KEY, user);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return R.ok(tokenInfo);
    }

}
