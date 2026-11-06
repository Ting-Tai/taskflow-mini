package com.example.taskflowmini.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.taskflowmini.dto.TaskStats;
import com.example.taskflowmini.dto.TaskUpdateRequest;
import com.example.taskflowmini.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {

    /**
     * 按状态和标题关键字查询任务列表。
     *
     * @param status  可为空；不为空时只能是 TODO / DONE
     * @param keyword 可为空；不为空时对 title 做模糊匹配
     */
    List<Task> listTasks(String status, String keyword);

    /**
     * 新建任务。状态固定为 TODO，优先级为空时默认 MEDIUM。
     */
    Task createTask(Task task);

    /**
     * 编辑任务，只更新请求里传了值的字段。
     * 任务不存在时抛 NotFoundException。
     */
    Task updateTask(Long id, TaskUpdateRequest request);

    /**
     * 切换任务状态（TODO / DONE）。
     */
    Task updateStatus(Long id, String status);

    /**
     * 删除任务，不存在时抛 NotFoundException。
     */
    void deleteTask(Long id);

    /**
     * 统计：总任务数、待办数、已完成数。
     */
    TaskStats getStats();
}
