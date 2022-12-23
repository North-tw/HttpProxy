package com.nv.test.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nv.expandUtil.util.ExceptionUtils;
import com.nv.expandUtil.util.StringUtils;
import com.nv.util.LogUtils;

public class ContextUtils {

	private static final Map<String, String> param = new HashMap<>();

	static {
		LogUtils.unitTest.info(ContextUtils.class.getName() + " start load class");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db
				.parse(new File(ContextUtils.class.getClassLoader().getResource(StringUtils.EMPTY).getPath()
					+ "context.xml"));
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("Parameter");
			for (int temp = 0; temp < list.getLength(); temp++) {

				Node node = list.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;
					param.put(element.getAttribute("name"), element.getAttribute("value"));
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LogUtils.unitTest.error(ContextUtils.class.getName() + " load class fail");
			throw ExceptionUtils.amendToUncheckedException(e);
		}

		LogUtils.unitTest.info(ContextUtils.class.getName() + " load class success");
	}

	public static String getInitParameter(String name) {
		return param.get(name);
	}
}
