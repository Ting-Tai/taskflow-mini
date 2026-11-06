package com.example.taskflowmini.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.taskflowmini.dto.TaskStats;
import com.example.taskflowmini.entity.Task;
import org.apache.ibatis.annotations.Select;

/**
 * 继承 BaseMapper 就自动拥有 insert / selectById / selectList / updateById / deleteById 等方法，
 * 简单的单表 CRUD 一行 SQL 都不用写。
 *
 * 需要自己写的 SQL 有两种放法：
 * 1. 就这么短、不用动态条件的，直接写 @Select / @Update 注解（下面 selectStats 就是）；
 * 2. 带 <if> 拼接、多表 join 的，放 resources/mapper/TaskMapper.xml，namespace 写本接口全类名。
 */
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 一次查询拿到「总数 / 待办 / 已完成」，比连发三条 count 少两次数据库往返。
     * 空表时 COUNT 是 0，SUM 返回 NULL，所以用 COALESCE 兜成 0。
     */
    @Select("""
            SELECT COUNT(*) AS total,
                   COALESCE(SUM(CASE WHEN status = 'TODO' THEN 1 ELSE 0 END), 0) AS todo,
                   COALESCE(SUM(CASE WHEN status = 'DONE' THEN 1 ELSE 0 END), 0) AS done
            FROM task
            """)
    TaskStats selectStats();
}
