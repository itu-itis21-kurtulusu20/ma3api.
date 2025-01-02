package tr.gov.tubitak.uekae.esya.api.infra.util;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.infra.cache.FixedSizedCache;

import java.time.Duration;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class HashMultiMapTest {

    @Test
    public void testHashMultiMap() {
        HashMultiMap<String, Integer> values = new HashMultiMap<String, Integer>();
        values.put("1", 1);
        values.put("1", 10);
        values.put("2", 2);

        List<Integer> ones = values.get("1");

        assertEquals(ones.get(0).intValue(), 1);
        assertEquals(ones.get(1).intValue(), 10);

        List<Integer> twos = values.get("2");

        assertEquals(twos.get(0).intValue(), 2);
    }
}
