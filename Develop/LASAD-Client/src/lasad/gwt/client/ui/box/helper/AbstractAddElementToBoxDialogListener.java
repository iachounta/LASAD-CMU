package lasad.gwt.client.ui.box.helper;

import lasad.gwt.client.ui.box.AbstractBox;
import lasad.gwt.client.ui.workspace.graphmap.GraphMap;

import com.extjs.gxt.ui.client.event.Events;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public abstract class AbstractAddElementToBoxDialogListener implements EventListener {

//	private final LASADActionSender communicator = LASADActionSender.getInstance();
//	private final ActionFactory actionBuilder = ActionFactory.getInstance();

	protected GraphMap myMap;
	private AbstractBox myBox;
	private AbstractAddElementToBoxDialog myDialog;

	public AbstractAddElementToBoxDialogListener(GraphMap map, AbstractBox box, AbstractAddElementToBoxDialog dialog) {
		this.myMap = map;
		this.myBox = box;
		this.myDialog = dialog;
	}

	public void onBrowserEvent(Event be) {
		if (be.getTypeInt() == Events.OnMouseOver.getEventCode()) {
			handleMouseOver(be);
		} else if (be.getTypeInt() == Events.OnClick.getEventCode()) {
			handleOnClick(be);
		} else if (be.getTypeInt() == Events.OnMouseOut.getEventCode()) {
			handleMouseOut(be);

		}
		be.stopPropagation();
	}

	private void handleOnClick(Event be) {
		// Get childelement with ElementIDs
		Element clickedElement = (Element) be.getEventTarget().cast();
		Element childTypeElement = clickedElement.getFirstChildElement();
		Element childIDElement = null;

		if (childTypeElement != null) {
			childIDElement = childTypeElement.getNextSiblingElement();
		}

		if (childTypeElement != null && childIDElement != null) {
			// Extract type
			String elementType = childTypeElement.getInnerText();
			// Extract elementID
			String elementID = childIDElement.getInnerText();

			// Updating server model
			//communicator.sendActionPackage(actionBuilder.addElement(myMap.getID(), myBox.getConnectedModel().getId(), elementType, elementID));
			onclickSendUpdate2Sever(myMap.getID(), myBox.getConnectedModel().getId(), elementType, elementID);
		}
		// else Cancel Element

		myDialog.removeFromParent();
	}
	protected abstract void onclickSendUpdate2Sever(String mapID, int boxID, String elementType, String elementID);

	private void handleMouseOut(Event be) {
		// End hover effect
		if (((Element) be.getEventTarget().cast()).getClassName().equals("dialog-text-highlighted")) {
			((Element) be.getEventTarget().cast()).setClassName("dialog-text");
		}
	}

	private void handleMouseOver(Event be) {
		// Start hover effect
		if (((Element) be.getEventTarget().cast()).getClassName().equals("dialog-text")) {
			((Element) be.getEventTarget().cast()).setClassName("dialog-text-highlighted");

		}
	}
}