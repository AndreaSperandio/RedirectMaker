package view;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.ExportCore;
import control.ImportCore;
import model.ImportedRow;
import model.QueryParam;
import view.component.RMButton;
import view.component.RMCheckBox;
import view.component.RMComboBox;
import view.component.RMComboBoxItem;
import view.component.RMTextField;
import view.util.RMColor;
import view.util.RMLocalizator;
import view.util.RMMessage;
import view.util.RMResource;

public class MainView extends JFrame {
	private static final long serialVersionUID = 846132440578478084L;

	public static final RMLocalizator LOC = new RMLocalizator(MainView.class);
	private static final String DESKTOP_FOLDER = System.getProperty("user.home") + File.separator + "Desktop";
	private static final String IMPORT_FILE_EXTENSION = ".xlsx";
	private static final String EXPORT_FILE_EXTENSION = ".json";
	private static final String NON_REDIRECTED_EXPORT_FILE = MainView.DESKTOP_FOLDER + File.separator
			+ "nonRedirectedUrls.txt";

	private final JLabel lblInstruction = new JLabel(MainView.LOC.getRes("lblInstruction"));
	private final JLabel lblDocument = new JLabel(MainView.LOC.getRes("lblDocument"));
	private final RMButton btnImportDocument = new RMButton(MainView.LOC.getRes("btnLoad"));
	private final JLabel lblDocumentFile = new JLabel();
	private final RMCheckBox chkSkipHeader = new RMCheckBox(MainView.LOC.getRes("chkSkipHeader"));
	private final RMCheckBox chkSiteMigration = new RMCheckBox(MainView.LOC.getRes("chkSiteMigration"));
	private final RMButton btnConfigSM = new RMButton(MainView.LOC.getRes("btnConfigSM"));
	private final RMButton btnCreateRedirects = new RMButton(RMResource.getStartImage());
	private final JLabel lblCreateRedirects = new JLabel(MainView.LOC.getRes("lblCreateRedirects"));
	private final JLabel lblGroupName = new JLabel(MainView.LOC.getRes("lblGroupName"));
	private final RMTextField txtGroupName = new RMTextField();
	private final JLabel lblQueryParams = new JLabel(MainView.LOC.getRes("lblQueryParams"));
	private final RMComboBox<RMComboBoxItem<QueryParam>, QueryParam> cmbQueryParams = new RMComboBox<>();
	private final JLabel lblOptions = new JLabel(MainView.LOC.getRes("lblOptions"));
	private final RMCheckBox chkRegex = new RMCheckBox(MainView.LOC.getRes("chkRegex"));
	private final RMCheckBox chkIgnoreSlash = new RMCheckBox(MainView.LOC.getRes("chkIgnoreSlash"));
	private final RMCheckBox chkIgnoreCase = new RMCheckBox(MainView.LOC.getRes("chkIgnoreCase"));
	private final JLabel lblExportPath = new JLabel(MainView.LOC.getRes("lblExportPath"));
	private final JLabel lblExportPathWrn = new JLabel(MainView.LOC.getRes("lblExportPathWrn"));
	private final RMButton btnExportPath = new RMButton(MainView.LOC.getRes("btnExportPath"));
	private final JLabel lblExportFile = new JLabel();
	private final RMButton btnExport = new RMButton(RMResource.getExportImage());
	private final JLabel lblExport = new JLabel(MainView.LOC.getRes("lblExport"));
	private final JLabel lblAuthor = new JLabel(MainView.LOC.getRes("lblAuthor"));

	private final ConfigSiteMigration configSiteMigration = new ConfigSiteMigration();

	private File importFile = null;
	private File exportFile = null;

	private List<ImportedRow> importedRows = null;
	private Map<String, String> redirects = null;

	public MainView() {
		this.setup();
		this.init();
	}

	private void setup() {
		this.setTitle(MainView.LOC.getRes("title"));
		final Dimension dimension = new Dimension(510, 680);// Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		// this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setIconImages(RMResource.getLogoIcons());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				//if (TMMessage.showConfirmWarnDialog(MainView.this, MainView.LOC.getRes("cnfExit"))) {
				MainView.this.dispose();
				System.exit(0);
				//}
			}
		});
		this.setLayout(null);

		this.add(this.lblInstruction);
		this.add(this.lblDocument);
		this.add(this.btnImportDocument);
		this.add(this.lblDocumentFile);
		this.add(this.chkSkipHeader);
		this.add(this.chkSiteMigration);
		this.add(this.btnConfigSM);
		this.add(this.btnCreateRedirects);
		this.add(this.lblCreateRedirects);
		this.add(this.lblGroupName);
		this.add(this.txtGroupName);
		this.add(this.lblQueryParams);
		this.add(this.cmbQueryParams);
		this.add(this.lblOptions);
		this.add(this.chkRegex);
		this.add(this.chkIgnoreSlash);
		this.add(this.chkIgnoreCase);
		this.add(this.lblExportPath);
		this.add(this.lblExportPathWrn);
		this.add(this.btnExportPath);
		this.add(this.lblExportFile);
		this.add(this.btnExport);
		this.add(this.lblExport);
		this.add(this.lblAuthor);

		final int height = 20;
		final int margin = height + 10;
		final int x = 20;
		int y = 10;
		this.lblInstruction.setBounds(x, y, 560, height * 6);
		y += 130;
		this.lblDocument.setBounds(x, y, 500, height);
		y += margin;
		this.btnImportDocument.setBounds(x, y, 100, height);
		this.lblDocumentFile.setBounds(x + 120, y, 400, height);
		y += 50;
		this.chkSkipHeader.setBounds(x, y, 100, height);
		this.chkSiteMigration.setBounds(x + 150, y, 120, height);
		this.btnConfigSM.setBounds(x + 270, y, 120, height);
		y += 40;
		this.btnCreateRedirects.setBounds(x, y, 35, height + 10);
		this.lblCreateRedirects.setBounds(x + 45, y, 200, height + 10);
		y += 70;
		this.lblGroupName.setBounds(x, y, 100, height);
		this.txtGroupName.setBounds(x + 140, y, 280, height);
		y += margin;
		this.lblQueryParams.setBounds(x, y, 140, height);
		this.cmbQueryParams.setBounds(x + 140, y, 280, height);
		y += margin;
		this.lblOptions.setBounds(x, y, 200, height);
		y += margin;
		this.chkRegex.setBounds(x, y, 70, height);
		this.chkIgnoreSlash.setBounds(x + 95, y, 150, height);
		this.chkIgnoreCase.setBounds(x + 270, y, 200, height);
		y += 50;
		this.lblExportPath.setBounds(x, y, 100, height);
		this.lblExportPathWrn.setBounds(x + 120, y, 400, height);
		y += margin;
		this.btnExportPath.setBounds(x, y, 100, height);
		this.lblExportFile.setBounds(x + 120, y, 400, height);
		y += 50;
		this.btnExport.setBounds(x, y, 35, height + 10);
		this.lblExport.setBounds(x + 45, y, 200, height + 10);
		y += 50;
		this.lblAuthor.setBounds(dimension.width - 150, y, 120, height);

		this.lblDocument.setToolTipText(MainView.LOC.getRes("lblDocumentToolTip"));
		this.chkSkipHeader.setToolTipText(MainView.LOC.getRes("chkSkipHeaderToolTip"));
		this.chkSiteMigration.setToolTipText(MainView.LOC.getRes("chkSiteMigrationToolTip"));
		this.btnConfigSM.setToolTipText(MainView.LOC.getRes("btnConfigSMToolTip"));
		this.lblCreateRedirects.setToolTipText(MainView.LOC.getRes("lblCreateRedirectsToolTip"));
		this.lblExportPath.setToolTipText(MainView.LOC.getRes("lblExportPathToolTip"));
		this.lblGroupName.setToolTipText(MainView.LOC.getRes("lblGroupNameToolTip"));
		this.lblQueryParams.setToolTipText(MainView.LOC.getRes("lblQueryParamsToolTip"));

		this.lblDocumentFile.setForeground(RMColor.LBL_BLUE);
		this.lblExportFile.setForeground(RMColor.LBL_BLUE);
		this.lblExportPathWrn.setForeground(RMColor.LBL_ORANGE);
		this.lblAuthor.setForeground(RMColor.LBL_BLUE);

		this.btnImportDocument.addActionListener(e -> {
			this.btnFileChooserActionPerformed(this.btnImportDocument, MainView.LOC.getRes("jfcDocument"),
					this.lblDocumentFile, "MS Excel Document (2007+)", "xlsx", MainView.IMPORT_FILE_EXTENSION);
			this.updateGraphics();
		});
		this.btnConfigSM.addActionListener(e -> {
			EventQueue.invokeLater(() -> {
				try {
					this.configSiteMigration.setVisible(true);
				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			});
		});
		this.btnCreateRedirects.addActionListener(e -> {
			this.btnCreateRedirectsActionPerformed();
			this.updateGraphics();
		});
		this.btnExportPath.addActionListener(e -> {
			this.btnFileChooserActionPerformed(this.btnExport, MainView.LOC.getRes("jfcExport"), this.lblExportFile,
					"JSON Document", "json", MainView.EXPORT_FILE_EXTENSION);
			this.updateGraphics();
		});
		this.btnExport.addActionListener(e -> this.btnExportActionPerformed());

		this.chkSiteMigration.addChangeListener(e -> {
			this.updateGraphics();
		});
		this.chkRegex.addChangeListener(e -> {
			this.updateGraphics();
		});
	}

	private void init() {
		this.cmbQueryParams.addItems(QueryParam.getComboItems());
		this.clear();

		this.updateGraphics();
		this.setVisible(true);
	}

	private void updateGraphics() {
		this.chkSkipHeader.setEnabled(this.importFile != null);
		this.chkSiteMigration.setEnabled(this.importFile != null);
		this.btnConfigSM.setEnabled(this.importFile != null && this.chkSiteMigration.isSelected());
		this.btnCreateRedirects.setEnabled(this.importFile != null);
		this.txtGroupName.setEnabled(this.redirects != null && !this.redirects.isEmpty());
		this.cmbQueryParams.setEnabled(this.redirects != null && !this.redirects.isEmpty());
		this.chkRegex.setEnabled(this.redirects != null && !this.redirects.isEmpty());
		this.chkIgnoreSlash.setEnabled(this.redirects != null && !this.redirects.isEmpty());
		this.chkIgnoreCase.setEnabled(this.redirects != null && !this.redirects.isEmpty());
		this.btnExportPath.setEnabled(this.redirects != null && !this.redirects.isEmpty());
		this.btnExport.setEnabled(this.exportFile != null);

		if (this.chkRegex.isSelected()) {
			this.cmbQueryParams.setSelectedItemByKey(QueryParam.EXACT);
			this.cmbQueryParams.setEnabled(false);
		}
		if (this.importFile == null) {
			this.clear();
		}

		this.lblExportPathWrn.setVisible(MainView.checkFile(this.exportFile, MainView.EXPORT_FILE_EXTENSION, false));
	}

	private void btnFileChooserActionPerformed(final RMButton caller, final String title, final JLabel label,
			final String extensionName, final String extensionCode, final String extension) {
		final FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionName, extensionCode);
		final JFileChooser jfc = new JFileChooser(MainView.DESKTOP_FOLDER);
		jfc.setFileFilter(filter);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setDialogTitle(title);

		final int retVaule = jfc.showOpenDialog(caller);
		if (retVaule == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			if (caller == this.btnImportDocument) {
				this.importFile = file;
				this.importedRows = null;
				this.redirects = null;
			} else {
				if (!file.getName().endsWith(extension)) {
					file = new File(file.getAbsolutePath() + extension);
				}
				this.exportFile = file;
			}
			label.setText(file.getName());
		} else if (retVaule == JFileChooser.CANCEL_OPTION) {
			if (caller == this.btnImportDocument) {
				this.importFile = null;
				this.importedRows = null;
				this.redirects = null;
			} else {
				this.exportFile = null;
			}
			label.setText(null);
		}
	}

	private void btnCreateRedirectsActionPerformed() {
		if (!this.checkParams()) {
			return;
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		ImportCore.importRows(this, this.importFile);
		this.setCursor(Cursor.getDefaultCursor());
		this.updateGraphics();
	}

	private void btnExportActionPerformed() {
		if (!this.checkExportParams()) {
			return;
		}

		if (ExportCore.doExport(this, this.exportFile.getAbsolutePath(), this.redirects, this.txtGroupName.getText(),
				this.cmbQueryParams.getSelectedItemKey(), this.chkRegex.isSelected(), this.chkIgnoreSlash.isSelected(),
				this.chkIgnoreCase.isSelected())) {
			if (RMMessage.showConfirmDialog(this, MainView.LOC.getRes("cnfExported"))) {
				try {
					Desktop.getDesktop().open(this.exportFile);
				} catch (final IOException e) {
					RMMessage.showErrDialog(this, MainView.LOC.getRes("errInternalError"));
					e.printStackTrace();
				}
			}

			this.lblDocumentFile.setText(null);
			this.lblExportFile.setText(null);
			this.importFile = null;
			this.exportFile = null;
			this.importedRows = null;
			this.redirects = null;
			this.updateGraphics();
		}
	}

	private boolean checkParams() {
		if (!MainView.checkFile(this.importFile, MainView.IMPORT_FILE_EXTENSION, false)) {
			RMMessage.showErrDialog(this, MainView.LOC.getRes("errDocumentFile"));
			return false;
		}

		return true;
	}

	private boolean checkExportParams() {
		if (!MainView.checkFile(this.exportFile, MainView.EXPORT_FILE_EXTENSION, true)) {
			RMMessage.showErrDialog(this, MainView.LOC.getRes("errExportPath"));
			return false;
		}

		if (this.txtGroupName.isEmpty()) {
			RMMessage.showErrDialog(this, MainView.LOC.getRes("errGroupName"));
			return false;
		}

		return true;
	}

	private static boolean checkFile(final File file, final String extension, final boolean createIfAbsent) {
		if (file == null || !file.exists() || !file.isFile()) {
			if (createIfAbsent) {
				try {
					file.createNewFile();
					return true;
				} catch (final IOException e) {
					//RMMessage.showErrDialog(this, MainView.LOC.getRes("errInternalError"));
					e.printStackTrace();
				}
			}
			return false;
		}

		return file.getName().endsWith(extension);
	}

	public void notifyImportCompleted(final List<ImportedRow> _importedRows) {
		if (_importedRows == null || _importedRows.isEmpty()) {
			RMMessage.showWarnDialog(this, MainView.LOC.getRes("wrnNoRowsFound"));
			return;
		}

		final int nCols = 2;
		if (_importedRows.get(0).getCells() == null || _importedRows.get(0).getCells().size() < nCols) {
			RMMessage.showErrDialog(this, MainView.LOC.getRes("errNotEnoughData"));
			return;
		}

		this.importedRows = _importedRows;
		if (this.chkSkipHeader.isSelected()) {
			this.importedRows.remove(0);
		}
		this.createRedirects(this.chkSiteMigration.isSelected());
	}

	private void createRedirects(final boolean isSiteMigration) {
		this.redirects = new HashMap<>();
		final Set<String> vecchiUrl = new HashSet<>();
		final Set<String> nuoviUrl = new HashSet<>();

		if (!isSiteMigration) {
			this.importedRows.forEach(r -> {
				final String urlPartenza = r.getStringAt(0);
				final String urlArrivo = r.getStringAt(1);
				if (urlPartenza != null && !"".equals(urlPartenza) && urlArrivo != null && !"".equals(urlArrivo)) {
					this.redirects.put(urlPartenza, urlArrivo);
				}
			});
		} else {
			final Map<String, String> stringReplacements = this.configSiteMigration.getReplacements();
			if (stringReplacements == null) {
				return;
			}
			this.populateSM(nuoviUrl, vecchiUrl);
			this.removeSameSM(nuoviUrl, vecchiUrl);
			this.generateRedirectsSM(nuoviUrl, vecchiUrl, stringReplacements);
		}

		if (!this.redirects.isEmpty()) {
			RMMessage.showInfoDialog(this, MainView.LOC.getRes("infNRedirects", this.redirects.size()));
		}

		if (!vecchiUrl.isEmpty()) {
			if (RMMessage.showConfirmWarnDialog(this, MainView.LOC.getRes("wrnNotRedirected", vecchiUrl.size()))) {
				this.exportNonRedirected(vecchiUrl);
			}
		}
	}

	private void populateSM(final Set<String> nuoviUrl, final Set<String> vecchiUrl) {
		this.importedRows.forEach(r -> {
			final String vecchioUrl = r.getStringAt(0);
			if (vecchioUrl != null) {
				vecchiUrl.add(vecchioUrl.toLowerCase());
			}
			final String nuovoUrl = r.getStringAt(1);
			if (nuovoUrl != null) {
				nuoviUrl.add(nuovoUrl.toLowerCase());
			}
		});
	}

	@SuppressWarnings("static-method")
	private void removeSameSM(final Set<String> nuoviUrl, final Set<String> vecchiUrl) {
		final Set<String> tempUrl = new HashSet<>(nuoviUrl);
		nuoviUrl.removeAll(vecchiUrl);
		vecchiUrl.removeAll(tempUrl);
	}

	private void generateRedirectsSM(final Set<String> nuoviUrl, final Set<String> vecchiUrl,
			final Map<String, String> stringReplacements) {
		final Map<String, List<String>> vecchiUrlMap = new HashMap<>();
		final Map<String, String> nuoviUrlMap = new HashMap<>();

		vecchiUrl.forEach(v -> {
			final String match = this.getModifiedString(v, stringReplacements);
			if (vecchiUrlMap.containsKey(match)) {
				vecchiUrlMap.get(match).add(v);
			} else {
				final List<String> values = new ArrayList<>();
				values.add(v);
				vecchiUrlMap.put(match, values);
			}
		});
		nuoviUrl.forEach(n -> nuoviUrlMap.put(this.getModifiedString(n, stringReplacements), n));

		final Set<String> newMatches = nuoviUrlMap.keySet();
		newMatches.retainAll(vecchiUrlMap.keySet());
		newMatches.forEach(match -> {
			final List<String> vecchioUrl = vecchiUrlMap.get(match);
			final String nuovoUrl = nuoviUrlMap.get(match);
			vecchioUrl.forEach(v -> this.redirects.put(v, nuovoUrl));
			vecchiUrl.removeAll(vecchioUrl);
			nuoviUrl.remove(nuovoUrl);
		});
	}

	private void exportNonRedirected(final Set<String> urls) {
		try (final Writer file = new OutputStreamWriter(new FileOutputStream(MainView.NON_REDIRECTED_EXPORT_FILE))) {
			file.write(urls.stream().collect(Collectors.joining(System.lineSeparator())));
			if (RMMessage.showConfirmDialog(this,
					MainView.LOC.getRes("cnfExportedNonRedirected", MainView.NON_REDIRECTED_EXPORT_FILE))) {
				try {
					Desktop.getDesktop().open(new File(MainView.NON_REDIRECTED_EXPORT_FILE));
				} catch (final IOException e) {
					RMMessage.showErrDialog(this, MainView.LOC.getRes("errInternalError"));
					e.printStackTrace();
				}
			}
		} catch (final FileNotFoundException e) {
			RMMessage.showErrDialog(this, ExportCore.LOC.getRes("errFileNotFoundException"));
			e.printStackTrace();
		} catch (final IOException e) {
			RMMessage.showErrDialog(this, ExportCore.LOC.getRes("errIOException"));
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-method")
	private String getModifiedString(final String string, final Map<String, String> stringReplacements) {
		String modString = string;
		for (final String regex : stringReplacements.keySet()) {
			modString = modString.replaceAll(regex, stringReplacements.get(regex));
		}
		return modString;
	}

	private void clear() {
		this.chkSkipHeader.setSelected(true);
		this.chkSiteMigration.setSelected(false);
		this.cmbQueryParams.setSelectedItemByKey(QueryParam.IGNORE);
		this.txtGroupName.clear();
		this.chkRegex.setSelected(false);
		this.chkIgnoreSlash.setSelected(true);
		this.chkIgnoreCase.setSelected(false);
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				final MainView frame = new MainView();
				frame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
