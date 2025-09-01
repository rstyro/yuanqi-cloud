package top.lrshuai.gencode.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

public class BaseGenerator {

    /**
     * 作者
     */
    public static final String AUTHOR = "rstyro";
    /**
     * 父级包名
     */
    public static final String basePackages = "top.lrshuai.gencode";
    /**
     * 数据库链接相关信息
     */
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/metaphysics?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=GMT%2B8";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "root";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 数据源配置
     */
    public static DataSourceConfig.Builder getDataSourceConfig() {
        return new DataSourceConfig.Builder(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static GlobalConfig.Builder getGlobalConfig(String outPath) {
        return new GlobalConfig.Builder()
                .author(AUTHOR) // 设置作者
//               .enableSwagger() // 开启 swagger 模式
//               .enableSpringdoc()
                .disableOpenDir()
                .outputDir(outPath + "/src/main/java");
    }

    public static PackageConfig.Builder getPackageConfig(String outPath) {
        return new PackageConfig.Builder().parent(basePackages)
                // 设置父包模块名
                .moduleName("test")
                // 设置mapperXml生成路径
                .pathInfo(Collections.singletonMap(OutputFile.xml, outPath + "/src/main/resources/mapper/test"));
    }

    public static StrategyConfig.Builder getStrategyConfig(List<String> includes, String outPath) {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.addInclude(includes) // 设置需要生成的表名
                // 设置过滤表前缀
                .addTablePrefix("t_", "c_")
                .entityBuilder()
                .enableLombok()
                .enableTableFieldAnnotation()
                .enableChainModel()
                .naming(NamingStrategy.underline_to_camel)//数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                .columnNaming(NamingStrategy.underline_to_camel)//表字段映射实体属性命名规则：默认null，不指定按照naming执行
                .idType(IdType.AUTO)//添加全局主键类型
                .mapperBuilder()
                .mapperAnnotation(Mapper.class)//开启mapper注解
//                            .enableBaseResultMap()//启用xml文件中的BaseResultMap 生成
                .enableBaseColumnList()//启用xml文件中的BaseColumnList
                .controllerBuilder()
                .enableRestStyle();

        builder.entityBuilder()
                .javaTemplate("/templates/entity1.java") // 设置实体类模板
                .mapperBuilder()
                .mapperTemplate("/templates/mapper.java")
                .mapperXmlTemplate("/templates/mapper.xml")
//                            .disable() // 禁用实体类生成
//                            .serviceBuilder()
//                            .disableService() // 禁用 Service 层生成
//                            .serviceTemplate("/templates/service.java") // 设置 Service 模板
//                            .serviceImplTemplate("/templates/serviceImpl.java") // 设置 ServiceImpl 模板
                .build();

        return builder;
    }

    public static InjectionConfig.Builder getInjectionConfig(Map<String, Object> param) {
        // 自定义代码参数
        return new InjectionConfig.Builder()
                .beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                })
                .customMap(param)
                // 生成自定义dto、vo
                .customFile(new CustomFile.Builder().fileName("Dto.java")
                        .templatePath("/templates/dto.java.ftl").packageName("dto").build())
                .customFile(new CustomFile.Builder().fileName("Vo.java")
                        .templatePath("/templates/vo.java.ftl").packageName("vo").build());
    }


    public static String scannerNext(String message) {
        System.out.println(message);
        String nextLine = scanner.nextLine();
        if (StringUtils.isBlank(nextLine)) {
            // 如果输入空行继续等待
            return scanner.next();
        }
        return nextLine;
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

}
