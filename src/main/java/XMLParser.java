import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class XMLParser.
 * Create ArrayList with parsed request dates.
 */
public class XMLParser {
    private String file;
    private Map currencies = new TreeMap<Integer, Currency>();

    public XMLParser(String file) {
        this.file = file;
    }

    public Map parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        new Data("data.xml");

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("src/main/resources/" + file);
            NodeList currencyList = doc.getElementsByTagName("Valute");

            iterateVal(currencyList);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return currencies;
    }

    private void iterateVal(NodeList currencyList) {
        for (int i = 0; i < currencyList.getLength(); i++) {
            Node currency = currencyList.item(i);

            if (currency.getNodeType() == Node.ELEMENT_NODE) {
                Element val = (Element) currency;
                String id = val.getAttribute("ID");

                NodeList valList = val.getChildNodes();

                addToList(id, valList);
            }
        }
    }

    private void addToList(String id, NodeList valList) {
        int numCode = 0;
        String charCode = null;
        int nominal = 0;
        String name = null;
        double value = 0;

        for (int j = 0; j < valList.getLength(); j++) {
            Node item = valList.item(j);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;

                switch (element.getTagName()) {
                    case "NumCode":
                        numCode = Integer.parseInt(element.getTextContent());
                        break;
                    case "CharCode":
                        charCode = element.getTextContent();
                        break;
                    case "Nominal":
                        nominal = Integer.parseInt(element.getTextContent());
                        break;
                    case "Name":
                        name = element.getTextContent();
                        break;
                    case "Value":
                        value = Double.parseDouble(element.getTextContent());
                }
            }
        }

        currencies.put(name, new Currency(name, numCode, charCode, nominal, value));
    }
}
