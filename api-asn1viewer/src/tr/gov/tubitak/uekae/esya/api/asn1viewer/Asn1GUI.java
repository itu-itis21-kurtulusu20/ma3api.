package tr.gov.tubitak.uekae.esya.api.asn1viewer;

import com.google.common.base.Predicate;
import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1Choice;
import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import com.objsys.asn1j.runtime.Asn1Type;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.ocsp.BasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Created by orcun.ertugrul on 14-May-18.
 */
public class Asn1GUI {
    protected static Logger logger = LoggerFactory.getLogger(Asn1GUI.class);
    public JPanel panelMain;
    private JTree treeAsn1Structure;
    private JTextArea textAreaAsn1String;
    private JTextArea textAreaHex;


    private Asn1Type decodeField(byte[] encodedBytes, String clazzName) throws ESYAException {
        try {
            Class<?> clazz = Class.forName(clazzName);
            return decodeField(encodedBytes, clazz);
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    private Asn1Type decodeField(byte[] encodedBytes, Class<?> clazz) throws ESYAException {
        try {
            Constructor<?> ctor = clazz.getConstructor();
            Asn1Type asn1Obj = (Asn1Type) ctor.newInstance();

            Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(encodedBytes);
            asn1Obj.decode(buff);

            return asn1Obj;
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }


    public void decode(byte[] encodedBytes, Class<? extends Asn1Type> clazz) {
        try {
            Asn1Type asnObj = decodeField(encodedBytes, clazz);

            DecodedAsn1JTreeObject rootAsn1Obj = new DecodedAsn1JTreeObject(new Asn1ObjectContainer(asnObj),
                    asnObj.getClass().getSimpleName());
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootAsn1Obj);

            DefaultTreeModel model = (DefaultTreeModel) treeAsn1Structure.getModel();
            model.setRoot(rootNode);

            insertChildNodes(rootNode);


            treeAsn1Structure.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    treeSelectionChanged(e);
                }
            });

        } catch (Exception ex) {
            logger.error("Error in Asn1GUI", ex);
            //System.out.println(ex.toString());
        }
    }

    private void treeSelectionChanged(TreeSelectionEvent e) {

        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeAsn1Structure.getLastSelectedPathComponent();

            if (node == null)
                return;

            DecodedAsn1JTreeObject treeObj = (DecodedAsn1JTreeObject) node.getUserObject();
            if (treeObj == null) {
                textAreaAsn1String.setText("");
                textAreaHex.setText("");
                return;
            }

            Asn1Type asnObj = treeObj.getAsn1Obj();
            if (asnObj == null) {
                textAreaAsn1String.setText("");
                textAreaHex.setText("");
                return;
            }


            if (asnObj instanceof Asn1OctetString) {
                textAreaAsn1String.setText(((Asn1OctetString) asnObj).toString());
            } else if (asnObj instanceof Asn1ObjectIdentifier) {
                String str = OIDUtil.concat(((Asn1ObjectIdentifier) asnObj).value);
                String fullClassName = AppSettings.getClassForOid(((Asn1ObjectIdentifier) asnObj).value);
                if (fullClassName != null && !fullClassName.equals("")) {
                    Class<?> clazz = Class.forName(fullClassName);
                    str = str + " - " + clazz.getSimpleName();
                }
                textAreaAsn1String.setText(str);
            } else if (asnObj instanceof Name) {
                String str = UtilName.name2String((Name) asnObj);
                textAreaAsn1String.setText(str);

            } else if (asnObj instanceof Asn1Integer) {
                long value = ((Asn1Integer) asnObj).value;
                textAreaAsn1String.setText(Long.toString(value));
            } else if (asnObj instanceof Certificate) {
                textAreaAsn1String.setText(new ECertificate((Certificate) asnObj).toString());
            } else if (asnObj instanceof Asn1BigInteger) {
                BigInteger value = ((Asn1BigInteger) asnObj).value;
                textAreaAsn1String.setText(value.toString());
            } else if (asnObj instanceof Time) {
                String str = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(UtilTime.timeToCalendar(((Time) asnObj)).getTime());
                textAreaAsn1String.setText(str);
            } else if (asnObj instanceof CertificateList) {
                textAreaAsn1String.setText(new ECRL((CertificateList) asnObj).toString());
            } else if (asnObj instanceof BasicOCSPResponse) {
                textAreaAsn1String.setText(new EBasicOCSPResponse((BasicOCSPResponse) asnObj).toString());
            } else {
                textAreaAsn1String.setText("");
            }


            Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
            asnObj.encode(buff);
            byte[] encoded = buff.getMsgCopy();
            String encodedStr = StringUtil.toHexString(encoded);

            textAreaHex.setText(encodedStr);

        } catch (Exception ex) {
            logger.error("Error in Asn1GUI", ex);
        }
    }

    public void insertChildNodes(DefaultMutableTreeNode node) throws ESYAException {

        try {
            DecodedAsn1JTreeObject nodeTreeObject = (DecodedAsn1JTreeObject) node.getUserObject();
            Asn1Type nodeObj = nodeTreeObject.getAsn1Obj();

            //No Childs
            if (nodeObj == null)
                return;

            Field[] allFields = nodeObj.getClass().getDeclaredFields();
            if (nodeObj instanceof Asn1Choice) {
                Field[] coiceFields = Asn1Choice.class.getDeclaredFields();
                Field[] concatFields = new Field[allFields.length + coiceFields.length];
                System.arraycopy(allFields, 0, concatFields, 0, allFields.length);
                System.arraycopy(coiceFields, 0, concatFields, allFields.length, coiceFields.length);
                allFields = concatFields;
            }

            Predicate<Field> fieldPredicate = ReflectionUtils.withTypeAssignableTo(Asn1Type.class);
            Predicate<Field> arrayPredicate = ReflectionUtils.withTypeAssignableTo(Asn1Type[].class);

            for (Field aField : allFields) {

                if (arrayPredicate.apply(aField) == false && fieldPredicate.apply(aField) == false)
                    continue;

                //skip static field
                if (Modifier.isStatic(aField.getModifiers()))
                    continue;

                if (arrayPredicate.apply(aField) == true) {
                    handleArrayObj(node, nodeTreeObject.getAsn1ObjectContainer(), aField, allFields);
                } else {
                    aField.setAccessible(true);

                    Asn1ObjectContainer asnObj = new Asn1ObjectContainer((Asn1Type) aField.get(nodeObj));

                    addNewField(node, nodeObj, asnObj, aField, allFields, null);
                }
            }
        } catch (Exception ex) {
            logger.error("Error in Asn1GUI", ex);
        }
    }

    private void addNewField(DefaultMutableTreeNode parentNode, Asn1Type parentObj, Asn1ObjectContainer asnObjC, Field aField, Field[] allFields, String extraName) throws ESYAException {

        String fieldNameAtTree = aField.getName();

        if (extraName != null)
            fieldNameAtTree = fieldNameAtTree + "-" + extraName;


        Asn1ObjectContainer advancedObjC = decodeIfItIsDefinedType(parentObj, asnObjC, aField, allFields);

        if (advancedObjC != null) {
            asnObjC = advancedObjC;
            fieldNameAtTree = fieldNameAtTree + "-" + advancedObjC.mObject.getClass().getSimpleName();
        }


        DecodedAsn1JTreeObject asnTreeObj = new DecodedAsn1JTreeObject(asnObjC, fieldNameAtTree);
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(asnTreeObj);

        parentNode.add(childNode);

        insertChildNodes(childNode);
    }

    private void handleArrayObj(DefaultMutableTreeNode parentNode, Asn1ObjectContainer parentObj, Field aField, Field[] allFields) throws ESYAException {
        try {
            Asn1Type[] asnObjs = (Asn1Type[]) aField.get(parentObj.mObject);
            int i = 1;
            for (Asn1Type aAsnObj : asnObjs) {
                String extraName = aAsnObj.getClass().getSimpleName() + "-" + i;
                i++;
                Asn1ObjectContainer objectContainer = new Asn1ObjectContainer(aAsnObj);
                if (parentObj.mType != null)
                    objectContainer.setType(parentObj.mType);


                addNewField(parentNode, parentObj.mObject, objectContainer, aField, allFields, extraName);
            }
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    private Asn1ObjectContainer decodeIfItIsDefinedType(Asn1Type parentObj, Asn1ObjectContainer asnObjC, Field fieldToDecode, Field[] allFields) throws ESYAException {
        String fieldName = fieldToDecode.getName();

        try {
            Asn1Type objToDecode = asnObjC.mObject;

            if (asnObjC.mType != null && objToDecode instanceof Asn1OpenType) {
                //ToDo: Decode and Return
                Asn1Type decodedField = decodeField(((Asn1OpenType) objToDecode).value, asnObjC.mType);
                return new Asn1ObjectContainer(decodedField);
            }

            if (parentObj.getClass().getName().equals("tr.gov.tubitak.uekae.esya.asn.x509.Attribute")) {
                //System.out.println("HERE");
            }

            //Decode edilmeye çalışan alan için tanımlı bir OID alanı var mı?
            String oidFieldName = AppSettings.getOidFieldForType(parentObj.getClass().getName(), fieldName);
            Field oidField = null;

            if (oidFieldName == null)
                return null;

            for (Field searchField : allFields) {
                if (searchField.getName().equals(oidFieldName)) {
                    oidField = searchField;
                    break;
                }
            }

            if (oidField == null)
                return null;

            Object oidObj = oidField.get(parentObj);

            if (oidObj instanceof Asn1ObjectIdentifier) {
                //Tanımlı OID alanına karşılık gelen
                String type = AppSettings.getClassForOid(((Asn1ObjectIdentifier) oidObj).value);

                if (type != null && objToDecode instanceof Asn1OctetString) {
                    Asn1Type decodedField = decodeField(((Asn1OctetString) objToDecode).value, type);
                    return new Asn1ObjectContainer(decodedField);
                }

                if (type != null) {
                    asnObjC.setType(type);
                    return null;
                }

                return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    public static Set<Class<? extends Asn1Type>> getClassesFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);

        return reflections.getSubTypesOf(Asn1Type.class);
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
        panelMain.setPreferredSize(new Dimension(500, 500));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setContinuousLayout(false);
        splitPane1.setDividerLocation(250);
        panelMain.add(splitPane1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPane1.setLeftComponent(scrollPane1);
        treeAsn1Structure = new JTree();
        scrollPane1.setViewportView(treeAsn1Structure);
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setDividerLocation(200);
        splitPane2.setDividerSize(10);
        splitPane2.setLastDividerLocation(-1);
        splitPane2.setOrientation(0);
        splitPane2.setResizeWeight(0.5);
        splitPane1.setRightComponent(splitPane2);
        textAreaAsn1String = new JTextArea();
        textAreaAsn1String.setEditable(false);
        textAreaAsn1String.setLineWrap(true);
        splitPane2.setLeftComponent(textAreaAsn1String);
        textAreaHex = new JTextArea();
        textAreaHex.setEditable(false);
        textAreaHex.setLineWrap(true);
        splitPane2.setRightComponent(textAreaHex);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
