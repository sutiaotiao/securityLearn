package com.sd.untils;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sd.pojo.User;
import org.springframework.util.StringUtils;
import static com.auth0.jwt.JWT.create;
import static com.auth0.jwt.JWT.decode;

import java.util.Date;


public class TokenUtil {

    /**
     * 常量:
     */
    //token中存放用户id对应的名字
    private static final String CLAIM_NAME_USERID = "CLAIM_NAME_USERID";
    //token中存放电话号码对应的名字
    private static final String CLAIM_NAME_USER_NAME = "CLAIM_NAME_USER_NAME ";


    public static String sign(User user, String securityKey) {
        String token = create()
                .withClaim(CLAIM_NAME_USERID, user.getId())
                .withClaim(CLAIM_NAME_USER_NAME, user.getUserName())
                .withIssuedAt(new Date())//发行时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 60*60*1000*24 ))//有效时间
                // 指定签名
                .sign(Algorithm.HMAC256(securityKey));
        return token;
    }

    /**
     * 将当前用户信息以用户密码为密钥生成token的方法
     */
    public static String loginSign(User user, String password) {
        //生成token
        String token = sign(user, password);
        return token;
    }

    /**
     * 从客户端归还的token中获取用户信息的方法
     */
    public static User getCurrentUser(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("令牌为空，请登录！");
        }
        //对token进行解码,获取解码后的token
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = decode(token);
        } catch (JWTDecodeException e) {
            throw new RuntimeException("令牌格式错误，请登录！");
        }
        //从解码后的token中获取用户信息并封装到CurrentUser对象中返回
        int userId = decodedJWT.getClaim(CLAIM_NAME_USERID).asInt();//用户账号id
        String userName = decodedJWT.getClaim(CLAIM_NAME_USER_NAME).asString();//用户姓名
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("令牌缺失用户信息，请登录！");
        }
        User user = new User();
        user.setId(userId);
        user.setUserName(userName);
        return user;

    }

}