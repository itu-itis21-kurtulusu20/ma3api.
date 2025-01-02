using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NUnit.Framework;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    public class FileTest
    {
        [Test]
        public void testi()
        {
            FileInfo first = new FileInfo("E:/deneme/first.txt");

            if(first.Exists)
                Console.WriteLine("first exists");
            else
                Console.WriteLine("first does not exist");

            Console.WriteLine(first.FullName);

            FileInfo second = new FileInfo("E:/deneme/second.txt");

            if (second.Exists)
                Console.WriteLine("second exists");
            else
                Console.WriteLine("second does not exist");

            first.MoveTo("E:/deneme/first_moved.txt");

            /*if (first.Exists)
                Console.WriteLine("first moved exists");
            else
                Console.WriteLine("first moved does not exist");*/

            FileInfo first_moved = new FileInfo("E:/deneme/first_moved.txt");

            if (first_moved.Exists)
                Console.WriteLine("first moved exists");
            else
                Console.WriteLine("first moved does not exist");

            Console.WriteLine(first_moved.FullName);

            //String fullname = file.FullName;
            /*DirectoryInfo directory = file.Directory;
            DirectoryInfo newDirectory = directory.Parent;*/

        }
    }
}
