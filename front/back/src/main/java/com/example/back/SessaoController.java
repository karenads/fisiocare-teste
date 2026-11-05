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
import javafx.scene.control.CheckBox;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SessaoController {

    @FXML
    private TextField txtData;

    @FXML
    private TextField txtHorario;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtObservacoes;

    @FXML
    private CheckBox chkRealizada;

    @FXML
    public void salvarSessao(ActionEvent event) {
        try {
            // Validar dados
            if (txtData.getText().isBlank() || txtHorario.getText().isBlank()) {
                showMessage(Alert.AlertType.ERROR, "Informe a data e o horário da sessão!");
                return;
            }

            LocalDate data = LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime horario = LocalTime.parse(txtHorario.getText(), DateTimeFormatter.ofPattern("HH:mm"));

            String jsonRequest = String.format("{\n"
                            + "  \"data\": \"%s\",\n"
                            + "  \"horario\": \"%s\",\n"
                            + "  \"descricao\": \"%s\",\n"
                            + "  \"observacoes\": \"%s\",\n"
                            + "  \"realizada\": %s\n"
                            + "}",
                    data,
                    horario,
                    txtDescricao.getText(),
                    txtObservacoes.getText(),
                    chkRealizada.isSelected());

            int respostaAPI = executaMetodoAPI("http://localhost:8080/sessoes", jsonRequest, "POST");

            if (respostaAPI == 200 || respostaAPI == 201) {
                showMessage(Alert.AlertType.INFORMATION, "Sessão salva com sucesso!");
                voltar(event);
            } else {
                showMessage(Alert.AlertType.ERROR, "Erro ao salvar sessão! Código: " + respostaAPI);
            }
        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, "Erro ao salvar sessão! " + e.getMessage());
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
