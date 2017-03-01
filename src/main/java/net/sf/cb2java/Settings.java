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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.sf.cb2java.types.Numeric;
import net.sf.cb2java.types.Numeric.Position;

public interface Settings {
	public static Settings DEFAULT = new Default();

	String getEncoding();

	Values getValues();

	boolean getLittleEndian();

	String getFloatConversion();

	Numeric.Position getSignPosition();

	int getColumnStart();

	int getColumnEnd();

	public static class Default implements Settings {
		private static final String DEFAULT_ENCODING;
		private static final boolean DEFAULT_LITTLE_ENDIAN;
		private static final String DEFAULT_FLOAT_CONVERSION;
		private static final Numeric.Position DEFAULT_SIGN_POSITION;
		private static final Values DEFAULT_VALUES = new Values();
		private static final int DEFAULT_COLUMN_START;
		private static final int DEFAULT_COLUMN_END;

		static {
			Properties props = new Properties();

			try (InputStream is = Settings.class.getResourceAsStream("/copybook.props")) {
				if (is == null) {
					System.out.println("Could not load 'copybook.props' file, reverting to defaults.");
				} else {
					props.load(is);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not load 'copybook.props' file, reverting to defaults.");
			}

			DEFAULT_ENCODING = getSetting("encoding", System.getProperty("file.encoding"), props);
			DEFAULT_LITTLE_ENDIAN = "false".equals(getSetting("little-endian", "false", props));
			DEFAULT_FLOAT_CONVERSION = getSetting("float-conversion", "net.sf.cb2java.copybook.floating.IEEE754",
					props);
			DEFAULT_SIGN_POSITION = "leading".equalsIgnoreCase(getSetting("default-sign-position", "trailing", props))
					? Numeric.LEADING : Numeric.TRAILING;
			DEFAULT_COLUMN_START = Integer.parseInt(getSetting("column.start", "6", props));
			DEFAULT_COLUMN_END = Integer.parseInt(getSetting("column.end", "72", props));
		}

		private static String getSetting(String name, String defaultValue, Properties props) {
			String result = defaultValue;
			try {
				result = System.getProperty("cb2java." + name, result);
				result = props.getProperty(name, result);
			} catch (RuntimeException e) {
			}
			return result;
		}

		public String getEncoding() {
			return DEFAULT_ENCODING;
		}

		public String getFloatConversion() {
			return DEFAULT_FLOAT_CONVERSION;
		}

		public boolean getLittleEndian() {
			return DEFAULT_LITTLE_ENDIAN;
		}

		public Values getValues() {
			return DEFAULT_VALUES;
		}

		public Position getSignPosition() {
			return DEFAULT_SIGN_POSITION;
		}

		public int getColumnStart() {
			return DEFAULT_COLUMN_START;
		}

		public int getColumnEnd() {
			return DEFAULT_COLUMN_END;
		}
	}
}
