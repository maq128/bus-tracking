package bus;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiService {
	@Autowired
	CacheService cacheService;

	@NotNull
	public Object getBuslines() {
		String cacheKey = "lines";
		Object value = cacheService.get(cacheKey);
		if (value != null) return value;

		value = request("getBuslines");

		if (value == null) {
			return new Object();
		}
		cacheService.put(cacheKey, value, 3600);
		return value;
	}

	@NotNull
	public Object monitorBus(boolean force) {
		String cacheKey = "locations";
		if (!force) {
			Object value = cacheService.get(cacheKey);
			if (value != null) return value;
		}

		Object value = request("monitorBus");

		if (value == null) {
			return new Object();
		}
		cacheService.put(cacheKey, value, 30);
		return value;
	}

	@NotNull
	public Object lineDetail(long id) {
		String cacheKey = "detail_" + String.valueOf(id);
		Object value = cacheService.get(cacheKey);
		if (value != null) return value;

		value = request("lineDetail", id);

		if (value == null) {
			return new Object();
		}
		cacheService.put(cacheKey, value, 3600);
		return value;
	}

	private Object request(String name) {
		return request(name, null);
	}

	private Object request(String name, long id) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));
		return request(name, paramMap);
	}

	private Object request(String name, Map<String, String> paramMap) {
		try {
			String url = ApiUtil.getFullURL(name, paramMap);
			HttpGet get = new HttpGet(url);

			CloseableHttpClient client = HttpClientBuilder.create().build();
			CloseableHttpResponse response = client.execute(get);
			String bodyAsString = EntityUtils.toString(response.getEntity());
			response.close();

			Gson g = new Gson();
			Object resp = g.fromJson(bodyAsString, Object.class);

			if (!(resp instanceof Map)) {
				return null;
			}
			Map<String, Object> respMap = (Map<String, Object>)resp;
			Object status = respMap.get("status");
			if (status == null || !(status instanceof Number)) {
				return null;
			}
			Number statusNumber = (Number)status;
			if (statusNumber.longValue() != 0) {
				return null;
			}
			return respMap.get("data");
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
