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
package net.sf.cb2java.copybook.floating;

import java.math.BigDecimal;

public interface Conversion
{
    static final Precision SINGLE = new Precision(4);
    static final Precision DOUBLE = new Precision(8);
    
    public BigDecimal fromBytes(byte[] input, Precision precision);
   
    public byte[] toBytes(BigDecimal data, Precision precision);
    
    public void validate(BigDecimal data, Precision precision);
    
    public static class Precision
    {
        public final int bytes;
        
        private Precision(int bytes)
        {
            this.bytes = bytes;
        }
    }
}