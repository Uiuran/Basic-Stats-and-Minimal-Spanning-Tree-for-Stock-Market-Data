/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseStats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.TreeSet;
import java.util.Vector;
import java.util.HashSet;

/**
 *
 * @author penalva
 */
public class Kruskal{
    
    private static void scrapGraph(KruskalEdges mstedges,String path){

        try{

            Writer write = new FileWriter("/home/penalva/Dropbox/Public/Data/stdplot/"+path+".txt");
            while(!mstedges.getEdges().isEmpty())
            {write.write(mstedges.getEdges().first().getVertexA()+"\t"+mstedges.getEdges().first().getVertexB()+"\t"+mstedges.getEdges().first().getWeight()+"\n");
             mstedges.getEdges().pollFirst();}
            write.close();

        }catch(IOException e){ System.err.println(e);}

    }
    
    public static void mstree(TreeSet<Edge> edges,String path)
    {

        KruskalEdges vv = new KruskalEdges();

        for (Edge edge : edges) {
            vv.insertEdge(edge);
        }

        scrapGraph(vv,path);

    }

    public static KruskalEdges mstree(TreeSet<Edge> edges)
    {

        KruskalEdges vv = new KruskalEdges();

        for (Edge edge : edges) {
            vv.insertEdge(edge);
        }

        return vv;

    }

    

    /*create a forest F (a set of trees), where each vertex in the graph is a separate tree
    create a set S containing all the edges in the graph
    while S is nonempty and F is not yet spanning
    remove an edge with minimum weight from S
    if that edge connects two different trees, then add it to the forest, combining two trees into a single tree
    otherwise discard that edge.
    At the termination of the algorithm, the forest has only one component and forms a minimum spanning tree of the graph.*/


}
