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
en lenguaje C el GestorDeMensajes.
Ten en cuenta que el GeneradordeMensajes actuara como cliente, el
GestorDeMensajes como servidor y el ReceptorDeMensajes como cliente pasivo,
ya que no enviara ningún mensaje y solo recibirá mensajes del GestorDeMensajes.

Coge este codigo de ejemplo de cliente para el GeneradorDeMensajes.java y 
tambien ten en cuanta los nombres de variables y funciones y hazlos similares
adaptandolos a la descripcion del problema.
*/

import java.io.*;
import java.net.*;

public class GeneradorDeMensajes {

   public static void main(String[] args) throws Exception {

    // Generamos un dato aleatorio del 0 al 100
    int dato = 80; //(int) (Math.random() * 100);
    String ip = "172.25.120.143";
    int puerto = 8081;
    
    // Creamos el socket
    Socket clientSocket = new Socket(ip, puerto);
    
    // Creamos el flujo de salida
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // Send to server
    
    // Creamos el mensaje con el dato y la ip y puerto
    outToServer.writeBytes(dato + ":" + ip + ":" + puerto + "\n");
    
    // Limpiamos el buffer
    outToServer.flush();

    // Cerramos el socket
    clientSocket.close();
    }
}

/*
Para que el código anterior funcione, el codigo en C de GestorDeMensajes.c
debe ser, teniendo en cuenta que Siempre tiene que sacar el mensaje recibido por pantalla y luego 
verificar que el dato sea mayor que 50, si es asi, enviar un mensaje
por protocolo UDP al ReceptorDeMensajes con la ip, puerto y dato. Si no, mostrar
por pantalla que el dato no es mayor que 50. Y la ip es 192.168.1.118

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8181
#define IP "192.168.1.118"

int main() {
    int serverSocket, newSocket;
    struct sockaddr_in serverAddr;
    struct sockaddr_storage serverStorage;
    socklen_t addr_size;
    char buffer[1024];
    int dato;
    char ip[15];
    int puerto;
    int i;

    // Creamos el socket
    serverSocket = socket(PF_INET, SOCK_STREAM, 0);

    // Configuramos la dirección
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = inet_addr(IP);
    memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);

    // Ligamos el socket a la dirección
    bind(serverSocket, (struct sockaddr *) &serverAddr, sizeof(serverAddr));

    // Escuchamos por el socket
    if(listen(serverSocket,5)==0)
        printf("Esperando conexiones...\n");
    else
        printf("Error\n");

    // Aceptamos la conexión
    addr_size = sizeof serverStorage;
    newSocket = accept(serverSocket, (struct sockaddr *) &serverStorage, &addr_size);

    // Creamos el flujo de entrada
    recv(newSocket, buffer, 1024, 0);

    // Obtenemos el dato
    sscanf(buffer, "%s %d %d", ip, &puerto, &dato);

    // Mostramos el mensaje
    printf("Mensaje recibido: %s %d %d\n", ip, puerto, dato);

    // Comprobamos si el dato es mayor que 50
    if(dato > 50) {
        // Creamos el socket
        int udpSocket;
        struct sockaddr_in udpAddr;
        socklen_t udpAddrLen = sizeof(udpAddr);

        // Creamos el socket
        udpSocket = socket(AF_INET, SOCK_DGRAM, 0);

        // Configuramos la dirección
        udpAddr.sin_family = AF_INET;
        udpAddr.sin_port = htons(puerto);
        udpAddr.sin_addr.s_addr = inet_addr(ip);
        memset(udpAddr.sin_zero, '\0', sizeof udpAddr.sin_zero);

        // Enviamos el mensaje
        sendto(udpSocket, buffer, 1024, 0, (struct sockaddr *) &udpAddr, udpAddrLen);

        // Cerramos el socket
        close(udpSocket);
    } else {
        printf("El dato no es mayor que 50\n");
    }

    // Cerramos el socket
    close(newSocket);
    close(serverSocket);

    return 0;

}

*/