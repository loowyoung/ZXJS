package com.liuyong.licensetesting.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 申请授权实体类
 * cpu、主板、项目名称、申请人、ip等
 *
 * @author ly
 * @date 2020年 07月13日 17:38:44
 */
@Data
public class LicenseDto {
    private String cpuId;
    private String mainboardNum;//主板
    private String applicant;//申请人
    private String ip;
    private String userDir;//jar包所在目录
    private LocalDateTime limitDate;//过期时间
}
