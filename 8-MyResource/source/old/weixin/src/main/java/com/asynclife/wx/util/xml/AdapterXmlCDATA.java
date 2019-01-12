package com.asynclife.wx.util.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterXmlCDATA extends XmlAdapter<String, String> {

    @Override
    public String marshal(String value) throws Exception {
        return "<![CDATA[" + value + "]]>";
    }
    @Override
    public String unmarshal(String value) throws Exception {
        return value;
    }

}