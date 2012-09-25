/**
 *    cb2java - Dynamic COBOL copybook parser for Java.
 *    Copyright (C) 2006 James Watson
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 1, or (at your option)
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.cb2java.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.cb2java.types.Group;

public class GroupData extends Data
{
    protected final Group definition;
    protected final List children;
    private final List wrapper;// = new Wrapper();
    
    public GroupData(final Group definition, final List children)
    {
        super(definition);
        this.definition = definition;
        this.children = children;
        wrapper = Collections.unmodifiableList(children);
    }
    
//    private class Wrapper extends AbstractList
//    {
//        private Object data;
//        
//        public Object get(int index)
//        {
//            if (data == null) {
//                DataHolder holder = (DataHolder) children.get(index);
//                
//                data = holder.evaluate();
//            }
//            
//            return data;
//        }
//
//        public int size()
//        {
//            return children.size();
//        }
//    }
    
    @Override
    public boolean isLeaf()
    {
        return false;
    }
    
    /**
     * returns an immutable collection of children
     */
    @Override
    public List getChildren()
    {
        return wrapper;
    }
    
    /**
     * returns the first child with the specified name
     * irrespective of case
     * 
     * @param name the name of the child to look for
     * @return the first child with the given name
     * @throws IllegalArgumentException if no child is found
     */
    public Data getChild(String name)
    {
        for (Iterator i = wrapper.iterator(); i.hasNext();)
        {
            Data child = (Data) i.next();
            
            if (child.getName().equalsIgnoreCase(name)) return child;
        }
        
        return null;
    }
    
    @Override
    public String toString()
    {
        return toString("");
    }
    
    @Override
    public String toString(String indent)
    {
        StringBuffer buffer = new StringBuffer(indent);
        
        buffer.append(getName());
        
        for (Iterator i = wrapper.iterator(); i.hasNext();) {
            buffer.append('\n');
            buffer.append(((Data) i.next()).toString(indent + INDENT));
        }
        
        return buffer.toString();
    }
    
    @Override
    public void write(OutputStream stream) throws IOException
    {
        for (Iterator i = wrapper.iterator(); i.hasNext();) {
            Data child = (Data) i.next();
            child.write(stream);
        }
    }

    /**
     * returns the children of this item
     */
    @Override
    public Object getValue()
    {
        return getChildren();
    }

    @Override
    public Object translate(String data)
    {
        throw new UnsupportedOperationException("cannot convert string to group");
    }
    
    /**
     * not supported
     */
    @Override
    protected void setValueImpl(Object data)
    {
        throw new IllegalArgumentException("operation not yet supported for groups");
    }
}