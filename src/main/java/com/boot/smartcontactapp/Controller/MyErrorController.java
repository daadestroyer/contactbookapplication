package com.boot.smartcontactapp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorPage = "error";
        String pageMessage = "error";
        String pageTitle = "errorPage";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                pageTitle = "Error 404 !!!";
                pageMessage = "Error 404 - Page not found";
                errorPage = "error/404";
                LOGGER.error("ERROR 404");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                pageTitle = " Error 500 !!!";
                pageMessage = "Error 500 - Internal Server Error";
                errorPage = "error/500";
                LOGGER.error("Error 500");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                pageTitle = "Error 403 !!!";
                pageMessage = "Error 403 - Forbidden Error";
                errorPage = "error/403";
                LOGGER.error("Error 403");
            }else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                pageTitle = "Error 405 !!!";
                pageMessage = "Error 405 - Method not allowed here";
                errorPage = "error/404";
                LOGGER.error("Error 405");
            }


        }
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageMessage", pageMessage);
        //do something like logging
        return "normal/error";
    }

    @Override
    public String getErrorPath() {

        return "normal/error";
    }
}
