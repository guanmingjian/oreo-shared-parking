package cn.oreo.common.util.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/23
 */
public class MybatisPlusGenerator {

    // 是否只生成 entity 和 XML,XML有映射的实体类
    public static Boolean isOnlyEntityAndXML = true;

    // 数据库主机
    public static String database_host = "118.178.89.11";
    // 数据库名
    public static String database_name = "shared_parking";
    // 数据库驱动
    public static String database_driver = "org.gjt.mm.mysql.Driver";
    // 需要生成的表名
    public static String table_name = "t_time_fee";
    // 数据库账号名
    public static String database_user_name = "oreo";
    // 数据库密码
    public static String database_password = "180530";
    // 作者昵称
    public static String author_name = "GuanMingJian";
    // 文件生成-模块文件夹路径
    public static String modulePath = "/oreo-server/oreo-server-admin";
    // 包名-模块包路径
    public static String moduleName = "server.admin";

    public static void main(String[] args) {

        AutoGenerator generator = new AutoGenerator();

        // 设置数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();

        dataSourceConfig.setUrl("jdbc:mysql://"+database_host+":3306/"+database_name+"?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName(database_driver);
        dataSourceConfig.setUsername(database_user_name);
        dataSourceConfig.setPassword(database_password);

        generator.setDataSource(dataSourceConfig);

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        // 设置文件输出路径
        String projectPath = System.getProperty("user.dir");    // 用户的当前工作目录
        globalConfig.setOutputDir(projectPath + modulePath + "/src/main/java");
        // 设置作者
        globalConfig.setAuthor(author_name);
        // 创建好不自动打开
        globalConfig.setOpen(false);
        // 覆盖已有的文件
        globalConfig.setFileOverride(true);
        // 开启 swagger2 模式
        globalConfig.setSwagger2(true);
        // 设置 日期为 Date 格式
        globalConfig.setDateType(DateType.ONLY_DATE);
        // 开启 BaseResultMap
        globalConfig.setBaseColumnList(true);
        // 开启 baseColumnList
        globalConfig.setBaseResultMap(true);
        // 命名方式
        globalConfig.setControllerName("%sController");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        // globalConfig.setEntityName("%sEntity");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");

        generator.setGlobalConfig(globalConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        // 配置父包信息
        packageConfig.setParent("cn.oreo");

        packageConfig.setController(moduleName+".controller");
        packageConfig.setService(moduleName+".service");
        packageConfig.setServiceImpl(moduleName+".service.impl");
        packageConfig.setMapper(moduleName+".mapper");
        // 配置在其他模块中的实体类包名路径
        // 样式：cn.oreo.common.model.entity.po.User
        packageConfig.setEntity("common.model.entity.po");

        generator.setPackageInfo(packageConfig);

        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 自定义输出配置，自定义配置会被优先输出
        List<FileOutConfig> focList = new ArrayList<>();

        // 自定义 XML 路径
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return projectPath + modulePath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        // 自定义 Entity 路径
        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return projectPath + "/oreo-common-model/src/main/java/cn/oreo/common/model/entity/po/"
                        + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });

        // 自定义 Entity 路径
//        focList.add(new FileOutConfig("templates/entity.java") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//
//                return projectPath + "/oreo-common-model/src/main/java/cn/oreo/common/model/entity/po/"
//                        + tableInfo.getEntityName() + StringPool.DOT_JAVA;
//            }
//        });

        injectionConfig.setFileOutConfigList(focList);

        generator.setCfg(injectionConfig);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 忽略 xml 的默认模板
        templateConfig.setXml(null);
        // 忽略 entity 的默认模板
        templateConfig.setEntity(null);
        if(isOnlyEntityAndXML){
            templateConfig.setController(null);
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
            templateConfig.setMapper(null);
        }

        generator.setTemplate(templateConfig);

        // 配置策略
        StrategyConfig strategyConfig = new StrategyConfig();
        // 自动配置Lombok注解
        strategyConfig.setEntityLombokModel(true);
        // 自动转驼峰
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        // 生成 @RestController 控制器
        strategyConfig.setRestControllerStyle(true);
        // 生成字段注解
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        // 忽略表前缀
        strategyConfig.setTablePrefix("t_");
        // 需要生成的表名
        strategyConfig.setInclude(table_name);
        // 开启驼峰转连字符
        strategyConfig.setControllerMappingHyphenStyle(true);

        generator.setStrategy(strategyConfig);

        // 执行
        generator.execute();
    }
}
