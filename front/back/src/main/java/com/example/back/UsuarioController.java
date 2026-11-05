package com.example.back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtSecretKey;

    @FXML
    public void salvarUsuario(ActionEvent event) throws Exception {
        if (txtNome.getText().isBlank() || txtCpf.getText().isBlank()
                || txtEmail.getText().isBlank() || txtSenha.getText().isBlank()
                || txtSecretKey.getText().isBlank()) {
            showMessage(Alert.AlertType.ERROR, "Preencha todos os campos!");
            return;
        }

        String jsonRequest = String.format("{\n"
                        + "  \"nome\": \"%s\",\n"
                        + "  \"email\": \"%s\",\n"
                        + "  \"senha\": \"%s\",\n"
                        + "  \"cpf\": \"%s\",\n"
                        + "  \"secretKey\": \"%s\"\n"
                        + "}", txtNome.getText(), txtEmail.getText(),
                txtSenha.getText(), txtCpf.getText(), txtSecretKey.getText());

        try {
            int resposta = executaMetodoAPI("http://localhost:8080/usuarios/admin", jsonRequest, "POST");
            if (resposta == 200) {
                showMessage(Alert.AlertType.INFORMATION, "Administrador salvo com sucesso!");
                voltar(event);
            } else {
                showMessage(Alert.AlertType.ERROR, "Erro ao salvar administrador! Código: " + resposta
                        + ". Verifique se já existe um administrador e se a chave é válida.");
            }
        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, "Erro ao salvar administrador: " + e.getMessage());
        }
    }

    @FXML
    public void voltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/back/menu-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private int executaMetodoAPI(String url, String json, String metodo) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(metodo);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        int resposta = connection.getResponseCode();
        connection.disconnect();
        return resposta;
    }

    private void showMessage(Alert.AlertType type, String msg) {
        Alert alerta = new Alert(type);
        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
