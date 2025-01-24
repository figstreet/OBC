package com.figstreet.service.rest;

import com.figstreet.core.DateUtil;
import com.figstreet.service.exception.InvalidEntityException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiUtils {

    public static final Variant JSON_VARIANT = new Variant(MediaType.APPLICATION_JSON);
    public static final Variant XML_VARIANT = new Variant(MediaType.APPLICATION_XML);
    public static final Variant TEXT_VARIANT = new Variant(MediaType.TEXT_PLAIN);
    public static final List<Variant> PREFERRED_VARIANT_LIST;
    static {
        ArrayList<Variant> vList = new ArrayList<>(3);
        vList.add(JSON_VARIANT);
        vList.add(XML_VARIANT);
        vList.add(TEXT_VARIANT);
        PREFERRED_VARIANT_LIST = Collections.unmodifiableList(vList);
    }

    public static <E extends ApiData> Document asXmlDocument(E apiData)
            throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element element = doc.createElement(apiData.getNodeName());
        doc.appendChild(element);
        apiData.appendTo(element);
        return doc;
    }

    public static <E extends ApiData> String asXmlString(E apiData)
            throws ParserConfigurationException, TransformerException, IOException {
        Document xmlDoc = asXmlDocument(apiData);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            outputXml(xmlDoc, baos);
            return baos.toString("UTF-8");
        }
    }

    public static <E extends ApiData> Document asXmlDocument(ListApiData<E> listApiData) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        listApiData.appendTo(doc);

        return doc;
    }

    public static <E extends ApiData> String asXmlString(ListApiData<E> listApiData)
            throws ParserConfigurationException, TransformerException, IOException {
        Document xmlDoc = asXmlDocument(listApiData);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            outputXml(xmlDoc, baos);
            return baos.toString("UTF-8");
        }
    }

    public static void outputXml(Document document, OutputStream output)
            throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }


    public static String asString(Timestamp timestamp) {
        return DateUtil.formatTimestamp(timestamp, DateUtil.ISO8601_TIMESTAMP_FORMAT);
    }

    public static Timestamp asTimestamp(String value) {
        return DateUtil.parseTimestamp(value, DateUtil.ISO8601_TIMESTAMP_FORMAT);
    }

    public static void checkRole(ServerResource pServerResource, String pRole)
            throws ResourceException {
        if (!pServerResource.isInRole(pRole)) {
            throw new ResourceException(
                    Status.CLIENT_ERROR_FORBIDDEN.getCode(),
                    "You're not authorized to send this call.");
        }
    }

    /**
     * Checks that the given entity is not null.
     *
     * @param entity
     *            The entity to check.
     * @throws InvalidEntityException
     *             In case the entity is null.
     */
    public static void notNull(Object entity) throws InvalidEntityException {
        if (entity == null) {
            throw new InvalidEntityException("No input entity");
        }
    }

}
