package control;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.ExportRow;
import view.MainView;
import view.util.RMLocalizator;
import view.util.RMMessage;

public class ExportCore {
	private static final RMLocalizator LOC = new RMLocalizator(ExportCore.class);

	public static boolean doExport(final MainView parent, final String path, final String sheetName,
			final List<ExportRow> exportRows) {

		if (parent == null || sheetName == null || exportRows == null) {
			return false;
		}

		try {
			RMMessage.showInfoDialog(parent, ExportCore.LOC.getRes("infExportStarted"));

			try (final Workbook workbook = new XSSFWorkbook();
					final FileOutputStream fileOut = new FileOutputStream(path)) {
				final Sheet sheet = workbook.createSheet(sheetName);

				sheet.setMargin(Sheet.LeftMargin, 0.6);
				sheet.setMargin(Sheet.RightMargin, 0.6);
				sheet.setMargin(Sheet.TopMargin, 1);
				sheet.setMargin(Sheet.BottomMargin, 1);
				sheet.setMargin(Sheet.HeaderMargin, 0.5);
				sheet.setMargin(Sheet.FooterMargin, 0.5);

				ExportCore.createBody(workbook, sheet, exportRows);

				for (int i = 0; i < exportRows.get(0).getCells().size(); i++) {
					sheet.autoSizeColumn(i);
				}

				workbook.write(fileOut);
			}
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

	private static void createBody(final Workbook workbook, final Sheet sheet, final List<ExportRow> exportRows) {
		final CellStyle bodyCellStyle = workbook.createCellStyle();
		ExportCore.addCellBorder(bodyCellStyle);
		final CellStyle matchedBodyCellStyle = workbook.createCellStyle();
		ExportCore.addCellBorder(matchedBodyCellStyle);
		ExportCore.colorCell(matchedBodyCellStyle, false);
		ExportCore.addCellBorder(bodyCellStyle);
		final CellStyle redirectBodyCellStyle = workbook.createCellStyle();
		ExportCore.addCellBorder(redirectBodyCellStyle);
		ExportCore.colorCell(redirectBodyCellStyle, true);

		Row row;
		ExportRow exportRow;
		for (int i = 0; i < exportRows.size(); i++) {
			row = sheet.createRow(i);
			exportRow = exportRows.get(i);
			if (exportRow == null) {
				continue;
			}
			ExportCore.fillBodyRow(row, exportRow, bodyCellStyle, matchedBodyCellStyle, redirectBodyCellStyle);
		}
	}

	private static void fillBodyRow(final Row row, final ExportRow exportRow, final CellStyle bodyCellStyle,
			final CellStyle matchedBodyCellStyle, final CellStyle redirectBodyCellStyle) {
		ExportRow.ExportCell exportCell;
		Cell cell;
		String value;
		for (int i = 0; i < exportRow.getCells().size(); i++) {
			exportCell = exportRow.getCellAt(i);
			cell = ExportCore.createCell(row, i,
					exportCell.isToColor() ? exportCell.isRedirect() ? redirectBodyCellStyle : matchedBodyCellStyle
							: bodyCellStyle);
			value = exportCell.getValue();
			cell.setCellValue(value != null ? value : "");
		}
	}

	private static void addCellBorder(final CellStyle cellStyle) {
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
	}

	private static void colorCell(final CellStyle cellStyle, final boolean isRedirect) {
		cellStyle
				.setFillForegroundColor((isRedirect ? IndexedColors.LIGHT_ORANGE : IndexedColors.PALE_BLUE).getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}

	private static Cell createCell(final Row row, final int pos, final CellStyle cellStyle) {
		final Cell cell = row.createCell(pos);
		cell.setCellStyle(cellStyle);
		return cell;
	}
}
