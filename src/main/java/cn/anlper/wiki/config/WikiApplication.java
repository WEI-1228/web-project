package cn.anlper.wiki.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@ComponentScan("cn.anlper")
@SpringBootApplication
@MapperScan("cn.anlper.wiki.mapper")
public class WikiApplication {
    private static final Logger LOG = LoggerFactory.getLogger(WikiApplication.class);
    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(WikiApplication.class, args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址：\thttp://127.0.0.1:{}", environment.getProperty("server.port"));
    }
}
