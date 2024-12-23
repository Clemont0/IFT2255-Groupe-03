import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Intervenant implements Utilisateur{
    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    static Registre registre = new Registre();

    private String nom;
    private String email;
    private String password;
    private String Type;
    private String identifiant;

    public Intervenant(String nom, String email, String password, String type, String identifiant){
        this.nom = nom;
        this.password = password;
        this.email = email;
        this.identifiant = identifiant;
        this.Type = type;
    }

    public Intervenant() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Profil :\n");
        sb.append(" Nom: ").append(nom).append("\n");
        sb.append(" Email: ").append(email).append("\n");
        sb.append(" Type: ").append(Type).append("\n");
        sb.append(" Identifiant: ").append(identifiant).append("\n");
        return sb.toString();
    }

    @Override
    public void inscription(Scanner scanner) {
        String nom, email, password, type = "", identifiant;

        Pattern pattern = Pattern.compile(emailRegex);

        while (true) {
            System.out.println("* Entrez votre nom : ");
            nom = scanner.nextLine();
            if (nom.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus d'inscription.");
                return;
            }
            if (!nom.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. Le nom ne peut pas être vide.");
            }
        }


        while (true) {
            System.out.println("* Entrez votre adresse email : ");
            email = scanner.nextLine();
            if (email.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus d'inscription.");
                return;
            }
            if (!email.trim().isEmpty()) {
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()) {
                    break;
                } else {
                    System.out.println("Format d'adresse email invalide. Veuillez réessayer.");
                }
            } else {
                System.out.println("Cette case est obligatoire. L'adresse email ne peut pas être vide.");
            }
        }

        while (true) {
            System.out.println("* Entrez votre identifiant à 8 chiffres : ");
            identifiant = scanner.nextLine();
            if (identifiant.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus d'inscription.");
                return;
            }
            if (!identifiant.trim().isEmpty() && identifiant.matches("\\d+") && identifiant.length() == 8) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. Veuillez entrer un identifiant valide.");
            }
        }

        // Obtenir le mot de passe
        while (true) {
            System.out.println("* Entrez votre mot de passe : ");
            password = scanner.nextLine();
            if (password.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus d'inscription.");
                return;
            }
            if (!password.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. Veuillez choisir un mot de passe.");
            }
        }

        // Obtenir le secteur d'activité
        while (true) {
            System.out.println("* Veuillez choisir votre secteur d'activité : ");
            System.out.println("1. Entrepreneur public");
            System.out.println("2. Entrepreneur privé");
            System.out.println("3. Particulier");
            String choix = scanner.nextLine();
            if (choix.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus d'inscription.");
                return; // Quitter la méthode
            }

            switch (choix) {
                case "1":
                    type = "Entrepreneur public"; // Assigner le type
                    System.out.println("Vous avez choisi : Entrepreneur public");
                    break;
                case "2":
                    type = "Entrepreneur privé"; // Assigner le type
                    System.out.println("Vous avez choisi : Entrepreneur privé");
                    break;
                case "3":
                    type = "Particulier"; // Assigner le type
                    System.out.println("Vous avez choisi : Particulier");
                    break;
                default:
                    System.out.println("Veuillez choisir 1 des 3 options");
                    continue;
            }
            break;
        }

        Intervenant intervenant = new Intervenant(nom, email, password, type, identifiant);
        registre.inscriptionIntervenant(intervenant);

        System.out.println("Vos informations ont été enregistrées.");
        System.out.println(intervenant);
    }

    @Override
    public void connection(Scanner scanner) throws InterruptedException {
        String email, password;
        Intervenant inscrit = null;


        Pattern pattern = Pattern.compile(emailRegex);

        while (true) {
            System.out.println("\nVeuillez entrer votre adresse mail (ou tapez 'EXIT' pour quitter):");
            email = scanner.nextLine();
            if (email.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus de connexion.");
                return;
            }

            if (email.trim().isEmpty()) {
                System.out.println("L'adresse email ne peut pas être vide. Veuillez réessayer.");
                continue;
            }

            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                System.out.println("Format d'adresse email invalide. Veuillez réessayer.");
                continue;
            }

            System.out.println("Veuillez entrer votre mot de passe:");
            password = scanner.nextLine();
            if (password.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus de connexion.");
                return;
            }

            if (password.trim().isEmpty()) {
                System.out.println("Le mot de passe ne peut pas être vide. Veuillez réessayer.");
                continue;
            }

            if (email.equals("User@gmail.com") && password.equals("User")) {
                System.out.println("Connexion réussie avec les informations prédéfinies.");
                Acceuil.afficherAcceuilIntervenant(new Intervenant());
                return;
            }
            inscrit = null;

            for (Intervenant intervenant : registre.getIntervenantsInscrits()) {
                if (intervenant.getPassword().equals(password) && intervenant.getEmail().equals(email)) {
                    System.out.println("Intervenant trouvé");
                    inscrit = intervenant;
                    Acceuil.afficherAcceuilIntervenant(intervenant);
                    return;
                }
            }

            System.out.println("Cet intervenant n'est pas inscrit. Veuillez réessayer.");
        }
    }

    @Override
    public  void soummettre() throws InterruptedException{
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("== Soumission d'un projet ==");

            System.out.print("* Entrez le titre du projet : ");
            String titre = scanner.nextLine();
            if (titre.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            System.out.print("* Entrez la description du projet : ");
            String description = scanner.nextLine();
            if (description.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            System.out.print("* Entrez le type de travaux : ");
            String typeTravaux = scanner.nextLine();
            if (typeTravaux.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            System.out.println("* Sélectionnez les quartiers affectés (choisissez parmi les suivants) :");
            String[] quartiers = {"Centre-ville", "Plateau Mont-Royal", "Outremont", "Saint-Laurent", "Rosemont"};
            for (int i = 0; i < quartiers.length; i++) {
                System.out.println((i + 1) + " - " + quartiers[i]);
            }
            System.out.print("Entrez les numéros des quartiers sélectionnés (séparés par des virgules) : ");
            String quartiersChoisis = scanner.nextLine();
            if (quartiersChoisis.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            System.out.print("* Entrez les rues affectées (séparées par des virgules) : ");
            String ruesAffectees = scanner.nextLine();
            if (ruesAffectees.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            String dateDebut = demanderDate(scanner, "Entrez la date de début (Format yyyy-mm-dd) : ");
            if (dateDebut.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            String dateFin = demanderDate(scanner, "Entrez la date de fin (Format yyyy-mm-dd) : ");
            if (dateFin.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            System.out.print("* Entrez l'horaire des travaux (ex: 8h-16h) : ");
            String horaireTravaux = scanner.nextLine();
            if (horaireTravaux.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            System.out.println("\nProjet soumis avec succès !");
            System.out.println("Titre : " + titre);
            System.out.println("Description : " + description);
            System.out.println("Type de travaux : " + typeTravaux);
            System.out.println("Quartiers affectés : " + quartiersChoisis);
            System.out.println("Rues affectées : " + ruesAffectees);
            System.out.println("Date de début : " + dateDebut);
            System.out.println("Date de fin : " + dateFin);
            System.out.println("Horaire des travaux : " + horaireTravaux);

            System.out.print("Soumettre un autre projet ? (O/N) : ");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("N")) {
                exit = true;
            }
        }
    }
    private String demanderDate(Scanner scanner, String message) {
        System.out.print(message);
        String date = scanner.nextLine();
        return date;
    }

}