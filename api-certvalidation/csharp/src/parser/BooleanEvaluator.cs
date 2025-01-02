using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.parser
{
    public interface BooleanEvaluator
    {
        bool evaluate(String expression);
    }
}
