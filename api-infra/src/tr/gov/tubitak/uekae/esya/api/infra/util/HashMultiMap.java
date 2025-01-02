package tr.gov.tubitak.uekae.esya.api.infra.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMultiMap<K,V> 
{
	HashMap<K, List<V>> map;
	
	public HashMultiMap()
	{
		map = new HashMap<K, List<V>>();
	}
	
	public void put(K key, V object)
	{
		List<V> list = map.get(key);
		if(list == null)
		{
			list = new ArrayList<V>();
			map.put(key, list);
		}
		list.add(object);
	}
	
	public List<V> get(Object key)
	{
		return map.get(key);
	}
	
}
