module com.example.blockchainvoting {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.blockchainvoting to javafx.fxml;
    exports com.example.blockchainvoting;
}