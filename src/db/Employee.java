package db;

public class Employee {

    private final int matricule;
    private final String nom;
    private final String prenom;
    private final String dateNaissance;
    private final String ville;
    private final int salaire;

    public Employee(int matricule, String nom, String prenom, String dateNaissance, String ville, int salaire) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.ville = ville;
        this.salaire = salaire;
    }

    public int getMatricule() {
        return matricule;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getVille() {
        return ville;
    }

    public int getSalaire() {
        return salaire;
    }
}