package test;

import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class SimpleGraphView
{
	Graph<Integer, String> g;

	/** Creates a new instance of SimpleGraphView */
	public SimpleGraphView()
	{
		// Graph<V, E> where V is the type of the vertices and E is the type of
		// the edges
		g = new SparseMultigraph<Integer, String>();
		// Add some vertices. From above we defined these to be type Integer.
		g.addVertex(1);
		g.addVertex(2);
		g.addVertex(3);
		// Note that the default is for undirected edges, our Edges are Strings.
		// g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes
		// primitives
		// g.addEdge("Edge-B", 2, 3);
	}

	public static void main(String[] args)
	{
		SimpleGraphView sgv = new SimpleGraphView(); // We create our graph in
														// here
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Integer, String> layout = new CircleLayout(sgv.g);
		layout.setSize(new Dimension(300, 300)); // sets the initial size of the
													// layout space
		// The BasicVisualizationServer<V,E> is parameterized by the vertex and
		// edge types
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350)); // Sets the viewing area
														// size

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

}
