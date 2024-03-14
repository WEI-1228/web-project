package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.CategoryQueryReq;
import cn.anlper.wiki.resp.CategoryQueryResp;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp<PageResp<CategoryQueryResp>> list(CategoryQueryReq categoryQueryReq) {
        CommonResp<PageResp<CategoryQueryResp>> commonResp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        commonResp.setContent(list);
        return commonResp;
    }

}
