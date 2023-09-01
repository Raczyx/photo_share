package com.example.photo_share.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
@Data
public class JwtUtil {

    @Value("${myproperty.overtime}")
    private Integer time;

    @Value("${myproperty.header}")
    private String Header;

    private Calendar calendar = Calendar.getInstance();

    private String secret = "1596700738@qq.com";
    /*
    注意啊，这里故意反着来
     */
    public String buildJWT(HashMap header, String username, Map<String,String> payload){
        calendar.add(Calendar.SECOND, time);
        JWTCreator.Builder builder = JWT.create();
//        String token = JWT.create()
//                .withClaim("id", encryptorUtil.AESEncode(username))
//                .withClaim("username", encryptorUtil.AESEncode(id))
//                .withHeader(header)
//                .withExpiresAt(calendar.getTime())
//                .sign(Algorithm.HMAC512(secret));
        builder.withHeader(header);
        builder.withClaim("username",username);
        payload.forEach((k,v) -> builder.withClaim(k,v));
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC512(secret));
        return token;
    }

    /**
     * 解析jwt
     * @param token
     * @return
     */

    public DecodedJWT dncodeJwt(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(secret)).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT;
        }catch (Exception e){
            log.info("jwt解析出现错误");
        }

        return null;
    }

    public boolean isTokenExpired(DecodedJWT decodedJWT){
        return decodedJWT.getClaim("exp").asDate().before(new Date());
    }

//    public void ReturnJson(HttpServletResponse response, String msg) throws IOException {
//        String string = new ObjectMapper().writeValueAsString(new JsonResult<>(JsonResult.FAIL, msg, null));
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//
//        out.write(string);
//        out.flush();
//        out.close();
//    }
}
