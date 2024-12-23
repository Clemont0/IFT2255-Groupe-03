import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resident implements Utilisateur {
    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    static Registre registre = new Registre();

    private String prenom;
    private String nom;
    private String password;
    private String number;
    private String email;
    private String adresse;
    private LocalDate birthdate;

    public Resident(String prenom,String nom, String email,String age,String password,String adresse, String numero) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.number = numero;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        this.birthdate = LocalDate.parse(age,formatter);
    }

    public Resident() {
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getNom() {
        return nom;
    }

    public String getNumber() {
        return number;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Profil :\n");
        sb.append(" Nom : ").append(nom).append("  ").append(prenom).append("\n");
        sb.append(" Date de naissance : ").append(birthdate).append("\n");
        sb.append(" Email : ").append(email).append("\n");
        sb.append(" Numero de Telephone: ").append(number).append("\n");
        sb.append(" Adresse: ").append(adresse).append("\n");

        return sb.toString();
    }

    @Override
    public void inscription(Scanner scanner) {
        Pattern pattern = Pattern.compile(emailRegex);

        String nom, prenom, email, password, adresse, age, numero;

        while (true) {
            System.out.println("* Entrez votre prenom : ");
            prenom = scanner.nextLine();
            if (prenom.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }
            if (!prenom.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. La prénom ne peut pas être vide.");
            }
        }

        while (true) {
            System.out.println("* Entrez votre nom : ");
            nom = scanner.nextLine();
            if (nom.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
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
                System.out.println("Retour au menu précédent.");
                return; // Retourne au menu principal
            }
            if (!email.trim().isEmpty()) {
                Matcher matcher = pattern.matcher(email);
                if (matcher.matches()) {
                    break;
                } else {
                    System.out.println("Format d'adresse email invalide. Veuillez réessayer.");
                }
            }
        }

        while (true) {
            System.out.println("* Entrez votre adresse : ");
            adresse = scanner.nextLine();
            if (adresse.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }
            if (!adresse.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. L'adresse ne peut pas être vide.");
            }
        }

        while (true) {
            System.out.println("* Entrez votre mot de passe : ");
            password = scanner.nextLine();
            if (password.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }
            if (!password.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. Veuillez choisir un mot de passe.");
            }
        }

        while (true) {
            System.out.println("* Entrez votre date de naissance (Format yyyy/mm/dd) : ");
            age = scanner.nextLine();
            if (age.equalsIgnoreCase("EXIT")) {
                System.out.println("Retour au menu précédent.");
                return;
            }

            if (!age.trim().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate birthdate = LocalDate.parse(age, formatter);
                int diff = LocalDate.now().getYear() - birthdate.getYear();

                if (birthdate.isBefore(LocalDate.now()) && diff >= 16) {
                    break;
                } else {
                    System.out.println("Âge non valide. Il faut avoir 16 ans et plus pour l'inscription.");
                }
            } else {
                System.out.println("Âge non valide. Il faut avoir 16 ans et plus pour l'inscription.");
            }
        }

        System.out.println("Entrez votre numéro de téléphone (Optionnel) : ");
        numero = scanner.nextLine();
        if (numero.equalsIgnoreCase("EXIT")) {
            System.out.println("Retour au menu précédent.");
            return;
        }

        if (!numero.trim().isEmpty() && numero.length() == 10) {
            System.out.println("Vous avez choisi d'ajouter le numéro : " + numero);
        } else if (numero.trim().isEmpty()) {
            System.out.println("Vous avez choisi de ne pas ajouter de numéro.");
        }

        Resident resident = new Resident(prenom, nom, email, age, password, adresse, numero);
        registre.inscriptionResident(resident);

        System.out.println("Vos informations ont été enregistrées.");
        System.out.println(resident);
    }


    @Override
    public void connection(Scanner scanner) throws InterruptedException {
        String email, password;
        Resident inscrit = null;


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

            System.out.println("Veuillez entrer votre mot de passe (ou tapez 'EXIT' pour quitter):");
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

                Acceuil.afficherAcceuilResidents((Resident) Acceuil.resident);
                return;
            }

            inscrit = null;

            for (Resident resident : registre.getResidentsInscrits()) {
                if (resident.getPassword().equals(password) && resident.getEmail().equals(email)) {
                    System.out.println("Résident trouvé");
                    inscrit = resident;
                    Acceuil.afficherAcceuilResidents((Resident) Acceuil.resident);
                    return;
                }
            }

            System.out.println("Cet résident n'est pas inscrit. Veuillez réessayer.");
        }
    }

    @Override
    public void soummettre() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String titre, description, type, dateDebut;
        String[] typesTravaux = {"Réparation de voirie", "Entretien des espaces verts", "Rénovation d'infrastructure", "Construction", "Autre"};
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        while (true) {
            System.out.println("Veuillez soumettre une requête de travail. Tapez 'EXIT' à tout moment pour quitter.");

            // Saisie du titre
            System.out.println("Entrez le titre du travail à réaliser : ");
            titre = scanner.nextLine();
            if (titre.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté la soumission de requête.");
                break;  // On quitte la boucle si l'utilisateur tape 'EXIT'
            }

            // Saisie de la description
            System.out.println("Entrez la description détaillée du travail : ");
            description = scanner.nextLine();
            if (description.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté la soumission de requête.");
                break;
            }

            // Sélection du type de travail
            System.out.println("Sélectionnez le type de travail à partir de la liste suivante : ");
            for (int i = 0; i < typesTravaux.length; i++) {
                System.out.println((i + 1) + ". " + typesTravaux[i]);
            }

            int choixType;
            while (true) {
                System.out.print("Votre choix (1-" + typesTravaux.length + ") : ");
                String choixTypeStr = scanner.nextLine();
                if (choixTypeStr.equalsIgnoreCase("EXIT")) {
                    System.out.println("Vous avez quitté la soumission de requête.");
                    return;  // On quitte la méthode si l'utilisateur tape 'EXIT'
                }

                try {
                    choixType = Integer.parseInt(choixTypeStr);
                    if (choixType >= 1 && choixType <= typesTravaux.length) {
                        type = typesTravaux[choixType - 1];
                        break;
                    } else {
                        System.out.println("Veuillez choisir un numéro valide.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un numéro valide.");
                }
            }

            // Saisie de la date
            while (true) {
                System.out.println("Entrez la date de début espérée (Format yyyy/MM/dd) : ");
                dateDebut = scanner.nextLine();
                if (dateDebut.equalsIgnoreCase("EXIT")) {
                    System.out.println("Vous avez quitté la soumission de requête.");
                    return;  // On quitte la méthode si l'utilisateur tape 'EXIT'
                }

                try {
                    LocalDate date = LocalDate.parse(dateDebut, formatter);
                    if (date.isBefore(LocalDate.now())) {
                        System.out.println("La date ne peut pas être antérieure à aujourd'hui. Veuillez entrer une date valide.");
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Veuillez entrer la date au format yyyy/MM/dd.");
                }
            }

            // Confirmation de la soumission
            System.out.println("\nVotre requête a été soumise avec les informations suivantes :");
            System.out.println("Titre : " + titre);
            System.out.println("Description : " + description);
            System.out.println("Type : " + type);
            System.out.println("Date de début espérée : " + dateDebut);

            // Proposition de soumettre une autre requête
            System.out.println("\nVoulez-vous soumettre une autre requête ? (Tapez 'EXIT' pour quitter ou appuyez sur Entrée pour continuer)");
            String choix = scanner.nextLine();
            if (choix.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté la soumission de requête.");
                break;
            }
        }
    }


    public void signalerProbleme(Scanner scanner) throws InterruptedException {
        String titre, description;

        System.out.println("Veuillez signaler un problème.");


        while (true) {
            System.out.print("Entrez le titre du problème (ou tapez 'EXIT' pour quitter) : ");
            titre = scanner.nextLine();
            if (titre.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus de signalement.");
                return;
            }
            if (!titre.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. Le titre ne peut pas être vide.");
            }
        }

        while (true) {
            System.out.print("Entrez la description du problème (ou tapez 'EXIT' pour quitter) : ");
            description = scanner.nextLine();
            if (description.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté le processus de signalement.");

                return;
            }
            if (!description.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Cette case est obligatoire. La description ne peut pas être vide.");
            }
        }

        System.out.println("Problème signalé : " + titre + "\nDescription : " + description);


    }

    public void gererNotif(Scanner scanner) throws InterruptedException {
        String choix;
        String[] arrondissements = {
                "1. Ville-Marie",
                "2. Rosemont–La Petite-Patrie",
                "3. Le Sud-Ouest",
                "4. Côte-des-Neiges–Notre-Dame-de-Grâce",
                "5. Ahuntsic-Cartierville",
                "6. Villeray–Saint-Michel–Parc-Extension",
                "7. Mercier–Hochelaga-Maisonneuve",
                "8. Plateau-Mont-Royal",
                "9. Outremont",
                "10. Lachine"
        };

        while (true) {
            System.out.println("Vous êtes automatiquement notifié des événements dans votre quartier.");
            System.out.println("Voulez-vous être notifié pour les événements dans les autres arrondissements de Montréal ?");
            System.out.println("Voici la liste des arrondissements :");

            for (String arrondissement : arrondissements) {
                System.out.println(arrondissement);
            }

            System.out.println("Veuillez entrer le numéro de l'arrondissement pour lequel vous souhaitez être notifié, ou 'EXIT' pour revenir en arrière :");
            choix = scanner.nextLine();

            if (choix.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous avez quitté la gestion des notifications.");

                return;
            }

            try {
                int index = Integer.parseInt(choix) - 1;
                if (index >= 0 && index < arrondissements.length) {
                    System.out.println("Vous serez notifié pour les événements dans l'arrondissement : " + arrondissements[index]);
                } else {
                    System.out.println("Choix invalide. Veuillez sélectionner un numéro d'arrondissement valide.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide. Veuillez entrer un numéro ou 'EXIT'.");
            }
        }
    }

    public void plannificationParticipative(){

    }

}