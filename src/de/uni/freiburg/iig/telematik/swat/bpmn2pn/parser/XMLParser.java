package de.uni.freiburg.iig.telematik.swat.bpmn2pn.parser;

import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXException;

public class XMLParser {

	private Document doc;
	private XPath xpath;
	private String query;

	public XMLParser(String pathToFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder;
		this.doc = null;
		try {
			builder = factory.newDocumentBuilder();
			this.doc = builder.parse(pathToFile);
			// Create XPathFactory object
			XPathFactory xpathFactory = XPathFactory.newInstance();
			// Create XPath object
			this.xpath = xpathFactory.newXPath();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}   
	}

	public static void main(String[] args) {
		XMLParser parser = new XMLParser("bpmn-sample-definition.bpmn");
	}

	public List<String> XPathHelper(String query) {
		List<String> list = new ArrayList<>();
		try {
			XPathExpression expr =
					this.xpath.compile(query);
			NodeList nodes = (NodeList) expr.evaluate(this.doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				list.add(nodes.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return list;
	}

}    