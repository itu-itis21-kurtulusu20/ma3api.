package tr.gov.tubitak.uekae.esya.api.asn1viewer;

import com.objsys.asn1j.runtime.Asn1Type;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by orcun.ertugrul on 14-May-18.
 */
public class Asn1SelectionForm {
    protected static Logger logger = LoggerFactory.getLogger(Asn1SelectionForm.class);
    public JPanel panelMain;
    private JTextField filePathTextField;
    private JButton decodeButton;
    private JTree treeAsn1Classes;
    private JButton makeLatestDecodeButton;


    public Asn1SelectionForm() {
        filePathTextField.setText(AppSettings.getLatestFilePath());


        decodeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    AppSettings.setLatestFilePath(filePathTextField.getText());
                    decodeFile();
                } catch (Exception ex) {
                    logger.error("Error in Asn1SelectionForm", ex);
                }

            }
        });

        makeLatestDecodeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    byte[] encodedBytes = AsnIO.dosyadanOKU(AppSettings.getLatestFilePath());
                    Class clazz = Class.forName(AppSettings.getLatestAsn1Type());
                    decodeFile(encodedBytes, clazz);
                } catch (Exception ex) {
                    logger.error("Error in Asn1SelectionForm", ex);
                }

            }
        });
    }


    public void loadAsn() {
        String namespace = "tr.gov.tubitak.uekae.esya.asn";

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(namespace);

        DefaultTreeModel model = (DefaultTreeModel) treeAsn1Classes.getModel();
        model.setRoot(rootNode);

        Set<Class<? extends Asn1Type>> asn1Types = getClassesFromPackage(namespace);

        ArrayList<Class> asn1ClassList = new ArrayList(asn1Types);

        Collections.sort(asn1ClassList, new Comparator<Class>() {
            public int compare(Class obj1, Class obj2) {
                return obj1.getName().compareTo(obj2.getName());
            }
        });


        for (Class aClass : asn1ClassList) {
            String nameWithOutNamespace = aClass.getName().replaceFirst(namespace + ".", "");
            insertClassToJTree(aClass, nameWithOutNamespace);
        }
    }

    public void decodeFile(byte[] encodedBytes, Class clazz) throws ESYAException {
        Asn1GUI asnGui = new Asn1GUI();
        asnGui.decode(encodedBytes, clazz);

        JFrame frame = new JFrame("Asn");
        frame.setContentPane(asnGui.panelMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public void decodeFile() throws Exception {
        String filePath = filePathTextField.getText();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeAsn1Classes.getLastSelectedPathComponent();
        if (selectedNode.getChildCount() > 0)
            throw new ESYAException("Please Select A Class");

        Class<? extends Asn1Type> clazz = ((Asn1TypeJTreeObject) selectedNode.getUserObject()).getAsn1Type();
        byte[] encodedBytes = AsnIO.dosyadanOKU(filePath);

        AppSettings.setLatestAsn1Type(clazz.getName());

        decodeFile(encodedBytes, clazz);
    }


    public void insertChildNodes(DefaultMutableTreeNode node) {
        Asn1Type nodeObj = (Asn1Type) node.getUserObject();


    }


    public static Set<Class<? extends Asn1Type>> getClassesFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);

        return reflections.getSubTypesOf(Asn1Type.class);
    }

    private void insertClassToJTree(Class<? extends Asn1Type> aAsn1Type, String aNameSpace) {
        String[] names = aNameSpace.split("\\.");

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeAsn1Classes.getModel().getRoot();

        DefaultMutableTreeNode travellerNode = rootNode;

        for (String aName : names) {
            boolean found = false;
            int childCount = travellerNode.getChildCount();

            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) travellerNode.getChildAt(i);
                String nodeName = ((Asn1TypeJTreeObject) aNode.getUserObject()).getName();
                if (aName.equals(nodeName)) {
                    found = true;
                    travellerNode = aNode;
                    break;
                }
            }

            if (found == false) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Asn1TypeJTreeObject(aAsn1Type, aName));
                travellerNode.add(newNode);
                travellerNode = newNode;
            }

        }

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout(0, 0));
        panelMain.setMinimumSize(new Dimension(500, 500));
        panelMain.setPreferredSize(new Dimension(500, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panelMain.add(panel1, BorderLayout.NORTH);
        filePathTextField = new JTextField();
        filePathTextField.setText("File Path");
        panel1.add(filePathTextField, BorderLayout.CENTER);
        makeLatestDecodeButton = new JButton();
        makeLatestDecodeButton.setText("MakeLatestDecode");
        panel1.add(makeLatestDecodeButton, BorderLayout.EAST);
        decodeButton = new JButton();
        decodeButton.setText("Decode");
        panelMain.add(decodeButton, BorderLayout.SOUTH);
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, BorderLayout.CENTER);
        treeAsn1Classes = new JTree();
        scrollPane1.setViewportView(treeAsn1Classes);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
