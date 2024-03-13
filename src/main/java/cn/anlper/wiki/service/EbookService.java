package cn.anlper.wiki.service;

import cn.anlper.wiki.Req.EbookReq;
import cn.anlper.wiki.domain.Ebook;
import cn.anlper.wiki.domain.EbookExample;
import cn.anlper.wiki.mapper.EbookMapper;
import cn.anlper.wiki.resp.EbookResp;
import cn.anlper.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName()))
            criteria.andNameLike("%" + req.getName() + "%");

        PageHelper.startPage(1, 3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }
//        return respList;
        return CopyUtil.copyList(ebookList, EbookResp.class);
    }
}
