package org.gs.services;

import org.gs.Database;
import org.gs.models.*;
import org.gs.props.PropertiesUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {
    private static final String MAX_SALARY_WORKER_SQL = "find.max.salary.worker.sql";
    private static final String MAX_PROJECTS_CLIENT_SQL = "find.max.projects.client.sql";
    private static final String LONGEST_PROJECT_SQL = "find.longest.project.sql";
    private static final String YOUNGEST_ELDEST_WORKERS_SQL = "find.youngest.eldest.workers.sql";
    private static final String PROJECT_PRICES_SQL = "print.project.prices.sql";

    Database database = Database.getInstance();

    public List<MaxSalaryWorker> findMaxSalaryWorker() {
        String sql = getSqlString(MAX_SALARY_WORKER_SQL);
        try(PreparedStatement prepStatement = database.getConnection().prepareStatement(sql);
            ResultSet resultSet = prepStatement.executeQuery()) {
            List<MaxSalaryWorker> maxSalaryWorker = new ArrayList<>();
            while (resultSet.next()) {
                maxSalaryWorker.add(
                        new MaxSalaryWorker(
                                resultSet.getString("name"),
                                resultSet.getInt("salary")
                        )
                );
            }
            return maxSalaryWorker;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<MaxProjectsClient> findMaxProjectsClient() {
        String sql = getSqlString(MAX_PROJECTS_CLIENT_SQL);
        try(PreparedStatement prepStatement = database.getConnection().prepareStatement(sql);
            ResultSet resultSet = prepStatement.executeQuery()) {
            List<MaxProjectsClient> maxProjectsClient = new ArrayList<>();
            while (resultSet.next()) {
                maxProjectsClient.add(
                        new MaxProjectsClient(
                                resultSet.getString("name"),
                                resultSet.getInt("project_count")
                        )
                );
            }
            return maxProjectsClient;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<LongestProject> findLongestProject() {
        String sql = getSqlString(LONGEST_PROJECT_SQL);
        try(PreparedStatement prepStatement = database.getConnection().prepareStatement(sql);
            ResultSet resultSet = prepStatement.executeQuery()) {
            List<LongestProject> longestProject = new ArrayList<>();
            while (resultSet.next()) {
                longestProject.add(
                        new LongestProject(
                                resultSet.getString("name"),
                                resultSet.getInt("month_count")
                        )
                );
            }
            return longestProject;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<YoungestEldestWorkers> findYoungestEldestWorkers() {
        String sql = getSqlString(YOUNGEST_ELDEST_WORKERS_SQL);
        try(PreparedStatement prepStatement = database.getConnection().prepareStatement(sql);
            ResultSet resultSet = prepStatement.executeQuery()) {
            List<YoungestEldestWorkers> youngestEldestWorkers = new ArrayList<>();
            while (resultSet.next()) {
                youngestEldestWorkers.add(
                        new YoungestEldestWorkers(
                                resultSet.getString("type"),
                                resultSet.getString("name"),
                                resultSet.getString("birthday")
                        )
                );
            }
            return youngestEldestWorkers;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public List<ProjectPrices> printProjectPrices() {
        String sql = getSqlString(PROJECT_PRICES_SQL);
        try(PreparedStatement prepStatement = database.getConnection().prepareStatement(sql);
            ResultSet resultSet = prepStatement.executeQuery()) {
            List<ProjectPrices> projectPrices = new ArrayList<>();
            while (resultSet.next()) {
                projectPrices.add(
                        new ProjectPrices(
                                resultSet.getString("name"),
                                resultSet.getBigDecimal("price")
                        )
                );
            }
            return projectPrices;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String getSqlString(String key) {
        try {
            String sqlPath = PropertiesUtil.getPropertyValue(key);
            return String.join("\n", Files.readAllLines(Path.of(sqlPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}