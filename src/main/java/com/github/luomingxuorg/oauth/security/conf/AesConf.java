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
 *  File Name : AesConf.java
 *  Url: https://github.com/LuomingXuOrg/OAuth
 */

package com.github.luomingxuorg.oauth.security.conf;

public class AesConf
{
    /**
     * aes加密的密钥
     */
    public static String AesPwd = "";

    public String getAesPwd()
    {
        return AesPwd;
    }

    public void setAesPwd(String aesPwd)
    {
        AesPwd = aesPwd == null || aesPwd.equals("") ? null : aesPwd;
    }
}
