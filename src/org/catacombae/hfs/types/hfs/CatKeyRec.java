/*-
 * Copyright (C) 2008 Erik Larsson
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

package org.catacombae.hfs.types.hfs;

import java.io.PrintStream;
import org.catacombae.csjc.DynamicStruct;
import org.catacombae.csjc.StructElements;
import org.catacombae.csjc.structelements.ASCIIStringField;
import org.catacombae.csjc.structelements.Dictionary;
import org.catacombae.util.Util;

/** This class was generated by CStructToJavaClass. */
public class CatKeyRec implements DynamicStruct, StructElements {
    /*
     * struct CatKeyRec
     * size: 38 bytes
     * description: 
     * 
     * BP  Size  Type      Identifier  Description                  
     * -------------------------------------------------------------
     * 0   1     SInt8     ckrKeyLen   key length (SignedByte)      
     * 1   1     SInt8     ckrResrv1   reserved (SignedByte)        
     * 2   4     SInt32    ckrParID    parent directory ID (LongInt)
     * 6   1     UInt8     ckrCNameLen length of catalog node name (part of Str31)
     * 7   1*31  Char[31]  ckrCName    catalog node name (part of Str31)
     */
    
    private static final int MAX_STRUCTSIZE = 38;
    
    private final byte[] ckrKeyLen = new byte[1];
    private final byte[] ckrResrv1 = new byte[1];
    private final byte[] ckrParID = new byte[4];
    private final byte[] ckrCNameLen; // = new byte[1];
    private final byte[] ckrCName;// = new byte[1*31];
    private final byte[] ckrPad;

    public CatKeyRec(byte[] data, int offset) {
        System.arraycopy(data, offset + 0, ckrKeyLen, 0, 1);
        System.arraycopy(data, offset + 1, ckrResrv1, 0, 1);
        System.arraycopy(data, offset + 2, ckrParID, 0, 4);

        int structSize = 1 + Util.unsign(getCkrKeyLen());
        if(structSize >= 6 && structSize <= MAX_STRUCTSIZE) {
            if(structSize >= 7) {
                ckrCNameLen = new byte[1];
                System.arraycopy(data, offset + 6, ckrCNameLen, 0, 1);

                final int trailingBytes = structSize - 7;

                final int cNameLen = Util.unsign(getCkrCNameLen());
                if(cNameLen > trailingBytes)
                    throw new RuntimeException("Malformed CatKeyRec: ckrCNameLen=" +
                            cNameLen + " > trailingBytes=" + trailingBytes);
                
                ckrCName = new byte[cNameLen];
                System.arraycopy(data, offset+7, ckrCName, 0, ckrCName.length);

                final int remainingBytes = trailingBytes-cNameLen;
                ckrPad = new byte[remainingBytes];
                System.arraycopy(data, offset+7+cNameLen, ckrPad, 0, remainingBytes);
            }
            else {
                ckrCNameLen = new byte[0];
                ckrCName = new byte[0];
                ckrPad = new byte[0];
            }
        }
        else
            throw new RuntimeException("Incorrect value for ckrKeyLen: " +
                    Util.unsign(getCkrKeyLen()));
    }

    /**
     * Builds a CatKeyRec from a specified nodeID and a string.
     * @param parID
     * @param cName
     */
    public CatKeyRec(int parID, byte[] cName) {
        if(cName.length < 0 || cName.length > 32)
            throw new IllegalArgumentException("String too large for a catalog" +
                    " file key! (size: " + cName.length + ")");
        byte[] parIDBytes = Util.toByteArrayBE(parID);
        if(parIDBytes.length != 4)
            throw new RuntimeException("Internal error! int array not 4 bytes but " +
                    parIDBytes.length);
        this.ckrCName = new byte[cName.length];
        this.ckrPad = new byte[0];

        this.ckrKeyLen[0] = (byte)(1 + 4 + cName.length); // length of the rest of the key
        this.ckrResrv1[0] = 0; // Reserved
        System.arraycopy(parIDBytes, 0, this.ckrParID, 0, this.ckrParID.length);
        if(cName.length > 0) {
            this.ckrCNameLen = new byte[1];
            this.ckrCNameLen[0] = (byte)cName.length;
            System.arraycopy(cName, 0, this.ckrCName, 0, this.ckrCName.length);
        }
        else {
            this.ckrCNameLen = new byte[0];
        }
    }

    //public int length() { return occupiedSize(); }
    
    public byte getCkrKeyLen() { return Util.readByteBE(ckrKeyLen); }
    public byte getCkrResrv1() { return Util.readByteBE(ckrResrv1); }
    public int getCkrParID() { return Util.readIntBE(ckrParID); }
    public byte getCkrCNameLen() { return Util.readByteBE(ckrCNameLen); }
    public byte[] getCkrCName() { return Util.createCopy(ckrCName); }
    public byte[] getCkrPad() { return Util.createCopy(ckrPad); }
    
    public void printFields(PrintStream ps, String prefix) {
        ps.println(prefix + " ckrKeyLen: " + getCkrKeyLen());
        ps.println(prefix + " ckrResrv1: " + getCkrResrv1());
        ps.println(prefix + " ckrParID: " + getCkrParID());
        ps.println(prefix + " ckrCNameLen: " + getCkrCNameLen());
        ps.println(prefix + " ckrCName: \"" + Util.toASCIIString(getCkrCName()) + "\" (0x" + Util.byteArrayToHexString(getCkrCName()) + ")");
        ps.println(prefix + " ckrPad: byte[" + ckrPad.length + "]");
    }

    public void print(PrintStream ps, String prefix) {
        ps.println(prefix + "CatKeyRec:");
        printFields(ps, prefix);
    }
    
    public byte[] getBytes() {
        byte[] result = new byte[occupiedSize()];
        int offset = 0;

        System.arraycopy(ckrKeyLen, 0, result, offset, ckrKeyLen.length); offset += ckrKeyLen.length;
        System.arraycopy(ckrResrv1, 0, result, offset, ckrResrv1.length); offset += ckrResrv1.length;
        System.arraycopy(ckrParID, 0, result, offset, ckrParID.length); offset += ckrParID.length;
        System.arraycopy(ckrCNameLen, 0, result, offset, ckrCNameLen.length); offset += ckrCNameLen.length;
        System.arraycopy(ckrCName, 0, result, offset, ckrCName.length); offset += ckrCName.length;
        System.arraycopy(ckrPad, 0, result, offset, ckrPad.length); offset += ckrPad.length;
        return result;
    }

    public int maxSize() {
        return MAX_STRUCTSIZE;
    }

    public int occupiedSize() {
        return 1+Util.unsign(getCkrKeyLen());
    }

    @Override
    public Dictionary getStructElements() {
        DictionaryBuilder db = new DictionaryBuilder(CatKeyRec.class.getSimpleName());
        
        db.addUIntBE("ckrKeyLen", ckrKeyLen, "Key length", "bytes");
        db.addUIntBE("ckrResrv1", ckrResrv1, "Reserved", HEXADECIMAL);
        db.addUIntBE("ckrParID", ckrParID, "Parent ID");
        db.addUIntBE("ckrCNameLen", ckrCNameLen, "Length of record name", "bytes");
        db.add("ckrCName", new ASCIIStringField(ckrCName), "Record name");
        
        return db.getResult();
    }
}
