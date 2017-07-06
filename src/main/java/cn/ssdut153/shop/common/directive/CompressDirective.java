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

package cn.ssdut153.shop.common.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.FastStringWriter;
import com.jfinal.template.TemplateException;
import com.jfinal.template.stat.Scope;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板压缩指令, 排除了pre、script、style标签
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class CompressDirective extends Directive {

    /**
     * 排除pre/script/style标签
     */
    private final static Pattern ignoredPattern = Pattern.compile("(<pre>(.|\n)*?</pre>)|(<script>(.|\n)*?</script>)|(<style>(.|\n)*?</style>)");
    /**
     * 匹配一段字符串中间的空白
     */
    private final static Pattern matchedPattern = Pattern.compile("\\s+");
    /**
     * 匹配一段字符串开头和结尾的空格
     */
    private final static Pattern startEndPattern = Pattern.compile("(^\\s+)|(\\s+$)");
    /**
     * 匹配两个标签中间的空格
     */
    private final static Pattern tagPattern = Pattern.compile("> <");

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        FastStringWriter fsw = new FastStringWriter();
        stat.exec(env, scope, fsw);

        try {

            // 获取所有等待压缩的标签
            StringBuilder temp = fsw.getBuffer();
            Matcher ignoredMatcher = ignoredPattern.matcher(temp);
            StringBuilder outputString = new StringBuilder();
            int lastIndex = 0;

            // 将被排除标签之外的部分依次压缩
            while (ignoredMatcher.find()) {
                int end = ignoredMatcher.start();
                outputString.append(subAndCompress(temp, lastIndex, end));
                outputString.append(ignoredMatcher.group());
                lastIndex = ignoredMatcher.end() + 1;
            }

            // 将最后一个被排除的标签后的内容压缩
            outputString.append(subAndCompress(temp, lastIndex));
            writer.write(outputString.toString());

        } catch (IOException e) {
            throw new TemplateException(e.getMessage(), location, e);
        }

    }

    /**
     * 压缩指定位置后的内容
     *
     * @param sb    待压缩的内容
     * @param start 起始位置
     * @return 压缩后的内容
     */
    private String subAndCompress(StringBuilder sb, int start) {
        return subAndCompress(sb, start, sb.length());
    }

    /**
     * 压缩指定位置的内容
     *
     * @param sb    待压缩的内容
     * @param start 起始位置
     * @param end   终止位置
     * @return 压缩后的内容
     */
    private String subAndCompress(StringBuilder sb, int start, int end) {
        String temp = sb.substring(start, end);
        Matcher startEndMatcher = startEndPattern.matcher(temp);
        temp = startEndMatcher.replaceAll("");
        Matcher matchedMatcher = matchedPattern.matcher(temp);
        temp = matchedMatcher.replaceAll(" ");
        Matcher tagMatcher = tagPattern.matcher(temp);
        temp = tagMatcher.replaceAll("><");
        return temp;
    }

    @Override
    public boolean hasEnd() {
        return true;
    }

}
