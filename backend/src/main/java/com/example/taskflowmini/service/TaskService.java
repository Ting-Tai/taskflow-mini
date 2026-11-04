package com.example.taskflowmini.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
