package cn.anlper.wiki.service;

import cn.anlper.wiki.Req.UserLoginReq;
import cn.anlper.wiki.Req.UserQueryReq;
import cn.anlper.wiki.Req.UserResetPasswordReq;
import cn.anlper.wiki.Req.UserSaveReq;
import cn.anlper.wiki.domain.User;
import cn.anlper.wiki.domain.UserExample;
import cn.anlper.wiki.exception.BusinessException;
import cn.anlper.wiki.exception.BusinessExceptionCode;
import cn.anlper.wiki.mapper.UserMapper;
import cn.anlper.wiki.resp.PageResp;
import cn.anlper.wiki.resp.UserLoginResp;
import cn.anlper.wiki.resp.UserQueryResp;
import cn.anlper.wiki.util.CopyUtil;
import cn.anlper.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName()))
            criteria.andLoginNameEqualTo(req.getLoginName());

        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);


        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public List<UserQueryResp> all() {
        List<User> userList = userMapper.selectByExample(null);
        return CopyUtil.copyList(userList, UserQueryResp.class);
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // 前端校验不可信，如果用户已经存在，不能允许用户修改用户名
            user.setLoginName(null);
            // Selective表示，如果某个元素是空，那就不进行更新，这里已经确定用户存在，所以可以不进行更新
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) return null;
        return users.get(0);
    }

    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResp login(UserLoginReq req) {
        User userDb = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDb)) {
            LOG.info("用户名不存在,{}", req.getLoginName());
            // 用户名不存在
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (userDb.getPassword().equals(req.getPassword())) {
                // 登陆成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDb, UserLoginResp.class);
                return userLoginResp;
            } else {
                // 密码错误
                LOG.info("密码不对，输入密码：{}, 数据库密码：{}", req.getLoginName(), req.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }

    }
}
