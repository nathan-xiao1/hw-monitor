import logging
import socket
import sys

# Settings
host = 'localhost'
port = 16779

# Setup logging
logging.basicConfig(level=logging.INFO,
                    datefmt='%Y-%m-%d %H:%M:%S',
                    format='%(asctime)s %(levelname)-4s %(message)s')

# Print server information
print('> Starting server')
print('> Server address: {}:{}'.format(host, port))

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
sock.bind((host, port))

# Listen for incoming connections
sock.listen(1)

logging.info('Server started')
logging.info('Listening on {}:{}'.format(host, port))

while True:
    try:
        # Wait for a connection
        connection, address = sock.accept()
        (client_addr, client_port) = address
        logging.info("Connection - {}:{}".format(client_addr, client_port))

        with connection:
            # Receive data from client
            data = connection.recv(1024)

            # Send reply to client
            connection.sendall(b"Received")

    except KeyboardInterrupt:
        break

# Close the socket
sock.close()
logging.info("Server closed")