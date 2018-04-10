/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.v2.DbxClientV2;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.settings.ApplicationSettings;
import latexstudio.editor.util.ApplicationUtils;
import latexstudio.editor.util.PropertyService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Remote",
        id = "latexstudio.editor.remote.ConnectDropbox"
)
@ActionRegistration(
        displayName = "#CTL_ConnectDropbox"
)
@ActionReference(path = "Menu/Remote", position = 3333, separatorAfter = 3383)
@Messages("CTL_ConnectDropbox=Connect to Dropbox")
public final class ConnectDropbox implements ActionListener {
    
    private static final String PROPERTIES = "dropbox.properties";
    
    private static final String APP_KEY = PropertyService.
            readProperties(PROPERTIES).getProperty("dropbox.appKey");
    private static final String APP_SECRET = PropertyService.
            readProperties(PROPERTIES).getProperty("dropbox.appSecret");
    
    private static final ApplicationLogger LOGGER = new ApplicationLogger("Cloud Support");

    @Override
    public void actionPerformed(ActionEvent e) {
        Cloud.Status currentCloudStatus = Cloud.getInstance().getStatus();
        Cloud.getInstance().setStatus(Cloud.Status.CONNECTING);
        
        DbxAppInfo appInfo;
        try {
            appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                        "You are using the development version of the application and have not provided the necessary properties.\n"
                        + "In order to develop Dropbox features, create Dropbox developer account and fill in the necessary fields in dropbox.properties file.\n"
                        + "You can also contact us at open-latex-studio@googlegroups.com in case of any troubles.",
                "Development version",
                JOptionPane.WARNING_MESSAGE);
            Cloud.getInstance().setStatus(Cloud.Status.DISCONNECTED);
            return;
        }
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(DbxUtil.getDbxConfig(), appInfo);
        final String authorizeUrl = webAuth.start();
        
        class OpenUrlAction implements ActionListener {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    open(new URI(authorizeUrl));
                } catch (URISyntaxException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        
        JButton button = new JButton();
        button.setText("Connect to Dropbox");
        button.setToolTipText(authorizeUrl);
        button.addActionListener(new OpenUrlAction());
        
        String userToken = JOptionPane.showInputDialog(
            null, 
            button,
            "Authentication token required",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (userToken != null && !userToken.isEmpty()) {
            try {
                DbxAuthFinish authFinish = webAuth.finish(userToken);
                userToken = authFinish.getAccessToken();
                
                ApplicationSettings.Setting.DROPBOX_TOKEN.setValue(userToken);
                ApplicationSettings.INSTANCE.save();

                // getting dbx client displayName
                DbxClientV2 client = DbxUtil.getDbxClient();
                String additional = "";
                if(client != null) {
                    additional = " (" + client.users().getCurrentAccount().getName().getDisplayName() + ")";
                }
                
                LOGGER.log("Successfully connected application with Dropbox account.");
                Cloud.getInstance().setStatus(Cloud.Status.DBX_CONNECTED, additional);
                
            } catch (DbxException ex) {
                JOptionPane.showMessageDialog(null,
                        "Invalid access token! Open LaTeX Studio has NOT been connected with Dropbox.\n Please try again and provide correct access token.",
                        "Invalid token",
                        JOptionPane.ERROR_MESSAGE);
                Cloud.getInstance().setStatus(Cloud.Status.DISCONNECTED);
            }
        } else {
            Cloud.getInstance().setStatus(currentCloudStatus);
        }
    }
    
    private static void open(URI uri) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            } else {
                // Desktop API does not support various platforms - try different approach
                runtimeCommandOpen(uri);
            }
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        }
    }
    
    private static void runtimeCommandOpen(URI uri) {
        Runtime rt = Runtime.getRuntime();
        
        try {
            if (ApplicationUtils.isWindows()) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + uri.toString());
            } else {
                String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror",
                                             "netscape", "opera", "links", "lynx" };

                StringBuilder cmd = new StringBuilder();
                for (int i = 0 ; i < browsers.length; i++) {
                    cmd.append((i == 0 ? "" : " || ")).append(browsers[i]).append(" \"").append(uri.toString()).append("\" ");
                }

                rt.exec(new String[] { "sh", "-c", cmd.toString() });
            }
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        }  
    }
}
