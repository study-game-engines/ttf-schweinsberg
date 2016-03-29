/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- * 
 * This software is published under the terms of the Apache Software License * 
 * version 1.1, a copy of which has been included with this distribution in  * 
 * the LICENSE file.                                                         * 
 *****************************************************************************/

package net.java.dev.typecast.ot.table;

import java.io.DataInput;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:david.schweinsberg@gmail.com">David Schweinsberg</a>
 */
public class LocaTable implements Table {

    private DirectoryEntry _de;
    private int[] _offsets = null;
    private short _factor = 0;

    static final Logger logger = LoggerFactory.getLogger(LocaTable.class);

    protected LocaTable(
            DirectoryEntry de,
            DataInput di,
            HeadTable head,
            MaxpTable maxp) throws IOException {
        _de = (DirectoryEntry) de.clone();
        _offsets = new int[maxp.getNumGlyphs() + 1];
        boolean shortEntries = head.getIndexToLocFormat() == 0;
        if (shortEntries) {
            _factor = 2;
            for (int i = 0; i <= maxp.getNumGlyphs(); i++) {
                _offsets[i] = di.readUnsignedShort();
            }
        } else {
            _factor = 1;
            for (int i = 0; i <= maxp.getNumGlyphs(); i++) {
                _offsets[i] = di.readInt();
            }
        }
        
        // Check the validity of the offsets
        int lastOffset = 0;
        int index = 0;
        for (int offset : _offsets) {
            if (offset < lastOffset) {
                logger.error("Offset at index {} is bad ({} < {})", index, offset, lastOffset);
            }
            lastOffset = offset;
            ++index;
        }
    }

    public int getOffset(int i) {
        if (_offsets == null) {
            return 0;
        }
        return _offsets[i] * _factor;
    }

    @Override
    public int getType() {
        return loca;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'loca' Table - Index To Location Table\n--------------------------------------\n")
            .append("Size = ").append(_de.getLength()).append(" bytes, ")
            .append(_offsets.length).append(" entries\n");
        for (int i = 0; i < _offsets.length; i++) {
            sb.append("        Idx ").append(i)
                .append(" -> glyfOff 0x").append(getOffset(i)).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Get a directory entry for this table.  This uniquely identifies the
     * table in collections where there may be more than one instance of a
     * particular table.
     * @return A directory entry
     */
    @Override
    public DirectoryEntry getDirectoryEntry() {
        return _de;
    }
}
