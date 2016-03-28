/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.DropboxRevisionsTopComponent;
import latexstudio.editor.RevisionDisplayTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.settings.ApplicationSettings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Remote",
        id = "latexstudio.editor.remote.DisconnectDropbox"
)
@ActionRegistration(
        displayName = "#CTL_DisconnectDropbox"
)
@ActionReference(path = "Menu/Remote", position = 3508)
@Messages("CTL_DisconnectDropbox=Disconnect from Dropbox")
public final class DisconnectDropbox implements ActionListener {

    private final DropboxRevisionsTopComponent drtc = new TopComponentFactory<DropboxRevisionsTopComponent>()
            .getTopComponent(DropboxRevisionsTopComponent.class.getSimpleName());
    private final RevisionDisplayTopComponent revtc = new TopComponentFactory<RevisionDisplayTopComponent>()
            .getTopComponent(RevisionDisplayTopComponent.class.getSimpleName());

    private static final ApplicationLogger LOGGER = new ApplicationLogger("Cloud Support");

    @Override
    public void actionPerformed(ActionEvent e) {
        Cloud.Status currentCloudStatus = Cloud.getInstance().getStatus();
        Cloud.getInstance().setStatus(Cloud.Status.CONNECTING);

        DbxClient client = DbxUtil.getDbxClient();

        if (client == null) {
            LOGGER.log("Dropbox account already disconnected.");
            Cloud.getInstance().setStatus(Cloud.Status.DISCONNECTED);
            return;
        }

        String userToken = client.getAccessToken();

        if (userToken != null && !userToken.isEmpty()) {
            try {

                client.disableAccessToken();

                drtc.updateRevisionsList(null);
                drtc.close();
                revtc.close();

                ApplicationSettings.INSTANCE.setDropboxToken("");
                ApplicationSettings.INSTANCE.save();
                LOGGER.log("Successfully disconnected from Dropbox account.");
                Cloud.getInstance().setStatus(Cloud.Status.DISCONNECTED);

            } catch (DbxException ex) {
                DbxUtil.showDbxAccessDeniedPrompt();
                Cloud.getInstance().setStatus(currentCloudStatus);
            }
        } else {
            LOGGER.log("Dropbox account already disconnected.");
            Cloud.getInstance().setStatus(Cloud.Status.DISCONNECTED);
        }
    }
}
