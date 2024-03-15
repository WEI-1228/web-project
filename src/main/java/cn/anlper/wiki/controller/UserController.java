package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.UserQueryReq;
import cn.anlper.wiki.Req.UserSaveReq;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.UserQueryResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

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
}
