package lasad.gwt.client.ui.common.argument.elements;

import java.util.Vector;

import lasad.gwt.client.communication.LASADActionSender;
import lasad.gwt.client.communication.helper.ActionFactory;
import lasad.gwt.client.model.ElementInfo;
import lasad.gwt.client.ui.common.ExtendedElementContainerInterface;
import lasad.gwt.client.ui.common.elements.AbstractExtendedUFrameElement;

public class ExtendedUFrameElementArgument extends
		AbstractExtendedUFrameElement {
	
	protected final LASADActionSender communicator = LASADActionSender.getInstance();
	protected final ActionFactory actionBuilder = ActionFactory.getInstance();

	public ExtendedUFrameElementArgument(
			ExtendedElementContainerInterface container, ElementInfo config,
			boolean readOnly, boolean dummy) {
		super(container, config, readOnly, dummy);
	}

	public ExtendedUFrameElementArgument(
			ExtendedElementContainerInterface container, ElementInfo config,
			boolean readonly) {
		super(container, config, readonly);
	}

	public ExtendedUFrameElementArgument(
			ExtendedElementContainerInterface container, ElementInfo config) {
		super(container, config);
	}

	@Override
	public void updateElementWithMultipleValues(String mapID, int id,
			Vector<Object[]> values) {
		communicator.sendActionPackage(actionBuilder.updateElementWithMultipleValues(mapID, id, values));
	}

}
