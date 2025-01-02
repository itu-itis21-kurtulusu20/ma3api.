package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import com.phison.cAssdLib.PscGtiLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tubitak.akis.cif.akisExceptions.AkisCardException;
import tubitak.akis.cif.akisExceptions.AkisException;
import tubitak.akis.cif.functions.ICommandTransmitter;

import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class UKTSDCommandTransmitter implements ICommandTransmitter {

    protected static Logger logger = LoggerFactory.getLogger(UKTSDCommandTransmitter.class);

    protected PscGtiLib oGtiLib = new PscGtiLib();

    public UKTSDCommandTransmitter(String strPath, String appPath) throws CardException {
        int dwRet;
        int[] dwRLen = new int[2];
        byte[] byTmp = new byte[64];
        //init
        dwRet = ASSDInitGti(1, appPath, strPath);
        if (dwRet != 0) {
            throw new CardException("ASSDInitGti failed, err: " + oGtiLib.ASSDGetGtiLastCode());
        }
        //Detect
        dwRet = oGtiLib.ASSDDetectAssdDevices(strPath.getBytes(), strPath.length());
        if (dwRet != 0) {
            throw new CardException("ASSDDetectAssdDevices failed, err: " + oGtiLib.ASSDGetGtiLastCode());
        }
        //Connect
        dwRet = oGtiLib.ASSDConnect();
        if (dwRet != 0) {
            throw new CardException("ASSDConnect failed, err: " + oGtiLib.ASSDGetGtiLastCode());
        }
        dwRet = oGtiLib.ASSDResetICC(1);
        if (dwRet != 0) {
            throw new CardException("ASSDResetICC failed, err: " + oGtiLib.ASSDGetGtiLastCode());
        }
        dwRet = oGtiLib.ASSDGetATR(byTmp, dwRLen);
        if (dwRet != 0) {
            throw new CardException("ASSDGetATR failed, err: " + oGtiLib.ASSDGetGtiLastCode());
        }

    }

    @Override
    public void closeCardTerminal() {
        int dwRet;
        dwRet = oGtiLib.ASSDDisconnect();
        if (dwRet != 0) {
            try {
                throw new CardException("ASSDDisconnect failed, err: " + oGtiLib.ASSDGetGtiLastCode());
            } catch (CardException e) {
                logger.error("Error in UKTSDCommandTransmitter", e);
            }
        }
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU apdu)  {

        int[] dwRLen = new int[2];
        byte[] byApdu = apdu.getBytes();
        byte[] byResp = new byte[512];
        oGtiLib.ASSDSendAndReceiveAPDU(byApdu, byApdu.length, byResp, dwRLen, 60);
        return new ResponseAPDU(Arrays.copyOf(byResp, dwRLen[0]));

    }

    @Override
    public int getCommandBufferSize() {
        // default
        return 0xF4;
    }

    @Override
    public void reset() {

    }

    @Override
    public void reset(String s) {

    }

    @Override
    public ATR atr() throws AkisCardException {
        int dwRet;
        int[] dwRLen = new int[2];
        byte[] byTmp = new byte[64];
        dwRet = oGtiLib.ASSDGetATR(byTmp, dwRLen);
        if (dwRet != 0) {
            try {
                throw new CardException("ASSDResetICC failed, err: " + oGtiLib.ASSDGetGtiLastCode());
            } catch (Exception e) {
                logger.error("Error in UKTSDCommandTransmitter", e);
            }
        }
        if (Arrays.copyOf(byTmp, dwRLen[0]).length > 0) {
            return new ATR(Arrays.copyOf(byTmp, dwRLen[0]));
        } else {
            try {
                throw new AkisException("ATR alinamadi: " + oGtiLib.ASSDGetGtiLastCode());
            } catch (AkisException e) {
                logger.error("Error in UKTSDCommandTransmitter", e);
            }
        }
        return null;
    }

    @Override
    public byte[] control(byte[] apdu)  {

        return apdu;
    }

    private int ASSDInitGti(int dwSDKType, String sAppPath, String strSDCardPath) {
        if (dwSDKType >= 1) {
            //check gti.bin exist?
            try {
                //check app's directory is exist?
                File app_dir = new File(sAppPath);
                if (!app_dir.exists())
                    throw new Exception("app's directory not exist");

                File f = new File(sAppPath, "gti.bin");
                if (!f.exists()) {
                    byte[] byTmp = new byte[64 * 1024];
                    f.createNewFile();
                    RandomAccessFile RfileRandomFile;//current accessing file handle
                    RfileRandomFile = new RandomAccessFile(f, "rw");

                    RfileRandomFile.write(byTmp, 0, 64 * 1024);
                    RfileRandomFile.getFD().sync();
                    RfileRandomFile.close();
                }
            } catch (Exception e) {
                logger.warn("Warning in UKTSDCommandTransmitter", e);
                return -1;
            }
        } else if (dwSDKType == 0) {
            //generate gti.bin at sdcard root folder
            try {

                File f = new File(strSDCardPath, "gti.bin");
                if (!f.exists()) {
                    byte[] byTmp = new byte[64 * 1024];
                    f.createNewFile();
                    RandomAccessFile RfileRandomFile;//current accessing file handle
                    RfileRandomFile = new RandomAccessFile(f, "rw");

                    RfileRandomFile.write(byTmp, 0, 64 * 1024);
                    RfileRandomFile.getFD().sync();
                    RfileRandomFile.close();
                }
            } catch (Exception e) {
                logger.warn("Warning in UKTSDCommandTransmitter", e);
                return -1;
            }

        }

        return oGtiLib.ASSDInitGti(dwSDKType, sAppPath.getBytes(), sAppPath.getBytes().length);
    }

}
