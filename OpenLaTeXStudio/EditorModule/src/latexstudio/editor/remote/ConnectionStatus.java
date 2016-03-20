/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 * 
 * @see http://wiki.netbeans.org/BookNBPlatformCookbookCH0211
 * @author Geraldo
 */
public class ConnectionStatus {
    private int connectionState = STATE_NOTCONN;

    private JLabel label;

    public static final int STATE_NOTCONN    = 0;
    public static final int STATE_STARTING   = 1;
    public static final int STATE_STOPPING   = 2;
    public static final int STATE_CONNECTED  = 3;

    private static final String [] STATE_NAMES_KEYS =
            { "STATE_NOTCONN", "STATE_STARTING"
             , "STATE_STOPPING", "STATE_CONNECTED"   };
    private static String [] STATE_NAMES ;

    private static final ImageIcon imgConnected;
    private static final ImageIcon imgStartStop;
    private static final ImageIcon imgNotconn;

    // property change support    
    static {
        imgConnected = new ImageIcon( 
                         ImageUtilities.loadImage(
                        "latexstudio/editor/resources/icons/dbx-connected.png") );
        imgStartStop = new  ImageIcon( 
                         ImageUtilities.loadImage(
                         "latexstudio/editor/resources/icons/dbx-start.png") );
        imgNotconn   = new ImageIcon( 
                         ImageUtilities.loadImage(
                         "latexstudio/editor/resources/icons/dbx-notconn.png") );
        STATE_NAMES = new String [STATE_NAMES_KEYS.length];
        for (int i = 0; i < STATE_NAMES_KEYS.length; i++) {
            STATE_NAMES [i] = NbBundle.getMessage(
                                    ConnectionStatus.class
                                  , STATE_NAMES_KEYS[i] );
        } // for i STATE_NAMES_KEYS
    }

    private static final ConnectionStatus instance = 
                           new ConnectionStatus();

    public static ConnectionStatus getInstance() {
        return instance;
    }

    public String getStateName(int state) {
        return STATE_NAMES [state];
    }
    
    protected ConnectionStatus() {
        this.label = new JLabel(imgNotconn);
        this.connectionState = STATE_NOTCONN;
        this.label.setToolTipText( 
                  NbBundle.getMessage(ConnectionStatus.class
                   , "DB_CONNECTION_STATUS"
                   , getStateName(this.connectionState)) );
    }

    JComponent getComponent() {
        return this.label;
    }

    public int getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(int connectionState) {
        // check parameter ...
        this.connectionState = connectionState;
        // propertyChangeSupport ...
        Icon icon = imgNotconn;
        switch (this.connectionState) {
            case STATE_CONNECTED : icon = imgConnected;
                                  break;                                  
            case STATE_STARTING : 
            case STATE_STOPPING : icon = imgStartStop;
                                  break;
            default : icon = imgNotconn;
                                  break;
        } // switch this.connectionState
        this.label.setIcon(icon);
        this.label.setToolTipText( 
                   NbBundle.getMessage(ConnectionStatus.class
                     , "DB_CONNECTION_STATUS"
                     , getStateName(this.connectionState)) );
    }
}
