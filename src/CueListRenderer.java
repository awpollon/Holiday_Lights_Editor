import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class CueListRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1476704357098753251L;

	public CueListRenderer() {
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		Cue cue = (Cue) value;
		
		setText(cue.getRuntTimeInSecs() + "");


		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} 
		else if(cue.isActive()) {
			setBackground(Color.yellow);
			setForeground(Color.black);
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;

	}
}
