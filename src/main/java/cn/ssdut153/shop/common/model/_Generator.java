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

package cn.ssdut153.shop.common.model;

import cn.ssdut153.shop.common.Config;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * This can generate the ORM(Object Relational Mapping) of the database.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class _Generator {

    public static void main(String[] args) {
        String baseModelPackageName = "cn.ssdut153.shop.common.model.base";
        String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/main/java/cn/ssdut153/shop/common/model/base";

        String modelPackageName = "cn.ssdut153.shop.common.model";
        String modelOutputDir = baseModelOutputDir + "/..";

        DruidPlugin druidPlugin = Config.getDruidPlugin();
        druidPlugin.start();
        DataSource dp = druidPlugin.getDataSource();
        Generator generator = new Generator(dp, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        generator.setGenerateChainSetter(false);
        generator.setMetaBuilder(new MetaBuilder(dp));
        generator.setGenerateDaoInModel(false);
        generator.setDialect(new PostgreSqlDialect());
        generator.setGenerateChainSetter(true);
        generator.setGenerateDataDictionary(false);
        generator.setRemovedTableNamePrefixes("s_");
        generator.generate();
    }
}
