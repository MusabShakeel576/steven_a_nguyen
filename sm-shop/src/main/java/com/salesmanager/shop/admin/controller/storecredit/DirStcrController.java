package com.salesmanager.shop.admin.controller.storecredit;

import com.salesmanager.core.business.services.storecredit.AdStcrService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.storecredit.AdStcr;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class DirStcrController {

    @Inject
    private AdStcrService adStcrService;

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/directory.html", method= RequestMethod.GET)
    public ModelAndView displayDirectory(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.setMenu(model, request);
        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        List<AdStcr> list=adStcrService.getAllAdStcr();
        return new ModelAndView(ControllerConstants.Tiles.Storecredit.dirStcr,"list",list);

    }

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/add.html", method= RequestMethod.GET)
    public String displayAdStcr(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.setAdStcrMenu(model, request);
        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        AdStcr adstcr = new AdStcr();
        model.addAttribute("adstcr", adstcr);
        return ControllerConstants.Tiles.Storecredit.adStcr;
    }

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/saveAd.html", method=RequestMethod.POST)
    public String saveAdStcr(@ModelAttribute("adstcr") AdStcr adstcr, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception
    {
        setAdStcrMenu(model, request);
        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        adStcrService.saveAdStcr(adstcr);
        String saveAdSuccess = "Save Ad Success";
        model.addAttribute("saveAdSuccess", saveAdSuccess);
        return com.salesmanager.shop.admin.controller.ControllerConstants.Tiles.Storecredit.adStcr;

    }

    @RequestMapping(value="/admin/storecredit/deleteAd.html/{id}",method = RequestMethod.GET)
    public ModelAndView deleteAdStcr(@PathVariable int id){
        adStcrService.deleteAdStcrById(id);
        String deleteSuccess = "Deleted Successfully";
        return new ModelAndView(ControllerConstants.Tiles.Storecredit.dirStcr, "deleteSuccess", deleteSuccess);
    }

    private void setMenu(Model model, HttpServletRequest request) throws Exception {

        Map<String,String> activeMenus = new HashMap<String,String>();
        activeMenus.put("storecredit", "storecredit");
        activeMenus.put("dir-stcr", "dir-stcr");

        @SuppressWarnings("unchecked")
        Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");

        Menu currentMenu = (Menu)menus.get("storecredit");
        model.addAttribute("currentMenu",currentMenu);
        model.addAttribute("activeMenus",activeMenus);
    }

    private void setAdStcrMenu(Model model, HttpServletRequest request) throws Exception {

        Map<String,String> activeMenus = new HashMap<String,String>();
        activeMenus.put("storecredit", "storecredit");
        activeMenus.put("ad-stcr", "ad-stcr");

        @SuppressWarnings("unchecked")
        Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");

        Menu currentMenu = (Menu)menus.get("storecredit");
        model.addAttribute("currentMenu",currentMenu);
        model.addAttribute("activeMenus",activeMenus);
    }
}
