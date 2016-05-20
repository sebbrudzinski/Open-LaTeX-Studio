/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.FileActions;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

@ActionID(
        category = "File",
        id = "latexstudio.editor.OpenTemplate"
)
@ActionRegistration(
        displayName = "#CTL_OpenTemplate"
)
@ActionReference(path = "Menu/File", position = 1201)
@NbBundle.Messages("CTL_OpenTemplate=Open Template")

public final class OpenTemplate extends JDialog implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());

    private static final ApplicationLogger LOGGER = new ApplicationLogger("Templates xml info");

    private OpenTemplate() {
        //Dialog window properties
        setTitle("Choose Template");
        setLocationRelativeTo(etc);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setModalityType(ModalityType.APPLICATION_MODAL);

        add(new OpenTemplatePanel(this, getListModelWithTemplates(readXML())), BorderLayout.CENTER);
        pack();
    }

    /**
     * @return Array of nodes that contain informations about preinstalled
     * templates
     */
    private List<Element> readXML() {
        try {
            Document templatesXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getClass().getResourceAsStream(ApplicationUtils.TEMPLATES_FILE));

            Element rootElement = templatesXML.getDocumentElement();
            NodeList templatesNodeList = rootElement.getChildNodes();

            List<Element> templates = new ArrayList<>();
            for (int i = 0; i < templatesNodeList.getLength(); i++) {
                Node node = templatesNodeList.item(i);
                if (node instanceof Element) {
                    templates.add((Element) node);
                }
            }

            return templates;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.log("Templates data couldn't be read. This might indicate an error with the application.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param path template filename
     * @return template full path
     */
    private String getFullPathToTemplate(String path) {
        return ApplicationUtils.TEMPLATES_DIR + path;
    }

    private DefaultListModel<Template> getListModelWithTemplates(List<Element> elements) {
        DefaultListModel<Template> listModel = new DefaultListModel<>();
        for (Element e : elements) {
            listModel.addElement(getTemplateFromElement(e.getChildNodes()));
        }
        return listModel;
    }

    private Template getTemplateFromElement(NodeList templateNodes) {
        List<Element> templateElements = new ArrayList<>(4);
        for (int i = 0; i < templateNodes.getLength(); i++) {
            Node node = templateNodes.item(i);
            if (node instanceof Element) {
                templateElements.add((Element) node);
            }
        }

        Template template = new Template();

        for (Element templateElement : templateElements) {
            Text elementText = (Text) templateElement.getFirstChild();
            String elementString = elementText.getData();

            switch (templateElement.getTagName()) {
                case "name":
                    template.setName(elementString);
                    break;

                case "filename":
                    template.setPath(getFullPathToTemplate(elementString));
                    break;

                case "description":
                    template.setDescription(elementString);
                    break;

                case "source":
                    template.setSource(elementString);
                    break;
                default:
            }
        }
        return template;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        FileActions fileAction= new FileActions();
        
        switch (etc.canOpen())
        {
            case SAVE_AND_OPEN:
                fileAction.saveFile();
                this.setVisible(true);
                break;
            
            case OPEN:
                this.setVisible(true);
                break;
                
            default:
                //Do nothing
                break;
        }
    }
}
