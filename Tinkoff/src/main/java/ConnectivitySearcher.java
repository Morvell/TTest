import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ConnectivitySearcher {

  private HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> mass;

  /**
   * Проверка на изоморфность 2 графов
   *
   * @return Изоморфен ли граф или нет
   */
  private boolean isIsomorphism(
      VF2GraphIsomorphismInspector<Pair<Integer, Integer>, DefaultEdge> inspector) {
    inspector.getMappings();
    Iterator<GraphMapping<Pair<Integer, Integer>, DefaultEdge>> iterator = inspector.getMappings();
    return iterator.hasNext();
  }

  /**
   * печатает количество различных графов с разным количеством вершин.
   * А так же общее количество
   */
  public void printDifferentResult(
      HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> mass) {
    AtomicInteger summ = new AtomicInteger();
    mass.forEach((integer, graphs) -> {
      System.out.println(integer + " :" + graphs.size());
      summ.addAndGet(graphs.size());
    });
    System.out.println("Different: " + summ);
  }

  /**
   * Удаляет изоморфные графы
   */
  public void removeIdentGraph(
      HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> mass) {
    this.mass = mass;

    ExecutorService executor = Executors
        .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    mass.forEach((integer, graphs) -> {
      executor.submit(() -> {
        HashSet<Graph<Pair<Integer, Integer>, DefaultEdge>> duplicate = new HashSet<>();
        VF2GraphIsomorphismInspector<Pair<Integer, Integer>, DefaultEdge> inspector;
        for (int i = 0; i < graphs.size(); i++) {
          for (int j = 0; j < graphs.size(); j++) {
            if (i == j || duplicate.contains(graphs.get(i))) {
              break;
            }
            inspector = new VF2GraphIsomorphismInspector<>(graphs.get(i), graphs.get(j));
            if (isIsomorphism(inspector)) {
              duplicate.add(graphs.get(j));
            }
          }
        }
        graphs.removeAll(duplicate);

      });


    });
    executor.shutdown();
    try {
      if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }
  }


  /**
   * Поиск компонент связности
   *
   * @return массив с компонентами связности
   */
  public HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> connectivitySearch(
      int[][] maze) {

    Queue<Pair<Integer, Integer>> q = new LinkedList<>();

    HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> mass = new HashMap<>();

    q.add(new Pair<>(0, 0));

    checkForConnectivitySearch(0, 0, maze, q, mass);

    while (!q.isEmpty()) {
      Pair<Integer, Integer> p = q.remove();

      if (isFree(p.getKey(), p.getValue() - 1, maze)) {
        checkForConnectivitySearch(p.getKey(), p.getValue() - 1, maze, q, mass);
      }

      if (isFree(p.getKey(), p.getValue() + 1, maze)) {
        checkForConnectivitySearch(p.getKey(), p.getValue() + 1, maze, q, mass);
      }

      if (isFree(p.getKey() - 1, p.getValue(), maze)) {
        checkForConnectivitySearch(p.getKey() - 1, p.getValue(), maze, q, mass);
      }

      if (isFree(p.getKey() + 1, p.getValue(), maze)) {
        checkForConnectivitySearch(p.getKey() + 1, p.getValue(), maze, q, mass);
      }

    }
    return mass;
  }

  private void checkForConnectivitySearch(Integer p1, Integer p2, int[][] maze,
      Queue<Pair<Integer, Integer>> q,
      HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> mass) {
    Pair<Integer, Integer> nextP = new Pair<>(p1, p2);
    if (maze[p1][p2] == 1) {
      addGraphToList(bfs(maze, q, nextP), mass);
    } else {
      maze[p1][p2] = -1;
      q.add(nextP);
    }
  }

  private void addGraphToList(Graph<Pair<Integer, Integer>, DefaultEdge> result,
      HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> mass) {
    int size = result.vertexSet().size();
    ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>> r = mass.get(size);
    if (r == null) {
      r = new ArrayList<>();
    }
    r.add(result);
    mass.put(size, r);
  }


  /**
   * Определение компоненты связности
   */
  private Graph<Pair<Integer, Integer>, DefaultEdge> bfs(int[][] maze,
      Queue<Pair<Integer, Integer>> globalQueue,
      Pair<Integer, Integer> start) {

    Graph<Pair<Integer, Integer>, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
    Queue<Pair<Integer, Integer>> q = new LinkedList<>();
    q.add(start);
    g.addVertex(start);

    maze[start.getKey()][start.getValue()] = -1;

    while (!q.isEmpty()) {
      Pair<Integer, Integer> p = q.remove();

      if (isFree(p.getKey(), p.getValue() - 1, maze)) {
        stepInBFS(p.getKey(), p.getValue() - 1, maze, g, q, globalQueue, p);
      }

      if (isFree(p.getKey(), p.getValue() + 1, maze)) {

        stepInBFS(p.getKey(), p.getValue() + 1, maze, g, q, globalQueue, p);
      }

      if (isFree(p.getKey() - 1, p.getValue(), maze)) {

        stepInBFS(p.getKey() - 1, p.getValue(), maze, g, q, globalQueue, p);
      }

      if (isFree(p.getKey() + 1, p.getValue(), maze)) {
        stepInBFS(p.getKey() + 1, p.getValue(), maze, g, q, globalQueue, p);
      }

    }

    return g;
  }

  private void stepInBFS(int p1, int p2, int[][] maze,
      Graph<Pair<Integer, Integer>, DefaultEdge> g, Queue<Pair<Integer, Integer>> q,
      Queue<Pair<Integer, Integer>> globalQueue,
      Pair<Integer, Integer> p) {
    if (maze[p1][p2] == 0) {
      globalQueue.add(new Pair<>(p1, p2));
      maze[p1][p2] = -1;
    } else {

      maze[p1][p2] = -1;
      Pair<Integer, Integer> nextP = new Pair<>(p1, p2);

      g.addVertex(nextP);
      g.addEdge(p, nextP);

      q.add(nextP);
    }
  }

  private boolean isFree(int x, int y, int[][] arr) {
    return (x >= 0 && x < arr.length) && (y >= 0 && y < arr[x].length) && arr[x][y] != -1;
  }

}
