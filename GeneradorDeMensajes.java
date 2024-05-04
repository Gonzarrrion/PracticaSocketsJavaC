/*Una empresa, como parte de un de sus sistemas de información, requiere de un nuevo
sistema de distribución de mensajes en red implementados por medio de Sockets.
En dicho sistema existirá una máquina o proceso que genera mensajes
(GeneradorDeMensajes) y tendrá la capacidad de enviar dichos mensajes, por medio
de una comunicación orientada a conexión, a otra máquina o proceso con capacidad
de gestionar y reenviar dichos mensajes (GestorDeMensajes). Además, existirán otra
serie de máquinas receptoras de mensajes (ReceptorDeMensaje1,
ReceptorDeMensaje2, etc,) que recibirán mensajes de la máquina GestorDeMensajes
por medio de una comunicación no orientada a conexión.
En el mensaje que genera y envía la máquina GeneradorDeMensajes se envía el propio
dato o mensaje que el GestorDeMensajes tiene que reenviar a las máquinas
ReceptorDeMensaje, y además, en ese mismo mensaje, se incluye la ip y el puerto
hacia donde éste gestor tiene que enviar dicho mensaje.
El GestorDeMensajes, recogerá cada mensaje, y además de sacarlo por pantalla,
comprobará que es un mensaje válido. Si el mensaje es válido lo enviará a la máquina
ReceptorDeMensaje configurada con la ip y puerto que le llega en el propio mensaje. Si
no es válido, descartará dicho mensaje.
Un mensaje se considera que es válido si el dato que recibe tiene un valor mayor a 50.
Las máquinas ReceptorDeMensaje tendrán que recoger los mensajes que reciben del
GestorDeMensajes y mostrarlos por pantalla. Además, cada una de ellas
contabilizaran, de manera independiente, el número de mensajes recibidos del
GestorDeMensajes, mostrando el número total de mensajes recibidos cada vez que
reciban un nuevo mensaje.
Implementar en lenguaje Java el GeneradorDeMensajes y el ReceptorDeMensaje y
en lenguaje C el GestorDeMensajes.*/

import java.io.*;
import java.net.*;

public class GeneradorDeMensajes {
    public static void main(String[] args) {
        try {
            // Creamos el socket
            Socket socket = new Socket("localhost", 1235);
            System.out.println("Conexión establecida");
            // Creamos los flujos de entrada y salida
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            // Creamos el mensaje
            String mensaje = "Hola GestorDeMensajes";
            // Enviamos el mensaje
            dos.writeUTF(mensaje);
            // Cerramos los flujos
            dis.close();
            dos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Codigo GestorDeMensajes.c
/*
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>

int main() {
    int socket_desc, client_sock, c, read_size;
    struct sockaddr_in server, client;
    char client_message[2000];

    // Creamos el socket
    socket_desc = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_desc == -1) {
        printf("No se pudo crear el socket");
        return 1;
    }

    // Preparamos la estructura sockaddr_in
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port = htons(1235);

    // Enlazamos el socket a la dirección IP y puerto
    if (bind(socket_desc, (struct sockaddr *)&server, sizeof(server)) < 0) {
        printf("Error en el enlace");
        return 1;
    }

    // Escuchamos las conexiones
    listen(socket_desc, 3);

    // Aceptamos y esperamos la conexión
    printf("Esperando conexiones...\n");
    c = sizeof(struct sockaddr_in);
    client_sock = accept(socket_desc, (struct sockaddr *)&client, (socklen_t *)&c);
    if (client_sock < 0) {
        printf("Error al aceptar la conexión");
        return 1;
    }
    printf("Conexión establecida\n");

    // Recibimos el mensaje del cliente
    if ((read_size = recv(client_sock, client_message, 2000, 0)) > 0) {
        // Mostramos el mensaje
        printf("Mensaje recibido: %s\n", client_message);
    }

    // Cerramos los sockets
    close(socket_desc);
    close(client_sock);

    return 0;
}

Codigo ReceptorDeMensajes.java

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

/*Para compilar y ejecutar el código GestorDeMensajes.c, ejecutamos los siguientes
comandos:
gcc GestorDeMensajes.c -o GestorDeMensajes
./GestorDeMensajes
Para compilar y ejecutar el código GeneradorDeMensajes.java, ejecutamos los
siguientes comandos:
javac GeneradorDeMensajes.java
java GeneradorDeMensajes
Para compilar y ejecutar el código ReceptorDeMensajes.java, ejecutamos los
siguientes comandos:
javac ReceptorDeMensajes.java
java ReceptorDeMensajes
Para comprobar el correcto funcionamiento del sistema, ejecutamos el código
GeneradorDeMensajes.java en una terminal, luego ejecutamos el código GestorDeMensajes.c en otra terminal y finalmente ejecutamos el código ReceptorDeMensajes.java en una tercera terminal. De esta manera, podremos comprobar que el mensaje se envía correctamente desde el GeneradorDeMensajes al GestorDeMensajes y luego se muestra por pantalla en el ReceptorDeMensajes.


¿Cómo puedo hacer para que el GestorDeMensajes se quede escuchando y no se cierre la ejecución al recibir un mensaje?
Para que el GestorDeMensajes se quede escuchando y no se cierre la ejecución al 
recibir un mensaje, puedes utilizar un bucle infinito que se encargue de recibir 
los mensajes y procesarlos. De esta manera, el GestorDeMensajes seguirá escuchando y 
procesando los mensajes de forma continua. Aquí te dejo un ejemplo de cómo modificar 
el código del GestorDeMensajes para que se quede escuchando:

Corrige el codigo porque se me puede enviar directamente el mensaje desde GeneradorDeMensajes a ReceptorDeMensajes
sin pasar por GestorDeMensajes
Dime solo las lineas que tendria que modificar


*/