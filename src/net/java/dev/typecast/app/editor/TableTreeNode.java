/*
 * $Id: TableTreeNode.java,v 1.1 2007-01-24 09:36:58 davidsch Exp $
 *
 * Typecast - The Font Development Environment
 *
 * Copyright (c) 2004 David Schweinsberg
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

package net.java.dev.typecast.app.editor;

/**
 *
 * @author <a href="mailto:david.schweinsberg@gmail.com">David Schweinsberg</a>
 * @version $Id: TableTreeNode.java,v 1.1 2007-01-24 09:36:58 davidsch Exp $
 */
public class TableTreeNode extends javax.swing.tree.DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    private String _nodeName;
    private int _index;
    
    /** Creates new TableTreeNode */
    public TableTreeNode(String nodeName, Object userObject, int index) {
        super(userObject);
        _nodeName = nodeName;
        _index = index;
    }

    public TableTreeNode(String nodeName, Object userObject) {
        this(nodeName, userObject, -1);
    }

    public int getIndex() {
        return _index;
    }
    
    public String toString() {
        return _nodeName;
    }
}
