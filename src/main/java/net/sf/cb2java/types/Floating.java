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
package net.sf.cb2java.types;

import java.math.BigDecimal;
import net.sf.cb2java.Value;
import net.sf.cb2java.copybook.floating.Conversion;
import net.sf.cb2java.copybook.floating.Conversion.Precision;
import net.sf.cb2java.copybook.floating.IEEE754;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.FloatingData;

/**
 * Class used to represent COBOL floating types
 * 
 * @author Matt Watson
 */
public class Floating extends Leaf
{
    /** the precision of the number */
    private final Precision precision;
    /** the conversion object for interpreting the bytes */
    private final Conversion conversion;
    
    public Floating(String name, int level, int occurs, Precision precision)
    {
        super(name, level, occurs);
        
        this.precision = precision;
        
        Conversion temp;
        
        try {
            Class<?> clazz = Class.forName(getSettings().getFloatConversion());
            temp = (Conversion) clazz.newInstance();
        } catch (Exception e) {
            temp = new IEEE754();
        }
        
        conversion = temp;
    }
    
    public Floating(Precision precision)
    {
        this("", 0, 1, precision);
    }
    
    public Floating(Precision precision, Conversion conversion)
    {
        super("", 0, 1);
        
        this.precision = precision;
        this.conversion = conversion;
    }

    @Override
    public Value getValue()
    {
        return super.getValue() == null ? getSettings().getValues().ZEROS : super.getValue();
    }

    public int getLength()
    {
        return precision.bytes;
    }

    public Data create()
    {
        return new FloatingData(this);
    }

    public Data parse(byte[] input)
    {
        FloatingData data = (FloatingData) create();
        
        data.setValue(conversion.fromBytes(input, precision));
        
        return data;
    }

    public void validate(Object data) throws IllegalArgumentException
    {
        if (!(data instanceof BigDecimal)) {
            throw new IllegalArgumentException("only BigDecimal is supported");
        }
        
        conversion.validate((BigDecimal) data, precision);
    }

    public byte[] toBytes(Object data)
    {
//        System.out.println("float:" + data);
        return conversion.toBytes((BigDecimal) data, precision);
    }
}