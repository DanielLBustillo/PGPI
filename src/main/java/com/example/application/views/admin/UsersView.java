package com.example.application.views.admin;

import java.util.ArrayList;
import java.util.Optional;

import com.example.application.data.entity.Appuser;
import com.example.application.data.entity.Appusers;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.checkbox.Checkbox;

@Route(value = "Appusers", layout = MainViewAdmin.class)
@PageTitle("Appusers")
@CssImport("./views/orders/orders-view.css")
public class UsersView extends Div implements BeforeEnterObserver {

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
        addClassName("user-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        TemplateRenderer<Appuser> importantRenderer = TemplateRenderer.<Appuser>of(
                "<iron-icon hidden='[[!item.important]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.important]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

		refreshGrid();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format("Orders/%d/edit", event.getValue().getId()));
                populateForm(event.getValue());
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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (true) {
            
	    } else {
	        Notification.show(
	                String.format("The requested Appuser was not found, ID = %d", this.appuser.getId()), 3000,
	                Notification.Position.BOTTOM_START);
	        // when a row is selected but the data is no longer available,
	        // refresh grid
	        refreshGrid();
	        event.forwardTo(UsersView.class);
	    }
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
    	
    }

    private void populateForm(Appuser appuser) {
        this.appuser = appuser;
        this.Name.setValue(appuser.getId());
        this.Role.setValue(appuser.getRol());
        this.Userinfo.setValue(appuser.getUserinf());
    }
}
