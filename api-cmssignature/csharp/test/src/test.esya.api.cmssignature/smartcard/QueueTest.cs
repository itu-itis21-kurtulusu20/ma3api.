using NUnit.Framework;
using System;
using System.Collections.Generic;

namespace test.esya.api.cmssignature.smartcard
{
    public class QueueTest
    {
        [Test]
        public void testQueueOverSize()
        {
            Queue<long> numberQueue = new Queue<long>(2);
            numberQueue.Enqueue(1);
            numberQueue.Enqueue(2);
            numberQueue.Enqueue(3);
        }


        [Test]
        public void testQueueOverPool()
        {
            Queue<long> numberQueue = new Queue<long>(2);
            numberQueue.Enqueue(1);

            Console.WriteLine(numberQueue.Count);
            numberQueue.Dequeue();


            Console.WriteLine(numberQueue.Count);
            Assert.Throws<InvalidOperationException>(()=>
            {
                numberQueue.Dequeue();
            });

        }
    }
}
