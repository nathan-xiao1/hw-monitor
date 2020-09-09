# https://stackoverflow.com/questions/3262603/accessing-cpu-temperature-in-python
import psutil, time, math
from functools import partial

# Helper function
def _byteToMB(byte):
    return byte / 1024 / 1024

# Store information that are static
_, cpu_freq_min, cpu_freq_max = psutil.cpu_freq()
memory_total = math.ceil(_byteToMB(psutil.virtual_memory().total))
disk_partitions = psutil.disk_partitions()

_static_info = {
    "cpu_count": psutil.cpu_count(),
    "cpu_freq_min": cpu_freq_min,
    "cpu_freq_max": cpu_freq_max,
    "memory_total": memory_total,
    "disk_partitions": disk_partitions,
}

# Dynamic information
_info_map = {
    "cpu_usage_package": partial(psutil.cpu_percent, interval=None),
    "cpu_usage_percpu": partial(psutil.cpu_percent, interval=None, percpu=True),
    "memory_available": lambda : _byteToMB(psutil.virtual_memory().available)
}


def get_static_info():
    return _static_info

def get_data():
    export_dict = {}
    for key, value in _info_map.items():
        export_dict[key] = value()
    return export_dict