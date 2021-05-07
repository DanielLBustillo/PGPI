package com.example.application.views.Generatepiking;

import com.example.application.views.MainAdmin.MainViewAdmin;
import com.example.application.views.Mainshop.MainViewShop;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "generate-piking", layout = MainViewShop.class)
@PageTitle("Generate piking")
@CssImport("./views/generatepiking/generatepiking-view.css")
public class GeneratepikingView extends Div {

    public GeneratepikingView() {
        addClassName("generatepiking-view");
        add(new Text("Content placeholder"));
    }

}
