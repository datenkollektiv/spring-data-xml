package de.datenkollektiv.sandbox.data.xml.util;

import de.datenkollektiv.sandbox.data.xml.VarBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.xquery.*;
import java.io.Closeable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

public class CloseableXQConnection implements Closeable {

    private static Logger LOG = LoggerFactory.getLogger(CloseableXQConnection.class);

    private XQConnection xqConnection;

    public CloseableXQConnection(XQConnection xqConnection) {
        this.xqConnection = xqConnection;
    }

    public Stream<XQItem> items(String xquery, VarBinder... varBinder) throws XQException {
        XQPreparedExpression preparedExpression = xqConnection.prepareExpression(xquery);
        for (VarBinder binder : varBinder) {
            binder.bindVariables(preparedExpression);
        }
        XQResultSequence resultSequence = preparedExpression.executeQuery();

        Iterator<XQItem> xqItemIterator = new Iterator<XQItem>() {
            Optional<XQItem> next = empty();

            @Override
            public boolean hasNext() {
                if (next.isPresent()) {
                    return true;
                }
                try {
                    boolean hasNext = resultSequence.next();
                    if (hasNext) {
                        next = of(resultSequence.getItem());
                    }
                    return hasNext;
                } catch (XQException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public XQItem next() {
                if (next.isPresent()) {
                    XQItem nextItem = next.get();
                    next = empty();
                    return nextItem;
                }
                try {
                    boolean next = resultSequence.next();
                    if (next) {
                        return resultSequence.getItem();
                    } else {
                        throw new NoSuchElementException();
                    }
                } catch (XQException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return stream(spliteratorUnknownSize(xqItemIterator, ORDERED | NONNULL), false);
    }

    @Override
    public void close() {
        try {
            LOG.debug("Closing XQConnection...");
            this.xqConnection.close();
            LOG.debug("done.");
        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }
}
