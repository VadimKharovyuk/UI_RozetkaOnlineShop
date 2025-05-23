package com.example.ui_rozetkaonlineshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class UiRozetkaOnlineShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiRozetkaOnlineShopApplication.class, args);



    }

}
