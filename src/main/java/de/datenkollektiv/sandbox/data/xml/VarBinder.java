package de.datenkollektiv.sandbox.data.xml;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;

public interface VarBinder {
    void bindVariables(XQPreparedExpression expr) throws XQException;
}
