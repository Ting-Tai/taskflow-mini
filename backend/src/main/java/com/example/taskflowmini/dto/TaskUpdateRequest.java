package com.example.taskflowmini.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * 编辑任务的请求体（PUT /api/tasks/{id}）。
 * 语义是「部分更新」：字段传 null 表示这一项不改，只更新传了值的字段。
 * 状态不在这里，改状态走 PATCH /api/tasks/{id}/status。
 */
public class TaskUpdateRequest {

    @Size(max = 100, message = "标题不能超过 100 个字符")
    private String title;

    @Size(max = 500, message = "描述不能超过 500 个字符")
    private String description;

    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "优先级只能是 LOW、MEDIUM、HIGH")
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
