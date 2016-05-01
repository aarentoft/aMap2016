package util.io;

import view.Loader;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OsmosisReaderProgressInputStream extends FilterInputStream {
    private Loader loader;
    private int counter;

    public OsmosisReaderProgressInputStream(InputStream in, Loader loader, int max) {
        super(in);
        this.loader = loader;
        this.counter = 0;
        if (loader != null)
            this.loader.setProgress(null, 0, max);
    }

    @Override
    public int read(byte[] buffer, int offset, int count) throws IOException {
        if (loader != null)
            loader.setProgress(counter += count);
        return super.read(buffer, offset, count);
    }
}
