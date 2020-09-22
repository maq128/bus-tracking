package bus;

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
		Map<String, Object>[] buses = (Map<String, Object>[])cacheService.get("buses");
		if (buses != null) return buses;

		buses = request("getBuslines");

		cacheService.put("buses", buses, 3600);
		return buses;
	}

	public Map<String, Object>[] monitorBus() {
		return request("monitorBus");
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

			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
