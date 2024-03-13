package cn.anlper.wiki.service;

import cn.anlper.wiki.Req.EbookQueryReq;
import cn.anlper.wiki.Req.EbookSaveReq;
import cn.anlper.wiki.domain.Ebook;
import cn.anlper.wiki.domain.EbookExample;
import cn.anlper.wiki.mapper.EbookMapper;
import cn.anlper.wiki.resp.EbookQueryResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.util.CopyUtil;
import cn.anlper.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;
    @Resource
    private SnowFlake snowFlake;
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName()))
            criteria.andNameLike("%" + req.getName() + "%");

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);


        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        System.out.println(pageResp);
        return pageResp;
    }

    public List<EbookQueryResp> all(EbookQueryReq req) {
        List<Ebook> ebookList = ebookMapper.selectByExample(null);
        return CopyUtil.copyList(ebookList, EbookQueryResp.class);
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }

}
