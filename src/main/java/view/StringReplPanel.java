package view;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.component.RMButton;
import view.component.RMTextField;
import view.util.RMLocalizator;
import view.util.RMResource;

public class StringReplPanel extends JPanel {
	private static final long serialVersionUID = 846132445578478084L;

	private static final RMLocalizator LOC = new RMLocalizator(StringReplPanel.class);

	private final JLabel lblRegex = new JLabel(StringReplPanel.LOC.getRes("lblRegex"));
	private final RMTextField txtRegex = new RMTextField();
	private final JLabel lblReplacement = new JLabel(StringReplPanel.LOC.getRes("lblReplacement"));
	private final RMTextField txtReplacement = new RMTextField();
	private final RMButton btnAdd = new RMButton(RMResource.getAddImage());
	private final RMButton btnDelete = new RMButton(RMResource.getDeleteImage());

	private final ConfigSiteMigration parent;

	public StringReplPanel(final ConfigSiteMigration parent) {
		this.parent = parent;

		this.setup();
		this.init();
	}

	public StringReplPanel(final ConfigSiteMigration parent, final String regex, final String replacement) {
		this(parent);
		this.txtRegex.setText(regex);
		this.txtReplacement.setText(replacement);
	}

	private void setup() {
		final Dimension dimension = new Dimension(500, 30);
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		this.setLayout(null);

		this.add(this.lblRegex);
		this.add(this.txtRegex);
		this.add(this.lblReplacement);
		this.add(this.txtReplacement);
		this.add(this.btnAdd);
		this.add(this.btnDelete);

		final int height = 20;
		final int x = 0;
		final int y = 10;
		this.lblRegex.setBounds(x, y, 50, height);
		this.txtRegex.setBounds(x + 50, y, 100, height);
		this.lblReplacement.setBounds(x + 170, y, 80, height);
		this.txtReplacement.setBounds(x + 250, y, 130, height);
		this.btnAdd.setBounds(x + 410, y, 20, 20);
		this.btnDelete.setBounds(x + 440, y, 20, 20);

		this.lblRegex.setToolTipText(StringReplPanel.LOC.getRes("lblRegexToolTip"));
		this.lblReplacement.setToolTipText(StringReplPanel.LOC.getRes("lblReplacementToolTip"));

		this.btnAdd.addActionListener(e -> this.btnAddActionPerformed());
		this.btnDelete.addActionListener(e -> this.btnDeleteActionPerformed());
	}

	private void init() {
		//Do nothing
	}

	private void btnAddActionPerformed() {
		this.parent.addStringReplPanel(this);
	}

	private void btnDeleteActionPerformed() {
		this.parent.removeStringReplPanel(this);
	}

	public boolean checkParams() {
		if (this.txtRegex.isEmpty()) {
			return false;
		}
		return true;
	}

	public Map<String, String> getReplacement() {
		if (!this.checkParams()) {
			return null;
		}
		final Map<String, String> replacement = new HashMap<>();
		replacement.put(this.txtRegex.getText(), this.txtReplacement.getText());
		return replacement;
	}

	public void clear() {
		this.txtRegex.clear();
		this.txtReplacement.clear();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.txtRegex.setEnabled(enabled);
		this.txtReplacement.setEnabled(enabled);
		this.btnAdd.setEnabled(enabled);
		this.btnDelete.setEnabled(enabled);
	}
}
