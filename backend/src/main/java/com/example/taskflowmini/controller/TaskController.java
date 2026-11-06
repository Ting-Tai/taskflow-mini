package com.example.taskflowmini.controller;

import com.example.taskflowmini.common.Result;
import com.example.taskflowmini.dto.StatusUpdateRequest;
import com.example.taskflowmini.dto.TaskStats;
import com.example.taskflowmini.dto.TaskUpdateRequest;
import com.example.taskflowmini.entity.Task;
import com.example.taskflowmini.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks?status=TODO&keyword=接口
     * 两个参数都可以不传，不传就是全部任务。
     */
    @GetMapping
    public Result<List<Task>> list(@RequestParam(required = false) String status,
                                   @RequestParam(required = false) String keyword) {
        return Result.success(taskService.listTasks(status, keyword));
    }

    /**
     * POST /api/tasks
     */
    @PostMapping
    public Result<Task> create(@Valid @RequestBody Task task) {
        return Result.success(taskService.createTask(task));
    }

    /**
     * PUT /api/tasks/{id}
     * 部分更新：只改请求体里传了的字段，状态不在这个接口里改。
     */
    @PutMapping("/{id}")
    public Result<Task> update(@PathVariable Long id,
                               @Valid @RequestBody TaskUpdateRequest request) {
        return Result.success(taskService.updateTask(id, request));
    }

    /**
     * PATCH /api/tasks/{id}/status
     * 只切状态，请求体：{"status":"DONE"}
     */
    @PatchMapping("/{id}/status")
    public Result<Task> updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody StatusUpdateRequest request) {
        return Result.success(taskService.updateStatus(id, request.getStatus()));
    }

    /**
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return Result.success(null);
    }

    /**
     * GET /api/tasks/stats
     * 返回总任务数、待办数、已完成数。
     * 注意放在 /{id} 类路径之前不会冲突：Spring 优先匹配字面量路径。
     */
    @GetMapping("/stats")
    public Result<TaskStats> stats() {
        return Result.success(taskService.getStats());
    }
}
