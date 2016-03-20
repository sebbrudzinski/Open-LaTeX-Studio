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
 * Class responsible for providing a customized StatusLineElement.
 * 
 * @see CloudStatus
 * @see http://wiki.netbeans.org/BookNBPlatformCookbookCH0211
 * @author Geraldo
 */
@ServiceProvider(service = StatusLineElementProvider.class)
public class CloudStatusDisplayer implements StatusLineElementProvider {

    private final Component component = CloudStatus.getInstance().getComponent();

    /**
     * Returns the CloudStatus component to be displayd at the StatusLineElement.
     * @return the CloudStatus component to be displayd at the StatusLineElement.
     */
    @Override
    public Component getStatusLineElement() {
        return component;
    }

}
