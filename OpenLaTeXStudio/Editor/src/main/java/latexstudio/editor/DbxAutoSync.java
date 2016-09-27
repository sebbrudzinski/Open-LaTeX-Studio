/* 
 * Copyright (c) 2016 White Hsu
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import javax.swing.SwingUtilities;
import latexstudio.editor.remote.Cloud;
import latexstudio.editor.remote.DbxUtil;

/**
 *
 * @author White Hsu
 */
public class DbxAutoSync implements Runnable {
    
    private final DropboxRevisionsTopComponent drtc = new TopComponentFactory<DropboxRevisionsTopComponent>()
            .getTopComponent(DropboxRevisionsTopComponent.class.getSimpleName());    
    
    private int interval;  
    
    public DbxAutoSync() {
        this(1);
    }
    
    public DbxAutoSync(int val) {
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
