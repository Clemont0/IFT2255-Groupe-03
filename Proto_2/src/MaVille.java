public class MaVille {

    Database database;

    public MaVille(Database database){
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public Utilisateur seConnecter(String courriel, String mdp){
        Utilisateur utilisateur = null;

        for (Resident user : database.getResidents()){
            if (courriel.equals(user.getCourriel()) && mdp.equals(user.getMotPasse())){
                utilisateur = user;
                System.out.println("Utilsateur trouvé(e): Bienvenue " + utilisateur.getNom() + " ! ");
                break;
            }
        }

        for (Intervenant user : database.getIntervenants()){
            if (courriel.equals(user.getCourriel()) && mdp.equals(user.getMotPasse())){
                utilisateur = user;
                System.out.println("Utilsateur trouvé(e): Bienvenue " + utilisateur.getNom() + " ! ");
                break;
            }
        }

        return utilisateur;

    }

    public void seDeconnecter() {
        System.out.println("Deconnexion reusie. Fermeture de l'application.");
    }

}
