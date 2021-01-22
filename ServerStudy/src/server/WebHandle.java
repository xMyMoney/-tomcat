package server;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class WebHandle extends DefaultHandler {
    private List<Entity> entityList;
    private List<Mapping> mappingList;
    private Entity entity;
    private Mapping mapping;
    //存储操作标签
    private String tag;
    private boolean isMapping = false;

    public List<Entity> getEntityList() {
        return entityList;
    }

    public List<Mapping> getMappingList() {
        return mappingList;
    }

    @Override
    public void startDocument() throws SAXException {
        entityList = new ArrayList<>();
        mappingList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (null != qName) {
            tag = qName;
            if (tag.equals("servlet")) {
                entity = new Entity();
                isMapping = false;
            } else if (tag.equals("servlet-mapping")) {
                mapping = new Mapping();
                isMapping = true;
            }
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String contents = new String(ch, start, length).trim();
        if (null != tag) {
            if (isMapping) {
                if (tag.equals("servlet-name")) {
                    mapping.setName(contents);
                } else if (tag.equals("url-pattern")) {
                    mapping.addPattern(contents);
                }
            }else {
                if (tag.equals("servlet-name")) {
                    entity.setName(contents);
                } else if (tag.equals("servlet-class")) {
                    entity.setClz(contents);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (null != qName) {
            if (qName.equals("servlet")) {
                entityList.add(entity);
            } else if (qName.equals("servlet-mapping")) {
                mappingList.add(mapping);
            }
        }
        tag = null;
    }
}