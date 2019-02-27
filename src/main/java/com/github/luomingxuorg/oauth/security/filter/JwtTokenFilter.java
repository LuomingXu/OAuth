/*
 *  Copyright 2018-2019 LuomingXuOrg
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
 *  File Name : JwtTokenFilter.java
 *  Url: https://github.com/LuomingXuOrg/OAuth
 */

package com.github.luomingxuorg.oauth.security.filter;

import com.github.luomingxuorg.oauth.security.conf.Token;
import com.github.luomingxuorg.oauth.security.util.CookiesUtil;
import com.github.luomingxuorg.oauth.security.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class JwtTokenFilter extends OncePerRequestFilter
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

        String token;
        String userName = null;

        //get token from header
        String header = request.getHeader(Token.Jwt_Token);
        if (header != null && header.startsWith(Token.Bearer))
        {
            token = header.substring(Token.Bearer.length() + 1);
            userName = JwtUtil.getUsername(token);
        }
        //get token from cookie
        else
        {
            token = CookiesUtil.getJwtTokenFromCookies(request.getCookies());
            //与本地的session进行比较, 如果进行了logout操作, 则无法获取到session里面的token
            //就设置token为null, 则无法进行权限的读取
            if (token != null && token.equals(request.getSession().getAttribute(Token.Jwt_Token)))
            {
                userName = JwtUtil.getUsername(token);
            }
            else
            {
                token = null;
            }
        }

        logger.info("Checking jwt_token's Possessor: " + userName);

        //set authorities by token
        //trust token, not valid authority from DB.
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            logger.info("Invalid date: " + sdf.format(JwtUtil.getExpirationDate(token)));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userName, JwtUtil.getUserId(token), JwtUtil.getAuthority(token));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            logger.info("Authenticated, setting security context.");

            //将用户信息存入Holder
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
