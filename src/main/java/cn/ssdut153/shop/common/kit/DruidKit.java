/*
 * Copyright (c) 2017 EnBug Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ssdut153.shop.common.kit;

import com.jfinal.plugin.druid.DruidStatViewHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * This is the monitor of database.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class DruidKit {

    /**
     * Get the DruidStatViewHandler.
     *
     * @return DruidStatViewHandler
     */
    public static DruidStatViewHandler getDruidStatViewHandler() {
        return new DruidStatViewHandler("/admin/druid", request -> {
            /*String sessionId = getCookie(request, LoginService.SESSION_ID_NAME);
            User user = LoginService.me.getUserWithSessionId(sessionId);
            return AdminAuthInterceptor.isAdmin(user);*/
            return false;
        });
    }

    /**
     * Get Cookie from Request.
     *
     * @param request HttpServletRequest
     * @param name name of cookie
     * @return value of cookie
     */
    private static String getCookie(HttpServletRequest request, String name) {
        Cookie cookie = getCookieObject(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * Get Cookie Object from Request.
     *
     * @param request HttpServletRequest
     * @param name name of cookie
     * @return Cookie Object
     */
    private static Cookie getCookieObject(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

}
