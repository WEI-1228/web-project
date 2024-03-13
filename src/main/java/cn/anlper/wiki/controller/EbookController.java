package cn.anlper.wiki.controller;

import cn.anlper.wiki.Req.EbookReq;
import cn.anlper.wiki.resp.CommonResp;
import cn.anlper.wiki.resp.EbookResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp<PageResp<EbookResp>> list(EbookReq req) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp<List<EbookResp>> all(EbookReq req) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.all(req);
        resp.setContent(list);
        return resp;
    }
}
