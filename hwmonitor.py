# https://stackoverflow.com/questions/3262603/accessing-cpu-temperature-in-python
import psutil, time
from functools import partial

# Store information that are static
_, cpu_freq_min, cpu_freq_max = psutil.cpu_freq()
_static_info = {
    "cpu_count": psutil.cpu_count(),
    "cpu_freq_min": cpu_freq_min,
    "cpu_freq_max": cpu_freq_max
}

# Dynamic information
_info_map = {
    "cpu_usage_package": partial(psutil.cpu_percent, interval=None),
    "cpu_usage_percpu": partial(psutil.cpu_percent, interval=None, percpu=True)
}

def export():
    export_dict = {}
    for key, value in _info_map.items():
        export_dict[key] = value()
    return export_dict