using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.signature.certval.impl;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    public class ValidationInfoResolverFromCertStore : ValidationInfoResolver
    {
        public ValidationInfoResolverFromCertStore()
        {

            addCertificateResolvers(new ReferencedCertFinderFromCertStore());
            addCrlResolvers(new ReferencedCRLFinderFromCertStore());
            addOcspResolvers(new ReferencedOCSPResponseFinderFromCertStore());
        }
    }
}
