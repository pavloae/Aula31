package com.nablanet.documents.odf.content;

import com.nablanet.documents.NameSpaceResolver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.management.modelmbean.XMLParseException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public abstract class BaseElement {

    private Element element;

    public BaseElement(Element element) {
        this.element = element;
    }

    protected void setAttr(String name, String value) {
        element.setAttribute(name, value);
    }

    public Document getDocument() {
        return element.getOwnerDocument();
    }

    public Element getElement() {
        return element;
    }

    protected XPath getXPath() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NameSpaceResolver(getDocument()));
        return xPath;
    }

    public void removeChild(BaseElement child) {
        this.element.removeChild(child.getElement());
    }

    public void insertBefore(BaseElement newElement, BaseElement oldElement) {
        this.element.insertBefore(newElement.getElement(), oldElement.getElement());
    }

    public void removeChilds() {
        while (element.hasChildNodes())
            element.removeChild(element.getFirstChild());
    }

    public Element clone(boolean deepCopy) {
        return (Element) element.cloneNode(deepCopy);
    }

}
