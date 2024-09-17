package com.example.blockchainvoting;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class VotingApp extends Application {

    private VoterRegistration voterRegistration = new VoterRegistration();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blockchain Voting System");

        // Register some voters for testing
        voterRegistration.registerVoter("VOTER123");
        voterRegistration.registerVoter("VOTER456");
        voterRegistration.registerVoter("aditya12f5");

        // Top Banner with an Image
        Image bannerImage = new Image("file:banner.png");  // Replace with your banner image path
        ImageView bannerImageView = new ImageView(bannerImage);
        bannerImageView.setFitWidth(400);
        bannerImageView.setPreserveRatio(true);

        Label titleLabel = new Label("Blockchain Voting System");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        VBox topBanner = new VBox(10, bannerImageView, titleLabel);
        topBanner.setPadding(new Insets(20));
        topBanner.setStyle("-fx-background-color: #ecf0f1;");

        // Center Grid for Voting Form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #ecf0f1;");

        Label voterIdLabel = new Label("Voter ID:");
        TextField voterIdField = new TextField();
        voterIdField.setPromptText("Enter your Voter ID");

        Label candidateLabel = new Label("Choose Candidate:");
        ComboBox<String> candidateChoice = new ComboBox<>();
        candidateChoice.getItems().addAll("Candidate A", "Candidate B", "Candidate C");

        // Submit Button with Styling
        Button submitButton = new Button("Vote");
        submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        submitButton.setTooltip(new Tooltip("Click to cast your vote"));

        submitButton.setOnAction(e -> {
            String voterId = voterIdField.getText();
            String selectedCandidate = candidateChoice.getValue();

            if (voterRegistration.isVoterRegistered(voterId)) {
                Block newBlock = new Block(selectedCandidate, Blockchain.blockchain.get(Blockchain.blockchain.size() - 1).hash);
                Blockchain.addBlock(newBlock);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vote cast successfully!", ButtonType.OK);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Voter not registered!", ButtonType.OK);
                alert.show();
            }
        });

        grid.add(voterIdLabel, 0, 0);
        grid.add(voterIdField, 1, 0);
        grid.add(candidateLabel, 0, 1);
        grid.add(candidateChoice, 1, 1);
        grid.add(submitButton, 1, 2);

        // Bottom Section with Admin Controls
        HBox bottomSection = new HBox();
        bottomSection.setPadding(new Insets(10));
        bottomSection.setSpacing(10);
        bottomSection.setStyle("-fx-background-color: #34495e;");

        Button checkBlockchain = new Button("Check Blockchain Validity");
        checkBlockchain.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        checkBlockchain.setTooltip(new Tooltip("Click to verify the integrity of the blockchain"));

        checkBlockchain.setOnAction(e -> {
            boolean isValid = Blockchain.isChainValid();
            Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, isValid ? "Blockchain is valid!" : "Blockchain has been tampered!", ButtonType.OK);
            alert.show();
        });

        bottomSection.getChildren().add(checkBlockchain);

        // Main Layout using BorderPane
        BorderPane root = new BorderPane();
        root.setTop(topBanner);
        root.setCenter(grid);
        root.setBottom(bottomSection);

        // Scene with Styling
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("file:styles.css");  // External CSS for advanced styling (create styles.css)

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Blockchain.addBlock(new Block("Genesis Block", "0")); // Adding the Genesis block
        launch(args);
    }
}
