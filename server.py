from http.server import BaseHTTPRequestHandler, HTTPServer
from hwmonitor import export
import json

# Settings
host = 'localhost'
port = 16779

# Server Handler Class
class MonitorServer(BaseHTTPRequestHandler):

    # Set header for response
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

    # Handle GET Request
    def do_GET(self):
        self._set_headers()
        self.wfile.write(json.dumps(export()).encode('utf-8'))


def run(server_class=HTTPServer, handler_class=MonitorServer, port=8000):
    httpd = server_class((host, port), MonitorServer)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()

if __name__ == '__main__':
    run()