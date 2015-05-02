/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latexstudio.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//latexstudio.editor//RevisionDisplay//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "RevisionDisplayTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "latexstudio.editor.RevisionDisplayTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_RevisionDisplayAction",
        preferredID = "RevisionDisplayTopComponent"
)
@Messages({
    "CTL_RevisionDisplayAction=RevisionDisplay",
    "CTL_RevisionDisplayTopComponent=RevisionDisplay Window",
    "HINT_RevisionDisplayTopComponent=This is a RevisionDisplay window"
})
public final class RevisionDisplayTopComponent extends TopComponent {

    public RevisionDisplayTopComponent() {
        initComponents();
        setName(Bundle.CTL_RevisionDisplayTopComponent());
        setToolTipText(Bundle.HINT_RevisionDisplayTopComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        rSyntaxTextArea = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();

        rSyntaxTextArea.setEditable(false);
        rSyntaxTextArea.setBackground(new java.awt.Color(204, 204, 204));
        rSyntaxTextArea.setColumns(20);
        rSyntaxTextArea.setRows(5);
        rSyntaxTextArea.setSyntaxEditingStyle(org.openide.util.NbBundle.getMessage(RevisionDisplayTopComponent.class, "RevisionDisplayTopComponent.rSyntaxTextArea.syntaxEditingStyle")); // NOI18N
        jScrollPane1.setViewportView(rSyntaxTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea rSyntaxTextArea;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public void setText(String text) {
        rSyntaxTextArea.setText(text);
    }
    
}
