package controller.repository;

import java.awt.Color;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import controller.model.aurora.ColorData;
import controller.model.aurora.EffectData;
import controller.model.aurora.StateData;
import io.github.rowak.Aurora;
import io.github.rowak.Effect;
import io.github.rowak.Setup;
import io.github.rowak.StatusCodeException;
import io.github.rowak.StatusCodeException.ResourceNotFoundException;
import io.github.rowak.StatusCodeException.UnauthorizedException;

@Service
public class NanoLeafRepository {

	private Aurora aurora;
	
	@Value("${nanoleaf.apiKey}")
	private String apiKey;
	
	@PostConstruct
	private void initAurora() {
		try {
   		List<InetSocketAddress> auroras = Setup.quickFindAuroras();
   		String host = auroras.get(0).getHostName();
   		int port = auroras.get(0).getPort();
   		String apiLevel = "v1";
   		try {
   			aurora = new Aurora(host, port, apiLevel, "z8eQAEh4LOF3phk2KN8ZajOQObuBqXUu");
   		} catch (Exception e) {
   			System.out.println("Trying to create new token");
   			String accessToken = Setup.createAccessToken(host, port, apiLevel);
   			System.out.println("Token created: " + accessToken);
   			apiKey = accessToken;
   			aurora = new Aurora(host, port, apiLevel, accessToken);
   		}
   		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		List<InetSocketAddress> auroras = Setup.quickFindAuroras();
		String host = auroras.get(0).getHostName();
		int port = auroras.get(0).getPort();
		String apiLevel = "v1";
		
		System.err.println("host: " + host + " port: " + port);
		try {
			String accessToken = Setup.createAccessToken(host, port, apiLevel);
			System.err.println(accessToken);
		} catch (StatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void turnOn()  {
		try {
			aurora.state().setOn(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void turnOff() {
		try {
			aurora.state().setOn(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean toggleLights() {
		try {
			int state = aurora.state().toggleOn();
			return aurora.state().getOn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<EffectData> getEffects() {
		List<EffectData> effects = new ArrayList<>();
			try {

				for (Effect e : aurora.effects().getAllEffects()) {
					EffectData ef = new EffectData();
					ef.setName(e.getName());
					
					List<ColorData> cdl = new ArrayList<>();
					HashMap<String, String> map = new HashMap<>();
					for (Entry<Object, Object> entry : e.getProperties().entrySet()) {
						map.put(entry.getKey().toString(), entry.getValue().toString());
						
						if (entry.getKey().equals("palette")) {
							ef.setColors((getColorData(entry.getValue().toString())));
						}
					}
					ef.setProperties(map);
					effects.add(ef);
				}
			} catch (UnauthorizedException e) {
				e.printStackTrace();
			} catch (StatusCodeException e) {
				e.printStackTrace();
			}
			return effects;
	}
	
	public Effect getCurrentEffect() {
		try {
			return aurora.effects().getCurrentEffect();
		} catch (UnauthorizedException e) {
			e.printStackTrace();
		} catch (StatusCodeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Effect getEffectByName(String name) {
		try {
			return aurora.effects().getEffect(name);
		} catch (UnauthorizedException e) {
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (StatusCodeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StateData getCurrentState() {
		try {
			StateData sd = new StateData();
			sd.setOn(aurora.state().getOn());
			sd.setBrightness(aurora.state().getBrightness());
			sd.setCurrentEffect(aurora.effects().getCurrentEffectName());
			Effect e = getEffectByName(aurora.effects().getCurrentEffectName());
			EffectData ed = new EffectData();
			ed.setName(aurora.effects().getCurrentEffectName());
			Map<String, String> properties = propertiesToMap(e.getProperties());
			for(String s: properties.keySet()) {
				if (s.equals("palette")) {
					ed.setColors(getColorData(properties.get(s)));
				}
			}
			ed.setProperties(properties);
			sd.setEffectData(ed);
			return sd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setEffect(String effect) {
		try {
			aurora.effects().setEffect(effect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setBrightness(Integer brightness) {
		try {
			aurora.state().setBrightness(brightness);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private List<ColorData> getColorData(String colorArray) {
		List<ColorData> cdl = new ArrayList<>();
		colorArray = colorArray.substring(1).replaceAll("\\]", "");
		String[] objectString = colorArray.split("},");
		for (String string : objectString) {
			ColorData cd = new ColorData();
			String[] keyValueList = string.split(",");
			for (String keyValue : keyValueList) {
				String temp = keyValue.replaceAll("\"", "");
				temp = temp.replaceAll("\\{", "");
				temp = temp.replaceAll("\\}", "");
				String[] kv = temp.split(":");		
				String key = kv[0];
				String value = kv[1];
				if (key.equals("saturation")) {
					cd.setSaturation(Integer.valueOf(value));
				}
				if (key.equals("brightness")) {
					cd.setBrightness(Integer.valueOf(value));
				}
				if (key.equals("hue")) {
					cd.setHue(Integer.valueOf(value));
				}
			}
						
			float sat = (float)cd.getSaturation()/100;
			float brigh = (float)cd.getBrightness()/100;
			Color c = Color.getHSBColor((float)cd.getHue()/360, sat, brigh);

		   int red = c.getRed();
		   int green = c.getGreen();
		   int blue = c.getBlue();
			cd.setRed(red);
			cd.setGreen(green);
			cd.setBlue(blue);
			
			cdl.add(cd);
		}
		
		return cdl;
	}
	
	public static String hsvToRgb(float hue, float saturation, float value) {

	    int h = (int)(hue * 6);
	    float f = hue * 6 - h;
	    float p = value * (1 - saturation);
	    float q = value * (1 - f * saturation);
	    float t = value * (1 - (1 - f) * saturation);

	    switch (h) {
	      case 0: return rgbToString(value, t, p);
	      case 1: return rgbToString(q, value, p);
	      case 2: return rgbToString(p, value, t);
	      case 3: return rgbToString(p, q, value);
	      case 4: return rgbToString(t, p, value);
	      case 5: return rgbToString(value, p, q);
	      default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
	    }
	}
	
	public static String rgbToString(float r, float g, float b) {
	    String rs = Integer.toHexString((int)(r * 256));
	    String gs = Integer.toHexString((int)(g * 256));
	    String bs = Integer.toHexString((int)(b * 256));
	    return rs + gs + bs;
	}
	
	private Map<String, String> propertiesToMap(Map<Object, Object> properties) {
		Map<String, String> map = new HashMap();
		
		for (Object o : properties.keySet()) {
			String key = o.toString();
			String value = properties.get(o).toString();
			map.put(key, value);
		}
		return map;
		
	}
}
