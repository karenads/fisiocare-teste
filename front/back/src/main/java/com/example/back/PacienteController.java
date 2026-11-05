package com.example.back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PacienteController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtDataNascimento;

    @FXML
    private TextField txtEndereco;

    @FXML
    private TextField txtObservacoes;

    @FXML
    public void salvarPaciente(ActionEvent event) {
        try {
            // Validar dados
            if (txtNome.getText().isBlank() || txtCpf.getText().isBlank()
                    || txtTelefone.getText().isBlank() || txtEmail.getText().isBlank()
                    || txtDataNascimento.getText().isBlank()) {
                showMessage(Alert.AlertType.ERROR, "Preencha os campos obrigatórios!");
                return;
            }

            LocalDate dataNascimento =
                    LocalDate.parse(txtDataNascimento.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String jsonRequest = String.format("{\n"
                            + "  \"nome\": \"%s\",\n"
                            + "  \"cpf\": \"%s\",\n"
                            + "  \"telefone\": \"%s\",\n"
                            + "  \"email\": \"%s\",\n"
                            + "  \"dataNascimento\": \"%s\",\n"
                            + "  \"endereco\": \"%s\",\n"
                            + "  \"observacoes\": \"%s\"\n"
                            + "}",
                    txtNome.getText(),
                    txtCpf.getText(),
                    txtTelefone.getText(),
                    txtEmail.getText(),
                    dataNascimento,
                    txtEndereco.getText(),
                    txtObservacoes.getText());

            int respostaAPI = executaMetodoAPI("http://localhost:8080/pacientes", jsonRequest, "POST");

            if (respostaAPI == 200 || respostaAPI == 201) {
                showMessage(Alert.AlertType.INFORMATION, "Paciente salvo com sucesso!");
                voltar(event);
            } else {
                showMessage(Alert.AlertType.ERROR, "Erro ao salvar paciente! Código: " + respostaAPI);
            }
        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, "Erro ao salvar paciente! " + e.getMessage());
        }
    }

    @FXML
    public void voltar(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/example/back/menu-view.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private int executaMetodoAPI(String url, String json, String protocoloHttp) throws Exception {
        URL urlAPI = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlAPI.openConnection();
        connection.setRequestMethod(protocoloHttp);
        connection.setDoOutput(true);
        if (!DesktopSession.token.isBlank()) {
            connection.setRequestProperty("Authorization", "Bearer " + DesktopSession.token);
        }

        if (!json.isEmpty()) {
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
        }

        return connection.getResponseCode();
    }

    private void showMessage(Alert.AlertType type, String msg) {
        Alert alerta = new Alert(type);
        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }

}
