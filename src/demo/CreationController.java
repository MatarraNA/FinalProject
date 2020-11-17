package demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import trail_module.Trail;
import trail_module.TrailDifficulty;
import trail_module.TrailType;

public class CreationController {
	
	@FXML
    private TextField nameBox;

    @FXML
    private Button createTrailBtn;

    @FXML
    private TextField addressBox;

    @FXML
    private TextField milesBox;

    @FXML
    private TextField elevationBox;

    @FXML
    private ComboBox<TrailDifficulty> diffComboBox;

    @FXML
    private ComboBox<TrailType> typeComboBox;
    
	@FXML
    private void initialize()
    {
		diffComboBox.getItems().setAll(TrailDifficulty.values());
		typeComboBox.getItems().setAll(TrailType.values());
		
		createTrailBtn.setOnAction(x->createTrailAction());
    }

	private void createTrailAction()
	{
		// check if all values are inputted properly
		if( diffComboBox.getValue() == null )
		{
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: No Difficulty selected.");
    		alert.show();
    		return;
		}
		if( typeComboBox.getValue() == null )
		{
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: No Trail Type selected.");
    		alert.show();
    		return;
		}
		// name or address null?
		if( nameBox.getText().isBlank() || addressBox.getText().isBlank() )
		{
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: All fields must be filled in.");
    		alert.show();
    		return;
		}
		
		// parse rest of values
		try
		{
			// duration
			float miles = Float.parseFloat(milesBox.getText());
			float elevation = Float.parseFloat(elevationBox.getText());
			
			// create trail
			Trail trail = new Trail(nameBox.getText(), addressBox.getText(), miles, elevation, diffComboBox.getValue(), typeComboBox.getValue());
		
			// add to treemap
			Trail.MainTreeMap.put(trail.getTrailName(), trail);
			
			// write changes
			Trail.writeTrailsToFile();
			
			// worked! close form
			Stage stage = (Stage) typeComboBox.getScene().getWindow();
			stage.close();
		}
		catch( Exception e )
		{
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText("Error: Something went wrong creating the trail.\n\n" + e.getMessage());
    		alert.show();
    		return;
		}
	}
}
