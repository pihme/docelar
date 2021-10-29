package com.github.pihme.docelar.frontend;

import com.github.pihme.docelar.frontend.home.HomeView;
import com.github.pihme.docelar.frontend.log.LogView;
import com.github.pihme.docelar.frontend.state.StateView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Doce Lar", shortName = "Doce Lar", enableInstallPrompt = false)
@Theme(themeFolder = "docelar")
@PageTitle("Main")
public class MainLayout extends AppLayout {

    public static class MenuItemInfo {

        private final String text;
        private final String iconClass;
        private final Class<? extends Component> view;

        public MenuItemInfo(final String text, final String iconClass, final Class<? extends Component> view) {
            this.text = text;
            this.iconClass = iconClass;
            this.view = view;
        }

        public String getText() {
            return text;
        }

        public String getIconClass() {
            return iconClass;
        }

        public Class<? extends Component> getView() {
            return view;
        }

    }

    public MainLayout() {
        addToNavbar(createHeaderContent());

        Notification.show("Don't Panic!", 10000, Notification.Position.MIDDLE);
    }

    private Component createHeaderContent() {
        final Header header = new Header();
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "flex-col", "w-full");

        final Div layout = new Div();
        layout.addClassNames("flex", "h-xl", "items-center", "px-l");

        final H1 appName = new H1("Doce Lar");
        appName.addClassNames("my-0", "me-auto", "text-l");
        layout.add(appName);

        final Nav nav = new Nav();
        nav.addClassNames("flex", "gap-s", "overflow-auto", "px-m");

        // Wrap the links in a list; improves accessibility
        final UnorderedList list = new UnorderedList();
        list.addClassNames("flex", "list-none", "m-0", "p-0");
        nav.add(list);

        for (final RouterLink link : createLinks()) {
            final ListItem item = new ListItem(link);
            list.add(item);
        }

        header.add(layout, nav);
        return header;
    }

    private List<RouterLink> createLinks() {
        final MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
                new MenuItemInfo("Home", "la la-home", HomeView.class), //

                new MenuItemInfo("Log", "la la-laptop-code", LogView.class), //

                new MenuItemInfo("State", "la la-database", StateView.class), //

        };
        final List<RouterLink> links = new ArrayList<>();
        for (final MenuItemInfo menuItemInfo : menuItems) {
            links.add(createLink(menuItemInfo));

        }
        return links;
    }

    private static RouterLink createLink(final MenuItemInfo menuItemInfo) {
        final RouterLink link = new RouterLink();
        link.addClassNames("flex", "h-m", "items-center", "px-s", "relative", "text-secondary");
        link.setRoute(menuItemInfo.getView());

        final Span icon = new Span();
        icon.addClassNames("me-s", "text-l");
        if (!menuItemInfo.getIconClass().isEmpty()) {
            icon.addClassNames(menuItemInfo.getIconClass());
        }

        final Span text = new Span(menuItemInfo.getText());
        text.addClassNames("font-medium", "text-s", "whitespace-nowrap");

        link.add(icon, text);
        return link;
    }

}
