package tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/15/13 - 2:53 PM <p>
 * <b>Description</b>: <br>
 */
public class CmpHttpLayer implements IConnection {
    private static final Logger logger = LoggerFactory.getLogger(CmpHttpLayer.class);

    private String url;
    private String cookie;
    private int timeOutInMillisec = 60 * 1000;

    public CmpHttpLayer(String url,int timeOutInMillisec) {
        this.url = url;
        this.timeOutInMillisec = timeOutInMillisec;
    }

    public CmpHttpLayer(String url) {
        this.url = url;
    }

    public CmpHttpLayer(String url, String cookie) {
        this.url = url;
        this.cookie = cookie;
    }

    public CmpHttpLayer(String url, String cookie, int timeOutInMillisec) {
        this.url = url;
        this.cookie = cookie;
        this.timeOutInMillisec = timeOutInMillisec;
    }

    private HttpURLConnection httpConnect() throws CMPConnectionException {
        try {
            logger.debug("Baglanti olusturulacak");
            URL urlServlet = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlServlet.openConnection();

            con.setConnectTimeout(timeOutInMillisec);
            con.setReadTimeout(timeOutInMillisec);

            con.setAllowUserInteraction(true);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/pkixcmp");
            if(cookie != null)
                con.setRequestProperty("Cookie", cookie);
            logger.debug("Baglanti olusturuldu");
            return con;
        } catch (Exception e) {
            logger.error("Error while connecting to Servlet:" + e.getMessage(), e);
            throw new CMPConnectionException("Error while connecting to Servlet:" + e.getMessage(), e);
        }
    }


    public void sendPKIMessage(EPKIMessage message) throws CMPConnectionException {
        HttpURLConnection connection = httpConnect();
        try {
            sendResponse(connection, message.getEncoded());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new CMPConnectionException("Invalid HTTP Response:" + connection.getResponseCode());
        } catch (Exception e) {
            logger.error("Error while receiving message from Servlet:" + e.getMessage(), e);
            throw new RuntimeException("Error while receiving message from Servlet:" + e.getMessage(), e);
        }
    }

    public EPKIMessage sendPKIMessageAndReceiveResponse(EPKIMessage message) throws CMPConnectionException, CMPProtocolException {
        HttpURLConnection connection = httpConnect();
        try {
            sendResponse(connection, message.getEncoded());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new CMPConnectionException("Invalid HTTP Response:" + connection.getResponseCode()+"-"+connection.getResponseMessage() );

        } catch (CMPConnectionException e) {
            logger.error("Error while receiving message from Servlet:" + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while receiving message from Servlet:" + e.getMessage(), e);
            throw new RuntimeException("Error while receiving message from Servlet:" + e.getMessage(), e);
        }
        try {
            byte[] response = getResponse(connection);
            /*
            String responseData = Base64.encode(response);
            System.out.println("Servisten gelen veri"+responseData);
            */
            return new EPKIMessage(response);
        } catch (Exception e) {
            logger.error("Error while receiving message from Servlet:" + e.getMessage(), e);
            throw new CMPProtocolException(EPKIFailureInfo.badDataFormat, "Error while receiving message from Servlet:" + e.getMessage());
        }
    }


    private void sendResponse(HttpURLConnection connection, byte[] aData) throws IOException {
        OutputStream outstream = connection.getOutputStream();
        outstream.write(aData);
        outstream.close();
    }

    private byte[] getResponse(HttpURLConnection connection) throws ClassNotFoundException, IOException {
        int pkiMessageSize = Integer.parseInt(connection.getHeaderField("Content-Length"));
        InputStream in = connection.getInputStream();
        byte[] pkiMessageInBytes = new byte[pkiMessageSize];
        int offset = 0;
        while (pkiMessageSize > 0) {
            int read = in.read(pkiMessageInBytes, offset, pkiMessageSize);
            if (read == -1)
                throw new IOException("Unexpected End of PKI Message");
            pkiMessageSize -= read;
            offset += read;
        }
        in.close();
        return pkiMessageInBytes;
    }


    public void finish() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
