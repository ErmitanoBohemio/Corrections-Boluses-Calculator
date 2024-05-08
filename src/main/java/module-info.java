module com.deitel.diabetescorrectionsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    opens com.deitel.diabetescorrectionsapp to javafx.fxml;
    exports com.deitel.diabetescorrectionsapp;
}
