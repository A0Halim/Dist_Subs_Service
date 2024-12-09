import socket
import struct
import matplotlib.pyplot as plt
from collections import deque
import threading
from Capacity_pb2 import Capacity

# TCP bağlantı ayarları
PYTHON_PORT = 5004
MAX_DATA_POINTS = 20  # Grafikte tutulacak maksimum veri sayısı

# Kapasite verileri için veri yapısı (Her sunucu için ayrı veri tutulur)
capacity_data = {1: deque(maxlen=MAX_DATA_POINTS), 2: deque(maxlen=MAX_DATA_POINTS), 3: deque(maxlen=MAX_DATA_POINTS)}

# Veri güncellemesi için kilit
data_lock = threading.Lock()

# Ruby'den gelen `Capacity` verilerini al
def receive_capacity_data(connection, address):
    try:
        print(f"Thread started for client: {address}")
        while True:
            # Mesaj uzunluğunu al
            length_data = connection.recv(4)
            if not length_data:
                break
            length = struct.unpack("!I", length_data)[0]

            # Gelen mesajı oku
            received_data = connection.recv(length)

            # Protobuf ile veriyi çöz
            capacity = Capacity()
            capacity.ParseFromString(received_data)

            print(f"Received from {address}: Server {capacity.server_id} - Status: {capacity.server_status}")

            # Kapasite verilerini güncelle
            with data_lock:
                capacity_data[capacity.server_id].append(capacity.server_status)

    except Exception as e:
        print(f"Error receiving data from {address}: {e}")
    finally:
        print(f"Connection closed for client: {address}")
        connection.close()

# Kapasite verilerini grafikle göster
def plot_capacity_data():
    plt.ion()
    fig, ax = plt.subplots()
    colors = {1: 'r', 2: 'g', 3: 'b'}
    lines = {server_id: ax.plot([], [], label=f"Server {server_id}", color=colors[server_id])[0]
             for server_id in capacity_data}

    ax.legend()
    ax.set_xlabel("Time")
    ax.set_ylabel("Capacity")

    while True:
        with data_lock:
            for server_id, line in lines.items():
                data = list(capacity_data[server_id])
                line.set_data(range(len(data)), data)
            ax.relim()
            ax.autoscale_view()
        plt.pause(0.5)

# Ana fonksiyon: Ruby'den veriyi al ve grafiği başlat
def main():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
        server.bind(('localhost', PYTHON_PORT))
        server.listen(3)  # Maksimum 5 istemci sırada bekleyebilir
        print(f"Waiting for Ruby clients on port {PYTHON_PORT}...")

        # Veri alma işlemleri için threadler
        client_threads = []

        def accept_clients():
            while True:
                connection, address = server.accept()
                print(f"Connected to Ruby client: {address}")
                #connection.sendall(b'Merhaba ruby istemcisi ') 
                client_thread = threading.Thread(target=receive_capacity_data, args=(connection, address), daemon=True)
                client_thread.start()
                client_threads.append(client_thread)

        # İstemci kabul thread'i başlat
        accept_thread = threading.Thread(target=accept_clients, daemon=True)
        accept_thread.start()

        # Grafiği çiz (ana thread'de)
        plot_capacity_data()

if __name__ == "__main__":
    main()
