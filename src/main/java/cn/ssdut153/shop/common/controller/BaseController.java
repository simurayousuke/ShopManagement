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

package cn.ssdut153.shop.common.controller;

import com.jfinal.core.ActionException;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.render.RenderManager;

import java.math.BigDecimal;

/**
 * This is the base controller.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @see com.jfinal.core.Controller
 * @since 1.0.0
 */
public abstract class BaseController extends Controller {

    /**
     * renderManager
     */
    private static final RenderManager renderManager = RenderManager.me();

    /**
     * 从请求中获取并转换为<code>java.lang.Double</code>类型
     *
     * @param value        带获取的请求参数
     * @param defaultValue 不存在时的默认值
     * @return 如果不存在则为<code>null</code>
     * @throws com.jfinal.core.ActionException 不为空且无法转换为java.lang.Double
     */
    private Double toDouble(String value, Double defaultValue) {

        try {

            if (StrKit.isBlank(value)) {
                return defaultValue;
            }

            String value2 = value.trim();
            return Double.parseDouble(value2);

        } catch (NumberFormatException e) {
            LogKit.logNothing(e);
            throw new ActionException(400, renderManager.getRenderFactory().getErrorRender(400), "Can not parse the parameter \"" + value + "\" to Double value.");
        }

    }

    /**
     * 获取参数并转换为<code>java.lang.Double</code>类型
     *
     * @param name         需要获取并转换的参数
     * @param defaultValue 默认值
     * @return 转换后的值，如果不存在则为<code>null</code>
     * @throws com.jfinal.core.ActionException 不为空且无法转换为java.lang.Double
     */
    public Double getParaToDouble(String name, Double defaultValue) {
        return toDouble(getPara(name), defaultValue);
    }

    /**
     * 获取参数并转换成<code>java.lang.Double</code>类型
     *
     * @param name 需要获取的参数
     * @return 转换后的值，如果不存在则为<code>null</code>
     * @throws com.jfinal.core.ActionException 不为空且无法转换为java.lang.Double
     */
    public Double getParaToDouble(String name) {
        return toDouble(getPara(name), null);
    }

    /**
     * 从请求中获取并转换为<code>java.math.BigDecimal</code>类型
     *
     * @param value        带获取的请求参数
     * @param defaultValue 不存在时的默认值
     * @return 如果不存在则为<code>null</code>
     * @throws com.jfinal.core.ActionException 不为空且无法转换为java.math.BigDecimal
     */
    private BigDecimal toBigDecimal(String value, BigDecimal defaultValue) {

        try {

            if (StrKit.isBlank(value)) {
                return defaultValue;
            }

            String value2 = value.trim();
            return new BigDecimal(value2);

        } catch (NumberFormatException e) {
            LogKit.logNothing(e);
            throw new ActionException(400, renderManager.getRenderFactory().getErrorRender(400), "Can not parse the parameter \"" + value + "\" to BigDecimal value.");
        }

    }

    public BigDecimal getParaToBigDecimal(String name, BigDecimal defaultValue) {
        return toBigDecimal(getPara(name), defaultValue);
    }

    public BigDecimal getParaToBigDecimal(String name) {
        return toBigDecimal(getPara(name), null);
    }

}
