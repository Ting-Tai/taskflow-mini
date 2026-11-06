package com.example.taskflowmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.taskflowmini.dto.TaskStats;
import com.example.taskflowmini.dto.TaskUpdateRequest;
import com.example.taskflowmini.entity.Task;
import com.example.taskflowmini.exception.NotFoundException;
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

    @Override
    public Task updateTask(Long id, TaskUpdateRequest request) {
        requireExists(id);

        Task patch = new Task();
        patch.setId(id);
        boolean changed = false;

        if (request.getTitle() != null) {
            String title = request.getTitle().trim();
            if (title.isEmpty()) {
                throw new IllegalArgumentException("标题不能为空");
            }
            patch.setTitle(title);
            changed = true;
        }
        if (request.getDescription() != null) {
            patch.setDescription(request.getDescription());
            changed = true;
        }
        if (request.getPriority() != null) {
            patch.setPriority(request.getPriority().trim().toUpperCase());
            changed = true;
        }
        if (request.getDueDate() != null) {
            patch.setDueDate(request.getDueDate());
            changed = true;
        }

        if (!changed) {
            throw new IllegalArgumentException("没有需要更新的字段");
        }

        // MyBatis-Plus 默认只更新非 null 字段，所以 patch 里没赋值的列不会进 SQL
        this.updateById(patch);
        return this.getById(id);
    }

    @Override
    public Task updateStatus(Long id, String status) {
        requireExists(id);

        String normalized = status.trim().toUpperCase();
        if (!VALID_STATUS.contains(normalized)) {
            throw new IllegalArgumentException("状态只能是 TODO 或 DONE");
        }

        Task patch = new Task();
        patch.setId(id);
        patch.setStatus(normalized);
        this.updateById(patch);
        return this.getById(id);
    }

    @Override
    public void deleteTask(Long id) {
        // removeById 对不存在的 id 返回 false，不会抛异常，所以要自己判断
        boolean removed = this.removeById(id);
        if (!removed) {
            throw new NotFoundException("任务不存在，id=" + id);
        }
    }

    @Override
    public TaskStats getStats() {
        // baseMapper 是 ServiceImpl 给的字段，直接调 Mapper 上的自定义 SQL
        TaskStats stats = baseMapper.selectStats();
        // 聚合查询总会返回一行，但兜一下比 NPE 好
        return stats == null ? new TaskStats(0, 0, 0) : stats;
    }

    private void requireExists(Long id) {
        if (this.getById(id) == null) {
            throw new NotFoundException("任务不存在，id=" + id);
        }
    }
}
