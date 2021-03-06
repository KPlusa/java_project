package project.controllers;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import project.Storage;
/**Klasa controlera dla zakladki "rejestracja" dostepnej z poziomu logowania*/
public class RegisterController extends Storage implements Initializable {
    private Socket s;
    private String st;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private Button button;
    @FXML
    private AnchorPane AnchorPaneMain;
    @FXML
    private TextField username;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField pass2;
    @FXML
    private TextField mail;
    @FXML
    private Label status;


    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
    }

    /** Metoda pozwalajaca na zarejestrowanie nieistniejacego uzytkownika
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
    @FXML
    public void go_register(ActionEvent event) throws IOException {
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setText("Brak polaczenia z serwerem");
                }
                dos.writeInt(2);
                dos.writeUTF(username.getText());
                dos.writeUTF(pass.getText());
                dos.writeUTF(pass2.getText());
                dos.writeUTF(mail.getText());
                st = dis.readUTF();
                System.out.println(st);
                status.setText(st);
                if(st.equals("Zarejestrowano"))
                {
                    username.setText("");
                    pass.setText("");
                    pass2.setText("");
                    mail.setText("");
                }
                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** Metoda pozwala na przejscie do zakladki logowanie
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        Scene scene = button.getScene();
        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) button.getScene().getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(AnchorPaneMain);
        });
        timeline.play();
    }

}