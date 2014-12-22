package ru.tykvin.loader;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class PropertyLoader {

    private JAXBContext jc;
    private Unmarshaller um;
    private Marshaller m;

    public PropertyLoader() {
        try {
            jc = JAXBContext.newInstance(new Class[] {Config.class, TimesAdapter.class});
            um = jc.createUnmarshaller();
            m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void marshall(Config config) throws JAXBException {
        m.marshal(config, new File("properties.xml"));
    }

    public Config unmarshall() throws JAXBException {
        return (Config) um.unmarshal(new File("../properties.xml"));
    }
}
