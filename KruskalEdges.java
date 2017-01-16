/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BaseStats;

import java.util.TreeSet;
import java.util.Vector;
import java.util.HashSet;
import java.io.*;



class KruskalEdges
{
    Vector<HashSet<String>> vertexGroups = new Vector<HashSet<String>>();
    Vector<String> newick = new Vector<String>();
    TreeSet<Edge> kruskalEdges = new TreeSet<Edge>();

    public TreeSet<Edge> getEdges()
    {
        return kruskalEdges;
    }

    HashSet<String> getVertexGroup(String vertex)
    {
        for (HashSet<String> vertexGroup : vertexGroups) {
            if (vertexGroup.contains(vertex)) {
                return vertexGroup;
            }
        }
        return null;
    }
    
    String getNewickGroup(String vertex)
    {
        for (String newickGroup : newick) {
            if (newickGroup.contains(vertex)) {
                return newickGroup ;
            
            }
        }
        return null;
    }

    private void newickSimple(Edge edge){

        String newBifurcation = new String();
        newBifurcation = edge.toString();
        this.newick.add(newBifurcation);

    }

    private void newickComplex(Edge edge){

        String vertexA = edge.getVertexA();
        String vertexB = edge.getVertexB();

        HashSet<String> vertexGroupA = getVertexGroup(vertexA);
        HashSet<String> vertexGroupB = getVertexGroup(vertexB);

        String newickgroupA = getNewickGroup(vertexA);
        String newickgroupB = getNewickGroup(vertexB);

        if( vertexGroupA == null ){

           String newnewick = "("+newickgroupB+":"+edge.getWeight()+","+vertexA+":"+edge.getWeight()+")";
           this.newick.removeElement(newickgroupB);
           this.newick.add(newnewick);

        }else{
           if(vertexGroupB == null ){
           String newnewick = "("+newickgroupA+":"+edge.getWeight()+","+vertexB+":"+edge.getWeight()+")";
           this.newick.removeElement(newickgroupA);
           this.newick.add(newnewick); }
           else{

               String newnewick = "("+newickgroupA+":"+edge.getWeight()+","+newickgroupB+":"+edge.getWeight()+")";
               this.newick.removeElement(newickgroupA);
               this.newick.removeElement(newickgroupB);
               this.newick.add(newnewick);

           }

        }

    }

    public void insertEdge(Edge edge)
    {
        String vertexA = edge.getVertexA();
        String vertexB = edge.getVertexB();

        HashSet<String> vertexGroupA = getVertexGroup(vertexA);
        HashSet<String> vertexGroupB = getVertexGroup(vertexB);

        if (vertexGroupA == null) {
            kruskalEdges.add(edge);
            if (vertexGroupB == null) {
                this.newickSimple(edge);
                HashSet<String> htNewVertexGroup = new HashSet<String>();
                htNewVertexGroup.add(vertexA);
                htNewVertexGroup.add(vertexB);
                vertexGroups.add(htNewVertexGroup);
                
            }
            else {
                this.newickComplex(edge);
                vertexGroupB.add(vertexA);
                
            }
        }
        else {
            if (vertexGroupB == null) {
                this.newickComplex(edge);
                vertexGroupA.add(vertexB);
                kruskalEdges.add(edge);
                
            }
            else if (vertexGroupA != vertexGroupB) {
                
                this.newickComplex(edge);
                vertexGroupA.addAll(vertexGroupB);
                vertexGroups.remove(vertexGroupB);
                kruskalEdges.add(edge);

            }
        }
    }

    public void extractNewick(String Path) throws IOException{

        Writer write = new FileWriter(Path);

        write.write(this.newick.lastElement());

        write.close();;

    }


}