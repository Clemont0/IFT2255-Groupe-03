import java.util.Scanner;

public interface Utilisateur {


    void connection(Scanner scanner) throws InterruptedException;

    void inscription(Scanner scanner);

    void soummettre() throws InterruptedException;

}