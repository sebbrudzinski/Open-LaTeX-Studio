/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Helper class for retrieving top components within application.
 * @author Sebastian
 */
public final class TopComponentFactory<T> {
    
    /**
     * Retrieves top component. 
     * @param name name of the top component
     * 
     * @return top component, casted appropriately
     */
    public T getTopComponent(String name) {
        TopComponent tc = WindowManager.getDefault().findTopComponent(name);
        return (T) tc; 
    }
    
}
