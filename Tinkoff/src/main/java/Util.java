import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Util<K,V> {

  public boolean isIsomorphism(VF2GraphIsomorphismInspector<K, V> inspector){
    inspector.getMappings();
    Iterator<GraphMapping<K, V>> iterator = inspector.getMappings();
    return iterator.hasNext();
  }


  public ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>> connectivetySearch(int[][] maze){
    ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>> connectivety = new ArrayList<>();
    Queue<Pair<Integer, Integer>> q = new LinkedList<>();
    q.add(new Pair<>(0, 0));
    maze[0][0] = -1;

    while(!q.isEmpty()) {
      Pair<Integer, Integer> p = q.remove();

      if(isFreeOut(p.getKey(),p.getValue()-1, maze)) {
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey(),p.getValue()-1);
        if(maze[p.getKey()][p.getValue()-1]==1){
          connectivety.add(bfs(maze, nextP));
        }
        maze[p.getKey()][p.getValue()-1] = -1;
        q.add(nextP);
      }

      if(isFreeOut(p.getKey(),p.getValue()+1, maze)) {
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey(),p.getValue()+1);
        if(maze[p.getKey()][p.getValue()+1]==1){
          connectivety.add(bfs(maze, nextP));
        }
        maze[p.getKey()][p.getValue()+1] = -1;
        q.add(nextP);
      }

      if(isFreeOut(p.getKey()-1,p.getValue(), maze)) {
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey()-1,p.getValue());
        if(maze[p.getKey()-1][p.getValue()]==1){
          connectivety.add(bfs(maze, nextP));
        }
        maze[p.getKey()-1][p.getValue()] = -1;
        q.add(nextP);
      }

      if(isFreeOut(p.getKey()+1,p.getValue(), maze)) {
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey()+1,p.getValue());
        if(maze[p.getKey()+1][p.getValue()]==1){
          connectivety.add(bfs(maze, nextP));
        }
        maze[p.getKey()+1][p.getValue()] = -1;
        q.add(nextP);
      }

    }
    return connectivety;
  }


  private Graph<Pair<Integer, Integer>, DefaultEdge> bfs(int[][] maze, Pair<Integer, Integer> start){

    Graph<Pair<Integer, Integer>, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
    Queue<Pair<Integer, Integer>> q = new LinkedList<>();
    q.add(start);
    g.addVertex(start);


    maze[start.getKey()][start.getValue()] = -1;


    while(!q.isEmpty()) {
      Pair<Integer, Integer> p = q.remove();

      if(isFreeIn(p.getKey(),p.getValue()-1, maze)) {

        maze[p.getKey()][p.getValue()-1] = -1;
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey(),p.getValue()-1);

        g.addVertex(nextP);
        g.addEdge(p,nextP);

        q.add(nextP);
      }

      if(isFreeIn(p.getKey(),p.getValue()+1, maze)) {
        maze[p.getKey()][p.getValue()+1] = -1;
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey(),p.getValue()+1);

        g.addVertex(nextP);
        g.addEdge(p,nextP);

        q.add(nextP);
      }

      if(isFreeIn(p.getKey()-1,p.getValue(), maze)) {
        maze[p.getKey()-1][p.getValue()] = -1;
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey() -1,p.getValue());

        g.addVertex(nextP);
        g.addEdge(p,nextP);

        q.add(nextP);
      }

      if(isFreeIn(p.getKey()+1,p.getValue(), maze)) {
        maze[p.getKey()+1][p.getValue()] = -1;
        Pair<Integer, Integer> nextP = new Pair<>(p.getKey()+1,p.getValue());

        g.addVertex(nextP);
        g.addEdge(p,nextP);

        q.add(nextP);
      }

    }

    return g;
  }


  private boolean isFreeOut(int x, int y, int[][] arr) {
    if((x >= 0 && x < arr.length) && (y >= 0 && y < arr[x].length) && arr[x][y] != -1) {
      return true;
    }
    return false;
  }

  private boolean isFreeIn(int x, int y, int[][] arr) {
    if((x >= 0 && x < arr.length) && (y >= 0 && y < arr[x].length) && arr[x][y] != -1 && arr[x][y] == 1) {
      return true;
    }
    return false;
  }
}
