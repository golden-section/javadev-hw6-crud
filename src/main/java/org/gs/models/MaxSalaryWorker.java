package org.gs.models;

public class MaxSalaryWorker {
    private final String name;
    private final int salary;

    public MaxSalaryWorker(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "MaxSalaryWorker{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}