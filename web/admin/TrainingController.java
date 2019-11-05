package com.jing.blogs.web.admin;

import com.jing.blogs.domain.Trainning;
import com.jing.blogs.domain.User;
import com.jing.blogs.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class TrainingController {
    private final String LIST = "admin/trainings";
    private final String REDIRECT_LIST = "redirect:/admin/trainings";
    private final String INPUT = "admin/training-input";
    @Autowired
    private TrainingService trainingService;

    @GetMapping("/trainings")
    public String listAll(Model model){
        model.addAttribute("trainings",trainingService.listAll());
        return LIST;
    }
    @GetMapping("/training/input")
    public String input(Model model){
        Trainning trainning = new Trainning();
        model.addAttribute("training",trainning);
        return INPUT;
    }
    @GetMapping("/training/{id}/input")
    public String edit(@PathVariable Long id, Model model){
        Trainning trainning = trainingService.getTraining(id);
        if(trainning == null) return REDIRECT_LIST;
        System.out.println(trainning.getId());
        model.addAttribute("training",trainning);
        return INPUT;
    }
    @GetMapping("/training/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        try{
            trainingService.deleteTraining(id);
            attributes.addFlashAttribute("message","Delete Succeed");
        }catch (Exception e){
            attributes.addFlashAttribute("message","Delete Failed");
        }
        return REDIRECT_LIST;
    }
    @PostMapping("/trainings")
    public String postTraining(RedirectAttributes attributes, Trainning trainning, HttpSession session){
        Trainning t;
        System.out.println("Test:"+trainning.getDurations());
        trainning.setCoach((User)session.getAttribute("user"));
        if(trainning.getId() == null) {

            trainning.setOrdered(0);
            t = trainingService.saveTraining(trainning);
        }else {
            t = trainingService.updateTraining(trainning.getId(), trainning);
        }
        if(t == null)
            attributes.addFlashAttribute("message","submit failed!");
        else
            attributes.addFlashAttribute("message","submit succeed!");
        return REDIRECT_LIST;
    }

}
