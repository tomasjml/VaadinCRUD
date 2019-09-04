package com.vaadin.example.spring;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {

    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>(Customer.class);
    private TextField filterText = new TextField();

    private CustomerForm form = new CustomerForm(this);
    public MainView(@Autowired MessageBean bean) {
        //Toolbar
        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });
        Button clearBtn = new Button("Clear");
        clearBtn.addClickListener(e -> {
            form.setCustomer(null);
            grid.asSingleSelect().clear();
        });
        clearBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerBtn, clearBtn);

        //Grid View
        grid.setColumns("firstName", "lastName", "status");
        grid.asSingleSelect().addValueChangeListener(event ->
                form.setCustomer(grid.asSingleSelect().getValue()));

        //Form
        form.setCustomer(null);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();

        add(toolbar, mainContent);
        setSizeFull();
        updateList();
    }

    public void updateList(){
        grid.setItems(service.findAll(filterText.getValue()));
    }
}
