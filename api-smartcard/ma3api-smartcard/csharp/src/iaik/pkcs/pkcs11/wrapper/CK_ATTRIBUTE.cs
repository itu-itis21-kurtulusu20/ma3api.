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
namespace iaik.pkcs.pkcs11.wrapper
{
	
	
	
	/// <summary> class CK_ATTRIBUTE includes the type, value and length of an attribute.<p>
	/// <B>PKCS#11 structure:</B>
	/// <PRE>
	/// typedef struct CK_ATTRIBUTE {&nbsp;&nbsp;
	/// CK_ATTRIBUTE_TYPE type;&nbsp;&nbsp;
	/// CK_VOID_PTR pValue;&nbsp;&nbsp;
	/// CK_ULONG ulValueLen;
	/// } CK_ATTRIBUTE;
	/// </PRE>
	/// 
	/// </summary>
	/// <author>  Karl Scheibelhofer <Karl.Scheibelhofer@iaik.at>
	/// </author>
	/// <author>  Martin Schläffer <schlaeff@sbox.tugraz.at>
	/// </author>
	public class CK_ATTRIBUTE : System.ICloneable
	{
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_ATTRIBUTE_TYPE type;
		/// </PRE>
		/// </summary>
		public long type;
		
		/// <summary> <B>PKCS#11:</B>
		/// <PRE>
		/// CK_VOID_PTR pValue;
		/// CK_ULONG ulValueLen;
		/// </PRE>
		/// </summary>
		public System.Object pValue;
		
		/// <summary> Create a (deep) clone of this object.
		/// 
		/// </summary>
		/// <returns> A clone of this object.
		/// </returns>
		public virtual System.Object Clone()
		{
			CK_ATTRIBUTE clone;
			
			try
			{
				clone = (CK_ATTRIBUTE) base.MemberwiseClone();
				
				// if possible, make a deep clone
				if (clone.pValue is byte[])
				{
					clone.pValue = new System.Object[((byte[]) this.pValue).Length];
					((byte[]) this.pValue).CopyTo((byte[]) clone.pValue, 0);
				}
				else if (clone.pValue is char[])
				{
					clone.pValue = new System.Object[((char[]) this.pValue).Length];
					((char[]) this.pValue).CopyTo((char[]) clone.pValue, 0);
				}
				else if (clone.pValue is CK_DATE)
				{
					clone.pValue = ((CK_DATE) this.pValue).Clone();
				}
				else if (clone.pValue is bool[])
				{
					clone.pValue = new System.Object[((bool[]) this.pValue).Length];
					((bool[]) this.pValue).CopyTo((bool[]) clone.pValue, 0);
				}
				else if (clone.pValue is int[])
				{
					clone.pValue = new System.Object[((int[]) this.pValue).Length];
					((int[]) this.pValue).CopyTo((int[]) clone.pValue, 0);
				}
				else if (clone.pValue is long[])
				{
					clone.pValue = new System.Object[((long[]) this.pValue).Length];
					((long[]) this.pValue).CopyTo((long[]) clone.pValue, 0);
				}
				else if (clone.pValue is System.Object[])
				{
					clone.pValue = new System.Object[((System.Object[]) this.pValue).Length];
					((System.Object[]) this.pValue).CopyTo((System.Object[]) clone.pValue, 0);
				}
				else
				{
					// the other supported objecty types: Boolean, Long, Byte, ... are immutable, no clone needed
					clone.pValue = this.pValue;
				}
			}
			//UPGRADE_NOTE: Exception 'java.lang.CloneNotSupportedException' was converted to 'System.Exception' which has different behavior. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1100'"
			catch (System.Exception ex)
			{
				// this must not happen, because this class is cloneable
				throw new PKCS11RuntimeException("An unexpected clone exception occurred.", ex);
			}
			
			return clone;
		}
		
		/// <summary> Returns the string representation of CK_ATTRIBUTE.
		/// 
		/// </summary>
		/// <returns> the string representation of CK_ATTRIBUTE
		/// </returns>
		public override System.String ToString()
		{
			System.Text.StringBuilder buffer = new System.Text.StringBuilder();
			
			buffer.Append(Constants.INDENT);
			buffer.Append("type: ");
			buffer.Append(type);
			buffer.Append(Constants.NEWLINE);
			
			buffer.Append(Constants.INDENT);
			buffer.Append("pValue: ");
			//UPGRADE_TODO: The equivalent in .NET for method 'java.lang.Object.toString' may return a different value. "ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?index='!DefaultContextWindowIndex'&keyword='jlca1043'"
			buffer.Append(pValue.ToString());
			//buffer.append(Constants.NEWLINE);
			
			return buffer.ToString();
		}
	}
}