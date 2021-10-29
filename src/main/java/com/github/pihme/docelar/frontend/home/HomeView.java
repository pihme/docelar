package com.github.pihme.docelar.frontend.home;

import com.github.pihme.docelar.backend.Unzipper;
import com.github.pihme.docelar.frontend.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@PageTitle("Home")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HomeView extends HorizontalLayout {


    private final TextField name;
    private final Button sayHello;
    private final Upload upload;
    private final MemoryBuffer memoryBuffer = new MemoryBuffer();

    @Autowired
    private Unzipper unzipper;


    public HomeView() {
        setMargin(true);
        upload = new Upload();
        upload.setReceiver(memoryBuffer);

        upload.addFinishedListener(finishedEvent -> {
            Notification.show("Upload finished for " + finishedEvent.getFileName());
            try {
                unzipper.unzip(memoryBuffer, finishedEvent.getMIMEType());
            } catch (final IOException e) {
                Notification.show(e.getMessage(), 10000, Notification.Position.MIDDLE);
            }
        });


        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        final var column = new VerticalLayout();
        column.add(upload, name, sayHello);
        add(column);

        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

}
