package com.example.xiaosage.utils;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtils {
    public static String generateToken(String userId,String sign){
        return JWT.create().withAudience(userId)    //将userId保存到token里，作为载荷
                    .withExpiresAt(DateUtil.offsetHour(new Date(),2))   //两小时后token过期
                    .sign(Algorithm.HMAC256(sign));    //以password作为token的密钥
    }
}
