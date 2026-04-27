#!/usr/bin/env python3
"""Generate class-level dependency graph (DOT) from Java/Scala sources.

Usage: python generate_deps.py > deps.dot

Scans compiler/src/main for .java and .scala files, extracts package and
top-level type names, parses imports (including Scala brace imports and
wildcards), and emits a directed DOT graph. By default filters imported
targets to the most-common package roots found in the sources (e.g.
org.teavm) to focus on internal dependencies.
"""
import os
import re
import sys
from collections import defaultdict, Counter

pkg_re = re.compile(r'^\s*package\s+([\w.]+)')
type_re = re.compile(r'^\s*(?:public\s+)?(?:final\s+)?(?:abstract\s+)?(?:sealed\s+)?(?:class|interface|enum|trait|object)\s+([A-Za-z_]\w*)')
import_re = re.compile(r'^\s*import\s+([^;]+)')

def parse_import(expr):
    expr = expr.strip()
    # Scala alias: x.y.{A => B}
    if '{' in expr and '}' in expr:
        prefix, brace = expr.split('{', 1)
        base = prefix.rstrip('. ')
        inside = brace.split('}', 1)[0]
        parts = [p.strip() for p in inside.split(',')]
        res = []
        for p in parts:
            if '=>' in p:
                name = p.split('=>',1)[0].strip()
            elif ' as ' in p:
                name = p.split(' as ',1)[0].strip()
            else:
                name = p
            if name:
                res.append(base + '.' + name)
        return res
    # wildcard styles
    if expr.endswith('._') or expr.endswith('.*'):
        return [expr[:-2] + '.*']
    # simple import like a.b.C or a.b.C as x
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
    pkg = "root"
    types = []
    imports = []
    try:
        with open(path, 'r', encoding='utf-8') as f:
            # collect package, types, and imports in one pass
            # this allows us to handle cases where package is declared after imports or types, which is valid in Java/Scala
            # we also keep updating the package if we encounter multiple package declarations, which is invalid in Java but valid (and idiomatic) in Scala
            # we also ignore errors and just return None if we fail to read the file, which can happen for various reasons (e.g. binary files, encoding issues)
            for line in f:
                m = pkg_re.match(line)
                if m:
                    pkg += "." + m.group(1)
                m2 = type_re.match(line)
                if m2:
                    types.append(m2.group(1))
                m3 = import_re.match(line)
                if m3:
                    imports.extend(parse_import(m3.group(1)))
        # strip common "root." prefix from package names to avoid distortion to the graph
        # - would interfere with tools which counts the dependency edges to packages, and "root" would be the most common package with many edges to other packages, which is not informative
        # - (e.g. if some files don't declare package, they will be in "root" package, and if we keep "root" prefix, it will create a large "root" node with many edges to other packages, which is not informative)
        pkg = pkg[5:]
    except Exception:
        return None
    return pkg or '', types, imports

def pick_roots(pkgs, min_count=3):
    # choose common two-segment roots (e.g. org.teavm)
    cnt = Counter()
    for p in pkgs:
        parts = p.split('.')
        if len(parts) >= 2:
            cnt['.'.join(parts[:2])] += 1
        elif parts:
            cnt[parts[0]] += 1
    roots = [r for r,c in cnt.items() if c >= min_count]
    # if none meet threshold, pick top-1
    if not roots and cnt:
        roots = [cnt.most_common(1)[0][0]]
    return roots

def main():
    m1(id = "jvmAstLib")
    m1(id = "compilerIrAst")
    m1(id = "compiler")

def m1(id: str  ):
    ROOT = os.path.join(id.lower(), 'src', 'main')
    files = collect_sources(ROOT)
    file_map = {}  # path -> (pkg, [types], [imports])
    packages = []
    for p in files:
        parsed = parse_file(p)
        if parsed:
            pkg, types, imports = parsed
            file_map[p] = (pkg, types, imports)
            if pkg:
                packages.append(pkg)

    roots = pick_roots(packages)

    # build nodes and edges
    nodes = set()
    edges = defaultdict(int)

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
            for resolved in (imp if isinstance(imp, list) else [imp]):
                # resolved is like 'org.foo.Bar' or 'org.foo.*'
                # keep only imports that match roots (if we found roots)
                if roots:
                    ok = False
                    for r in roots:
                        if resolved.startswith(r + '.') or resolved == r or resolved.startswith(r + '.*'):
                            ok = True
                            break
                    if not ok:
                        continue
                # add target node(s)
                if resolved.endswith('.*'):
                    target = resolved
                    nodes.add(target)
                    for src in fqdns:
                        edges[(src, target)] += 1
                else:
                    # single type
                    nodes.add(resolved)
                    for src in fqdns:
                        edges[(src, resolved)] += 1

    # emit DOT
    print(f'digraph {id.upper()}' + ' {')
    print('  rankdir=LR;')
    # nodes
    for n in sorted(nodes):
        label = n.split('.')[-1]
        safe = n.replace('"', '\\"')
        print(f'  "{safe}" [label="{label}"];')
    # edges with weights
    for (a,b), w in sorted(edges.items(), key=lambda x: (-x[1], x[0])):
        a_s = a.replace('"', '\\"')
        b_s = b.replace('"', '\\"')
        attrs = f'label="{w}"' if w != 1 else ''
        print(f'  "{a_s}" -> "{b_s}" [{attrs}];')
    print('}')
    print('')

if __name__ == '__main__':
    main()
