import java.util.ArrayList;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

class Main {
  public static void main(String[] args) {
    Graph<String, DefaultEdge> stringGraph1 = createStringGraph();
    Graph<String, DefaultEdge> stringGraph2 = createStringGraph2();

    Util<Pair<Integer, Integer>, DefaultEdge> util = new Util<>();


    int[][] arr = {{0,0,0,1,1},
                   {1,1,0,1,1},
                   {1,1,0,0,0}};


    VF2GraphIsomorphismInspector<Pair<Integer, Integer>, DefaultEdge> inspector;

    ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>> conarr =  util.connectivetySearch(arr);

    for (int i = 0; i < conarr.size() ; i++) {
      for (int j = 0; j < conarr.size() ; j++) {
        if(i==j) continue;
        inspector = new VF2GraphIsomorphismInspector<>(conarr.get(i), conarr.get(j));
        System.out.println(util.isIsomorphism(inspector));

      }

    }

    }





  private static Graph<String, DefaultEdge> createStringGraph2()
  {
    Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

    String v1 = "v1";
    String v2 = "v2";
    String v3 = "v3";
    String v4 = "v4";



    // add the vertices
    g.addVertex(v1);
    g.addVertex(v2);
    g.addVertex(v3);
    g.addVertex(v4);

    // add edges to create a circuit
    g.addEdge(v2, v3);


    return g;
  }

  private static Graph<String, DefaultEdge> createStringGraph()
  {
    Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

    String v1 = "v1";
    String v2 = "v2";
    String v3 = "v3";
    String v4 = "v4";

    // add the vertices
    g.addVertex(v1);
    g.addVertex(v2);
    g.addVertex(v3);
    g.addVertex(v4);

    // add edges to create a circuit
    g.addEdge(v1, v2);
    g.addEdge(v2, v3);
    g.addEdge(v4, v2);

    return g;
  }
} 