/* Copyright  (c) 2002 Graz University of Technology. All rights reserved.
*
* Redistribution and use in  source and binary forms, with or without 
* modification, are permitted  provided that the following conditions are met:
*
* 1. Redistributions of  source code must retain the above copyright notice,
*    this list of conditions and the following disclaimer.
*
* 2. Redistributions in  binary form must reproduce the above copyright notice,
*    this list of conditions and the following disclaimer in the documentation
*    and/or other materials provided with the distribution.
*  
* 3. The end-user documentation included with the redistribution, if any, must
*    include the following acknowledgment:
* 
*    "This product includes software developed by IAIK of Graz University of
*     Technology."
* 
*    Alternately, this acknowledgment may appear in the software itself, if 
*    and wherever such third-party acknowledgments normally appear.
*  
* 4. The names "Graz University of Technology" and "IAIK of Graz University of
*    Technology" must not be used to endorse or promote products derived from 
*    this software without prior written permission.
*  
* 5. Products derived from this software may not be called 
*    "IAIK PKCS Wrapper", nor may "IAIK" appear in their name, without prior 
*    written permission of Graz University of Technology.
*  
*  THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
*  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
*  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
*  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE LICENSOR BE
*  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
*  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
*  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
*  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
*  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
*  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
*  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*  POSSIBILITY  OF SUCH DAMAGE.
*/

using System;
using System.Globalization;
using System.Reflection;
using System.Resources;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace iaik.pkcs.pkcs11.wrapper
{
	
	
	
	/// <summary> This is the superclass of all checked exceptions used by this package. An
	/// exception of this class indicates that a function call to the underlying
	/// PKCS#11 module returned a value not equal to CKR_OK. The application can get
	/// the returned value by calling getErrorCode(). A return value not equal to
	/// CKR_OK is the only reason for such an exception to be thrown.
	/// PKCS#11 defines the meaning of an error-code, which may depend on the
	/// context in which the error occurs.
	/// 
	/// </summary>
	/// <author>  <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
	/// </author>
	/// <version>  1.0
	/// </version>
	/// <invariants>  </invariants>
	[Serializable]
	public class PKCS11Exception:TokenException
    {

        /// <summary> The name of the properties file that holds the names of the PKCS#11 error codes. </summary>
        //protected internal const System.String ERROR_CODE_PROPERTIES = "iaik/pkcs/pkcs11/wrapper/ExceptionMessages.properties";
        
        protected internal readonly System.String ERROR_CODE_PROPERTIES = "tr.gov.tubitak.uekae.esya.api.smartcard.Properties.Resource";

        /// <summary> The properties object that holds the mapping from error-code to the name of the PKCS#11 error. </summary>
        //UPGRADE_ISSUE: Class hierarchy differences between 'java.util.Properties' and 'System.Collections.Specialized.NameValueCollection' may cause compilation errors. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1186'"
        protected internal System.Resources.ResourceManager errorCodeNames_;

        /// <summary> True, if the mapping of error codes to PKCS#11 error names is available.</summary>
        protected internal bool errorCodeNamesAvailable_;

        /// <summary> The code of the error which was the reason for this exception.</summary>
        protected internal long errorCode_;

        private readonly object _lockObj = new object();


        /// <summary> This method gets the corresponding text error message from
        /// a property file. If this file is not available, it returns the error
        /// code as a hex-string.
        /// 
        /// </summary>
        /// <returns> The message or the error code; e.g. "CKR_DEVICE_ERROR" or
        /// "0x00000030".
        /// </returns>
        /// <preconditions>  </preconditions>
        /// <postconditions>  (result <> null) </postconditions>
        public override System.String Message
		{
			get
            {
                // if the names of the defined error codes are not yet loaded, load them
                if (errorCodeNames_ == null)
				{
					lock (_lockObj)
					{
						if (errorCodeNames_ == null)
						{
							// ensure that another thread has not loaded the codes meanwhile														
							try
							{								                             
                                errorCodeNames_ = new ResourceManager(ERROR_CODE_PROPERTIES, Assembly.GetExecutingAssembly());
								errorCodeNamesAvailable_ = true;
							}
							catch (System.Exception exception)
							{
								//UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.getMessage' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
								System.Console.Error.WriteLine("Could not read properties for error code names: " + exception.Message + ", type: " + exception.GetType());
							}
						}
					}
				}
				
				// if we can get the name of the error code, take the name, otherwise return the code
				System.String errorCodeHexString = "0x" + Functions.toFullHexString((int) errorCode_);                
                System.String errorCodeName = null;
                try
                {
                    if (errorCodeNames_ != null)
                        errorCodeName = errorCodeNamesAvailable_ ? errorCodeNames_.GetString(errorCodeHexString, I18nSettings.getLocale()) : null;
                } 
                catch (Exception exception)
                {
                    System.Console.Error.WriteLine("Could not read key:{0}  " + exception.Message + ", type: " + exception.GetType(), errorCodeHexString);
                }
				System.String message = errorCodeName ?? errorCodeHexString;
				
				return message;
			}
			
		}
		/// <summary> Returns the PKCS#11 error code.
		/// 
		/// </summary>
		/// <returns> The error code; e.g. 0x00000030.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public virtual long ErrorCode
		{
			get
			{
				return errorCode_;
			}
			
		}
		
	
		/// <summary> Constructor taking the error code as defined for the CKR_* constants
		/// in PKCS#11.
		/// 
		/// </summary>
		/// <param name="errorCode">The PKCS#11 error code (return value).
		/// </param>
		public PKCS11Exception(long errorCode)
		{
			errorCode_ = errorCode;
		}
	}
}