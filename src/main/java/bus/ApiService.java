package bus;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class ApiService {

	public static String getBuslines() throws Exception {
		String url = ApiUtil.getFullURL("getBuslines");
		HttpGet get = new HttpGet(url);

		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = client.execute(get);
		String bodyAsString = EntityUtils.toString(response.getEntity());
		response.close();
		return bodyAsString;
	}

	public static String monitorBus() throws Exception {
		String url = ApiUtil.getFullURL("monitorBus");
		HttpGet get = new HttpGet(url);

		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = client.execute(get);
		String bodyAsString = EntityUtils.toString(response.getEntity());
		response.close();
		return bodyAsString;
	}

}
