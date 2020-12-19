package com.salesmanager.shop.admin.controller.storecredit;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DirStcrController {
    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/directory.html", method= RequestMethod.GET)
    public String displayAccounts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.setMenu(model, request);

        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

        return ControllerConstants.Tiles.Storecredit.dirStcr;

    }

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/add.html", method= RequestMethod.GET)
    public String displayAddAccounts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.setAddMenu(model, request);

        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

        return ControllerConstants.Tiles.Storecredit.adStcr;

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
    private void setAddMenu(Model model, HttpServletRequest request) throws Exception {

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
