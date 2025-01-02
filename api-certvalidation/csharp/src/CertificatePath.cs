using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    public class CertificatePath
    {
        protected ValidationSystem mValidationSystem;

        protected CertificatePathNode mHead;
        protected CertificatePathNode mTail;
        protected List<List<EIssuerAndSerialNumber>> mFoundPaths = new List<List<EIssuerAndSerialNumber>>();

        private enum PathDirection
        {
            Forward,
            Backward
        };
        
        private PathDirection mDirection;
        private readonly List<List<ECertificate>> mIncompletePathList = new List<List<ECertificate>>();

        public CertificatePath(ValidationSystem aValidationSystem)
        {
            mValidationSystem = aValidationSystem;
        }

        public CertificatePath(ECertificate aCert, ValidationSystem aValidationSystem)
        {
            mValidationSystem = aValidationSystem;
            mHead = new CertificatePathNode(aCert, aValidationSystem);
            mTail = mHead;
            _initPath();
        }

        private void _initPath()
        {
            if (!_createNextNode())
            {
                mHead = null;
                mTail = null;
            }
            else
            {
                mFoundPaths.Add(getCertificateISs());
            }

        }

        /*private bool _createNextNode(CertificatePathNode aNode)
        {
            if (aNode == null)
                return false;

            // Guvenilir Listedeyse burada duruyoruz
            if (mValidationSystem.isTrustedCertificate(aNode.getSubject()))
                return true;

            while (aNode.iterateIssuer(mValidationSystem, getCertificates()))
            {
                CertificatePathNode newNode = new CertificatePathNode(aNode.getIssuer(), mValidationSystem);
                aNode.setNext(newNode);
                newNode.setPrevious(aNode);
                mTail = newNode;
                bool created = _createNextNode(newNode);
                if (created)
                    return true;
            }

            return false;
        }//*/

        /**
         * Zincirin sonuna verilen issueri ekler.
         * @param iIssuer
         */
        void _makeNext(ECertificate iIssuer)
        {
            CertificatePathNode newNode = new CertificatePathNode(iIssuer, mValidationSystem);
            mTail.setNext(newNode);
            newNode.setPrevious(mTail);
            mTail = newNode;
            mDirection = PathDirection.Forward;// Yönümü ileri çeviriyorum
        }

        /**
         * Zincir  üzerinde bir adım geri atar
         * @return
         */
        bool _stepBack()
        {
            CertificatePathNode pNode = mTail.previous();
            if (pNode == null)
                return false;
            pNode.setNext(null);
            mTail = null;
            mTail = pNode;
            mDirection = PathDirection.Backward;// Yönümü geri çeviriyorum
            return true;
        }

        private bool _createNextNode()
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
                bool b = _createNextNode();

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

        void _addToIncompletePathList(List<ECertificate> aPath)
        {
            if (aPath.Count != 0)
                mIncompletePathList.Add(aPath);
        }

        public List<List<ECertificate>> getIncompletePathList()
        {
            return mIncompletePathList;
        }

        /*public bool createNewPath()
        {
            if ((mTail != null) && (!mTail.Equals(mHead)))
            {
                CertificatePathNode pathNode = mTail.previous();
                if (pathNode == null) return false;
                pathNode.setNext(null);
                mTail = null;
                mTail = pathNode;
                while (!_createNextNode(pathNode))
                {
                    pathNode = mTail.previous();
                    if (pathNode == null) return false;
                    pathNode.setNext(null);
                    mTail = null;
                    mTail = pathNode;
                }
                // if found before retry
                List<EIssuerAndSerialNumber> foundPath = getCertificateISs();
                if (!isFoundBefore(foundPath))
                {
                    mFoundPaths.Add(foundPath);
                    return true;
                }
                else return createNewPath();

            }
            return false;
        }//*/

        public bool createNewPath()
        {
            if ((mTail != null) && (!mTail.Equals(mHead))) {

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
                    mFoundPaths.Add(foundPath);
                    return true;
                }
                else
                    return createNewPath();// Daha önce bulunmuş bir path. Tekrar dene.

            }
            return false;
        }
        
        public override String ToString()
        {
            StringBuilder buffer = new StringBuilder("-- CERTIFICATE PATH --\n");

            CertificatePathNode pNode = mTail;

            while (pNode != null)
            {
                buffer.Append(pNode.getSubject().getSubject().stringValue()).Append("\n");
                pNode = pNode.previous();
            }

            return buffer.ToString();
        }

        public List<ECertificate> getCertificates()
        {
            List<ECertificate> certList = new List<ECertificate>();

            CertificatePathNode pNode = mTail;

            while (pNode != null)
            {
                certList.Add(pNode.getSubject());
                pNode = pNode.previous();
            }

            return certList;
        }

        public List<EIssuerAndSerialNumber> getCertificateISs()
        {
            List<EIssuerAndSerialNumber> certList = new List<EIssuerAndSerialNumber>();

            CertificatePathNode pNode = mTail;

            while (pNode != null)
            {
                certList.Add(new EIssuerAndSerialNumber(pNode.getSubject()));
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
            while ((tmp != null) && (tmp != mTail))
            {
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
        bool isFoundBefore(List<EIssuerAndSerialNumber> aPath)
        {
            //return mFoundPaths.Contains(aPath);
            foreach (List<EIssuerAndSerialNumber> path in mFoundPaths)
            {
                if (aPath.Count == path.Count)
                {
                    for (int i = 0; i < aPath.Count; i++)
                    {
                        if (!path[i].Equals(aPath[i]))
                        {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

    }
}
