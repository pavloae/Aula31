package com.nablanet.documents.odf.content.spreadsheet;

import com.nablanet.documents.odf.content.BaseElement;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public abstract class Sheet<T extends DataSheet> extends BaseElement {

    public Sheet(Element sheet) {
        super(sheet);
    }

    public String getName() {
        return getElement().getAttribute("table:name");
    }

    public void setName(String name) {
        getElement().setAttribute("table:name", name);
    }

    protected RowElement getRow(int position) {
        try {
            Node node = (Node) getXPath().evaluate(
                    String.format("./table:table-row[%d]", position),
                    getElement(), XPathConstants.NODE
            );
            return  new RowElement((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CellElement getCell(int row, int col) {
        try {
            Node node = (Node) getXPath().evaluate(
                    String.format("./table:table-row[%d]/table:table-cell[%d]", row, col),
                    getElement(), XPathConstants.NODE
            );
            return new CellElement((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setData(T data){
        if (data != null && data.getSheetName() != null && !data.getSheetName().isEmpty())
            setName(data.getSheetName());
    }

}
