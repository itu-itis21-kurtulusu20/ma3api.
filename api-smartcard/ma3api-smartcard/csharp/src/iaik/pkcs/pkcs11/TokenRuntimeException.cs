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
namespace iaik.pkcs.pkcs11
{
	
	
	
	/// <summary> The base class for all runtiem exceptions in this package. It is able to wrap
	/// a other exception from a lower layer.
	/// 
	/// </summary>
	/// <author>  <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
	/// </author>
	/// <version>  1.0
	/// </version>
	/// <invariants>  </invariants>
	[Serializable]
	public class TokenRuntimeException:System.SystemException
	{
		/// <summary> Get the encapsulated (wrapped) exceptin. May be null.
		/// 
		/// </summary>
		/// <returns> The encasulated (wrapped) exception, or null if there is no inner
		/// exception.
		/// </returns>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		virtual public System.Exception EncapsulatedException
		{
			get
			{
				return encapsulatedException_;
			}
			
		}
		
		/// <summary> An encapsulated (inner) exception. Possibly, an exception from a lower
		/// layer that ca be propagated to a higher layer only in wrapped form.
		/// </summary>
		protected internal System.Exception encapsulatedException_;
		
		/// <summary> The default constructor.
		/// 
		/// </summary>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public TokenRuntimeException():base()
		{
		}
		
		/// <summary> Constructor taking an exception message.
		/// 
		/// </summary>
		/// <param name="message">The message giving details about the exception to ease
		/// debugging.
		/// </param>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public TokenRuntimeException(System.String message):base(message)
		{
		}
		
		/// <summary> Constructor taking an other exception to wrap.
		/// 
		/// </summary>
		/// <param name="encapsulatedException">The other exception the wrap into this.
		/// </param>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public TokenRuntimeException(System.Exception encapsulatedException):base()
		{
			encapsulatedException_ = encapsulatedException;
		}
		
		/// <summary> Constructor taking a message for this exception and an other exception to
		/// wrap.
		/// 
		/// </summary>
		/// <param name="message">The message giving details about the exception to ease
		/// debugging.
		/// </param>
		/// <param name="encapsulatedException">The other exception the wrap into this.
		/// </param>
		/// <preconditions>  </preconditions>
		/// <postconditions>  </postconditions>
		public TokenRuntimeException(System.String message, System.Exception encapsulatedException):base(message)
		{
			encapsulatedException_ = encapsulatedException;
		}
		
		/// <summary> Returns the string representation of this exception, including the string
		/// representation of the wrapped (encapsulated) exception.
		/// 
		/// </summary>
		/// <returns> The string representation of exception.
		/// </returns>
		public override System.String ToString()
		{
			//UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
			System.Text.StringBuilder buffer = new System.Text.StringBuilder(base.ToString());
			
			if (encapsulatedException_ != null)
			{
				buffer.Append(", Encasulated Exception: ");
				//UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Throwable.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
				buffer.Append(encapsulatedException_.ToString());
			}
			
			return buffer.ToString();
		}
	}
}