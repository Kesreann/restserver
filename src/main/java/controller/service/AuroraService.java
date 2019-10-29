package controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.model.aurora.EffectData;
import controller.model.aurora.StateData;
import controller.repository.NanoLeafRepository;
import io.github.rowak.Effect;

@Service
public class AuroraService {

	@Autowired
	private NanoLeafRepository repo;
	
	public void init() {
		repo.init();
	}
	
	public void turnOn()  {
		repo.turnOn();
	}
	
	public void turnOff()  {
		repo.turnOff();
	}
	
	public boolean toggleLights()  {
		return repo.toggleLights();
	}
	
	public List<EffectData> getEffcts() {
		return repo.getEffects();
	}
	
	public Effect getCurrentEffct() {
		return repo.getCurrentEffect();
	}
	
	public StateData getCurrentState() {
		return repo.getCurrentState();
	}
	
	public void setEffect(String effect) {
		repo.setEffect(effect);
	}
	
	public void setBrightness(Integer brightness) {
		repo.setBrightness(brightness);
	}
	
}
