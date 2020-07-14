package com.liuyong.licensetesting.model;

import lombok.Data;

/**
 * 申请码
 *
 * @author ly
 * @date 2020年 07月13日 20:14:43
 */
@Data
public class LicenseRequest {
    private String cpuId;
    private String mainboardNum;
    private String userDir;
}
