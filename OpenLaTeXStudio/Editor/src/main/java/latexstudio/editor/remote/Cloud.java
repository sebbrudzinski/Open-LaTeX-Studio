/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Class responsible for displaying the Cloud Support Status.
 *
 * @see http://wiki.netbeans.org/BookNBPlatformCookbookCH0211
 * @author Geraldo
 */
public class Cloud {

    private Status status = Status.DISCONNECTED;

    private final JLabel label;

    /**
     * Enum that represents the possible Status for the Cloud Connection
     */
    public enum Status {
        /**
         * Not connected with any cloud service provider
         */
        DISCONNECTED,
        /**
         * Connecting with a cloud service provider
         */
        CONNECTING,
        /**
         * Connected to Dropbox cloud provider
         */
        DBX_CONNECTED;

        /**
         * Get the Description related to the Status
         * @return Description related to the Status
         */
        @Override
        public String toString() {
            switch (this) {
                case DISCONNECTED:
                    return "Working locally";
                case CONNECTING:
                    return "Connecting";
                case DBX_CONNECTED:
                    return "Connected to Dropbox";
                default:
                    return null;
            }
        }

        /**
         * Get the ImageIcon related to the Status
         * @return 
         */
        public ImageIcon getImageIcon() {
            switch (this) {
                case DISCONNECTED:
                    return new ImageIcon(
                            Cloud.class.getResource(
                                    "/openlatexstudio/icons/disconnected.png"));
                case CONNECTING:
                    return new ImageIcon(
                            Cloud.class.getResource(
                                    "/openlatexstudio/icons/connecting.gif"));
                case DBX_CONNECTED:
                    return new ImageIcon(
                            Cloud.class.getResource(
                                    "/openlatexstudio/icons/dbx-connected.png"));
                default:
                    return null;
            }
        }
    }

    private static final Cloud INSTANCE = new Cloud();

    /**
     * Get the single CloudStatus' instance
     *
     * @return the single CloudStatus' instance
     */
    public static Cloud getInstance() {
        return INSTANCE;
    }

    private Cloud() {
        this.status = Status.DISCONNECTED;
        this.label = new JLabel(this.status.getImageIcon());
        this.label.setToolTipText(this.status.toString());
    }

    /**
     * Returns the label that will be used to display the Connection Status.
     *
     * @return the label that will be used to display the Connection Status.
     */
    JComponent getComponent() {
        return this.label;
    }

    /**
     * Returns the current Cloud Status. 
     * @see Status
     *
     * @return the current Cloud Status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Updates the current Cloud Status.
     * @see Status
     * 
     * @param newStatus The new Status
     */
    public void setStatus(Status newStatus) {
        this.setStatus(newStatus, "");
    }

    /**
     * Updates the current Cloud Status.
     *
     * @param newStatus The new Status
     *
     * @param additional An additional text to display next to the status
     * message
     */
    public void setStatus(Status newStatus, String additional) {
        this.status = newStatus;

        this.label.setToolTipText(this.status.toString() + additional);
        this.label.setIcon(this.status.getImageIcon());
    }
}
