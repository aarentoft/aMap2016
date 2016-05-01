package util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openstreetmap.osmosis.xml.v0_6.XmlReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.openstreetmap.osmosis.core.OsmosisRuntimeException;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionActivator;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.impl.OsmHandler;
import view.Loader;


/**
 * This is a tweaked version of org.openstreetmap.osmosis.xml.v0_6.XmlReader which
 * has been modified to update the loader window used by this map application.
 *
 * An OSM data source reading from an xml file. The entire contents of the file
 * are read.
 */
public class OsmosisXmlProgressReader implements RunnableSource {

    private static Logger log = Logger.getLogger(XmlReader.class.getName());

    private Sink sink;
    private File file;
    private boolean enableDateParsing;
    private CompressionMethod compressionMethod;
    private InputStream input;


    /**
     * Creates a new instance.
     *
     * @param file
     *            The file to read.
     * @param enableDateParsing
     *            If true, dates will be parsed from xml data, else the current
     *            date will be used thus saving parsing time.
     * @param compressionMethod
     *            Specifies the compression method to employ.
     */
    public OsmosisXmlProgressReader(File file, boolean enableDateParsing, CompressionMethod compressionMethod, InputStream input) {
        this.file = file;
        this.enableDateParsing = enableDateParsing;
        this.compressionMethod = compressionMethod;
        this.input = input;
    }


    /**
     * {@inheritDoc}
     */
    public void setSink(Sink sink) {
        this.sink = sink;
    }


    /**
     * Creates a new SAX parser.
     *
     * @return The newly created SAX parser.
     */
    private SAXParser createParser() {
        try {
            return SAXParserFactory.newInstance().newSAXParser();

        } catch (ParserConfigurationException | SAXException e) {
            throw new OsmosisRuntimeException("Unable to create SAX Parser.", e);
        }
    }


    /**
     * Reads all data from the file and send it to the sink.
     */
    public void run() {
        InputStream inputStream = null;

        try {
            SAXParser parser;

            sink.initialize(Collections.emptyMap());

            // make "-" an alias for /dev/stdin
            if (file.getName().equals("-")) {
                inputStream = System.in;
            } else {
                inputStream = input;
            }


            inputStream =
                    new CompressionActivator(compressionMethod).
                            createCompressionInputStream(inputStream);

            parser = createParser();

            parser.parse(inputStream, new OsmHandler(sink, enableDateParsing));

            sink.complete();

        } catch (SAXParseException e) {
            throw new OsmosisRuntimeException(
                    "Unable to parse xml file " + file
                            + ".  publicId=(" + e.getPublicId()
                            + "), systemId=(" + e.getSystemId()
                            + "), lineNumber=" + e.getLineNumber()
                            + ", columnNumber=" + e.getColumnNumber() + ".",
                    e);
        } catch (SAXException e) {
            throw new OsmosisRuntimeException("Unable to parse XML.", e);
        } catch (IOException e) {
            throw new OsmosisRuntimeException("Unable to read XML file " + file + ".", e);
        } finally {
            sink.release();

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Unable to close input stream.", e);
                }
                inputStream = null;
            }
        }
    }
}
