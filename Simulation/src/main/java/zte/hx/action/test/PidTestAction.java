package zte.hx.action.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import zte.hx.service.test.PidTestService;
import zte.hx.service.test.PidTestService.TestView;

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
