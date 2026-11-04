package com.example.taskflowmini.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.taskflowmini.entity.Task;

/**
 * 继承 BaseMapper 就自动拥有 insert / selectById / selectList / updateById / deleteById 等方法，
 * 简单的单表 CRUD 一行 SQL 都不用写。
 */
public interface TaskMapper extends BaseMapper<Task> {
}
