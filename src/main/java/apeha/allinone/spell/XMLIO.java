package apeha.allinone.spell;

import apeha.allinone.common.Utils;
import apeha.allinone.item.ItemBuilder;
import apeha.allinone.item.spell.SpellableItem;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class XMLIO {
    Document dom;

    private void createDOMDoc() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try {
            db = dbf.newDocumentBuilder();
            dom = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void createDOMTree(List<SpellableItem> list) {
        Element rootNode = dom.createElement(XMLTags.items);

        for (SpellableItem item : list) {
            Element itemNode = this.createItemElement(item);
            rootNode.appendChild(itemNode);
        }

        dom.appendChild(rootNode);
    }

    private Element createItemElement(SpellableItem item) {
        Element itemNode = dom.createElement(XMLTags.item);

        createElement(itemNode, XMLTags.name, item.getName());
        createElement(itemNode, XMLTags.date,
                Utils.dateFormat.print(item.getDate()));
        createElement(itemNode, XMLTags.info, item.getProperties());

        String comment = item.getComment();
        if (comment != null) {
            createElement(itemNode, XMLTags.comment, comment);
        }

        return itemNode;
    }

    private void createElement(Element itemNode, String tag, String text) {
        Element node = dom.createElement(tag);
        Text textNode = dom.createTextNode(text);

        node.appendChild(textNode);
        itemNode.appendChild(node);
    }

    public String getXMLfromListAsString(List<SpellableItem> list) {
        this.createDOMDoc();
        this.createDOMTree(list);

        DOMSource domSource = new DOMSource(dom);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t;

        try {
            t = tf.newTransformer();
            t.transform(domSource, result);
            writer.close();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    public List<SpellableItem> getItemsFromXML(Reader reader) {
        List<SpellableItem> list = Lists.newArrayList();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        SpellableItem item;

        try {
            db = dbf.newDocumentBuilder();
            dom = db.parse(new InputSource(reader));
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
            return null;
        } catch (SAXException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }

        Element root = dom.getDocumentElement();
        for (Node child = root.getFirstChild(); child != null; child = child
                .getNextSibling()) {
            StringBuilder builder = new StringBuilder();
            DateTime date = null;
            String comment = null;

            for (Node subChild = child.getFirstChild(); subChild != null; subChild = subChild
                    .getNextSibling()) {
                String nodeName = subChild.getNodeName();
                String textContent = subChild.getTextContent();

                if (nodeName.equals(XMLTags.name)
                        || nodeName.equals(XMLTags.info)) {
                    builder.append(textContent + "\n");
                } else if (nodeName.equals(XMLTags.date)) {
                    date = Utils.formatDateFrom(textContent);
                } else if (nodeName.equals(XMLTags.comment)) {
                    comment = textContent.replaceAll("\t", "").trim();
                }
            }

            item = ItemBuilder.createSpellableItem(builder.toString(), date,
                    comment);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    public void saveItemsTo(List<SpellableItem> items, Writer writer) {
        try {
            writer.write(getXMLfromListAsString(items));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class XMLTags {
        static final String comment = "comment";
        static final String date = "date";
        static final String info = "info";
        static final String item = "item";
        static final String items = "items";
        static final String name = "name";
    }
}
