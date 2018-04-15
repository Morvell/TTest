import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

class Main {

  public static void main(String[] args) {

    ConnectivitySearcher connectivitySearcher = new ConnectivitySearcher();


    // Простите что не написал тесты. Нет особо на это времени
    int[][] arr = {{1, 1, 0, 1, 1, 0, 0, 1, 1, 1},
                   {1, 1, 0, 1, 0, 0, 0, 0, 1, 0},
                   {0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                   {0, 1, 0, 1, 0, 0, 1, 1, 0, 0}};

    HashMap<Integer, ArrayList<Graph<Pair<Integer, Integer>, DefaultEdge>>> conarr = connectivitySearcher
        .connectivitySearch(arr);

    connectivitySearcher.removeIdentGraph(conarr);
    connectivitySearcher.printDifferentResult(conarr);

  }

} 