/**
 * Sample Skeleton for 'application.fxml' Controller Class
 */

package demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trail_module.Trail;
import trail_module.TrailDifficulty;
import trail_module.TrailType;
import user_module.UserProfile;

/**
 * FXML Controller class to handle the login form.
 */
public class Controller {

    @FXML // fx:id="loginUsernameBox"
    private TextField loginUsernameBox; // Value injected by FXMLLoader

    @FXML // fx:id="loginBtn"
    private Button loginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="loginPasswordBox"
    private PasswordField loginPasswordBox; // Value injected by FXMLLoader

    @FXML // fx:id="createUsernameBox"
    private TextField createUsernameBox; // Value injected by FXMLLoader

    @FXML // fx:id="createBtn"
    private Button createBtn; // Value injected by FXMLLoader

    @FXML // fx:id="createPasswordBox"
    private PasswordField createPasswordBox; // Value injected by FXMLLoader

    @FXML // fx:id="createIsAdminCheck"
    private CheckBox createIsAdminCheck; // Value injected by FXMLLoader

    @FXML
    private void initialize()
    {	
    	// handle account creation
    	createBtn.setOnAction((x) -> 
    	{
    		createBtnAction();
    	});
    	
    	// handle login btn action
    	loginBtn.setOnAction((x) ->
    	{
    		loginBtnAction();
    	});
    	
    	// read users from filestore
    	UserProfile.readUsersFromFile();
    	
    	// read trails from filestore
    	Trail.readTrailsFromFile();
    	
    	// ensure these trails always exist
    	Trail.MainTreeMap.put("Treeland Trail", new Trail("Treeland Trail", "112 Dawn Dr", 3, 1, TrailDifficulty.easy, TrailType.loop));
		Trail.MainTreeMap.put("Mt. Trail", new Trail("Mt. Trail", "Mt Olympus Dr", 2, 0.6f, TrailDifficulty.easy, TrailType.pointToPoint));
		Trail.MainTreeMap.put("Beach Trail", new Trail("Beach Trail", "102 Beach Ave", 5, 0.3f, TrailDifficulty.moderate, TrailType.outAndBack));
		Trail.MainTreeMap.put("Greenblet Hike", new Trail("Greenbelt Hike", "102 New Mill Rd", 32, 2.3f, TrailDifficulty.hard, TrailType.pointToPoint));
		Trail.MainTreeMap.put("Snowy Drive", new Trail("Snowy Drive", "932 North Pole", 26, 0.9f, TrailDifficulty.hard, TrailType.pointToPoint));
		Trail.MainTreeMap.put("Brooklyn Hike", new Trail("Brooklyn Hike", "34 Brooklyn Ave", 16, 0.4f, TrailDifficulty.moderate, TrailType.loop));
		Trail.MainTreeMap.put("NYC Trail", new Trail("NYC Trail", "12 Time Square", 10, 0.2f, TrailDifficulty.easy, TrailType.outAndBack));

    }
    
    /**
     * This method on btn fire will parse the login boxes and check if the user exists, then proceed to log them in.
     */
    private void loginBtnAction()
    {
    	// CHECKS
    	if( loginUsernameBox.getText().isBlank())
    	{
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: Username box is empty.");
			alert.showAndWait();
			return;
    	}
    	if( loginPasswordBox.getText().isBlank())
    	{
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: Passowrd box is empty.");
			alert.showAndWait();
			return;
    	}
    	
    	UserProfile loginUser = UserProfile.searchUser(loginUsernameBox.getText());
    	if( loginUser == null )
    	{
    		// cannot login to a null user
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: Failed to find user with specified Username.");
			alert.showAndWait();
			return;
    	}
    	if( !loginUser.getPassword().equals(loginPasswordBox.getText()) )
    	{
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: Password mismatch.");
			alert.showAndWait();
			return;
    	}
    
    	// log in user!
    	UserProfile.loggedInUser = loginUser;
    	
    	// display form!
    	try
    	{
    		Parent root = FXMLLoader.load(getClass().getResource("main_form.fxml"));
    		Stage stage = new Stage();
    		stage.setTitle("User Form");
    		stage.setScene(new Scene(root));
    		stage.getScene().getStylesheets().add(getClass().getResource("tableview.css").toExternalForm());
    		stage.setResizable(false);    		
    		
    		// disable login btn until closed
    		loginBtn.setDisable(true);
    		stage.showAndWait();
    		loginBtn.setDisable(false);
    	}
    	catch( Exception e ) {}
    }
    
    /**
     * Handles checks and account creation.
     */
    private void createBtnAction()
    {
    	// does username exist?
		if( createUsernameBox.getText().isBlank() )
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: Username box is empty.");
			alert.showAndWait();
			return;
		}
		
		// does password exist?
		if( createUsernameBox.getText().isBlank() )
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: Password box is empty.");
			alert.showAndWait();
			return;
		}
		
		// is username already taken?
		if( UserProfile.searchUser(createUsernameBox.getText()) != null )
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error: User already exists with specified username.");
			alert.showAndWait();
			return;
		}
		
		// both exist, create account!
		UserProfile profile = new UserProfile(createUsernameBox.getText(), createPasswordBox.getText());
		
		// is admin?
		profile.setIsAdmin(createIsAdminCheck.isSelected());
		
		// push profile to treemap
		UserProfile.addUser(profile);

		// write changes to DB
		UserProfile.writeUsersToFile();
		
		// display happy message
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("User has been created!");
		alert.showAndWait();
		return;
    }
}
