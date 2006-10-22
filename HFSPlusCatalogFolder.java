import java.io.PrintStream;

/** This class was generated by CStructToJavaClass. */
public class HFSPlusCatalogFolder {
    /*
     * struct HFSPlusCatalogFolder
     * size: 88 bytes
     * description: 
     * 
     * BP  Size  Type                Identifier        Description
     * -----------------------------------------------------------
     * 0   2     SInt16              recordType                   
     * 2   2     UInt16              flags                        
     * 4   4     UInt32              valence                      
     * 8   4     HFSCatalogNodeID    folderID                     
     * 12  4     UInt32              createDate                   
     * 16  4     UInt32              contentModDate               
     * 20  4     UInt32              attributeModDate             
     * 24  4     UInt32              accessDate                   
     * 28  4     UInt32              backupDate                   
     * 32  16    HFSPlusBSDInfo      permissions                  
     * 48  16    FolderInfo          userInfo                     
     * 64  16    ExtendedFolderInfo  finderInfo                   
     * 80  4     UInt32              textEncoding                 
     * 84  4     UInt32              reserved                     
     */
    
    private final byte[] recordType = new byte[2];
    private final byte[] flags = new byte[2];
    private final byte[] valence = new byte[4];
    private final HFSCatalogNodeID folderID;
    private final byte[] createDate = new byte[4];
    private final byte[] contentModDate = new byte[4];
    private final byte[] attributeModDate = new byte[4];
    private final byte[] accessDate = new byte[4];
    private final byte[] backupDate = new byte[4];
    private final HFSPlusBSDInfo permissions;
    private final FolderInfo userInfo;
    private final ExtendedFolderInfo finderInfo;
    private final byte[] textEncoding = new byte[4];
    private final byte[] reserved = new byte[4];
    
    public HFSPlusCatalogFolder(byte[] data, int offset) {
	System.arraycopy(data, offset+0, recordType, 0, 2);
	System.arraycopy(data, offset+2, flags, 0, 2);
	System.arraycopy(data, offset+4, valence, 0, 4);
	folderID = new HFSCatalogNodeID(data, offset+8);
	System.arraycopy(data, offset+12, createDate, 0, 4);
	System.arraycopy(data, offset+16, contentModDate, 0, 4);
	System.arraycopy(data, offset+20, attributeModDate, 0, 4);
	System.arraycopy(data, offset+24, accessDate, 0, 4);
	System.arraycopy(data, offset+28, backupDate, 0, 4);
	permissions = new HFSPlusBSDInfo(data, offset+32);
	userInfo = new FolderInfo(data, offset+48);
	finderInfo = new ExtendedFolderInfo(data, offset+64);
	System.arraycopy(data, offset+80, textEncoding, 0, 4);
	System.arraycopy(data, offset+84, reserved, 0, 4);
    }
    
    public static int length() { return 88; }
    
    public short getRecordType() { return Util.readShortBE(recordType); }
    public short getFlags() { return Util.readShortBE(flags); }
    public int getValence() { return Util.readIntBE(valence); }
    public HFSCatalogNodeID getFolderID() { return folderID; }
    public int getCreateDate() { return Util.readIntBE(createDate); }
    public int getContentModDate() { return Util.readIntBE(contentModDate); }
    public int getAttributeModDate() { return Util.readIntBE(attributeModDate); }
    public int getAccessDate() { return Util.readIntBE(accessDate); }
    public int getBackupDate() { return Util.readIntBE(backupDate); }
    public HFSPlusBSDInfo getPermissions() { return permissions; }
    public FolderInfo getUserInfo() { return userInfo; }
    public ExtendedFolderInfo getFinderInfo() { return finderInfo; }
    public int getTextEncoding() { return Util.readIntBE(textEncoding); }
    public int getReserved() { return Util.readIntBE(reserved); }
    
    public void printFields(PrintStream ps, String prefix) {
	ps.println(prefix + " recordType: " + getRecordType());
	ps.println(prefix + " flags: " + getFlags());
	ps.println(prefix + " valence: " + getValence());
	ps.println(prefix + " folderID: ");
	getFolderID().print(ps, prefix+"  ");
	ps.println(prefix + " createDate: " + getCreateDate());
	ps.println(prefix + " contentModDate: " + getContentModDate());
	ps.println(prefix + " attributeModDate: " + getAttributeModDate());
	ps.println(prefix + " accessDate: " + getAccessDate());
	ps.println(prefix + " backupDate: " + getBackupDate());
	ps.println(prefix + " permissions: ");
	getPermissions().print(ps, prefix+"  ");
	ps.println(prefix + " userInfo: ");
	getUserInfo().print(ps, prefix+"  ");
	ps.println(prefix + " finderInfo: ");
	getFinderInfo().print(ps, prefix+"  ");
	ps.println(prefix + " textEncoding: " + getTextEncoding());
	ps.println(prefix + " reserved: " + getReserved());
    }
    
    public void print(PrintStream ps, String prefix) {
	ps.println(prefix + "HFSPlusCatalogFolder:");
	printFields(ps, prefix);
    }
}
