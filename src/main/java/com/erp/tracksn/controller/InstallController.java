package com.erp.tracksn.controller;

import com.erp.tracksn.entity.SysUser;
import com.erp.tracksn.repository.SysUserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统安装 Controller
 * 用于首次安装配置
 */
@RestController
@RequestMapping("/api/install")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InstallController {

    private final SysUserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 检查系统是否已安装
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getInstallStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 检查是否存在管理员账户
        long adminCount = userRepository.count();
        boolean isInstalled = adminCount > 0;
        
        status.put("installed", isInstalled);
        status.put("userCount", adminCount);
        
        return ResponseEntity.ok(status);
    }

    /**
     * 测试数据库连接
     */
    @PostMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection(
            @RequestBody DatabaseConfig config) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 测试数据库连接
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            
            result.put("success", true);
            result.put("message", "数据库连接成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据库连接失败：" + e.getMessage());
            
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 执行系统安装
     */
    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> install(@RequestBody InstallRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查是否已安装
            long userCount = userRepository.count();
            if (userCount > 0) {
                result.put("success", false);
                result.put("message", "系统已安装，请勿重复安装");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 验证密码
            if (!request.getAdmin().getPassword().equals(request.getAdmin().getConfirmPassword())) {
                result.put("success", false);
                result.put("message", "两次输入的密码不一致");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 创建管理员账户
            SysUser admin = new SysUser();
            admin.setUsername(request.getAdmin().getUsername());
            admin.setEmail(request.getAdmin().getEmail());
            admin.setRealName("系统管理员");
            admin.setDepartment("管理层");
            admin.setRole("ADMIN");
            admin.setIsActive(true);
            
            // 设置密码（加密）
            org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            admin.setPassword(encoder.encode(request.getAdmin().getPassword()));
            
            userRepository.save(admin);
            
            result.put("success", true);
            result.put("message", "系统安装成功");
            result.put("adminUsername", request.getAdmin().getUsername());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "安装失败：" + e.getMessage());
            
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 数据库配置
     */
    @Data
    public static class DatabaseConfig {
        private String host;
        private Integer port;
        private String database;
        private String username;
        private String password;
    }

    /**
     * 安装请求
     */
    @Data
    public static class InstallRequest {
        private DatabaseConfig database;
        private AdminConfig admin;
    }

    /**
     * 管理员配置
     */
    @Data
    public static class AdminConfig {
        private String username;
        private String email;
        private String password;
        private String confirmPassword;
    }
}
