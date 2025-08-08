//Estas son las librerias que se utilizan
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    // Instancia  para el  gestor de memoria
    private GestorMemoria gestor = new GestorMemoria();
    // Lista para visualizar  los procesos que se ejecutan
    private ListView<String> listaEjecucion = new ListView<>();
   //Muestra la lista de procesos en espera 
    private ListView<String> listaEspera = new ListView<>();
    //es la etiqueta utilizada para mostrar la memoria disponible
    private Label memoriaLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        /* contenedor principal 
          con dispoción vertical y un espacio de 
          10 pixeles     */
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        //input para el texto del proceso
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del proceso");
        //cuadro de texto para para la memoria , representada en MB
        TextField memoriaField = new TextField();
        memoriaField.setPromptText("Memoria (MB)");
        //Campo de texto para la duración del proceso en segundos
        TextField duracionField = new TextField();
        duracionField.setPromptText("Duración (seg)");

        //Botón para añadir un proceso nuevo
        Button agregarBtn = new Button("Agregar Proceso");
        agregarBtn.setOnAction(e -> {
            try {
                //Obtiene los valores ingresador por el usuario
                String nombre = nombreField.getText();
                int memoria = Integer.parseInt(memoriaField.getText());
                int duracion = Integer.parseInt(duracionField.getText());

                //Crea el proceso y lo agrega al gestor de memoria
                Proceso p = new Proceso(nombre, memoria, duracion);
                gestor.agregarProceso(p, this::actualizarUI);
                //Limpia los campos después que se agregen
                nombreField.clear();
                memoriaField.clear();
                duracionField.clear();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Memoria y duración deben ser números enteros.");
            }
        });

        //Muestra la memoria disponible al iniciar la aplicación
        memoriaLabel.setText("Memoria Disponible: " + gestor.getMemoriaDisponible() + " MB");
        /*Contenidr horizontal con dos lista 
          la prima es los procesos en ejecución y en espera
         */
        HBox form = new HBox(10, nombreField, memoriaField, duracionField, agregarBtn);
        HBox listas = new HBox(20,
                new VBox(new Label("En ejecución"), listaEjecucion),
                new VBox(new Label("En espera"), listaEspera)
        );
        //Agrega los elementos al contenedor principal
        root.getChildren().addAll(form, memoriaLabel, listas);
        //Configura y visualiza la venta principal
        stage.setTitle("Simulador de Gestión de Memoria");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }
    /*
     Actualiza la interfaz de usuario con la
     información reciente
     */
    private void actualizarUI() {
        Platform.runLater(() -> {
            //Refresca la lista de proceso que esta en ejecucón 
            listaEjecucion.getItems().clear();
            for (Proceso p : gestor.getEnEjecucion()) {
                listaEjecucion.getItems().add(p.toString());
            }
            //Refresca la lista de procesos en espera
            listaEspera.getItems().clear();
            for (Proceso p : gestor.getEnEspera()) {
                listaEspera.getItems().add(p.toString());
            }
           // Actualiza la etiqueta de memoria disponible 
            memoriaLabel.setText("Memoria Disponible: " + gestor.getMemoriaDisponible() + " MB");
        });
    }
//Desplega un cuadro de alerta con un titulo y un mensaje 
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
