import java.io.*;
import java.net.*;
import java.util.Scanner;

public class GeneradorDeMensajes {

   public static void main(String[] args) throws Exception {

    // Creamos un objeto Scanner para leer la entrada
    Scanner scanner = new Scanner(System.in);

    while (true) {
        // Pedimos al usuario que introduzca la IP y el puerto del gestor
        System.out.print("Introduce la IP del gestor: ");
        String ipGestor = scanner.next();

        System.out.print("Introduce el puerto del gestor: ");
        int puertoGestor = scanner.nextInt();

        // Pedimos al usuario que introduzca el dato, la IP y el puerto del receptor, que es lo que ira en el mensaje
        System.out.print("Introduce el dato a enviar: ");
        int dato = scanner.nextInt();

        System.out.print("Introduce la IP del receptor: ");
        String ipReceptor = scanner.next();

        System.out.print("Introduce el puerto del receptor: ");
        int puertoReceptor = scanner.nextInt();

        // Creamos el socket
        Socket clientSocket = new Socket(ipGestor, puertoGestor);
        
        // Creamos el flujo de salida
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        
        // Creamos el mensaje con el dato y la ip y puerto del receptor
        outToServer.writeBytes(dato + ":" + ipReceptor + ":" + puertoReceptor + "\n");
        
        System.out.println("Mensaje enviado al gestor: " + dato + ":" + ipReceptor + ":" + puertoReceptor + "\n");

        // Limpiamos el buffer
        outToServer.flush();

        // Cerramos el socket
        clientSocket.close();

        // Preguntamos al usuario si quiere seguir enviando mensajes
        System.out.print("¿Quieres enviar otro mensaje? (s/n): ");
        String respuesta = scanner.next();

        // Si la respuesta es "n", salimos del bucle
        if (respuesta.equalsIgnoreCase("n")) {
            System.out.println("Deteniendo el generador de mensajes...");
            break;
        }
    }

    // Cerramos el scanner
    scanner.close();
    }
}