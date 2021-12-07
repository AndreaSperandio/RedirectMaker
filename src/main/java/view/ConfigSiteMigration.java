package view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import view.component.RMButton;
import view.util.RMColor;
import view.util.RMLocalizator;
import view.util.RMMessage;
import view.util.RMResource;

public class ConfigSiteMigration extends JFrame {
	private static final long serialVersionUID = 846132440578478084L;

	private static final RMLocalizator LOC = new RMLocalizator(ConfigSiteMigration.class);

	private static final int DEFAULT_MAX_PANELS_VIEW = 12;

	private final JLabel lblInstruction = new JLabel(ConfigSiteMigration.LOC.getRes("lblInstruction"));
	private final JLabel lblAuthor = new JLabel(MainView.LOC.getRes("lblAuthor"));
	private final RMButton btnSave = new RMButton(ConfigSiteMigration.LOC.getRes("btnSave"));
	private final RMButton btnDefault = new RMButton(ConfigSiteMigration.LOC.getRes("btnDefault"));

	private final JPanel pnlStringRepl = new JPanel();
	private JScrollPane scrollStringRepl = new JScrollPane(this.pnlStringRepl);
	private int yScrollStringRepl;

	private final List<StringReplPanel> stringReplacements = new ArrayList<>();

	public ConfigSiteMigration() {
		this.setup();
		this.init();
		this.recalculateColumnPanels();
	}

	private void setup() {
		this.setTitle(ConfigSiteMigration.LOC.getRes("title"));
		final Dimension dimension = new Dimension(605, 660);// Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		// this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setIconImages(RMResource.getLogoIcons());

		this.setLayout(null);
		this.pnlStringRepl.setLayout(null);

		this.add(this.lblInstruction);
		this.add(this.lblAuthor);
		this.add(this.btnSave);
		this.add(this.btnDefault);

		final int height = 20;
		final int x = 20;
		int y = 10;
		this.lblInstruction.setBounds(x, y, 560, height * 6);
		this.yScrollStringRepl = y + 130;
		y += 520;
		this.btnSave.setBounds(x + 192, y, 80, 30);
		this.btnDefault.setBounds(x + 292, y, 80, 30);
		y += 50;
		this.lblAuthor.setBounds(dimension.width - 150, y, 120, height);

		this.lblAuthor.setForeground(RMColor.LBL_BLUE);

		this.btnSave.addActionListener(e -> this.btnSaveActionPerformed());
		this.btnDefault.addActionListener(e -> this.btnDefaultActionPerformed());
	}

	private void init() {
		this.setDefaults();
	}

	private void btnSaveActionPerformed() {
		if (this.checkReplacements()) {
			this.setVisible(false);
		}
	}

	private void btnDefaultActionPerformed() {
		this.stringReplacements.clear();
		this.setDefaults();
		this.recalculateColumnPanels();
	}

	private void recalculateColumnPanels() {
		this.pnlStringRepl.removeAll();
		this.remove(this.scrollStringRepl);

		int y = 0;
		StringReplPanel stringReplPanel;
		for (final StringReplPanel element : this.stringReplacements) {
			stringReplPanel = element;
			stringReplPanel.setBounds(0, y, 460, 30);
			this.pnlStringRepl.add(stringReplPanel);
			y += 30;
		}

		// Don't move
		this.pnlStringRepl.setPreferredSize(new Dimension(490, y));
		if (y > ConfigSiteMigration.DEFAULT_MAX_PANELS_VIEW * 30) {
			y = ConfigSiteMigration.DEFAULT_MAX_PANELS_VIEW * 30;
		}

		this.scrollStringRepl = new JScrollPane(this.pnlStringRepl);
		this.scrollStringRepl.setBorder(null);
		this.scrollStringRepl.getVerticalScrollBar().setUnitIncrement(20); // 2 row at a time
		this.scrollStringRepl.setBounds(20, this.yScrollStringRepl, 555, y);
		this.add(this.scrollStringRepl);
		this.repaint();
		this.pack();
	}

	public void addStringReplPanel(final StringReplPanel prevStringReplPanel) {
		final StringReplPanel stringReplPanel = new StringReplPanel(this);
		this.stringReplacements.add(this.stringReplacements.indexOf(prevStringReplPanel) + 1, stringReplPanel);
		this.recalculateColumnPanels();
	}

	public void removeStringReplPanel(final StringReplPanel stringReplPanel) {
		if (this.stringReplacements.size() == 1) {
			stringReplPanel.clear();
			return;
		}
		this.stringReplacements.remove(stringReplPanel);
		this.pnlStringRepl.remove(stringReplPanel);
		this.recalculateColumnPanels();
	}

	private void setDefaults() {
		final Map<String, String> defReplacements = new HashMap<>();
		defReplacements.put("à", "a");
		defReplacements.put("é", "e");
		defReplacements.put("è", "e");
		defReplacements.put("ì", "i");
		defReplacements.put("ò", "o");
		defReplacements.put("ù", "u");
		defReplacements.put("â", "a");
		defReplacements.put("ê", "e");
		defReplacements.put("î", "i");
		defReplacements.put("ô", "o");
		defReplacements.put("û", "u");
		defReplacements.put("ä", "a");
		defReplacements.put("ë", "e");
		defReplacements.put("ï", "i");
		defReplacements.put("ö", "o");
		defReplacements.put("ü", "u");
		defReplacements.put("œ", "e");
		defReplacements.put("ç", "c");
		defReplacements.put("ß", "ss");

		defReplacements.forEach(
				(regex, replacement) -> this.stringReplacements.add(new StringReplPanel(this, regex, replacement)));
	}

	private boolean checkReplacements() {
		final boolean errors = this.stringReplacements.stream().map(StringReplPanel::checkParams).anyMatch(s -> !s);
		if (errors) {
			RMMessage.showErrDialog(this, ConfigSiteMigration.LOC.getRes("errReplacement"));
		}
		return !errors;
	}

	public Map<String, String> getReplacements() {
		if (!this.checkReplacements()) {
			return null;
		}
		return this.stringReplacements.stream().map(StringReplPanel::getReplacement)
				.collect(Collectors.toMap(map -> map.keySet().stream().collect(Collectors.toList()).get(0),
						map -> map.values().stream().collect(Collectors.toList()).get(0)));
	}
}
