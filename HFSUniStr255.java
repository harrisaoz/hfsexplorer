import java.io.PrintStream;

/** This class was generated by CStructToJavaClass. */
public class HFSUniStr255 {
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
	unicode = new byte[2*Util2.unsign(getLength())];
	System.arraycopy(data, offset+2, unicode, 0, unicode.length);
    }
    
    public int length() { return 2+unicode.length; }
    
    public short getLength() { return Util.readShortBE(length); }
    public char[] getUnicode() { return Util2.readCharArrayBE(unicode); }
    public String getUnicodeAsString() { return new String(getUnicode()); }

    public void printFields(PrintStream ps, String prefix) {
	ps.println(prefix + " length: " + Util2.unsign(getLength()));
	ps.println(prefix + " unicode: \"" + getUnicodeAsString() + "\"");
    }
    
    public void print(PrintStream ps, String prefix) {
	ps.println(prefix + "HFSUniStr255:");
	printFields(ps, prefix);
    }
}
