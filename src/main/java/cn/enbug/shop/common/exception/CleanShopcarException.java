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

package cn.enbug.shop.common.exception;

/**
 * Exception occurred during CleanShopcar.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.1.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class CleanShopcarException extends ShopException {

    /**
     * @see RuntimeException#RuntimeException()
     */
    public CleanShopcarException() {
        // RunTimeException默认构造函数
        super();
    }

    /**
     * @param message message
     * @see RuntimeException#RuntimeException()
     */
    public CleanShopcarException(String message) {
        super(message);
    }

    /**
     * @param message message
     * @param cause   cause
     * @see RuntimeException#RuntimeException()
     */
    public CleanShopcarException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause cause
     * @see RuntimeException#RuntimeException()
     */
    public CleanShopcarException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message            message
     * @param cause              cause
     * @param enableSuppression  enable suppression
     * @param writableStackTrace writable stack trace
     * @see RuntimeException#RuntimeException()
     */
    public CleanShopcarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
