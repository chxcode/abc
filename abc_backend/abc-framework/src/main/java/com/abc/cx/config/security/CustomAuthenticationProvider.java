package com.abc.cx.config.security;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.abc.cx.config.Configure;
import com.abc.cx.service.impl.UserServiceImpl;
import com.abc.cx.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @Author: ChangXuan
 * @Decription: 自定义认证器
 * @Date: 16:55 2021/3/22
 **/
@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {



    // ~ Static fields/initializers
    // =====================================================================================

    /**
     * The plaintext password used to perform
     * PasswordEncoder#matches(CharSequence, String)}  on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    // ~ Instance fields
    // ================================================================================================


    private PasswordEncoder passwordEncoder;


    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    private UserServiceImpl userDetailsService;

//    private UserDetailsPasswordService userDetailsPasswordService;

    private RSA rsa;

    private Configure configure;

    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setConfigure(Configure configure) {
        this.configure = configure;
    }

    @Autowired
    public void setRsa(RSA rsa) {
        this.rsa = rsa;
    }

    public CustomAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        // RSA 解密凭证
        String presentedPassword = StrUtil.str(rsa.decrypt(Base64.decode(authentication.getCredentials().toString()), KeyType.PrivateKey), CharsetUtil.CHARSET_UTF_8);

        // 凭证有效性校验
        if (StrUtil.length(presentedPassword) < 11) {
            throw new BadCredentialsException("密码错误");
        }
        long nowTimestamp = DateUtil.currentSeconds();
        long submitTimestamp = Long.parseLong(StrUtil.sub(presentedPassword, presentedPassword.length()-10, presentedPassword.length()));

        if (nowTimestamp - submitTimestamp > configure.getPwdTimeout()) {
            throw new BadCredentialsException("凭证失效，请重新登录！");
        }

        // 验证码校验
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        String uuid = details.getUuid();
        String code = details.getCode();

        String cacheCode = redisUtil.getString(uuid);
        if (StrUtil.isBlank(cacheCode)) {
            throw new BadCaptchaException("验证码失效，请刷新后登录！");
        }
        redisUtil.del(uuid);
        if (StrUtil.isBlank(code) || !StrUtil.equalsIgnoreCase(code, cacheCode)) {
            throw new BadCaptchaException("验证码错误！");
        }

        String pwd = StrUtil.sub(presentedPassword, -10, 0);

        if (!passwordEncoder.matches(pwd, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    @Override
    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    @Override
    protected final UserDetails retrieveUser(String username,
                                             UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            // 防范计时攻击
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

//    @Override
//    protected Authentication createSuccessAuthentication(Object principal,
//                                                         Authentication authentication, UserDetails user) {
//        boolean upgradeEncoding = this.userDetailsPasswordService != null
//                && this.passwordEncoder.upgradeEncoding(user.getPassword());
//        if (upgradeEncoding) {
//            String presentedPassword = authentication.getCredentials().toString();
//            String newPassword = this.passwordEncoder.encode(presentedPassword);
//            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
//        }
//        return super.createSuccessAuthentication(principal, authentication, user);
//    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Autowired
    public void setUserDetailsService(UserServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

//    @Autowired
//    public void setUserDetailsPasswordService(
//            UserDetailsPasswordService userDetailsPasswordService) {
//        this.userDetailsPasswordService = userDetailsPasswordService;
//    }

}
