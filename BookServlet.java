import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.SAXException;

public class BookServlet extends HttpServlet {
    private static final String XML_PATH = "path_to_books.xml";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int count = Integer.parseInt(request.getParameter("count"));

        try {
            // Load and parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(XML_PATH));

            // Find the book element by ID
            NodeList bookList = document.getElementsByTagName("book");
            for (int i = 0; i < bookList.getLength(); i++) {
                Element bookElement = (Element) bookList.item(i);
                int id = Integer.parseInt(bookElement.getElementsByTagName("id").item(0).getTextContent());

                if (id == bookId) {
                    // Update the count
                    Element countElement = (Element) bookElement.getElementsByTagName("count").item(0);
                    int currentCount = Integer.parseInt(countElement.getTextContent());
                    countElement.setTextContent(String.valueOf(currentCount - count));

                    // Save the updated XML
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(document);
                    StreamResult result = new StreamResult(new File(XML_PATH));
                    transformer.transform(source, result);

                    // Redirect back to the store
                    response.sendRedirect("index.html");
                    return;
                }
            }
        } catch (ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement logic to read XML data and send it as a response
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(XML_PATH));

            // Convert XML to a string
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            String xmlString = writer.getBuffer().toString();

            // Set the response content type and send XML
            response.setContentType("text/xml");
            response.getWriter().write(xmlString);
        } catch (ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
