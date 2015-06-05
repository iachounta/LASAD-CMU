package lasad.gwt.client.ui.box.helper;

import com.extjs.gxt.ui.client.event.Events;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class BoxResizeCornerListener implements EventListener {

	public void onBrowserEvent(Event be) {
		if (be.getTypeInt() == Events.OnMouseDown.getEventCode()){
			handleMouseDown(be);
				
		}
		else if (be.getTypeInt() == Events.OnMouseOver.getEventCode()){
			handleMouseOver(be);
			
		}
		else if (be.getTypeInt() == Events.OnMouseOut.getEventCode()){
			handleMouseOut(be);
			
		}
		be.stopPropagation();
	}		
	
	private void handleMouseDown(Event be) { }
		
	private void handleMouseOut(Event be) {			
		// End hover effect
		if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerNE-red")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerNE");
		}
		else if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerNW-red")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerNW");
		}
		else if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerSE-red")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerSE");
		}
		else if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerSW-red")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerSW");
		}
	}
		
	private void handleMouseOver(Event be) {
		// Start hover effect
		if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerNE")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerNE-red");
		}
		else if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerNW")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerNW-red");
		}
		else if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerSE")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerSE-red");
		}
		else if(((Element) be.getEventTarget().cast()).getClassName().equals("resizeCornerSW")) {
			((Element) be.getEventTarget().cast()).setClassName("resizeCornerSW-red");
		}
	}
}