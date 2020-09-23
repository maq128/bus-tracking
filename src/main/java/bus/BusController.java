package bus;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@RestController
public class BusController {
	@Autowired
	ApiService apiService;

	@GetMapping("/")
	public ModelAndView index(@Nullable @RequestParam Long did) {
		ModelAndView mv = new ModelAndView("index");

		try {
			if (did != null) {
				mv.addObject("did", did.longValue());
			}

			mv.addObject("lines", apiService.getBuslines());
			mv.addObject("locations", apiService.monitorBus(false));
		} catch (Exception e) {
			mv.addObject("errorMsg", e.getMessage());
		}

		return mv;
	}

	@GetMapping("/ws")
	public ModelAndView ws() {
		ModelAndView mv = new ModelAndView("ws");

		mv.addObject("lines", apiService.getBuslines());
		mv.addObject("locations", apiService.monitorBus(false));

		return mv;
	}

	@GetMapping("/broadcast")
	public String broadcast() {
		Map<String, Object>[] locations = apiService.monitorBus(true);
		Gson g = new Gson();
		String json = g.toJson(locations);
		WsServerEndpoint.broadcast(json);
		return "OK";
	}
}
