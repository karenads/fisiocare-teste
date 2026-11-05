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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginController {

    private String key = "1234";

    @FXML private TextField txtKey;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {

        // Chave local: somente para inicializar o primeiro administrador.
        if (txtKey.getText().equals(key)
                && txtEmail.getText().isBlank() && txtSenha.getText().isBlank()) {
            DesktopSession.token = "";
            abrirMenu(event);
            return;
        }

        try {
            String json = String.format(
                    "{\"email\":\"%s\",\"senha\":\"%s\"}",
                    txtEmail.getText(), txtSenha.getText()
            );

            HttpURLConnection conexao = (HttpURLConnection) new URL(
                    "http://localhost:8080/auth/login").openConnection();
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json");
            conexao.setDoOutput(true);

            try (OutputStream os = conexao.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            if (conexao.getResponseCode() != 200) {
                showMessage(Alert.AlertType.ERROR, "E-mail ou senha inválidos!");
                return;
            }

            String retorno;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conexao.getInputStream()))) {
                retorno = br.readLine();
            }

            int inicio = retorno.indexOf("\"token\":\"");
            if (inicio < 0) {
                showMessage(Alert.AlertType.ERROR, "Token não encontrado na resposta!");
                return;
            }
            inicio += 9;
            int fim = retorno.indexOf("\"", inicio);
            DesktopSession.token = retorno.substring(inicio, fim);
            abrirMenu(event);

        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, "Erro ao autenticar: " + e.getMessage());
        }
    }

    private void abrirMenu(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/example/back/menu-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void showMessage(Alert.AlertType type, String msg) {
        Alert alerta = new Alert(type);
        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
