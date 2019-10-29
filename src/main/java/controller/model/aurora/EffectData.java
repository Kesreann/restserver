package controller.model.aurora;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EffectData {

	private String name;
	private List<ColorData> colors;
	private Map<String, String> properties = new HashMap<>();
}
