package apeha.allinone.db.xml;

import apeha.allinone.common.Category;
import apeha.allinone.db.XMLHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.ItemSupportW;
import apeha.allinone.item.Property;
import apeha.allinone.items.TextFilesHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMLHandlerRW extends XMLHandler implements ItemSupportW {

    public XMLHandlerRW() {
        super();
    }

    public XMLHandlerRW(String pathToDb) {
        super(pathToDb);
    }

    @Override
    public void saveTo() {
        this.createDOMTree();
        DOMSource src = new DOMSource(dom);
        FileOutputStream fos = null;
        StreamResult result = null;
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t;
        try {
            File file = new File(dbStream);
            if (!file.exists())
                file.createNewFile();
            fos = new FileOutputStream(dbStream);
            result = new StreamResult(fos);
            t = tf.newTransformer();
            t.transform(src, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDOMTree() {
        Element root = dom.createElement("предметы");

        TextFilesHandler textFilesHandler = TextFilesHandler.getInstance();
        Map<Category, List<Item>> categoryAndItems = textFilesHandler
                .getCategoryAndItems();
        Iterator<Category> iterator = categoryAndItems.keySet().iterator();
        while (iterator.hasNext()) {
            Category cat = iterator.next();
            Element typeNode = this.createTypeNode(cat,
                    categoryAndItems.get(cat));
            root.appendChild(typeNode);
        }
        dom.appendChild(root);
    }

    private Element createTypeNode(Category cat, List<Item> list) {
        String typeName = cat.getType().replaceAll("\\ ", "_");
        Element type = null;
        if (typeName != null) {
            type = dom.createElement(typeName);
            Iterator<Item> it = list.iterator();
            while (it.hasNext()) {
                Element item = this.createItemNode(it.next());
                type.appendChild(item);
            }
        }

        return type;

    }

    private Element createItemNode(Item item) {
        String name = item.getName();
        name = name.replaceAll("\"", "Q");
        name = name.replaceAll("\\ ", "\\_");
        Element nameNode = dom.createElement(name);
        Map<Property, String> nameValueMap = item.getPropertiesAndValues();
        Iterator<Property> it = nameValueMap.keySet().iterator();
        while (it.hasNext()) {
            Property prop = it.next();
            Text propertyVal = dom.createTextNode(nameValueMap.get(prop)
                    .toString());
            String next = prop.getName().replaceAll("\\ ", "\\_");
            next = next.replaceAll("\\:", "");
            Element property = dom.createElement(next);

            property.appendChild(propertyVal);
            nameNode.appendChild(property);
        }
        Element imageNode = dom.createElement("Изображение");
        Text imageText = dom.createTextNode(item.getImageSrc().toString());
        imageNode.appendChild(imageText);
        nameNode.appendChild(imageNode);

        return nameNode;
    }

}
