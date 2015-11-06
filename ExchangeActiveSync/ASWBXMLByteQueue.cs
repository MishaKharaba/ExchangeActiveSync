using System.Collections.Generic;
using System.Text;

namespace ExchangeActiveSync
{
    // This class extends the .NET Queue<byte> class
    // to add some WBXML-specific functionality.
    class ASWBXMLByteQueue : Queue<byte>
    {
        public ASWBXMLByteQueue(byte[] bytes)
            : base(bytes)
        {
        }

        // This function will pop a multi-byte integer
        // (as specified in WBXML) from the WBXML stream. 
        public int DequeueMultibyteInt()
        {
            int returnValue = 0;
            byte singleByte = 0xFF;

            do
            {
                returnValue <<= 7;
                
                singleByte = this.Dequeue();
                returnValue += (int)(singleByte & 0x7F);
            }
            while (CheckContinuationBit(singleByte));

            return returnValue;
        }

        // This function checks a byte to see if the continuation
        // bit is set. This is used in deciphering multi-byte integers
        // in a WBXML stream.
        private bool CheckContinuationBit(byte byteValue)
        {
            byte continuationBitmask = 0x80;
            return (continuationBitmask & byteValue) != 0;
        }

        // This function pops a string from the WBXML stream.
        // It will read from the stream until a null byte is found.
        public string DequeueString()
        {
            StringBuilder returnStringBuilder = new StringBuilder();
            byte currentByte = 0x00;
            do
            {
                // TODO: Improve this handling. We are technically UTF-8, meaning
                // that characters could be more than one byte long. This will fail if we have
                // characters outside of the US-ASCII range
                currentByte = this.Dequeue();
                if (currentByte != 0x00)
                {
                    returnStringBuilder.Append((char)currentByte);
                }
            }
            while (currentByte != 0x00);

            return returnStringBuilder.ToString();
        }

        // This function pops a string of the specified length from
        // the WBXML stream.
        public string DequeueString(int length)
        {
            StringBuilder returnStringBuilder = new StringBuilder();
           
            byte currentByte = 0x00;
            for (int i = 0; i < length; i++)
            {
                // TODO: Improve this handling. We are technically UTF-8, meaning
                // that characters could be more than one byte long. This will fail if we have
                // characters outside of the US-ASCII range
                currentByte = this.Dequeue();

                if ((char)currentByte == '\n')
                    returnStringBuilder.AppendLine();
                else
                    returnStringBuilder.Append((char)currentByte);
            }

            return returnStringBuilder.ToString();
        }

        // This function dequeues a byte array of the specified length
        // from the WBXML stream.
        public byte[] DequeueBinary(int length)
        {
            List<byte> returnBytes = new List<byte>();

            for (int i = 0; i < length; i++)
            {
                returnBytes.Add(this.Dequeue());
            }

            return returnBytes.ToArray();
        }
    }
}
