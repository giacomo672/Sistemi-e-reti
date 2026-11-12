import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class CalcolatriceServer {

  private static final int port = 5000;
  private static int conteggio = 0; 

  public static void main(String[] args) throws IOException {
    try (ServerSocket server = new ServerSocket(port)) {

      while (true) {
        Socket client = server.accept();
        new Thread(() -> manage(client)).start(); 
      }
    }
  }

  static void manage(Socket client) {
    String ip = client.getInetAddress().getHostAddress();

    try (Socket socket = client;
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter out = new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

      String texts;
      while ((texts = in.readLine()) != null) {
        texts = texts.trim();

        if (texts.equalsIgnoreCase("quit")) {
          out.println("chiusura...");
          break;
        }

        String risposta = calc(texts);
        if (risposta.startsWith("incrementa")) {
          incremento();
        }
        out.println(risposta);
      }

    } catch (IOException e) {
      System.out.println("Errore con " + ip + ": " + e.getMessage());
    }
  }

  public static synchronized void incremento() {
    conteggio++;
  }

  public static synchronized int getConteggio() {
    return conteggio;
  }

  public static String calc(String texts) {
    String[] parti = texts.split("\\s+");
    if (parti.length != 3) {
      return "formato invalido";
    }

    double a, b;
    try {
      a = Double.parseDouble(parti[0]);
      b = Double.parseDouble(parti[2]);
    } catch (NumberFormatException e) {
      return "numeri non validi";
    }

    if (!Double.isFinite(a) || !Double.isFinite(b)) {
      return "numeri invalidi";
    }

    switch (parti[1]) {
      case "+":
        return "" + (a + b);
      case "-":
        return "" + (a - b);
      case "*":
        return "" + (a * b);
      case "/":
        if (b == 0) {
          return "non puoi dividere per 0";
        }
        return "" + (a / b);
      default:
        return "operazione invalida";
    }
  }
}
