package application;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main extends Application {
  
    @Override
    public void start(Stage primaryStage) {
        try {
            // Background Image
            Image backgroundImage = new Image("WallP.jpg");
            ImageView imageView = new ImageView(backgroundImage);
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(primaryStage.widthProperty());
            imageView.fitHeightProperty().bind(primaryStage.heightProperty());
            imageView.setOpacity(0.8);
            StackPane stackPane = new StackPane(imageView);

            // Main content VBox
            VBox mainVBox = new VBox(200); // Adjusted to default spacing
            mainVBox.setPadding(new Insets(40));
            mainVBox.setFillWidth(true);
            mainVBox.setMaxWidth(400);

            // Weather Icons HBox
            HBox iconsBox = new HBox(180); // Horizontal gap between icons

            // Temp Icon Area
            VBox iconArea1 = new VBox();
            TextArea area1 = new TextArea();
            area1.setPrefSize(16, 18);
            area1.setEditable(false);
            iconArea1.getChildren().addAll(createIcon("temp.png", 60, 60), area1);

            // Wind Icon Area
            VBox iconArea2 = new VBox();
            TextArea area2 = new TextArea();
            area2.setPrefSize(3, 5);
            area2.setEditable(false);
            iconArea2.getChildren().addAll(createIcon("wind.png", 60, 60), area2);

            iconsBox.getChildren().addAll(iconArea1, iconArea2);
            iconsBox.setAlignment(javafx.geometry.Pos.CENTER);

            // Search TextField
            TextField textField = new TextField();
            textField.setPromptText("Enter city name...");
            textField.setStyle("-fx-font-size: 14pt; -fx-padding: 9px;");

            // Search Button
            Button searchButton = new Button("Search");
            searchButton.setStyle("-fx-font-size: 14pt; -fx-padding: 9px;");
            searchButton.setOnAction(event -> {
                String city = textField.getText().trim();
                fetchWeatherData(city, area1, area2);
                textField.clear();
            });

            // Add elements to VBox in correct order
            mainVBox.getChildren().addAll(textField, iconsBox, searchButton);
            mainVBox.setAlignment(javafx.geometry.Pos.CENTER);

            stackPane.getChildren().add(mainVBox);

            // Scene setup
            Scene scene = new Scene(stackPane, 500, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            // Stage setup
            Image icon = new Image("cloudy.png");
            primaryStage.getIcons().add(icon);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Weather Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to create ImageView from image file
    private ImageView createIcon(String iconName, double width, double height) {
        Image iconImage = new Image(iconName);
        ImageView iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(width);
        iconImageView.setFitHeight(height);
        return iconImageView;
    }

    // Method to fetch weather data using OpenWeatherMap API
//    @SuppressWarnings("deprecation")
	private void fetchWeatherData(String city, TextArea tempArea, TextArea windArea) {
        try {
        	URL url = null;
            String apiKey = "5bb817f64de124c3c69da6395e19765a"; // Replace with your OpenWeatherMap API key
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

            try {
				url = new URI(urlString).toURL();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = new String();
            String line;
            while ((line = reader.readLine()) != null) {
                response += line;
            } 
            reader.close();

            JSONObject json = (JSONObject) JSONValue.parse(response.toString());

            JSONObject main = (JSONObject) json.get("main");
            Double temperature = (Double) main.get("temp") - 273.15; // Convert to Celsius
            Double windSpeed = (Double) main.get("windSpeed");
         
            JSONArray  weatherArray = (JSONArray) json.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);
            String Description = (String) weather.get("Description");
            System.out.println(Description);
            tempArea.setText("Temperature: %.2f °C" +temperature);
            windArea.setText("Wind Speed: %d m/s" + windSpeed);
                       
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    public static void main(String[] args) {
        launch(args);
    }
}
   



	 
