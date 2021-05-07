package com.example.application.views.pickinglist;

import com.example.application.views.admin.MainViewAdmin;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "generate_piking", layout = MainViewPiking.class)
@PageTitle("Generate piking")
@CssImport("./views/generatepiking/generatepiking-view.css")
public class GeneratepikingView extends Div {

    public GeneratepikingView() {
        addClassName("generatepiking-view");
        add(new Text("Content placeholder"));
    }

}
