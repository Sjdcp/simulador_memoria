//Estas son las librerias que se utilizan
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
//ultimo comentario
public class Main extends Application {
    //Crea una insancia el gestor de memoria 
    private GestorMemoria gestor = new GestorMemoria();
    //Lista para visualizare los procesos que se ejecutan
    private ListView<Label> listaEjecucion = new ListView<>();
    //Muestra la lista de proceso en espera
    private ListView<Label> listaEspera = new ListView<>();
    //Muestra la lista de proceso Finalizados
    private ListView<Label> listaFinalizados = new ListView<>();
    //Etiqueta utilizada para mopstrar la memoria disponible
    private Label memoriaLabel = new Label();
    //Barra del proceso muestra el uso de memoria 
    private ProgressBar memoriaBar = new ProgressBar(0);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        /*Contenedor principal con la disposición
         de 10 pixeles 
         */
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        //input para el texto del proceso
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del proceso");
        //Campo de texto de la duracion del proceso en segundos
        TextField memoriaField = new TextField();
        memoriaField.setPromptText("Memoria (MB)");
        //Campo de texto para la duración del proceso en segundos
        TextField duracionField = new TextField();
        duracionField.setPromptText("Duración (seg)");
        //Butón para añadir un proceso nuevo
        Button agregarBtn = new Button("Agregar Proceso");
        agregarBtn.setOnAction(e -> {
            try {
                //Obtiene los valores ingresados por el usuario
                String nombre = nombreField.getText();
                int memoria = Integer.parseInt(memoriaField.getText());
                int duracion = Integer.parseInt(duracionField.getText());
                //Crea el proceso y lo agrega al gestor memoris
                Proceso p = new Proceso(nombre, memoria, duracion);
                gestor.agregarProceso(p, this::actualizarUI);
                //Limpia los campos despúes que se agregen  
                nombreField.clear();
                memoriaField.clear();
                duracionField.clear();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Memoria y duración deben ser números enteros.");
            }
        });
        //Muestra la memoria disponible al iniciar la aplicación
        memoriaLabel.setText("Memoria Disponible: " + gestor.getMemoriaDisponible() + " MB");
        //Estable el anchj de la barra de memoria en 300 pixeles
        //Aplica el color azul 
        memoriaBar.setPrefWidth(300);
        memoriaBar.setStyle("-fx-accent: #2196F3;");
    /*
        contennedor horizontal con tres lista
        la primera los procesos en ejecución , 
        la segundo de process en espero 
        la tercera los procesos finalizados
    */
        HBox form = new HBox(10, nombreField, memoriaField, duracionField, agregarBtn);

        VBox ejecucionBox = new VBox(new Label("En ejecución"), listaEjecucion);
        VBox esperaBox = new VBox(new Label("En espera"), listaEspera);
        VBox finalizadosBox = new VBox(new Label("Finalizados"), listaFinalizados);

        HBox listas = new HBox(20, ejecucionBox, esperaBox, finalizadosBox);

        VBox memoriaBox = new VBox(5, memoriaLabel, memoriaBar);

        //Agrega los elementos al contenedor principal
        root.getChildren().addAll(form, memoriaBox, listas);

        stage.setTitle("Simulador de Gestión de Memoria");
        stage.setScene(new Scene(root, 950, 500));
        stage.show();
    }
    /*
     Actualiza la interfaz de usuario conn 
     la información reciente 
     */
    private void actualizarUI() {
        Platform.runLater(() -> {
            listaEjecucion.getItems().clear();
            //Refresca la lista de proceso que esta enn ejecucción
            for (Proceso p : gestor.getEnEjecucion()) {
                Label label = new Label(p.toString());
                label.setTextFill(Color.GREEN);
                listaEjecucion.getItems().add(label);
            }
            //Refresca la lista de proceso que esta en espera
            listaEspera.getItems().clear();
            for (Proceso p : gestor.getEnEspera()) {
                Label label = new Label(p.toString());
                label.setTextFill(Color.ORANGE);
                listaEspera.getItems().add(label);
            }
            //Refresca la lista de proceso que esta en ejecución
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
    //Desplega un cuadro de alerta con un titulo y un mensaje 
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
//Finaliza main
