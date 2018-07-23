package de.datenkollektiv.sandbox.data.xml.util;

import org.w3c.dom.Node;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItem;

public class XQItems {

    public static int toInt(XQItem item) {
        try {
            return item.getInt();
        } catch (XQException e) {
            throw new IllegalArgumentException("Expected XQItem to be a valid int value.", e);
        }

    }

    public static long toLong(XQItem item) {
        try {
            return item.getLong();
        } catch (XQException e) {
            throw new IllegalArgumentException("Expected XQItem to be a valid long value.", e);
        }

    }

    public static Node toNode(XQItem item) {
        try {
            return item.getNode();
        } catch (XQException e) {
            throw new IllegalArgumentException("Expected XQItem to be a valid node", e);
        }

    }
}
