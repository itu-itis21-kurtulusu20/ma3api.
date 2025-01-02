package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

public class CertificatePath
{
    protected ValidationSystem mValidationSystem;

    protected CertificatePathNode mHead;
    protected CertificatePathNode mTail;
    protected List<List<EIssuerAndSerialNumber>> mFoundPaths = new ArrayList<List<EIssuerAndSerialNumber>>();

    enum PathDirection {Forward, Backward};

    PathDirection mDirection;

    List<List<ECertificate>> mIncompletePathList = new ArrayList<List<ECertificate>>();

    public CertificatePath(ValidationSystem aValidationSystem)
    {
        mValidationSystem = aValidationSystem;
    }

    public CertificatePath(ECertificate aCert, ValidationSystem aValidationSystem) throws ESYAException
    {
        mValidationSystem = aValidationSystem;
        mHead = new CertificatePathNode(aCert, aValidationSystem);
        mTail = mHead;
        _initPath();
    }

    private void _initPath() throws ESYAException
    {
        if (!_createNextNode()) {
            mHead = null;
            mTail = null;
        }
        else {
            mFoundPaths.add(getCertificateISs());
        }

    }
    /*
    private boolean _createNextNode(CertificatePathNode aNode) throws ESYAException
    {
        if (aNode == null)
            return false;

        // Guvenilir Listedeyse burada duruyoruz
        if (mValidationSystem.isTrustedCertificate(aNode.getSubject()))
            return true;

        while (aNode.iterateIssuer(mValidationSystem, getCertificates())) {
            CertificatePathNode newNode = new CertificatePathNode(aNode.getIssuer(), mValidationSystem);
            aNode.setNext(newNode);
            newNode.setPrevious(aNode);
            mTail = newNode;
            boolean created = _createNextNode(newNode);
            if (created)
                return true;
        }

        return false;
    } */

    /**
     * Zincirin sonuna verilen issueri ekler.
     * @param iIssuer
     */
    void _makeNext(ECertificate iIssuer)
    {
        CertificatePathNode newNode= new CertificatePathNode(iIssuer, mValidationSystem);
        mTail.setNext(newNode);
        newNode.setPrevious(mTail);
        mTail = newNode;
        mDirection = PathDirection.Forward;// Yönümü ileri çeviriyorum
    }

    /**
     * Zincir  üzerinde bir adım geri atar
     * @return
     */
    boolean _stepBack()
    {
        CertificatePathNode pNode= mTail.previous();
        if (pNode==null)
            return false;
        pNode.setNext(null);
        mTail = null;
        mTail = pNode;
        mDirection = PathDirection.Backward;// Yönümü geri çeviriyorum
        return true;
    }

    private boolean _createNextNode() throws ESYAException
    {
        if (mTail==null)
            return false;

        // Guvenilir Listedeyse burada duruyoruz
        if (mValidationSystem.isTrustedCertificate(mTail.getSubject()))
            return true;

        List<ECertificate> currentPath = getCertificates();

        while (mTail.iterateIssuer(mValidationSystem, getCertificates()))
        {
            _makeNext(mTail.getIssuer());
            boolean b = _createNextNode();

            if (b)
                return true;
            else // Bu issuer dan bir yere varamadık. Onu silip başka bir issuer a geçiyoruz.
            {
                _stepBack();
            }
        }
        if (mDirection == PathDirection.Forward)    // ileri gidiyorken durduysam bu pathi kaydediyorum.
        {
            _addToIncompletePathList(currentPath);
        }
        return false;
    }

    void _addToIncompletePathList(List<ECertificate> aPath )
    {
        if (!aPath.isEmpty())
            mIncompletePathList.add(aPath);
    }

    public List<List<ECertificate>> getIncompletePathList()
    {
        return mIncompletePathList ;
    }

    public boolean createNewPath() throws ESYAException
    {
        if ((mTail != null) && (!mTail.equals(mHead))) {

            if (!_stepBack())// Buraya düşmememiz gerekir ama kontrol edelim.
                return false;

            while (!_createNextNode()) // Geçerli path bulana kadar geriye dönüyoruz
            {
                if (!_stepBack()) // En başa döndük demektir. Yapacak bir şey yok.
                    return false;
            }

            List<EIssuerAndSerialNumber> foundPath = getCertificateISs();
            if (!isFoundBefore(foundPath))
            {
                mFoundPaths.add(foundPath);
                return true;
            }
            else
                return createNewPath();// Daha önce bulunmuş bir path. Tekrar dene.

        }
        return false;
    }

    public String toString()
    {
        StringBuilder buffer = new StringBuilder("-- CERTIFICATE PATH --\n");

        CertificatePathNode pNode = mTail;

        while (pNode != null) {
            buffer.append(pNode.getSubject().getSubject().stringValue()).append("\n");
            pNode = pNode.previous();
        }

        return buffer.toString();
    }

    public List<ECertificate> getCertificates()
    {
        List<ECertificate> certList = new ArrayList<ECertificate>();

        CertificatePathNode pNode = mTail;

        while (pNode != null) {
            certList.add(pNode.getSubject());
            pNode = pNode.previous();
        }

        return certList;
    }

    public List<EIssuerAndSerialNumber> getCertificateISs()
    {
        List<EIssuerAndSerialNumber> certList = new ArrayList<EIssuerAndSerialNumber>();

        CertificatePathNode pNode = mTail;

        while (pNode != null) {
            certList.add(new EIssuerAndSerialNumber(pNode.getSubject()));
            pNode = pNode.previous();
        }

        return certList;
    }

    public int pathLength()
    {
        if (mHead == null)
            return 0;
        if (mTail == null)
            return -1; // invalid path

        CertificatePathNode tmp = mHead;
        int n = 1;
        while ((tmp != null) && (tmp != mTail)) {
            n++;
            tmp = tmp.next();
        }
        if (tmp != null) return n;
        else return -1;// invalid path
    }

    public CertificatePathNode getHead()
    {
        return mHead;
    }

    public CertificatePathNode getTail()
    {
        return mTail;
    }

    boolean isFoundBefore(List<EIssuerAndSerialNumber> aPath)
    {
        return mFoundPaths.contains(aPath);
    }

}
