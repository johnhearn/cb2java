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

public class Usage
{
    private Usage() {}
    
    public static final Usage BINARY = new Usage();
    
    public static final Usage COMPUTATIONAL = new Usage(); // binary 
    
    public static final Usage COMPUTATIONAL_1 = new Usage(); // single precision float
    
    public static final Usage COMPUTATIONAL_2 = new Usage(); // double precision float
    
    public static final Usage COMPUTATIONAL_3 = new Usage(); // packed
    
    public static final Usage COMPUTATIONAL_4 = new Usage(); // binary
    
    public static final Usage COMPUTATIONAL_5 = new Usage(); // binary or comp-5
    
//    public static final Usage DISPLAY = new Usage(); // uh, who cares?
//    
    public static final Usage DISPLAY_1 = new Usage();
    
    public static final Usage INDEX = new Usage(); // 4 bytes
    
    public static final Usage NATIONAL = new Usage();  // not supported
    
    public static final Usage OBJECT_REFERENCE = new Usage(); // ???
    
    public static final Usage PACKED_DECIMAL = new Usage(); 
    
    public static final Usage POINTER = new Usage(); // ??? binary?  how many bytes?
    
    public static final Usage PROCEDURE_POINTER = new Usage(); // ???
    
    public static final Usage FUNCTION_POINTER = new Usage(); // ???
}