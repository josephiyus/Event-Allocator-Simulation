package planner.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import planner.Corridor;
import planner.Event;
import planner.FormatException;
import planner.Traffic;
import planner.Venue;

/**
 * The controller for the event allocator program.
 */
public class EventAllocatorController {

    // the model of the event allocator
    private EventAllocatorModel model;
    
    // the view of the event allocator
    private EventAllocatorView view;
    
    // the list of the venue variable
    private List<Venue> venues;
    
    // the temporary of the message variable
    private String messageError = "";

    // the list of the mapped event and venue variable
    private List<EventAllocatorModel> events;
    
    private EventAllocatorModel newEvent = null;
    
    /**
     * Initialises the controller for the event allocator program.
     * 
     * @param model
     *            the model of the event allocator
     * @param view
     *            the view of the event allocator
     */
    public EventAllocatorController(EventAllocatorModel model,
            EventAllocatorView view) {    	
        this.model = model;
        this.view = view;
        this.view.setController(this);
        this.view.init();
        try {
        	this.venues = model.getAllRegiteredVenue("venues.txt");
		} catch (FormatException e) {
			messageError = e.getMessage();
		} catch (IOException e) {
			messageError = e.getMessage();
		} catch (Exception e) {
			messageError = e.getMessage();
		} finally {
			if(messageError!=""){
				Thread th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						showErrorMessage();
					}
				});
				th.setDaemon(true);
				th.start();	
			}
		}        
    }

    /**
     * The action for mapping between new event and available venue in add process
     */
    public void saveNewEvent(){    	
		try {
			String eventName = null;
			String size = "0";
			String venueName = "";
			int sizeInt = 0;
			if(view.getCbVenues().getValue()!=null){				
				if(!view.getTxtEvent().getText().trim().isEmpty()){
					eventName = view.getTxtEvent().getText();
				}else{
					throw new NullPointerException("The event name cannot be null.");
				}
				
				if(!view.getTxtSize().getText().trim().isEmpty()){
					size = view.getTxtSize().getText();
					try {
						sizeInt = Integer.parseInt(size);
					} catch (Exception e) {
						throw new FormatException("The size have to be number format");
					}
				}else{
					throw new NullPointerException("The size cannot be null.");
				}
				
				Event event = null;
				try {
					event = new Event(eventName, sizeInt);
				} catch (IllegalArgumentException e) {
					throw new Exception(e.getMessage());
				}				
							
				if(isExistEvent(event, events)){
					throw new Exception("Equal event has been allocated");
				}
				
				venueName = view.getCbVenues().getValue().toString();
				venueName = venueName.split("\\(")[0].trim();
				Venue choosenVenue = model.removeVenue(venueName, venues);					
				
				newEvent = new EventAllocatorModel(event, choosenVenue);
				if(events==null){
					events = new ArrayList<EventAllocatorModel>();
				}
				events.add(newEvent);
				view.refreshTable();
				view.closeAddScene();
			}else{
				throw new NullPointerException("The venue cannot be null.");
			}			
		} catch (FormatException e) {
			view.displayMessage("Error", e.getMessage());
		} catch (IllegalArgumentException e) {		
			rollback();
			view.displayMessage("Error", e.getMessage());
		} catch (NullPointerException e) {
			view.displayMessage("Error", e.getMessage());
		} catch (Exception e) {
			view.displayMessage("Error", e.getMessage());
		}
	}
    
    /**
     * checking existence of new event in current allocated events
     * 
     * @param event
     *            the model of the new event which will be allocated
     * @param models
     *            the set of current allocated events
     */
    private boolean isExistEvent(Event event, List<EventAllocatorModel> models) {
		boolean result = false;
		if(models!=null){
			for (EventAllocatorModel model : models) {
				if(model.getEvent().getName().equals(event.getName())
						&& model.getSize()==event.getSize()){
					result = true;
					break;
				}
			}
		}
		return result;
	}

	public void deleteAllocatedEvent(){
		try {
			String eventName = null;
			try {
				eventName = view.getCbEvent().getValue().toString();
			} catch (Exception e) {
				throw new Exception("The event name has to be choosen");
			}
			int size = Integer.parseInt(eventName.split("\\(")[1].replaceAll("\\)", ""));
			eventName = eventName.split("\\(")[0].trim();
			
			Venue unAllocatedVenue = model.removeEvent(eventName, size, events);
			
			if(venues==null){
				venues = new ArrayList<Venue>();
			}
			venues.add(unAllocatedVenue);

			view.refreshTable();
			view.closeDeleteScene();
		} catch (Exception e) {
			view.displayMessage("Error", e.getMessage());
		}
	}
    
	/**
     * Generate traffic set of the current allocated event set
     */
    public List<EventAllocatorModel> getTraffics() throws IllegalArgumentException{
    	List<EventAllocatorModel> result = new ArrayList<EventAllocatorModel>();
    	for (EventAllocatorModel mappedEvent : events) {
    		Traffic traffic = mappedEvent.getVenue().getTraffic(mappedEvent.getEvent());
    		Set<Corridor> corridors = traffic.getCorridorsWithTraffic();
    		for (Corridor corridor : corridors) {
    			String start = corridor.getStart().getName();
    			String end = corridor.getEnd().getName();
    			int max = corridor.getCapacity();
    			int total = traffic.getTraffic(corridor);
    			EventAllocatorModel model = new EventAllocatorModel(start, end, max, total);
    			model.setEvent(mappedEvent.getEvent());
    			model.setVenue(mappedEvent.getVenue());
    			try {
					if(isExistCorridor(model, result)==false){
						result.add(model);
					}
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("The traffic caused by the allocation is unsafe");
				}
			}
    	}
    	
    	//sorting for Traffic Table
    	Collections.sort(result, new Comparator<EventAllocatorModel>() {
    		@Override
			public int compare(EventAllocatorModel o1, EventAllocatorModel o2) {
    			int comp = o1.getStart().compareTo(o2.getStart());
    			if(comp==0){
    				comp = o1.getEnd().compareTo(o2.getEnd());  
    				if(comp==0){
        				if(o1.getMax() == o2.getMax()){
        					if(o1.getTotal() == o2.getTotal()){
            					comp = 0;
            				}else if(o1.getMax() > o2.getMax()){
            					comp = 1;
            				}else{
            					comp = -1;
            				}
        				}else if(o1.getMax() > o2.getMax()){
        					comp = 1;
        				}else{
        					comp = -1;
        				}
        			}
    			}
    			return comp;
			}
		});
		return result;
	}
    
    /**
     * Checks existence of new corridor from current used corridor
     * 
     * @param newModel
     *            the model of the new allocated event contains new corridors
     * @param models
     *            the allocated events model contains registered corridors
     */
    private boolean isExistCorridor(EventAllocatorModel newModel, List<EventAllocatorModel> models) throws IllegalArgumentException {
		boolean result = false;
    	for (EventAllocatorModel model : models) {
			if(model.isCorridor(newModel)){				
				int sumSize = model.getTotal() + newModel.getTotal();				
				if(model.getMax() < sumSize){
					throw new IllegalArgumentException();
				}else{
					model.setTotal(sumSize);
				}
				result = true;
				break;
			}
		}
		return result;
	}

    /**
     * Calling message view for showing common message
     */
	public void showErrorMessage(){
    	view.displayFirstMessage("Error", messageError);
    }
    
	public EventAllocatorView getView() {
		return view;
	}

	public void setView(EventAllocatorView view) {
		this.view = view;
	}

	public List<Venue> getVenues() {
		return venues;
	}

	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}

	public List<EventAllocatorModel> getEvents() {
		return events;
	}

	public void setEvents(List<EventAllocatorModel> events) {
		this.events = events;
	}

	/**
     * Canceling new allocated event
     */
	public void rollback() {
		venues.add(newEvent.getVenue());
		events.remove(events.indexOf(newEvent));
		view.refreshTable();
	}

}
