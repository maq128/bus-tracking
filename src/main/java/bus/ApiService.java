package bus;

import java.util.HashMap;
import java.util.Map;

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

	public Map<String, Object>[] getBuslines() {
		Map<String, Object>[] lines = (Map<String, Object>[])cacheService.get("lines");
		if (lines != null) return lines;

		lines = request("getBuslines");

		cacheService.put("lines", lines, 3600);
		return lines;
	}

	public Map<String, Object>[] monitorBus(boolean force) {
		if (!force) {
			Map<String, Object>[] locations = (Map<String, Object>[])cacheService.get("locations");
			if (locations != null) return locations;
		}

		Map<String, Object>[] locations = request("monitorBus");

		cacheService.put("locations", locations, 30);
		return locations;
	}

	private Map<String, Object>[] request(String name) {
		try {
			String url = ApiUtil.getFullURL(name);
			HttpGet get = new HttpGet(url);

			CloseableHttpClient client = HttpClientBuilder.create().build();
			CloseableHttpResponse response = client.execute(get);
			String bodyAsString = EntityUtils.toString(response.getEntity());
			response.close();

			Gson g = new Gson();
			ApiResponse resp = g.fromJson(bodyAsString, ApiResponse.class);
			if (resp.status == 0) {
				return resp.data;
			}

			return new HashMap[0];
		} catch (Exception e) {
			log.error(e.getMessage());
			return new HashMap[0];
		}
	}
}
