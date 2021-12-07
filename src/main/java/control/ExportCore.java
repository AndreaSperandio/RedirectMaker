package control;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.QueryParam;
import view.MainView;
import view.util.RMLocalizator;
import view.util.RMMessage;

public class ExportCore {
	public static final RMLocalizator LOC = new RMLocalizator(ExportCore.class);

	@SuppressWarnings("unchecked")
	public static boolean doExport(final MainView parent, final String path, final Map<String, String> redirects,
			final String groupName, final QueryParam queryParam, final boolean isRegex, final boolean ignoreSlash,
			final boolean ignoreCase) {

		if (parent == null || redirects == null || redirects.isEmpty()) {
			return false;
		}

		RMMessage.showInfoDialog(parent, ExportCore.LOC.getRes("infExportStarted"));
		final int groupId = new Random().nextInt(1000) + 100;

		final JSONObject output = new JSONObject();

		final JSONArray groups = new JSONArray();
		final JSONObject group = new JSONObject();
		group.put("id", groupId);
		group.put("name", groupName);
		group.put("module_id", 1);
		group.put("moduleName", "WordPress");
		group.put("enabled", true);
		groups.add(group);
		output.put("groups", groups);

		final JSONArray redirs = new JSONArray();
		redirects.forEach((vecchioUrl, nuovoUrl) -> {
			final JSONObject redirect = new JSONObject();
			redirect.put("url", vecchioUrl);
			redirect.put("match_url", isRegex ? "regex" : vecchioUrl);

			final JSONObject source = new JSONObject();
			source.put("flag_query", (isRegex ? QueryParam.EXACT : queryParam).toString().toLowerCase());
			source.put("flag_case", ignoreCase);
			source.put("flag_trailing", ignoreSlash);
			source.put("flag_regex", isRegex);
			final JSONObject matchData = new JSONObject();
			matchData.put("source", source);
			redirect.put("match_data", matchData);

			redirect.put("action_code", 301);
			redirect.put("action_type", "url");

			final JSONObject actionData = new JSONObject();
			actionData.put("url", nuovoUrl);
			redirect.put("action_data", actionData);

			redirect.put("match_type", "url");
			redirect.put("title", "");
			redirect.put("regex", isRegex);
			redirect.put("group_id", groupId);
			redirect.put("enabled", true);

			redirs.add(redirect);
		});

		output.put("redirects", redirs);

		try (final Writer file = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
			file.write(output.toJSONString());
		} catch (final FileNotFoundException e) {
			RMMessage.showErrDialog(parent, ExportCore.LOC.getRes("errFileNotFoundException"));
			e.printStackTrace();
			return false;
		} catch (final IOException e) {
			RMMessage.showErrDialog(parent, ExportCore.LOC.getRes("errIOException"));
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
