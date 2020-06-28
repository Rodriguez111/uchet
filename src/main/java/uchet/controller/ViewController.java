package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uchet.models.InDoc;
import uchet.models.User;
import uchet.security.CustomUserPrincipal;
import uchet.service.indocs.InDocService;

import java.util.HashMap;

@Controller
public class ViewController {

    private InDocService inDocService;

    @RequestMapping("/login")
    public String showLogin() {
        return "root/login";
    }

    @RequestMapping(value={"/main", "/"})
    public String showMainPage(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "root/main_page";
    }


    @RequestMapping("/admin/users")
    public String showUsers(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "admin/users";
    }

    @RequestMapping("/admin/positions")
    public String showPositions(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "admin/positions";
    }

    @RequestMapping("/accounting/remains")
    public String showGoodsRemains(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "accounting/remains";
    }



    @RequestMapping("/accounting/incoming_docs")
    public String showIncomingDocs(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "accounting/in_docs/incoming_docs";
    }

    @GetMapping
    @RequestMapping("/accounting/incoming_doc_details/{docNumber}")
    public String showIncomingDocDetails(@PathVariable String docNumber,
            Model model,
                                         @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        System.out.println(docNumber);
        InDoc inDoc = inDocService.getByInDocNumber(docNumber);
        model.addAttribute("inDoc", inDoc);
        return "accounting/in_docs/incoming_doc";
    }

    @RequestMapping("/accounting/add_incoming_doc")
    public String showAddIncomingDoc(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "accounting/in_docs/add_in_doc";
    }

    @RequestMapping("/accounting/sku_in")
    public String showIncomingSku(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "accounting/sku_in/sku_in";
    }

    @RequestMapping("/accounting/outgoing_docs")
    public String showOutgoingDocs(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "accounting/outgoing_docs";
    }

    @RequestMapping("/accounting/sku_out")
    public String showOutgoingSku(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "accounting/sku_out";
    }

    @RequestMapping("/contractors")
    public String showContractors(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "root/contractors";
    }


    @RequestMapping("/sku")
    public String showSku(Model model, @AuthenticationPrincipal UserDetails currentUserDetails) {
        model.addAttribute("authData", getUserDetails(currentUserDetails));
        return "root/sku";
    }


    private HashMap<Object, Object> getUserDetails(UserDetails currentUserDetails) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal)currentUserDetails;
        User user = ((CustomUserPrincipal) currentUserDetails).getUser();
        HashMap<Object, Object> data = new HashMap<>();
        data.put("profile", currentUserDetails);
        data.put("name", user.getName());
        data.put("surname", user.getSurname());
        data.put("position", user.getPosition().getPosition());
        return data;
    }

    @Autowired
    public void setInDocService(InDocService inDocService) {
        this.inDocService = inDocService;
    }



}
