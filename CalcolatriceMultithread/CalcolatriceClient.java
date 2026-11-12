import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;




public class CalcolatriceClient {

  private static final int port = 5000;
  public static void main(String[] args) {
    try (
        Socket socket = new Socket("localhost", port);

        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true, StandardCharsets.UTF_8
        );


        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        );

        Scanner user_input = new Scanner(System.in)
    ) {

        System.out.println("Scrivi un operazione o quit per uscire");

        while (true) {
          System.out.print(">> ");
          String operazione = user_input.nextLine().trim();

          if (operazione.isEmpty()) {
            continue;
          }

          out.println(operazione);
          String result = in.readLine();
          if (result == null) {
            break;
          }
          System.out.println(result);

          if (operazione.equalsIgnoreCase("quit")) {
            break;
          }
        }
      } catch (Exception e) {
        System.out.println("Errore: " + e.getMessage());
      }
  }
}
