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
 *  File Name : JwtTokenConf.java
 *  Url: https://github.com/LuomingXuOrg/OAuth
 */

package com.github.luomingxuorg.oauth.security.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(JwtTokenConf.PREFIX)
public class JwtTokenConf
{
    static final String PREFIX = "luomingxu.oauth";

    @NestedConfigurationProperty
    public static AesConf aesConf;

    /**
     * jwt.jks文件的密钥
     */
    public static String JwtPwd;

    /**
     * 默认失效时间, 单位: 天
     */
    public static Long JwtTokenExpiration = 7L;

    public AesConf getAesConf()
    {
        return aesConf;
    }

    public void setAesConf(AesConf aesConf)
    {
        JwtTokenConf.aesConf = aesConf;
    }

    public Long getJwtTokenExpiration()
    {
        return JwtTokenExpiration * 24 * 3600 * 1000;
    }

    public void setJwtTokenExpiration(Long jwtTokenExpiration)
    {
        JwtTokenExpiration = jwtTokenExpiration;
    }

    public String getJwtPwd()
    {
        return JwtPwd;
    }

    public void setJwtPwd(String jwtPwd)
    {
        JwtPwd = jwtPwd == null || jwtPwd.equals("") ? null : jwtPwd;
    }
}
