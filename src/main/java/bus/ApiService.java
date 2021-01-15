package bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiService {
	final static String prefix = "http://jfzg.21vianet.com/xingzheng/banche/line/";

	@Autowired
	CacheService cacheService;

	@SuppressWarnings("unchecked")
	@NonNull
	public List<Object> getBuslines() {
		String cacheKey = "lines";
		Object value = cacheService.get(cacheKey);
		if (value instanceof List) return (List<Object>)value;

		value = request("getRealBancheLineList", null);

		if (!(value instanceof List)) {
			return new ArrayList<Object>();
		}
		cacheService.put(cacheKey, value, 3600);
		return (List<Object>)value;
	}

	@SuppressWarnings("unchecked")
	@NonNull
	public Map<String, Object> lineDetail(long id) {
		String cacheKey = "detail_" + String.valueOf(id);
		Object value = cacheService.get(cacheKey);
		if (value instanceof Map) return (Map<String, Object>)value;

		value = request("getRealBancheLineDetail", id);

		if (!(value instanceof Map)) {
			return new HashMap<String, Object>();
		}
		cacheService.put(cacheKey, value, 3600);
		return (Map<String, Object>)value;
	}

	private Object request(String name, Long id) {
		try {
			String url = prefix + name;

			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", "application/json;charset=UTF-8");
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (id != null) {
				parameters.put("id", id);
			}
			Gson g = new Gson();
			StringEntity stringEntity = new StringEntity(g.toJson(parameters), "UTF-8");
            post.setEntity(stringEntity);

			CloseableHttpClient client = HttpClientBuilder.create().build();
			CloseableHttpResponse response = client.execute(post);
			String bodyAsString = EntityUtils.toString(response.getEntity());
			response.close();
			client.close();

			Object resp = g.fromJson(bodyAsString, Object.class);
			if (!(resp instanceof Map)) {
				return null;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> respMap = (Map<String, Object>)resp;

			Object message = respMap.get("message");
			if (message == null || !(message instanceof String) || !"success".equals(message)) {
				return null;
			}

			return respMap.get("result");
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
