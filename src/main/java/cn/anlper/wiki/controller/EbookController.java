package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.EbookQueryReq;
import cn.anlper.wiki.Req.EbookSaveReq;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.EbookQueryResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<PageResp<EbookQueryResp>> list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp<List<EbookQueryResp>> all(EbookQueryReq req) {
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> list = ebookService.all(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp<Object> save(@RequestBody EbookSaveReq req) {
        CommonResp<Object> resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
