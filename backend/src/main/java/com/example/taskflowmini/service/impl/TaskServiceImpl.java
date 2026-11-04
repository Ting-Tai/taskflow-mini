package com.example.taskflowmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.taskflowmini.entity.Task;
import com.example.taskflowmini.mapper.TaskMapper;
import com.example.taskflowmini.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    private static final Set<String> VALID_STATUS = Set.of("TODO", "DONE");

    @Override
    public List<Task> listTasks(String status, String keyword) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            String normalized = status.trim().toUpperCase();
            if (!VALID_STATUS.contains(normalized)) {
                throw new IllegalArgumentException("status 只能是 TODO 或 DONE");
            }
            wrapper.eq(Task::getStatus, normalized);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Task::getTitle, keyword.trim());
        }

        // 最新创建的排前面；同一秒内创建时按 id 兜底，保证顺序稳定
        wrapper.orderByDesc(Task::getCreatedAt).orderByDesc(Task::getId);
        return this.list(wrapper);
    }

    @Override
    public Task createTask(Task task) {
        // 主键、状态、时间戳都由后端决定，不接受前端传值
        task.setId(null);
        task.setStatus("TODO");
        task.setCreatedAt(null);
        task.setUpdatedAt(null);

        if (!StringUtils.hasText(task.getPriority())) {
            task.setPriority("MEDIUM");
        } else {
            task.setPriority(task.getPriority().trim().toUpperCase());
        }

        this.save(task);
        // 重新查一次，把数据库生成的 created_at / updated_at 带回给前端
        return this.getById(task.getId());
    }
}
