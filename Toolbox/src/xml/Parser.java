package xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser
{

	public void loadFile(String file)
	{
		try
		{

			File xmlfile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlfile);
			doc.getDocumentElement().normalize();

			System.out.println("root of xml file"
					+ doc.getDocumentElement().getNodeName());

			NodeList nodes = doc.getElementsByTagName("Results");
			System.out.println("==========================");

			for (int i = 0; i < nodes.getLength(); i++)
			{
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					System.out.println("Stock Symbol: "
							+ getValue("symbol", element));
					System.out.println("Stock Price: "
							+ getValue("price", element));
					System.out.println("Stock Quantity: "
							+ getValue("quantity", element));
				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static String getValue(String tag, Element element)
	{
		NodeList nodes = element.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();

	}

	// Read more:
	// http://javarevisited.blogspot.com/2011/12/parse-xml-file-in-java-example-tutorial.html#ixzz2SiCVapIc

}
