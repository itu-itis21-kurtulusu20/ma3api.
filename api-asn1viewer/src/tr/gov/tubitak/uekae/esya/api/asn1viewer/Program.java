package tr.gov.tubitak.uekae.esya.api.asn1viewer;

import javax.swing.*;

/**
 * Created by orcun.ertugrul on 13-Oct-17.
 */
public class Program
{
    public static void main(String[] args)
    {
        Asn1SelectionForm main = new Asn1SelectionForm();
        main.loadAsn();

        JFrame frame = new JFrame("App");
        frame.setContentPane(main.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
    }
}
