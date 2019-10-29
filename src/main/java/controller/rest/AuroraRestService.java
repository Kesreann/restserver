package controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import controller.model.aurora.EffectData;
import controller.model.aurora.StateData;
import controller.service.AuroraService;

@RestController
@RequestMapping("/api/aurora")
public class AuroraRestService {

	@Autowired
	private AuroraService service;
	
	@RequestMapping(value="/init", method = RequestMethod.GET)
	public void init() {
		service.init();
	}
	
	@RequestMapping(value="/on", method = RequestMethod.GET)
	public void turnOn() {
		service.turnOn();
	}
	
	@RequestMapping(value="/off", method = RequestMethod.GET)
	public void turnOff() {
		service.turnOff();
	}
	
	@RequestMapping(value="/effects", method = RequestMethod.GET)
	public List<EffectData> getEffects() {
		return service.getEffcts();
	}
	
	@RequestMapping(value="/current-state", method = RequestMethod.GET)
	public StateData getCurrentState() {
		return service.getCurrentState();
	}
	
	@RequestMapping(value="/current-effect", method = RequestMethod.GET)
	public String getCurrentEffect() {
		return service.getCurrentEffct().getName();
	}
	
	@RequestMapping(value="/effect", method = RequestMethod.POST)
	public void setEffect(@RequestBody String effect) {
		service.setEffect(effect);
	}
	
	@RequestMapping(value="/brightness", method = RequestMethod.POST)
	public void setBrightness(@RequestBody String brightness) {
		System.err.println(brightness);
		Integer brightnessInt = Integer.valueOf(brightness);
		service.setBrightness(brightnessInt);
	}
}
