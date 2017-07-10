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

package cn.enbug.shop.common.model;

import cn.enbug.shop.common.Config;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * This can generate the ORM(Object Relational Mapping) of the database.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
public class Generator {

    private Generator() {
    }

    public static void main(String... args) {
        String baseModelPackageName = "cn.enbug.shop.common.model.base";
        String baseModelOutputDir = "src/main/java/cn/enbug/shop/common/model/base";

        String modelPackageName = "cn.enbug.shop.common.model";
        String modelOutputDir = baseModelOutputDir + "/..";

        DruidPlugin druidPlugin = Config.getDruidPlugin();
        druidPlugin.start();
        DataSource dp = druidPlugin.getDataSource();
        com.jfinal.plugin.activerecord.generator.Generator generator = new com.jfinal.plugin.activerecord.generator.Generator(dp, baseModelPackageName,
                baseModelOutputDir, modelPackageName, modelOutputDir);
        generator.setGenerateChainSetter(false);
        generator.setMetaBuilder(new MetaBuilder(dp));
        generator.setGenerateDaoInModel(false);
        generator.setDialect(new PostgreSqlDialect());
        generator.setGenerateChainSetter(true);
        generator.setGenerateDataDictionary(false);
        generator.setRemovedTableNamePrefixes("t_");
        generator.setMappingKitClassName("MappingKit");
        generator.generate();
    }
}
