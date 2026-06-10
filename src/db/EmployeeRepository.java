package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    public List<Employee> findAll(Connection connection) throws SQLException {
        String sql = "select matricule, nom, prenom, date_naissance, ville, salaire from employe order by matricule";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Employee> employees = new ArrayList<>();

            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getInt("matricule"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("date_naissance"),
                        resultSet.getString("ville"),
                        resultSet.getInt("salaire")
                ));
            }

            return employees;
        }
    }

    public void insert(Connection connection, Employee employee) throws SQLException {
        String sql = "insert into employe (matricule, nom, prenom, date_naissance, ville, salaire) values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, employee.getMatricule());
            statement.setString(2, employee.getNom());
            statement.setString(3, employee.getPrenom());
            statement.setString(4, employee.getDateNaissance());
            statement.setString(5, employee.getVille());
            statement.setInt(6, employee.getSalaire());
            statement.executeUpdate();
        }
    }

    public void update(Connection connection, Employee employee) throws SQLException {
        String sql = "update employe set nom = ?, prenom = ?, date_naissance = ?, ville = ?, salaire = ? where matricule = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employee.getNom());
            statement.setString(2, employee.getPrenom());
            statement.setString(3, employee.getDateNaissance());
            statement.setString(4, employee.getVille());
            statement.setInt(5, employee.getSalaire());
            statement.setInt(6, employee.getMatricule());
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection, int matricule) throws SQLException {
        String sql = "delete from employe where matricule = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, matricule);
            statement.executeUpdate();
        }
    }
}