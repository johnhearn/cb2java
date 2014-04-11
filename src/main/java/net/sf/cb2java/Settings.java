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
package net.sf.cb2java;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cb2java.types.Numeric;
import net.sf.cb2java.types.Numeric.Position;

public interface Settings
{
    public static Settings DEFAULT = new Default();
    
    
    String getEncoding();
    
    Values getValues();
    
    boolean getLittleEndian();
    
    String getFloatConversion();
    
    Numeric.Position getSignPosition();
    
    public static class Default implements Settings
    {
    	private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
    	
        private static final String DEFAULT_ENCODING;
        private static final boolean DEFAULT_LITTLE_ENDIAN;
        private static final String DEFAULT_FLOAT_CONVERSION;
        private static final Numeric.Position DEFAULT_DEFAULT_SIGN_POSITION;
        private static final Values DEFAULT_VALUES = new Values();
        
        static {
            Properties props = new Properties();
            
            try {
                props.load(Settings.class.getResourceAsStream("/copybook.props"));
            } catch (Exception e) {
            	LOGGER.warn("Could not load 'copybook.props' file, reverting to defaults.", e.getMessage());
            }  
            
            DEFAULT_ENCODING = getSetting("encoding", System.getProperty("file.encoding"), props);
            DEFAULT_LITTLE_ENDIAN = "false".equals(getSetting("little-endian", "false", props));
            DEFAULT_FLOAT_CONVERSION = getSetting("float-conversion", 
                "net.sf.cb2java.copybook.floating.IEEE754", props);
            DEFAULT_DEFAULT_SIGN_POSITION = "leading".equalsIgnoreCase(
                getSetting("default-sign-position", "trailing", props)) ? Numeric.LEADING : Numeric.TRAILING;
        }
        
        private static String getSetting(String name, String defaultValue, Properties props)
        {
            try {
                String value = System.getProperty("cb2java." + name, defaultValue);
                value = props.getProperty(name, value);
                return value;
            } catch (Exception e) {
                return defaultValue;
            }
        }
        
        public String getEncoding()
        {
            return DEFAULT_ENCODING;
        }

        public String getFloatConversion()
        {
            return DEFAULT_FLOAT_CONVERSION;
        }

        public boolean getLittleEndian()
        {
            return DEFAULT_LITTLE_ENDIAN;
        }

        public Values getValues()
        {
            return DEFAULT_VALUES;
        }

        public Position getSignPosition()
        {
            return DEFAULT_DEFAULT_SIGN_POSITION;
        }
    }
}
