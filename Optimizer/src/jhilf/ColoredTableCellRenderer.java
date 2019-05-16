package jhilf;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColoredTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4719389931244223959L;

	public void setValue(Object value)
	{
		
		//setBackground(Color.BLUE);
		super.setValue(value);
		
		/*if (value instanceof Long)
		{
			if (((Long) value).longValue() % 2 == 0)
				setForeground(Color.red);
			else
				setForeground(Color.gray);

			setText("" + value);
		} else
			super.setValue(value);*/
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{

		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		
		
		/*if (value instanceof Integer)
		{
			
				setBackground(Color.GREEN);
			
		}
		if (value instanceof String)
		{
			
				setBackground(Color.RED);
			
		}*/
	
		return this;
	}
}
