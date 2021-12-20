package com.github.pihme.docelar.frontend.zeebe;

import com.github.pihme.docelar.frontend.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.camunda.zeebe.client.ZeebeClient;

@PageTitle("Zeebe")
@Route(value = "zeebe", layout = MainLayout.class)
public class ZeebeView extends VerticalLayout {

	private final TextArea credentialsField;
	private final Button connectButton;
	private final Button disconnectButton;

	private final Credentials credentials = new Credentials();

	private ZeebeClient client = null;

	public ZeebeView() {
		setSpacing(false);

		credentialsField = new TextArea("Credentials");
		credentialsField.setWidth("800px");
		credentialsField.setHeight("120px");
		add(credentialsField);		
		Binder<Credentials> binder = new Binder<>(Credentials.class);
		binder.forField(credentialsField).withValidator(new StringLengthValidator("Must not be empty", 1, null))
				.withValidator(new RegexpValidator("Must match '" + Credentials.REGEX_ZEEBE_ADRESS + "'",
						Credentials.REGEX_ZEEBE_ADRESS, false))
				.withValidator(new RegexpValidator("Must match '" + Credentials.REGEX_ZEEBE_CLIENT_ID + "'",
						Credentials.REGEX_ZEEBE_CLIENT_ID, false))
				.withValidator(new RegexpValidator("Must match '" + Credentials.REGEX_ZEEBE_CLIENT_SECRET + "'",
						Credentials.REGEX_ZEEBE_CLIENT_SECRET, false))
				.withValidator(
						new RegexpValidator("Must match '" + Credentials.REGEX_ZEEBE_AUTHORIZATION_SERVER_URL + "'",
								Credentials.REGEX_ZEEBE_AUTHORIZATION_SERVER_URL, false))
				.bind(Credentials::getCredentialsRaw, Credentials::setCredentialsRaw);

		binder.setBean(credentials);

		connectButton = new Button("Connect");
		disconnectButton = new Button("Disconnect");
		disconnectButton.setVisible(false);
		add(connectButton, disconnectButton);
		connectButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				if (binder.validate().isOk()) {
					binder.writeBeanIfValid(credentials);

					if (client != null) {
						client.close();
					}

					client = new ZeebeClientBuilder().build(credentials);
					try {
						client.newTopologyRequest().send().join();
						Notification.show("Connection established!");
						credentialsField.setVisible(false);
						connectButton.setVisible(false);
						disconnectButton.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
						Notification.show("Connection failed: " + e.getMessage(), 10000, Notification.Position.MIDDLE);
						client.close();
						client = null;						
					}
				}
			}
		});

		disconnectButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				client.close();
				client = null;
				credentials.setCredentialsRaw(null);				
				
				credentialsField.clear();				
				credentialsField.setVisible(true);				
				connectButton.setVisible(true);
				disconnectButton.setVisible(false);
			}
		});
	}

}
