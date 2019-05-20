package com.nablanet.documents.odf.content.spreadsheet;

import com.nablanet.documents.odf.content.BaseElement;
import com.nablanet.documents.odf.content.TextElement;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CellElement extends BaseElement {

    public CellElement(Element element) {
        super(element);
    }

    public List<TextElement> getValue() {
        List<TextElement> paragraphs = new ArrayList<>();
        NodeList nodeList = getElement().getChildNodes();
        TextElement textElement;
        for (int i = 0 ; i < nodeList.getLength() ; i++)
            if (nodeList.item(i).getNodeName().equals("text:p")) {
                textElement = new TextElement(getDocument());
                textElement.setValue(nodeList.item(i).getTextContent());
                paragraphs.add(textElement);
            }
        return paragraphs;
    }

    public void setIntegerValue(int value) {
        setAttr("calcext:value-type", "float");
        setAttr("office:value-type", "float");
        setAttr("office:value", String.valueOf(value));
        setTextValue(String.valueOf(value));
    }

    public void setFloatValue(Float value) {
        setAttr("calcext:value-type", "float");
        setAttr("office:value-type", "float");
        setAttr("office:value", String.format("%.3f", value).replace(",","."));
        setTextValue(String.format("%.3f", value).replace(",","."));
    }

    public void setPercentageValue(Float value) {
        setAttr("calcext:value-type", "percentage");
        setAttr("office:value-type", "percentage");
        setAttr("office:value", String.format("%.2f", value)
                .replace(",","."));
        setTextValue(String.format("%d%%", (int)(value * 100)));
    }

    public void setDate(Long longDate) {

        DateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat format2 = new SimpleDateFormat("dd/mm/yy");

        Date date = new Date(longDate);

        setAttr("calcext:value-type", "date");
        setAttr("office:value-type", "date");
        setAttr("office:date-value", format1.format(date));
        setTextValue(format2.format(date));

    }

    public void setTextValue(String... values) {
        setTextValue(Arrays.asList(values));
    }

    public void setTextValue(List<String> values) {
        removeChilds();
        TextElement textElement;
        for (String value : values) {
            textElement = new TextElement(getDocument());
            textElement.setValue(value);
            getElement().appendChild(textElement.getElement());
        }
    }

}
