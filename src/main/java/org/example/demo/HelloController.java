package org.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo.Models.*;
import org.example.demo.FoodFormController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public TableView MainTable;


    ObservableList<food> foodList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // заполнили список данными

        foodList.add(new Fruts(100, "Яблоко", true));
        foodList.add(new Chocolates(200, "шоколад Аленка", Chocolates.Type.milk));
        foodList.add(new Coockies(300, "сладкая булочка с Маком", true, true, false));
        MainTable.setItems(foodList);
        // создаем столбец, указываем что столбец преобразует Food в String,
        // указываем заголовок колонки как "Название"
        TableColumn<food, String> titleColumn = new TableColumn<>("Название");
        // указываем какое поле брать у модели Food
        // в данном случае title, кстати именно в этих целях необходимо было создать getter и setter для поля title
        //   titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setCellValueFactory(cellData -> {
            // плюс надо обернуть возвращаемое значение в обертку свойство
            return new SimpleStringProperty(cellData.getValue().getTitle());
        });


        // тоже самое для калорийности
        TableColumn<food, String> kkalColumn = new TableColumn<>("Калорийность");
        // kkalColumn.setCellValueFactory(new PropertyValueFactory<>("kkal"));
//v-1
        kkalColumn.setCellValueFactory(cellData -> {
            // плюс надо обернуть возвращаемое значение в обертку свойство
            return new SimpleStringProperty(String.valueOf(cellData.getValue().getKkal()));
        });
        // добавляем столбец с описанием
        TableColumn<food, String> descriptionColumn = new TableColumn<>("Описание");
        // если хотим что-то более хитрое выводить, то используем лямбда выражение
        descriptionColumn.setCellValueFactory(cellData -> {
            // плюс надо обернуть возвращаемое значение в обертку свойство
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

        // подцепляем столбцы к таблице
        MainTable.getColumns().addAll(titleColumn, kkalColumn, descriptionColumn);

    }
    @FXML
    public void onAddClick(ActionEvent actionEvent) {

        try {
            // эти три строчки создают форму из fxml файлика
            // в принципе можно было бы обойтись
            // Parent root = FXMLLoader.load(getClass().getResource("FoodForm.fxml"));
            // но дальше вот это разбиение на три строки упростит нам жизнь
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org/example/demo/FoodForm.fxml"));
            Parent root = loader.load();

            // ну а тут создаем новое окно
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // указываем что оно модальное
            stage.initModality(Modality.WINDOW_MODAL);
            // указываем что оно должно блокировать главное окно
            // ну если точнее, то окно, на котором мы нажали на кнопку
            stage.initOwner(this.MainTable.getScene().getWindow());

            // открываем окно и ждем пока его закроют
            stage.showAndWait();

            // вытаскиваем контроллер который привязан к форме
            FoodFormController controller = loader.getController();
            // проверяем что наали кнопку save
            if (controller.getModalResult()) {
                // собираем еду с формы
                food newFood = controller.getFood();
                // добавляем в список
                this.foodList.add(newFood);

            }
            }
        catch (IOException e){
            e.printStackTrace();
        }

    }
    public void onEditClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FoodForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.MainTable.getScene().getWindow());

        stage.showAndWait();
        FoodFormController controller = loader.getController();
        controller.setFood((food) this.MainTable.getSelectionModel().getSelectedItem());

        stage.showAndWait();

        // если нажали кнопку сохранить
        if (controller.getModalResult()) {
            // узнаем индекс выбранной в таблице строки
            int index = this.MainTable.getSelectionModel().getSelectedIndex();
            // подменяем строку в таблице данными на форме
            this.MainTable.getItems().set(index, controller.getFood());
        }


    }


    public void onDeleteClick(ActionEvent actionEvent) {

        // берем выбранную на форме еду
        food food = (food) this.MainTable.getSelectionModel().getSelectedItem();

        // выдаем подтверждающее сообщение
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(String.format("Точно удалить %s?", food.getTitle()));

        // если пользователь нажал OK
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            // удаляем строку из таблицы
            this.MainTable.getItems().remove(food);
        }
    }
}
    /*public void showCreateWindow(Dog dog) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppFX.class.getResource("/maket/new.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Wow Wow Wow");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(page));
            CreateController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDog(dog);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
    @FXML
    public void onAddClick(ActionEvent actionEvent) throws IOException {


        // эти три строчки создюат форму из fxml файлика
        // в принципе можно было бы обойтись
        // Parent root = FXMLLoader.load(getClass().getResource("FoodForm.fxml"));
        // но дальше вот это разбиение на три строки упростит нам жизнь
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("C:\\Users\\dania\\OneDrive\\Рабочий стол\\учеба\\ооп\\pract5\\demo\\src\\main\\resources\\org\\example\\demo\\FoodForm.fxml"));
        Parent root = loader.load();

        // ну а тут создаем новое окно
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        // указываем что оно модальное
        stage.initModality(Modality.WINDOW_MODAL);
        // указываем что оно должно блокировать главное окно
        // ну если точнее, то окно, на котором мы нажали на кнопку
        stage.initOwner(this.MainTable.getScene().getWindow());

        // открываем окно и ждем пока его закроют
        stage.showAndWait();



    }
 */







