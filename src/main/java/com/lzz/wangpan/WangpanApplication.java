package com.lzz.wangpan;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.zhazhapan.config.JsonParser;
import com.lzz.wangpan.config.TokenConfig;
import com.lzz.wangpan.modules.constant.ConfigConsts;
import com.lzz.wangpan.modules.constant.DefaultValues;
import com.zhazhapan.util.FileExecutor;
import com.zhazhapan.util.MailSender;
import com.zhazhapan.util.ReflectUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
@SpringBootApplication
@EnableSwagger2Doc
@MapperScan("com.lzz.wangpan.mapper")
@ServletComponentScan("com.lzz.wangpan.filter")
@EnableTransactionManagement
public class WangpanApplication {

    public static JsonParser settings = new JsonParser();

    public static List<Class<?>> controllers;

    public static Hashtable<String, Integer> tokens;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        settings.setJsonObject(FileExecutor.read(WangpanApplication.class.getResourceAsStream(DefaultValues.SETTING_PATH)));
        MailSender.config(settings.getObjectUseEval(ConfigConsts.EMAIL_CONFIG_OF_SETTINGS));
        controllers = ReflectUtils.getClasses(DefaultValues.CONTROLLER_PACKAGE);
        tokens = TokenConfig.loadToken();
        SpringApplication.run(WangpanApplication.class, args);
    }
}
