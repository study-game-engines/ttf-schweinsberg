/*
 * Typecast - The Font Development Environment
 *
 * Copyright (c) 2004-2016 David Schweinsberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.java.dev.typecast.ot.table;

import java.io.DataInput;
import java.io.IOException;

/**
 * When we encounter a cmap format we don't understand, we can use this class
 * to hold the bare minimum information about it.
 * @author <a href="mailto:david.schweinsberg@gmail.com">David Schweinsberg</a>
 */
public class CmapFormatUnknown extends CmapFormat {
    
    /** Creates a new instance of CmapFormatUnknown
     * @param format
     * @param di
     * @throws java.io.IOException */
    protected CmapFormatUnknown(int format, DataInput di) throws IOException {
        super(di);
        _format = format;
        
        // We don't know how to handle this data, so we'll just skip over it
        di.skipBytes(_length - 6);
    }

    @Override
    public int getRangeCount() {
        return 0;
    }
    
    @Override
    public Range getRange(int index) throws ArrayIndexOutOfBoundsException {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public int mapCharCode(int charCode) {
        return 0;
    }
}
