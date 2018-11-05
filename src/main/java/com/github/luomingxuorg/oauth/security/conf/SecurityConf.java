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
 *  File Name : SecurityConf.java
 *  Url: https://github.com/LuomingXuOrg/OAuth
 */

package com.github.luomingxuorg.oauth.security.conf;

import com.github.luomingxuorg.oauth.security.filter.JwtTokenFilter;
import com.github.luomingxuorg.oauth.security.handler.LogoutSuccessHandler;
import com.github.luomingxuorg.oauth.security.handler.UnauthorizedHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class SecurityConf extends WebSecurityConfigurerAdapter
{
//    @Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
//    {
//        authenticationManagerBuilder
//                .userDetailsService(this.userService)
//                .passwordEncoder(new Password());
//    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                //使用JWT, 不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedHandler()).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().permitAll()
                //已kkn结尾的路径需要进行验证
                .antMatchers("/**/kkn").authenticated();

        httpSecurity.logout().logoutUrl("/oauth/logout/{userName}/{pwd}")
                .logoutSuccessHandler(new LogoutSuccessHandler());

        // 添加JWT filter
        httpSecurity.addFilterBefore(
                new JwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }
}
