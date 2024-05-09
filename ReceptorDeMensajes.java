import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ReceptorDeMensajes {
    public static void main(String[] args) {
        try {
            // Creamos un objeto Scanner para leer la entrada
            Scanner scanner = new Scanner(System.in);

            // Pedimos al usuario que introduzca el puerto
            System.out.print("Introduce el puerto de escucha del receptor: ");
            int puerto = scanner.nextInt();

            // Creamos el socket
            DatagramSocket socket = new DatagramSocket(puerto);
            System.out.println("ReceptorDeMensajes (UDP) escuchando en el puerto" + " " + puerto + "...");

            // Creamos el buffer y el paquete para recibir el mensaje
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Inicializamos el contador de mensajes
            int contadorMensajes = 0;

            while (true) {
                // Recibimos el mensaje
                socket.receive(packet);

                // Convertimos el mensaje a una cadena
                String mensaje = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

                // Incrementamos el contador de mensajes
                contadorMensajes++;

                // Mostramos el mensaje y el número total de mensajes recibidos
                System.out.println("Mensaje recibido: " + mensaje);
                System.out.println("Número total de mensajes recibidos: " + contadorMensajes);

                // Preguntamos al usuario si quiere seguir escuchando
                System.out.print("¿Quieres seguir escuchando? (s/n): ");
                String respuesta = scanner.next();

                // Si la respuesta es "n", salimos del bucle, si es "s" seguimos escuchando
                if (respuesta.equalsIgnoreCase("n")) {
                    System.out.println("Deteniendo el servidor...");
                    break;
                } else {
                    System.out.println("Escuchando de nuevo en el puerto " + puerto + "...");
                }
            }

            // Cerramos el socket
            socket.close();

            // Cerramos el scanner
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}