package org.cobra.web.ui.controller;

import com.alibaba.fastjson.JSON;
import org.cobra.web.bean.User;
import org.cobra.web.cache.CacheKey;
import org.cobra.web.cache.CacheService;
import org.cobra.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")// isAuthenticated 如果用户不是匿名用户就返回true
    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView("/index/index");
        try {
            User user = userService.loadUserByUsername("admin");
            modelAndView.addObject("username", user.getUsername());
            //测试缓存服务
            //缓存用户对象到redis,以用户ID区分
            cacheService.set(CacheKey.LOGIN_USER_KEY + user.getId(), JSON.toJSONString(user));
            //从缓存中取出
            String userStr = cacheService.get(CacheKey.LOGIN_USER_KEY + user.getId());
            //进行反序列化
            User u = JSON.parseObject(userStr, User.class);
            if (u != null) {
                logger.info("user:{}", u);
                System.out.println("user:{}" + " " + u);
            }
            logger.info("load user " + user);
            System.out.println("load user " + user);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return modelAndView;
    }
}

