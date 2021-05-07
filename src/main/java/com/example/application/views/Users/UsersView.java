package com.example.application.views.Users;

import java.util.ArrayList;
import java.util.Optional;

import com.example.application.data.entity.Appuser;
import com.example.application.data.entity.Appusers;
import com.example.application.views.MainAdmin.MainViewAdmin;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.checkbox.Checkbox;

@Route(value = "Appusers", layout = MainViewAdmin.class)
@PageTitle("UsersView")
@CssImport("./views/orders/orders-view.css")
public class UsersView extends Div  {

	private Appusers appusers= new Appusers();
    private Grid<Appuser> grid = new Grid<>(Appuser.class);

	private Appuser appuser;
    

    private TextField Name;  
    private TextField Password;
    private TextField Role;
    private TextField Userinfo;
    
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");



    public UsersView() throws ValidationException {
    	if(!VaadinSession.getCurrent().getAttribute("role").toString().equals("ADMIN"))
			UI.getCurrent().navigate("login-view");
        addClassName("user-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

		refreshGrid();

        // when a row is selected or deselected, populate form
        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().get() != null) {
                populateForm(event.getFirstSelectedItem().get());
            } else {
                clearForm();
                UI.getCurrent().navigate(UsersView.class);
            }
        });
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            if (this.appuser == null) {
			    this.appuser = new Appuser("","","");
			}
			//add appuser to database
			clearForm();
			refreshGrid();
			Notification.show("Appuser details stored.");
			UI.getCurrent().navigate(UsersView.class);
        });

    }

   

    private void refreshGrid() {
		this.grid.setItems(this.appusers.getPersonList());
		
	}

	private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        Name = new TextField("Name");
        Password = new TextField("Password");
        Role = new TextField("Role");
        Userinfo = new TextField("Userinfo");
        Component[] fields = new Component[]{Name, Password, Role, Userinfo};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void clearForm() {
        this.Name.setValue("");
        this.Role.setValue("");
        this.Userinfo.setValue("");
    }

    private void populateForm(Appuser appuser) {
        this.appuser = appuser;
        this.Name.setValue(appuser.getId());
        this.Role.setValue(appuser.getRol());
        this.Userinfo.setValue(appuser.getUserinf());
    }
}
