package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceimpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        User u = dao.findByUsername(user.getUsername());
        if(u != null) {
            //用户已存在
            return false;
        }
        //设置唯一激活码
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        dao.save(user);
        //发送激活邮件
        String content = "<a href='user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"杜扬发给你的激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code 身份证码
     * @return
     */
    @Override
    public boolean active(String code) {
        User user = dao.findByCode(code);
        if(user != null){
            dao.updateStatus(user);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    public User auto(String username){
        return dao.findByUsername(username);
    }
}
