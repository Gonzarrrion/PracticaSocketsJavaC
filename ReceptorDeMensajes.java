import java.io.*;
import java.net.*;

public class ReceptorDeMensajes {
    public static void main(String[] args) {
        try {
            // Creamos el socket
            ServerSocket serverSocket = new ServerSocket(1235);
            System.out.println("Esperando conexiones...");
            // Aceptamos la conexión
            Socket socket = serverSocket.accept();
            System.out.println("Conexión establecida");
            // Creamos los flujos de entrada y salida
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // Creamos el mensaje
            String mensaje = dis.readUTF();
            // Mostramos el mensaje
            System.out.println("Mensaje recibido: " + mensaje);
            // Cerramos los flujos
            dis.close();
            dos.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}