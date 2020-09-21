package bus;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class BusController {
	@GetMapping("/")
	public ModelAndView index(@Nullable @RequestParam Long did) throws Exception {
		ModelAndView mv = new ModelAndView("index");

		try {
			String url = ApiUtil.getFullURL("getBuslines");
			HttpGet get = new HttpGet(url);

			CloseableHttpClient client = HttpClientBuilder.create().build();
			CloseableHttpResponse response = client.execute(get);
			String bodyAsString = EntityUtils.toString(response.getEntity());
			response.close();
			mv.addObject("buses", bodyAsString);

			if (did != null) {
				mv.addObject("did", did.longValue());
			}

			url = ApiUtil.getFullURL("monitorBus");
			get = new HttpGet(url);

			client = HttpClientBuilder.create().build();
			response = client.execute(get);
			bodyAsString = EntityUtils.toString(response.getEntity());
			response.close();
			mv.addObject("locations", bodyAsString);
		} catch (Exception e) {
			mv.addObject("errorMsg", e.getMessage());
		}

		return mv;
	}
}
