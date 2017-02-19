import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Interface extends Application {
    
    private static final int WIDTH = 680;
    private static final int HEIGHT = 480;

    private Stage primaryStage;
    private Group root = new Group();
    private Group general = new Group();
    private Group left = new Group();
    private Group right = new Group();

    private void addEverything() {
        // GENERAL STUFF
        Rectangle mainPanel = new Rectangle(WIDTH, HEIGHT);
        mainPanel.setFill(Color.web("#E9E9ED"));

        Rectangle topPanel = new Rectangle(WIDTH, HEIGHT / 5.2);
        topPanel.setFill(Color.web("#3E50B5"));

        Label title = new Label("HTML Link Stripper");
        title.setFont(Font.font(36));
        title.setTextFill(Color.WHITE);
        title.setLayoutX(20);
        title.setLayoutY(20);

        CheckBox httpCheck = new CheckBox("http/https only");
        httpCheck.setFont(Font.font(17));
        httpCheck.setTextFill(Color.WHITE);
        httpCheck.setLayoutX(350);
        httpCheck.setLayoutY(35);
        httpCheck.setSelected(true);

        TextField keyword = new TextField("KEYWORD");
        keyword.setFont(Font.font(17));
        keyword.setPrefWidth(130);
        keyword.setLayoutX(525);
        keyword.setLayoutY(30);
        keyword.setOnMouseClicked(event -> {
            if (keyword.getText().equals("KEYWORD"))
                keyword.setText("");
        });


        general.getChildren().addAll(mainPanel, topPanel, title, httpCheck, keyword);


        // LEFT STUFF
        Rectangle leftPanel = new Rectangle(WIDTH / 1.6, HEIGHT - topPanel.getHeight() - 50);
        leftPanel.setLayoutX(10);
        leftPanel.setLayoutY(topPanel.getHeight() + 10);
        leftPanel.setFill(Color.web("#FFFFFF"));

        Label instructions = new Label("Select file or paste text below:");
        instructions.setFont(Font.font("System", FontWeight.BOLD, 21));
        instructions.setLayoutX(leftPanel.getLayoutX() + 15);
        instructions.setLayoutY(leftPanel.getLayoutY() + 15);

        TextArea inputText = new TextArea();
        inputText.setMaxWidth(leftPanel.getWidth() - 30);
        inputText.setPrefHeight(leftPanel.getHeight() - 100);
        inputText.setLayoutX(instructions.getLayoutX());
        inputText.setLayoutY(instructions.getLayoutY() + 35);

        Button clearText = new Button("Clear Text");
        clearText.setPrefWidth(110);
        clearText.setFont(Font.font(15));
        clearText.setLayoutX(instructions.getLayoutX());
        clearText.setLayoutY(inputText.getLayoutY() + 245);
        clearText.setOnMouseClicked(event -> {
            inputText.setText("");
        });

        Button browse = new Button("Select File");
        browse.setPrefWidth(120);
        browse.setFont(Font.font(15));
        browse.setLayoutX(instructions.getLayoutX() + 120);
        browse.setLayoutY(inputText.getLayoutY() + 245);
        browse.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Files");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String str;
                    while ((str = in.readLine()) != null)
                        inputText.appendText(str+"\n");
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button process = new Button("Process HTML");
        process.setPrefWidth(145);
        process.setFont(Font.font("System", FontWeight.BOLD, 15));
        process.setLayoutX(instructions.getLayoutX() + 250);
        process.setLayoutY(inputText.getLayoutY() + 245);

        left.getChildren().addAll(leftPanel, instructions, inputText, clearText, browse, process);


        // RIGHT STUFF
        Rectangle rightPanel = new Rectangle(WIDTH - leftPanel.getWidth() - 40 , HEIGHT - topPanel.getHeight() - 50);
        rightPanel.setLayoutX(leftPanel.getLayoutX() + leftPanel.getWidth() + 12);
        rightPanel.setLayoutY(topPanel.getHeight() + 10);
        rightPanel.setFill(Color.web("#FFFFFF"));

        Label filters = new Label("Results:");
        filters.setFont(Font.font("System", FontWeight.BOLD, 21));
        filters.setLayoutX(rightPanel.getLayoutX() + 15);
        filters.setLayoutY(rightPanel.getLayoutY() + 15);

        TextArea results = new TextArea();
        results.setEditable(false);
        results.setMaxWidth(rightPanel.getWidth() - 30);
        results.setPrefHeight(rightPanel.getHeight() - 130);
        results.setLayoutX(filters.getLayoutX());
        results.setLayoutY(filters.getLayoutY() + 35);

        process.setOnMouseClicked(event -> {
            ArrayList<String> inputLines = new ArrayList<>();
            Collections.addAll(inputLines, inputText.getText().split("\\n"));

            Stripper stripper = new Stripper(inputLines, httpCheck.isSelected(), keyword.getText());
            ArrayList<String> outputLines = stripper.getResults();
            results.setText("");
            for (String line : outputLines) {
                results.appendText(line+"\n");
            }
        });

        Button copyToClipBoard = new Button("To Clipboard");
        copyToClipBoard.setPrefWidth(112);
        copyToClipBoard.setFont(Font.font(15));
        copyToClipBoard.setLayoutX(filters.getLayoutX());
        copyToClipBoard.setLayoutY(results.getLayoutY() + 215);
        copyToClipBoard.setOnMouseClicked(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(results.getText());
            Clipboard.getSystemClipboard().setContent(content);
            Toast.makeText(primaryStage, "Successfully copied to clipboard.", 2000, 500, 500);
        });

        Button saveResults = new Button("Save");
        saveResults.setPrefWidth(65);
        saveResults.setFont(Font.font(15));
        saveResults.setLayoutX(filters.getLayoutX() + 120);
        saveResults.setLayoutY(results.getLayoutY() + 215);
        saveResults.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Results");

            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    BufferedWriter bf = new BufferedWriter(new FileWriter(file));
                    bf.write(results.getText());
                    bf.flush();
                    bf.close();
                    Toast.makeText(primaryStage, "Results successfully saved.", 2000, 500, 500);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Label copyright = new Label("\u00a9 William Shen 2017.");
        copyright.setFont(Font.font(16));
        copyright.setLayoutX(filters.getLayoutX());
        copyright.setLayoutY(HEIGHT - 75);

        right.getChildren().addAll(rightPanel, filters, results, copyToClipBoard, saveResults, copyright);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("HTML Link Stripper");
        primaryStage.setResizable(false);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        root.getChildren().addAll(general, left, right);

        addEverything();

        root.requestFocus();
    }
}

