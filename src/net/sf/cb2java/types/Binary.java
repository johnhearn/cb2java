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
import java.math.BigInteger;

import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.DecimalData;
import net.sf.cb2java.data.IntegerData;

/**
 * class that represents binary data types 
 * 
 * @author James Watson
 */
public class Binary extends Numeric
{
    private final int digits;
    private final int length;
    
    public Binary(String name, int level, int occurs, String picture)
    {
        super(name, level, occurs, picture);
        digits = super.getLength();
        length = getLength(digits);
    }
    
    public Binary(String name, String picture)
    {
        super(name, 0, 1, picture);
        digits = super.getLength();
        length = getLength(digits);
    }
    
    public Binary(String picture)
    {
        super("", 0, 1, picture);
        digits = super.getLength();
        length = getLength(digits);
    }
    
    public Binary(String name, int length, int decimalPlaces, boolean signed)
    {
        super(name, length, decimalPlaces, signed, null);
        digits = super.getLength();
        this.length = getLength(digits);
    }
    
    public Binary(int length, int decimalPlaces, boolean signed)
    {
        super("", length, decimalPlaces, signed, null);
        digits = super.getLength();
        this.length = getLength(digits);
    }
    
    protected static final int getLength(int digits)
    {   
        if (1 <= digits && digits <= 4) {
            return 2;
        } else if (5 <= digits && digits <= 9) {
            return 4;
        } else if (10 <= digits && digits <= 18) {
            return 8;
        } else {
            throw new IllegalArgumentException("invalid numeric length");
        }
    }
    
    public int getLength()
    {
        return length;
    }

    public int digits()
    {
        return digits;
    }
        
    public Data parse(byte[] input)
    {
//        display(input);
        
        BigInteger bigI = new BigInteger(input);
        
        Data data = create();
        
        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            
            BigDecimal bigD = new BigDecimal(bigI, decimalPlaces());
            
            dData.setValue(bigD);
            
            return data;
        } else {
            IntegerData iData = (IntegerData) data;
            
            iData.setValue(bigI);
            
            return data;
        }
    }

//    private static void display(byte[] bytes)
//    {
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            String s = "0" + Integer.toHexString(0xFF & b);
//            System.out.print(s.toUpperCase().substring(s.length() - 2));
//            System.out.print(' ');
//        }
//    }
    
    public byte[] toBytes(Object data)
    {
        BigInteger bigI;
        
        if (data == null) {
            bigI = BigInteger.ZERO;
        } else {
            bigI = getUnscaled(data);
        }
        
//        display(copybook.values.LOW_VALUES.fill(bigI.toByteArray(), length(), Value.LEFT));
        
        return getSettings().getValues().LOW_VALUES.fill(bigI.toByteArray(), getLength(), Value.LEFT);
    }
    
    /**
     * reverses the order of the provided input
     * 
     * @param input the bytes to reverse
     * @return the reversed byte
     */
    public static byte[] reverse(byte[] input)
    {
        final int length = input.length;
        byte[] output = new byte[length];
        
        for (int i = 0; i < length; i++) {
            output[length - (i + 1)] = input[i];
        }
        
        return output;
    }
    
    /**
     * Binary extension for native types 
     * 
     * @author Matt Watson
     */
    public static class Native extends Binary
    {
        public Native(String name, int level, int occurs, String picture)
        {
            super(name, level, occurs, picture);
        }
        
        public Native(String name, String picture)
        {
            super(name, 0, 1, picture);
        }
        
        public Native(String picture)
        {
            super("", 0, 1, picture);
        }

        public Native(String name, int length, int decimalPlaces, boolean signed)
        {
            super(name, length, decimalPlaces, signed);
        }
        
        public Native(int length, int decimalPlaces, boolean signed)
        {
            super("", length, decimalPlaces, signed);
        }
        
        public byte[] toBytes(Object data)
        {
            byte[] bytes = super.toBytes(data);
            
            return getSettings().getLittleEndian() ? reverse(bytes) : bytes;
        }
        
        public Data parse(byte[] input)
        {
            return super.parse(getSettings().getLittleEndian() ? reverse(input) : input);
        }
    }
}