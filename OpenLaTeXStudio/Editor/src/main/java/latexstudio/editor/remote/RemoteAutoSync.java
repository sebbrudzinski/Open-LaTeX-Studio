/* 
 * Copyright (c) 2016 White Hsu
 * 
 * See the file LICENSE for copying permission.
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
    
    public enum SyncPeriod {
        Disable(0),
        OneMinute(1),
        ThreeMinutes(3),
        FiveMinutes(5),
        TenMinutes(10);
        
        private final int period;
        
        private SyncPeriod(int periodInt) {
            this.period = periodInt;
        }

        public int getPeriod() {
            return period;
        }
        
        @Override
        public String toString() {
            switch(this) {
                case Disable:
                    return "Disable";
                case OneMinute:
                    return "1 Minute";
                case ThreeMinutes:
                    return "3 Minutes";
                case FiveMinutes:
                    return "5 Minutes";
                case TenMinutes:
                    return "10 Minutes";
                default:
                    return null;
            }
        }
    }
    
    private Thread autoSyncThread = null;
    private DbxAutoSync dbxAutoSyncObj = null;
    private SyncPeriod syncPeriodEnum = SyncPeriod.Disable;
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        DbxState dbxState = etc.getEditorState().getDbxState();       
        
        if(Cloud.getInstance().getStatus() != Cloud.Status.DBX_CONNECTED || dbxState == null) {
            JOptionPane.showMessageDialog(null, "No Dropbox file has been loaded.\n"
                        + "You must open Dropbox file, before you save it.", "Cannot save progress", JOptionPane.WARNING_MESSAGE);
           return; 
        }
        
        SyncPeriod[] periodOptions = {
            SyncPeriod.Disable,
            SyncPeriod.OneMinute, 
            SyncPeriod.ThreeMinutes, 
            SyncPeriod.FiveMinutes, 
            SyncPeriod.TenMinutes
        };                
        
        syncPeriodEnum = (SyncPeriod) JOptionPane.showInputDialog(
                null,
                "Select the auto sync period (in minute)",
                "Period of Dropbox Remote Sync",
                JOptionPane.QUESTION_MESSAGE,
                null,
                periodOptions,
                periodOptions[0]
        );                                        
        
        if(syncPeriodEnum == null) {
            return;             
        }
        
        LOGGER.log("You set your interval of Dropbox auto sync as: " + syncPeriodEnum.toString());
        
        if(syncPeriodEnum != SyncPeriod.Disable) {                  
            if(autoSyncThread == null) {
                dbxAutoSyncObj = new DbxAutoSync(syncPeriodEnum.getPeriod());
                autoSyncThread = new Thread(dbxAutoSyncObj);
                autoSyncThread.start();
            } else {
               dbxAutoSyncObj.setInterval(syncPeriodEnum.getPeriod());
            }
        } else {            
            if(autoSyncThread != null){
               dbxAutoSyncObj.setInterval(SyncPeriod.Disable.getPeriod());
               autoSyncThread.interrupt();
               autoSyncThread = null;
            }
        }                
    }
}




