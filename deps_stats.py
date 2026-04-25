#!/usr/bin/env python3
import os
import re
from collections import defaultdict, Counter

ROOT = os.path.join('compiler', 'src', 'main')
pkg_re = re.compile(r'^\s*package\s+([\w.]+)')
type_re = re.compile(r'^\s*(?:public\s+)?(?:final\s+)?(?:abstract\s+)?(?:sealed\s+)?(?:class|interface|enum|trait|object)\s+([A-Za-z_]\w*)')
import_re = re.compile(r'^\s*import\s+([^;]+)')

def parse_import(expr):
    expr = expr.strip()
    if '{' in expr and '}' in expr:
        prefix, brace = expr.split('{', 1)
        base = prefix.rstrip('. ')
        inside = brace.split('}', 1)[0]
        parts = [p.strip() for p in inside.split(',')]
        res = []
        for p in parts:
            if '=>' in p:
                name = p.split('=>',1)[0].strip()
            else:
                name = p
            if name:
                res.append(base + '.' + name)
        return res
    if expr.endswith('._') or expr.endswith('.*'):
        return [expr[:-2] + '.*']
    if ' as ' in expr:
        expr = expr.split(' as ',1)[0].strip()
    if '=>' in expr:
        expr = expr.split('=>',1)[0].strip()
    return [expr]

def collect_sources(root):
    files = []
    for dirpath, _, filenames in os.walk(root):
        for fn in filenames:
            if fn.endswith('.java') or fn.endswith('.scala'):
                files.append(os.path.join(dirpath, fn))
    return files

def parse_file(path):
    pkg = None
    types = []
    imports = []
    try:
        with open(path, 'r', encoding='utf-8', errors='ignore') as f:
            for line in f:
                if pkg is None:
                    m = pkg_re.match(line)
                    if m:
                        pkg = m.group(1)
                m2 = type_re.match(line)
                if m2:
                    types.append(m2.group(1))
                m3 = import_re.match(line)
                if m3:
                    imports.extend(parse_import(m3.group(1)))
    except Exception:
        return None
    return pkg or '', types, imports

def pick_roots(pkgs, min_count=3):
    from collections import Counter
    cnt = Counter()
    for p in pkgs:
        parts = p.split('.')
        if len(parts) >= 2:
            cnt['.'.join(parts[:2])] += 1
        elif parts:
            cnt[parts[0]] += 1
    roots = [r for r,c in cnt.items() if c >= min_count]
    if not roots and cnt:
        roots = [cnt.most_common(1)[0][0]]
    return roots, cnt

def main():
    files = collect_sources(ROOT)
    file_map = {}
    pkgs = []
    wildcard_count = 0
    total_imports = 0
    for p in files:
        parsed = parse_file(p)
        if parsed:
            pkg, types, imports = parsed
            file_map[p] = (pkg, types, imports)
            if pkg:
                pkgs.append(pkg)
            for imp in imports:
                total_imports += 1
                if isinstance(imp, str) and imp.endswith('.*'):
                    wildcard_count += 1

    roots, root_counts = pick_roots(pkgs)

    nodes = set()
    edges = defaultdict(int)
    outdeg = defaultdict(int)
    indeg = defaultdict(int)

    for path, (pkg, types, imports) in file_map.items():
        fqdns = []
        for t in types:
            if pkg:
                fqn = pkg + '.' + t
            else:
                fqn = t
            fqdns.append(fqn)
            nodes.add(fqn)
        for imp in imports:
            targets = [imp] if isinstance(imp, str) else list(imp)
            for resolved in targets:
                if roots:
                    ok = any(resolved.startswith(r + '.') or resolved == r or resolved.startswith(r + '.*') for r in roots)
                    if not ok:
                        continue
                if resolved.endswith('.*'):
                    tgt = resolved
                    nodes.add(tgt)
                    for src in fqdns:
                        edges[(src,tgt)] += 1
                else:
                    nodes.add(resolved)
                    for src in fqdns:
                        edges[(src,resolved)] += 1

    for (a,b), w in edges.items():
        outdeg[a] += 1
        indeg[b] += 1

    # Summaries
    print(f'files_scanned: {len(files)}')
    print(f'source_files_parsed: {len(file_map)}')
    total_types = sum(len(v[1]) for v in file_map.values())
    print(f'total_top_level_types: {total_types}')
    print(f'unique_packages: {len(set(pkgs))}')
    print(f'roots_detected: {roots}')
    print('root_package_counts:')
    for r,c in root_counts.most_common(10):
        print(f'  {r}: {c}')
    print(f'total_import_statements: {total_imports}')
    print(f'wildcard_imports: {wildcard_count}')
    print(f'unique_graph_nodes: {len(nodes)}')
    print(f'unique_graph_edges: {len(edges)}')
    print(f'total_edge_weight_sum: {sum(edges.values())}')

    def top(d, n=10):
        return sorted(d.items(), key=lambda x: -x[1])[:n]

    print('\nTop 10 nodes by out-degree:')
    for k,v in top(outdeg,10):
        print(f'  {k}: {v}')

    print('\nTop 10 nodes by in-degree:')
    for k,v in top(indeg,10):
        print(f'  {k}: {v}')

    print('\nTop 10 edges by weight:')
    for (a,b), w in sorted(edges.items(), key=lambda x: -x[1])[:10]:
        print(f'  {a} -> {b}: {w}')

if __name__ == "__main__":
    main()
