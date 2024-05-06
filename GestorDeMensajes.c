#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define LONGITUD 100
#define PUERTO 8081

int main() {
    int socketServidor, socketCliente, socketUDP;
    struct sockaddr_in servidor, cliente, destUDP;
    socklen_t tamano = sizeof(struct sockaddr);
    char msg[LONGITUD];
    int dato;
    char ipDestino[INET_ADDRSTRLEN];
    int puertoDestino;

    // Inicializo estructura del servidor TCP
    servidor.sin_family = AF_INET;
    servidor.sin_port = htons(PUERTO);
    servidor.sin_addr.s_addr = INADDR_ANY;
    memset(servidor.sin_zero, '\0', 8);

    // Creo el socket TCP
    socketServidor = socket(AF_INET, SOCK_STREAM, 0);
    if (socketServidor < 0) {
        perror("Error al crear el socket TCP");
        exit(-1);
    }

    // Asocio el socket TCP a un puerto
    if (bind(socketServidor, (struct sockaddr *)&servidor, tamano) < 0) {
        perror("Error al asociar el puerto TCP");
        exit(-1);
    }

    // Escucho en el socket TCP
    if (listen(socketServidor, 5) < 0) {
        perror("Error al escuchar en el socket TCP");
        exit(-1);
    }

    printf("GestorDeMensajes TCP escuchando en el puerto %d...\n", PUERTO);

    // Acepto conexiones
    while (1) {
        socketCliente = accept(socketServidor, (struct sockaddr *)&cliente, &tamano);
        if (socketCliente < 0) {
            perror("Error al aceptar conexión TCP");
            continue; // Pruebo con la siguiente conexión
        }

        memset(msg, '\0', LONGITUD);
        if (recv(socketCliente, msg, LONGITUD, 0) < 0) {
            perror("Error al recibir mensaje");
            close(socketCliente);
            continue; // Pruebo con la siguiente conexión
        }

        printf("Mensaje recibido: %s\n", msg);

        // Parseo el mensaje para extraer el dato, IP y puerto destino
        sscanf(msg, "%d:%15[^:]:%d", &dato, ipDestino, &puertoDestino);

        // Imprimo los datos extraídos
        printf("Dato: %d, IP: %s, Puerto: %d\n", dato, ipDestino, puertoDestino);

        // Valido el mensaje
        if (dato <= 50) {
            printf("Mensaje inválido descartado.\n");
            close(socketCliente);
            printf("GestorDeMensajes TCP escuchando en el %d 8081...\n", PUERTO);
            continue; // Pruebo con la siguiente conexión
        }

        // Preparo el socket UDP
        socketUDP = socket(AF_INET, SOCK_DGRAM, 0);
        if (socketUDP < 0) {
            perror("Error al crear el socket UDP");
            close(socketCliente);
            continue; // Pruebo con la siguiente conexión
        }

        destUDP.sin_family = AF_INET;
        destUDP.sin_port = htons(puertoDestino);
        inet_pton(AF_INET, ipDestino, &destUDP.sin_addr);

        // Envio el mensaje usando UDP
        if (sendto(socketUDP, msg, strlen(msg), 0, (struct sockaddr *)&destUDP, sizeof(destUDP)) < 0) {
            perror("Error al enviar mensaje UDP");
        } else {
            printf("Mensaje reenviado a %s:%d\n", ipDestino, puertoDestino);
        }

        close(socketCliente);
        close(socketUDP);
    }

    return 0;
}

