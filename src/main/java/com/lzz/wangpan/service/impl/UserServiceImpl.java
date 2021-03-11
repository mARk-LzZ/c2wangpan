package com.lzz.wangpan.service.impl;

import com.lzz.wangpan.WangpanApplication;
import com.lzz.wangpan.config.SettingConfig;
import com.lzz.wangpan.config.TokenConfig;
import com.lzz.wangpan.mapper.UserMapper;
import com.lzz.wangpan.entity.User;
import com.lzz.wangpan.modules.constant.ConfigConsts;
import com.lzz.wangpan.service.IUserService;
import com.lzz.wangpan.util.BeanUtils;
import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.Checker;
import com.zhazhapan.util.DateUtils;
import com.zhazhapan.util.MailSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {this.userMapper=userMapper;}
    @Autowired
    HttpServletRequest request;

    @Override
    public boolean updatePermission(int id, int permission) {
        return userMapper.updatePermission(id, permission > 2 ? 2 : permission);
    }

    @Override
    public boolean resetPassword(int id, String password) {
        boolean result = Checker.isNotEmpty(password) && userMapper.updatePasswordById(id, password);
        if (result) {
            TokenConfig.removeTokenByValue(id);
            try {
                MailSender.sendMail(getUserById(id).getEmail(), "密码重置通知", "您的密码已被管理员重置为：" + password);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public boolean updateFileAuth(int id, String auths) {
        int[] auth = BeanUtils.getAuth(auths);
        return userMapper.updateAuthById(id, auth[0], auth[1], auth[2], auth[3], auth[4]);
    }

    @Override
    public List<User> listUser(int permission, String condition, int offset) {
        return userMapper.listUserBy(permission, condition, offset);
    }

    @Override
    public User login(String loginName, String password, String token, HttpServletResponse response) {
        boolean allowLogin = WangpanApplication.settings.getBooleanUseEval(ConfigConsts.ALLOW_LOGIN_OF_SETTINGS);
        User user = null;
        if (allowLogin) {
            if (Checker.isNotEmpty(token) && WangpanApplication.tokens.containsKey(token)) {
                user = userMapper.getUserById(WangpanApplication.tokens.get(token));
                if (Checker.isNotNull(response)) {
                    Cookie cookie = new Cookie(ValueConsts.TOKEN_STRING, TokenConfig.generateToken(token, user.getId
                            ()));
                    cookie.setMaxAge(30 * 24 * 60 * 60);
                    response.addCookie(cookie);
                }
            }
            if (Checker.isNull(user) && Checker.isNotEmpty(loginName) && Checker.isNotEmpty(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user",user);
                session.setAttribute("sessusername",loginName);

                user = userMapper.login(loginName, password);
                if (Checker.isNotNull(user)) {
                    TokenConfig.removeTokenByValue(user.getId());
                }
            }
            updateUserLoginTime(user);
        }
        return user;
    }

    @Override
    public boolean register(String username, String email, String password) {
        boolean allowRegister = WangpanApplication.settings.getBooleanUseEval(ConfigConsts.ALLOW_REGISTER_OF_SETTINGS);
        if (allowRegister) {
            boolean isValid = checkPassword(password) && Pattern.compile(WangpanApplication.settings
                    .getStringUseEval(ConfigConsts.USERNAME_PATTERN_OF_SETTINGS)).matcher(username).matches();
            if (isValid) {
                User user = new User(username, ValueConsts.EMPTY_STRING, email, password);
                int[] auth = SettingConfig.getAuth(ConfigConsts.USER_DEFAULT_AUTH_OF_SETTING);
                user.setAuth(auth[0], auth[1], auth[2], auth[3], auth[4]);
                return userMapper.insertUser(user);
            }
        }
        return false;
    }

    @Override
    public boolean resetPasswordByEmail(String email, String password) {
        return Checker.isEmail(email) && checkPassword(password) && userMapper.updatePasswordByEmail(password, email);
    }

    @Override
    public boolean checkPassword(String password) {
        int min = WangpanApplication.settings.getIntegerUseEval(ConfigConsts.PASSWORD_MIN_LENGTH_OF_SETTINGS);
        int max = WangpanApplication.settings.getIntegerUseEval(ConfigConsts.PASSWORD_MAX_LENGTH_OF_SETTINGS);
        return Checker.isLimited(password, min, max);
    }

    @Override
    public boolean emailExists(String email) {
        return Checker.isEmail(email) && userMapper.checkEmail(email) > 0;
    }

    @Override
    public boolean updateBasicInfoById(int id, String avatar, String realName, String email) {
        return Checker.isEmail(email) && userMapper.updateBasicInfo(id, Checker.checkNull(avatar), Checker.checkNull
                (realName), email);
    }

    @Override
    public int getUserId(String usernameOrEmail) {
        try {
            return userMapper.getUserId(Checker.checkNull(usernameOrEmail));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean usernameExists(String username) {
        return Checker.isNotEmpty(username) && userMapper.checkUsername(username) > 0;
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void updateUserLoginTime(User user) {
        if (Checker.isNotNull(user)) {
            user.setLastLoginTime(DateUtils.getCurrentTimestamp());
            userMapper.updateUserLoginTime(user.getId());
        }
    }

    @Override
    public boolean updatePasswordById(String password, int id) {
        return checkPassword(password) && userMapper.updatePasswordById(id, password);
    }
}
