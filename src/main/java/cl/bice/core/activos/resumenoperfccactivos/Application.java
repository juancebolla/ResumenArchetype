package cl.bice.core.activos.resumenoperfccactivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cl.bice.core.activos.resumenoperfccactivos.mybatis.mapper")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}