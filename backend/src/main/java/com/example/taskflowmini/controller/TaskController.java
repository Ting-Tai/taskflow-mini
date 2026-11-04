package com.example.taskflowmini.controller;

import com.example.taskflowmini.common.Result;
import com.example.taskflowmini.entity.Task;
import com.example.taskflowmini.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public Result<Task> create(@Valid @RequestBody Task task) {
        return Result.success(taskService.createTask(task));
    }
}
