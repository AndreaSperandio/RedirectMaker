package view.util;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RMMessage {
	private static final RMLocalizator LOC = new RMLocalizator(RMMessage.class);

	public static boolean showConfirmDialog(final Component comp, String message) {
		message = message != null ? message.trim() : null;
		if (comp == null || message == null) {
			return false;
		}

		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(comp, message,
				RMMessage.getMessageTitle(comp, "confirmDialog"), JOptionPane.YES_NO_OPTION);
	}

	public static boolean showConfirmWarnDialog(final Component comp, String message) {
		message = message != null ? message.trim() : null;
		if (comp == null || message == null) {
			return false;
		}

		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(comp, message,
				RMMessage.getMessageTitle(comp, "confirmDialog"), JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}

	public static void showInfoDialog(final Component comp, final String message) {
		RMMessage.showDialog(comp, message, "infoDialog", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showWarnDialog(final Component comp, final String message) {
		RMMessage.showDialog(comp, message, "warnDialog", JOptionPane.WARNING_MESSAGE);
	}

	public static void showErrDialog(final Component comp, final String message) {
		RMMessage.showDialog(comp, message, "errDialog", JOptionPane.ERROR_MESSAGE);
	}

	private static void showDialog(final Component comp, String message, final String resTitle, final int msgType) {
		message = message != null ? message.trim() : null;
		if (comp == null || message == null) {
			return;
		}

		JOptionPane.showMessageDialog(comp, message, RMMessage.getMessageTitle(comp, resTitle), msgType);
	}

	private static String getMessageTitle(final Component comp, final String resTitle) {
		return comp instanceof JFrame ? ((JFrame) comp).getTitle() : RMMessage.LOC.getRes(resTitle);
	}
}