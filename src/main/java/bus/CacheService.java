package bus;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class CacheService {

	ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

	public Object get(String name) {
		Object value = cache.get(name + "_expire");
		if (value != null && value instanceof Long) {
			long expire = ((Long)value).longValue();
			if (expire < System.currentTimeMillis()) {
				return null;
			}
		}

		value = cache.get(name);
		return value;
	}

	public void put(String name, Object value, long ttl) {
		cache.put(name, value);
		cache.put(name + "_expire", System.currentTimeMillis() + ttl * 1000);
	}

}
