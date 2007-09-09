/*-
 * Copyright (C) 2006-2007 Erik Larsson
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

package org.catacombae.hfsexplorer.partitioning;

import org.catacombae.hfsexplorer.LowLevelFile;
import org.catacombae.hfsexplorer.Util;
import org.catacombae.hfsexplorer.Util2;
import java.io.PrintStream;

/** This class was generated by CStructToJavaClass. (Modified afterwards) */
public class DriverDescriptorRecord {
    public static final short DDR_SIGNATURE = 0x4552;
    /*
     * struct DriverDescriptorRecord
     * size: 269 bytes
     * description: 
     * 
     * BP   Size  Type                       Identifier   Description                          
     * ----------------------------------------------------------------------------------------
     * 0    2     UInt16                     sbSig        {device signature}                   
     * 2    2     UInt16                     sbBlkSize    {block size of the device}           
     * 4    4     UInt32                     sbBlkCount   {number of blocks on the device}     
     * 8    2     UInt16                     reserved1    {reserved}                           
     * 10   2     UInt16                     reserved2    {reserved}                           
     * 12   4     UInt32                     reserved3    {reserved}                           
     * 16   2     UInt16                     sbDrvrCount  {number of driver descriptor entries}
     * 18   8     DriverDescriptorEntry      firstEntry   {first driver descriptor entry}      
     * 26   8*30  DriverDescriptorEntry[30]  additional   {additional drivers, if any}         
     * 266  3     byte[3]                    ddPad        {reserved}                           
     *
     */
    
    private final byte[] sbSig = new byte[2];
    private final byte[] sbBlkSize = new byte[2];
    private final byte[] sbBlkCount = new byte[4];
    private final byte[] reserved1 = new byte[2];
    private final byte[] reserved2 = new byte[2];
    private final byte[] reserved3 = new byte[4];
    private final byte[] sbDrvrCount = new byte[2];
    private final DriverDescriptorEntry[] entries;
    private final byte[] ddPad;
    
    public DriverDescriptorRecord(LowLevelFile llf, long offset) {
	this(readData(llf, offset), 0);
    }
    public DriverDescriptorRecord(byte[] data, int offset) {
	System.arraycopy(data, offset+0, sbSig, 0, 2);
	System.arraycopy(data, offset+2, sbBlkSize, 0, 2);
	System.arraycopy(data, offset+4, sbBlkCount, 0, 4);
	System.arraycopy(data, offset+8, reserved1, 0, 2);
	System.arraycopy(data, offset+10, reserved2, 0, 2);
	System.arraycopy(data, offset+12, reserved3, 0, 4);
	System.arraycopy(data, offset+16, sbDrvrCount, 0, 2);
	int numEntries = Util.unsign(getSbDrvrCount());
	if(numEntries > 31) // BUGFIX: Stucture size does not allow for more than 31 values
	    numEntries = 31;
	entries = new DriverDescriptorEntry[numEntries];
	int i;
	for(i = 0; i < entries.length; ++i)
	    entries[i] = new DriverDescriptorEntry(data, offset+18 + DriverDescriptorEntry.length()*i);
	int padOffset = offset+18 + DriverDescriptorEntry.length()*i;
	ddPad = new byte[length()-padOffset];
	System.arraycopy(data, padOffset, ddPad, 0, ddPad.length);
    }
    
    private static byte[] readData(LowLevelFile llf, long offset) {
	byte[] data = new byte[length()];
	llf.seek(offset);
	if(llf.read(data) != data.length)
	    throw new RuntimeException("Could not read enough bytes from LowLevelFile!");
	return data;
    }
    
    public static int length() { return 269; }
    
    /** Device signature. (Should be "ER"...) */
    public short getSbSig() { return Util.readShortBE(sbSig); }
    /** Block size of the device. */
    public short getSbBlkSize() { return Util.readShortBE(sbBlkSize); }
    /** Number of blocks on the device. */
    public int getSbBlkCount() { return Util.readIntBE(sbBlkCount); }
    /** Reserved. */
    public short getReserved1() { return Util.readShortBE(reserved1); }
    /** Reserved. */
    public short getReserved2() { return Util.readShortBE(reserved2); }
    /** Reserved. */
    public int getReserved3() { return Util.readIntBE(reserved3); }
    /** Number of driver descriptor entries. Won't be more than 31 in a valid structure. */
    public short getSbDrvrCount() { return Util.readShortBE(sbDrvrCount); }
    public DriverDescriptorEntry[] getDriverDecriptorEntries() {
	DriverDescriptorEntry[] result = new DriverDescriptorEntry[entries.length];
	System.arraycopy(entries, 0, result, 0, entries.length);
	return result;
    }
    /** Reserved. */
    public byte[] getDdPad() { return Util.createCopy(ddPad); }

    /** Returns a String representation of the device signature. */
    public String getSbSigAsString() { return Util2.toASCIIString(sbSig); }
    
    public boolean isValid() {
	int driverCount = Util.unsign(getSbDrvrCount());
	return getSbSig() == DDR_SIGNATURE && driverCount <= 31 && entries.length == driverCount;
    }
    
    public byte[] getData() {
	byte[] result = new byte[length()];
	int offset = 0;
	System.arraycopy(sbSig, 0, result, offset, sbSig.length); offset += sbSig.length;
	System.arraycopy(sbBlkSize, 0, result, offset, sbBlkSize.length); offset += sbBlkSize.length;
	System.arraycopy(sbBlkCount, 0, result, offset, sbBlkCount.length); offset += sbBlkCount.length;
	System.arraycopy(reserved1, 0, result, offset, reserved1.length); offset += reserved1.length;
	System.arraycopy(reserved2, 0, result, offset, reserved2.length); offset += reserved2.length;
	System.arraycopy(reserved3, 0, result, offset, reserved3.length); offset += reserved3.length;
	System.arraycopy(sbDrvrCount, 0, result, offset, sbDrvrCount.length); offset += sbDrvrCount.length;
	for(DriverDescriptorEntry dde : entries) {
	    byte[] tmp = dde.getData();
	    System.arraycopy(tmp, 0, result, offset, tmp.length); offset += tmp.length;
	}
	System.arraycopy(ddPad, 0, result, offset, ddPad.length); offset += ddPad.length;
	//System.arraycopy(, 0, result, offset, .length); offset += .length;
	if(offset != length())
	    throw new RuntimeException("Internal miscalculation...");
	else
	    return result;
    }
    
    public void printFields(PrintStream ps, String prefix) {
	ps.println(prefix + " sbSig: \"" + getSbSigAsString() + "\"");
	ps.println(prefix + " sbBlkSize: " + getSbBlkSize());
	ps.println(prefix + " sbBlkCount: " + getSbBlkCount());
	ps.println(prefix + " reserved1: " + getReserved1());
	ps.println(prefix + " reserved2: " + getReserved2());
	ps.println(prefix + " reserved3: " + getReserved3());
	ps.println(prefix + " sbDrvrCount: " + getSbDrvrCount());
	ps.println(prefix + " entries (" + entries.length + " elements):");
	for(int i = 0; i < entries.length; ++i) {
	    ps.println(prefix + "  entries[" + i + "]: ");
	    entries[i].print(ps, prefix + "   ");
	}
	if(entries.length == 0)
	    ps.println(prefix + "  <empty>");
	ps.println(prefix + " ddPad: {" + ddPad.length + " bytes...}");
    }
    
    public void print(PrintStream ps, String prefix) {
	ps.println(prefix + "DriverDescriptorRecord:");
	printFields(ps, prefix);
    }
}
