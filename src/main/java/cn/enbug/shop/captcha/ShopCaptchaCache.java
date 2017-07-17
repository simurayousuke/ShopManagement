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

package cn.enbug.shop.captcha;

import cn.enbug.shop.common.kit.RedisKit;
import com.jfinal.captcha.Captcha;
import com.jfinal.captcha.ICaptchaCache;
import com.jfinal.plugin.redis.Redis;

/**
 * 图片验证码缓存
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class ShopCaptchaCache implements ICaptchaCache {

    public void put(Captcha captcha) {
        Redis.use(RedisKit.IMAGE_CAPTCHA).setex(captcha.getKey(), Captcha.DEFAULT_EXPIRE_TIME, captcha);
    }

    public Captcha get(String key) {
        return key == null ? null : Redis.use(RedisKit.IMAGE_CAPTCHA).get(key);
    }

    public void remove(String key) {
        if (null != key) {
            Redis.use(RedisKit.IMAGE_CAPTCHA).del(key);
        }
    }

    public void removeAll() {
        Redis.use(RedisKit.IMAGE_CAPTCHA).getJedis().flushDB();
    }

}
