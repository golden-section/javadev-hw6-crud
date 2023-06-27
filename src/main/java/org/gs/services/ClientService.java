package org.gs.services;

import org.gs.models.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final String INSERT_CLIENT_QUERY = "INSERT INTO client (name) VALUES (?)";
    private static final String SELECT_CLIENT_BY_ID_QUERY = "SELECT name FROM client WHERE id = ?";
    private static final String UPDATE_CLIENT_NAME_QUERY = "UPDATE client SET name = ? WHERE id = ?";
    private static final String DELETE_CLIENT_QUERY = "DELETE FROM client WHERE id = ?";
    private static final String SELECT_ALL_CLIENTS_QUERY = "SELECT id, name FROM client";

    private final PreparedStatement addNewClient;
    private final PreparedStatement getClientById;
    private final PreparedStatement setClientName;
    private final PreparedStatement deleteClient;
    private final PreparedStatement getAllClients;

    public ClientService(Connection connection) {
        try {
            addNewClient = connection.prepareStatement(INSERT_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            getClientById = connection.prepareStatement(SELECT_CLIENT_BY_ID_QUERY);
            setClientName = connection.prepareStatement(UPDATE_CLIENT_NAME_QUERY);
            deleteClient = connection.prepareStatement(DELETE_CLIENT_QUERY);
            getAllClients = connection.prepareStatement(SELECT_ALL_CLIENTS_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long create(String name) {
        nameValidator(name);
        try {
            addNewClient.setString(1, name);
            addNewClient.executeUpdate();
            try (ResultSet keys = addNewClient.getGeneratedKeys()) {
                if(keys.next()) {
                    return keys.getLong(1);
                } else {
                    throw new SQLException("The client ID is missing");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getById(long id) {
        try {
            getClientById.setLong(1, id);
            try(ResultSet resultSet = getClientById.executeQuery()) {
                if(!resultSet.next()) {
                    throw new SQLException("The client with ID " + id + " doesn't exist");
                }
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(long id, String name) {
        nameValidator(name);
        try {
            setClientName.setString(1, name);
            setClientName.setLong(2, id);
            int affectedRowCount = setClientName.executeUpdate();
            if(affectedRowCount == 0) {
                throw new SQLException("The client with ID " + id + " doesn't exist");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(long id) {
        try {
            deleteClient.setLong(1, id);
            deleteClient.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Client> listAll() {
        List<Client> allClients = new ArrayList<>();
        try(ResultSet resultSet = getAllClients.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                );
                allClients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allClients;
    }

    private void nameValidator(String name) {
        boolean condition = name.length() > 1 && name.length() <= 1000;
        if(!condition) {
            throw new IllegalArgumentException("Incorrect name length");
        }
    }
}