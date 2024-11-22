import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTest {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Scanner scanner = new Scanner(System.in);

            out.println("getQuartier");
            System.out.println(in.readLine());
            System.out.println(Arrays.toString(Quartier.values()));
            out.println(scanner.nextLine());
            String choice = in.readLine();

            System.out.println(choice);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

