package controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.service.HomeStateService;

@RestController
@RequestMapping("/api/home")
public class HomeStateRestService {

	@Autowired
	private HomeStateService hss;
	
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public void atHome(@RequestParam(name="atHome") boolean atHome) {
		hss.atHome(atHome);
	}
}
