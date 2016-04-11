package latexstudio.editor.menu;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

@ActionID(
        category = "File",
        id = "latexstudio.editor.OpenTemplate"
)
@ActionRegistration(
        displayName = "#CTL_OpenTemplate"
)
@ActionReference(path = "Menu/File", position = 1201)
@NbBundle.Messages("CTL_OpenTemplate=Open Template")

/**
 * Dialog window that allow user to either open a preinstalled LaTeX template or use his own
*/
public final class OpenTemplate extends JDialog implements ActionListener
{

    private static final String pathToTemplatesDirectory = ApplicationUtils.getAppDirectory()
            + System.getProperty("file.separator") + "Templates";

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());

    private JList<TemplateFile> preinstalledTemplatesList;
    private JPanel preinstalledTemplatesPanel, customTemplatePanel, inputPanel, okPanel;
    private JButton choosePath;
    final JTextField customTemplatePath;

    private OpenTemplate()
    {
        //Dialog window properties
        setTitle("Choose Template");
        setLocationRelativeTo(etc);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initPanels();

        //Components initialization
        preinstalledTemplatesList = new JList<TemplateFile>(getPreinstalledTemplates());
        preinstalledTemplatesList.setSelectedIndex(0);
        preinstalledTemplatesList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
               customTemplatePath.setText("");
            }
        });

        customTemplatePath = new JTextField(20);
        choosePath = new JButton("Choose path");
        choosePath.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File selectedFile = FileChooserService.getSelectedFile("tex", "TeX files", FileChooserService.DialogType.OPEN);
                if (selectedFile != null && selectedFile.exists())
                {
                    customTemplatePath.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                File file = new File(customTemplatePath.getText());
                if (file != null && file.exists()) //When user choosed correct file
                {
                    OpenFile.openFile(etc, file);
                }
                else //When user choosed wrong file / selected template from list
                {
                    OpenFile.openFile(etc, preinstalledTemplatesList.getSelectedValue().getFile());
                }

                dispose();
            }
        });

        //Adds comonents to panels and panels to dialog window
        preinstalledTemplatesPanel.add(preinstalledTemplatesList);
        inputPanel.add(preinstalledTemplatesPanel);

        customTemplatePanel.add(customTemplatePath);
        customTemplatePanel.add(choosePath);
        inputPanel.add(customTemplatePanel);

        okPanel.add(okButton);

        add(inputPanel, BorderLayout.CENTER);
        add(okPanel, BorderLayout.SOUTH);

        pack();
    }

    private void initPanels()
    {
        preinstalledTemplatesPanel = new JPanel();
        preinstalledTemplatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                "Choose preinstalled template"));

        customTemplatePanel = new JPanel();
        customTemplatePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                "Or use your own template"));

        okPanel = new JPanel();

        inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    }

    private TemplateFile[] getPreinstalledTemplates()
    {
        //TODO make Templates directory self creating at installing (or building) 
        //and include there some templates to use

        File[] templatesFiles = new File(pathToTemplatesDirectory).listFiles();
        TemplateFile[] templates = new TemplateFile[templatesFiles.length];

        for (int i = 0; i < templatesFiles.length; i++)
        {
            templates[i] = new TemplateFile(templatesFiles[i]);
        }

        return templates;
    }

    //Invoked, when users clicks OpenTemplate menu item 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.setVisible(true);
    }

}

/*
 * This class exist to avoid extending File class, becouse
 * you can't Override listFiles() method (to return TemplateFile[] array for example)
 * It is just for printing only names of files (without whole path)
 */
class TemplateFile
{

    private File file;

    public TemplateFile(File file)
    {
        this.file = file;
    }

    public File getFile()
    {
        return file;
    }

    @Override
    public String toString()
    {
        return file.getName().substring(0, file.getName().length() - 4);
    }
}
