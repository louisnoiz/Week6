package com.example.week6.view;

import com.example.week6.pojo.Wizard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Route(value = "/mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullName, dollars;
    private RadioButtonGroup<String> gender;
    private ComboBox<String> position, school, house;
    private Button backward, create , update, delete ,forward;
    private int num = 0;
    Wizard present;
    public MainWizardView() {
        fullName = new TextField("", "Fullname");

        gender = new RadioButtonGroup<>();
        gender.setLabel("Gender :");
        gender.setItems("Male", "Female");

        position = new ComboBox<>();
        position.setItems("Student", "Teacher");
        position.setPlaceholder("Position");

        dollars = new TextField("Dollars");
        dollars.setPrefixComponent(new Span("$"));

        school = new ComboBox<>();
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        school.setPlaceholder("School");

        house = new ComboBox<>();
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");
        house.setPlaceholder("House");

        backward = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        forward = new Button(">>");
        HorizontalLayout btnGroup = new HorizontalLayout(backward, create, update, delete, forward);

        this.add(fullName, gender, position, dollars, school, house, btnGroup);

        create.addClickListener(event -> {
            String sex = "";
            if (gender.getValue().equals("Male")){
                sex = "m";
            } else if (gender.getValue().equals("Female")) {
                sex = "f";
            }
            Wizard out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(new Wizard(null,sex, fullName.getValue(), school.getValue(), house.getValue(), Integer.parseInt(dollars.getValue()), position.getValue())),Wizard.class)
                    .retrieve()
                    .bodyToMono((Wizard.class))
                    .block();
            new Notification("created", 5000).open();

        });
        backward.addClickListener(event -> {

            List<Wizard> out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();
            new Notification("Backward", 5000).open();

            if (num != 0){
                num --;
            }
            Wizard wizard = mapper.convertValue(out.get(num), Wizard.class);
            present = wizard;
            fullName.setValue(wizard.getName());
            if (wizard.getSex().equals("m")){
                gender.setValue("Male");
            } else if (wizard.getSex().equals("f")) {
                gender.setValue("Female");
            }
            position.setValue(wizard.getPosition());
            dollars.setValue(String.valueOf(wizard.getMoney()));
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());
        });

        forward.addClickListener(event -> {

             List<Wizard> out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();
            new Notification("Forward", 5000).open();

            if (num < out.size()-1){
                num ++;
            }
            Wizard wizard = mapper.convertValue(out.get(num), Wizard.class);
            present = wizard;
            fullName.setValue(wizard.getName());
            if (wizard.getSex().equals("m")){
                gender.setValue("Male");
            } else if (wizard.getSex().equals("f")) {
                gender.setValue("Female");
            }
            position.setValue(wizard.getPosition());
            dollars.setValue(String.valueOf(wizard.getMoney()));
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());
        });

        delete.addClickListener( event -> {
            boolean out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(Mono.just(this.present.get_id()), String.class)
                    .retrieve()
                    .bodyToMono((boolean.class))
                    .block();
            new Notification("deleted", 5000).open();
        });

        update.addClickListener(event -> {
            String sex = "";
            if (gender.getValue().equals("Male")){
                sex = "m";
            } else if (gender.getValue().equals("Female")) {
                sex = "f";
            }
            Wizard out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .body(Mono.just(new Wizard(this.present.get_id(),sex, fullName.getValue(), school.getValue(), house.getValue(), Integer.parseInt(dollars.getValue()), position.getValue())),Wizard.class)
                    .retrieve()
                    .bodyToMono((Wizard.class))
                    .block();
            new Notification("updated", 5000).open();
        });


    }
}
