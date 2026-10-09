package com.jaspersoft.ireport.designer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.StringReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import org.junit.Test;
import static org.junit.Assert.*;

/** JDK StAX must work for both IDE XML consumers and Jackson report metadata. */
public class XmlProviderCompatibilityTest {
    public static class Metadata {
        public String title;
        public int count;
    }

    @Test public void defaultStaxAndJacksonCanReadUnicodeXml() throws Exception {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        assertFalse("Private Woodstox provider must not leak into the IDE",
                factory.getClass().getName().startsWith("com.ctc.wstx."));
        var reader = factory.createXMLStreamReader(new StringReader("<report>ação &amp; venda</report>"));
        StringBuilder value = new StringBuilder();
        while (reader.hasNext()) {
            if (reader.next() == XMLStreamConstants.CHARACTERS) value.append(reader.getText());
        }
        reader.close();
        assertEquals("ação & venda", value.toString());
        XmlMapper mapper = new XmlMapper();
        Metadata before = new Metadata();
        before.title = "ação & venda";
        before.count = 215;
        Metadata after = mapper.readValue(mapper.writeValueAsString(before), Metadata.class);
        assertEquals(before.title, after.title);
        assertEquals(before.count, after.count);
    }

    @Test public void bundledSoapStackParsesOfflineEnvelope() throws Exception {
        String xml = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<s:Body><value>ação &amp; venda</value></s:Body></s:Envelope>";
        var message = MessageFactory.newInstance().createMessage(new MimeHeaders(),
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        assertEquals("ação & venda", message.getSOAPBody().getTextContent());
    }
}
