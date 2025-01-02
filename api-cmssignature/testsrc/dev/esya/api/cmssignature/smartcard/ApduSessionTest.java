package dev.esya.api.cmssignature.smartcard;

import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.APDUSmartCard;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class ApduSessionTest
{
	static protected APDUSmartCard bsc;


	private static void decideParameter() {
		boolean parameter = false;
		int major = 0;
		int minor = 0;

		String versionStr = System.getProperty("java.version");
		String[] split = versionStr.split("\\.");
		if(split.length == 3)
		{
			String []minorStrParse = split[2].split("_");
			if(minorStrParse.length == 2)//1.8.0_66, 1.6.0_45, 1.7.0_11
			{
				major = Integer.parseInt(split[1]);
				minor = Integer.parseInt(minorStrParse[1]);
			}
			else //9.0.1;
			{
				major = Integer.parseInt(split[0]);
			}
		}
		else if(split.length == 1) //10
		{
			major = Integer.parseInt(versionStr);
		}

		//Bütün 7'lerde false olacak şekilde ayarlandı. O şekilde çalışıyor.
		if(major < 8)
			parameter = false;
		if(major == 8)
		{
			if(minor < 20)
				parameter = false;
			else
				parameter = true;
		}
		if(major > 8)
			parameter = true;


		System.out.println("versionStr: " + versionStr);
		System.out.println("major: " + major + " minor: " + minor);
		System.out.println("Parametre: " + parameter);

	}
	
	public static void main(String[] args) {


		//decideParameter();

		runGUI();



	}

	private static void runGUI() {
		System.out.println("System.getProperty(\"java.version\"): " + System.getProperty("java.version"));
		System.out.println("System.getProperty(\"java.runtime.version\"): " + System.getProperty("java.runtime.version"));






		//readParameters();


		JFrame frame = new JFrame("Apdu Test");

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());


		JButton button = new JButton();
		button.setText("Session ac");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				openSession();

			}
		});

		panel.add(button);

		JButton button2 = new JButton();
		button2.setText("Session kapat");
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				closeSession();

			}
		});

		panel.add(button2);

		frame.add(panel);
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}


	private static void readParameters() {
		System.out.println("-----------Parameters--------------");

        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List<String> list = bean.getInputArguments();

        for (String param: list) {
            System.out.println(param);

        }

    }

	static CommandTransmitterPCSC pcsc;

	protected static void openSession() {
		try {



			List<CardTerminal> tList = TerminalFactory.getDefault().terminals().list();
			pcsc = new CommandTransmitterPCSC(tList.get(0), false);





//			TerminalFactory terminalFactory = TerminalFactory.getDefault();
//			CardTerminal terminal = terminalFactory.terminals().list().get(0);
//			Card card = terminal.connect("T=1");
//			CardChannel channel = card.getBasicChannel();
//
//
//			CommandAPDU getChallange = new CommandAPDU(new byte []{});
//			ResponseAPDU responseAPDU = channel.transmit(getChallange);

			//1
			//card.disconnect(true);
			//2
			//card.endExclusive();

			bsc = new APDUSmartCard();
			bsc.openSession(1);





			System.out.println("Session açıldı");


//			/readParameters();

		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	protected static void closeSession() {
		try {

			/*Method method = pcsc.getClass().getDeclaredMethod("b");
			method.setAccessible(true);
			Boolean disConnectparam = (Boolean)method.invoke(pcsc);
			System.out.println("disConnectparam: " + disConnectparam);*/



			bsc.closeSession();
			System.out.println("Session kapatıldı");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
