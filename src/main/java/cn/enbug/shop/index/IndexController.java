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

package cn.enbug.shop.index;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.service.OpenSearchService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.NoUrlPara;

/**
 * This is the controller of index
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.3
 * @since 1.0.0
 */
@Before(NoUrlPara.class)
public class IndexController extends BaseController {

    /**
     * The entry point of index
     */
    @Before(UserInterceptor.class)
    public void index() {
        setAttr("list", OpenSearchService.ME.getHot());
        render("index.html");
    }

}
