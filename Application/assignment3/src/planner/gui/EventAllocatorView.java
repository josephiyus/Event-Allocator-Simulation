package planner.gui;

import java.util.Collections;
import java.util.Comparator;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import planner.Venue;

/**
 * The view for the event allocator program.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EventAllocatorView {

    // the model of the event allocator
    private EventAllocatorModel model;
    private Scene mainScene;
    private Scene addScene;
    private Scene deleteScene;
    private Scene messageScene;
    private TableView tableEvents;
    private TableView tableTraffics;
    private Stage addStage;
    private Stage deleteStage;
    private Stage messageStage;
    private Label note;
    private ComboBox cbVenues;
    private ComboBox cbEvent;
    private TextField txtEvent;
    private TextField txtSize;
    private EventAllocatorController controller;
    private Button btnOk;

    /**
     * Initialises the view for the event allocator program.
     * 
     * @param model
     *            the model of the event allocator
     */
    public EventAllocatorView(EventAllocatorModel model) {
    	this.model = model;
    }
    
    /**
     * Initialises all required object for building all necessary GUI
     */
    public void init() {    	
        this.note = new Label();
        this.btnOk = new Button("OK");
        this.txtEvent = new TextField();
        this.txtSize = new TextField();
        this.tableEvents = new TableView<>();
        this.tableTraffics = new TableView<>();
        this.mainScene = createViewScene();
        this.addScene = createAddScene(); 
        this.deleteScene = createDeleteScene(); 
        this.messageScene = createMessageScene();        
	}
    
    /**
     * Builds main screen GUI
     */
	private Scene createViewScene() {
    	GridPane grid = createGridPane();
    	Scene scene = new Scene(grid);
    	Button btnAdd = new Button("Add");
		Button btnDelete = new Button("Delete");
		Label label = new Label("Event Alocator");
		label.setFont(new Font("Arial", 30));
		
		HBox hbButtons = new HBox();
		HBox hbScroll = new HBox();
		
	    hbButtons.setSpacing(10.0);
	    hbScroll.setSpacing(10.0);	
		
		hbButtons.getChildren().addAll(btnAdd, btnDelete);
		
		ScrollPane scrollEvents = new ScrollPane(tableEvents);
		scrollEvents.setPrefSize(460, 300);
		scrollEvents.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		ScrollPane scrollTraffics = new ScrollPane(tableTraffics);
		scrollTraffics.setPrefSize(340, 300);
		scrollTraffics.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		hbScroll.getChildren().addAll(scrollEvents, scrollTraffics);
		
	    tableEvents.setEditable(false);
	    tableEvents.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
	    tableTraffics.setEditable(false);
	    tableTraffics.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
	    
	    TableColumn eventsCol = new TableColumn<>("Events");
	    TableColumn trafficsCol = new TableColumn<>("Traffics");
	    
	    TableColumn namesCol = new TableColumn<>("Name");
	    namesCol.setMinWidth(180);
	    namesCol.setCellValueFactory(new PropertyValueFactory<>("name"));
	    
	    TableColumn sizeCol = new TableColumn<>("Size");
	    sizeCol.setMinWidth(50);
	    sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
	    
	    TableColumn capacitysCol = new TableColumn<>("Venue Name (Capacity)");
	    capacitysCol.setMinWidth(180);
	    capacitysCol.setCellValueFactory(new PropertyValueFactory<>("venueName"));
	    
	    TableColumn startCol = new TableColumn<>("Start");
	    startCol.setMinWidth(50);
	    startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
	    
	    TableColumn endCol = new TableColumn<>("End");
	    endCol.setMinWidth(50);
	    endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
	    
	    TableColumn maxCol = new TableColumn<>("Max");
	    maxCol.setMinWidth(50);
	    maxCol.setCellValueFactory(new PropertyValueFactory<>("max"));
	    
	    TableColumn totalCol = new TableColumn<>("Total");
	    totalCol.setMinWidth(50);
	    totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
	    
	    eventsCol.getColumns().addAll(namesCol, sizeCol, capacitysCol);
	    trafficsCol.getColumns().addAll(startCol, endCol, maxCol, totalCol);
	    
	    tableEvents.getColumns().addAll(eventsCol);
	    tableTraffics.getColumns().addAll(trafficsCol);
	    
	    grid.add(label, 0, 0);
	    grid.add(new Separator(), 0, 1);		
		grid.add(hbScroll, 0, 2);
		grid.add(new Separator(), 0, 3);
		grid.add(hbButtons, 0, 4);
		
		btnAdd.setOnAction(
				e -> showAddScene()
		);
		
		btnDelete.setOnAction(
				e -> showDeleteScene()
		);
		
		btnAdd.setFont(new Font("Arial", 20));
		btnDelete.setFont(new Font("Arial", 20));
		return scene;
	}

	/**
     * Builds add screen GUI
     */
	private Scene createAddScene() {
    	GridPane grid = createGridPane();
    	Scene scene = new Scene(grid);
    	this.cbVenues = new ComboBox();
    	HBox hbVenues = new HBox();
		HBox hbEvent = new HBox();
		HBox hbSize = new HBox();
		HBox hbButtons = new HBox();
		
		Label lblHeader = new Label("Add New Event Alocator");
		Label lblVenue = new Label("Venue");
		Label lblEvent = new Label("Event Name");
		Label lblSize = new Label("Event Size");
		
		lblHeader.setFont(new Font("Arial", 30));
		lblVenue.setFont(new Font("Arial", 20));
		lblEvent.setFont(new Font("Arial", 20));
		lblSize.setFont(new Font("Arial", 20));
		
		Button btnSave = new Button("Save");
		Button btnClose = new Button("Close");
		
		btnSave.setOnAction(e -> controller.saveNewEvent());
		btnClose.setOnAction(e -> closeAddScene());		
		
		grid.add(lblHeader, 0, 0);
		grid.add(new Separator(), 0, 1);
	    grid.add(hbVenues, 0, 2);
	    grid.add(hbEvent, 0, 3);
	    grid.add(hbSize, 0, 4);
		grid.add(hbButtons, 0, 5);	
		
		hbVenues.setSpacing(10.0);
		hbEvent.setSpacing(10.0);
		hbSize.setSpacing(10.0);
		hbButtons.setSpacing(10.0);
		
		hbEvent.getChildren().addAll(lblEvent, txtEvent);
		hbSize.getChildren().addAll(lblSize, txtSize);
	    hbButtons.getChildren().addAll(btnSave, btnClose);
		hbVenues.getChildren().addAll(lblVenue, this.cbVenues);
		
		return scene;
	}
    
	/**
     * Builds delete screen GUI
     */
    private Scene createDeleteScene() {
    	GridPane grid = createGridPane();
    	Scene scene = new Scene(grid);
    	
    	this.cbEvent = new ComboBox<>();
    	Label lblHeader = new Label("Delete Alocated Event");
		Label lblEvent = new Label("Event Name");
		
		lblHeader.setFont(new Font("Arial", 30));
		lblEvent.setFont(new Font("Arial", 20));
		
		Button btnDelete = new Button("Delete");
		Button btnClose = new Button("Close");
		
		HBox hbButtons = new HBox();
		HBox hbEvents = new HBox();
		
		btnDelete.setOnAction(e -> controller.deleteAllocatedEvent());
		btnClose.setOnAction(e -> closeDeleteScene());		
		
	    hbButtons.setSpacing(10.0);
	    hbButtons.getChildren().addAll(btnDelete, btnClose);
	    hbEvents.setSpacing(10.0);
	    hbEvents.getChildren().addAll(lblEvent, cbEvent);
	    grid.add(lblHeader, 0, 0);
	    grid.add(new Separator(), 0, 1);
		grid.add(hbEvents, 0, 2);
		grid.add(hbButtons, 0, 3);
    	
		return scene;
	}
    
    /**
     * Builds message box screen GUI
     */
    private Scene createMessageScene(){
    	GridPane grid = createGridPane();
    	Scene scene = new Scene(grid, 800, 150);    	
    	
		note.setFont(new Font("Arial", 30));		
		
		btnOk.setOnAction(e -> closeMessageScene());
		
		grid.add(note, 0, 0);
		grid.add(btnOk, 0, 1);
    	
    	return scene;
    }
    
    /**
     * The action for showing add screen GUI
     */
    private void showAddScene(){
    	if(controller.getVenues()!=null){
			cbVenues.getItems().clear();
			for (Venue venue : controller.getVenues()) {
				cbVenues.getItems().add(venue.getName() + " (" + venue.getCapacity() + ")");
			}
		}
    	getTxtEvent().setText("");
    	getTxtSize().setText("");
		addStage = new Stage();
		addStage.setScene(addScene);
		addStage.setTitle("Add New Event");
		addStage.show();
	}
	
    /**
     * The action for showing delete screen GUI
     */
	private void showDeleteScene(){
		if(controller.getEvents()!=null){
			cbEvent.getItems().clear();
			for (EventAllocatorModel event : controller.getEvents()) {
				cbEvent.getItems().add(event.getName()+"("+event.getSize()+")");
			}
		}
		deleteStage = new Stage();
		deleteStage.setScene(deleteScene);
		deleteStage.setTitle("Delete Current Allocated Event");
		deleteStage.show();
	}
	
	/**
     * The action for showing message screen GUI
     * 
     * @param title
     *            the parameter of upper title text
     * @param message
     *            the parameter of content message text
     */
	public void displayMessage(String title, String message){
		messageStage = new Stage();
		messageStage.setScene(messageScene);
		messageStage.setTitle(title);
		btnOk.setOnAction(e -> closeMessageScene());
		note.setText(message);
		messageStage.show();
	}
	
	/**
     * The action for showing message screen GUI and enable to Exit program
     * 
     * @param title
     *            the parameter of upper title text
     * @param message
     *            the parameter of content message text
     */
	public void displayMessageAndExit(String title, String message){
		messageStage = new Stage();
		messageStage.setScene(messageScene);
		messageStage.setTitle(title);
		btnOk.setOnAction(e -> exitApp());
		note.setText(message);
		messageStage.show();
	}
	
	/**
     * Shuts down of the application
     */
	private void exitApp() {
		closeMessageScene();
		Platform.exit();
	}

	/**
     * The action for showing message firstly application booting
     * 
     * @param title
     *            the parameter of upper title text
     * @param message
     *            the parameter of content message text
     */
	public void displayFirstMessage(String title, String message){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				displayMessageAndExit(title,  message);
			}
		});
	}
	
	/**
     * Closing add screen GUI
     */
	public void closeAddScene(){
		addStage.close();
	}
	
	/**
     * Closing delete screen GUI
     */
	public void closeDeleteScene(){
		deleteStage.close();
	}
	
	/**
     * Closing message screen GUI
     */
	public void closeMessageScene(){
		messageStage.close();
	}
    
	/**
     * Returns the scene for the event allocator application.
     * 
     * @return returns the scene for the application
     */
    public Scene getScene() {
        return mainScene;
    }
    
    /**
     * Create common screen GUI foundation
     */
    private GridPane createGridPane() {
    	GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setPadding(new Insets(10));
		return grid;
	}

	public EventAllocatorModel getModel() {
		return model;
	}

	public void setModel(EventAllocatorModel model) {
		this.model = model;
	}

	public Scene getMainScene() {
		return mainScene;
	}

	public void setMainScene(Scene mainScene) {
		this.mainScene = mainScene;
	}

	public Scene getAddScene() {
		return addScene;
	}

	public void setAddScene(Scene addScene) {
		this.addScene = addScene;
	}

	public Scene getDeleteScene() {
		return deleteScene;
	}

	public void setDeleteScene(Scene deleteScene) {
		this.deleteScene = deleteScene;
	}

	public TableView getTable() {
		return tableEvents;
	}

	public void setTable(TableView table) {
		this.tableEvents = table;
	}

	public EventAllocatorController getController() {
		return controller;
	}

	public void setController(EventAllocatorController controller) {
		this.controller = controller;
	}

	public ComboBox getCbVenues() {
		return cbVenues;
	}

	public void setCbVenues(ComboBox cbVenues) {
		this.cbVenues = cbVenues;
	}

	public ComboBox getCbEvent() {
		return cbEvent;
	}

	public void setCbEvent(ComboBox cbEvent) {
		this.cbEvent = cbEvent;
	}

	public TextField getTxtEvent() {
		return txtEvent;
	}

	public void setTxtEvent(TextField txtEvent) {
		this.txtEvent = txtEvent;
	}

	public TextField getTxtSize() {
		return txtSize;
	}

	public void setTxtSize(TextField txtSize) {
		this.txtSize = txtSize;
	}

	/**
     * Rebuilds the content of events and traffics table
     */
	public void refreshTable() {		
		try {
			tableTraffics.getItems().clear();
			tableTraffics.getItems().addAll(controller.getTraffics());
			tableTraffics.refresh();
			
			Collections.sort(controller.getEvents(), new Comparator<EventAllocatorModel>() {
	    		@Override
				public int compare(EventAllocatorModel o1, EventAllocatorModel o2) {
					String name1 = o1.getEvent().getName();
					String name2 = o2.getEvent().getName();
	    			int comp = name1.compareTo(name2);
					if(comp!=0){
	    				return comp;
	    			}else{
	    				if(o1.getSize()>o2.getSize()){
	    					return 1;
	    				}else{
	    					return -1;
	    				}
	    			}
				}
			});
			
			tableEvents.getItems().clear();
			tableEvents.getItems().addAll(controller.getEvents());
			tableEvents.refresh();
		} catch (IllegalArgumentException e) {
			displayMessage("Error", e.getMessage());
			controller.rollback();			
		}
			
	}
}
