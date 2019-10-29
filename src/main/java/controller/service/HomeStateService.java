package controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeStateService {

	@Autowired
	private AuroraService auroraService;
	
	public void atHome(boolean atHome) {
		if (atHome) {
			auroraService.turnOn();
		} else {
			auroraService.turnOff();
		}
	}
}
