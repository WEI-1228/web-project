package cn.anlper.wiki.service;

import cn.anlper.wiki.Req.CategoryQueryReq;
import cn.anlper.wiki.Req.CategorySaveReq;
import cn.anlper.wiki.domain.Category;
import cn.anlper.wiki.domain.CategoryExample;
import cn.anlper.wiki.mapper.CategoryMapper;
import cn.anlper.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    private SnowFlake snowFlake;
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

    public void save(CategorySaveReq categorySaveReq) {
        Category category = CopyUtil.copy(categorySaveReq, Category.class);
        if (!ObjectUtils.isEmpty(category.getId())) {
            categoryMapper.updateByPrimaryKey(category);
        } else {
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    public List<CategoryQueryResp> all() {
        List<Category> categoryList = categoryMapper.selectByExample(null);
        return CopyUtil.copyList(categoryList, CategoryQueryResp.class);
    }
}
