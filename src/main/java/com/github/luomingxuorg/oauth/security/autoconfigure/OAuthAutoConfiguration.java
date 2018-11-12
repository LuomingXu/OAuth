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
import com.github.luomingxuorg.oauth.security.exception.OAuthException;
import com.github.luomingxuorg.oauth.security.filter.JwtTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtTokenConf.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class OAuthAutoConfiguration
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JwtTokenConf jwtTokenConf;

    public OAuthAutoConfiguration(JwtTokenConf jwtTokenConf)
    {
        this.jwtTokenConf = jwtTokenConf;

        logger.info("Enable OAuth repo success!");
        System.out.println("  _                    ");
        System.out.println(" / \\  /\\     _|_ |_  ");
        System.out.println(" \\_/ /--\\ |_| |_ | | ");
        System.out.println("                      0.0.2");
    }

    @PostConstruct
    public void validProperty()
    {
        try
        {
            //keytool -genkey -alias jwt -keyalg  RSA -keysize 1024 -validity 365 -keystore jwt.jks
            File file = new ClassPathResource("jwt.jks").getFile();

            if (!(file.exists() && file.length() >= 0))
            {
                throw new IOException("Required file: \"jwt.jks\" is not exist in resources dir!");
            }

            String jwtPwd = jwtTokenConf.getJwtPwd();
            String aesPwd = jwtTokenConf.getAesConf().getAesPwd();
            if (jwtPwd == null || jwtPwd.equals(""))
            {
                throw new OAuthException("JwtPwd can not be null!");
            }
            if (aesPwd == null || aesPwd.equals(""))
            {
                throw new OAuthException("Aes pwd can not be null");
            }

            logger.info("JwtTokenPwd: {}", jwtPwd);
            logger.info("AesPwd: {}", aesPwd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            failedStart(e);
        }
    }

    private void failedStart(Exception e)
    {
        System.out.println("\n***************************");
        System.out.println("APPLICATION FAILED TO START");
        System.out.println("***************************\n");
        System.out.println("Description:");
        System.out.println(e.getMessage());
        System.exit(-1);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenFilter jwtTokenFilter()
    {
        logger.info("Enable jwt token filter success!");
        return new JwtTokenFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityConf securityConf()
    {
        logger.info("Enable security conf success");
        return new SecurityConf();
    }
}
