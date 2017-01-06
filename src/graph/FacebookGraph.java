package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import util.GraphLoader;

public class FacebookGraph implements Graph {

	private int size;

	private Map<Integer, HashSet<Integer>> adjList = new HashMap<Integer, HashSet<Integer>>();
	private List<FacebookGraph> stronglyConnected = new ArrayList<FacebookGraph>();
	public int getSize()
	{
		return size;
	}
	
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) 
	{
		adjList.put(num, new HashSet<Integer>());
		size++;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) 
	{
		if (this.exportGraph().containsKey(from) && this.exportGraph().containsKey(to))
			adjList.get(from).add(to);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) 
	{
		if (!adjList.containsKey(center)) 
			return new FacebookGraph();
		FacebookGraph egonet = new FacebookGraph();
		egonet.addVertex(center);
		for (Integer vertex: adjList.get(center))
			egonet.addVertex(vertex);
		for (Integer from: egonet.adjList.keySet())
			for (Integer to: egonet.adjList.keySet())
				if (adjList.get(from).contains(to))
					egonet.addEdge(from, to);
		return egonet;
	}
	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs(): Returns all Strongly Connected Components (SSCs) 
	 * of the graph.
	 */
	@Override
	public List<Graph> getSCCs() 
	{
		Stack<Integer> finished = DFS(this.vertices(), false);
		FacebookGraph transpose = this.graphTanspose();
		transpose.DFS(finished, true);
		for (FacebookGraph graph: transpose.stronglyConnected)
			this.stronglyConnected.add(graph.graphTanspose());
		List<Graph> stronglyConnectedGraphs = new ArrayList<Graph>();
		for (FacebookGraph graph: stronglyConnected)
			stronglyConnectedGraphs.add(graph);
		return stronglyConnectedGraphs;
	}
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() 
	{
		return (HashMap<Integer, HashSet<Integer>>)adjList;
	}
	public Stack<Integer> DFS(Stack<Integer> vertices, boolean strongCon)
	{
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		while (!vertices.isEmpty())
		{
			Integer v = vertices.pop();
			if (!visited.contains(v))
			{
				DFSVisit(v, visited, finished);
				if (strongCon)
				    stronglyConnected(finished);
			}
		}
		return finished;
	}
	public void stronglyConnected(Stack<Integer> finished)
	{
		FacebookGraph graph = new FacebookGraph();
		List<Integer> vertices = new ArrayList<Integer>();
		int sizeSCG = stronglyConnected.size();
		int i = 0, j = 0;
		while (j < sizeSCG)
		{
			i += stronglyConnected.get(j).getSize();
			j++;
		}
		while (i < finished.size())
		{
			vertices.add(finished.get(i));
			i++;
		}
		for (Integer vertex: vertices)
			graph.addVertex(vertex);
		for (Integer vertex: vertices)
			for (Integer a: adjList.get(vertex))
				graph.addEdge(vertex, a);
		stronglyConnected.add(graph);
	}
	public void DFSVisit(Integer vertex, HashSet<Integer> visited, Stack<Integer> finished)
	{
		visited.add(vertex);
		for (Integer n: exportGraph().get(vertex))
			if (!visited.contains(n))
				DFSVisit(n, visited, finished);
		finished.push(vertex);
	}
	public Stack<Integer> vertices(){
		Stack<Integer> vertices = new Stack<Integer>();
		for (Integer vertex: adjList.keySet())
		{
			vertices.push(vertex);
		}
		return vertices;
	}
	public static void main(String[] args)
	{
		FacebookGraph testGraph = new FacebookGraph();
		GraphLoader.loadGraph(testGraph, "data/facebook_1000.txt");
		int minimum =  100000;
		int i;
		
	}

}
