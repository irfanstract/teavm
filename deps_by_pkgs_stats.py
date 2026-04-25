
import re
from collections import defaultdict

def get_package(class_name: str) -> str:
    # Handle both / and . separators
    if '/' in class_name:
        idx = class_name.rfind('/')
        return class_name[:idx].replace('/', '.')
    elif '.' in class_name:
        idx = class_name.rfind('.')
        return class_name[:idx]
    else:
        return "(default)"

def count_package_edges(dot_file: str):
    edge_pattern = re.compile(r'([^ ]+)\s*->\s*([^ ]+)')
    package_edges = defaultdict(int)

    with open(dot_file, 'r', encoding = "utf-16") as f:
        for line in f:
            m = edge_pattern.search(line)
            if m:
                src_class, dst_class = m.groups()
                src_pkg = get_package(src_class)
                dst_pkg = get_package(dst_class)
                key = (src_pkg, dst_pkg)
                package_edges[key] += 1

    return package_edges

if __name__ == "__main__":
    edges = count_package_edges("deps.dot")
    # print(f"n={len(tuple(edges.items()))}")
    for (src, dst), count in edges.items():
        print(f"{src} -> {dst}: {count}")



