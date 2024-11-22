import java.util.Scanner;


public class App {
    static Database database = new Database();
    static MaVille maVille = new MaVille(database);
    static Menu menu = new Menu();
    public static String separator = "################################################################################";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String courriel, password;
        boolean exit = true;

        while(exit){

            System.out.println(separator);
            System.out.println("     M     M    AAAAA    V     V    IIIII   L         L         EEEEE ");
            System.out.println("     MM   MM   A     A   V     V      I     L         L         E     ");
            System.out.println("     M M M M   AAAAAAA    V   V       I     L         L         EEEEE ");
            System.out.println("     M  M  M   A     A     V V        I     L         L         E     ");
            System.out.println("     M     M   A     A      V       IIIII   LLLLLL    LLLLLL    EEEEE ");
            System.out.println("\n---------------------- Bienvenue sur Maville! ----------------------- \n");
            System.out.println("1 - Connexion ");
            System.out.println("2 - Deconnexion");
            System.out.print("\nVeuillez sélectionner une option:\n");

            switch (scanner.nextLine()){
                case "1" :
                    System.out.println("Veuillez entrez votre adresse email:");
                    courriel = scanner.nextLine();
                    System.out.println("Veuillez entrez votre mot de passe:");
                    password = scanner.nextLine();

                    Utilisateur utilisateur = maVille.seConnecter(courriel,password);

                    switch (utilisateur) {
                        case null -> System.out.println("Utilisateur introuvable.");
                        case Resident resident -> menu.menuResident(resident, maVille);
                        case Intervenant intervenant -> menu.menuIntervenant(intervenant, maVille);
                        default -> System.out.println("Erreur !!");
                    }
                    break;

                case "2":
                    maVille.seDeconnecter();
                    exit = false;
                    break;

                default:
                    System.out.println("Option non valide, veuillez réessayer.");
            }
        }
    }
}