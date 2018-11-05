/*
 *  Copyright 2018-2018 LuomingXuOrg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Author : Luoming Xu
 *  File Name : JwtUtil.java
 *  Url: https://github.com/LuomingXuOrg/OAuth
 */

package com.github.luomingxuorg.oauth.security.util;


import com.github.luomingxuorg.oauth.security.conf.JwtTokenConf;
import com.github.luomingxuorg.oauth.security.userdetails.UserDetailsExtend;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

public class JwtUtil
{
    private static final String CLAIM_KEY_OWNER = "KeyOwner";
    private static final String CLAIM_KEY_CREATED = "Created";
    private static final String CLAIM_KEY_ROLE_AUTHORITY = "OwnerRolesAuthorities";

    private static final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.jks");
    private static final char[] filePassword = JwtTokenConf.JwtPwd.toCharArray();
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    static
    {
        try
        {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, filePassword);
            privateKey = (PrivateKey) keyStore.getKey("jwt", filePassword);
            publicKey = keyStore.getCertificate("jwt").getPublicKey();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static final Long expiration = JwtTokenConf.JwtTokenExpiration;

    private static Date generateExpirationDate()
    {
        return new Date(System.currentTimeMillis() + expiration);
    }

    public static Date getExpirationDate(String token)
    {
        Claims claims = getClaims(token);
        return claims != null ? claims.getExpiration() : null;
    }

    private static Claims getClaims(String token)
    {
        if (token == null) { return null; }

        Claims claims;
        try
        {
            claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(AESenc.decrypt(token))
                    .getBody();
        }
        catch (Exception e) { claims = null; }

        return claims;
    }

    private static Map<String, Object> setClaims(UserDetailsExtend userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_OWNER, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ROLE_AUTHORITY, userDetails.getAuthorities());

        return claims;
    }

    @SuppressWarnings("unchecked")
    public static List<GrantedAuthority> getAuthority(String token)
    {
        Claims claims = getClaims(token);
        List<GrantedAuthority> authorities = null;

        if (claims != null)
        {
            authorities = new ArrayList<>();
            List<Map<String, String>> list = (List<Map<String, String>>) claims.get(CLAIM_KEY_ROLE_AUTHORITY);
            for (Map<String, String> item : list)
            {
                authorities.add(new SimpleGrantedAuthority(item.get("authority")));
            }
        }

        return authorities;
    }

    public static Boolean isTokenEffective(String token)
    {
        Date expiration = getExpirationDate(token);
        return expiration != null && expiration.after(new Date());
    }

    public static Long getUserId(String token)
    {
        Claims claims = getClaims(token);
        return claims != null ? Long.valueOf(claims.getId()) : null;
    }

    public static String getUsername(String token)
    {
        Claims claims = getClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public static Date getCreatedDate(String token)
    {
        Claims claims = getClaims(token);
        return claims != null ? new Date((Long) claims.get(CLAIM_KEY_CREATED)) : null;
    }

    public static <T extends UserDetailsExtend> T getUser(T user, String token)
    {
        Claims claims = getClaims(token);
        if (claims != null)
        {
            user.setUsername(claims.getSubject());
            user.setUserid(Long.valueOf(claims.getId()));
            user.setAuthority(getAuthority(token));
        }

        return user;
    }

    /**
     * 生成一个jwt_token
     * <ul>
     * <li>subject: user_name</li>
     * <li>id: user_id</li>
     * </ul>
     *
     * @param userDetails
     * @return
     */
    public static String generateToken(UserDetailsExtend userDetails)
    {
        return AESenc.encrypt(Jwts.builder()
                .setClaims(setClaims(userDetails))
                .setSubject(userDetails.getUsername())
                .setId(userDetails.getUserid().toString())
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.RS512, privateKey)
                .compact());
    }
}

