package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Connection connection = DataBase.connecterBase();
        if (connection == null) {
            return;
        }

        Scanner scanner = new Scanner(System.in);
        Crud crud = new Crud();

        while (true) {
            crud.menu();
            int code;
            try {
                code = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException nfe) {
                System.err.println("Operation invalide !");
                continue;
            }

            switch (code) {
                case 1:
                    crud.ajouter(connection, scanner);
                    break;
                case 2:
                    crud.modifier(connection, scanner);
                    break;
                case 3:
                    crud.supprimer(connection, scanner);
                    break;
                case 4:
                    crud.afficher(connection);
                    break;
                case 5:
                    System.out.println("Le programme est termine !");
                    scanner.close();
                    try {
                        connection.close();
                    } catch (SQLException exception) {
                        System.out.println("Erreur lors de la fermeture de la connexion : " + exception.getMessage());
                    }
                    return;
                default:
                    System.err.println("Operation invalide !");
            }
        }
    }
}