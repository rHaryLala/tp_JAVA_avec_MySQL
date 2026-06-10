package ui;

import db.DataBase;
import db.Employee;
import db.EmployeeRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmployeeManagementFrame extends JFrame {

    private final Connection connection;
    private final EmployeeRepository repository;
    private final DefaultTableModel tableModel;
    private final JTable employeeTable;
    private final JTextField matriculeField;
    private final JTextField nomField;
    private final JTextField prenomField;
    private final JTextField dateNaissanceField;
    private final JTextField villeField;
    private final JTextField salaireField;
    private final JLabel statusLabel;

    public static void launch() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Connection connection = DataBase.connecterBase();
            if (connection == null) {
                JOptionPane.showMessageDialog(null,
                        "Impossible de se connecter a la base de donnees.",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            EmployeeManagementFrame frame = new EmployeeManagementFrame(connection);
            frame.setVisible(true);
        });
    }

    public EmployeeManagementFrame(Connection connection) {
        super("Gestion des employes");

        this.connection = connection;
        this.repository = new EmployeeRepository();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout(16, 16));
        rootPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        rootPanel.setBackground(new Color(245, 247, 250));
        setContentPane(rootPanel);

        JLabel titleLabel = new JLabel("Gestion des employes");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));

        JLabel subtitleLabel = new JLabel("Ajouter, modifier, supprimer et consulter les enregistrements sans passer par la console.");
        subtitleLabel.setForeground(new Color(90, 98, 110));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 4));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Matricule", "Nom", "Prenom", "Date naissance", "Ville", "Salaire"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(26);
        employeeTable.setAutoCreateRowSorter(true);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        employeeTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Liste des employes"));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Formulaire"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 8, 6, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        matriculeField = new JTextField();
        nomField = new JTextField();
        prenomField = new JTextField();
        dateNaissanceField = new JTextField();
        villeField = new JTextField();
        salaireField = new JTextField();

        addField(formPanel, constraints, 0, "Matricule", matriculeField);
        addField(formPanel, constraints, 1, "Nom", nomField);
        addField(formPanel, constraints, 2, "Prenom", prenomField);
        addField(formPanel, constraints, 3, "Date de naissance", dateNaissanceField);
        addField(formPanel, constraints, 4, "Ville", villeField);
        addField(formPanel, constraints, 5, "Salaire", salaireField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Actualiser");
        JButton clearButton = new JButton("Vider");

        addButton.addActionListener(event -> handleAdd());
        updateButton.addActionListener(event -> handleUpdate());
        deleteButton.addActionListener(event -> handleDelete());
        refreshButton.addActionListener(event -> refreshTable());
        clearButton.addActionListener(event -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);

        statusLabel = new JLabel("Pret.", SwingConstants.LEFT);
        statusLabel.setForeground(new Color(90, 98, 110));

        JPanel formContainer = new JPanel(new BorderLayout(0, 10));
        formContainer.setOpaque(false);
        formContainer.add(statusLabel, BorderLayout.NORTH);
        formContainer.add(formPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.add(formContainer, BorderLayout.NORTH);

        rootPanel.add(leftPanel, BorderLayout.WEST);
        rootPanel.add(tableScrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                closeConnection();
            }
        });

        pack();
        setLocationRelativeTo(null);
        refreshTable();
    }

    private void addField(JPanel panel, GridBagConstraints constraints, int row, String labelText, JTextField field) {
        constraints.gridy = row;

        constraints.gridx = 0;
        constraints.weightx = 0.35;
        panel.add(new JLabel(labelText), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.65;
        field.setPreferredSize(new Dimension(220, 28));
        panel.add(field, constraints);
    }

    private void handleAdd() {
        try {
            Employee employee = readEmployeeFromForm();
            repository.insert(connection, employee);
            statusLabel.setText("Employe ajoute avec succes.");
            refreshTable();
            clearForm();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (SQLException exception) {
            showError("Ajout impossible : " + exception.getMessage());
        }
    }

    private void handleUpdate() {
        try {
            Employee employee = readEmployeeFromForm();
            repository.update(connection, employee);
            statusLabel.setText("Employe modifie avec succes.");
            refreshTable();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (SQLException exception) {
            showError("Modification impossible : " + exception.getMessage());
        }
    }

    private void handleDelete() {
        try {
            int matricule = parseIntField(matriculeField, "matricule");
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Supprimer l'employe " + matricule + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            repository.delete(connection, matricule);
            statusLabel.setText("Employe supprime.");
            refreshTable();
            clearForm();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (SQLException exception) {
            showError("Suppression impossible : " + exception.getMessage());
        }
    }

    private Employee readEmployeeFromForm() {
        int matricule = parseIntField(matriculeField, "matricule");
        String nom = readTextField(nomField, "nom");
        String prenom = readTextField(prenomField, "prenom");
        String dateNaissance = readTextField(dateNaissanceField, "date de naissance");
        String ville = readTextField(villeField, "ville");
        int salaire = parseIntField(salaireField, "salaire");

        return new Employee(matricule, nom, prenom, dateNaissance, ville, salaire);
    }

    private int parseIntField(JTextField field, String label) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Le champ " + label + " est obligatoire.");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Le champ " + label + " doit contenir un nombre valide.");
        }
    }

    private String readTextField(JTextField field, String label) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Le champ " + label + " est obligatoire.");
        }
        return value;
    }

    private void refreshTable() {
        try {
            List<Employee> employees = repository.findAll(connection);
            tableModel.setRowCount(0);

            for (Employee employee : employees) {
                tableModel.addRow(new Object[]{
                        employee.getMatricule(),
                        employee.getNom(),
                        employee.getPrenom(),
                        employee.getDateNaissance(),
                        employee.getVille(),
                        employee.getSalaire()
                });
            }

            statusLabel.setText(employees.isEmpty()
                    ? "Aucun employe enregistre."
                    : employees.size() + " employe(s) charge(s)." );
        } catch (SQLException exception) {
            showError("Chargement impossible : " + exception.getMessage());
        }
    }

    private void clearForm() {
        matriculeField.setText("");
        nomField.setText("");
        prenomField.setText("");
        dateNaissanceField.setText("");
        villeField.setText("");
        salaireField.setText("");
        employeeTable.clearSelection();
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        matriculeField.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
        nomField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        prenomField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        dateNaissanceField.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
        villeField.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));
        salaireField.setText(String.valueOf(tableModel.getValueAt(modelRow, 5)));
    }

    private void showError(String message) {
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la fermeture de la connexion : " + exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}