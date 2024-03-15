package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.UserLoginReq;
import cn.anlper.wiki.Req.UserQueryReq;
import cn.anlper.wiki.Req.UserResetPasswordReq;
import cn.anlper.wiki.Req.UserSaveReq;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.resp.UserLoginResp;
import cn.anlper.wiki.resp.UserQueryResp;
import cn.anlper.wiki.service.UserService;
import cn.anlper.wiki.util.SnowFlake;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    @GetMapping("/list")
    public CommonResp<PageResp<UserQueryResp>> list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp<List<UserQueryResp>> all() {
        CommonResp<List<UserQueryResp>> resp = new CommonResp<>();
        List<UserQueryResp> list = userService.all();
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<Object> save(@RequestBody @Valid UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<Object> resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        CommonResp<Object> resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp<Object> resetPassword(@RequestBody @Valid UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<Object> resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@RequestBody @Valid UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        // 生成单点登陆token，并放入redis中
        Long token = snowFlake.nextId();
        LOG.info("生成单点登陆token：{}", token);
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
        userLoginResp.setToken(token.toString());


        resp.setContent(userLoginResp);
        return resp;
    }
}
