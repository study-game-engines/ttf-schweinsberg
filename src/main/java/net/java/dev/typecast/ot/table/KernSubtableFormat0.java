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

/**
 *
 * @author <a href="mailto:david.schweinsberg@gmail.com">David Schweinsberg</a>
 */
public class KernSubtableFormat0 extends KernSubtable {
    
    private int nPairs;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;
    private KerningPair[] kerningPairs;

    /** Creates new KernSubtableFormat0 */
    KernSubtableFormat0(DataInput di) throws IOException {
        nPairs = di.readUnsignedShort();
        searchRange = di.readUnsignedShort();
        entrySelector = di.readUnsignedShort();
        rangeShift = di.readUnsignedShort();
        kerningPairs = new KerningPair[nPairs];
        for (int i = 0; i < nPairs; i++) {
            kerningPairs[i] = new KerningPair(di);
        }
    }

    public int getKerningPairCount() {
        return nPairs;
    }

    public KerningPair getKerningPair(int i) {
        return kerningPairs[i];
    }

}
