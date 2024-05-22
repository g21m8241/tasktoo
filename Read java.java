import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.Scanner;
import org.json.JSONObject;

public class ReadXML {
    public static void main(String[] args) {
        try {
            File xmlFile = new File("input.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            saxParser.parse(xmlFile, handler);
            JSONObject jsonOutput = handler.getJsonOutput();
            System.out.println(jsonOutput.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class XMLHandler extends DefaultHandler {
        private JSONObject jsonOutput = new JSONObject();
        private StringBuilder content = new StringBuilder();
        private boolean isFieldSelected = false;
        private String[] selectedFields;

        public JSONObject getJsonOutput() {
            return jsonOutput;
        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println("Enter the fields you want to display (comma-separated):");
            Scanner scanner = new Scanner(System.in);
            String inputFields = scanner.nextLine();
            selectedFields = inputFields.split(",");
            scanner.close();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (contains(selectedFields, qName)) {
                isFieldSelected = true;
            }
            content.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            content.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (isFieldSelected) {
                jsonOutput.put(qName, content.toString().trim());
                isFieldSelected = false;
            }
        }

        private boolean contains(String[] arr, String targetValue) {
            for (String s : arr) {
                if (s.trim().equals(targetValue)) {
                    return true;
                }
            }
            return false;
        }
    }
}

