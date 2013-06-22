/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.freedomotic.persistence;

import it.freedomotic.reactions.Payload;
import it.freedomotic.reactions.Statement;

import java.util.ArrayList;
import java.util.Iterator;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author Enrico
 */
public class PayloadConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
        Payload payload = (Payload) o;
        writer.startNode("payload");
        Iterator<Statement> it = payload.iterator();
        while (it.hasNext()) {
            Statement statement = it.next();
            writer.startNode("it.freedomotic.reactions.Statement");
            writer.startNode("logical");
            writer.setValue(statement.getLogical());
            writer.endNode(); //</logical>
            writer.startNode("attribute");
            writer.setValue(statement.getAttribute());
            writer.endNode(); //</attribute>
            writer.startNode("operand");
            writer.setValue(statement.getOperand());
            writer.endNode(); //</operand>
            writer.startNode("value");
            writer.setValue(statement.getValue());
            writer.endNode(); //</value>
            writer.endNode(); //</it.freedomotic.reactions.Statement>
        }
        writer.endNode(); //</payload>
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
        Payload payload = new Payload();
        reader.moveDown(); //goes down to <payload>
        while (reader.hasMoreChildren()) { //<statements> are the childs of payload
            reader.moveDown();
            ArrayList<String> statementValues = new ArrayList<String>();
            while (reader.hasMoreChildren()) { //childs of statement (logical, attribute, ...)
                reader.moveDown();
                statementValues.add(reader.getValue());
                reader.moveUp();
            }
            payload.addStatement(
                    statementValues.get(0),
                    statementValues.get(1),
                    statementValues.get(2),
                    statementValues.get(3));
            reader.moveUp(); //next <statement>
        } //no more <statements> (childs of payload)
        reader.moveUp(); //goes up to the next <payload>
        reader.moveUp(); //goes up to the next <payload>
        return payload;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(Payload.class);
    }
}
