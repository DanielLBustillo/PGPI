package com.example.application.views.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "admin", layout = MainView.class)
@PageTitle("Admin")
@CssImport("./views/admin/admin-view.css")
public class AdminView extends Div {

    public AdminView() {
        addClassName("admin-view");
        add(new Text("Content placeholder"));
    }

}
