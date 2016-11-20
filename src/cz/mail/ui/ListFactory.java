package cz.mail.ui;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Create a JList, and handle the optional items attribute.
 * 
 * @author HuberB1
 */
public class ListFactory extends AbstractFactory {

    @Override
    public Object newInstance(FactoryBuilderSupport factoryBuilderSupport, Object value, Object name, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsNull(value, name);
        Object items = attributes.remove("items");
        if (items instanceof Vector) {
            return new JList((Vector) items);
        } else if (items instanceof List) {
            List list = (List) items;
            return new JList(list.toArray());
        } else if (items instanceof Object[]) {
            return new JList((Object[]) items);
        } else {
            return new JList();
        }
    }
}
