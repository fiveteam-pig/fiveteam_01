package com.fiveteam.controller;

import com.fiveteam.pojo.User;
import com.fiveteam.service.LoginService;
import com.fiveteam.util.AuthImageServlet;
import com.fiveteam.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goLogin")
public class LoginController {

    @Autowired
    private LoginService loginservice;

    /**
     * 注册页面
     * @return
     */
    @RequestMapping("/tologin")
    public String toregister(){
        return "login";
    }

    @RequestMapping("/login")
    public String login (User user, HttpSession session,String randomCode){
        user.setPasswd(MD5Utils.MD5Encode(user.getPasswd(),"utf-8"));
        User u =  loginservice.login(user.getMobile());
        String rand = (String) session.getAttribute("rand");//图片验证码

        if(u==null){

            return "error";
        }
        if(u.getMobile()==null){
            session.setAttribute("mobileError","手机号错误");
            return "error";
        }
        if(u.getPasswd()==null){
            session.setAttribute("passwdError","请输入正确的密码");
            return "error";
        }
        if(randomCode==null||rand!=randomCode){
            session.setAttribute("imageError","图形验证码错误");
            return "error";
        }
        return "list";
    }

    @RequestMapping("/AuthImageServlet")
    public void AuthImageServlet (HttpServletRequest request, HttpServletResponse response){
        new AuthImageServlet().createImage(request,response);
    }



}
