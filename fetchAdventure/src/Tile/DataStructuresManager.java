package Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps parallel data structures for tiles and maps:
 * - ArrayList<Tile> tilesList
 * - LinkedList<int[]> mapRows    (each int[] is one row of mapTileNum)
 * - Stack<int[][]> mapHistory    (store previous maps for undo)
 * - Queue<String> mapLoadQueue   (requested maps to load)
 * - Simple BST for tile IDs (tileIdTree)
 * - Simple Graph adjacency list for passable tile connectivity
 *
 * This class does NOT replace the original fields in TileManager.
 * It only mirrors and supplements them so you can use common data structures.
 */
public class DataStructuresManager {

    public final ArrayList<Tile> tilesList = new ArrayList<>();
    public final LinkedList<int[]> mapRows = new LinkedList<>();
    public final Stack<int[][]> mapHistory = new Stack<>();
    public final Queue<String> mapLoadQueue = new java.util.ArrayDeque<>();
    public final IntBST tileIdTree = new IntBST();
    public final Graph graph = new Graph();

    // helper: mirror tiles array into ArrayList
    public void syncTilesArray(Tile[] tiles) {
        tilesList.clear();
        if (tiles == null) return;
        for (Tile t : tiles) tilesList.add(t);
    }

    // helper: push current map to history (copy)
    public void pushMapToHistory(int[][] mapTileNum) {
        if (mapTileNum == null) return;
        int cols = mapTileNum.length;
        int rows = mapTileNum[0].length;
        int[][] copy = new int[cols][rows];
        for (int c = 0; c < cols; c++) {
            System.arraycopy(mapTileNum[c], 0, copy[c], 0, rows);
        }
        mapHistory.push(copy);
    }

    // helper: mirror rows for quick row traversal
    public void syncMapRows(int[][] mapTileNum) {
        mapRows.clear();
        if (mapTileNum == null) return;
        int cols = mapTileNum.length;
        int rows = mapTileNum[0].length;
        for (int r = 0; r < rows; r++) {
            int[] rowArr = new int[cols];
            for (int c = 0; c < cols; c++) {
                rowArr[c] = mapTileNum[c][r];
            }
            mapRows.add(rowArr);
        }
    }

    // Build BST of tile IDs present in the tiles array
    public void buildTileIdTree(Tile[] tiles) {
        tileIdTree.clear();
        if (tiles == null) return;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) tileIdTree.insert(i);
        }
    }

    // Build graph of passable tiles (0..N-1 node ids)
    // nodeId = row * cols + col
    public void buildGraphFromMap(int[][] mapTileNum, int cols, int rows, Tile[] tiles) {
        graph.clear();
        if (mapTileNum == null || tiles == null) return;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int tileNum = mapTileNum[c][r];
                int nodeId = r * cols + c;
                // if tile exists and is NOT collision => passable
                boolean passable = (tileNum >= 0 && tileNum < tiles.length) && !tiles[tileNum].collision;
                graph.addNode(nodeId, passable);
            }
        }

        // connect 4-neighbors if both passable
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int src = r * cols + c;
                if (!graph.isPassable(src)) continue;
                int[][] deltas = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
                for (int[] d : deltas) {
                    int nc = c + d[0];
                    int nr = r + d[1];
                    if (nc >= 0 && nc < cols && nr >= 0 && nr < rows) {
                        int dst = nr * cols + nc;
                        if (graph.isPassable(dst)) graph.addEdge(src, dst);
                    }
                }
            }
        }
    }

    // Simple integer BST (stores tile indices)
  
    public static class IntBST {
        private Node root;
        private static class Node {
            int val;
            Node left, right;
            Node(int v){ val = v; }
        }

        public void insert(int v) { root = insertRec(root, v); }
        private Node insertRec(Node n, int v) {
            if (n == null) return new Node(v);
            if (v < n.val) n.left = insertRec(n.left, v);
            else if (v > n.val) n.right = insertRec(n.right, v);
            return n;
        }
        public boolean contains(int v) { return containsRec(root, v); }
        private boolean containsRec(Node n, int v) {
            if (n == null) return false;
            if (v == n.val) return true;
            return v < n.val ? containsRec(n.left, v) : containsRec(n.right, v);
        }
        public void clear() { root = null; }
    }

  
    // Simple graph using adjacency lists
   
    public static class Graph {
        private final Map<Integer, List<Integer>> adj = new HashMap<>();
        private final Map<Integer, Boolean> passable = new HashMap<>();

        public void addNode(int id, boolean isPassable) {
            adj.putIfAbsent(id, new ArrayList<>());
            passable.put(id, isPassable);
        }
        public void addEdge(int a, int b) {
            adj.computeIfAbsent(a, k -> new ArrayList<>());
            adj.computeIfAbsent(b, k -> new ArrayList<>());
            adj.get(a).add(b);
        }
        public List<Integer> neighbors(int id) {
            return adj.getOrDefault(id, java.util.Collections.emptyList());
        }
        public boolean isPassable(int id) {
            Boolean v = passable.get(id);
            return v != null && v;
        }
        public void clear() { adj.clear(); passable.clear(); }
    }
}