import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class CalcolatriceServer {

  private static final int port = 5000;

  public static void main(String[] args) throws IOException {
    try (ServerSocket server = new ServerSocket(port)) {
    System.out.println("In ascolto su porta: " + port);
      while (true) {
        Socket client = server.accept();
        System.out.println("Accettata richiesta da: " + getIp(client));
        new Thread(() -> manage(client)).start();
      }
    }
  }

  static void manage(Socket client) {


    try (
        Socket socket = client;

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter out = new PrintWriter(
            new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)

    ) {


        String texts;
          while ((texts = in.readLine()) != null) {
            texts = texts.trim();

            if (texts.equalsIgnoreCase("quit")) {
              out.println("chiusura...");
              break;
            }

            String risposta = calc(texts);
            out.println(risposta);
          }

    } catch (IOException e) {
      System.out.println("Errore con " + getIp(client) + ": " + e.getMessage());
    }
    if(client.isClosed() == true){
        System.out.println("Client " + getIp(client) + " disconesso");
    }
  }

private static String getIp(Socket client){
    return client.getInetAddress().toString().replaceFirst("/","");
}

  public static String calc(String texts) {
    String[] richiesta = texts.split("[+*/-]");
    char calcolo = ' ';
    for(int i=0;i<texts.length();i++){
        if ("+*/-".indexOf(texts.charAt(i)) >= 0) {
            calcolo = texts.charAt(i);
        }
    }
    if (richiesta.length != 2) {
      return "formato invalido";
    }

    double a, b;
    try {
      a = Double.parseDouble(richiesta[0]);
      b = Double.parseDouble(richiesta[1]);
    } catch (NumberFormatException e) {
      return "numeri non validi";
    }

    if (!Double.isFinite(a) || !Double.isFinite(b)) {
      return "numeri invalidi";
    }

    switch (calcolo) {
      case '+':
        return "" + (a + b);
      case '-':
        return "" + (a - b);
      case '*':
        return "" + (a * b);
      case '/':
        if (b == 0) {
          return "non puoi dividere per 0";
        }
        return "" + (a / b);
      default:
        return "operazione invalida";
    }
  }
}
