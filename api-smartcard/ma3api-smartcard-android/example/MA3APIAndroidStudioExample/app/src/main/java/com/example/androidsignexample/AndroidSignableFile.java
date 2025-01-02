package com.example.androidsignexample;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

public class AndroidSignableFile implements ISignable {
    private Uri uri;
    private ContentResolver resolver;

    public AndroidSignableFile(Uri uri, ContentResolver resolver){
        this.uri = uri;
        this.resolver = resolver;
    }

    @Override
    public byte[] getContentData() {
        try {
            InputStream inputStream = resolver.openInputStream(uri);
            return StreamUtil.readAll(inputStream);
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public byte[] getMessageDigest(DigestAlg digestAlg) throws CryptoException, IOException {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public InputStream getAsInputStream() throws IOException {
        return resolver.openInputStream(this.uri);
    }
}
