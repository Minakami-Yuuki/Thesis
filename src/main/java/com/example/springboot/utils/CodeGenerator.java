package com.example.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * @title: CodeGenerator
 * @Author
 * @Date: 2023/1/11 14:17
 * @Version 1.0
 */
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/vue?serverTimezone=GMT%2b8",
                        "root",
                        "20011104")
                .globalConfig(builder -> {
                    builder.author("Misaki")    // 设置作者
                            .enableSwagger()    // 开启 swagger 模式
                            .fileOverride()     // 覆盖已生成文件
                            .outputDir("D:/Chrome-Edge/IDEA/Vue/Springboot/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.springboot") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                    "D:/Chrome-Edge/IDEA/Vue/Springboot/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok(); // Autowired 注解
//                    builder.mapperBuilder().enableMapperAnnotation().build();   // Mapper 注解
                    builder.controllerBuilder().enableHyphenStyle()             // 开启驼峰转连字符
                            .enableRestStyle();             // 开启生成@RestController 控制器
                    builder.addInclude("sys_school")          // 设置需要生成的表名
                           .addTablePrefix("t_", "sys_");   // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}