import socket
import struct
from Capacity_pb2 import Capacity
import matplotlib.pyplot as plt
from collections import deque
import threading
import traceback
import time

PYTHON_SERVER_PORT = 5004
MAX_DATA_POINTS = 200

# Her sunucu için ayrı bir veri kuyruğu
server_capacity_data = {
    'server1': deque(maxlen=MAX_DATA_POINTS),
    'server2': deque(maxlen=MAX_DATA_POINTS),
    'server3': deque(maxlen=MAX_DATA_POINTS)
}

# Veri güncellemesi için kilit
data_lock = threading.Lock()
current_server = 'server1'
start_time = time.time()

# Kapasite verilerini işleyen fonksiyon
def handle_capacity_data(connection):
    global current_server
    while True:
        try:
            length_data = connection.recv(4)
            if not length_data:
                break
            length = struct.unpack("!I", length_data)[0]
            received_data = connection.recv(length)
            capacity = Capacity()
            
            try:
                capacity.ParseFromString(received_data)
            except Exception as e:
                print(f"Parse hatası: {e}")
                print(traceback.format_exc())
                continue
            
            # Sunucu bilgilerini ve zaman damgasını yazdır
            print(f"Kapasite alındı - Sunucu: {current_server}, Durum: {capacity.serverX_status}, Zaman: {capacity.timestamp}")
            
            # Kapasite verilerini güncelle
            with data_lock:
                current_time = time.time() - start_time
                server_capacity_data[current_server].append((capacity.serverX_status, current_time))
                
                # Sunucu sırasını değiştir (döngüsel olarak)
                if current_server == 'server1':
                    current_server = 'server2'
                elif current_server == 'server2':
                    current_server = 'server3'
                else:
                    current_server = 'server1'
        
        except Exception as e:
            print(f"Sunucuda işlem sırasında hata: {e}")
            print(traceback.format_exc())
            break

# Grafiği güncelleyen fonksiyon
def plot_capacity_data():
    plt.ion()
    fig, ax = plt.subplots(figsize=(12, 6))
    
    # Sunucu renkleri ve çizgi stilleri
    server_styles = {
        'server1': {'color': 'blue', 'linestyle': '-', 'label': 'Server 1'},
        'server2': {'color': 'red', 'linestyle': '--', 'label': 'Server 2'},
        'server3': {'color': 'green', 'linestyle': ':', 'label': 'Server 3'}
    }
    
    # Her sunucu için çizgi nesnelerini dinamik olarak oluştur
    lines = {}
    
    ax.set_xlabel("Zaman (saniye)")
    ax.set_ylabel("Kapasite")
    
    plt.tight_layout()
    
    while True:
        with data_lock:
            # Veri içeren sunucuları belirle
            active_servers = [
                server for server, data in server_capacity_data.items() if data
            ]
            
            # Çizgileri güncelle
            for server_name in active_servers:
                # Eğer bu sunucu için çizgi oluşturulmadıysa oluştur
                if server_name not in lines:
                    style = server_styles[server_name]
                    lines[server_name] = ax.plot([], [], 
                        color=style['color'], 
                        linestyle=style['linestyle'], 
                        label=style['label']
                    )[0]
                
                # Veriyi çiz
                data = server_capacity_data[server_name]
                times = [point[1] for point in data]
                capacities = [point[0] for point in data]
                
                lines[server_name].set_data(times, capacities)
            
            # Eğer aktif sunucu varsa
            if active_servers:
                # X eksenini son veriye göre ayarla
                max_time = max(
                    max(point[1] for point in server_capacity_data[server]) 
                    for server in active_servers
                )
                ax.set_xlim(0, max_time + 5)
                
                # Eğer çizilen sunucular değişmişse legend'ı güncelle
                ax.legend()
        
        ax.relim()
        ax.autoscale_view(scaley=True, scalex=False)
        plt.pause(5)  # Her 5 saniyede bir güncelle

# Sunucu başlatma
def start_server():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server:
        server.bind(('localhost', PYTHON_SERVER_PORT))
        server.listen(1)
        print(f"Python sunucusu {PYTHON_SERVER_PORT} portunda dinleniyor...")
        
        while True:
            connection, address = server.accept()
            print(f"İstemciye bağlanıldı: {address}")
            handle_capacity_data(connection)

if __name__ == "__main__":
    server_thread = threading.Thread(target=start_server, daemon=True)
    server_thread.start()
    plot_capacity_data()