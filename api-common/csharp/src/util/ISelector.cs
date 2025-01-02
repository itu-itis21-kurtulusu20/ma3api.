using System;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public interface ISelector
    {
        /// <summary>
        /// You can provide your selector implementations by this interface. It is mainly needed when multiple smartcards are connected to system.
        /// </summary>
        /// <param name="description">Describes why this selection needed.</param>
        /// <param name="inputs">Input list</param>
        /// <returns>Selected item index value. 
        ///  -1 for cancellation.
        /// </returns>

        int Select(string description, string[] inputs);
    }
}