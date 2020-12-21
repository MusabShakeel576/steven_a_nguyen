package com.salesmanager.shop.admin.controller.storecredit;

import com.salesmanager.core.business.services.system.MerchantConfigurationService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.system.MerchantConfiguration;
import com.salesmanager.core.model.system.MerchantConfigurationType;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.ConfigListWrapper;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class DirStcrController {

    @Inject
    private MerchantConfigurationService merchantConfigurationService;

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/directory.html", method= RequestMethod.GET)
    public String displayDirectory(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.setMenu(model, request);

        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);

        return ControllerConstants.Tiles.Storecredit.dirStcr;

    }

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/storecredit/add.html", method= RequestMethod.GET)
    public String displayAdd(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.setAddMenu(model, request);
        List<MerchantConfiguration> configs = new ArrayList<MerchantConfiguration>();
        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        MerchantConfiguration merchantFBConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_FACEBOOK_PAGE_URL,store);
        if(null == merchantFBConfiguration)
        {
            merchantFBConfiguration = new MerchantConfiguration();
            merchantFBConfiguration.setKey(Constants.KEY_FACEBOOK_PAGE_URL);
            merchantFBConfiguration.setMerchantConfigurationType(MerchantConfigurationType.SOCIAL);
        }
        configs.add(merchantFBConfiguration);

        MerchantConfiguration merchantGoogleAnalyticsConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_GOOGLE_ANALYTICS_URL,store);
        if(null == merchantGoogleAnalyticsConfiguration)
        {
            merchantGoogleAnalyticsConfiguration = new MerchantConfiguration();
            merchantGoogleAnalyticsConfiguration.setKey(Constants.KEY_GOOGLE_ANALYTICS_URL);
            merchantGoogleAnalyticsConfiguration.setMerchantConfigurationType(MerchantConfigurationType.SOCIAL);
        }
        configs.add(merchantGoogleAnalyticsConfiguration);

        MerchantConfiguration merchantInstagramConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_INSTAGRAM_URL,store);
        if(null == merchantInstagramConfiguration)
        {
            merchantInstagramConfiguration = new MerchantConfiguration();
            merchantInstagramConfiguration.setKey(Constants.KEY_INSTAGRAM_URL);
            merchantInstagramConfiguration.setMerchantConfigurationType(MerchantConfigurationType.SOCIAL);
        }
        configs.add(merchantInstagramConfiguration);

        MerchantConfiguration merchantPinterestConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_PINTEREST_PAGE_URL,store);
        if(null == merchantPinterestConfiguration)
        {
            merchantPinterestConfiguration = new MerchantConfiguration();
            merchantPinterestConfiguration.setKey(Constants.KEY_PINTEREST_PAGE_URL);
            merchantPinterestConfiguration.setMerchantConfigurationType(MerchantConfigurationType.SOCIAL);
        }
        configs.add(merchantPinterestConfiguration);

        /**
         MerchantConfiguration merchantGoogleApiConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_GOOGLE_API_KEY,store);
         if(null == merchantGoogleApiConfiguration)
         {
         merchantGoogleApiConfiguration = new MerchantConfiguration();
         merchantGoogleApiConfiguration.setKey(Constants.KEY_GOOGLE_API_KEY);
         merchantGoogleApiConfiguration.setMerchantConfigurationType(MerchantConfigurationType.CONFIG);
         }
         configs.add(merchantGoogleApiConfiguration);
         **/

        MerchantConfiguration twitterConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_TWITTER_HANDLE,store);
        if(null == twitterConfiguration)
        {
            twitterConfiguration = new MerchantConfiguration();
            twitterConfiguration.setKey(Constants.KEY_TWITTER_HANDLE);
            twitterConfiguration.setMerchantConfigurationType(MerchantConfigurationType.SOCIAL);
        }
        configs.add(twitterConfiguration);

        ConfigListWrapper configWrapper = new ConfigListWrapper();
        configWrapper.setMerchantConfigs(configs);
        model.addAttribute("configuration",configWrapper);
        return ControllerConstants.Tiles.Storecredit.adStcr;

    }

    @PreAuthorize("hasRole('AUTH')")
    @RequestMapping(value="/admin/configuration/saveConfiguration.html", method=RequestMethod.POST)
    public String saveAdd(@ModelAttribute("configuration") ConfigListWrapper configWrapper, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception
    {
        setAddMenu(model, request);
        List<MerchantConfiguration> configs = configWrapper.getMerchantConfigs();
        MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        for(MerchantConfiguration mConfigs : configs)
        {
            mConfigs.setMerchantStore(store);
            if(!StringUtils.isBlank(mConfigs.getValue())) {
                mConfigs.setMerchantConfigurationType(MerchantConfigurationType.SOCIAL);
                merchantConfigurationService.saveOrUpdate(mConfigs);
            } else {//remove if submited blank and exists
                MerchantConfiguration config = merchantConfigurationService.getMerchantConfiguration(mConfigs.getKey(), store);
                if(config!=null) {
                    merchantConfigurationService.delete(config);
                }
            }
        }
        model.addAttribute("success","success");
        model.addAttribute("configuration",configWrapper);
        return com.salesmanager.shop.admin.controller.ControllerConstants.Tiles.Storecredit.adStcr;

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
