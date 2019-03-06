package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceimpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceimpl();
    //验证码验证
    public boolean getCheckcode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //验证校验
        String check = request.getParameter("check");
        //获取session中的验证码
        HttpSession session = request.getSession();
        String checkcode_session = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_session == null || !checkcode_session.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValue(info,response);
            return false;
        }
        return true;
    }

    //注册
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!getCheckcode(request,response)){
            return;
        }
        //获取数据封装到user对象
        User user = new User();
        Map<String, String[]> map = request.getParameterMap();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        if(flag){
            //注册成功
            info.setFlag(true);
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }
        writeValue(info,response);
    }


    //激活
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if(code != null) {
            //调用service完成激活
            boolean flag = service.active(code);
            String msg = null;
            if(flag){
                msg = "激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            }else{
                msg = "激活失败，请联系管理员！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }


    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!getCheckcode(request,response)){
            return;
        }
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User u  = service.login(user);
        ResultInfo info = new ResultInfo();
        if(u == null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        if (u != null && !"Y".equals(u.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        if(u != null && "Y".equals(u.getStatus())){
            info.setFlag(true);
            //将登陆成功的用户存储到session域里
            HttpSession session = request.getSession();
            session.setAttribute("user",u);
            /*//自动登录
            try {
                String auto = request.getParameter("auto");
                if (auto.equals("on")){
                    HttpSession session1 = request.getSession();
                    session1.setAttribute("username",u.getUsername());
                    session1.setMaxInactiveInterval(3600*24*7);//一周
                    Cookie sessionId = new Cookie("JSESESSIONID",session1.getId());
                    sessionId.setMaxAge(24*60*7);//设置一周
                    sessionId.setPath("/");
                    response.addCookie(sessionId);
                }
            } catch (Exception e) {

            }*/
        }
        writeValue(info,response);
    }


    //回显用户名
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        writeValue(user,response);
    }


    //退出用户
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/login.html");
    }
   /* //自动登录
    public void auto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String)request.getSession().getAttribute("username");
        System.out.println(username);
        User user = service.auto(username);
        if(user != null){
            writeValue(true,response);
        }else{
            writeValue(false,response);
        }
    }*/
}
