using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.CompilerServices;

namespace tr.gov.tubitak.uekae.esya.api.common.tools
{
    public class CombinedInputStream : Stream
    {
        private readonly List<Stream> _mStreams = new List<Stream>();
        private int _mStreamIndex;
        

        public void addInputStream(Stream aInputStream)
        {
            _mStreams.Add(aInputStream);
        }

        //@Override
        [MethodImpl(MethodImplOptions.Synchronized)]
        public override /*synchronized*/ int ReadByte()
        {
            while (_mStreamIndex < _mStreams.Count)
            {
                int value = _mStreams[_mStreamIndex].ReadByte();
                if (value == -1)
                    _mStreamIndex++;
                else
                    return value;
            }
            return -1;
        }


        //DotNet standardında stream sonunda 0 dönülüyor.
        [MethodImpl(MethodImplOptions.Synchronized)]
        public override int Read(byte[] b, int off, int len)
        {
            if (b == null)
            {
                throw new NullReferenceException();
            }
            if (off < 0 || len < 0 || len > b.Length - off)
            {
                throw new IndexOutOfRangeException();
            }
            if (len == 0)
            {
                return 0;
            }

            int total = 0;
            while (_mStreamIndex < _mStreams.Count && len > 0)
            {
                int i = _mStreams[_mStreamIndex].Read(b, off, len);
                if (i > 0)
                {
                    off = off + i;
                    len = len - i;
                    total = total + i;
                }
                else
                    _mStreamIndex++;
            }
            return total;
        }

        public override bool CanRead
        {
            get { throw new NotImplementedException(); }
        }

        public override bool CanSeek
        {
            get { throw new NotImplementedException(); }
        }

        public override bool CanWrite
        {
            get { throw new NotImplementedException(); }
        }

        public override void Flush()
        {
            throw new NotImplementedException();
        }

        public override long Length
        {
            get { throw new NotImplementedException(); }
        }

        public override long Position
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public override long Seek(long offset, SeekOrigin origin)
        {
            throw new NotImplementedException();
        }

        public override void SetLength(long value)
        {
            throw new NotImplementedException();
        }

        public override void Write(byte[] buffer, int offset, int count)
        {
            throw new NotImplementedException();
        }
    }
}
