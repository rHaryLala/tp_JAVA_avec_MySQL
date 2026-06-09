package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Crud {

    private final Scanner scanner = new Scanner(System.in);

    public void ajouter(Connection connection) {
        System.out.print("Entrez le matricule : ");
        int matricule = scanner.nextInt();

        System.out.print("Entrez le nom : ");
        String nom = scanner.next();

        System.out.print("Entrez le prenom : ");
        String prenom = scanner.next();

        System.out.print("Entrez la date de naissance : ");
        String dateNaissance = scanner.next();

        System.out.print("Entrez la ville : ");
        String ville = scanner.next();

        System.out.print("Entrez le salaire : ");
        int salaire = scanner.nextInt();

        String sql = "insert into employe (matricule, nom, prenom, date_naissance, ville, salaire) values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, matricule);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setString(4, dateNaissance);
            statement.setString(5, ville);
            statement.setInt(6, salaire);
            statement.executeUpdate();
            System.out.println("L'employe a ete ajoute avec succes.");
        } catch (SQLException exception) {
            System.out.println("Operation echouee : " + exception.getMessage());
        }
    }

    public void modifier(Connection connection) {
        System.out.print("Entrez le matricule : ");
        int matricule = scanner.nextInt();

        System.out.print("Entrez la nouvelle ville : ");
        String ville = scanner.next();

        System.out.print("Entrez le nouveau salaire : ");
        int salaire = scanner.nextInt();

        String sql = "update employe set ville = ?, salaire = ? where matricule = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ville);
            statement.setInt(2, salaire);
            statement.setInt(3, matricule);
            statement.executeUpdate();
            System.out.println("L'enregistrement a ete modifie avec succes.");
        } catch (SQLException exception) {
            System.out.println("Operation echouee : " + exception.getMessage());
        }
    }

    public void supprimer(Connection connection) {
        System.out.print("Entrez le matricule de l'employe : ");
        int matricule = scanner.nextInt();

        String sql = "delete from employe where matricule = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, matricule);
            int lignesSupprimees = statement.executeUpdate();
            if (lignesSupprimees > 0) {
                System.out.println("L'employe est supprime.");
            } else {
                System.out.println("Aucun employe trouve avec ce matricule.");
            }
        } catch (SQLException exception) {
            System.out.println("Operation echouee : " + exception.getMessage());
        }
    }

    public void afficher(Connection connection) {
        String sql = "select matricule, nom, prenom, date_naissance, ville, salaire from employe";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.printf("%-10s %-15s %-15s %-18s %-15s %-10s%n",
                    "Matricule", "Nom", "Prenom", "Date naissance", "Ville", "Salaire");
            System.out.println("-------------------------------------------------------------------------------");

            while (resultSet.next()) {
                System.out.printf("%-10d %-15s %-15s %-18s %-15s %-10d%n",
                        resultSet.getInt("matricule"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("date_naissance"),
                        resultSet.getString("ville"),
                        resultSet.getInt("salaire"));
            }
        } catch (SQLException exception) {
            System.out.println("Operation echouee : " + exception.getMessage());
        }
    }

    public void menu() {
        System.out.println("1- Insertion");
        System.out.println("2- Modification");
        System.out.println("3- Suppression");
        System.out.println("4- Consultation");
        System.out.println("5- Fin du programme");
        System.out.println("Tapez le code de l'operation");
    }
}