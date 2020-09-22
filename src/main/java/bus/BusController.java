package bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

			mv.addObject("buses", apiService.getBuslines());
			mv.addObject("locations", apiService.monitorBus());
		} catch (Exception e) {
			mv.addObject("errorMsg", e.getMessage());
		}

		return mv;
	}
}
