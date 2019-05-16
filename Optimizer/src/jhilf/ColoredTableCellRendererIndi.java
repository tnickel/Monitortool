package jhilf;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColoredTableCellRendererIndi extends DefaultTableCellRenderer
{
	public void setValue(Object value)
	{

		if (value instanceof Long)
		{
			if (((Long) value).longValue() % 2 == 0)
				setForeground(Color.red);
			else
				setForeground(Color.gray);

			setText("" + value);
		} else
			super.setValue(value);

	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{

		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		
		if(column<=2)
			setBackground(Color.WHITE);
		if ((column >2) && (value instanceof String))
		{
			
			String stval = ((String) value).toString();
			int intval = Integer.valueOf(stval);

			if(intval == -1)
				setBackground(Color.CYAN);
			else if (intval == 0)
				setBackground(Color.RED);
			else
				setBackground(Color.GREEN);
		}

		return this;
	}
}
