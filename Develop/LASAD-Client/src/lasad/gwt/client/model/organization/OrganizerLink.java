package lasad.gwt.client.model.organization;
import lasad.gwt.client.model.organization.LinkedBox;

// Doesn't work --- import lasad.database.DatabaseConnectionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OrganizerLink is, like LinkedBox is to AbstractBox, a way of representing an AbstractLinkPanel (and/or an AbstractLink) that is more
 * friendly to AutoOrganizer.  The important info for updating a link via an Action is contained within an OrganizerLink.
 * @author Kevin Loughlin
 * @since 17 June 2015, Updated 24 June 2015
 */
public class OrganizerLink
{
	// Be mindful of difference between boxID and rootID.
	private LinkedBox startBox;
	private LinkedBox endBox;

	// I.e. what kind of relation (perhaps it could be support, refutation, Linked Premises, depending on the ontology and its terminology)
	private String type;

	// Helps with removal of links, otherwise not needed
	private int linkID;

	/**
	 * Constructor
	 */
	public OrganizerLink(int linkID, LinkedBox startBox, LinkedBox endBox, String type)
	{
		this.linkID = linkID;
		this.startBox = startBox;
		this.endBox = endBox;
		this.type = type;
	}

	/**
	 * I'm trying to get it so that the next available ID is given to this link, but I can't import the commented out library, so
	 * I don't know what's going on.  Hence the commented out code in this constructor.
	 */
	public OrganizerLink(LinkedBox startBox, LinkedBox endBox, String type)
	{
		this.linkID = -1;
		this.startBox = startBox;
		this.endBox = endBox;
		this.type = type;
	}

	// Don't use the default constructor, hence why it's set as private and does nothing other than shutting the compiler up
	private OrganizerLink()
	{
		this.linkID = -1;
	}

	/* Gets */

	public int getLinkID()
	{
		return linkID;
	}

	public LinkedBox getStartBox()
	{
		return startBox;
	}

	public LinkedBox getEndBox()
	{
		return endBox;
	}

	public int getStartBoxID()
	{
		return startBox.getBoxID();
	}

	public int getEndBoxID()
	{
		return endBox.getBoxID();
	}

	public int getStartBoxRootID()
	{
		return startBox.getRootID();
	}

	public int getEndBoxRootID()
	{
		return endBox.getRootID();
	}

	public String getType()
	{
		return type;
	}

	/* Sets */

	public void setStartBox(LinkedBox startBox)
	{
		this.startBox = startBox;
	}

	public void setEndBox(LinkedBox endBox)
	{
		this.endBox = endBox;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		return ((this.startBox.getBoxID())^3 + (this.endBox.getBoxID()^3));
	}

	public OrganizerLink clone()
	{
		return new OrganizerLink(this.getLinkID(), this.getStartBox(), this.getEndBox(), this.getType());
	}

	/* BoxIDs should be unique and thus just checking the start and end as well as type should be sufficient for equality.
		If the invariants of an ArgumentMap change, then this method will need to be updated accordingly. */
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof OrganizerLink)
		{
			OrganizerLink link = (OrganizerLink) object;
			if (startBox.equals(link.getStartBox()) && endBox.equals(link.getEndBox()) && link.getType().equalsIgnoreCase(type))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	//TODO not necessary for my purposes right now, but a toString method is always nice. Self reminder to come back and write it.
}