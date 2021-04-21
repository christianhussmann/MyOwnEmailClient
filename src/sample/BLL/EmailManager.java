package sample.BLL;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.BE.EmailMessage;
import sample.GUI.controller.services.FetchFoldersService;
import sample.BE.EmailAccount;
import sample.GUI.controller.services.FolderUpdaterService;
import sample.GUI.model.EmailTreeItem;
import sample.GUI.view.IconResolver;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    private IconResolver iconResolver = new IconResolver();

    public ObservableList<EmailAccount> getEmailAccounts(){
        return emailAccounts;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public void setFolderUpdaterService(FolderUpdaterService folderUpdaterService) {
        this.folderUpdaterService = folderUpdaterService;
    }

    public void setFoldersRoot(EmailTreeItem<String> foldersRoot) {
        this.foldersRoot = foldersRoot;
    }

    public void setFoldersList(List<Folder> foldersList) {
        this.foldersList = foldersList;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public FolderUpdaterService getFolderUpdaterService() {
        return folderUpdaterService;
    }

    public EmailMessage getSelectedMessage(){
        return selectedMessage;
    }

    private FolderUpdaterService folderUpdaterService;
    //Folder handling:
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    private List<Folder> foldersList = new ArrayList<Folder>();
    public List<Folder> getFoldersList(){
        return this.foldersList;
    }

    public EmailManager(){
        folderUpdaterService = new FolderUpdaterService(foldersList);
        folderUpdaterService.start();
    }

    public void addEmailAccount(EmailAccount emailAccount){
        emailAccounts.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem, foldersList);
        fetchFoldersService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public void setRead() {
        try{
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN,true);
            selectedFolder.decrementMessagesCount();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUnRead() {
        try{
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN,false);
            selectedFolder.incrementMessagesCount();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try{
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED,true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
