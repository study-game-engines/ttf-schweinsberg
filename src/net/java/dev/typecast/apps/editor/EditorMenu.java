/*
 * $Id: EditorMenu.java,v 1.1 2004-12-15 14:07:40 davidsch Exp $
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

package net.java.dev.typecast.apps.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.StreamTokenizer;
import java.io.StringReader;

import java.util.ResourceBundle;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import net.java.dev.typecast.ot.OTFontCollection;

/**
 * The application menu bar
 * @author <a href="mailto:davidsch@dev.java.net">David Schweinsberg</a>
 * @version $Id: EditorMenu.java,v 1.1 2004-12-15 14:07:40 davidsch Exp $
 */
public class EditorMenu {

    private Main _app;
    private ResourceBundle _rb;
    private Properties _properties;
    private OTFontCollection _selectedFontCollection;
    private JMenuItem _closeMenuItem;
    private String _closeMenuString;
    
    /** Creates a new instance of TypecastMenu */
    public EditorMenu(Main app, ResourceBundle rb, Properties props) {
        _app = app;
        _rb = rb;
        _properties = props;
    }
    
    public OTFontCollection getSelectedFontCollection() {
        return _selectedFontCollection;
    }
    
    public void setSelectedFontCollection(OTFontCollection fc) {
        _selectedFontCollection = fc;
        if (_selectedFontCollection != null) {
            _closeMenuItem.setText(
                    _closeMenuString +
                    " \"" +
                    _selectedFontCollection.getFileName() +
                    "\"");
            _closeMenuItem.setEnabled(true);
        } else {
            _closeMenuItem.setText(_closeMenuString);
            _closeMenuItem.setEnabled(false);
        }
    }

    private static void parseMenuString(String menuString, String[] tokens) {
        try {
            StreamTokenizer st = new StreamTokenizer(new StringReader(menuString));
            st.nextToken();
            if (st.sval != null) {
                tokens[0] = st.sval;
            }
            st.nextToken();
            if (st.sval != null) {
                tokens[1] = st.sval;
            }
            st.nextToken();
            if (st.sval != null) {
                tokens[2] = st.sval;
            }
        } catch (Exception e) {
        }
    }

    private static JMenuItem createMenuItem(
            Class menuClass,
            String name,
            String mnemonic,
            String description,
            KeyStroke accelerator,
            boolean enabled,
            ActionListener al) {
        JMenuItem menuItem = null;
        try {
            menuItem = (JMenuItem) menuClass.newInstance();
            menuItem.setText(name);
            menuItem.setToolTipText(description);
            menuItem.setMnemonic(mnemonic.length() > 0 ? mnemonic.charAt(0) : 0);
            menuItem.getAccessibleContext().setAccessibleDescription(description);
            menuItem.setEnabled(enabled);
            if (accelerator != null) {
                menuItem.setAccelerator(accelerator);
            }
            if (al != null) {
                menuItem.addActionListener(al);
            }
        } catch (Exception e) {
        }
        return menuItem;
    }

    private static JMenuItem createMenuItem(
            String menuText,
            KeyStroke accelerator,
            boolean enabled,
            ActionListener al) {
        String[] tokens = new String[3];
        parseMenuString(menuText, tokens);
        return createMenuItem(JMenuItem.class, tokens[0], tokens[1], tokens[2], accelerator, enabled, al);
    }

    private static JMenu createMenu(String menuText) {
        String[] tokens = new String[3];
        parseMenuString(menuText, tokens);
        return (JMenu) createMenuItem(JMenu.class, tokens[0], tokens[1], tokens[2], null, true, null);
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        //menuBar.add(createElementMenu());
        //menuBar.add(createPointsMenu());
        //menuBar.add(createMetricsMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.file"));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.new"),
                KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(new JSeparator());
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.open"),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        _app.openFont();
                    }
                }));
        menu.add(_closeMenuItem = createMenuItem(
                _rb.getString("Typecast.menu.file.close"),
                null,
                false,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        _app.closeFont();
                    }
                }));
        _closeMenuString = _closeMenuItem.getText();
        menu.add(new JSeparator());
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.save"),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.saveAs"),
                null,
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.export"),
                null,
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        _app.exportFont();
                    }
                }));
        menu.add(new JSeparator());
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.preferences"),
                null,
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(new JSeparator());

        // Generate a MRU list
        boolean foundMru = false;
        for (int i = 0; i < 4; i++) {
            String mru = _properties.getProperty("MRU" + i);
            if (mru != null) {
                foundMru = true;
                JMenuItem menuItem = menu.add(new JMenuItem(
//                    String.valueOf(i) + " " + mru,
                    mru,
                    KeyEvent.VK_0 + i));
                menuItem.getAccessibleContext().setAccessibleDescription(
                    "Recently used font");
                menuItem.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            _app.loadFont(e.getActionCommand());
                        }
                    }
                );
            }
        }
        if (!foundMru) {
            
            // Add a placeholder
            JMenuItem menuItem = menu.add(new JMenuItem("Recently used files"));
            menuItem.setEnabled(false);
        }

        menu.add(new JSeparator());

        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.file.exit"),
                null,
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        _app.close();
                    }
                }));

        return menu;
    }

    private JMenu createEditMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.edit"));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.undo"),
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.redo"),
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(new JSeparator());
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.cut"),
                KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.copy"),
                KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.paste"),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.clear"),
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        //menu.add(createMenuItem(
        //    _rb.getString("Typecast.menu.edit.copyWidths"),
        //    null,
        //    new ActionListener() {
        //        public void actionPerformed(ActionEvent e) {
        //        }
        //    }));
        //menu.add(createMenuItem(
        //    _rb.getString("Typecast.menu.edit.copyReference"),
        //    null,
        //    new ActionListener() {
        //        public void actionPerformed(ActionEvent e) {
        //        }
        //    }));
        //menu.add(createMenuItem(
        //    _rb.getString("Typecast.menu.edit.unlinkReference"),
        //    null,
        //    new ActionListener() {
        //        public void actionPerformed(ActionEvent e) {
        //        }
        //    }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.edit.selectAll"),
                KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        //menu.add(createMenuItem(
        //    _rb.getString("Typecast.menu.edit.duplicate"),
        //    KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK),
        //    new ActionListener() {
        //        public void actionPerformed(ActionEvent e) {
        //        }
        //    }));
        //menu.add(createMenuItem(
        //    _rb.getString("Typecast.menu.edit.clone"),
        //    null,
        //    new ActionListener() {
        //        public void actionPerformed(ActionEvent e) {
        //        }
        //    }));
        return menu;
    }
    
    private JMenu createViewMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.view"));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.preview"),
                KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //setPreview(!getPreview());
                    }
                }));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.showPoints"),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        JMenu subMenu = createMenu(_rb.getString("Typecast.menu.view.magnification"));
        menu.add(subMenu);
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.fitInWindow"),
                KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        subMenu.add(new JSeparator());
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.00625"),
                KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.01250"),
                KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.02500"),
                KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.05000"),
                KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.10000"),
                KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        subMenu.add(createMenuItem(
                _rb.getString("Typecast.menu.view.magnification.20000"),
                KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.CTRL_MASK),
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    }
                }));
        return menu;
    }
    
    private JMenu createElementMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.element"));

        JMenuItem menuItem = menu.add(new JMenuItem("New"));
        menuItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            }
        );
        return menu;
    }
    
    private JMenu createPointsMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.points"));

        JMenuItem menuItem = menu.add(new JMenuItem("New"));
        menuItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            }
        );
        return menu;
    }
    
    private JMenu createMetricsMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.metrics"));

        JMenuItem menuItem = menu.add(new JMenuItem("New"));
        menuItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            }
        );
        return menu;
    }
    
    private JMenu createHelpMenu() {
        JMenu menu = createMenu(_rb.getString("Typecast.menu.help"));
        menu.add(createMenuItem(
                _rb.getString("Typecast.menu.help.about"),
                null,
                true,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        _app.showAbout();
                    }
                }));
        return menu;
    }

    public void addMru(String mru) {
        String oldMru;
        for (int i = 0; i < 4; i++) {
            oldMru = _properties.getProperty("MRU" + i);
            if (mru != null) {
                _properties.setProperty("MRU" + i, mru);
                mru = oldMru;
            } else {
                break;
            }
        }
    }
}