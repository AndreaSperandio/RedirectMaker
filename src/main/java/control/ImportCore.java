package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.ImportedRow;
import view.MainView;
import view.util.RMLocalizator;
import view.util.RMMessage;

public class ImportCore {
	private static final RMLocalizator LOC = new RMLocalizator(ImportCore.class);

	private ImportCore() {
	}

	public static void importRows(final MainView parent, final File importFile) {
		final List<ImportedRow> importedRows = new ArrayList<>();
		try {
			if (parent == null || importFile == null || !importFile.exists() || importFile.isDirectory()) {
				return;
			}

			try {
				//TRMessage.showInfoDialog(parent, ImportCore.LOC.getRes("infImportStarted"));

				try (final Workbook workbook = WorkbookFactory.create(importFile)) {
					if (workbook.getNumberOfSheets() < 1) {
						RMMessage.showErrDialog(parent, ImportCore.LOC.getRes("errNoSheetsFound"));
						return;
					}

					final Sheet sheet = workbook.getSheetAt(0);
					sheet.forEach(row -> {
						final ImportedRow importedRow = new ImportedRow();
						for (int i = 0; i < row.getLastCellNum(); i++) {
							importedRow.addCell(row.getCell(i));
						}
						if (!importedRow.isEmpty()) {
							importedRows.add(importedRow);
						}
					});
				} catch (final EncryptedDocumentException e) {
					RMMessage.showErrDialog(parent, ImportCore.LOC.getRes("errEncryptedDocument"));
					e.printStackTrace();
				}
			} catch (final FileNotFoundException e) {
				RMMessage.showErrDialog(parent, ImportCore.LOC.getRes("errFileNotFoundException"));
				e.printStackTrace();
			} catch (final IOException e) {
				RMMessage.showErrDialog(parent, ImportCore.LOC.getRes("errIOException"));
				e.printStackTrace();
			}
		} finally {
			parent.notifyImportCompleted(importedRows);
		}
	}
}
