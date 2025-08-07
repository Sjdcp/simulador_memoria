import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene; // prueba
import javafx.scene.control.*;
import javafx.scene.layout.*; //;-;
import javafx.stage.Stage; //Turip ip ip ip
//prueba 2
public class Main extends Application {
    private GestorMemoria gestor = new GestorMemoria();
    private ListView<String> listaEjecucion = new ListView<>();
    private ListView<String> listaEspera = new ListView<>();
    private Label memoriaLabel = new Label();

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

        HBox form = new HBox(10, nombreField, memoriaField, duracionField, agregarBtn);
        HBox listas = new HBox(20,
                new VBox(new Label("En ejecución"), listaEjecucion),
                new VBox(new Label("En espera"), listaEspera)
        );

        root.getChildren().addAll(form, memoriaLabel, listas);

        stage.setTitle("Simulador de Gestión de Memoria");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    private void actualizarUI() {
        Platform.runLater(() -> {
            listaEjecucion.getItems().clear();
            for (Proceso p : gestor.getEnEjecucion()) {
                listaEjecucion.getItems().add(p.toString());
            }

            listaEspera.getItems().clear();
            for (Proceso p : gestor.getEnEspera()) {
                listaEspera.getItems().add(p.toString());
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
