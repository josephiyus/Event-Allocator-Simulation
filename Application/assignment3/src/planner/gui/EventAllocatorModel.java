package planner.gui;

import java.io.IOException;
import java.util.List;

import planner.Event;
import planner.FormatException;
import planner.Venue;
import planner.VenueReader;

/**
 * The model for the event allocator program.
 */
public class EventAllocatorModel {

	private String name;
	private String venueName;
	private int size;
	private String start;
    private String end;
    private int max;
    private int total;
    private Event event;
    private Venue venue;
    
    /**
     * Initialises the model for the event allocator program.
     */
    public EventAllocatorModel() {
        
    }
    
	public EventAllocatorModel(String start, String end, int max, int total) {
		super();
		this.start = start;
		this.end = end;
		this.max = max;
		this.total = total;
	}

	public EventAllocatorModel(Event event, Venue venue) {
		this.name = event.getName();
		this.size = event.getSize();
		this.venue = venue;
		this.event = event;
		this.venueName = this.venue.getName()+"("+this.venue.getCapacity()+")";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Venue> getAllRegiteredVenue(String fileName) throws IOException, FormatException{
		List<Venue> result = VenueReader.read(fileName);
		return result;
	}

	/**
     * Unallocate choosen venue from current allocated venue
     * 
     * @param venueName
     *            choosen venue will be unallocated
     * @param venues
     *            current allocated venues set will be choosen to deleted
     */
	public Venue removeVenue(String venueName, List<Venue> venues) {
		Venue result = null;
		if(venueName!=null && !venueName.trim().equals("")){
			for (Venue venue : venues) {
				if(venue.getName().equals(venueName)){
					result = venues.remove(venues.indexOf(venue));
					break;
				}
			}
		}	
		return result;
	}

	/**
     * Unallocate choosen venue from current allocated venue by event name
     * 
     * @param eventName
     *            choosen event name will be unallocated
     * @param events
     *            current allocated events set will be choosen to deleted
     */
	public Venue removeEvent(String eventName, int size, List<EventAllocatorModel> events) {
		Venue result = null;
		if(eventName!=null && !eventName.trim().equals("")){
			for (EventAllocatorModel event : events) {
				if(event.getName().equals(eventName)
						&& event.getSize() == size){
					event = events.remove(events.indexOf(event));
					result = event.getVenue();
					break;
				}
			}
		}	
		return result;
	}
	
	/**
     * Checks corridor availability in registered corridor set
     * 
     * @param other
     *            other model which contains corridor will be checked
     */
	public boolean isCorridor(EventAllocatorModel other){
		String thisCorridor = this.start.concat(this.end);
		String otherCorridor = other.start.concat(other.end);
		return thisCorridor.equals(otherCorridor);
	}
}
