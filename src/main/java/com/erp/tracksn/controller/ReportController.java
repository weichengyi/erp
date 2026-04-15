package com.erp.tracksn.controller;

import com.erp.tracksn.entity.*;
import com.erp.tracksn.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 报表统计 Controller
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final CompanyRepository companyRepository;
    private final ProductSnRepository productSnRepository;
    private final AssetRepository assetRepository;

    /**
     * 仪表盘统计数据
     */
    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("companyCount", companyRepository.count());
        stats.put("productCount", productSnRepository.count());
        stats.put("assetCount", assetRepository.count());
        // TODO: 添加订单统计
        stats.put("orderCount", 0);
        return ResponseEntity.ok(stats);
    }

    /**
     * 公司统计
     */
    @GetMapping("/company-stats")
    public ResponseEntity<Map<String, Object>> getCompanyStats() {
        Map<String, Object> result = new HashMap<>();
        
        List<Company> allCompanies = companyRepository.findAll();
        long customerCount = allCompanies.stream().filter(c -> "CUSTOMER".equals(c.getType())).count();
        long supplierCount = allCompanies.stream().filter(c -> "SUPPLIER".equals(c.getType())).count();
        long activeCount = allCompanies.stream().filter(Company::getIsActive).count();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", allCompanies.size());
        stats.put("customerCount", customerCount);
        stats.put("supplierCount", supplierCount);
        stats.put("activeCount", activeCount);
        
        result.put("stats", stats);
        return ResponseEntity.ok(result);
    }

    /**
     * 导出公司数据为 Excel
     */
    @GetMapping("/export/companies")
    public ResponseEntity<byte[]> exportCompanies() throws IOException {
        List<Company> companies = companyRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("公司数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"公司 ID", "公司名称", "类型", "统一信用代码", "联系人", "电话", "邮箱", "合作状态", "合作等级", "回款周期", "创建时间"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            int rowNum = 1;
            for (Company company : companies) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(company.getId());
                row.createCell(1).setCellValue(company.getCompanyName());
                row.createCell(2).setCellValue("CUSTOMER".equals(company.getType()) ? "客户" : "供应商");
                row.createCell(3).setCellValue(company.getUniformCreditCode() != null ? company.getUniformCreditCode() : "");
                row.createCell(4).setCellValue(company.getContactPerson() != null ? company.getContactPerson() : "");
                row.createCell(5).setCellValue(company.getContactPhone() != null ? company.getContactPhone() : "");
                row.createCell(6).setCellValue(company.getContactEmail() != null ? company.getContactEmail() : "");
                row.createCell(7).setCellValue(company.getCooperationStatus() != null ? company.getCooperationStatus() : "");
                row.createCell(8).setCellValue(company.getCooperationLevel() != null ? company.getCooperationLevel() : "");
                row.createCell(9).setCellValue(company.getPaymentCycle() != null ? company.getPaymentCycle() : 0);
                row.createCell(10).setCellValue(company.getCreatedAt() != null ? company.getCreatedAt().toString() : "");
                
                // 自动调整列宽
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            
            workbook.write(baos);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = "公司数据_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        }
    }

    /**
     * 导出资产数据为 Excel
     */
    @GetMapping("/export/assets")
    public ResponseEntity<byte[]> exportAssets() throws IOException {
        List<Asset> assets = assetRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("资产数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"资产编码", "资产名称", "资产类型", "购置日期", "购置价格", "状态", "存放位置", "负责人", "备注", "创建时间"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            int rowNum = 1;
            for (Asset asset : assets) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(asset.getAssetCode());
                row.createCell(1).setCellValue(asset.getAssetName());
                row.createCell(2).setCellValue(asset.getAssetType() != null ? asset.getAssetType() : "");
                row.createCell(3).setCellValue(asset.getPurchaseDate() != null ? asset.getPurchaseDate().toString() : "");
                row.createCell(4).setCellValue(asset.getPurchasePrice() != null ? asset.getPurchasePrice().doubleValue() : 0);
                row.createCell(5).setCellValue(asset.getStatus() != null ? asset.getStatus() : "");
                row.createCell(6).setCellValue(asset.getLocation() != null ? asset.getLocation() : "");
                row.createCell(7).setCellValue(asset.getResponsiblePerson() != null ? asset.getResponsiblePerson() : "");
                row.createCell(8).setCellValue(asset.getRemark() != null ? asset.getRemark() : "");
                row.createCell(9).setCellValue(asset.getCreatedAt() != null ? asset.getCreatedAt().toString() : "");
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(baos);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = "资产数据_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        }
    }

    /**
     * 导出 SN 码数据为 Excel
     */
    @GetMapping("/export/product-sns")
    public ResponseEntity<byte[]> exportProductSns() throws IOException {
        List<ProductSn> sns = productSnRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("SN 码数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"SN 码", "产品名称", "产品型号", "类别", "生产日期", "状态", "仓库位置", "备注", "创建时间"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            int rowNum = 1;
            for (ProductSn sn : sns) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sn.getSnCode());
                row.createCell(1).setCellValue(sn.getProductName() != null ? sn.getProductName() : "");
                row.createCell(2).setCellValue(sn.getProductModel() != null ? sn.getProductModel() : "");
                row.createCell(3).setCellValue(sn.getCategory() != null ? sn.getCategory() : "");
                row.createCell(4).setCellValue(sn.getProductionDate() != null ? sn.getProductionDate().toString() : "");
                row.createCell(5).setCellValue(sn.getStatus() != null ? sn.getStatus() : "");
                row.createCell(6).setCellValue(sn.getWarehouseLocation() != null ? sn.getWarehouseLocation() : "");
                row.createCell(7).setCellValue(sn.getRemark() != null ? sn.getRemark() : "");
                row.createCell(8).setCellValue(sn.getCreatedAt() != null ? sn.getCreatedAt().toString() : "");
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(baos);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = "SN 码数据_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);
            
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        }
    }
}
