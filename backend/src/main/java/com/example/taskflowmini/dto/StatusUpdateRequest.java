package com.example.taskflowmini.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 切换任务状态的请求体（PATCH /api/tasks/{id}/status）。
 */
public class StatusUpdateRequest {

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "TODO|DONE", message = "状态只能是 TODO 或 DONE")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
