package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.DocQueryReq;
import cn.anlper.wiki.Req.DocSaveReq;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.DocQueryResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping("/all/{ebook_id}")
    public CommonResp<List<DocQueryResp>> all(@PathVariable Long ebook_id) {
        CommonResp<List<DocQueryResp>> commonResp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebook_id);
        commonResp.setContent(list);
        return commonResp;
    }

    @GetMapping("/find-content/{doc_id}")
    public CommonResp<String> findContent(@PathVariable Long doc_id) {
        CommonResp<String> commonResp = new CommonResp<>();
        String list = docService.findContent(doc_id);
        commonResp.setContent(list);
        return commonResp;
    }

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DocSaveReq docSaveReq) {
        CommonResp<Object> commonResp = new CommonResp<>();
        docService.save(docSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{idString}")
    public CommonResp<Object> delete(@PathVariable String idString) {
        CommonResp<Object> commonResp = new CommonResp<>();
        String[] list = idString.split(",");
        List<Long> longList = new ArrayList<>();
        for (String s: list)
            longList.add(Long.valueOf(s));
        docService.delete(longList);
        return commonResp;
    }

}
