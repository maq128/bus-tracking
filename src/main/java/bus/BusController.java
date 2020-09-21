package bus;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class BusController {
	@GetMapping("/")
	public ModelAndView index(@Nullable @RequestParam Long did) {
		ModelAndView mv = new ModelAndView("index");

		try {
			if (did != null) {
				mv.addObject("did", did.longValue());
			}

			mv.addObject("buses", ApiService.getBuslines());
			mv.addObject("locations", ApiService.monitorBus());
		} catch (Exception e) {
			mv.addObject("errorMsg", e.getMessage());
		}

		return mv;
	}
}
