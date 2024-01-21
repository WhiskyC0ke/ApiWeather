package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloApplication extends Application {

    private static final String API_KEY = "1e9e72771c0bc3c444ec8d1ff8204f19";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";





    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather App");

        TextField cityTextField = new TextField();
        Label resultLabel = new Label();


        Image lupa = new Image("C:\\Users\\shani\\Documents\\lupa.png");
        ImageView lupaImage = new ImageView(lupa);
        lupaImage.setFitHeight(20);
        lupaImage.setPreserveRatio(true);


        Button getWeatherButton = new Button("enter");
        getWeatherButton.setOnAction(e -> {
            String cityName = cityTextField.getText();
            if (!cityName.isEmpty()) {
                String apiUrl = String.format(API_URL, cityName, API_KEY);
                String weatherInfo = getWeatherInfo(apiUrl);
                resultLabel.setText(weatherInfo);
            } else {
                resultLabel.setText("Wpisz miasto");
            }
        });

        getWeatherButton.setGraphic(lupaImage);

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.BASELINE_CENTER);
        layout.setStyle("-fx-background-color: gray");
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(cityTextField, getWeatherButton, resultLabel);

        Scene scene = new Scene(layout, 500, 400);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private String getWeatherInfo(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject main = jsonResponse.getJSONObject("main");
            double temperatureKelvin = main.getDouble("temp");
            double temperatureCelsius = kelvinToCelsius(temperatureKelvin);

            return "Temperatura: " + Math.round(temperatureCelsius) + " °C";
        } catch (Exception e) {
            return "Błąd";
        }
    }

    private double kelvinToCelsius(double kelvinTemperature) {
        return kelvinTemperature - 273.15;
    }
}
