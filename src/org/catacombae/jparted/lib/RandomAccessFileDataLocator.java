/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.catacombae.jparted.lib;

import java.io.File;
import org.catacombae.io.ReadableConcatenatedStream;
import org.catacombae.io.ReadableRandomAccessStream;
import org.catacombae.io.ReadableFileStream;
import org.catacombae.io.ConcatenatedStream;
import org.catacombae.io.RandomAccessStream;
import org.catacombae.io.FileStream;

/**
 *
 * @author erik
 */
public class RandomAccessFileDataLocator extends DataLocator {
    private final File file;
    private final Long pos, len;
    
    public RandomAccessFileDataLocator(String pPath) {
        this(new File(pPath));
    }
    
    public RandomAccessFileDataLocator(String pPath, long pPos, long pLen) {
        this(new File(pPath), pPos, pLen);
    }
    public RandomAccessFileDataLocator(File pFile) {
        this(pFile, null, null);
    }
    
    public RandomAccessFileDataLocator(File pFile, long pPos, long pLen) {
        this(pFile, new Long(pPos), new Long(pLen));
    }
    
    private RandomAccessFileDataLocator(File pFile, Long pPos, Long pLen) {
        if(!pFile.canRead())
            throw new RuntimeException("Can not read from file!");

        this.file = pFile;
        this.pos = pPos;
        this.len = pLen;
    }
    
    @Override
    public ReadableRandomAccessStream createReadOnlyFile() {
        ReadableRandomAccessStream llf = new ReadableFileStream(file);
        if(pos != null && len != null)
            return new ReadableConcatenatedStream(llf, pos, len);
        else
            return llf;
    }

    @Override
    public RandomAccessStream createReadWriteFile() {
        RandomAccessStream wllf = new FileStream(file);
        if(pos != null && len != null)
            return new ConcatenatedStream(wllf, pos, len);
        else
            return wllf;
    }
    
    @Override
    public boolean isWritable() {
        return true;
    }    
}