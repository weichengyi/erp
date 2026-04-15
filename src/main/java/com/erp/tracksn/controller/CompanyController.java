package com.erp.tracksn.controller;

import com.erp.tracksn.entity.Company;
import com.erp.tracksn.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 公司管理 Controller
 */
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompanyController {

    private final CompanyRepository companyRepository;

    /**
     * 获取所有公司
     */
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        
        List<Company> companies;
        if (type != null) {
            companies = companyRepository.findByType(type);
        } else if (status != null) {
            companies = companyRepository.findByCooperationStatus(status);
        } else {
            companies = companyRepository.findAll();
        }
        return ResponseEntity.ok(companies);
    }

    /**
     * 根据公司 ID 获取
     */
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建公司
     */
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新公司
     */
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company) {
        
        return companyRepository.findById(id)
                .map(existing -> {
                    company.setId(id);
                    return ResponseEntity.ok(companyRepository.save(company));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 删除公司
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 搜索公司
     */
    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam String keyword) {
        List<Company> companies = companyRepository.findByCompanyNameContaining(keyword);
        return ResponseEntity.ok(companies);
    }
}
