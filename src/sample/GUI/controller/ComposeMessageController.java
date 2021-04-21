package sample.GUI.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import sample.BE.BaseController;
import sample.BE.EmailAccount;
import sample.BLL.EmailManager;
import sample.GUI.controller.services.EmailSenderService;
import sample.GUI.view.ViewFactory;
import java.net.URL;
import java.util.ResourceBundle;


    public class ComposeMessageController extends BaseController implements Initializable {

        @FXML
        private TextField recipientTextField;
        @FXML
        private TextField subjectTextField;
        @FXML
        private HTMLEditor htmlEditor;
        @FXML
        private Label errorLabel;

        @FXML
        private ChoiceBox<EmailAccount> emailAccountChoice;


        @FXML
        void sendButtonAction() {
            EmailSenderService emailSenderService = new EmailSenderService(
                    emailAccountChoice.getValue(),
                    subjectTextField.getText(),
                    recipientTextField.getText(),
                    htmlEditor.getHtmlText()
            );
            emailSenderService.start();
            emailSenderService.setOnSucceeded(e->{
                EmailSendingResult emailSendingResult = emailSenderService.getValue();
                switch (emailSendingResult) {
                    case SUCCESS:
                        Stage stage = (Stage) recipientTextField.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        break;
                    case FAILED_BY_PROVIDER:
                        errorLabel.setText("Provider error!");
                        break;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("Unexpected error!");
                        break;
                }
            });
        }

        public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
            super(emailManager, viewFactory, fxmlName);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            emailAccountChoice.setItems(emailManager.getEmailAccounts());
            emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
        }
    }
