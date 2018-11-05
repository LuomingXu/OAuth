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
 *  File Name : OAuthAutoConfiguration.java
 *  Url: https://github.com/LuomingXuOrg/OAuth
 */

package com.github.luomingxuorg.oauth.security.autoconfigure;

import com.github.luomingxuorg.oauth.security.conf.JwtTokenConf;
import com.github.luomingxuorg.oauth.security.conf.SecurityConf;
import com.github.luomingxuorg.oauth.security.filter.JwtTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtTokenConf.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class OAuthAutoConfiguration
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public OAuthAutoConfiguration()
    {
        logger.info("Enable OAuth repo success!");
        System.out.println("                      _\\ /       _  _             ");
        System.out.println("  |      _ __  o __ (_| X       / \\|_|   _|_|_    ");
        System.out.println("  |__|_|(_)||| | | |__|/ \\|_|   \\_/| ||_| |_| |  ");
        System.out.println("                                              0.0.1");
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter()
    {
        logger.info("Enable jwt token filter success!");
        return new JwtTokenFilter();
    }

    @Bean
    public SecurityConf securityConf()
    {
        logger.info("Enable security conf success");
        return new SecurityConf();
    }
}
