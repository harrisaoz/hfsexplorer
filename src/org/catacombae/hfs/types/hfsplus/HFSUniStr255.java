/*-
 * Copyright (C) 2006 Erik Larsson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.hfs.types.hfsplus;

import org.catacombae.csjc.structelements.Dictionary;
import org.catacombae.util.Util;
import org.catacombae.hfs.UnicodeNormalizationToolkit;
import java.io.PrintStream;
import org.catacombae.csjc.StructElements;

/** This class was generated by CStructToJavaClass. */
public class HFSUniStr255 implements StructElements {
    /*
     * struct HFSUniStr255
     * size: max 512 bytes
     * description: 
     * 
     * BP  Size   Type          Identifier  Description
     * ------------------------------------------------
     * 0   2      UInt16        length                 
     * 2   2*255  UniChar[255]  unicode                
     */
    
    private final byte[] length = new byte[2];
    private final byte[] unicode;
    
    public HFSUniStr255(byte[] data, int offset) {
	System.arraycopy(data, offset+0, length, 0, 2);
	unicode = new byte[2*Util.unsign(getLength())];
	System.arraycopy(data, offset+2, unicode, 0, unicode.length);
    }
    public HFSUniStr255(String unicodeString) {
	char[] unicodeChars = unicodeString.toCharArray();
	if(unicodeChars.length > 255)
	    throw new RuntimeException("String too large.");
	System.arraycopy(Util.toByteArrayBE((short)unicodeChars.length), 0, length, 0, 2);
	unicode = Util.readByteArrayBE(unicodeChars);
    }
    
    public int length() { return 2+unicode.length; }
    
    public short getLength() { return Util.readShortBE(length); }
    
    /**
     * Returns the raw bytes constituting this UTF-16BE string.
     * @return the raw bytes constituting this UTF-16BE string.
     */
    public byte[] getRawUnicode() { return Util.createCopy(unicode); }
    
    /** This is a char for char representation of what data is in the actual file system. The string will
	(if the filesystem is valid) be in decomposed form, as the HFS+ volume format requires. */
    public char[] getUnicode() { return Util.readCharArrayBE(unicode); }
    
    /** A simple conversion of the decomposed string from getUnicode() into a String object. */
    public String getUnicodeAsDecomposedString() { return new String(getUnicode()); }
    
    /** Returns a composed string that will differ from the decomposed string whenever the decomposed
	string contains decomposed characters. In these cases it will be shorter than the decomposed string */
    public String getUnicodeAsComposedString() {
	return UnicodeNormalizationToolkit.getDefaultInstance().compose(getUnicodeAsDecomposedString());
    }


    public byte[] getBytes() {
	byte[] result = new byte[length()];
	System.arraycopy(length, 0, result, 0, 2);
	System.arraycopy(unicode, 0, result, 2, unicode.length);
	return result;
    }
    
    public void printFields(PrintStream ps, String prefix) {
	ps.println(prefix + " length: " + Util.unsign(getLength()));
	ps.println(prefix + " unicode (decomposed): \"" + getUnicodeAsDecomposedString() + "\"");
	ps.println(prefix + "           (composed): \"" + getUnicodeAsComposedString() + "\"");
    }
    
    public void print(PrintStream ps, String prefix) {
	ps.println(prefix + "HFSUniStr255:");
	printFields(ps, prefix);
    }

    @Override
    public String toString() {
	return getUnicodeAsComposedString();
    }

    @Override
    public Dictionary getStructElements() {
        DictionaryBuilder db = new DictionaryBuilder(HFSUniStr255.class.getSimpleName());
        
        db.addUIntBE("length", length);
        db.addEncodedString("unicode", unicode, "UTF-16BE");
        
        return db.getResult();
    }
}
