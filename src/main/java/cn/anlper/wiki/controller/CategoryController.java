package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.CategoryQueryReq;
import cn.anlper.wiki.Req.CategorySaveReq;
import cn.anlper.wiki.resp.CategoryQueryResp;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/all")
    public CommonResp<List<CategoryQueryResp>> all() {
        CommonResp<List<CategoryQueryResp>> commonResp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        commonResp.setContent(list);
        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody CategorySaveReq categorySaveReq) {
        CommonResp<Object> commonResp = new CommonResp<>();
        categoryService.save(categorySaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        CommonResp<Object> commonResp = new CommonResp<>();
        categoryService.delete(id);
        return commonResp;
    }

}
