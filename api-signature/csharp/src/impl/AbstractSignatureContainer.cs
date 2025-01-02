using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    /**
     * Base class for SignatureContainer implementations
     *
     * @author ayetgin
     */
    public abstract class AbstractSignatureContainer : SignatureContainerEx
    {
        protected ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        protected List<Signature> rootSignatures = new List<Signature>(1);
        protected Context context;
        protected SignaturePackage signaturePackage;

        public virtual void setContext(Context context)
        {
            this.context = context;
        }

        public Context getContext()
        {
            return context;
        }

        public virtual void addExternalSignature(Signature signature)
        {
            rootSignatures.Add(signature);
        }

        public void addSignature(Signature signature)
        {
            rootSignatures.Add(signature);
        }

        public List<Signature> getSignatures()
        {
            return rootSignatures;
        }

        public void setSignaturePackage(SignaturePackage signaturePackage)
        {
            this.signaturePackage = signaturePackage;
        }

        public SignaturePackage getPackage()
        {
            return signaturePackage;
        }

        public ContainerValidationResult verifyAll()
        {
            bool incompleteExists = false;
            bool invalidExists = false;
            ContainerValidationResultImpl cvr = new ContainerValidationResultImpl();
            foreach (Signature signature in rootSignatures)
            {
                try
                {
                    SignatureValidationResult svr = signature.verify();

                    cvr.addResult(signature, svr);

                    ValidationResultType subResult = getCounterSeriesType(svr);
                    if (subResult.Equals(ValidationResultType.INCOMPLETE))
                    {
                        incompleteExists = true;
                    }

                    if (subResult.Equals(ValidationResultType.INVALID))
                    {
                        invalidExists = true;
                    }

                }
                catch (Exception x)
                {
                    logger.Warn("Error in signature validation ", x);
                    invalidExists = true;
                }

            }
            if (invalidExists)
            {
                cvr.setResultType(ContainerValidationResultType.CONTAINS_INVALID);
            }
            else if (incompleteExists)
            {
                cvr.setResultType(ContainerValidationResultType.CONTAINS_INCOMPLETE);
            }
            else
            {
                cvr.setResultType(ContainerValidationResultType.ALL_VALID);
            }
            return cvr;
        }

        private ValidationResultType getCounterSeriesType(SignatureValidationResult svr)
        {
            ValidationResultType found = ValidationResultType.VALID;
            if (svr.getResultType() == ValidationResultType.INVALID)
                return ValidationResultType.INVALID;
            else if (svr.getResultType() == ValidationResultType.INCOMPLETE)
                found = ValidationResultType.INCOMPLETE;

            foreach (SignatureValidationResult csvr in svr.getCounterSignatureValidationResults())
            {
                ValidationResultType counter = getCounterSeriesType(csvr);
                if (counter == ValidationResultType.INVALID)
                    return ValidationResultType.INVALID;
                else if (counter == ValidationResultType.INCOMPLETE)
                    found = ValidationResultType.INCOMPLETE;
            }

            return found;
        }

        public abstract Signature createSignature(tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate certificate);

        public abstract SignatureFormat getSignatureFormat();

        public abstract bool isSignatureContainer(System.IO.Stream stream);

        public abstract void read(System.IO.Stream stream);

        public abstract void write(System.IO.Stream stream);

        public abstract object getUnderlyingObject();

        public abstract void detachSignature(Signature signature);
    }
}
