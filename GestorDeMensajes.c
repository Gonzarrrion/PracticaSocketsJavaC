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
    while (1) {
        if ((read_size = recv(client_sock, client_message, 2000, 0)) > 0) {
            // Mostramos el mensaje
            printf("Mensaje recibido: %s\n", client_message);

            //Reenviamos el mensaje al receptor
            send(client_sock, client_message, strlen(client_message), 0);
        } else {
            printf("Cliente desconectado\n");
            break;
        }
    }




    // Cerramos los sockets
    close(socket_desc);
    close(client_sock);

    return 0;
}