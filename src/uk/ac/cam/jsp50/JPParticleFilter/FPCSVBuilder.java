package uk.ac.cam.jsp50.JPParticleFilter;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

public class FPCSVBuilder {

	public static class Vertex {
		String x,y,type,z,target;
		
		public Vertex(Node node) {
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				if (attribute.getNodeName().equals("edgetype")) {
					this.type = attribute.getNodeValue();
				}
				if (attribute.getNodeName().equals("target")) {
					this.target = attribute.getNodeValue();
				}
				if (attribute.getNodeName().equals("x")) {
					this.x = attribute.getNodeValue();
				}
				if (attribute.getNodeName().equals("y")) {
					this.y = attribute.getNodeValue();
				}
				if (attribute.getNodeName().equals("z")) {
					this.z = attribute.getNodeValue();
				}
			}
			
			double x = Double.parseDouble(this.x);
			double y = Double.parseDouble(this.y);
			x -= 920;
			y -= 990;
			
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.HALF_UP);
			this.x = df.format(x);
			this.y = df.format(y);
			
		}
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File("wgb_full.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("Room");
        Node room;
        
        HashSet<Integer> seenPolygons = new HashSet<Integer>();
        
        PrintWriter writer = new PrintWriter("wgb_ground.csv", "UTF-8");
        writer.println("0");
        
        for (int i = 0; i < nList.getLength(); i++) {
        	room = nList.item(i);
        	NodeList roomChildren = room.getChildNodes();
        	for (int child = 0; child < roomChildren.getLength(); child++) {
        		Node poly = roomChildren.item(child);
        		if (poly.getNodeType() == Node.ELEMENT_NODE && poly.getNodeName().equals("Poly")) {
        			seenPolygons.add(getUID(poly));
        			NodeList polyChildren = poly.getChildNodes();
        			ArrayList<Node> vertices = new ArrayList<Node>();
        			for (int polyChildNo = 0; polyChildNo < polyChildren.getLength(); polyChildNo++) {
        				Node vertex = polyChildren.item(polyChildNo);
        				if (vertex.getNodeName().equals("Vertex")) {
        					vertices.add(vertex);
        				}
        			}
        			if (vertices.size() > 1) {
        				for (int vertexNo = 0; vertexNo < vertices.size(); vertexNo++) {
        					Vertex firstVertex = new Vertex(vertices.get(vertexNo));
        					Vertex secondVertex = new Vertex(vertices.get((vertexNo+1)%vertices.size()));
        					if (firstVertex.z.equals("-7.04") && (!firstVertex.type.equals("connector") || !seenPolygons.contains(Integer.parseInt(firstVertex.target)))) {
        						String edge = getEdge(firstVertex, secondVertex);
        						System.out.println(edge);
        						writer.println(edge);
        					}
        				}
        				
        			}
        		}
        	}
        }
        
        writer.close();
        
	}
	
	public static int getUID(Node poly) {
		NamedNodeMap attrs = poly.getAttributes();
		for (int attrNo = 0; attrNo < attrs.getLength(); attrNo++) {
			if (attrs.item(attrNo).getNodeName().equals("uid")) {
				return Integer.parseInt(attrs.item(attrNo).getNodeValue());
			}
		}
		return -1;
	}
	
	public static String getEdge(Vertex firstVertex, Vertex secondVertex) {
		String edge = firstVertex.x + "," + firstVertex.y + "," + secondVertex.x + "," + secondVertex.y;
		if (firstVertex.type.equals("connector")) edge += ",d";
		return edge;
	}

}
