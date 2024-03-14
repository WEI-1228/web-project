package cn.anlper.wiki.service;

import cn.anlper.wiki.Req.DocQueryReq;
import cn.anlper.wiki.Req.DocSaveReq;
import cn.anlper.wiki.domain.Content;
import cn.anlper.wiki.domain.Doc;
import cn.anlper.wiki.domain.DocExample;
import cn.anlper.wiki.mapper.ContentMapper;
import cn.anlper.wiki.mapper.DocMapper;
import cn.anlper.wiki.resp.DocQueryResp;
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
public class DocService {

    @Resource
    DocMapper docMapper;

    @Resource
    ContentMapper contentMapper;
    @Resource
    private SnowFlake snowFlake;
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);

        List<DocQueryResp> docQueryRespList = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setList(docQueryRespList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public void save(DocSaveReq docSaveReq) {
        Doc doc = CopyUtil.copy(docSaveReq, Doc.class);
        Content content = CopyUtil.copy(docSaveReq, Content.class);
        if (!ObjectUtils.isEmpty(doc.getId())) {
            docMapper.updateByPrimaryKey(doc);
            int cnt = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (cnt == 0) {
                contentMapper.insert(content);
            }
        } else {
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<Long> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public List<DocQueryResp> all(Long ebook_id) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");
        criteria.andEbookIdEqualTo(ebook_id);
        List<Doc> docList = docMapper.selectByExample(docExample);
        return CopyUtil.copyList(docList, DocQueryResp.class);
    }

    public String findContent(Long docId) {
        Content content = contentMapper.selectByPrimaryKey(docId);
        if (content != null) return content.getContent();
        return "";
    }
}
