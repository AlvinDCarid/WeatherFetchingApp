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

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class APPMain extends Application {
  
    @Override
    public void start(Stage primaryStage) {
        try {
            Image backgroundImage = new Image("WallP.jpg");
            ImageView imageView = new ImageView(backgroundImage);
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(primaryStage.widthProperty());
            imageView.fitHeightProperty().bind(primaryStage.heightProperty());
            imageView.setOpacity(0.8);
            StackPane stackPane = new StackPane(imageView);

            VBox mainVBox = new VBox(120); 
            mainVBox.setPadding(new Insets(40));
            mainVBox.setFillWidth(true);
            mainVBox.setMaxWidth(400);

            HBox iconsBox = new HBox(10); 
            	VBox iconArea1 = new VBox(10);
            TextArea area1 = new TextArea();
            area1.setPrefSize(250, 50); 
            area1.setEditable(false);
            iconArea1.getChildren().addAll(createIcon("temp.png", 60, 60), area1);
            iconArea1.setAlignment(javafx.geometry.Pos.CENTER);
            	VBox iconArea2 = new VBox(10);
            TextArea area2 = new TextArea();
            area2.setPrefSize(250, 50); 
            area2.setEditable(false);
            iconArea2.getChildren().addAll(createIcon("wind.png", 60, 60), area2);
            iconArea2.setAlignment(javafx.geometry.Pos.CENTER);

            
            	VBox iconArea3 = new VBox(10);
            TextArea area3 = new TextArea();
            area3.setPrefSize(250, 50); 
            area3.setEditable(false);
            iconArea3.getChildren().addAll(createIcon("humid.png", 60, 60), area3);
            iconArea3.setAlignment(javafx.geometry.Pos.CENTER);
            
            iconsBox.getChildren().addAll(iconArea1, iconArea2, iconArea3);
            iconsBox.setAlignment(javafx.geometry.Pos.CENTER);

            TextField textField = new TextField();
            textField.setPromptText("Enter city name...");
            textField.setStyle("-fx-font-size: 14pt; -fx-padding: 9px;");

            Button searchButton = new Button("Search");
            searchButton.setStyle("-fx-font-size: 14pt; -fx-padding: 9px;");
            searchButton.setOnAction(event -> {
                String city = textField.getText().trim();
                fetchWeatherData(city, area1, area2, area3);
//                textField.clear();
            });

            mainVBox.getChildren().addAll(textField, iconsBox, searchButton);
            mainVBox.setAlignment(javafx.geometry.Pos.CENTER);

            stackPane.getChildren().add(mainVBox);

            Scene scene = new Scene(stackPane, 500, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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

    private ImageView createIcon(String iconName, double width, double height) {
        Image iconImage = new Image(iconName);
        ImageView iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(width);
        iconImageView.setFitHeight(height);
        return iconImageView;
    }

    private void fetchWeatherData(String city, TextArea tempArea, TextArea windArea, TextArea humidArea) {
    	if (city.contains(" ")) {
            city = city.replace(" ", "%20");
        }

        try {
            URL url = null;
            String apiKey = "Enter Your Key Here";
            String urlString = "https://api.openweathermap.org/data/2.5/we" + city + "&appid=" + apiKey;

            try {
                url = new URI(urlString).toURL();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            String line;
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            reader.close();

            JSONObject json = (JSONObject) JSONValue.parse(response.toString());

            JSONObject main = (JSONObject) json.get("main");
            Double temperature = (Double) main.get("temp") - 273.15; 
            
            long humidity = (long) main.get("humidity");
            
            JSONObject wind = (JSONObject) json.get("wind");
            Double windSpeed = (Double) wind.get("speed");

            tempArea.setText(String.format("Temp: %.2f °C", temperature));
            
            windArea.setText(String.format("Wind: %.2f m/s", windSpeed));
            
            humidArea.setText(String.format("Humid: %d%%", humidity));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
