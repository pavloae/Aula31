package com.nablanet.documents.odf.content.spreadsheet;

import com.nablanet.documents.odf.content.BaseElement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class RowElement extends BaseElement {

    public RowElement(Element element) {
        super(element);
    }

    protected CellElement getCell(int position) {
        try {
            Node node = (Node) getXPath().evaluate(
                    String.format("./table:table-cell[%d]", position),
                    getElement(), XPathConstants.NODE
            );
            return  new CellElement((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
