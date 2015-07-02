package lasad.gwt.client.ui.workspace.graphmap.elements;

import java.util.TreeMap;
import java.util.Vector;

import lasad.gwt.client.model.AbstractMVController;
import lasad.gwt.client.model.ElementInfo;
import lasad.gwt.client.ui.box.AbstractBox;
import lasad.gwt.client.ui.link.AbstractLink;
import lasad.gwt.client.ui.workspace.LASADInfo;
import lasad.shared.communication.objects.parameters.ParameterTypes;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Element;

import lasad.gwt.client.model.organization.ArgumentModel;
import lasad.gwt.client.model.organization.LinkedBox;
import lasad.gwt.client.model.organization.OrganizerLink;
import lasad.gwt.client.model.organization.ArgumentThread;
import lasad.gwt.client.model.organization.AutoOrganizer;
import lasad.gwt.client.logger.Logger;

/**
 * This class creates the dialog box for when the user selects to add a relation via the argument map drop down menu.
 * If creating links via dragging, see AbstractCreateLinkDialog (link only) and AbstractCreateBoxLinkDialog (link and box).
 * Documentation added by Kevin Loughlin, 16 June 2015
 * @author Unknown
 */

public abstract class AbstractCreateSpecialLinkDialog extends Window {

//	private final LASADActionSender communicator = LASADActionSender.getInstance();
//	private final ActionFactory actionBuilder = ActionFactory.getInstance();
//	private MVController myController;

	protected final int MAX_SIBLINGS = 2;

	protected FormData formData;
	protected SimpleComboBox<String> comboStart = new SimpleComboBox<String>();
	protected SimpleComboBox<String> comboEnd = new SimpleComboBox<String>();

	protected ElementInfo config;
	protected String correspondingMapId;

	// Maps ROOTELEMENTID to corresponding element
	private TreeMap<String, AbstractBox> boxes;
	private TreeMap<String, AbstractLink> links;

	public AbstractCreateSpecialLinkDialog(ElementInfo config, String mapId, TreeMap<String, AbstractBox> boxes, TreeMap<String, AbstractLink> links) {
		this.config = config;
		this.correspondingMapId = mapId;
		this.boxes = boxes;
		this.links = links;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		this.setAutoHeight(true);
		this.setWidth(200);
		this.setHeading("Add relation...");
		formData = new FormData("-20");
		createForm();
	}

	private void createForm() {
		FormPanel simple = new FormPanel();
		simple.setFrame(true);
		simple.setHeaderVisible(false);
		simple.setAutoHeight(true);

		// Fill combo boxes
		comboStart.setFieldLabel("<font color=\"#000000\">" + "Start" + "</font>");
		comboStart.setAllowBlank(false);
		for (String rootID : boxes.keySet()) {
			// TODO : Verify I'm only doing linked premises between premises
			// Be careful about RootID vs Box ID

			if (config.getElementID().equalsIgnoreCase("Linked Premises"))
			{
				ArgumentModel argModel = ArgumentModel.getInstanceByMapID(correspondingMapId);
				LinkedBox alpha = argModel.getBoxByRootID(Integer.parseInt(rootID));

				if (alpha.getNumSiblings() < MAX_SIBLINGS && alpha.getType().equalsIgnoreCase("Premise"))
				{
					comboStart.add(rootID);
				}
			}
			else
			{
				comboStart.add(rootID);
			}

			comboEnd.add(rootID);
		}
		simple.add(comboStart, formData);

		comboEnd.setFieldLabel("<font color=\"#000000\">" + "End" + "</font>");
		comboEnd.setAllowBlank(false);
		for (String rootID : links.keySet()) {
			comboEnd.add(rootID);
		}
		comboEnd.setEnabled(false);
		simple.add(comboEnd, formData);

		// Filter comboEnd depending on comboStart selection
		comboStart.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
				ArgumentModel argModel = ArgumentModel.getInstanceByMapID(correspondingMapId);

				// Important to use rootID
				LinkedBox startBox = argModel.getBoxByRootID(Integer.parseInt(comboStart.getRawValue()));

				comboEnd.setEnabled(true);

				boolean comboEndHasElements = false;

				if (getMyController() == null) {
//					myController = LASAD_Client.getMVCController(correspondingMapId);
					setMyController();
				}
				comboEnd.removeAll();

				Vector<String> restrictedIds = new Vector<String>();

				AutoOrganizer autoOrganizer = AutoOrganizer.getInstanceByMapID(correspondingMapId);

				//TODO (Marcel) unite following two loops
				for (String rootID : boxes.keySet()) {

					LinkedBox endBox = argModel.getBoxByRootID(Integer.parseInt(rootID));

					if (config.getElementID().equalsIgnoreCase("Linked Premises"))
					{
						OrganizerLink newLink = new OrganizerLink(startBox, endBox, config.getElementID());

						if (autoOrganizer.linkedPremisesCanBeCreated(newLink) != 0)
						{
							restrictedIds.add(rootID);
						}

					}
					if (rootID.equals(comboStart.getRawValue())) {
						restrictedIds.add(rootID);

						// It's not allowed to connect two boxes that already
						// are connected to each other
						for (String linkId : links.keySet()) {
							if (links.get(linkId).getConnectedModel().getParents().get(0).getValue(ParameterTypes.RootElementId).equals(comboStart.getRawValue())) {
								restrictedIds.add(links.get(linkId).getConnectedModel().getParents().get(1).getValue(ParameterTypes.RootElementId));
							} else if (links.get(linkId).getConnectedModel().getParents().get(1).getValue(ParameterTypes.RootElementId).equals(comboStart.getRawValue())) {
								restrictedIds.add(links.get(linkId).getConnectedModel().getParents().get(0).getValue(ParameterTypes.RootElementId));
							}
						}
					}
				}

				for (String rootID : boxes.keySet()) {
					if (!restrictedIds.contains(rootID)) {
						comboEnd.add(rootID);
						if (comboEndHasElements == false) comboEndHasElements = true;
					}
				}

				for (String id : links.keySet()) {
					// It's not allowed to have a link from a box to its
					// connected links
					if (!(links.get(id).getConnectedModel().getParents().get(0).getValue(ParameterTypes.RootElementId).equals(comboStart.getRawValue()) || links.get(id).getConnectedModel().getParents().get(1).getValue(ParameterTypes.RootElementId).equals(comboStart.getRawValue()))) {
						comboEnd.add(id);
						if (comboEndHasElements == false) comboEndHasElements = true;
					}
				}

				if (!comboEndHasElements) {
					LASADInfo.display("Error", "No more connections available for this starting element.");
				}
			}
		});

		comboEnd.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
				ArgumentModel argModel = ArgumentModel.getInstanceByMapID(correspondingMapId);
				LinkedBox endBox = argModel.getBoxByRootID(Integer.parseInt(comboEnd.getRawValue()));

				comboStart.removeAll();

				AutoOrganizer autoOrganizer = AutoOrganizer.getInstanceByMapID(correspondingMapId);

				for (String rootID : boxes.keySet()) {

					LinkedBox startBox = argModel.getBoxByRootID(Integer.parseInt(rootID));

					if (config.getElementID().equalsIgnoreCase("Linked Premises"))
					{
						OrganizerLink newLink = new OrganizerLink(startBox, endBox, "Linked Premises");

						if (autoOrganizer.linkedPremisesCanBeCreated(newLink) == 0)
						{
							comboStart.add(rootID);
						}
					}
					else
					{
						if (!rootID.equals(comboEnd.getRawValue())) {
							comboStart.add(rootID);
						}
					}	
				}
			}
		});

		// Okay Button
		Button btnOkay = new Button("Ok");
		btnOkay.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				String startId = Integer.toString(boxes.get(comboStart.getRawValue()).getConnectedModel().getId());
				String endId = "";
				if (boxes.containsKey(comboEnd.getRawValue())) {
					endId = Integer.toString(boxes.get(comboEnd.getRawValue()).getConnectedModel().getId());
				} else {
					endId = Integer.toString(links.get(comboEnd.getRawValue()).getConnectedModel().getId());
				}
				//communicator.sendActionPackage(actionBuilder.createLinkWithElements(config, correspondingMapId, startId, endId));
				onClickSendCreateLinkWithElementsToServer(config, correspondingMapId, startId, endId);
				AbstractCreateSpecialLinkDialog.this.hide();
			}
		});
		simple.addButton(btnOkay);

		// Cancel Button
		Button btnCancel = new Button("Cancel");
		btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				AbstractCreateSpecialLinkDialog.this.hide();
			}
		});
		simple.addButton(btnCancel);

		simple.setButtonAlign(HorizontalAlignment.CENTER);
		FormButtonBinding binding = new FormButtonBinding(simple);
		binding.addButton(btnOkay);

		this.add(simple);
	}
	protected abstract void onClickSendCreateLinkWithElementsToServer(ElementInfo info, String mapID, String startElementID, String endElementID);
	protected abstract  AbstractMVController getMyController();
	protected abstract void setMyController();

	protected TreeMap<String, AbstractBox> getBoxes() {
		return boxes;
	}

	protected TreeMap<String, AbstractLink> getLinks() {
		return links;
	}

}
