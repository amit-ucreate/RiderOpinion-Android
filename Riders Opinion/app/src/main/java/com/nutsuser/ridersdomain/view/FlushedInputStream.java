package com.nutsuser.ridersdomain.view;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by eweba1-pc-84 on 3/22/2016.
 */
public class FlushedInputStream extends FilterInputStream {

    public FlushedInputStream(final InputStream inputStream) {

        super(inputStream);

    }


    @Override

    public long skip(final long n) throws IOException {

        long totalBytesSkipped = 0L;

        while (totalBytesSkipped < n) {

            long bytesSkipped = in.skip(n - totalBytesSkipped);

            if (bytesSkipped == 0L) {

                int bytesRead = read();



                if (bytesRead < 0) { // we reached EOF

                    break;

                }

                bytesSkipped = 1;

            }

            totalBytesSkipped += bytesSkipped;

        }

        return totalBytesSkipped;

    }

}
