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
 * 开发搜索异常
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenSearchException extends RuntimeException {

    public OpenSearchException() {

    }

    public OpenSearchException(String message) {
        super(message);
    }

    public OpenSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenSearchException(Throwable cause) {
        super(cause);
    }

    public OpenSearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}