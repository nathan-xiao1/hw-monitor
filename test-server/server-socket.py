import socket
import sys
import json
from log import serverLogger, getAdapter
from hwmonitor import get_data

# Settings
host = 'localhost'
port = 16779

# Create UDP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((host, port))

# Print server information
print('> Server started')
print('> Server address: {}:{}'.format(host, port))

try:
    while True:
        print("Waiting")
        data, address = sock.recvfrom(128)
        print(f"Connected: {address}")
        sock.sendto(b"Hello", address)
        print("Sent")
except KeyboardInterrupt:
    print("Ctrl+C")

# Close socket
sock.close()

# # Client connection handler
# def handle_connection(connection, address, logger):
#     try:
#         # Continously send information to client
#         while True:
#             e = export()
#             connection.sendall(bytes(str(e['cpu_usage_package']).encode()))
#             time.sleep(0.5)

#     except OSError as e:
#         logger.info(e.strerror)
#     except Exception as e:
#         logger.error("An exception has occurred - {}".format(str(e)))
#     finally:
#         connection.close()



# # Create a TCP/IP socket
# sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# # Bind the socket to the port
# sock.bind((host, port))

# # Listen for incoming connections
# sock.listen(1)

# serverLogger.info('Server started')
# serverLogger.info('Listening on {}:{}'.format(host, port))

# # Server listen loop
# while True:
#     try:
#         # Wait for a connection
#         connection, address = sock.accept()

#         # Log connection
#         (client_addr, client_port) = address
#         conn_logging_adapter = getAdapter("{}:{}".format(client_addr, client_port))
#         conn_logging_adapter.info("Incoming connection from {}:{}".format(client_addr, client_port))

#         # Start handling in new thread
#         t = threading.Thread(target=handle_connection, args=(connection, address, conn_logging_adapter))
#         t.start()

#     except KeyboardInterrupt:
#         break

# # Close the socket
# sock.close()
# serverLogger.info("Server closed")
