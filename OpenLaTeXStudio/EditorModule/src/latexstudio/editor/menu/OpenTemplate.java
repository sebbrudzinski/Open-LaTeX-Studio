package latexstudio.editor.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
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

    private static final String pathToTemplatesDirectory = "/latexstudio/editor/resources/templates/";

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());

    private OpenTemplate() {
        //Dialog window properties
        setTitle("Choose Template");
        setLocationRelativeTo(etc);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(new OpenTemplatePanel(this, getListModelWithTemplates(readXML())), BorderLayout.CENTER);
        pack();
    }

    /**
     @return Array of nodes that contain informations about preinstalled
     templates
     */
    private ArrayList<Element> readXML() {
        try {
            Document templatesXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getClass().getResourceAsStream("/latexstudio/editor/resources/templates.xml"));

            Element rootElement = templatesXML.getDocumentElement();
            NodeList templatesNodeList = rootElement.getChildNodes();

            ArrayList<Element> templates = new ArrayList<Element>();
            for (int i = 0; i < templatesNodeList.getLength(); i++) {
                Node node = templatesNodeList.item(i);
                if (node instanceof Element) {
                    templates.add((Element) node);
                }
            }
            
            return templates;
        } catch (IOException e) {
        } catch (SAXException e) {
        } catch (ParserConfigurationException e) {
        }
        return null;
    }
    
    /**
    
    @param path template filename
    @return template full path
    */
    private String getFullPathToTemplate(String path) {
        return pathToTemplatesDirectory + path;
    }

    private DefaultListModel<Template> getListModelWithTemplates(ArrayList<Element> elements) {
        DefaultListModel<Template> listModel = new DefaultListModel<Template>();
        for (Element e : elements) {
            listModel.addElement(getTemplateFromElement(e.getChildNodes()));
        }
        return listModel;
    }

    private Template getTemplateFromElement(NodeList templateNodes) {
        ArrayList<Element> templateElements = new ArrayList<Element>(4);
        for (int i = 0; i < templateNodes.getLength(); i++) {
            Node node = templateNodes.item(i);
            if (node instanceof Element) {
                templateElements.add((Element) node);
            }
        }

        Text name = (Text) templateElements.get(0).getFirstChild();
        Text filename = (Text) templateElements.get(1).getFirstChild();
        Text description = (Text) templateElements.get(2).getFirstChild();
        Text source = (Text) templateElements.get(3).getFirstChild();

        return new Template(name.getData(), getFullPathToTemplate(filename.getData()), description.getData(), source.getData());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
    }
}
