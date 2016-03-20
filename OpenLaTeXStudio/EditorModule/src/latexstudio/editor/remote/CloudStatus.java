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

/**
 * Class responsible for displaying the Cloud Support Status.
 * 
 * @see http://wiki.netbeans.org/BookNBPlatformCookbookCH0211
 * @author Geraldo
 */
public class CloudStatus {

    private int status = STATUS_DISCONNECTED;

    private final JLabel label;

    /**
     * Not connected with any cloud service provider
     */
    public static final int STATUS_DISCONNECTED = 0;
    
    /**
     * Connecting with a cloud service provider
     */
    public static final int STATUS_CONNECTING = 1;
    
    /**
     * Connected to Dropbox cloud provider
     */
    public static final int STATUS_DBX_CONNECTED = 2;

    private static final String[] STATUS_MESSAGES;

    private static final ImageIcon IMG_DISCONNECTED;
    private static final ImageIcon IMG_CONNECTING;
    private static final ImageIcon IMG_DBX_CONNECTED;

    static {
        IMG_DISCONNECTED = new ImageIcon(
                ImageUtilities.loadImage(
                        "latexstudio/editor/resources/icons/dbx-connected.png"));
        IMG_CONNECTING = new ImageIcon(
                ImageUtilities.loadImage(
                        "latexstudio/editor/resources/icons/connecting.png"));
        IMG_DBX_CONNECTED = new ImageIcon(
                ImageUtilities.loadImage(
                        "latexstudio/editor/resources/icons/disconnected.png"));

        STATUS_MESSAGES = new String[3];

        STATUS_MESSAGES[STATUS_DISCONNECTED] = "Disconnected";
        STATUS_MESSAGES[STATUS_CONNECTING] = "Connecting";
        STATUS_MESSAGES[STATUS_DBX_CONNECTED] = "Connected to Dropbox";
    }

    private static final CloudStatus instance
            = new CloudStatus();

    /**
     * Get the single CloudStatus' instance
     * @return the single CloudStatus' instance
     */
    public static CloudStatus getInstance() {
        return instance;
    }

    /**
     * Returns the message of the specified status.
     * @param status One of the constants defined in ConnectionStatus
     *              started as: STATUS_*
     * @return The message of the specified status.
     */
    public static String getStatusMessage(int status) {
        if (status >= 0 && status <= STATUS_MESSAGES.length) {
            return STATUS_MESSAGES[status];
        }
        
        return null;
    }

    protected CloudStatus() {
        this.status = STATUS_DISCONNECTED;
        this.label = new JLabel(IMG_DISCONNECTED);
        this.label.setToolTipText(getStatusMessage(this.status));
    }

    /**
     * Returns the label that will be used to display the Connection Status.
     * @return the label that will be used to display the Connection Status.
     */
    JComponent getComponent() {
        return this.label;
    }

    /**
     * Returns the current Cloud Status.
     * One of the constants defined in ConnectionStatus started as: STATUS_*
     * @return the current Cloud Status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Updates the current Cloud Status.
     * @param newStatus One of the constants defined in ConnectionStatus started as: STATUS_*
     */
    public void setStatus(int newStatus) {
        this.setStatus(newStatus, "");
    }

    /**
     * Updates the current Cloud Status.
     * @param newStatus One of the constants defined in ConnectionStatus started as: STATUS_*
     * @param additional An additional text to display next to the status message
     */
    public void setStatus(int newStatus, String additional) {
        this.status = newStatus;
        Icon icon;
        switch (this.status) {
            case STATUS_DBX_CONNECTED:
                icon = IMG_DISCONNECTED;
                break;
            case STATUS_CONNECTING:
                icon = IMG_CONNECTING;
                break;
            default:
                icon = IMG_DBX_CONNECTED;
                break;
        }
        
        this.label.setToolTipText(getStatusMessage(this.status) + additional);
        this.label.setIcon(icon);
    }
}
