package com.erp.tracksn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 贸易行业序列号全流程追踪管理系统
 * 
 * @author ERP Team
 * @version 1.0.0
 */
@SpringBootApplication
public class TradeSnTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeSnTrackingApplication.class, args);
        System.out.println("========================================");
        System.out.println("贸易行业序列号追踪管理系统启动成功!");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("========================================");
    }
}
