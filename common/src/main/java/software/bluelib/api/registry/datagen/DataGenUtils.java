package software.bluelib.api.registry.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataGenUtils {

	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
}
