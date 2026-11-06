package com.example.taskflowmini.dto;

/**
 * 统计结果（GET /api/tasks/stats）。
 */
public class TaskStats {

    private long total;
    private long todo;
    private long done;

    public TaskStats() {
    }

    public TaskStats(long total, long todo, long done) {
        this.total = total;
        this.todo = todo;
        this.done = done;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTodo() {
        return todo;
    }

    public void setTodo(long todo) {
        this.todo = todo;
    }

    public long getDone() {
        return done;
    }

    public void setDone(long done) {
        this.done = done;
    }
}
