package cn.anlper.wiki.service;

import cn.anlper.wiki.Req.CategoryQueryReq;
import cn.anlper.wiki.domain.Category;
import cn.anlper.wiki.domain.CategoryExample;
import cn.anlper.wiki.mapper.CategoryMapper;
import cn.anlper.wiki.resp.CategoryQueryResp;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);

        List<CategoryQueryResp> categoryQueryRespList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setList(categoryQueryRespList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

}
