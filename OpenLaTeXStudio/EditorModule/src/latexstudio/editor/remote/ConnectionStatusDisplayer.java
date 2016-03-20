/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import java.awt.Component;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @see http://wiki.netbeans.org/BookNBPlatformCookbookCH0211
 * @author Geraldo
 */
@ServiceProvider(service=StatusLineElementProvider.class)
public class ConnectionStatusDisplayer implements StatusLineElementProvider {

    private Component component;
    
    @Override
    public Component getStatusLineElement() {
        return component;
    }
    
}
