package com.example.androidsignexample;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.smartcardio.CardTerminal;

import tr.gov.tubitak.uekae.esya.api.smartcard.android.reader.atlantis.AtlantisReader;
import tr.gov.tubitak.uekae.esya.api.smartcard.android.reader.atlantis.AtlantisTerminalHandler;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.ECAlgorithmUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.android.acs.reader.ACSTerminalHandler;
import tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader.SCDTerminalHandler;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.APDUSmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.TerminalHandler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_PICK_FILE = 1;
    private static final int REQUEST_SAVE_FILE = 2;
    private static final String ACTION_USB_PERMISSION = "tr.gov.tubitak.bilgem.esya.android.signexample.USB_PERMISSION";

    private ProgressDialog progress;

    Uri uri;
    Uri saveUri;
    String hata;
    byte[] signedDocument;
    String fileName = null;
    TextView filePathView;
    ContentResolver resolver;
    APDUSmartCard mAPDUSmartCard;

    private class LoadCertificatesTask extends AsyncTask<CardTerminal, Void, Exception> {

        public LoadCertificatesTask(Activity callerActivity) {
            this.callerActivity = callerActivity;
        }

        Activity callerActivity;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Sertifikalar yükleniyor.\n Lütfen bekleyiniz...");
            progress.show();
        }

        @Override
        protected Exception doInBackground(CardTerminal... params) {
            Exception result = null;

            PendingIntent permissionIntent;
            {
                int flags = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ? PendingIntent.FLAG_MUTABLE : 0;
                permissionIntent = PendingIntent.getBroadcast(callerActivity, 0, new Intent(ACTION_USB_PERMISSION), flags);
            }

            try {
                ICommandTransmitter transmitter;
                TerminalHandler terminalHandler = null;

                // Farklı kart okuyucular aynı projede kullanılıyorsa, aşağıdaki örnekte olduğu gibi bir kontrol ekleyebilirsiniz.
                Iterator<UsbDevice> deviceIterator;
                {
                    UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
                    deviceIterator = deviceList.values().iterator();
                }

                while (deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    if (device == null) {
                        continue;
                    }

                    if (AtlantisReader.isSupported(device)) {
                        terminalHandler = new AtlantisTerminalHandler(callerActivity, permissionIntent);
                    } else if (device.getManufacturerName() != null) {
                        String mfcName = device.getManufacturerName().toUpperCase();
                        if (mfcName.contains("ACS")) {
                            terminalHandler = new ACSTerminalHandler(callerActivity, permissionIntent);
                        } else if (mfcName.contains("GEM") || mfcName.contains("UDEA") || mfcName.contains("OMNIKEY")) {
                            terminalHandler = new SCDTerminalHandler(callerActivity, permissionIntent);
                        }
                    }

                    if (terminalHandler != null) {
                        break;
                    }
                }

                if (terminalHandler == null) {
                    String msg = "terminalHandler is null";
                    Log.e(TAG, msg);
                    throw new RuntimeException(msg);
                }

                // Secure MicroSD kart için UKTSDTerminalHandler kullanabilirsiniz.
                // terminalHandler = new UKTSDTerminalHandler(callerActivity, permissionIntent);

                mAPDUSmartCard = new APDUSmartCard(terminalHandler);

                mAPDUSmartCard.setDisableSecureMessaging(true);

                CardTerminal[] terminalList = mAPDUSmartCard.getTerminalList();

                if (terminalList.length == 0) {
                    throw new Exception("Bağlı terminal sayısı 0");
                }

                CardTerminal cardTerminal = terminalList[0];

                transmitter = terminalHandler.getTransmitter(cardTerminal);

                mAPDUSmartCard.openSession(cardTerminal);

                List<byte[]> signCertValueList = mAPDUSmartCard.getSignatureCertificates();

                for (byte[] signCertValue : signCertValueList) {
                    ECertificate signCert = new ECertificate(signCertValue);
                    // Sadece nitelikli sertifikalar çekiliyor.
                    // Kanuni geçerliliği olmayan sertifikalarla imza atılmak istenirse bu kontrol kaldırılabilir.
                    // if(signCert.isQualifiedCertificate()){
                    certListAdapter.addItem(cardTerminal, signCert);
                    //get ATR Value
                    certListAdapter.getATRValue(transmitter);
                    Log.d(TAG, "Sertifika Sahibi :" + signCert.getSubject().getCommonNameAttribute());
                    // }
                }
                handler.sendEmptyMessage(1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception exc) {
                Log.e(TAG, "Sertifikalar alınırken hata oluştu.", exc);
                result = exc;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Exception result) {
            progress.dismiss();
        }
    }

    void showMessage(final String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("Tamam", null).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    void showMessageAndReload(final String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("Yeni İmza", (dialog, which) -> {
            finish();
            startActivity(getIntent());
        }).setNegativeButton("Çıkış", (dialog, which) -> {
            closeSmartCard();
            finish();
        }).setOnCancelListener(dialog -> {
            closeSmartCard();
            finish();
        }).setIcon(android.R.drawable.ic_dialog_info).show();
    }

    public class SignFileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                hata = null;

                Locale trLocale = new Locale("tr", "TR");
                Locale.setDefault(trLocale);
                I18nSettings.setLocale(trLocale);

                EditText pwdEdit = findViewById(R.id.txtPassword);

                String signingFileName = filePathView.getText().toString();
                if (signingFileName.isEmpty()) {
                    throw new Exception("Dosya seçiniz");
                }

                String pwdText = pwdEdit.getText().toString();
                if (pwdText.isEmpty()) {
                    throw new Exception("Parola giriniz");
                }

                ISignable content = new AndroidSignableFile(uri, resolver);

                if (!mAPDUSmartCard.isSessionActive()){
                    runOnUiThread(MainActivity.this::loadCertTree);
                }

                // todo PIN and/or PIN block status check
                mAPDUSmartCard.login(pwdText);

                Pair<CardTerminal, ECertificate> selection = certListAdapter.getSelection();
                ECertificate signCert = selection.second;

                BaseSigner signer;
                {
                    String signingAlgorithm;
                    if (SignatureAlg.fromAlgorithmIdentifier(signCert.getPublicKeyAlgorithm()).getObject1().getAsymmetricAlg() == AsymmetricAlg.ECDSA) {
                        signingAlgorithm = String.valueOf(ECAlgorithmUtil.getConvenientECSignatureAlgForECCertificate(signCert));
                    } else {
                        signingAlgorithm = Algorithms.SIGNATURE_RSA_SHA1;
                    }

                    Log.d(TAG, "signing algorithm is " + signingAlgorithm);

                    signer = mAPDUSmartCard.getSigner(signCert.asX509Certificate(), signingAlgorithm);
                }

                List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
                optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

                BaseSignedData bsd = new BaseSignedData();
                // fixme content.getContentData().length == 0
                bsd.addContent(content);
                bsd.addSigner(ESignatureType.TYPE_BES, signCert, signer, optionalAttributes, params);

                signedDocument = bsd.getEncoded();

            } catch (final Throwable ex) {
                handleException(ex);
            }

            progress.dismiss();

            publishProgress();

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            String signedFileName = fileName + ".imz";
            Intent saveIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT)
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("*/*")
                    .putExtra(Intent.EXTRA_TITLE, signedFileName);

            startActivityForResult(saveIntent, REQUEST_SAVE_FILE);
        }

        private void handleException(Throwable ex) {
            try {
                hata = ex.getMessage();
                if (hata == null || hata.isEmpty()) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    hata = sw.toString();
                }
            } catch (Throwable e) {
            }
        }
    }

    private void closeSmartCard() {
        try {
            mAPDUSmartCard.logout();
        } catch (Throwable e) {

        }

        try {
            mAPDUSmartCard.closeSession();
        } catch (Throwable e) {
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            certListAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };

    void loadCertTree() {
        LoadCertificatesTask loadCertificatesTask = new LoadCertificatesTask(this);
        loadCertificatesTask.execute();
    }

    CertListAdapter certListAdapter;
    ListView certTreeList;

    void initCertTreeView() {

        certTreeList = findViewById(R.id.lstCert);
        certTreeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        certListAdapter = new CertListAdapter(this);
        certTreeList.setAdapter(certListAdapter);
    }

    @Override
    protected void onDestroy() {
        if (mAPDUSmartCard != null) {
            try {
                mAPDUSmartCard.logout();
            } catch (Exception exc) {
            }

            try {
                mAPDUSmartCard.closeSession();
            } catch (Throwable exc) {
                exc.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public void loadLicense() {
        try {
            // Load license
            Resources res = getResources();
            
            InputStream lisansStream = res.openRawResource(R.raw.lisans);
            LicenseUtil.setLicenseXml(lisansStream);
            lisansStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLicense();
        initCertTreeView();

        loadCertTree();
        filePathView = findViewById(R.id.tvFilePath);

        Button btnSign = findViewById(R.id.btnSign);
        btnSign.setOnClickListener(
                view -> {
                    progress = new ProgressDialog(MainActivity.this);
                    progress.setMessage("İmzalama işlemi yapılıyor.\nLütfen Bekleyiniz...");
                    progress.show();

                    new SignFileTask().execute();
                });

        Button btnSelectFile = findViewById(R.id.btnSelectFile);
        btnSelectFile.requestFocus();
        btnSelectFile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT)//ACTION_OPEN_DOCUMENT
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("*/*");
            startActivityForResult(intent, REQUEST_PICK_FILE);
        });
    }

    public String getSigningFileNameFromUri(ContentResolver resolver, Uri uri) {
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_FILE) {
                uri = data.getData();
                resolver = getContentResolver();
                fileName = getSigningFileNameFromUri(resolver, uri);
                filePathView.setText(fileName);

                //String path = uri.getPath();
                //Toast.makeText(MainActivity.this, path, Toast.LENGTH_LONG).show();
                super.onActivityResult(requestCode, resultCode, data);
            }

            else if (requestCode == REQUEST_SAVE_FILE) {
                saveUri = data.getData();
                resolver = getContentResolver();
                OutputStream os;
                try {
                    os = resolver.openOutputStream(saveUri);
                    os.write(signedDocument);
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                progress.dismiss();

                // todo move under "return from file save"
                try {
                    if (hata != null) {
                        showMessage("Hata", hata);
                        hata = null;
                    } else {
                        showMessageAndReload("işlem başarılı", "İmzalama işlemi başarılı.");
                    }
                } catch (Throwable e) {
                }

                //Toast.makeText(MainActivity.this, saveUri.toString(), Toast.LENGTH_LONG).show();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
