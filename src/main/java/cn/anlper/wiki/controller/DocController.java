package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.DocQueryReq;
import cn.anlper.wiki.Req.DocSaveReq;
import cn.anlper.wiki.resp.DocQueryResp;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping("/list")
    public CommonResp<PageResp<DocQueryResp>> list(DocQueryReq docQueryReq) {
        CommonResp<PageResp<DocQueryResp>> commonResp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docQueryReq);
        commonResp.setContent(list);
        return commonResp;
    }

    @GetMapping("/all")
    public CommonResp<List<DocQueryResp>> all() {
        CommonResp<List<DocQueryResp>> commonResp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        commonResp.setContent(list);
        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DocSaveReq docSaveReq) {
        CommonResp<Object> commonResp = new CommonResp<>();
        docService.save(docSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        CommonResp<Object> commonResp = new CommonResp<>();
        docService.delete(id);
        return commonResp;
    }

}
