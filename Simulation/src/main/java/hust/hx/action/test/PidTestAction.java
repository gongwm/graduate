package hust.hx.action.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hust.hx.service.test.PidTestService;
import hust.hx.service.test.PidTestService.TestView;

@Controller
public class PidTestAction {
	@ResponseBody
	@GetMapping(path = "/pid-test")
	public TestView getView() {
		PidTestService service = new PidTestService();
		service.simulate();
		return service.getView();
	}
}
