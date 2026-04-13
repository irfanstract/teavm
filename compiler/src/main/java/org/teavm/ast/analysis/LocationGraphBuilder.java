/*
 *  Copyright 2019 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.ast.analysis;

import com.carrotsearch.hppc.IntArrayDeque;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntDeque;
import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.teavm.ast.AssignmentStatement;
import org.teavm.ast.BlockStatement;
import org.teavm.ast.BoundCheckExpr;
import org.teavm.ast.BreakStatement;
import org.teavm.ast.ConditionalExpr;
import org.teavm.ast.ConditionalStatement;
import org.teavm.ast.ContinueStatement;
import org.teavm.ast.ControlFlowEntry;
import org.teavm.ast.Expr;
import org.teavm.ast.IdentifiedStatement;
import org.teavm.ast.InitClassStatement;
import org.teavm.ast.MonitorEnterStatement;
import org.teavm.ast.MonitorExitStatement;
import org.teavm.ast.RecursiveVisitor;
import org.teavm.ast.ReturnStatement;
import org.teavm.ast.Statement;
import org.teavm.ast.SwitchClause;
import org.teavm.ast.SwitchStatement;
import org.teavm.ast.ThrowStatement;
import org.teavm.ast.TryCatchStatement;
import org.teavm.ast.WhileStatement;
import org.teavm.common.Graph;
import org.teavm.common.GraphBuilder;
import org.teavm.model.TextLocation;

/**
 * <p> a helper class that builds a control flow graph of a method, where nodes represent locations in source code and edges represent possible control flow between them. It uses {@link IdentifiedStatement} to identify statements that can be referred to from other statements, such as {@link BreakStatement} and {@link ContinueStatement}. It also uses {@link TextLocation} to store location information for statements and expressions, which can be used for debugging and error reporting. The resulting control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
 * 
 * <p> the control flow graph is built by visiting the AST of a method and creating nodes for statements and expressions that have location information. Edges are created between nodes based on the structure of the AST and the control flow of the method. For example, a {@link ConditionalStatement} will create edges from the condition to the consequent and alternative branches, while a {@link WhileStatement} will create edges from the condition to the body and back to the condition. A {@link BreakStatement} will create an edge to its target statement, while a {@link ContinueStatement} will create an edge to its target statement. A {@link ReturnStatement} or {@link ThrowStatement} will create an edge to a special terminal node, which indicates that the control flow can exit the method at that point.
 * 
 * <p> the resulting control flow graph is represented as an array of {@link ControlFlowEntry}, where each entry contains a {@link TextLocation} and an array of successor {@link TextLocation}s. The {@link TextLocation} represents the location in source code that corresponds to the node, while the array of successor {@link TextLocation}s represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point. The control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
 * 
 */
public final class LocationGraphBuilder {
    private LocationGraphBuilder() {
    }

    /**
     * builds a control flow graph of a method, where nodes represent locations in source code and edges represent possible control flow between them. It uses {@link IdentifiedStatement} to identify statements that can be referred to from other statements, such as {@link BreakStatement} and {@link ContinueStatement}. It also uses {@link TextLocation} to store location information for statements and expressions, which can be used for debugging and error reporting. The resulting control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
     * 
     * @param node the root statement of the method's AST, which is typically a {@link BlockStatement} that contains the method's body. The method will be visited recursively to build the control flow graph.
     * 
     * @implNote
     * internal implementation details:
     * - visit the AST of the method using a custom visitor that builds the control flow graph. The visitor creates nodes for statements and expressions that have location information, and creates edges based on the structure of the AST and the control flow of the method. The resulting graph is stored in a GraphBuilder, while the location information is stored in a list of TextLocations. The visitor also keeps track of terminal nodes, which are nodes that correspond to return or throw statements, so they can be marked as such in the resulting graph.
     * - after visiting the AST, build the control flow graph from the GraphBuilder and propagate location information through the graph. The resulting graph is represented as an array of ControlFlowEntry, where each entry contains a TextLocation and an array of successor TextLocations. The TextLocation represents the location in source code that corresponds to the node, while the array of successor TextLocations represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point. The control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
     * - mark terminal nodes in the graph based on the visitor's terminalNodes BitSet, which indicates which nodes correspond to return or throw statements. This information is used to create edges to a special terminal node in the resulting control flow graph, which indicates that the control flow can exit the method at those points.
     * - propagate location information through the control flow graph using a helper method that performs a breadth-first search. The resulting array of TextLocations can be used to determine which locations can be reached from each node in the control flow graph, which is useful for debugging and error reporting. The resulting array is then used to build the final array of ControlFlowEntry, where each entry contains a TextLocation and an array of successor TextLocations based on the edges in the graph and the terminal nodes.
     * ...
     * 
     * @return an array of {@link ControlFlowEntry}, where each entry contains a {@link TextLocation} and an array of successor {@link TextLocation}s. The {@link TextLocation} represents the location in source code that corresponds to the node, while the array of successor {@link TextLocation}s represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point. The control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
     * 
     * @implNote
     * ...
     * 
     */
    public static ControlFlowEntry[] build(Statement node) {
        // visit the AST of the method using a custom visitor that builds the control flow graph. The visitor creates nodes for statements and expressions that have location information, and creates edges based on the structure of the AST and the control flow of the method. The resulting graph is stored in a GraphBuilder, while the location information is stored in a list of TextLocations. The visitor also keeps track of terminal nodes, which are nodes that correspond to return or throw statements, so they can be marked as such in the resulting graph.
        Visitor visitor = new Visitor();
        node.acceptVisitor(visitor);
        // after visiting the AST, build the control flow graph from the GraphBuilder and propagate location information through the graph. The resulting graph is represented as an array of ControlFlowEntry, where each entry contains a TextLocation and an array of successor TextLocations. The TextLocation represents the location in source code that corresponds to the node, while the array of successor TextLocations represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point. The control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
        Graph graph = visitor.builder.build();
        // mark terminal nodes in the graph based on the visitor's terminalNodes BitSet, which indicates which nodes correspond to return or throw statements. This information is used to create edges to a special terminal node in the resulting control flow graph, which indicates that the control flow can exit the method at those points.
        for (int terminal : visitor.nodes) {
            visitor.terminalNodes.set(terminal);
        }
        // propagate location information through the control flow graph using a helper method that performs a breadth-first search. The resulting array of TextLocations can be used to determine which locations can be reached from each node in the control flow graph, which is useful for debugging and error reporting. The resulting array is then used to build the final array of ControlFlowEntry, where each entry contains a TextLocation and an array of successor TextLocations based on the edges in the graph and the terminal nodes.
        TextLocation[][] locations = propagate(visitor.locations.toArray(new TextLocation[0]), graph);

        // build the final array of ControlFlowEntry from the graph and the propagated location information. For each node in the graph, create a ControlFlowEntry with the corresponding TextLocation and an array of successor TextLocations based on the edges in the graph. If a node is marked as a terminal node, add a null successor to indicate that the control flow can exit the method at that point. The resulting array of ControlFlowEntry can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
        Map<TextLocation, Set<TextLocation>> builder = new LinkedHashMap<>();
        // iterate through the graph and create ControlFlowEntry for each node based on the location information and the edges in the graph. For each node, add edges to successor nodes based on the outgoing edges in the graph, and add a null successor if the node is marked as a terminal node. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
        for (int i = 0; i < graph.size(); ++i) {
            // for each node in the graph, get the corresponding TextLocation from the propagated location information. If the TextLocation is null, it means that the node does not correspond to a specific location in source code, and it can be skipped. If the TextLocation is not null, create a ControlFlowEntry for that node and add edges to successor nodes based on the outgoing edges in the graph. If a successor node has a non-null TextLocation, add it as a successor to the current node. If a successor node has a null TextLocation, it means that it does not correspond to a specific location in source code, and it can be skipped. If the current node is marked as a terminal node, add a null successor to indicate that the control flow can exit the method at that point.
            for (int j : graph.outgoingEdges(i)) {
                // for each successor node, add edges to the corresponding TextLocations based on the propagated location information. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
                for (TextLocation from : locations[i]) {
                    for (TextLocation to : locations[j]) {
                        builder.computeIfAbsent(from, k -> new LinkedHashSet<>()).add(to);
                    }
                }
            }
            // if the current node is marked as a terminal node, add a null successor to indicate that the control flow can exit the method at that point. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
            if (visitor.terminalNodes.get(i)) {
                // if the current node is marked as a terminal node, add a null successor to indicate that the control flow can exit the method at that point. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
                for (TextLocation loc : locations[i]) {
                    builder.computeIfAbsent(loc, k -> new LinkedHashSet<>()).add(null);
                }
            }
        }

        // an array of ControlFlowEntry is created from the builder map, where each entry contains a TextLocation and an array of successor TextLocations based on the edges in the graph and the terminal nodes. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining. The TextLocation represents the location in source code that corresponds to the node, while the array of successor TextLocations represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point.
        ControlFlowEntry[] result = new ControlFlowEntry[builder.size()];
        int index = 0;
        // iterate through the builder map and create ControlFlowEntry for each entry based on the TextLocation and the set of successor TextLocations. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining. The TextLocation represents the location in source code that corresponds to the node, while the array of successor TextLocations represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point.
        for (Map.Entry<TextLocation, Set<TextLocation>> entry : builder.entrySet()) {
            // for each entry in the builder map, create a ControlFlowEntry with the corresponding TextLocation and an array of successor TextLocations based on the set of successor TextLocations. The resulting ControlFlowEntry array can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining. The TextLocation represents the location in source code that corresponds to the node, while the array of successor TextLocations represents the possible control flow paths from that node. If a successor is null, it indicates that the control flow can exit the method at that point.
            result[index++] = new ControlFlowEntry(entry.getKey(), entry.getValue().toArray(new TextLocation[0]));
        }
        return result;
    }

    /**
     * a helper method that propagates location information through the control flow graph. It takes an array of {@link TextLocation}s, where each index corresponds to a node in the graph, and a {@link Graph} that represents the control flow graph. It returns a two-dimensional array of {@link TextLocation}s, where each index corresponds to a node in the graph, and contains all the locations that can be reached from that node through the control flow. The propagation is done using a breadth-first search algorithm, starting from nodes that have non-null location information and marking them as visited. For each visited node, its location information is added to the result for that node, and its successors are added to the queue if they have not been visited yet. The resulting array can be used to determine which locations can be reached from each node in the control flow graph.
     * 
     * @param locations an array of {@link TextLocation}s, where each index corresponds to a node in the graph. The value at each index can be null, which indicates that the node does not have location information, or a non-null {@link TextLocation}, which indicates that the node corresponds to a specific location in source code.
     * @param graph a {@link Graph} that represents the control flow graph of the method. The graph should have the same number of nodes as the length of the locations array, and should have edges that represent the possible control flow paths between nodes.
     * 
     * @return a two-dimensional array of {@link TextLocation}s, where each index corresponds to a node in the graph, and contains all the locations that can be reached from that node through the control flow. The resulting array can be used to determine which locations can be reached from each node in the control flow graph.
     * 
     * @implNote
     * ...
     * 
     */
    private static TextLocation[][] propagate(TextLocation[] locations, Graph graph) {
        List<Set<TextLocation>> result = new ArrayList<>();
        /** an array that indicates whether a node has been visited during the breadth-first search. It is initialized to false for all nodes, and is set to true for nodes that have non-null location information at the beginning of the search. It is used to avoid visiting the same node multiple times and to ensure that the search terminates when all reachable nodes have been visited. */
        boolean[] stop = new boolean[graph.size()];
        /** a queue that holds the indices of nodes that have been visited but whose successors have not been processed yet. It is initialized with the indices of nodes that have non-null location information, and is used to perform a breadth-first search through the graph. Nodes are added to the queue when they are visited for the first time, and are removed from the queue when their successors are processed. The search continues until the queue is empty, which means that all reachable nodes have been visited and their location information has been propagated. */
        IntDeque queue = new IntArrayDeque();
        // initialize the result list and the stop array based on the input locations. For each node in the graph, if it has non-null location information, it is added to the result list for that node, marked as visited in the stop array, and added to the queue for further processing. If a node does not have location information, an empty set is added to the result list for that node, and it is not added to the queue.
        for (int i = 0; i < stop.length; ++i) {
            Set<TextLocation> set = new LinkedHashSet<>();
            result.add(set);
            if (locations[i] != null) {
                stop[i] = true;
                queue.addLast(i);
                set.add(locations[i]);
            }
        }

        // perform a breadth-first search through the graph, starting from nodes that have non-null location information. For each visited node, its location information is added to the result for that node, and its successors are added to the queue if they have not been visited yet. The search continues until the queue is empty, which means that all reachable nodes have been visited and their location information has been propagated.
        while (!queue.isEmpty()) {
            int node = queue.removeFirst();
            for (int successor : graph.outgoingEdges(node)) {
                if (stop[successor]) {
                    continue;
                }
                if (result.get(successor).addAll(result.get(node))) {
                    queue.addLast(successor);
                }
            }
        }

        // convert the result list of sets into a two-dimensional array of TextLocations, where each index corresponds to a node in the graph, and contains all the locations that can be reached from that node through the control flow. The resulting array can be used to determine which locations can be reached from each node in the control flow graph.
        return result.stream().map(s -> s.toArray(new TextLocation[0])).toArray(TextLocation[][]::new);
    }

    /**
     * a visitor that builds a control flow graph of a method, where nodes represent locations in source code and edges represent possible control flow between them. It uses {@link IdentifiedStatement} to identify statements that can be referred to from other statements, such as {@link BreakStatement} and {@link ContinueStatement}. It also uses {@link TextLocation} to store location information for statements and expressions, which can be used for debugging and error reporting. The resulting control flow graph can be used for various analyses and optimizations, such as dead code elimination, loop unrolling, and inlining.
     * 
     * the control flow graph is built by visiting the AST of a method and creating nodes for statements and expressions that have location information. Edges are created between nodes based on the structure of the AST and the control flow of the method. For example, a {@link ConditionalStatement} will create edges from the condition to the consequent and alternative branches, while a {@link WhileStatement} will create edges from the condition to the body and back to the condition. A {@link BreakStatement} will create an edge to its target statement, while a {@link ContinueStatement} will create an edge to its target statement. A {@link ReturnStatement} or {@link ThrowStatement} will create an edge to a special terminal node, which indicates that the control flow can exit the method at that point.
     * 
     */
    static class Visitor extends RecursiveVisitor {
        static final int[] EMPTY = new int[0];
        int[] nodes = EMPTY;
        ObjectIntMap<IdentifiedStatement> breakNodes = new ObjectIntHashMap<>();
        ObjectIntMap<IdentifiedStatement> continueNodes = new ObjectIntHashMap<>();
        IdentifiedStatement defaultBreakTarget;
        IdentifiedStatement defaultContinueTarget;
        GraphBuilder builder = new GraphBuilder();
        List<TextLocation> locations = new ArrayList<>();
        BitSet terminalNodes = new BitSet();

        @Override
        protected void afterVisit(Expr expr) {
            setLocation(expr.getLocation());
        }

        @Override
        public void visit(BlockStatement statement) {
            int exit = createNode(null);
            breakNodes.put(statement, exit);
            super.visit(statement);
            breakNodes.remove(statement);

            setNode(exit);
        }

        @Override
        public void visit(WhileStatement statement) {
            IdentifiedStatement oldDefaultBreakTarget = defaultBreakTarget;
            IdentifiedStatement oldDefaultContinueTarget = defaultContinueTarget;

            int head = createNode(null);
            int exit = createNode(null);

            setNode(head);

            breakNodes.put(statement, exit);
            continueNodes.put(statement, head);
            defaultBreakTarget = statement;
            defaultContinueTarget = statement;

            if (statement.getCondition() != null) {
                statement.getCondition().acceptVisitor(this);
            }
            for (int node : nodes) {
                builder.addEdge(node, exit);
            }
            visit(statement.getBody());
            for (int node : nodes) {
                builder.addEdge(node, head);
            }
            nodes = new int[] { exit };

            defaultBreakTarget = oldDefaultBreakTarget;
            defaultContinueTarget = oldDefaultContinueTarget;
            breakNodes.remove(statement);
            continueNodes.remove(statement);
        }

        @Override
        public void visit(SwitchStatement statement) {
            IdentifiedStatement oldDefaultBreakTarget = defaultBreakTarget;

            int exit = createNode(null);

            breakNodes.put(statement, exit);
            defaultBreakTarget = statement;

            statement.getValue().acceptVisitor(this);
            int[] headNodes = nodes;
            for (SwitchClause clause : statement.getClauses()) {
                nodes = headNodes;
                visit(clause.getBody());
                for (int node : nodes) {
                    builder.addEdge(node, exit);
                }
            }
            nodes = headNodes;
            visit(statement.getDefaultClause());
            for (int node : nodes) {
                builder.addEdge(node, exit);
            }

            nodes = new int[] { exit };

            defaultBreakTarget = oldDefaultBreakTarget;
            breakNodes.remove(statement);
        }

        @Override
        public void visit(ConditionalStatement statement) {
            statement.getCondition().acceptVisitor(this);
            IntArrayList exit = new IntArrayList();

            int[] head = nodes;
            visit(statement.getConsequent());
            exit.add(nodes);

            nodes = head;
            visit(statement.getAlternative());
            exit.add(nodes);

            nodes = distinct(exit);
        }

        private int[] distinct(IntArrayList list) {
            IntHashSet set = new IntHashSet();
            int j = 0;
            int[] result = new int[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                int e = list.get(i);
                if (set.add(e)) {
                    result[j++] = e;
                }
            }
            if (j < result.length) {
                result = Arrays.copyOf(result, j);
            }
            return result;
        }

        @Override
        public void visit(BreakStatement statement) {
            IdentifiedStatement target = statement.getTarget();
            if (target == null) {
                target = defaultBreakTarget;
            }
            int targetNode = breakNodes.get(target);
            for (int node : nodes) {
                builder.addEdge(node, targetNode);
            }
            nodes = EMPTY;
        }

        @Override
        public void visit(ContinueStatement statement) {
            IdentifiedStatement target = statement.getTarget();
            if (target == null) {
                target = defaultContinueTarget;
            }
            int targetNode = continueNodes.get(target);
            for (int node : nodes) {
                builder.addEdge(node, targetNode);
            }
            nodes = EMPTY;
        }

        @Override
        public void visit(ThrowStatement statement) {
            super.visit(statement);
            setLocation(statement.getLocation());
            nodes = EMPTY;
        }

        @Override
        public void visit(ReturnStatement statement) {
            super.visit(statement);
            setLocation(statement.getLocation());
            for (int node : nodes) {
                terminalNodes.set(node);
            }
            nodes = EMPTY;
        }

        @Override
        public void visit(TryCatchStatement statement) {
            int catchNode = createNode(null);
            for (Statement s : statement.getProtectedBody()) {
                s.acceptVisitor(this);
                for (int node : nodes) {
                    builder.addEdge(node, catchNode);
                }
            }

            nodes = new int[] { catchNode };
            visit(statement.getHandler());
        }

        @Override
        public void visit(AssignmentStatement statement) {
            super.visit(statement);
            setLocation(statement.getLocation());
        }

        @Override
        public void visit(InitClassStatement statement) {
            super.visit(statement);
            setLocation(statement.getLocation());
        }

        @Override
        public void visit(MonitorEnterStatement statement) {
            super.visit(statement);
            setLocation(statement.getLocation());
        }

        @Override
        public void visit(MonitorExitStatement statement) {
            super.visit(statement);
            setLocation(statement.getLocation());
        }

        @Override
        public void visit(ConditionalExpr expr) {
            expr.getCondition().acceptVisitor(this);
            IntArrayList exit = new IntArrayList();

            int[] head = nodes;
            expr.getConsequent().acceptVisitor(this);
            exit.add(nodes);

            nodes = head;
            expr.getAlternative().acceptVisitor(this);
            exit.add(nodes);

            nodes = distinct(exit);
        }

        @Override
        public void visit(BoundCheckExpr expr) {
            super.visit(expr);
            setLocation(expr.getLocation());
        }

        private void setNode(int node) {
            for (int prevNode : nodes) {
                builder.addEdge(prevNode, node);
            }
            nodes = new int[] { node };
        }

        private void setLocation(TextLocation location) {
            if (location == null || location.isEmpty()) {
                return;
            }
            int node = createNode(location);
            for (int prevNode : nodes) {
                builder.addEdge(prevNode, node);
            }
            nodes = new int[] { node };
        }

        private int createNode(TextLocation location) {
            int index = locations.size();
            locations.add(location);
            return index;
        }
    }
}
