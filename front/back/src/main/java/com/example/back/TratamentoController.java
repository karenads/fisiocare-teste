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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TratamentoController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtDataInicio;

    @FXML
    private TextField txtDataFinal;

    @FXML
    private TextField txtTotalSessoes;

    @FXML
    private TextField txtSessoesRealizadas;

    @FXML
    public void salvarTratamento(ActionEvent event) {
        try {
            // Validar dados
            if (txtNome.getText().isBlank() || txtDataInicio.getText().isBlank()
                    || txtDataFinal.getText().isBlank() || txtTotalSessoes.getText().isBlank()
                    || txtSessoesRealizadas.getText().isBlank()) {
                showMessage(Alert.AlertType.ERROR, "Preencha os campos obrigatórios!");
                return;
            }

            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataInicio = LocalDateTime.parse(txtDataInicio.getText(), formatoData);
            LocalDateTime dataFinal = LocalDateTime.parse(txtDataFinal.getText(), formatoData);

            int totalSessoes = Integer.parseInt(txtTotalSessoes.getText());
            int sessoesRealizadas = Integer.parseInt(txtSessoesRealizadas.getText());

            String jsonRequest = String.format("{\n"
                            + "  \"nome\": \"%s\",\n"
                            + "  \"descricao\": \"%s\",\n"
                            + "  \"dataInicio\": \"%s\",\n"
                            + "  \"dataFinal\": \"%s\",\n"
                            + "  \"totalSessoes\": %d,\n"
                            + "  \"sessoesRealizadas\": %d\n"
                            + "}",
                    txtNome.getText(),
                    txtDescricao.getText(),
                    dataInicio,
                    dataFinal,
                    totalSessoes,
                    sessoesRealizadas);

            int respostaAPI = executaMetodoAPI("http://localhost:8080/tratamentos", jsonRequest, "POST");

            if (respostaAPI == 200 || respostaAPI == 201) {
                showMessage(Alert.AlertType.INFORMATION, "Tratamento salvo com sucesso!");
                voltar(event);
            } else {
                showMessage(Alert.AlertType.ERROR, "Erro ao salvar tratamento! Código: " + respostaAPI);
            }
        } catch (Exception e) {
            showMessage(Alert.AlertType.ERROR, "Erro ao salvar tratamento! " + e.getMessage());
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
