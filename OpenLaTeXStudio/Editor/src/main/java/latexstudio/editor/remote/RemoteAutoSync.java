/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latexstudio.editor.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.DbxAutoSync;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Remote",
        id = "latexstudio.editor.remote.RemoteAutoSync"
)
@ActionRegistration(
        displayName = "#CTL_RemoteAutoSync"
)
@ActionReference(path = "Menu/Remote", position = 3458)
@Messages("CTL_RemoteAutoSync=Remote Auto Sync")
public final class RemoteAutoSync implements ActionListener {
    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private static final ApplicationLogger LOGGER = new ApplicationLogger("Cloud Support");
    
    private DbxAutoSync autoSyncThread = null;
    private String syncPeriod = "0";
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        DbxState dbxState = etc.getDbxState();
        
        if(Cloud.getInstance().getStatus() != Cloud.Status.DBX_CONNECTED || dbxState == null) {
            JOptionPane.showMessageDialog(null, "No Dropbox file has been loaded.\n"
                        + "You must open Dropbox file, before you save it.", "Cannot save progress", JOptionPane.WARNING_MESSAGE);
           return; 
        }
        
        String[] periodOptions = {"Disable", "1", "3", "5", "10"};                
        syncPeriod = (String) JOptionPane.showInputDialog(
                null,
                "Select the auto sync period (in min)",
                "Period of Dropbox Remote Sync",
                JOptionPane.QUESTION_MESSAGE,
                null,
                periodOptions,
                periodOptions[0]
        );                                
        
        LOGGER.log("You set your interval of Dropbox auto sync as: " + syncPeriod);
        
        if(syncPeriod == null) {
            return;             
        }
        
        if(!syncPeriod.startsWith("Disable")) {
            if(autoSyncThread == null) {
                autoSyncThread = new DbxAutoSync(Integer.valueOf(syncPeriod));
                autoSyncThread.start();
            } else {
               autoSyncThread.setInterval(Integer.valueOf(syncPeriod));
            }
        } else {
            if(autoSyncThread != null){
               autoSyncThread.setInterval(0);
               autoSyncThread.interrupt();
               autoSyncThread = null;
            }
        }
    }
}




