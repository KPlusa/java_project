package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.Rankclass;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class RankController extends Storage implements Initializable {
    private int counter;
    private Socket s;
    private String name;
    private String subject;
    private String sub;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    int id, points;
    final ObservableList mylist = FXCollections.observableArrayList();
    @FXML
    private ComboBox chb;
    @FXML
    TableView<Rankclass> table;
    @FXML
    private TableColumn<Rankclass, Integer> ColId;
    @FXML
    private TableColumn<Rankclass, String> ColSub;
    @FXML
    private TableColumn<Rankclass, String> ColName;
    @FXML
    private TableColumn<Rankclass, Integer> ColPoints;
    @FXML
    private TextField text;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColSub.setCellValueFactory(new PropertyValueFactory<>("subject"));
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        try {
            table.setItems(fill_table());
        } catch (Exception e) {
        }
        try {
            fillcombo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void go_menu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/menu.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void go_menu_avatar(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/menu.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    @FXML
    private ObservableList<Rankclass> fill_table() throws IOException {
        ObservableList<Rankclass> rank = FXCollections.observableArrayList();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dos.writeInt(10);
                dos.writeUTF("");
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    id = dis.readInt();
                    subject = dis.readUTF();
                    name = dis.readUTF();
                    points = dis.readInt();
                    rank.add(new Rankclass(id, subject, name, points));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        return rank;
    }


    @FXML
    private void fillcombo() throws IOException {
        chb.setMaxHeight(30);
        mylist.clear();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Brak polaczenia z serwerem");
                }
                dos.writeInt(3);
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    sub = dis.readUTF();
                    mylist.add(sub);
                }
                chb.setItems(mylist);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }

    }
    @FXML
    private void setdisplay() throws IOException{
        table.setItems(Combo_fill_table());
    }

    @FXML
    private ObservableList<Rankclass> Combo_fill_table() throws IOException {
        ObservableList<Rankclass> rank = FXCollections.observableArrayList();
        table.getItems().clear();
        sub = chb.getValue().toString();
        text.clear();
        try {
            while (true) {
                counter = 0;
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(11);
                dos.writeUTF(sub);
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    id = dis.readInt();
                    subject = dis.readUTF();
                    name = dis.readUTF();
                    points = dis.readInt();
                    rank.add(new Rankclass(id, subject, name, points));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        return rank;
    }
    @FXML
    private void setdisplaybyname() throws IOException{
        table.setItems(Combo_fill_table_by_name());
    }
    @FXML
    private ObservableList<Rankclass> Combo_fill_table_by_name() throws IOException {
        ObservableList<Rankclass> rank = FXCollections.observableArrayList();
        table.getItems().clear();
        sub = text.getText();
        chb.setValue("");
        try {
            while (true) {
                counter = 0;
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(12);
                dos.writeUTF(sub);
                counter = dis.readInt();
                for (int i = 0; i < counter; i++) {
                    id = dis.readInt();
                    subject = dis.readUTF();
                    name = dis.readUTF();
                    points = dis.readInt();
                    rank.add(new Rankclass(id, subject, name, points));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        return rank;
    }
}