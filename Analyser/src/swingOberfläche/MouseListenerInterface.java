package swingOberfläche;

import java.awt.event.MouseEvent;
import java.util.EventListener;

public interface MouseListenerInterface extends EventListener
{
	 // Invoked when the mouse has been clicked on a component.
    public void mouseClicked(MouseEvent e);
}
