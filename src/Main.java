import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private GestorMemoria gestor = new GestorMemoria();
    private ListView<Label> listaEjecucion = new ListView<>();
    private ListView<Label> listaEspera = new ListView<>();
    private ListView<Label> listaFinalizados = new ListView<>();
    private Label memoriaLabel = new Label();
    private ProgressBar memoriaBar = new ProgressBar(0);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del proceso");

        TextField memoriaField = new TextField();
        memoriaField.setPromptText("Memoria (MB)");

        TextField duracionField = new TextField();
        duracionField.setPromptText("Duración (seg)");

        Button agregarBtn = new Button("Agregar Proceso");
        agregarBtn.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                int memoria = Integer.parseInt(memoriaField.getText());
                int duracion = Integer.parseInt(duracionField.getText());

                Proceso p = new Proceso(nombre, memoria, duracion);
                gestor.agregarProceso(p, this::actualizarUI);

                nombreField.clear();
                memoriaField.clear();
                duracionField.clear();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Memoria y duración deben ser números enteros.");
            }
        });

        memoriaLabel.setText("Memoria Disponible: " + gestor.getMemoriaDisponible() + " MB");

        memoriaBar.setPrefWidth(300);
        memoriaBar.setStyle("-fx-accent: #2196F3;");

        HBox form = new HBox(10, nombreField, memoriaField, duracionField, agregarBtn);

        VBox ejecucionBox = new VBox(new Label("En ejecución"), listaEjecucion);
        VBox esperaBox = new VBox(new Label("En espera"), listaEspera);
        VBox finalizadosBox = new VBox(new Label("Finalizados"), listaFinalizados);

        HBox listas = new HBox(20, ejecucionBox, esperaBox, finalizadosBox);

        VBox memoriaBox = new VBox(5, memoriaLabel, memoriaBar);

        root.getChildren().addAll(form, memoriaBox, listas);

        stage.setTitle("Simulador de Gestión de Memoria");
        stage.setScene(new Scene(root, 950, 500));
        stage.show();
    }

    private void actualizarUI() {
        Platform.runLater(() -> {
            listaEjecucion.getItems().clear();
            for (Proceso p : gestor.getEnEjecucion()) {
                Label label = new Label(p.toString());
                label.setTextFill(Color.GREEN);
                listaEjecucion.getItems().add(label);
            }

            listaEspera.getItems().clear();
            for (Proceso p : gestor.getEnEspera()) {
                Label label = new Label(p.toString());
                label.setTextFill(Color.ORANGE);
                listaEspera.getItems().add(label);
            }

            listaFinalizados.getItems().clear();
            for (Proceso p : gestor.getFinalizados()) {
                Label label = new Label(p.toString());
                label.setTextFill(Color.GRAY);
                listaFinalizados.getItems().add(label);
            }

            double usoMemoria = (double) (gestor.getMemoriaTotal() - gestor.getMemoriaDisponible()) / gestor.getMemoriaTotal();
            memoriaBar.setProgress(usoMemoria);

            if (usoMemoria < 0.5) {
                memoriaBar.setStyle("-fx-accent: green;");
            } else if (usoMemoria < 0.8) {
                memoriaBar.setStyle("-fx-accent: orange;");
            } else {
                memoriaBar.setStyle("-fx-accent: red;");
            }

            memoriaLabel.setText("Memoria Disponible: " + gestor.getMemoriaDisponible() + " MB");
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

