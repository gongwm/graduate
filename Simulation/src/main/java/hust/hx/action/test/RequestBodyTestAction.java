package hust.hx.action.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestBodyTestAction {
	@ResponseBody
	@PostMapping(path = "/request_body_test")
	public void test(@RequestParam("name") String name) {
		System.out.println(name);
	}
}
