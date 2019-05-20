package com.nablanet.documents.odf.content;

import org.w3c.dom.Document;

public class TextElement extends BaseElement {

    public TextElement(Document document) {
        super(document.createElement("text:p"));
    }

    public String getValue() {
        return getElement().getTextContent();
    }

    public void setValue(String value) {
        getElement().setTextContent(value);
    }

}
