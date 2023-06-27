package org.gs;

import org.gs.models.Client;
import org.gs.services.ClientService;
import java.sql.Connection;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Connection connection = Database.getInstance().getConnection();
        ClientService clientService = new ClientService(connection);

        // Add a new client
        long clientId = clientService.create("Les Binks");
        System.out.println("Added a new client with ID: " + clientId);

        // Get the client by ID
        String clientById = clientService.getById(5L);
        System.out.println("clientById: " + clientById);

        // Set the client name by ID
        clientService.setName(3L, "Dave Murray");
        System.out.println("New client name: " + clientService.getById(3L));

        // Delete the client with ID 6
        clientService.deleteById(6L);

        // List all clients
        List<Client> allClients = clientService.listAll();
        for (Client client : allClients) {
            System.out.println(client);
        }
    }
}