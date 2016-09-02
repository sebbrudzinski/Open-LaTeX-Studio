/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latexstudio.editor;

import javax.swing.SwingUtilities;
import latexstudio.editor.remote.Cloud;
import latexstudio.editor.remote.DbxUtil;

/**
 *
 * @author White Hsu
 */
public class DbxAutoSync extends Thread {

    private final DropboxRevisionsTopComponent drtc = new TopComponentFactory<DropboxRevisionsTopComponent>()
            .getTopComponent(DropboxRevisionsTopComponent.class.getSimpleName());    
    
    private int interval;  
    
    public DbxAutoSync() {
        super();
        this.interval = 1;
    }
    
    public DbxAutoSync(int val) {
        super();
        this.interval = val;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }      
    
    @Override
    public void run() {
        while(this.interval > 0 && Cloud.getInstance().getStatus() == Cloud.Status.DBX_CONNECTED) {
            try {                                
                Thread.sleep(this.interval * 1000 * 60);
                
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run(){
                        new DbxFileActions().saveProgress(DbxUtil.getDbxClient(), drtc, false);
                    }
                }); 
                
            } catch (InterruptedException ex) {
                break;
            } 
        }
    }    
}
