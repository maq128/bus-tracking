package bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class BusController {
	@Autowired
	ApiService apiService;

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");

		mv.addObject("lines", apiService.getBuslines());

		return mv;
	}

	@GetMapping("/detail")
	public Object detail(@RequestParam long id) {
		return apiService.lineDetail(id);
	}
}
