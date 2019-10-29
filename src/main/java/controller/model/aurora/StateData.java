package controller.model.aurora;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateData {

	private boolean isOn;
	private Integer brightness;
	private String currentEffect;
	private EffectData effectData;
}
