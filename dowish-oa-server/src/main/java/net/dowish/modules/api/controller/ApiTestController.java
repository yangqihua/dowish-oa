package net.dowish.modules.api.controller;


import net.dowish.common.utils.Apis;
import net.dowish.modules.api.annotation.AuthIgnore;
import net.dowish.modules.api.annotation.LoginUser;
import net.dowish.modules.api.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API测试接口
 *
 *
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/api")
@Api("测试接口")
public class ApiTestController {

    /**
     * 获取用户信息
     */
    @GetMapping("userInfo")
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
    public Apis userInfo(@LoginUser UserEntity user){
        return Apis.ok().put("user", user);
    }

    /**
     * 忽略Token验证测试
     */
    @AuthIgnore
    @GetMapping("notToken")
    @ApiOperation(value = "忽略Token验证测试")
    public Apis notToken(){
        return Apis.ok().put("msg", "无需token也能访问。。。");
    }
}
