/**
 * Sample Skeleton for 'main_form.fxml' Controller Class
 */

package demo;
import java.time.LocalDate;
import java.util.Map;

import hikingHistory.HikingHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import trail_module.Trail;
import user_module.UserProfile;
import utilities.Util;

public class MainController {

    @FXML // fx:id="guidLabel"
    private Label guidLabel; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLabel"
    private Label usernameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="trailCompletedLabel"
    private Label trailCompletedLabel; // Value injected by FXMLLoader

    @FXML // fx:id="totalDistanceLabel"
    private Label totalDistanceLabel; // Value injected by FXMLLoader

    @FXML // fx:id="totalDurationTabel"
    private Label totalDurationTabel; // Value injected by FXMLLoader

    @FXML // fx:id="longestHikeLabel"
    private Label longestHikeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="historyTable"
    private TableView<HikingHistory> historyTable; // Value injected by FXMLLoader
    
    @FXML // fx:id="trailTable"
    private TableView<Trail> trailTable; // Value injected by FXMLLoader
    
    @FXML // fx:id="trailSearchBox"
    private TextField trailSearchBox; // Value injected by FXMLLoader

    @FXML // fx:id="hikeTrailBtn"
    private Button hikeTrailBtn; // Value injected by FXMLLoader
    
    @FXML // fx:id="remSelectedUserTrail"
    private Button remSelectedUserTrail; // Value injected by FXMLLoader

    @FXML // fx:id="clearUserTrailsBtn"
    private Button clearUserTrailsBtn; // Value injected by FXMLLoader

    @FXML // fx:id="createTrailBtn"
    private Button createTrailBtn; // Value injected by FXMLLoader

    @FXML // fx:id="deleteSelectedTrailBtn"
    private Button deleteSelectedTrailBtn; // Value injected by FXMLLoader
    
    @FXML
    private void initialize()
    {	
    	// search results?
    	trailSearchBox.setOnKeyReleased(x -> searchTrails());
    	hikeTrailBtn.setOnAction(x -> hikeBtnAction());
    	remSelectedUserTrail.setOnAction(x -> remSelectedUserTrailAction());
    	clearUserTrailsBtn.setOnAction(x-> clearUserTrailsAction());
    	deleteSelectedTrailBtn.setOnAction(x -> deleteSelectedTrailAction());
    	createTrailBtn.setOnAction( x-> createTrailAction());
    	
    	// populate with the main trail list
    	populateTrailView();
    	
    	// populate data from user class
    	populateHistoryView();
    	populateUserStats();
    }
    
    private void createTrailAction()
    {
    	// is user an admin?
    	if( !UserProfile.loggedInUser.getIsAdmin() )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: User is NOT an administrator.");
    		alert.show();
    		return;
    	}
    	
    	// display form!
    	try
    	{
    		Parent root = FXMLLoader.load(getClass().getResource("create_form.fxml"));
    		Stage stage = new Stage();
    		stage.setTitle("Creation Form");
    		stage.setScene(new Scene(root));
    		stage.getScene().getStylesheets().add(getClass().getResource("comboBox.css").toExternalForm());
    		stage.setResizable(false);    		
    		
    		// disable login btn until closed
    		createTrailBtn.setDisable(true);
    		stage.showAndWait();
    		createTrailBtn.setDisable(false);
    		
    		// repopulate
    		populateTrailView();
    	}
    	catch( Exception e ) {}
    }
    
    private void deleteSelectedTrailAction()
    {
    	Trail trail = trailTable.getSelectionModel().getSelectedItem();
    	if( trail == null )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: No trail selected.");
    		alert.show();
    		return;
    	}
    	
    	// is user admin?
    	if( !UserProfile.loggedInUser.getIsAdmin() )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: User is NOT an administrator.");
    		alert.show();
    		return;
    	}
    	
    	// delete it from the TreeMap
    	Trail.MainTreeMap.remove(trail.getTrailName());
    	
    	// save changes
    	Trail.writeTrailsToFile();
    	
    	// repopulate
    	populateTrailView();
    }
    
    private void clearUserTrailsAction()
    {
    	// does user have any trails to clear?
    	if( UserProfile.loggedInUser.hikingHistoryList.getFirst() == null )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: User has no history to clear.");
    		alert.show();
    		return;
    	}
    	
    	// clear list!
    	UserProfile.loggedInUser.hikingHistoryList.clear();
    	
    	// save changes
    	UserProfile.writeUsersToFile();
    	
    	// refresh content
    	populateUserStats();
    	populateHistoryView();
    }
    
    // on action event for remove selected user trail btn
    private void remSelectedUserTrailAction()
    {
    	HikingHistory trail = historyTable.getSelectionModel().getSelectedItem();
    	if( trail == null )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: No user trail selected.");
    		alert.show();
    		return;
    	}
    	
    	// remove selected trail from the user!
    	UserProfile.loggedInUser.hikingHistoryList.remove(trail);
    	
    	// save changes
    	UserProfile.writeUsersToFile();
    	
    	// refresh content
    	populateUserStats();
    	populateHistoryView();
    }
    
    private void hikeBtnAction()
    {
    	Trail trail = trailTable.getSelectionModel().getSelectedItem();
    	if( trail == null )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: No trail selected.");
    		alert.show();
    		return;
    	}
    	
    	// trail was found. hike it!
    	float milesPerHour = 2;
    	milesPerHour = (float) (milesPerHour + Math.random() * milesPerHour); // just to give some flavor
    	float totalDurationHours = milesPerHour * trail.getLength();
    	float totalDurationDays = totalDurationHours / 24f;
    	totalDurationDays = Util.round(totalDurationDays, 2);
    	
    	// random amount of pics!
    	int numPics = (int)(Math.random() * 5 + 1);
    	
    	// save changes
    	HikingHistory history = new HikingHistory(trail.getTrailName(), LocalDate.now(), trail.getLength(), totalDurationDays, numPics);
    	UserProfile.loggedInUser.hikingHistoryList.add(history);
    	UserProfile.writeUsersToFile();
    	
    	// display it!
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Trail hiked successfully!\nYou managed to hike " 
    	+ trail.getLength() + " miles in " + totalDurationDays 
    	+ " days!\nHiked Trail: " + trail.getTrailName() 
    	+ "\nInitial Address: " + trail.getTrailHeadAddress()
    	+ "\nAverage Pace: " + Util.round(history.getAveragePace(), 2) + " mph"
    	+ "\n\nYou managed to take " + numPics + " pictures!");
		alert.showAndWait();
		
		// update table
		populateUserStats();
		populateHistoryView();
    }
    
    private void populateUserStats()
    {
    	if( UserProfile.loggedInUser == null ) return;
    	
    	// set stats
    	usernameLabel.setText(UserProfile.loggedInUser.getUsername());
    	guidLabel.setText(UserProfile.loggedInUser.GetUserId());
    	trailCompletedLabel.setText(UserProfile.loggedInUser.GetCompletedTrails() + "");
    	
    	float totalDistance = 0f;
    	float longestHike = 0f;
    	float totalDuration = 0f;
    	for( HikingHistory history : UserProfile.loggedInUser.hikingHistoryList )
    	{
    		totalDistance += history.getDistance();
    		totalDuration += history.getDuration();
    		if( history.getDistance() > longestHike )
    		{
    			longestHike = history.getDistance();
    		}
    	}
    	totalDistanceLabel.setText(totalDistance + " Mi");
    	totalDurationTabel.setText(Util.round(totalDuration, 2) + " Days");
    	longestHikeLabel.setText(longestHike + " Mi");
    }
    
    private void searchTrails()
    {
    	// search through trails for containing info
        ObservableList<Trail> data = FXCollections.observableArrayList();
        for(Map.Entry<String,Trail> entry : Trail.MainTreeMap.entrySet()) 
        {
			String key = entry.getKey();
			Trail value = entry.getValue();
			
			// does name contain it?
			if( key.toLowerCase().contains(trailSearchBox.getText().toLowerCase()))
				data.add(value);
			else if( value.getTrailHeadAddress().toLowerCase().contains(trailSearchBox.getText().toLowerCase()))
				data.add(value);
    	}
        trailTable.setItems(data);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateTrailView()
	{
    	//Creating columns
        TableColumn trailNameCol = new TableColumn("Name");
        trailNameCol.setPrefWidth(110);
        trailNameCol.setCellValueFactory(new PropertyValueFactory<>("trailName"));
        trailNameCol.setResizable(false);
        trailNameCol.setReorderable(false);
        
        TableColumn addressCol = new TableColumn("Address");
        addressCol.setPrefWidth(110);
        addressCol.setCellValueFactory(new PropertyValueFactory<>("trailHeadAddress"));
        addressCol.setResizable(false);
        addressCol.setReorderable(false);
        
        TableColumn lengthCol = new TableColumn("Distance (Mi)");
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        lengthCol.setResizable(false);
        lengthCol.setReorderable(false);
        
        TableColumn elevationCol = new TableColumn("Elevation (Mi)");
        elevationCol.setPrefWidth(110);
        elevationCol.setCellValueFactory(new PropertyValueFactory<>("elevationGain"));
        elevationCol.setResizable(false);
        elevationCol.setReorderable(false);
        
        TableColumn difficultyCol = new TableColumn("Difficulty");
        difficultyCol.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        difficultyCol.setResizable(false);
        difficultyCol.setReorderable(false);
        
        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setResizable(false);
        typeCol.setReorderable(false);
        
        // concat data
        ObservableList<Trail> data = FXCollections.observableArrayList();
        for(Map.Entry<String,Trail> entry : Trail.MainTreeMap.entrySet()) 
        {
			Trail value = entry.getValue();
			data.add(value);
    	}
	
        // first clear, then add
        trailTable.getColumns().clear();
        trailTable.getColumns().addAll(trailNameCol, addressCol, lengthCol, elevationCol, difficultyCol, typeCol);
        trailTable.setItems(data);
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateHistoryView()
    {
    	//Creating columns
        TableColumn trailNameCol = new TableColumn("Name");
        trailNameCol.setCellValueFactory(new PropertyValueFactory<>("trailName"));
        trailNameCol.setResizable(false);
        trailNameCol.setReorderable(false);
        
        TableColumn dateTraversedCol = new TableColumn("Date");
        dateTraversedCol.setCellValueFactory(new PropertyValueFactory<>("dateTraversed"));
        dateTraversedCol.setResizable(false);
        dateTraversedCol.setReorderable(false);
        
        TableColumn distanceCol = new TableColumn("Distance (Mi)");
        distanceCol.setCellValueFactory(new PropertyValueFactory<>("distance"));
        distanceCol.setResizable(false);
        distanceCol.setReorderable(false);
        
        TableColumn durationCol = new TableColumn("Duration (D)");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setResizable(false);
        durationCol.setReorderable(false);
        
        TableColumn picsTakenCol = new TableColumn("Pics");
        picsTakenCol.setCellValueFactory(new PropertyValueFactory<>("picturesTaken"));
        picsTakenCol.setResizable(false);
        picsTakenCol.setReorderable(false);
        
        // concat data
        ObservableList<HikingHistory> data = FXCollections.observableArrayList();
        for( HikingHistory elem : UserProfile.loggedInUser.hikingHistoryList )
        {
        	data.add(elem);        	
        }
        
        // first clear, then add
        historyTable.getColumns().clear();
        historyTable.getColumns().addAll(trailNameCol, dateTraversedCol, distanceCol, durationCol, picsTakenCol);
        historyTable.setItems(data);
    }
}
