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
package net.sf.cb2java.copybook;

import java.util.ArrayList;
import java.util.List;

import net.sf.cb2java.Settings;
import net.sf.cb2java.Value;
import net.sf.cb2java.Values;
import net.sf.cb2java.copybook.floating.Conversion;
import net.sf.cb2java.types.AlphaNumeric;
import net.sf.cb2java.types.Binary;
import net.sf.cb2java.types.Decimal;
import net.sf.cb2java.types.Element;
import net.sf.cb2java.types.Floating;
import net.sf.cb2java.types.Group;
import net.sf.cb2java.types.Numeric;
import net.sf.cb2java.types.Packed;
import net.sf.cb2java.types.SignedSeparate;

/**
 * our internal representation of a copybook "item" node
 */ 
class Item
{
    final boolean document;
    
    final Values values;
    
    /**
     * @param analyzer
     */
    Item(final Values values, final boolean document)
    {
        this.values = values;
        this.document = document;
    }

    String name;
    int level;
    Item parent;
    int length;
    
    List children = new ArrayList();
    
    String redefines;
    int occurs = 1;
    int minOccurs;  // not supported
    String dependsOn; // not supported
    
    boolean isAlpha;
    boolean signSeparate;
    SignedSeparate.Position signPosition = Settings.DEFAULT.getSignPosition();
    
    String picture;
    Value value;
    Usage usage;
    
    private Element element;
        
    void setParent(Item candidate)
    {
        if (level > candidate.level) {
            parent = candidate;
            parent.children.add(this);
        } else {
            setParent(candidate.parent);
        }
    }
    
    void createElement()
    {
        if (document) {
            createDocument();
//            Copybook copybook = (Copybook) element;
//            values.setCopybook(copybook);
//            copybook.values = values;
        } else if (picture == null) {
            if (usage == Usage.COMPUTATIONAL_1) {
                createSingleFloat();
            } else if (usage == Usage.COMPUTATIONAL_2) {
                createDoubleFloat();
            } else {
                createGroup();
            }
        } else if (isAlpha) {
            createAlphaNumeric();
        } else {
            if (usage == Usage.BINARY) {
                createBinary();
            } else if (usage == Usage.COMPUTATIONAL) {
                createBinary();
            } else if (usage == Usage.PACKED_DECIMAL) {
                createPacked();
            } else if (usage == Usage.COMPUTATIONAL_3) {
                createPacked();
            } else if (usage == Usage.COMPUTATIONAL_4) {
                createBinary();
            } else if (usage == Usage.COMPUTATIONAL_5) {
                createNativeBinary();
            } else if (signSeparate) {
                createSignedSeparate();
            } else {
                createDecimal();
            }
        }
        
        if (value != null) {
            element.setValue(value);
        }
    }
    
    private void createDocument()
    {
        element = new Copybook(name, values);
    }
    
    private void createGroup()
    {
        element = new Group(name, level, occurs);
    }
    
    private void createBinary()
    {
        element = new Binary(name, level, occurs, picture);
    }
    
    private void createNativeBinary()
    {
        element = new Binary.Native(name, level, occurs, picture);
    }
    
    private void createPacked()
    {
        element = new Packed(name, level, occurs, picture);
        ((Numeric) element).setSignPosition(signPosition);
    }
    
    private void createSignedSeparate()
    {
        element = new SignedSeparate(name, level, occurs, picture);
        ((Numeric) element).setSignPosition(signPosition);
    }
    
    private void createDecimal()
    {
        element = new Decimal(name, level, occurs, picture);
        ((Numeric) element).setSignPosition(signPosition);
    }
    
    private void createAlphaNumeric()
    {
        element = new AlphaNumeric(name, level, occurs, picture);
    }
    
    private void createSingleFloat()
    {
        element = new Floating(name, level, occurs, Conversion.SINGLE);
    }
    
    private void createDoubleFloat()
    {
        element = new Floating(name, level, occurs, Conversion.DOUBLE);
    }
    
    Element getElement()
    {
        if (element == null) createElement();
        
        return element;
    }
}