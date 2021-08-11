package com.njupt.dzyh.config;


import com.njupt.dzyh.beans.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShiroFilter extends FormAuthenticationFilter {

    //加密的字符串,相当于签名
    private static final String SINGNATURE_TOKEN = "加密token";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //这里只有返回false才会执行onAccessDenied方法,因为
        // return super.isAccessAllowed(request, response, mappedValue);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        String token = getRequestToken((HttpServletRequest) request);
        String login = ((HttpServletRequest) request).getServletPath();

        //如果为登录,就放行
        if ("/user/login".equals(login)){
            return true;
        }
        if (StringUtils.isBlank(token)){
            System.out.println("没有token");
            return false;
        }

        //从当前shiro中获得用户信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) return false;
        //对当前ID进行SHA256加密
        String encryptionKey= DigestUtils.sha256Hex(SINGNATURE_TOKEN+user.getUserId());
        if (encryptionKey.equals(token)){
            return true;
        }else{
            System.out.println("无效token");
        }
        return false;
    }
    private String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        return token;
    }
}

