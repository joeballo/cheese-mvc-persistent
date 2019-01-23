package org.launchcode.controllers;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {

        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();

    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable("id") int id) {

        model.addAttribute("menu", menuDao.findOne(id));



        return "menu/view";
    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.GET)
    public String displayAddItemForm(Model model, @PathVariable("id") int id) {

        Menu aMenu = menuDao.findOne(id);

        model.addAttribute("form", new AddMenuItemForm(aMenu, cheeseDao.findAll()));
        model.addAttribute("title", "Add item to the " + aMenu.getName() + " menu");

        return "menu/add-item";
    }



    @RequestMapping(value = "add-item/{id}", method = RequestMethod.POST)
    public String processAddItemForm(Model model, @ModelAttribute @Valid AddMenuItemForm aMenuItemForm, Errors errors) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu:");
            return "add-item";
        }

        Menu theMenu = menuDao.findOne(aMenuItemForm.getMenuId());

        Cheese theCheese = cheeseDao.findOne(aMenuItemForm.getCheeseId());

        theMenu.addItem(theCheese);

        menuDao.save(theMenu);

        return "redirect:/menu/view/" + theMenu.getId();



    }



}
