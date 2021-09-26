package com.epam.esm.controllers;

import java.util.HashMap;
import java.util.Map;

public interface BaseController {

    String PREVIOUS_PAGE = "previousPage";
    String NEXT_PAGE = "nextPage";

    static Map<String, Object> getPaginationInfo(long pageQuantity, int pageNumber) {
        Map<String, Object> pages = new HashMap<>();
        Integer previousPage = null;
        Integer nextPage = null;
        if ((pageNumber - 1) > 0) {
            previousPage = pageNumber - 1;
        }
        if ((pageNumber + 1) <= pageQuantity) {
            nextPage = pageNumber + 1;
        }

        if (previousPage != null) {
            pages.put(PREVIOUS_PAGE, previousPage);
        }
        if (nextPage != null) {
            pages.put(NEXT_PAGE, nextPage);
        }
        return pages;
    }

}
