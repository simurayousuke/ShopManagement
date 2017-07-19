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

package cn.enbug.shop.shop.good;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;

/**
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class UpdateGoodValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        validateRequired("name", Ret.MSG, "请输入商品名");
        validateRequired("description", Ret.MSG, "请输入商品描述");
        validateRequired("uuid", Ret.MSG, "请选择商品");
        validateMoney("price", Ret.MSG, "价格格式错误");
        validateInteger("number", Ret.MSG, "请输入数量");
        validateFile("avator", Ret.MSG, "图片不存在");
    }

}
